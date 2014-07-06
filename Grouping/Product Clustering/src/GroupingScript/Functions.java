package GroupingScript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import GroupingScript.RedWrite;
import GroupingScript.SortMap.ValueComparator;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.google.gson.Gson;

public class Functions {

	public static void main(String[] args) {
		String jsp = ModifyData("{\"price\":\" 1250\",\"product_title\":\"                    Kiara Hand Bag            \",\"url\":\"http://www.flipkart.com/kiara-hand-bag/p/itmdgzq8ayrcfjfs?pid\\u003dHMBDGZQ6PEUEGEGY\\u0026ref\\u003da6a894c7-d50b-42ab-8d25-acd7945f0065\",\"storeName\":{\"www.flipkart.com\":{\"price\":\" 1250\",\"product_title\":\"                    Kiara Hand Bag            \",\"url\":\"http://www.flipkart.com/kiara-hand-bag/p/itmdgzq8ayrcfjfs?pid\\u003dHMBDGZQ6PEUEGEGY\\u0026ref\\u003da6a894c7-d50b-42ab-8d25-acd7945f0065\"}}}");
		System.out.println(jsp);
	}

	// ------------------------------------------------------------------------------------------------
	// Function for Inserting data in cluster table in dynmodb
	// ------------------------------------------------------------------------------------------------
	private static void EntryInSolrDataInHbase(String tableName, String url, String data, String storeName, String masterIp) {
		// code for checking that there is and entry with this key if then add
		// it

		Map<String, String> solrDataMap = new HashMap<String, String>();

		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp + ":60000");

		try {
			HTable table = new HTable(conf, tableName);
			Scan s = new Scan();
			s.setBatch(100);

			ResultScanner ss = table.getScanner(s);
			for (Result r : ss) {

				for (KeyValue kv : r.raw()) {
					String qualifier_1 = new String(kv.getQualifier());
					String qualifier_value = new String(kv.getValue());

					// add data in solrDataMap
					solrDataMap.put(qualifier_1, qualifier_value);
				} // for close
					// in temp row is url and value_list is data
			}

			// System.out.println("---------------------");
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// code for inserting in solr_data table if the key is already inserted.
		if (!solrDataMap.isEmpty()) {

			String count = solrDataMap.get("count");
			count = "" + (Integer.parseInt(count) + 1);

			// in new Data we are getting the combined data from the matched
			// json

			/**
			 * Needs to be upgraded
			 */
			String newData = CombineTwoHashMaps(data,
					solrDataMap.get("attribute"));

			try {

				HTable table = new HTable(conf, tableName);
				List<Put> listOfPut = new ArrayList<Put>();

				for (Map.Entry<String, String> m : solrDataMap.entrySet()) {
					if (m.getKey().startsWith("storeName")) {
						if (!m.getValue().equals(storeName)) {
							Put put1 = new Put(Bytes.toBytes(url));
							put1.add(Bytes.toBytes("cf"),
									Bytes.toBytes("storeName" + count),
									Bytes.toBytes(storeName));
							listOfPut.add(put1); // add in list

							Put put3 = new Put(Bytes.toBytes(url));

							put3.add(Bytes.toBytes("cf"),
									Bytes.toBytes("count"),
									Bytes.toBytes(count));
							listOfPut.add(put3); // add in list
						}
					}
				}

				Put put2 = new Put(Bytes.toBytes(url));

				put2.add(Bytes.toBytes("cf"), Bytes.toBytes("attribute"),
						Bytes.toBytes(newData));
				listOfPut.add(put2); // add in list

				// put the items in the solr_data table
				table.put(listOfPut);

				table.close();
				// System.out.println("Record Added to hbase");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}// if close
		else {

			try {
				HTable table = new HTable(conf, tableName);
				List<Put> listOfPut = new ArrayList<Put>();

				Put put1 = new Put(Bytes.toBytes(url));
				put1.add(Bytes.toBytes("cf"), Bytes.toBytes("storeName1"),
						Bytes.toBytes(storeName));
				listOfPut.add(put1);

				Put put2 = new Put(Bytes.toBytes(url));

				put2.add(Bytes.toBytes("cf"), Bytes.toBytes("attribute"),
						Bytes.toBytes(data));
				listOfPut.add(put2);

				Put put3 = new Put(Bytes.toBytes(url));

				put3.add(Bytes.toBytes("cf"), Bytes.toBytes("count"),
						Bytes.toBytes("1"));
				listOfPut.add(put3);

				// put the items in the solr_data table
				table.put(listOfPut);

				table.close();
				// System.out.println("Record Added to hbase");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}// else close
	}// fn close

	// ------------------------------------------------------------------------------------------------
	// Function for Inserting data in cluster table in dynmodb
	// ------------------------------------------------------------------------------------------------
	public static void EntryInClusterTableInDynamodb(String tableName,
			String url, String data, String storeName,
			AmazonDynamoDBClient client) {

		Map<String, String> mapOfProductsInHash = new HashMap<String, String>();
		Map<String, AttributeValue> lastEvaluatedKey = null;

		do {

			Condition hashKeyCondition = new Condition()
					.withComparisonOperator(ComparisonOperator.EQ.toString())
					.withAttributeValueList(new AttributeValue().withS(url));

			Map<String, Condition> keyConditions = new HashMap<String, Condition>();
			keyConditions.put("url", hashKeyCondition);

			QueryRequest queryRequest = new QueryRequest()
					.withTableName(tableName).withKeyConditions(keyConditions)
					.withLimit(50)
					.withAttributesToGet(Arrays.asList("count", "attribute"))
					.withExclusiveStartKey(lastEvaluatedKey);

			QueryResult result = client.query(queryRequest);
			for (Map<String, AttributeValue> item : result.getItems()) {

				Map<String, AttributeValue> attributeList = item;

				for (Map.Entry<String, AttributeValue> item1 : attributeList
						.entrySet()) {
					String attributeName = item1.getKey();
					AttributeValue value = item1.getValue();

					mapOfProductsInHash.put(attributeName, value.getS());
				}// inner for close

			}

			lastEvaluatedKey = result.getLastEvaluatedKey();
		} while (lastEvaluatedKey != null);
		// do-while close

		if (!mapOfProductsInHash.isEmpty()) {
			// call function to combine attribute from dynamodb and

			String newjson = CombineTwoHashMaps(data,
					mapOfProductsInHash.get("attribute"));

			String count = mapOfProductsInHash.get("count");
			count = "" + ((Integer.parseInt(count)) + 1);

			try {
				Map<String, AttributeValueUpdate> updateItems = new HashMap<String, AttributeValueUpdate>();

				HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
				key.put("url", new AttributeValue().withS(url));
				key.put("range", new AttributeValue().withS("cf"));

				// Add two new authors to the list.
				updateItems.put("storeName" + count, new AttributeValueUpdate()
						.withValue(new AttributeValue().withS(storeName)));

				// Add a new attribute.
				updateItems.put("count", new AttributeValueUpdate()
						.withValue(new AttributeValue().withS(count)));

				updateItems.put("attribute", new AttributeValueUpdate()
						.withValue(new AttributeValue().withS(newjson)));

				UpdateItemRequest updateItemRequest = new UpdateItemRequest()
						.withTableName(tableName).withKey(key)
						.withAttributeUpdates(updateItems);

				UpdateItemResult result = client.updateItem(updateItemRequest);

			} catch (AmazonServiceException ase) {
				System.out.println(ase);
				System.err.println("Failed to update multiple attributes in "
						+ tableName);
			}
		}// if close
		else {
			// just add it
			try {
				Map<String, AttributeValue> putMap = new HashMap<String, AttributeValue>();
				putMap.put("url", new AttributeValue().withS(url));
				putMap.put("range", new AttributeValue().withS("cf"));

				putMap.put("storeName1", new AttributeValue().withS(storeName));
				putMap.put("count", new AttributeValue().withS("1"));
				putMap.put("attribute", new AttributeValue().withS(data));

				// create put object

				PutItemRequest putItemRequest = new PutItemRequest()
						.withTableName(tableName).withItem(putMap);

				PutItemResult result = client.putItem(putItemRequest);

			}// try close
			catch (AmazonServiceException ase) {

			}// catch close

		} // else close

	}

	// --------------------------------------------------------------------------
	// Function for CombineTwoHashMaps
	// --------------------------------------------------------------------------
	private static String CombineTwoHashMaps(String data1, String data2) {

		Gson g = new Gson();

		Map temp1 = new HashMap();
		Map<String, String> temp2 = new HashMap<String, String>();
		Map<String, String> temp3 = new HashMap<String, String>();

		temp1 = g.fromJson(data1, Map.class);
		temp2 = g.fromJson(data2, Map.class);

		if (temp1.containsKey("attributes")) {
			Map temp4 = new HashMap();
			temp4 = g.fromJson(temp1.get("attributes").toString(), Map.class);

			if (temp2.containsKey("attributes")) {
				Map temp5 = new HashMap();
				temp5 = g.fromJson(temp2.get("attributes").toString(),
						Map.class);
				temp4.putAll(temp5);
			}

			String t = g.toJson(temp4);

			temp2.put("attributes", t);
		}

		temp3.putAll(temp1);
		temp3.putAll(temp2);

		String returnString = g.toJson(temp3);
		return returnString;
	} // fn close

	// ------------------------------------------------------------------------------------------------
	// Function for emptyParsedDataTableInHbase here |
	// ------------------------------------------------------------------------------------------------
	public static void emptyParsedDataTableInHbase(String tableName,
			String masterIp, Logger logger, AmazonDynamoDBClient client)
			throws IOException {
		Configuration conf = new Configuration();

		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp + ":60000");
		HBaseAdmin admin = new HBaseAdmin(conf);
		if (admin.tableExists(tableName)) {
			System.out.println("table already exists!");
			admin.disableTable(tableName);
			admin.deleteTable(tableName);
			System.out.println("deleted table " + tableName + " ok.");
		} // if close

		HTableDescriptor tableDesc = new HTableDescriptor(tableName);
		tableDesc.addFamily(new HColumnDescriptor("cf"));
		admin.createTable(tableDesc);
		System.out.println("created table " + tableName + " ok.");
		admin.close();
	}

	// ------------------------------------------------------------------------------------------------
	// Function for CopyDataFromHashContentToDynamodbHashContent here |
	// ------------------------------------------------------------------------------------------------
	public static void CopyDataFromHashContentToDynamodbHashContent(
			String tableName, String masterIp2, Logger logger,
			AmazonDynamoDBClient client) {
		// fetch all the data from HashContent Hbase

		Map<String, Map<String, String>> mapForHashContent = new HashMap<String, Map<String, String>>();

		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp2);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp2 + ":60000");

		try {
			HTable table = new HTable(conf, tableName);
			Scan s = new Scan();
			// s.setBatch(100);

			ResultScanner ss = table.getScanner(s);
			for (Result r : ss) {
				String row = null;
				String qualifierName = null;
				String qualifierValue = null;
				Map<String, String> temp = new HashMap<String, String>();

				for (KeyValue kv : r.raw()) {
					row = new String(kv.getRow());
					// System.out.println(new String(kv.getQualifier())
					// + "-------" + new String(kv.getValue()));
					qualifierName = new String(kv.getQualifier());
					qualifierValue = new String(kv.getValue());

					if(qualifierName != null && qualifierValue != null)
					temp.put(qualifierName, qualifierValue);
				} // for close
				if(row != null && !temp.isEmpty())
				mapForHashContent.put(row, temp);
			} // outer for close

			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}// catch close

		// send mapForHashContent of hbase into HashContent Dynamodb HashContent
		if(!mapForHashContent.isEmpty())
		UpdateHashContentInDynamodb("HashContent", mapForHashContent, logger,
				client);
	}

	// ------------------------------------------------------------------------------------------------
	// Function for CopyDataFromHashCountToDynamodbHashCount here |
	// ------------------------------------------------------------------------------------------------
	public static void CopyDataFromHashCountToDynamodbHashCount(
			String tableName, String masterIp2, AmazonDynamoDBClient client,
			Logger logger) {
		// fetch all the data from hashCount
		Map<String, String> mapOfHashCount = new HashMap<String, String>();

		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp2);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp2 + ":60000");

