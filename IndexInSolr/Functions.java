package IndexInSolr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.google.gson.Gson;

public class Functions {
	
	private static void InsertIntoTable(String hashName, String newHashValue,
			String rangeName, String rangeValue, String columnName,
			String columnValue, String tableName,AmazonDynamoDBClient client) {
		try {
			Map<String, AttributeValue> item1 = new HashMap<String, AttributeValue>();
			item1.put(hashName, new AttributeValue().withS(newHashValue));
			item1.put(rangeName, new AttributeValue().withS(rangeValue));
			item1.put(columnName, new AttributeValue().withS(columnValue));

			PutItemRequest putItemRequest1 = new PutItemRequest()
					.withTableName(tableName).withItem(item1);
			PutItemResult result1 = client.putItem(putItemRequest1);
			 

		} catch (AmazonServiceException ase) {
			System.err.println("Create items failed.");
		}

	}// function close

	// function for deleting from dynamodb
	private static void deleteItem(String hashName, String hashValue,
			String rangeName, String rangeValue, String tableName,AmazonDynamoDBClient client) {

		try {
			Map<String, ExpectedAttributeValue> expectedValues = new HashMap<String, ExpectedAttributeValue>();
			HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
			key.put(hashName, new AttributeValue().withS(hashValue));
			key.put(rangeName, new AttributeValue().withS(rangeValue));
			DeleteItemRequest deleteItemRequest = new DeleteItemRequest()
					.withTableName(tableName).withKey(key)
					.withExpected(expectedValues);
			DeleteItemResult result = client.deleteItem(deleteItemRequest);
			// Check the response.
			 //System.out.println(rangeValue + " is deleted");

		} catch (AmazonServiceException ase) {
			System.err
					.println("Failed to get item after deletion " + tableName);
		}
	}// function close

