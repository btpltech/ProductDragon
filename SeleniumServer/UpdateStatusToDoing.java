import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;


public class UpdateStatusToDoing {
	static AmazonDynamoDBClient client;
	private static void createClient() throws IOException {

		AWSCredentials credentials = new PropertiesCredentials(
				SeleniumServer.class
						.getResourceAsStream("AwsCredentials.properties"));
		client = new AmazonDynamoDBClient(credentials);

	} // create client is closed
	public static void main(String args[]){
		String filename=args[0];
		String IP=args[1];
		String TableName="Slave";
		String HashName="status";
		String HashValue="doing";
		String RangeName="slaveIp";
		String RangeValue=IP;	
		String ColumnName="file";
		String ColumnValue=filename;
		
		Insert(TableName,HashName,HashValue,RangeName,RangeValue,ColumnName,ColumnValue);
//		 thread_pagination.updateStatusToDone(IP,filename);   
//	      String host=thread_pagination.fileread(filename);
		
	}
	private static void Insert(String tableName, String hashname,
			String hashvalue, String rangeName, String rangevalue,
			String columnName, String columnValue) {
		try {
			createClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map<String, AttributeValue> item1 = new HashMap<String, AttributeValue>();
		item1.put(hashname, new AttributeValue().withS(hashvalue));
		item1.put(rangeName, new AttributeValue().withS(rangevalue));
		item1.put(columnName, new AttributeValue().withS(columnValue));

		PutItemRequest putItemRequest1 = new PutItemRequest().withTableName(
				tableName).withItem(item1);
		PutItemResult result1 = client.putItem(putItemRequest1);
		System.out.println("status of " + rangevalue + " is updated to Doing");
	}
	

}
