import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;


public class UpdateStatusToDone {
	static AmazonDynamoDBClient client;
	private static void createClient() throws IOException {

		AWSCredentials credentials = new PropertiesCredentials(
				SeleniumServer.class
						.getResourceAsStream("AwsCredentials.properties"));
		client = new AmazonDynamoDBClient(credentials);

	} // create client is closed
	public static void main(String args[]){
		try {
			createClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String filename=args[0];
		String IP=args[1];
//		String IP=args[2];
////		String host="http://"+store;
		 updateStatusToDone(IP,filename);   
//	      String host=thread_pagination.fileread(filename);
//	     updateStatusofStoreLink(host,store);
		
	}
	static void updateStatusToDone(String IP,String filename) {

//		for (Map.Entry<String, String> item : storeList.entrySet()) {
		

			String hashName = "status";
			String hashValue = "doing";
			String rangeName = "slaveIp";
			String rangeValue = IP;
			String columnValue = filename;
			String tableName = "Slave";

			deleteItem(hashName, hashValue, rangeName, rangeValue, tableName);
			// function for inserting item into table
			Map<String, AttributeValue> return_hashMap = new HashMap<String, AttributeValue>();

			return_hashMap.put("status", new AttributeValue().withS("done"));
			return_hashMap
					.put("slaveIp", new AttributeValue().withS(rangeValue));

			return_hashMap.put("file",
					new AttributeValue().withS(columnValue));
			PutItemRequest putItemRequest1 = new PutItemRequest()
			.withTableName(tableName).withItem(return_hashMap);
	PutItemResult result1 = client.putItem(putItemRequest1);

//		} // for close
	}
	static void deleteItem(String hashName, String hashValue,
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
//	static void updateStatusofStoreLink(String url,String storename) {
//
////		for (Map.Entry<String, String> item : storeList.entrySet()) {
//
//			String hashName = "status";
//			String hashValue = "doing";
//			String newHashValue = "done";
//			String rangeName = "url";
//			String rangeValue = url;
//			String columnName = "storeName";
//			String columnValue = storename;
//			String tableName = "StoreLink";
//
//			thread_pagination.deleteItem(hashName, hashValue, rangeName, rangeValue, tableName);
//			// function for inserting item into table
//			Map<String, AttributeValue> return_hashMap = new HashMap<String, AttributeValue>();
//
//			return_hashMap.put(hashName, new AttributeValue().withS(newHashValue));
//			return_hashMap
//					.put(rangeName, new AttributeValue().withS(rangeValue));
//
//			return_hashMap.put(columnName,
//					new AttributeValue().withS(columnValue));
//			thread_pagination.InsertIntoTable(tableName, return_hashMap);
////		} // for close
//	}
//

}