		try {

			HTable table = new HTable(conf, tableName);
			Scan s = new Scan();

			ResultScanner ss = table.getScanner(s);
			for (Result r : ss) {
				String row = null;
				String count = null;
				Map<String,String> temp = new HashMap<String,String>();

				for (KeyValue kv : r.raw()) {
					row = new String(kv.getRow());
					// System.out.println(new String(kv.getQualifier())
					// + "-------" + new String(kv.getValue()));
					//String qualifier = new String(kv.getFamily());
					
					if (new String(kv.getQualifier()).contains("count")) {
						count = new String(kv.getValue());
					}
//					if(row != null && count != null)
//					temp.put(qualifier, count);
//					
					// in temp row is url and value_list is data
					if(count != null)
					mapOfHashCount.put(row, count);

				}
				

				// System.out.println("---------------------");

			}// for close
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// pass this mapOfHashCount into the following function for inserting
		// data in DynamoDb

		UpdateHashCountInDynamodb("HashCount", mapOfHashCount, logger, client);

	}// function close

	// ------------------------------------------------------------------------------------------------
	// in this function Matching of two json data will occur |
	// ------------------------------------------------------------------------------------------------

	// public static Boolean matchAndGroupData(String key, String data,
	// String tableName, String storeName, String masterIp2,Logger
	// logger,AmazonDynamoDBClient client) {
	// // using this key we will get all the json from HashContent table and
	// // then we will loop for it two match
	// // if any of the match occur then return true otherwise false
	//
	// Boolean status = false;
	//
	// logger.info("Comparing data in The HashContent table....");
	// try {
	// status = MatchandGroupDataHelperFunction(key, data, masterIp2,
	// storeName, logger,client);
	// } catch (IOException e) {
	// logger.info(e.toString());
	//
	// }
	// logger.info("Comparing data in The HashContent table is done.");
	//
	// logger.info("Making entry in AmpleCluster Table...");
	// // EntryInClusterTableInDynamodb("AmpleCluster", url, data, storeName);
	// logger.info("entry in AmpleCluster Table is finished.");
	//
	// return status;
	// }

	// ------------------------------------------------------------------------------------------------
	// Matching of two hashMaps will start here |
	// ------------------------------------------------------------------------------------------------

	// private static Boolean MatchandGroupDataHelperFunction(String key,
	//
	// String data, String masterIp2, String storeName,Logger
	// logger,AmazonDynamoDBClient client) throws IOException {
	// ArrayList<String> storeData = new ArrayList<String>();
	//
	// // make comparision between data and in HashContent table known by key
	// Configuration conf = new Configuration();
	//
	// conf = HBaseConfiguration.create();
	// conf.set("hbase.zookeeper.quorum", masterIp2);
	// conf.set("hbase.zookeeper.property.clientPort", "2181");
	// conf.set("hbase.master", masterIp2 + ":60000");
	// HTable table = new HTable(conf, "HashContent");
	// Get get = new Get(key.getBytes());
	//
	// Result rs = table.get(get);
	// for (KeyValue kv : rs.raw()) {
	//
	// // System.out.print(new String(kv.getRow()) + " " );
	// if (!(new String(kv.getQualifier())).equals("count"))
	// storeData.add(new String(kv.getValue()));
	//
	// }
	// table.close();
	//
	// // code for comparing products
	// Boolean status = false;
	// for (int i = 0; i < storeData.size(); i++) {
	// logger.info("MatchTwoJsonString function is called...");
	// String checkMatchData = MatchTwoJsonString(data, storeData.get(i));
	// logger.info("MatchTwoJsonString function is finished.");
	//
	// if (!checkMatchData.equals("notMatch")) {
	// logger.info("updating solr_data in hbase...");
	// EntryInSolrDataInHbase("solr_data", checkMatchData, data,
	// storeName, masterIp2);
	// logger.info("updating solr_data in hbase is done.");
	//
	// logger.info("updating AmpleCluster table in Dynamodb....");
	// EntryInClusterTableInDynamodb("AmpleCluster", checkMatchData,
	// data, storeName,client);
	// logger.info("updating AmpleCluster table in Dynamodb is finished.");
	//
	// status = true;
	// break;
	// }
	//
	// }// for close
	//
	// return status;
	// } // fn close

