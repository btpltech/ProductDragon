import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
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
import com.jcraft.jsch.JSchException;

public class SeleniumServer {
	static AmazonDynamoDBClient client;
	
	static String pathForFile = "/home/ubuntu/input/";
	// path for file that contains instanceid verses storeName
	static String pathForComparison;
	static Functions fun = new Functions();
	static Function fun2 = new Function();
	private static void createClient() throws IOException {

		AWSCredentials credentials = new PropertiesCredentials(
				SeleniumServer.class
						.getResourceAsStream("AwsCredentials.properties"));
		client = new AmazonDynamoDBClient(credentials);

	} // create client is closed

	public static void main(String[] args) throws IOException, JSchException {
		
		// function is called here
//		pathForComparison = "status.txt";
		StartSeleniumServer(Integer.parseInt(args[0]),
				Integer.parseInt(args[1]));
	} // main close

	// function for creating client
	

	private static void StartSeleniumServer(int no_of_menulink,
			int menulink_per_thread) throws IOException, JSchException {

		// code for making logger
		Logger logger = Logger.getLogger("MyLog");
		FileHandler fh;
		try {

			// This block configure the logger with handler and formatter
			fh = new FileHandler("SeleniumServer.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

			// the following statement is used to log any messages
			logger.info("SeleniumServer Log File");

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// till here code for logging

		// here while loop is working continue working
		while (true) {

			// function for fetching data from DynamoDb
//			createClient();
			Map<String, String> storeList = new HashMap<String, String>();
			logger.info("scanning StoreLink table");
			storeList = scanStoreLinkTable();
			logger.info("scanning StoreLink is finished.");
			String firstLine = new String();

			if (!storeList.isEmpty()&&!storeList.equals("")) {
				logger.info("Now file creation is started per store");
				// function for getting perStoreInformation
				ArrayList<String> PathStoreForCreatingInstances = new ArrayList<String>();

				String fileName = new String();

				for (Map.Entry<String, String> item : storeList.entrySet()) {
					// arraylist for remembering that how many files are being
					// craeted.

					Map<String, ArrayList<String>> mapForStoreUrl = new HashMap<String, ArrayList<String>>();

					logger.info("Fetching data for " + item.getValue());
					mapForStoreUrl = scanMenuLinkTable(item.getValue());
					logger.info("Fetching data for" + item.getValue()
							+ "finished.");

					if (!mapForStoreUrl.isEmpty()&&!mapForStoreUrl.equals("")) {
						logger.info("making text file for " + item.getValue());

						for (Map.Entry<String, ArrayList<String>> item2 : mapForStoreUrl
								.entrySet()) {
							int total = item2.getValue().size()
									/ no_of_menulink;
							int mod = item2.getValue().size() % no_of_menulink;
							int n;
							if (mod != 0) {
								n = total + 1;
							} else {
								n = total;
							}
							int i = 0;
							int kp = item2.getValue().size() / n;
							for (int k = 1; k <= total; k++) {

								fileName = item2.getKey();

								File file = new File(pathForFile + fileName
										+ "_" + k + ".txt");
								// add complete path in ArrayList

								PathStoreForCreatingInstances.add(pathForFile
										+ fileName + "_" + k + ".txt");
//								String slave_filename = fileName + "_" + k
//										+ ".txt";

								// while (i < filelist.size()) {

								FileOutputStream f = new FileOutputStream(file);
//								firstLine = "#" + item.getKey().toString();
//								byte[] firstLineByte = firstLine.getBytes();
//								f.write(firstLineByte);
								// if (n > 2) {
								while (i < kp) {
									if (item.getKey() != item2.getValue()
											.get(i)
											&& item2.getKey() != item2
													.getValue().get(i)) {

										String content = "\n"
												+ item2.getValue().get(i)
														.replace(" ", "");

										byte[] contentInBytes1 = content
												.getBytes();
										// fop.write(c_byte);
										f.write(contentInBytes1);
									} //end of if 
									i++;

									// }
								} // end of while
								n--;
								fun.startProcessThread(
										"create_selenium_slave.sh",
										fileName + "_" + k
										+ ".txt", fileName,
										no_of_menulink,
										menulink_per_thread, logger);
								logger.info("create selenium slave for "
										+ fileName + "_" + k
										+ ".txt" + " sucessfully");
								if (n != 0) {
									
									
									i = kp;
									kp = item2.getValue().size() / n;
								} //end of if
								else {
									break;
								}// end of else

							}// end of for loop of which create various no on files
							logger.info("making text file for "
									+ item2.getKey() + "finished.");
							
							logger.info("total true menulink are  "+item2.getValue().size());
						}//for loop close of menulink table itmes
							
							// // }// if for mapForSiteurl is closed

							updateStatusToDoing(item.getKey(), fileName);

						logger.info("all files are created.");
						

					} //end of menulink if
					else{
						logger.info("There are no menulink of store"+item.getKey());
					}
				} //end of storelink list for
			}// end of if storelink
			else {
				logger.info("there is no data in StoreLink table whose status is notDone");
			}// code for fetching and making file is done till here
			System.out.println("killing slaves");
			changeStatustoDone(logger);
			/* Waits for while loop*/
			try {
				Thread.sleep(600000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}// while close
	}

	private static void InsertintoSlave(String tableName, String hashname,
			String hashvalue, String rangeName, String rangevalue,
			String columnName, String columnValue) {

		Map<String, AttributeValue> item1 = new HashMap<String, AttributeValue>();
		item1.put(hashname, new AttributeValue().withS(hashvalue));
		item1.put(rangeName, new AttributeValue().withS(rangevalue));
		item1.put(columnName, new AttributeValue().withS(columnValue));

		deleteItem(hashname, hashvalue, rangeName, rangevalue, tableName);
		// function for inserting item into table
		InsertIntoTable(hashname, "done", rangeName, rangevalue, columnName,
				columnValue, tableName);
		System.out.println("status of " + rangevalue + " is updated to Done");

	}

	

	// function for scanning MenuLink Table
	private static Map<String, ArrayList<String>> scanMenuLinkTable(String host) {
//		System.out.println("++++++++++++++++++++++++++++");

		// List to return
		try {
			createClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, ArrayList<String>> mapToReturn = new HashMap<String, ArrayList<String>>();
		ArrayList<String> tempStoreLinks = new ArrayList<String>();

		Map<String, AttributeValue> lastEvaluatedKey = null;

		do {

			Condition hashKeyCondition = new Condition()
					.withComparisonOperator(ComparisonOperator.EQ.toString())
					.withAttributeValueList(new AttributeValue().withS(host));

			Map<String, Condition> keyConditions = new HashMap<String, Condition>();
			keyConditions.put("storeName", hashKeyCondition);

			QueryRequest queryRequest = new QueryRequest()
					.withTableName("MenuLink").withKeyConditions(keyConditions)
					.withLimit(80).withExclusiveStartKey(lastEvaluatedKey);

			QueryResult result = client.query(queryRequest);

			for (Map<String, AttributeValue> item : result.getItems()) {
				String hash = null;
				String range = null;
				String status = null;
				String final_range = null;
				Map<String, AttributeValue> attributeList = item;

				for (Map.Entry<String, AttributeValue> item1 : attributeList
						.entrySet()) {
					String attributeName = item1.getKey();
					AttributeValue value = item1.getValue();
					// System.out.print(attributeName + " "
					// + (value.getS() == null ? "" : "S=[" + value.getS() +
					// "]"));
					if (attributeName.equals("storeName")) {
						hash = value.getS();
						// System.out.println("hash is " + hash);
					}
					if (attributeName.equals("url")) {
						range = value.getS();
						if(!range.toString().equals("http://"+host)&&!range.toString().equals("http://"+host+"#")){
							final_range=value.getS();
						}
						// System.out.println("range is " + range);
					}
					if (attributeName.equals("status")) {
						status = value.getS();
						// System.out.println("status" + status);
					}

				}// inner for close
				if (status.equals("notDone"))
					tempStoreLinks.add(final_range);

			}
			lastEvaluatedKey = result.getLastEvaluatedKey();
		} while (lastEvaluatedKey != null);

		// returning map for storeLinks
		mapToReturn.put(host, tempStoreLinks);
		System.out.println("return hashmap is ...." + mapToReturn);
		return mapToReturn;

	}

	private static Map<String, String> scanStoreLinkTable() throws IOException {
		
		
		// List to return
		
			createClient();
		Map<String, String> mapToReturn = new HashMap<String, String>();
		Map<String, AttributeValue> lastEvaluatedKey = null;

		do {

			Condition hashKeyCondition = new Condition()
					.withComparisonOperator(ComparisonOperator.EQ.toString())
					.withAttributeValueList(
							new AttributeValue().withS("notDone"));

			Map<String, Condition> keyConditions = new HashMap<String, Condition>();
			keyConditions.put("status", hashKeyCondition);

			QueryRequest queryRequest = new QueryRequest()
					.withTableName("StoreLink")
					.withKeyConditions(keyConditions)
					.withExclusiveStartKey(lastEvaluatedKey);

			QueryResult result = client.query(queryRequest);
			for (Map<String, AttributeValue> item : result.getItems()) {
//				System.out.println("////////////////////////////");
				String hash = null;
				String range = null;
				String storeName = null;
				Map<String, AttributeValue> attributeList = item;
				for (Map.Entry<String, AttributeValue> item1 : attributeList
						.entrySet()) {
					String attributeName = item1.getKey();
					AttributeValue value = item1.getValue();
					System.out.print(attributeName
							+ " "
							+ (value.getS() == null ? "" : "S=[" + value.getS()
									+ "]"));
					if (attributeName.equals("status")) {
						hash = value.getS();
					}
					if (attributeName.equals("url")) {
						range = value.getS();
					}
					if (attributeName.equals("storeName")) {
						storeName = value.getS();
					}

				}// inner for close

				mapToReturn.put(range, storeName);
			}
			lastEvaluatedKey = result.getLastEvaluatedKey();
		} while (lastEvaluatedKey != null);

		return mapToReturn;
	}

	private static void updateStatusToDoing(String url, String store) {

		// for (Map.Entry<String, String> item : storeList.entrySet()) {

		String hashName = "status";
		String hashValue = "notDone";
		String newHashValue = "doing";
		String rangeName = "url";
		String rangeValue = url;
		String columnName = "storeName";
		String columnValue = store;
		String tableName = "StoreLink";
		try {
			createClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		deleteItem(hashName, hashValue, rangeName, rangeValue, tableName);
		// function for inserting item into table
		InsertIntoTable(hashName, newHashValue, rangeName, rangeValue,
				columnName, columnValue, tableName);

		// } // for close
	}

	private static void InsertIntoTable(String hashName, String newHashValue,
			String rangeName, String rangeValue, String columnName,
			String columnValue, String tableName) {
		try {
			Map<String, AttributeValue> item1 = new HashMap<String, AttributeValue>();
			item1.put(hashName, new AttributeValue().withS(newHashValue));
			item1.put(rangeName, new AttributeValue().withS(rangeValue));
			item1.put(columnName, new AttributeValue().withS(columnValue));

			PutItemRequest putItemRequest1 = new PutItemRequest()
					.withTableName(tableName).withItem(item1);
			PutItemResult result1 = client.putItem(putItemRequest1);
			System.out.println("status of " + rangeValue
					+ " is updated to Doing");

		} catch (AmazonServiceException ase) {
			System.err.println("Create items failed.");
		}

	}// function close

	// function for deleting from dynamodb
	private static void deleteItem(String hashName, String hashValue,
			String rangeName, String rangeValue, String tableName) {

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
			System.out.println(rangeValue + " is deleted");

		} catch (AmazonServiceException ase) {
			System.err
					.println("Failed to get item after deletion " + tableName);
		}
	}// function close

	static void changeStatustoDone(Logger logger) {
		// List to return
		try {
			createClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, AttributeValue> lastEvaluatedKey = null;

		do {

			Condition hashKeyCondition = new Condition()
					.withComparisonOperator(ComparisonOperator.EQ.toString())
					.withAttributeValueList(new AttributeValue().withS("done"));

			Map<String, Condition> keyConditions = new HashMap<String, Condition>();
			keyConditions.put("status", hashKeyCondition);
			// keyConditions.put("url", rangeCondition);

			QueryRequest queryRequest = new QueryRequest()
					.withTableName("Slave").withKeyConditions(keyConditions)
					.withExclusiveStartKey(lastEvaluatedKey);

			QueryResult result = client.query(queryRequest);
			for (Map<String, AttributeValue> item : result.getItems()) {
				String SlaveIP = "";
				Map<String, AttributeValue> attributeList = item;
				for (Map.Entry<String, AttributeValue> item1 : attributeList
						.entrySet()) {
					if (item1.getKey().equals("slaveIp")) {
						SlaveIP = item1.getValue().getS();
					}

				}// inner while loop
				fun2.startProcessThread("killing-selenium-slave.sh", SlaveIP,logger);
				deleteItem("status", "done", "slaveIp", SlaveIP, "Slave");

			}// outter for close
			lastEvaluatedKey = result.getLastEvaluatedKey();
		} while (lastEvaluatedKey != null);
	}// function is closed
}// class close