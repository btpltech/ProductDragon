package GroupingManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class Functions {
	
	public static void insertIntoDynamodb_ParsedProducts_processing(
			Map<String, String> dataFromDynamodb,
			String tableName, String hashName, String rangeName,
			String columnName,AmazonDynamoDBClient client,Logger logger) {
		System.out
				.println("Inside insertIntoDynamodb ParsedProducts processing");
		for (Map.Entry<String, String> item : dataFromDynamodb
				.entrySet()) {
				try {
					String data[] = item.getKey().split("~!~");
					
					String hashValue = data[0];
					String rangeValue = data[1];
					String columnValue = item.getValue();

					Map<String, AttributeValue> putItemStore = new HashMap<String, AttributeValue>();
					putItemStore.put(hashName,
							new AttributeValue().withS(hashValue));
					putItemStore.put(rangeName,
							new AttributeValue().withS(rangeValue));
					putItemStore.put(columnName,
							new AttributeValue().withS(columnValue));

					PutItemRequest putItemRequest = new PutItemRequest()
							.withTableName(tableName).withItem(putItemStore);
					PutItemResult result1 = client.putItem(putItemRequest);

				} catch (AmazonServiceException ase) {
					//ase.printStackTrace();
					System.err.println("Create items failed.");
					logger.info("insertion in " + tableName + " is failed.");
				}
		}// for close
}
	// function for inserting data in HashCount
		public static void InsertIntoHashCountInHbase(String tableName,
				String masterIp, Map<String, String> hashCountMap) {

			Configuration conf = new Configuration();
			conf = HBaseConfiguration.create();
			conf.set("hbase.zookeeper.quorum", masterIp);
			conf.set("hbase.zookeeper.property.clientPort", "2181");
			conf.set("hbase.master", masterIp + ":60000");
			try {
				HTable table = new HTable(conf, tableName);
				List<Put> listPut = new ArrayList<Put>();
			for (Map.Entry<String, String> item : hashCountMap.entrySet()) {
				String hash = item.getKey();
				String count = item.getValue();
					Put put = new Put(Bytes.toBytes(hash));
					put.add(Bytes.toBytes("cf"), Bytes.toBytes("count"),
							Bytes.toBytes(count));
					listPut.add(put);
				}// for close
					table.put(listPut);	
					table.close();
					// System.out.println("Record Added to hbase");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		// function for createTable in hbase => HashCount
		public static void createHashCountTable(String TABLENAME, String family,
				String masterIP,Logger logger) throws IOException {
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
		// createTableInhbase
		public static void createTableInhbase(String tableName,
				String columnFamily, String masterIP,Logger logger) throws IOException {

			Configuration conf = new Configuration();
			conf = HBaseConfiguration.create();
			conf.set("hbase.zookeeper.quorum", masterIP);
			conf.set("hbase.zookeeper.property.clientPort", "2181");
			conf.set("hbase.master", masterIP + ":60000");
			HBaseAdmin admin = new HBaseAdmin(conf);
			if (admin.tableExists(tableName)) {
				System.out.println("table already exists!");
				logger.info(tableName + " table already exists");
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
				System.out.println(tableName + " is deleted.");
			}

			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			tableDesc.addFamily(new HColumnDescriptor(columnFamily));
			admin.createTable(tableDesc);
			admin.close();
			// System.out.println("created table " + tableName + " is created.");
			logger.info("created table " + tableName + " is created.");
		} // function close
		// function for fetching hashCount data from dynamoDb

		public static Map<String, String> scanHashCount(String tableName,AmazonDynamoDBClient client) {

			Map<String, String> hashCountMap = new HashMap<String, String>();

			Map<String, AttributeValue> lastKeyEvaluated = null;

			do {
				ScanRequest scanRequest = new ScanRequest()
						.withTableName(tableName).withLimit(500)
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

//						System.out.print(attributeName
//								+ " "
//								+ (value.getS() == null ? "" : "S=[" + value.getS()
//										+ "]"));

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
		
		
		public static Map<String,String> scanParsedProducts(
				String tableName,int limit,int copyLimitFromDynamo, String separator, AmazonDynamoDBClient client) {

			Map<String, String> mapToReturn = new HashMap<String, String>();
			Map<String, AttributeValue> lastEvaluatedKey = null;
			int count = 0;
			do {
			ScanRequest scanRequest = new ScanRequest().withTableName(tableName)
					.withLimit(copyLimitFromDynamo)
					.withExclusiveStartKey(lastEvaluatedKey);
			ScanResult result = client.scan(scanRequest);
			for (Map<String, AttributeValue> item : result.getItems()) {
				String storeName = null;
				String url = null;
				String data = null;
				Map<String, AttributeValue> attributeList = item;
				for (Map.Entry<String, AttributeValue> item1 : attributeList
						.entrySet()) {
					String attributeName = item1.getKey();
					AttributeValue value = item1.getValue();
//					System.out.print(attributeName
//							+ " "
//							+ (value.getS() == null ? "" : "S=[" + value.getS()
//									+ "]"));
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
                if(count<=limit)
                {
                	count++;
                	mapToReturn.put(storeName+separator+url, data);
	            }
				
			} // outer for close
				 lastEvaluatedKey = result.getLastEvaluatedKey();
			 } while (lastEvaluatedKey != null && count<=copyLimitFromDynamo);
			return mapToReturn;
		} // function is closed

		// file deleting function
		public static void deleteFile(String path,Logger logger) {
			try{
				 
				File file = new File(path);
		        file.setExecutable(true);
		        file.setWritable(true);
		        file.setReadable(true);
				
		        if(file.delete()){
					logger.info(file.getName() + " is deleted!");
					//System.out.println(file.getName() + " is deleted!");
				}else{
					logger.info("Delete operation is failed.");
				}
			}catch(Exception e){
				 logger.info("File : "+path+" not found");
			}
		}
		public static void deleteFromDynamoDbParsedProducts(
				Set<String> set,
				String deleteTableName, String hashName, String rangeName,Logger logger, AmazonDynamoDBClient client) {
			String hashValue = null;
			String rangeValue = null;
			String data[] = null;
			for (String s : set) {
				try
				{
					data = s.split("~!~");
					hashValue  = data[0];
					rangeValue = data[1];
					//Map<String, ExpectedAttributeValue> expectedValues = new HashMap<String, ExpectedAttributeValue>();
					HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
					key.put(hashName, new AttributeValue().withS(hashValue));
					key.put(rangeName, new AttributeValue().withS(rangeValue));
					DeleteItemRequest deleteItemRequest = new DeleteItemRequest()
							.withTableName(deleteTableName).withKey(key);
							//.withExpected(expectedValues);
					DeleteItemResult result = client
							.deleteItem(deleteItemRequest);

				}
				catch (AmazonServiceException ase) {
					ase.printStackTrace();
					logger.info("Failed to get item after deletion "
							+ deleteTableName);
				}
				catch(Exception e)
				{
					logger.info("For link "+s+" updation is failed");
				}
			}// for close
		}
		public static void UploadInS3(String path,String fileName,Logger logger) {

			String accessKey = "AKIAJMAEQRZSCGU3JZTA";
			String secretKey = "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";
		    String bucketName = "ParsedData";
		    //String key = fileName;
		    
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		    AmazonS3 s3 = new AmazonS3Client(credentials);
			Region usWest2 = Region.getRegion(Regions.US_EAST_1);
			s3.setRegion(usWest2);
		    	//logger.info("Uploading a new object to S3 from a file\n");
		    	//logger.info("Uploading a new object to S3 from a file"+fileName);
		    	s3.putObject(bucketName,fileName, new File(path));
		    	//logger.info("Uploading a new object to S3 from a file is done");
		}
		public static void WriteInDynamo(String tableName, String hashName,
				String rangeName, String hashValue, String rangeValue,
				AmazonDynamoDBClient client) {
			
		    try {
					Map<String, AttributeValue> item1 = new HashMap<String, AttributeValue>();
			        item1.put(hashName, new AttributeValue().withS(hashValue));
			        item1.put(rangeName, new AttributeValue().withS(rangeValue));
			        
			        PutItemRequest putItemRequest1 = new PutItemRequest()
			        .withTableName(tableName)
			        .withItem(item1);
			        PutItemResult result1 = client.putItem(putItemRequest1);
		       } catch (AmazonServiceException ase) {
		           	//System.out.println(ase);
		               System.err.println("Create items failed.");
		    }// for close
		}
		public static void GetCountOfClusterNumberAndMakeEntryInAmpleClusterInHbase(
				String masterIP, AmazonDynamoDBClient client, Logger logger) {

			String countValue = "0";

			Condition hashKeyCondition = new Condition().withComparisonOperator(
					ComparisonOperator.EQ.toString()).withAttributeValueList(
					new AttributeValue().withS("count"));

			Map<String, Condition> keyConditions = new HashMap<String, Condition>();
			keyConditions.put("cluster", hashKeyCondition);

			QueryRequest queryRequest = new QueryRequest()
					.withTableName("AmpleCluster").withKeyConditions(keyConditions)
					.withLimit(90);

			QueryResult result = client.query(queryRequest);

			// get clusterNo to make
			for (Map<String, AttributeValue> item : result.getItems()) {
				Map<String, AttributeValue> attributeList = item;
				for (Map.Entry<String, AttributeValue> item1 : attributeList
						.entrySet()) {
					String attributeName = item1.getKey();
					AttributeValue value = item1.getValue();
					if (attributeName.equals("count")) {
						countValue = value.getS();

					}
				} // inner for close
			}
			// now we have a map hash => clusterNo and value => data(json)

			Configuration conf = new Configuration();
			conf = HBaseConfiguration.create();
			conf.set("hbase.zookeeper.quorum", masterIP);
			conf.set("hbase.zookeeper.property.clientPort", "2181");
			conf.set("hbase.master", masterIP + ":60000");

			try {
				HTable table = new HTable(conf, "AmpleCluster");
				;
				Put put = new Put(Bytes.toBytes("count"));
				put.add(Bytes.toBytes("cf"), Bytes.toBytes("count"),
						Bytes.toBytes(countValue));
				// System.out.println("Record Added to hbase");
				table.put(put);
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// if close
		}
}