	// ------------------------------------------------------------------------------------------------
	// function for Updating content in HashContent
	// ------------------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	private static Boolean MatchTwoJsonString(String data, String clusterData,
			String storeName, String masterIp, String clusterId,String range, Logger logger)
			throws IOException {
		// if match occures then make entry in Dynamodb AmpleCluster Table

		Boolean flag = false, storeNameFlag = true;
		// variable for denoting match in maps
		// List of attributes for which there is no match
		ArrayList<String> listOfNotMatchedAttributes = new ArrayList<String>();

		/*
		 * Condition for matching title fot both maps
		 */

		// System.out.println(data);
		Map parent1 = new HashMap();
		Map parent2 = new HashMap();

		Gson g = new Gson();
		// convert json string into Map
		parent1 = g.fromJson(data, Map.class);
		parent2 = g.fromJson(clusterData, Map.class);

		// System.out.println("  " + parent1);
		// System.out.println("   " + parent2);

		if (parent1.containsKey("product_title")
				&& parent2.containsKey("product_title")) {
			if (parent1.get("product_title").equals(
					parent2.get("product_title"))) {
				flag = true;
				System.out.println("Title are Matched");
				if (parent2.containsKey("storeName")) {
					// String val = new
					// String(parent2.get("storeName").toString());
					Map json = (Map) parent2.get("storeName");

					Iterator entries = json.entrySet().iterator();
					while (entries.hasNext()) {
						Entry thisEntry = (Entry) entries.next();
						Object key = thisEntry.getKey();
						Object value = thisEntry.getValue();

						if (key.toString().equals(storeName)) {
							storeNameFlag = false;
							System.out
									.println("Store Name are Matched Rejecting the product");
							break;

						}
					}// while close
						// for (Map.Entry item : json.entrySet()) {
						// if (item.getKey().toString().equals(storeName)) {
						// storeNameFlag = false;
						// System.out
						// .println("Store Name are Matched Rejecting the product");
						// break;
						// }
						// }// for class
						//
				}
			} // if close
		} // main if close

		if (!flag && storeNameFlag) {
			if (parent1.containsKey("attributes")
					&& parent2.containsKey("attributes")) {
				Map m1 = new HashMap();
				Map m2 = new HashMap();

				m1 = (Map) g.fromJson(parent1.get("attributes").toString(),
						Map.class);
				m2 = (Map) g.fromJson(parent2.get("attributes").toString(),
						Map.class);
				// m1 = (Map) parent1.get("attributes");
				// m2 = (Map) parent2.get("attributes");
				int a = m1.size();
				int b = m2.size();

				ArrayList<String> keySet = new ArrayList<String>();
				if (a >= b) {
					Iterator entries = m1.entrySet().iterator();
					while (entries.hasNext()) {
						Entry thisEntry = (Entry) entries.next();
						Object key = thisEntry.getKey();
						Object value = thisEntry.getValue();
						if (m2.containsKey(key)) {
							keySet.add(key.toString());

						}// inner if close
						else
							listOfNotMatchedAttributes.add(key.toString());

					}// while close
						// for (Map.Entry item : m1.entrySet()) {
						// if (m2.containsKey(item.getKey())) {
						// keySet.add(item.getKey().toString());
						//
						// }// inner if close
						// else
						// listOfNotMatchedAttributes.add(item.getKey()
						// .toString());
						// }// for close
				}// if close
				else {
					Iterator entries = m2.entrySet().iterator();
					while (entries.hasNext()) {
						Entry thisEntry = (Entry) entries.next();
						Object key = thisEntry.getKey();
						Object value = thisEntry.getValue();
						if (m1.containsKey(key)) {
							keySet.add(key.toString());
						}// inner if close
						else
							listOfNotMatchedAttributes.add(key.toString());
					}
					// for (Map.Entry item : m2.entrySet()) {
					// if (m1.containsKey(item.getKey())) {
					// keySet.add(item.getKey().toString());
					// }// inner if close
					// else
					// listOfNotMatchedAttributes.add(item.getKey()
					// .toString());
					//
					// }// for close

				}// else close

				// Code for checking that the values match for the matched keys
				// or not
				/**
				 * This code responsible for % matching of the products
				 * currently
				 */
				Boolean flag2 = true;
				if (keySet.size() > 0) {
					for (int i = 0; i < keySet.size(); i++) {
						if (!(m1.get(keySet.get(i))).toString()
								.equalsIgnoreCase(
										(String) m2.get(keySet.get(i)))) {
							flag2 = false;
							break;
						}
					}// for close
				} // if close
				else {
					flag2 = false;
				}// else close

				// Assign value of flag2 into flag
				System.out.println("the status of the attribute matching is :"
						+ flag2);
				flag = flag2;

			}// Second main if close
		}

		// ---------------------------------------------------------------------------------
		// Function for updating data in AmpleCluster in hbase
		// ---------------------------------------------------------------------------------

		if (flag && storeNameFlag) {

			if (listOfNotMatchedAttributes.size() != 0) {

				System.out.println("Not Matched attribute : "
						+ listOfNotMatchedAttributes);
				// //Getting new Cluster Number
				// String clusterNo = Functions
				// .FindClusterNumberUsingAmpleClusterInHbase(
				// "AmpleCluster", logger, masterIp);
				// insert into HashCount table
				for (int i = 0; i < listOfNotMatchedAttributes.size(); i++) {

					ArrayList<String> listclusterNo = new ArrayList<String>();
					listclusterNo.add(clusterId);
					Functions.InsertInHashContentInHbaseFirstTime(
							listOfNotMatchedAttributes.get(i), listclusterNo,
							"HashContent", range, masterIp, logger);
				}// for close
			}// if close
				// code for making entry in storeName

			Map newStoreData = new HashMap();
			Iterator entries = parent1.entrySet().iterator();
			while (entries.hasNext()) {
				Entry thisEntry = (Entry) entries.next();
				Object key = thisEntry.getKey();
				Object value = thisEntry.getValue();
				if (!key.toString().equals("attributes")) {
					newStoreData.put(key, value);
				}
			}
			// for (Map.Entry item : parent1.entrySet()) {
			// if (!item.getKey().toString().equals("attributes")) {
			// newStoreData.put(item.getKey(), item.getValue());
			// }
			// }// for close

			Map bufferStoreData = new HashMap();
			// make entry in storeData key as

			Map storeData = new HashMap();
			if (parent2.containsKey("storeName")) {
				storeData = (Map) parent2.get("storeName");
			}// if close

			// here we are adding new store in storeName key in json
			if (!storeData.isEmpty() && !newStoreData.isEmpty()) {
				bufferStoreData.put(storeName, newStoreData);
				storeData.putAll(bufferStoreData);
				parent2.put("storeName", storeData);

			}
			// merging of attributes
			if (parent1.containsKey("attributes")
					&& parent2.containsKey("attributes")) {
				Map m1 = new HashMap();
				Map m2 = new HashMap();
				m1 = (Map) g.fromJson(parent1.get("attributes").toString(),
						Map.class);
				m2 = (Map) g.fromJson(parent2.get("attributes").toString(),
						Map.class);
				// m1 = (Map) parent1.get("attributes");
				// m2 = (Map) parent2.get("attributes");

				m2.putAll(m1);
				System.out.println(m2);
				String temp = g.toJson(m2);
				parent2.put("attributes", temp); // merging attributes
			}// if close

			String newData = g.toJson(parent2);

			try {
				Configuration conf = new Configuration();
				conf = HBaseConfiguration.create();
				conf.set("hbase.zookeeper.quorum", masterIp);
				conf.set("hbase.zookeeper.property.clientPort", "2181");
				conf.set("hbase.master", masterIp + ":60000");

				HTable table = new HTable(conf, "AmpleCluster");
				Put put = new Put(Bytes.toBytes(clusterId));
				put.add(Bytes.toBytes(range), Bytes.toBytes("data"),
						Bytes.toBytes(newData));

				table.put(put);
				table.close();

				logger.info("insertion in " + "AmpleCluster" + " is finished.");
				// System.out.println("Record Added to hbase");
			} catch (IOException e) {
				e.printStackTrace();
				logger.info(e.toString());
			}

		} // if close

		return flag;
	}// fn close

	private static String GetCountFromHashContentInhbase(String tableName,
			String rowKey,String columnFamily, String masterIp2) throws IOException {
		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp2);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp2 + ":60000");
		HTable table = new HTable(conf, tableName);
		String count = null;

		Get get = new Get(Bytes.toBytes(rowKey));
		get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("count"));
		Result rs = table.get(get);
		for (KeyValue kv : rs.raw()) {
			// System.out.print(new String(kv.getRow()) + " ");
			// System.out.print(new String(kv.getFamily()) + ":");
			// System.out.print(new String(kv.getQualifier()) + " ");
			// System.out.print(kv.getTimestamp() + " ");
			count = new String(kv.getValue());
		}
		table.close();

		return count;

	}

	// ------------------------------------------------------------------------------------------------
	// function for Updating content in HashContent
	// ------------------------------------------------------------------------------------------------
	public static void UpdateHashContent(String rowKey, String tableName,
			String columnFamily, String masterIp2, String jsonStringOfProduct,
			Logger logger) {
		// code for getting data of a rowKey from HashContent and then update
		// the data in it.
		// get one record of a key

		Configuration conf = new Configuration();
		// code for fetching data in this key
		String count = null;

		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp2);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp2 + ":60000");

		HTable table = null;
		try {
			table = new HTable(conf, tableName);
		} catch (IOException e) {
			logger.info(e.toString());
		}

		// -------------------------------------------------------------------------------------------
		Get get = new Get(rowKey.getBytes());

		Result rs = null;
		try {
			rs = table.get(get);
		} catch (IOException e) {
			logger.info(e.toString());

		}

		Map<String, String> qualifierValueMap = new HashMap<String, String>();

		for (KeyValue kv : rs.raw()) {

			String qualifierName = new String(kv.getQualifier());
			String qualifierValue = new String(kv.getValue());

			// add the above vars into the qualifierValueMap
			qualifierValueMap.put(qualifierName, qualifierValue);
		}

		try {
			table.close();
		} catch (IOException e) {
			logger.info(e.toString());

		}

		// --------------------------------------------------------------------------------------------

		// update qualifierValueMap and then update it in HashContent table
		String countGet = qualifierValueMap.get("count");
		countGet = "" + (Integer.parseInt(countGet) + 1); // here count is
															// incremented
		// this are two things to update
		qualifierValueMap.put(countGet, jsonStringOfProduct);
		qualifierValueMap.put("count", countGet);

		// code for updating data in HashContent table
		List<Put> listOfPut = new ArrayList<Put>();

		for (Map.Entry<String, String> item : qualifierValueMap.entrySet()) {

			Put put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(item.getKey()),
					Bytes.toBytes(item.getValue()));
			listOfPut.add(put);

		}// for close

		try {
			table.put(listOfPut);
		} catch (IOException e) {
			logger.info(e.toString());
		}
		try {
			table.close();
		} catch (IOException e) {
			logger.info(e.toString());
		}

	} // function close

	// ------------------------------------------------------------------------------------------------
	// function for Updating content in HashCount
	// ------------------------------------------------------------------------------------------------
	public static void UpdateHashCount(String rowKey, String tableName,
			String columnFamily, String columnName, String masterIp2,
			Logger logger) {

		Configuration conf = new Configuration();
		// here we will update data in key to data+1
		// code for fetching data in this key
		String count = null;

		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp2);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp2 + ":60000");
		HTable table = null;
		try {
			table = new HTable(conf, tableName);
		} catch (IOException e) {
			logger.info(e.toString());
		}
		Get get = new Get(rowKey.getBytes());
		get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName));
		Result rs = null;
		try {
			rs = table.get(get);
		} catch (IOException e) {
			logger.info(e.toString());

		}
		for (KeyValue kv : rs.raw()) {
			// System.out.print(new String(kv.getRow()) + " " );
			// System.out.print(new String(kv.getFamily()) + ":" );
			// System.out.print(new String(kv.getQualifier()) + " " );
			// System.out.print(kv.getTimestamp() + " " );
			count = new String(kv.getValue());
		}

		// code for inserting data in this key
		// increment count by 1
		int countIncrement = Integer.parseInt(count);

		countIncrement = countIncrement + 1;
		try {
			Put put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName),
					Bytes.toBytes("" + countIncrement));
			table.put(put);
			table.close();
			// System.out.println("Record Added to hbase");
		} catch (IOException e) {
			e.printStackTrace();
		}

	} // function close

	// ------------------------------------------------------------------------------------------------
	// function for Inserting data from Dynamodb to Hbase
	// ------------------------------------------------------------------------------------------------
	public static void InsertInHashContentInHbase(String rowKey,

	ArrayList<String> listOfProductsInHash, String tableName,
			String columnFamily, String masterIp2, Logger logger)
			throws IOException {

		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp2);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp2 + ":60000");

		// here get count for the above key and if the count is not zero then
