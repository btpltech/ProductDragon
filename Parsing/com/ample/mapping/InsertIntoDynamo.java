package com.ample.mapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;


public class InsertIntoDynamo {
	static AmazonDynamoDBClient client;
	static Logger logger;
	public static Configuration confh = null;
	private static void createClient() throws IOException {

		AWSCredentials credentials = new PropertiesCredentials(
				InsertIntoDynamo.class
						.getResourceAsStream("AwsCredentials.properties"));
		client = new AmazonDynamoDBClient(credentials);
		// calling function

	}
	public static void main(String args[]){
		Logger logger = Logger.getLogger("MyLog");
		FileHandler fh;
		try {

			// This block configure the logger with handler and formatter
			fh = new FileHandler("Testing.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

			// the following statement is used to log any messages
			logger.info("Iserting Log File");

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			createClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Insert(args[0],logger);
		
	}
	
	public static void Insert(String hbaseIP,Logger logger){
		confh = HBaseConfiguration.create();

		confh.set("hbase.zookeeper.quorum", hbaseIP);
		confh.set("hbase.zookeeper.property.clientPort", "2181");
		confh.set("hbase.master", hbaseIP + ":60000");
		
		try {
			HTable table = new HTable(confh, "parsing_result");
			Scan s = new Scan();
			ResultScanner rs;			
			rs = table.getScanner(s);
			logger.info("Inserting into dynamo started");
			
			for (Result r : rs) {
				for (KeyValue kv : r.raw()) {
					Map<String, AttributeValue> return_hashMap = new HashMap<String, AttributeValue>();

					return_hashMap.put("storeName",
							new AttributeValue().withS(new String(kv.getQualifier())));
					return_hashMap.put("url",
							new AttributeValue().withS(new String(kv.getRow())));

					return_hashMap.put("data",
							new AttributeValue().withS(new String(kv.getValue())));
					try {
						PutItemRequest putItemRequest1 = new PutItemRequest()
								.withTableName("ParsedProducts").withItem(
										return_hashMap);
						PutItemResult result1 = client.putItem(putItemRequest1);

					} //end of try block for put into dynamo
					catch (AmazonServiceException ase) {
						System.err.println("Create items failed.");
						logger.info("error occurs during insertion into dynamo");
						
					}	// end of catch block of amazon				
				   } //end of kv for
				} // end of result r for
			logger.info("Inserting into dynamo ended");
		} // end of try block 
		catch (IOException e) {
			e.printStackTrace();
		} //end of catch block
	} //end of Insert method
}// end of main class	