	public static void scanDataFromParsedProductsAndIndexInSolr(String tableName,
			int limit, AmazonDynamoDBClient client, String solrUrl, String solrPort, Logger logger) {


		int clusterId = 1;
while(true)
{
		// List to return
	System.out.println("===============================================================================");
	System.out.println("                              SolrIndexing");
	System.out.println("===============================================================================");
		Map<String, Map<String, String>> mapToReturn = new HashMap<String, Map<String, String>>();
		Map<String, AttributeValue> lastEvaluatedKey = null;
    	HttpSolrServer server = new HttpSolrServer("http://" + solrUrl + ":"
				+ solrPort + "/solr");
        
        do {
			ScanRequest scanRequest = new ScanRequest().withTableName(tableName)
				.withLimit(limit)
				// give the list of attributes here in the below line of
				// code
				// .withAttributesToGet(Arrays.asList("attributename1","attribure_name2"
				// and so on...))
				.withExclusiveStartKey(lastEvaluatedKey);

		ScanResult result = client.scan(scanRequest);
		
		Map<String, String> temp = new HashMap<String, String>();
		
		for (Map<String, AttributeValue> item : result.getItems()) {
			String storeName = null;
			String url = null;
			String data = null;
			Map<String, AttributeValue> attributeList = item;
			
			for (Map.Entry<String, AttributeValue> item1 : attributeList
					.entrySet()) {
				String attributeName = item1.getKey();
				AttributeValue value = item1.getValue();
//				System.out.print(attributeName
//						+ " "
//						+ (value.getS() == null ? "" : "S=[" + value.getS()
//								+ "]"));
//				
				if (attributeName.equals("storeName")) {
					storeName = value.getS();
				}
				if (attributeName.equals("url")) {
					url = value.getS();
				}
				if (attributeName.equals("data")) {
					data = value.getS();
				}

			}// inner for close
			
			temp.put(data, storeName);
			deleteItem("storeName", storeName,"url", url, "ParsedProducts", client);
			InsertIntoTable("storeName", storeName,"url", url, "data", data, "ParsedProductsBackUp", client);
			
		} // outer for close
		
		for(Map.Entry<String, String> item : temp.entrySet())
		{
			try
			{
			String data = modifyData(item.getKey(),item.getValue());
			data = ModifyDataAgain(data);
			//logger.info("Indexing for "+clusterId+" started");
			indexingInSolr(Integer.toString(clusterId), data, server);
			if(clusterId % 4000 == 0)
				logger.info("Indexing for "+clusterId+" finished.");
			
			clusterId++;
			
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}// for close
			try {
				server.commit();
			} catch (SolrServerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			lastEvaluatedKey = result.getLastEvaluatedKey();
		} while (lastEvaluatedKey != null);

		// System.out.println("I am going from here.");

}// while close
	}
	private static String ModifyDataAgain(String json ) {
		/*-------------------------------------------------------------------------------------------------------------
		 *| Code for combining columns of Cluster Table Respective To Cluster_id                                      |                          
		 *| CombineHashMap Contains data for attributeGroupList and hashMap12 Contains data for attributes inside json|  
		 *-------------------------------------------------------------------------------------------------------------
		 */

		Gson g = new Gson();

		String solrData; // Variable For Storing SolrData
		// Map jsonData = new HashMap();
		Map jsonData = g.fromJson(json, Map.class);
		// System.out.println("......." + jsonData);
		int counts = 0;

		// here we are getting storeNumber

		if (jsonData.containsKey("storeName")) {
			Map storeNameData = new HashMap();
			storeNameData = (Map) jsonData.get("storeName");
			if (!storeNameData.isEmpty()) {
				Iterator entries = storeNameData.entrySet().iterator();
				while (entries.hasNext()) {
					Entry thisEntry = (Entry) entries.next();
					Object key = thisEntry.getKey();
					Object value = thisEntry.getValue();
					counts++;
					// ...
				}
				// for (Map.Entry item : storeNameData.entrySet()) {
				// counts++;
				// }
			}
		}
		//
		// Map AttributeMap = new HashMap();
		// if (jsonData.containsKey("attributes")) {
		// AttributeMap = g.fromJson(jsonData.get("attributes").toString(),
		// Map.class);
		// }

		/*-----------------------------------------------------------------------------
		 *|Declaring varibles  for Average ,Max and Min Prices                         | 
		 * -----------------------------------------------------------------------------
		 */
		ArrayList<Float> priceStore = new ArrayList<Float>();
		float min_price = 0;
		float max_price = 0;
		float avg_price = 0;

		String color = null;
		String brand = null;
		String type = null;

		/*
		 * ----------------------------------------------------------------------
		 * ---------- | Code for finding min_price, max_price & avg_price |
		 * ------
		 * ----------------------------------------------------------------
		 * ----------
		 */
		
		Map StoreNameMap = new HashMap();
		if (jsonData.containsKey("storeName")) {
			StoreNameMap = (Map) jsonData.get("storeName");
			if (!StoreNameMap.isEmpty()) {
				Map tempMap = new HashMap();
				Iterator entries = StoreNameMap.entrySet().iterator();
				while (entries.hasNext()) {
					Entry thisEntry = (Entry) entries.next();
					Object key = thisEntry.getKey();
					Object value = thisEntry.getValue();
					// for (Map.Entry item : StoreNameMap.entrySet()) {
					tempMap = (Map) value;
					if (!tempMap.isEmpty()) {
						Iterator entries1 = StoreNameMap.entrySet().iterator();
						while (entries1.hasNext()) {
							Entry thisEntry1 = (Entry) entries1.next();
							Object key1 = thisEntry1.getKey();
							Object value1 = thisEntry1.getValue();
							// for (Map.Entry item2 : StoreNameMap.entrySet()) {
							if (key1.toString().equalsIgnoreCase("price")) {
								priceStore.add(Float.parseFloat(value1
										.toString()));
							}
							if (key1.toString().equalsIgnoreCase("color")) {
								color = value1.toString();
							}
							if (key1.toString().equalsIgnoreCase("type")) {
								type = value1.toString();
							}
							if (key1.toString().equalsIgnoreCase("brand")) {
								brand = value1.toString();
							}
						}
					}
				}
			}
		}

		if (priceStore.size() > 1) {
			max_price = Collections.max(priceStore);
			min_price = Collections.min(priceStore);
		} else if (priceStore.size() == 1) {
			max_price = priceStore.get(0);
			min_price = priceStore.get(0);
		}

		// code for avg price
		int tempSum = 0;
		int count = 0;
		for (; count < priceStore.size(); count++) {
			tempSum += priceStore.get(count);
		}// for close
		if (count > 0)
			avg_price = (float) tempSum / count;

		/*-----------------------------------------------------------------------------
		 *| Some additional fields For Solrdata                                                                           |                                                     
		 * ----------------------------------------------------------------------------
		 */
		if (avg_price != 0)
			jsonData.put("avg_price", avg_price);
		if (max_price != 0)
			jsonData.put("max_price", max_price);
		if (min_price != 0)
			jsonData.put("min_price", min_price);
		if (counts != 0)
			jsonData.put("counts", Integer.toString(counts));
		if (avg_price != 0)
			jsonData.put("price", avg_price);

		if (color != null)
			jsonData.put("color", color);
		if (brand != null)
			jsonData.put("brand", brand);
		if (type != null)
			jsonData.put("type", type);

		solrData = g.toJson(jsonData, Map.class);
		// System.out.println(json);

		return solrData;
	}
	public static String modifyData(String data, String storeName)
	{

		String newData = null;
		Gson g = new Gson();
		Map mainJson = new HashMap();
		mainJson = g.fromJson(data, Map.class);

		Map temp1 = new HashMap();
		Iterator entries = mainJson.entrySet().iterator();
		while (entries.hasNext()) {
			Entry thisEntry = (Entry) entries.next();
			Object key = thisEntry.getKey();
			Object value = thisEntry.getValue();
			if (!key.toString().equals("attributes")) {
				temp1.put(key, value);
			}
		}

		// for (Map.Entry item : mainJson.entrySet()) {
		// if (!(item.getKey().toString().equals("attributes"))) {
		// temp1.put(item.getKey(), item.getValue());
		// }// if close
		// }// for close

		Map temp2 = new HashMap();
		temp2.put(storeName, temp1); // storeName vrs key-value pair

		mainJson.put("storeName", temp2); // "storeName" vrs storeName
											// information as storeName as key
											// and values in key-value pair
		newData = g.toJson(mainJson); // making a new String

		return newData;
	
	}
	public static void indexingInSolr(String id, String value,
			HttpSolrServer server) {
		//System.out.println(value);
		Gson g = new Gson();
		Map<Object, Object> solrData = new HashMap<Object, Object>();
		solrData = g.fromJson(value, Map.class);

		// counter for doc information

		SolrInputDocument doc = new SolrInputDocument();

		doc.addField("cluster_id", id);

		for (Map.Entry<Object, Object> item2 : solrData.entrySet()) {
			if (item2.getKey().toString().equalsIgnoreCase("image")) {
				String image_img[] = item2.getValue().toString().split("~!~");
				for (int img = 0; img < image_img.length; img++) {
					image_img[img] = image_img[img].replace("\"", "");
					doc.addField("image", image_img[img]);

				}
						
					
						if (item2.getKey().toString().equalsIgnoreCase("product_title")) {
							doc.addField("product_title", item2.getValue());
			
						}
			
						if (item2.getKey().toString().equalsIgnoreCase("avg_price")) {
							doc.addField("avg_price", item2.getValue());
			
						}
						if (item2.getKey().toString().equalsIgnoreCase("min_price")) {
							doc.addField("min_price", item2.getValue());
			
						}
						if (item2.getKey().toString().equalsIgnoreCase("max_price")) {
							doc.addField("max_price", item2.getValue());
			
						}
				
						if (item2.getKey().toString().equalsIgnoreCase("counts")) {
							doc.addField("counts", item2.getValue());
			
						}
			
						if (item2.getKey().toString().equalsIgnoreCase("url")) {
							doc.addField("url", item2.getValue());
			
						}
						if (item2.getKey().toString().equalsIgnoreCase("description")) {
							doc.addField("description", item2.getValue());
			
						}
						if (item2.getKey().toString().equalsIgnoreCase("storeName")) {
							//System.out.println(item2.getValue());
							String temp = g.toJson(item2.getValue());
							doc.addField("storeName", temp);
						}
						if (item2.getKey().toString().equalsIgnoreCase("attributes")) {
							String temp = g.toJson(item2.getValue());
							doc.addField("attributes", temp);
			
						}
						if (item2.getKey().toString().equalsIgnoreCase("color")) {
							doc.addField("color", item2.getValue());
						}
						if (item2.getKey().toString().equalsIgnoreCase("brand")) {
							doc.addField("brand", item2.getValue());
						}
						if (item2.getKey().toString().equalsIgnoreCase("type")) {
							doc.addField("type", item2.getValue());
						}
						if (item2.getKey().toString().equalsIgnoreCase("price")) {
							doc.addField("price", item2.getValue());
						}
						if (item2.getKey().toString().equalsIgnoreCase("shipping_charges")) {
							doc.addField("shipping_charges", item2.getValue());
						}
						if (item2.getKey().toString().equalsIgnoreCase("shipping_time")) {
							doc.addField("shipping_time", item2.getValue());
						}
						if (item2.getKey().toString().equalsIgnoreCase("rating")) {
							doc.addField("rating", item2.getValue());
						}
						
			}// if closes
		}

		try {
			server.add(doc);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}// function is closed
}