//		String count = GetCountFromHashContentInhbase("HashCount", rowKey,columnFamily,masterIp2);

		List<Put> listOfPut = new ArrayList<Put>();

		try {
			HTable table = new HTable(conf, tableName);

			for (int i = 0; i < listOfProductsInHash.size(); i++) {

				Put put = new Put(Bytes.toBytes(rowKey));
				put.add(Bytes.toBytes(columnFamily),
						Bytes.toBytes(Integer.toString((i + 1))),
						Bytes.toBytes(listOfProductsInHash.get(i)));
				listOfPut.add(put);

				// System.out.println("Record Added to hbase");

			}// for class

			// and at last you have to add count in rowKey to tell that how
			// many
			// column does a rowKey has

			Put put1 = new Put(Bytes.toBytes(rowKey));
			put1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("count"), Bytes
					.toBytes(Integer.toString(listOfProductsInHash.size())));

			listOfPut.add(put1);

			table.put(listOfPut);
			table.close();

		} // try close
		catch (IOException e) {
			logger.info(e.toString());
		}// catch close
	}

	public static void InsertInHashContentInHbaseFirstTime(String rowKey,

	ArrayList<String> listOfProductsInHash, String tableName,
			String columnFamily, String masterIp2, Logger logger)
			throws IOException {

		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp2);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp2 + ":60000");

		// here get count for the above key and if the count is not zero then
		String count = GetCountFromHashContentInhbase(tableName, rowKey,columnFamily,
				masterIp2);
		if (count != null) {

			ArrayList<String> listOfClusters = new ArrayList<String>();
			listOfClusters = FindKeyInHashContentInHbase(tableName, rowKey,columnFamily,
					masterIp2, logger);

			System.out.println(listOfClusters);
			for (int i = 0; i < listOfClusters.size(); i++) {
				if (!listOfClusters.get(i).equals(listOfProductsInHash.get(0))) {
					InsertIntoHashContent(rowKey, tableName, columnFamily,
							Integer.toString(Integer.parseInt(count) + 1),
							listOfProductsInHash, "count",
							Integer.toString(Integer.parseInt(count) + 1),
							masterIp2, logger);
					InsertIntoHashCount(rowKey, "HashCount", columnFamily,
							"count", masterIp2, logger,
							Integer.toString(Integer.parseInt(count) + 1));
				}// end of if
			}// end of for
		} else {
			InsertIntoHashContent(rowKey, tableName, columnFamily, "1",
					listOfProductsInHash, "count", "1", masterIp2, logger);
		}
	}

	// ------------------------------------------------------------------------------------------------
	// function for fetching data from DynamoDb
	// ------------------------------------------------------------------------------------------------
	public static ArrayList<String> fetchDataFromHashContentFromDynamodb(
			String tableName, String rowKey, AmazonDynamoDBClient client) {

		// list to return
		ArrayList<String> temp = new ArrayList<String>();

		Map<String, AttributeValue> lastEvaluatedKey = null;

		do {

			Condition hashKeyCondition = new Condition()
					.withComparisonOperator(ComparisonOperator.EQ.toString())
					.withAttributeValueList(new AttributeValue().withS(rowKey));

			Map<String, Condition> keyConditions = new HashMap<String, Condition>();
			keyConditions.put("hash", hashKeyCondition);

			QueryRequest queryRequest = new QueryRequest()
					.withTableName(tableName).withKeyConditions(keyConditions)
					.withLimit(50).withExclusiveStartKey(lastEvaluatedKey);

			QueryResult result = client.query(queryRequest);
			for (Map<String, AttributeValue> item : result.getItems()) {
				String hash = null;
				String range = null;
				String storeName = null;

				Map<String, AttributeValue> attributeList = item;
				for (Map.Entry<String, AttributeValue> item1 : attributeList
						.entrySet()) {
					String attributeName = item1.getKey();
					AttributeValue value = item1.getValue();
					// System.out.print(attributeName + " "
					// + (value.getS() == null ? "" : "S=[" + value.getS() +
					// "]"));
					if (!attributeName.equals("count")
							&& !attributeName.equals("hash")
							&& !attributeName.equals("range"))
						temp.add(value.getS());

				}// inner for close

			}// outer for close

			lastEvaluatedKey = result.getLastEvaluatedKey();
		} while (lastEvaluatedKey != null);
		// do-while close

		return temp; // list of products json is returning

	}

	// ------------------------------------------------------------------------------------------------
	// in this function map will be sorted
	// ------------------------------------------------------------------------------------------------
	public static Map<String, String> SortMapUsingValues(
			Map<String, String> MapForSoring) {

		Map<String, Integer> tempMapForSoring = new HashMap<String, Integer>();
		Map<String, String> tempMapForSoring_1 = new HashMap<String, String>();

		// conversion
		for (Map.Entry<String, String> item : MapForSoring.entrySet()) {
			tempMapForSoring.put(item.getValue(),
					Integer.parseInt(item.getKey()));
		}// for close

		// call function to sort

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		ValueComparator vc = new ValueComparator(map);
		TreeMap<String, Integer> sorted = new TreeMap<String, Integer>(vc);

		for (Map.Entry<String, Integer> item : tempMapForSoring.entrySet()) {
			map.put(item.getKey(), item.getValue());
		}
		sorted.putAll(map);

		for (Entry<String, Integer> item : sorted.entrySet()) {
			tempMapForSoring_1.put(item.getKey(), "" + item.getValue());
		}

		// conversion
		for (Map.Entry<String, Integer> item : tempMapForSoring.entrySet()) {
			tempMapForSoring_1.put(item.getKey(), "" + item.getValue());
		}// for close

		return tempMapForSoring_1;
	}

	// ------------------------------------------------------------------------------------------------
	// function for insertingIntoHashContent table in hbase for first time
	// ------------------------------------------------------------------------------------------------
	public static void InsertIntoHashContent(String rowKey, String tableName,
			String columnFamily, String qualifier_1,
			ArrayList<String> listOfProductsInHash, String qualifier_2,
			String value_2, String masterIp2, Logger logger) {

		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp2);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp2 + ":60000");
		try {
			HTable table = new HTable(conf, tableName);
			List<Put> listOfPut = new ArrayList<Put>();

			for (int i = 0; i < listOfProductsInHash.size(); i++) {
				Put put = new Put(Bytes.toBytes(rowKey));
				put.add(Bytes.toBytes(columnFamily),
						Bytes.toBytes(Integer.toString(Integer
								.parseInt(qualifier_1) + i)),
						Bytes.toBytes(listOfProductsInHash.get(i)));
				listOfPut.add(put);

			}// for close

			Put put1 = new Put(Bytes.toBytes(rowKey));
			put1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier_2),
					Bytes.toBytes(value_2));

			listOfPut.add(put1);

			table.put(listOfPut);
			table.close();
			// System.out.println("Record Added to hbase");

		} catch (IOException e) {

			logger.info(e.toString());
		}

	} // function close

	// ------------------------------------------------------------------------------------------------
	// function for adding record in HashCount is written here
	// ------------------------------------------------------------------------------------------------
	public static void InsertIntoHashCount(String rowKey, String tableName,
			String columnFamily, String qualifier_1, String masterIp2,
			Logger logger, String countValue) {
		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp2);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp2 + ":60000");
		try {
			HTable table = new HTable(conf, tableName);
			Put put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier_1),
					Bytes.toBytes(countValue));
			table.put(put);
			table.close();
			// System.out.println("Record Added to hbase");

		} catch (IOException e) {

			logger.info(e.toString());
		}

	}// function close

	// ------------------------------------------------------------------------------------------------
	// function for getting probability of hash from HashCount in hbase
	// ------------------------------------------------------------------------------------------------
	public static String getValueOfRowKey(String tableName, String rowKey,
			String columnFamily, String qualifier, String masterIp2)
			throws IOException {

		// System.out.println("Getting count" + tableName + rowKey +
		// columnFamily
		// + qualifier + masterIp2);
		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp2);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp2 + ":60000");
		HTable table = new HTable(conf, tableName);
		String count = null;

		Get get = new Get(Bytes.toBytes(rowKey));
		get.addFamily(Bytes.toBytes(columnFamily));
		Result rs = table.get(get);
		for (KeyValue kv : rs.raw()) {
			// System.out.print(new String(kv.getRow()) + " ");
			// System.out.print(new String(kv.getFamily()) + ":");
			// System.out.print(new String(kv.getQualifier()) + " ");
			// System.out.print(kv.getTimestamp() + " ");
			count = new String(kv.getValue());
		}
		table.close();

		return count;
	}// functin id close

	// ------------------------------------------------------------------------------------------------
	// function for scanning data from habse "parsed_data"
	// ------------------------------------------------------------------------------------------------
	public static Map<String, Map<String, String>> scanParsed_DataTableFromHbase(
			String tableName, String masterIp) {

		Map<String, Map<String, String>> return_hash_map = new HashMap<String, Map<String, String>>();

		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp + ":60000");

		try {
			HTable table = new HTable(conf, tableName);
			Scan s = new Scan();
			s.setBatch(100);

			ResultScanner ss = table.getScanner(s);
			for (Result r : ss) {
				String row = "";
				String storeName = "";
				String jsonProductString = "";

				Map<String, String> temp = new HashMap<String, String>();

				for (KeyValue kv : r.raw()) {
					row = new String(kv.getRow());
					// System.out.println(new String(kv.getQualifier())
					// + "-------" + new String(kv.getValue()));
					if (new String(kv.getQualifier()).contains("storeName")) {
						storeName = new String(kv.getValue());

					}
					if (new String(kv.getQualifier()).contains("data")) {
						jsonProductString = new String(kv.getValue());

					} // for close
						// in temp row is url and value_list is data
				}
				temp.put(jsonProductString, storeName);

				// structure of map is hash=> url ,Map<json,storeName>
				return_hash_map.put(row, temp);

				// System.out.println("---------------------");

			}
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return return_hash_map;
	}// function close

	// ------------------------------------------------------------------------------------------------
	// function for inserting data in HashCount
	// ------------------------------------------------------------------------------------------------
	public static void InsertIntoHashCountInHbase(String tableName,
			String masterIp, Map<String, String> hashCountMap) {

		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp + ":60000");

		for (Map.Entry<String, String> item : hashCountMap.entrySet()) {
			String hash = item.getKey();
			String count = item.getValue();
			try {
				HTable table = new HTable(conf, tableName);
				Put put = new Put(Bytes.toBytes(hash));
				put.add(Bytes.toBytes("cf"), Bytes.toBytes("count"),
						Bytes.toBytes(count));
				table.put(put);
				table.close();
				// System.out.println("Record Added to hbase");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}// for close
	}

	// ------------------------------------------------------------------------------------------------
	// function for fetching hashCount data from dynamoDb
	// ------------------------------------------------------------------------------------------------

	public static Map<String, String> scanHashCount(String tableName,
			AmazonDynamoDBClient client) {

		Map<String, String> hashCountMap = new HashMap<String, String>();

		Map<String, AttributeValue> lastKeyEvaluated = null;

		do {
			ScanRequest scanRequest = new ScanRequest()
					.withTableName(tableName).withLimit(50)
					// give the list of attributes here in the below line of
					// code
					// .withAttributesToGet(Arrays.asList("attributename1","attribure_name2"
					// and so on...))
					.withExclusiveStartKey(lastKeyEvaluated);

			ScanResult result = client.scan(scanRequest);

			for (Map<String, AttributeValue> item : result.getItems()) {
				String hash = null;
				String count = null;
				Map<String, AttributeValue> attributeList = item;
				for (Map.Entry<String, AttributeValue> item1 : attributeList
						.entrySet()) {
					String attributeName = item1.getKey();
					AttributeValue value = item1.getValue();
					/*
					 * System.out.print(attributeName + " " + (value.getS() ==
					 * null ? "" : "S=[" + value.getS() + "]"));
					 */
					if (attributeName.equals("hash")) {

						hash = value.getS();
					}
					if (attributeName.equals("count")) {
						count = value.getS();
					}

				}// inner for close
				hashCountMap.put(hash, count);
				// Move to next line
				System.out.println();
				// deleteItems1(cluster_id,tableName);
			}// outer for close
			lastKeyEvaluated = result.getLastEvaluatedKey();

		} while (lastKeyEvaluated != null);
		// do-while close
		return hashCountMap;
	}// function close

	// ------------------------------------------------------------------------------------------------
	// function for createTable in hbase => HashCount
	// ------------------------------------------------------------------------------------------------
	public static void createHashCountTable(String TABLENAME, String family,
			String masterIP, Logger logger) throws IOException {
		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIP);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIP + ":60000");
		HBaseAdmin admin = new HBaseAdmin(conf);
		if (admin.tableExists(TABLENAME)) {
			logger.info("HashCount Table in hbase already exist");
			admin.disableTable(TABLENAME);
			admin.deleteTable(TABLENAME);
			logger.info("HashCount Table is deleted successfully");

		}

		HTableDescriptor tableDesc = new HTableDescriptor(TABLENAME);
		tableDesc.addFamily(new HColumnDescriptor(family));
		admin.createTable(tableDesc);
		admin.close();
		logger.info("HashCount Table in hbase is created successfully.");

	}// function close

	// ------------------------------------------------------------------------------------------------
	// in this function Update in HashCount in DynamoDb will occur |
	// ------------------------------------------------------------------------------------------------
	public static void UpdateHashCountInDynamodb(String tableName,
			Map<String, String> mapOfHashCount, Logger logger,
			AmazonDynamoDBClient client) {

		for (Map.Entry<String, String> item : mapOfHashCount.entrySet()) {

			try {

				Map<String, AttributeValueUpdate> updateItems = new HashMap<String, AttributeValueUpdate>();

				HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();

				key.put("hash", new AttributeValue().withS(item.getKey()));
					updateItems.put(
						"count",
						new AttributeValueUpdate().withAction(
								AttributeAction.PUT).withValue(
								new AttributeValue().withS(item.getValue())));
				
				UpdateItemRequest updateItemRequest = new UpdateItemRequest()
						.withTableName(tableName).withKey(key)
						.withAttributeUpdates(updateItems);

				UpdateItemResult result = client.updateItem(updateItemRequest);

			} // try close
			catch (AmazonServiceException ase) {
				logger.info(ase.toString());
			}

		}// for close

	}

	// ------------------------------------------------------------------------------------------------
	// Function for CopyDataFromHashCountToDynamodbHashCount here |
	// ------------------------------------------------------------------------------------------------
	public static void UpdateHashContentInDynamodb(String tableName,
			Map<String, Map<String, String>> mapForHashContent, Logger logger,
			AmazonDynamoDBClient client) {

		for (Map.Entry<String, Map<String, String>> item : mapForHashContent
				.entrySet()) {
			try {

				Map<String, AttributeValue> putMap = new HashMap<String, AttributeValue>();

				putMap.put("hash", new AttributeValue().withS(item.getKey()));
				putMap.put("range", new AttributeValue().withS("cf"));

				for (Map.Entry<String, String> item2 : item.getValue()
						.entrySet()) {
					putMap.put(item2.getKey(),
							new AttributeValue().withS(item2.getValue()));

				}// inner for close

				// create put object

				PutItemRequest putItemRequest = new PutItemRequest()
						.withTableName(tableName).withItem(putMap);

				PutItemResult result = client.putItem(putItemRequest);

			}// try close
			catch (AmazonServiceException ase) {
				logger.info(ase.toString());
			}// catch close

		}// for close

	}// fn close
	// -----------------------------------------------------------------------------------------
	// Function For Finding and inserting data in HashContent and return it
	// -----------------------------------------------------------------------------------------
	public static ArrayList<String> FindKeyInHashContent(String tableName,
			String key, String range,
			String masterIp, Logger logger) throws IOException {
		ArrayList<String> listOfClusters = new ArrayList<String>();
		listOfClusters = FindKeyInHashContentInHbase(tableName, key,range, masterIp,
				logger);
		if (listOfClusters.size() == 0) {
		// Wtite in file .hashContent that we need this key
			System.out.println(key+" in HashContent");
			TaskStatus.WriteInHashContentTaskStatus("HashContentTaskStatus",range,key,"task","notDone",masterIp);
			while(true)
			{
				
				Boolean status = TaskStatus.FindStatusOfHashContentTaskStatus("HashContentTaskStatus",range,key,"task","done",masterIp);
				System.out.println(status+" in HashContent");
				if(status)
				{
					// task is finished
					break;
				}// if close
			}// while close
			
			//delete record from HashContentTaskStatus
			RedWrite.delRecord("HashContentTaskStatus", key,range, masterIp);
			
		}// if close

		// again scan
		listOfClusters = FindKeyInHashContentInHbase(tableName, key,range, masterIp,
				logger);
		
		
	
		return listOfClusters;
	}

	// -----------------------------------------------------------------------------------------
	// Function For Fetching data from HashContent table
	// -----------------------------------------------------------------------------------------

	private static ArrayList<String> FindKeyInHashContentInDynamo(
			String tableName, String key, AmazonDynamoDBClient client) {

		ArrayList<String> listForCluster = new ArrayList<String>();

		Map<String, AttributeValue> lastEvaluatedKey = null;

		// do {

		Condition hashKeyCondition = new Condition().withComparisonOperator(
				ComparisonOperator.EQ.toString()).withAttributeValueList(
				new AttributeValue().withS(key));

		Map<String, Condition> keyConditions = new HashMap<String, Condition>();
		keyConditions.put("hash", hashKeyCondition);

		QueryRequest queryRequest = new QueryRequest().withTableName(tableName)
				.withKeyConditions(keyConditions).withLimit(90)
				// .withAttributesToGet(Arrays.asList("url","storeName"))
				.withExclusiveStartKey(lastEvaluatedKey);

		QueryResult result = client.query(queryRequest);
		for (Map<String, AttributeValue> item : result.getItems()) {

			Map<String, AttributeValue> attributeList = item;

			for (Map.Entry<String, AttributeValue> item1 : attributeList
					.entrySet()) {
				String attributeName = item1.getKey();
				AttributeValue value = item1.getValue();

				if (!attributeName.equals("count")
						&& !attributeName.equals("range")
						&& !attributeName.equals("hash"))
					listForCluster.add(value.getS());

			} // inner for close

			// delete the specific item

		}

		// lastEvaluatedKey = result.getLastEvaluatedKey();
		// } while (lastEvaluatedKey != null);
		// do-while close
		return listForCluster;
	}// fn close

	// ---------------------------------------------------------------------------------------------
	// Function For FindKeyInHashContentInHbase
	// ---------------------------------------------------------------------------------------------
	private static ArrayList<String> FindKeyInHashContentInHbase(
			String tableName, String rowKey, String range, String masterIp2, Logger logger) {

		ArrayList<String> qualifierValueList = new ArrayList<String>();

		Configuration conf = new Configuration();

		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp2);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp2 + ":60000");

		HTable table = null;
		try {
			table = new HTable(conf, tableName);
		} catch (IOException e) {
			logger.info(e.toString());
		}

		// -------------------------------------------------------------------------------------------
		Get get = new Get(rowKey.getBytes());
		get.addFamily(Bytes.toBytes(range));
		Result rs = null;
		try {
			rs = table.get(get);
		} catch (IOException e) {
			logger.info(e.toString());

		}

		for (KeyValue kv : rs.raw()) {

			String qualifierName = new String(kv.getQualifier());
			String qualifierValue = new String(kv.getValue());

			// add the above vars into the qualifierValueMap except count
			if (!qualifierName.equals("count"))
				qualifierValueList.add(qualifierValue);

		}// for close

		try {
			table.close();
		} catch (IOException e) {
			logger.info(e.toString());

		}// catch close

		return qualifierValueList;
	}

	// ----------------------------------------------------------------------------------------------
	// Function For FindKeyInHashContentInDynamo
	// ----------------------------------------------------------------------------------------------
	public static Boolean MatchClusterWithParsedData(String tableName,
			String key,String range, String data, Logger logger, String masterIp,
			String storeName) throws IOException {
		Configuration conf = new Configuration();

		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp + ":60000");
		HTable table = null;

		try {
			table = new HTable(conf, tableName);
		} catch (IOException e) {
			logger.info(e.toString());
			e.printStackTrace();
		}
		Get get = new Get(key.getBytes());
		get.addColumn(Bytes.toBytes(range), Bytes.toBytes("data"));
		Result rs = null;
		try {
			rs = table.get(get);
		} catch (IOException e) {
			logger.info(e.toString());
			e.printStackTrace();
		}

		String clusterData = null;
		for (KeyValue kv : rs.raw()) {
			// System.out.print(new String(kv.getRow()) + " ");
			// System.out.print(new String(kv.getValue() + ":"));
			// System.out.print(new String(kv.getQualifier()) + " ");
			// System.out.print(kv.getTimestamp() + " " );
			String qualifierName = new String(kv.getQualifier());

			if (qualifierName.equals("data")) {
				clusterData = new String(kv.getValue());

			}// if close

		}
		try {
			table.close();
		} catch (IOException e) {
			logger.info(e.toString());
			e.printStackTrace();
		}

		// -----------------------------------------------------------------
		// Code For Matching
		// -----------------------------------------------------------------

		// System.out.println("data :- " + data);
		// System.out.println(clusterData);

		Boolean status = MatchTwoJsonString(data, clusterData, storeName,
				masterIp, key,range, logger);

		return status;
	}

	// ----------------------------------------------------------------------------------------------
	// Function For FindKeyInHashContentInDynamo
	// ----------------------------------------------------------------------------------------------

	public static String FindClusterNumberUsingAmpleClusterInHbase(
			String tableName, Logger logger, String masterIp) {

		String clusterNo = null;
		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp + ":60000");
		HTable table = null;
		try {
			table = new HTable(conf, tableName);
		} catch (IOException e) {
			logger.info(e.toString());
			e.printStackTrace();
		}
		Get get = new Get("count".getBytes());
		get.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("count"));
		Result rs = null;
		try {
			rs = table.get(get);
		} catch (IOException e) {
			logger.info(e.toString());
			e.printStackTrace();
		}

		String count = null;
		for (KeyValue kv : rs.raw()) {
			// System.out.print(new String(kv.getRow()) + " " );
			// System.out.print(new String(kv.getFamily()) + ":" );
			// System.out.print(new String(kv.getQualifier()) + " " );
			// System.out.print(kv.getTimestamp() + " " );
			String qualifierName = new String(kv.getQualifier());
			if (qualifierName.equals("count")) {
				count = new String(kv.getValue());
			}// if close
		}
		try {
			table.close();
		} catch (IOException e) {
			logger.info(e.toString());
			e.printStackTrace();
		}

		clusterNo = Integer.toString(Integer.parseInt(count) + 1);
		// call function for updating AmpleCluster table
		UpdateAmpleClusterCountHashInhbase("AmpleCluster", clusterNo, logger,
				masterIp);

		return clusterNo;
	}

	// -------------------------------------------------------------------------------------------
	// Function for updating AmpleCluster Table for hash => "count" in hbase
	// -------------------------------------------------------------------------------------------

	private static void UpdateAmpleClusterCountHashInhbase(String tableName,
			String clusterNo, Logger logger, String masterIp) {
		try {

			Configuration conf = new Configuration();
			conf = HBaseConfiguration.create();
			conf.set("hbase.zookeeper.quorum", masterIp);
			conf.set("hbase.zookeeper.property.clientPort", "2181");
			conf.set("hbase.master", masterIp + ":60000");

			HTable table = new HTable(conf, tableName);
			Put put = new Put(Bytes.toBytes("count"));
			put.add(Bytes.toBytes("cf"), Bytes.toBytes("count"),
					Bytes.toBytes(clusterNo));

			table.put(put);
			table.close();

			logger.info("insertion in " + tableName + " is finished.");
			// System.out.println("Record Added to hbase");
		} catch (IOException e) {
			e.printStackTrace();
			logger.info(e.toString());
		}

	}// fr close

	// -------------------------------------------------------------------------------------------
	// Function for updating AmpleCluster Table for hash => "count"
	// -------------------------------------------------------------------------------------------
	private static void UpdateAmpleClusterCountHash(String tableName,
			AmazonDynamoDBClient client, String count, Logger logger) {
		try {
			Map<String, AttributeValueUpdate> updateItems = new HashMap<String, AttributeValueUpdate>();

			HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
			key.put("cluster", new AttributeValue().withS("count"));
			key.put("range", new AttributeValue().withS("cf"));

			// Add a new attribute.
			updateItems.put("count", new AttributeValueUpdate()
					.withValue(new AttributeValue().withS(count)));

			UpdateItemRequest updateItemRequest = new UpdateItemRequest()
					.withTableName(tableName).withKey(key)
					.withAttributeUpdates(updateItems);

			UpdateItemResult result = client.updateItem(updateItemRequest);

		} catch (AmazonServiceException ase) {
			logger.info("error in updating count hash in AmpleCluster :"
					+ ase.toString());
		}

	}

	// -------------------------------------------------------------------------------------------
	// Function for updating AmpleCluster Table for hash => "count"
	// -------------------------------------------------------------------------------------------
	public static void EnrtyInAmpleClusterInhbase(String tableName,
			String masterIp, AmazonDynamoDBClient client, Logger logger,
			ArrayList<String> listOfClusterNo) {

		Map<String, String> mapForClusters = new HashMap<String, String>();

		for (int i = 0; i < listOfClusterNo.size(); i++) {
			Condition hashKeyCondition = new Condition()
					.withComparisonOperator(ComparisonOperator.EQ.toString())
					.withAttributeValueList(
							new AttributeValue().withS(listOfClusterNo.get(i)));

			Map<String, Condition> keyConditions = new HashMap<String, Condition>();
			keyConditions.put("cluster", hashKeyCondition);

			QueryRequest queryRequest = new QueryRequest()
					.withTableName(tableName).withKeyConditions(keyConditions)
					.withLimit(90);

			QueryResult result = client.query(queryRequest);

			// get clusterNo to make
			for (Map<String, AttributeValue> item : result.getItems()) {
				Map<String, AttributeValue> attributeList = item;
				String data = null;
				for (Map.Entry<String, AttributeValue> item1 : attributeList
						.entrySet()) {
					String attributeName = item1.getKey();
					AttributeValue value = item1.getValue();
					if (attributeName.equals("data")) {
						data = value.getS();
						mapForClusters.put(listOfClusterNo.get(i), data);
					}
				} // inner for close
			}// item for close
		}// for close for list of cluster

		// now we have a map hash => clusterNo and value => data(json)

		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp + ":60000");

		HTable table = null;
		try {
			table = new HTable(conf, tableName);
		} catch (IOException e1) {
			logger.info(e1.toString());
			e1.printStackTrace();
		}
		try {
			List<Put> listOfPut = new ArrayList<Put>();

			for (Map.Entry<String, String> item : mapForClusters.entrySet()) {

				String hash = item.getKey();
				String value = item.getValue();
				Put put = new Put(Bytes.toBytes(hash));
				put.add(Bytes.toBytes("cf"), Bytes.toBytes("data"),
						Bytes.toBytes(value));
				// System.out.println("Record Added to hbase");
				listOfPut.add(put);
			}// for close
			table.put(listOfPut);
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	} // fn close

	// -------------------------------------------------------------------------------------------
	// Function for EnrtyInAmpleClusterInhbase2
	// -------------------------------------------------------------------------------------------
	public static void EnrtyInAmpleClusterInhbase2(String tableName,
			String masterIp, Logger logger, String data, String clusterNo) {

		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp + ":60000");

		HTable table = null;
		try {
			table = new HTable(conf, tableName);
		} catch (IOException e1) {
			logger.info(e1.toString());
			e1.printStackTrace();
		}

		try {
			Put put = new Put(Bytes.toBytes(clusterNo));
			put.add(Bytes.toBytes("cf"), Bytes.toBytes("data"),
					Bytes.toBytes(data));

			table.put(put);
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}// fn close

	// -------------------------------------------------------------------------------------------
	// Function for Modifying Data
	// -------------------------------------------------------------------------------------------
	public static String modifyData(String data, String storeName) {
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

	// -----------------------------------------------------------------------------------------
	// Function for CopyDataAmpleClusterToDynamodbAmpleCluster
	// -----------------------------------------------------------------------------------------
	public static void CopyDataAmpleClusterToDynamodbAmpleCluster(
			String tableName, String masterIp2, Logger logger,
			AmazonDynamoDBClient client) {

		Map<String, Map<String, String>> mapForAmpleCluster = new HashMap<String, Map<String, String>>();
		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp2);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp2 + ":60000");

		try {
			HTable table = new HTable(conf, tableName);
			Scan s = new Scan();
			// s.setBatch(100);

			ResultScanner ss = table.getScanner(s);
			for (Result r : ss) {
				String row = null;
				String qualifierName = null;
				String qualifierValue = null;
				Map<String, String> temp = new HashMap<String, String>();

				for (KeyValue kv : r.raw()) {
					row = new String(kv.getRow());
					// System.out.println(new String(kv.getQualifier())
					// + "-------" + new String(kv.getValue()));
					qualifierName = new String(kv.getQualifier());
					qualifierValue = new String(kv.getValue());
					if(qualifierName != null && qualifierValue != null)
					   temp.put(qualifierName, qualifierValue);
				} // for close
				if(!temp.isEmpty())
				mapForAmpleCluster.put(row, temp);
			} // outer for close

			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}// catch close

		// send mapForHashContent of hbase into HashContent Dynamodb HashContent
		if(!mapForAmpleCluster.isEmpty())
		UpdateAmpleClusterInDynamodb(tableName, mapForAmpleCluster, logger,
				client);

	}

	// ------------------------------------------------------------
	// Function for Updating AmpleCluster
	// ------------------------------------------------------------
	private static void UpdateAmpleClusterInDynamodb(String tableName,
			Map<String, Map<String, String>> mapForAmpleCluster, Logger logger,
			AmazonDynamoDBClient client) {

		for (Map.Entry<String, Map<String, String>> item : mapForAmpleCluster
				.entrySet()) {
			try {

				Map<String, AttributeValue> putMap = new HashMap<String, AttributeValue>();

				putMap.put("cluster", new AttributeValue().withS(item.getKey()));
				putMap.put("range", new AttributeValue().withS("cf"));

				for (Map.Entry<String, String> item2 : item.getValue()
						.entrySet()) {
					putMap.put(item2.getKey(),
							new AttributeValue().withS(item2.getValue()));

				}// inner for close

				// create put object

				PutItemRequest putItemRequest = new PutItemRequest()
						.withTableName(tableName).withItem(putMap);

				PutItemResult result = client.putItem(putItemRequest);

			}// try close
			catch (AmazonServiceException ase) {
				logger.info(ase.toString());
			}// catch close

		}// for close

	}

	public static void MakeEntryInSolrDataFromAmpleClusterInhbase(
			String masterIp2) {

		Map<String, Map<String, String>> mapForAmpleCluster = new HashMap<String, Map<String, String>>();

		Configuration conf = null;
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIp2);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIp2 + ":60000");

		try {
			HTable table = new HTable(conf, "AmpleCluster");
			Scan s = new Scan();
			// s.setBatch(100);

			ResultScanner ss = table.getScanner(s);
			for (Result r : ss) {
				String row = null;
				String qualifierName = null;
				String qualifierValue = null;

				for (KeyValue kv : r.raw()) {
					row = new String(kv.getRow());

					qualifierName = new String(kv.getQualifier());
					if (qualifierName.equals("data")) {

						qualifierValue = new String(kv.getValue());

					}

				} // for close

				if (row != null && qualifierName != null
						&& qualifierValue != null) {
					// calling function for modifying data

					try {
						String data = ModifyData(qualifierValue);
						RedWrite.addRecord("solr_data", row, "cf",
								qualifierName, data, masterIp2);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out
								.println("Certain Problem Occured........................................");
						e.printStackTrace();
					}
				}// if close
			} // outer for close

			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}// catch close

	}// fn close

	// ------------------------------------------------------------------
	// Function for modifying json data
	// ------------------------------------------------------------------
	public static String ModifyData(String json) {
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
			jsonData.put("counts", counts);
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
	}// fn close
	
	//-------------------------00000000000000000000000--------------------------------------
	//Function for testing
	//
	public static void Testing1() throws IOException
	{
		AmazonDynamoDBClient client;

		AWSCredentials credentials = new PropertiesCredentials(
				Functions.class
						.getResourceAsStream("AwsCredentials.properties"));
		
		
	    client = new AmazonDynamoDBClient(credentials);
	
		Map<String, String> mapOfProductsInHash = new HashMap<String, String>();
		Map<String, AttributeValue> lastEvaluatedKey = null;

		//do 
		{

			Condition hashKeyCondition = new Condition()
					.withComparisonOperator(ComparisonOperator.EQ.toString())
					.withAttributeValueList(new AttributeValue().withS("doing"));

			Map<String, Condition> keyConditions = new HashMap<String, Condition>();
			keyConditions.put("status", hashKeyCondition);

			QueryRequest queryRequest = new QueryRequest()
					.withTableName("ProductLink").withKeyConditions(keyConditions)
					.withLimit(5)
					//.withAttributesToGet(Arrays.asList("count", "attribute"))
					.withExclusiveStartKey(lastEvaluatedKey);

			QueryResult result = client.query(queryRequest);
			for (Map<String, AttributeValue> item : result.getItems()) {

				Map<String, AttributeValue> attributeList = item;

				for (Map.Entry<String, AttributeValue> item1 : attributeList
						.entrySet()) {
					String attributeName = item1.getKey();
					AttributeValue value = item1.getValue();
					System.out.println(value);
					//mapOfProductsInHash.put(attributeName, value.getS());
				}// inner for close

			}

			lastEvaluatedKey = result.getLastEvaluatedKey();
		} 
		//while (lastEvaluatedKey != null);
		// do-while close

	}// fn close
	//-----------------------------------------------------------------
	public static void Testing()
	{
		System.out.println("I am in this function....");
	}// fn close

	

}// class close
