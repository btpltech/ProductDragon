package com.ample.parser;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;


public class GetDataFromDynamo {
	static AmazonDynamoDBClient client;
	private static void createClient() throws IOException {

		AWSCredentials credentials = new PropertiesCredentials(
				GetDataFromDynamo.class
						.getResourceAsStream("AwsCredentials.properties"));
		client = new AmazonDynamoDBClient(credentials);

	} // create client is closed
	public static void main(String args[]) throws IOException{
		
		RedWrite.creatTablehasonefamily("entity_info","cf",args[0]);
	try {
		createClient();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
//	do {

		ScanRequest scanRequest = new ScanRequest()
		.withTableName("entity_info");
             ScanResult result = client.scan(scanRequest);

		for (Map<String, AttributeValue> item : result.getItems()) {
//			System.out.println("////////////////////////////");
			String host_name = null;
			String json = null;
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
				if (attributeName.equals("host_name")) {
					host_name = value.getS();
				}
				if (attributeName.equals("value")) {
					json= value.getS();
				}
			}// inner for close
			try {
				RedWrite.addRecord("entity_info",host_name,"cf","cf",json,args[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		lastEvaluatedKey = result.getLastEvaluatedKey();
//	} while (lastEvaluatedKey != null);
	}


}
