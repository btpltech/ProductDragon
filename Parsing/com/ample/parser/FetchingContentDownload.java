package com.ample.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
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
import com.amazonaws.services.identitymanagement.model.UpdateAccessKeyRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class FetchingContentDownload {
	static AmazonDynamoDBClient client;
	public static void main(String[] args) throws IOException {

		
		// create client
		createClient();
		
		String path = null;
		String status = null;
		String newStatus = null;
		
		try{
		path = args[0];
		status = args[1];
		newStatus = args[2];
		String accessKey = "AKIAJMAEQRZSCGU3JZTA";
    	String secretKey = "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";
        String bucketName = "FetchedData";
        String key = "text";
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3 = new AmazonS3Client(credentials);
		Region usWest2 = Region.getRegion(Regions.US_EAST_1);
		s3.setRegion(usWest2);
        

	 System.out.println("Listing objects");
     ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
             .withBucketName(bucketName));
             
     for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
         System.out.println(" - " + objectSummary.getKey() + "  " +
                            "(size = " + objectSummary.getSize() + ")");
     }
     
     ArrayList<String> listofKeys = new ArrayList<String>();
     listofKeys = scanTableFromDynamo("FetchedFileStore",status,newStatus);  // scan table from FetchedFileStore
     for (int i = 0; i < listofKeys.size(); i++) {

         System.out.println();
         System.out.println("Downloading an object");
         ObjectMetadata object = s3.getObject(new GetObjectRequest(bucketName, listofKeys.get(i)), new File(path+"/"+listofKeys.get(i)));
//         System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
//         displayTextInputStream(object.getObjectContent());

	 }// for close

		}
		catch(Exception e){System.out.println("one of the args is missing \n 1. path \n 2. statusKey \n 3. newStatusKey");}
		
	}

	private static void createClient() throws IOException {

		AWSCredentials credentials = new PropertiesCredentials(
				FetchingContentDownload.class
						.getResourceAsStream("AwsCredentials.properties"));
		client = new AmazonDynamoDBClient(credentials);
		// calling function

	}
	 private static void displayTextInputStream(InputStream input) throws IOException {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	        while (true) {
	            String line = reader.readLine();
	            if (line == null) break;

	            System.out.println("    " + line);
	        }
	        System.out.println();
	    }
private static ArrayList<String> scanTableFromDynamo(String tableName,String status,String newStatus)
{
	Map<String, ArrayList<String>> mapToReturn = new HashMap<String, ArrayList<String>>();
	ArrayList<String> tempStoreLinks = new ArrayList<String>();

	Map<String, AttributeValue> lastEvaluatedKey = null;
	ArrayList<String> listofkeys = new ArrayList<String>();
	int count = 0;
	do 
	{
	Condition hashKeyCondition = new Condition()
				.withComparisonOperator(ComparisonOperator.EQ.toString())
				.withAttributeValueList(
						new AttributeValue().withS(status));
		Map<String, Condition> keyConditions = new HashMap<String, Condition>();
		keyConditions.put("status", hashKeyCondition);
		QueryRequest queryRequest = new QueryRequest()
				.withTableName(tableName)
				.withKeyConditions(keyConditions)
				.withLimit(500)
				.withExclusiveStartKey(lastEvaluatedKey);

		QueryResult result = client.query(queryRequest);

		for (Map<String, AttributeValue> item : result.getItems()) {
			Map<String, AttributeValue> attributeList = item;
			for (Map.Entry<String, AttributeValue> item1 : attributeList
					.entrySet()) {
				String attributeName = item1.getKey();
				AttributeValue value = item1.getValue();
				// System.out.print(attributeName + " "
				// + (value.getS() == null ? "" : "S=[" + value.getS() +
				// "]"));
				if (attributeName.equals("fileName")) {
				   listofkeys.add(value.getS());
				}
				}// inner for close
			
		}
		lastEvaluatedKey = result.getLastEvaluatedKey();
} while (lastEvaluatedKey != null);
	
	for(int i = 0;i<listofkeys.size(); i++)
	{
		deleteItem("status", status, "fileName", listofkeys.get(i), tableName);
	InsertIntoTable("status", newStatus, "fileName", listofkeys.get(i), tableName);
	}
	return listofkeys;
}
private static void InsertIntoTable(String hashName, String newHashValue,
		String rangeName, String rangeValue,String tableName) {
	try {
		Map<String, AttributeValue> item1 = new HashMap<String, AttributeValue>();
		item1.put(hashName, new AttributeValue().withS(newHashValue));
		item1.put(rangeName, new AttributeValue().withS(rangeValue));

		PutItemRequest putItemRequest1 = new PutItemRequest()
				.withTableName(tableName).withItem(item1);
		PutItemResult result1 = client.putItem(putItemRequest1);
//		System.out.println(">>>>>>>>>>>>.status of " + rangeValue
//				+ " is updated");

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
		//System.out.println("->->->->" + rangeValue + " is deleted");

	} catch (AmazonServiceException ase) {
		System.err
				.println("Failed to get item after deletion " + tableName);
	}
}// function close
}// class close