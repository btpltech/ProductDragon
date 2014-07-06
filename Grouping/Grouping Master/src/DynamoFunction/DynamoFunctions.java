package DynamoFunction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.google.gson.Gson;

public class DynamoFunctions {
	static String tableName = "HashContent";
	static AmazonDynamoDBClient client;	
	public static void main(String[] args) throws IOException {
		createClient();
		 Map<String,ArrayList<String>> mapOfProductsInHash = new HashMap<String,ArrayList<String>>();
System.out.println(".....");
		 //mapOfProductsInHash = queryTable("color");
		 //System.out.println(mapOfProductsInHash);
//scanTable();
String attr = "\"attribute:{\"product_title\":\"samsung\"}";

EntryInClusterTableInDynamodb("AmpleCluster","http://flipkart.com/books/123",attr,"flipkart.com");
	}
	
	public static void EntryInClusterTableInDynamodb(String tableName,
			String url, String data,String storeName) {

		Map<String,String> mapOfProductsInHash = new HashMap<String,String>();	
		Map<String, AttributeValue> lastEvaluatedKey = null;

	    do {
			
			Condition hashKeyCondition = new Condition()
		    .withComparisonOperator(ComparisonOperator.EQ.toString())
		    .withAttributeValueList(new AttributeValue().withS(url));
		
			Map<String, Condition> keyConditions = new HashMap<String, Condition>();
			keyConditions.put("url", hashKeyCondition);
			
			QueryRequest queryRequest = new QueryRequest()
		    .withTableName(tableName)
		    .withKeyConditions(keyConditions)
		    .withLimit(50)
		    .withAttributesToGet(Arrays.asList("count","attribute"))
		    .withExclusiveStartKey(lastEvaluatedKey);
			
		    QueryResult result = client.query(queryRequest);
		    for (Map<String, AttributeValue> item : result.getItems()) {
		    	
		    	
		    	ArrayList<String> temp = new ArrayList<String>();
		    	Map<String, AttributeValue> attributeList=item;
		    	String count = null;
		    	String attribute = null;
		        for (Map.Entry<String, AttributeValue> item1 : attributeList.entrySet()) {
		            String attributeName = item1.getKey();
		            AttributeValue value = item1.getValue();
	
		           mapOfProductsInHash.put(attributeName,value.getS());
		        }//inner for close
		        
		       
		        
		    }
		    
	    lastEvaluatedKey = result.getLastEvaluatedKey();
	} while (lastEvaluatedKey != null);
		//do-while close

System.out.println(mapOfProductsInHash);
if(!mapOfProductsInHash.isEmpty())
{
  // call function to combine attribute from dynamodb and 

  String newjson = CombineTwoHashMaps(mapOfProductsInHash.get("attribute"),mapOfProductsInHash.get("attribute"));

  String count = mapOfProductsInHash.get("count");
  count = ""+((Integer.parseInt(count))+1);
  
  try {
      Map<String, AttributeValueUpdate> updateItems = 
          new HashMap<String, AttributeValueUpdate>();

      HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put("url", new AttributeValue().withS(url));
		key.put("range", new AttributeValue().withS("cf"));
	      
      // Add two new authors to the list.
      updateItems.put("storeName"+count, 
              new AttributeValueUpdate()
                  .withValue(new AttributeValue().withS(storeName)));
     
      // Add a new attribute.
      updateItems.put("count", 
              new AttributeValueUpdate()
			      .withValue(new AttributeValue().withS(count)));
			      
      updateItems.put("attribute", 
              new AttributeValueUpdate()
      			  .withValue(new AttributeValue().withS(newjson)));
      
      
      UpdateItemRequest updateItemRequest = new UpdateItemRequest()
          .withTableName(tableName)
          .withKey(key)
          .withAttributeUpdates(updateItems);
      
      UpdateItemResult result = client.updateItem(updateItemRequest);
      
      
  }   catch (AmazonServiceException ase) {
      System.out.println(ase);
	  System.err.println("Failed to update multiple attributes in " + tableName);
  }  
}// if close
else
{
	// just add it
	try 
	{
    Map<String, AttributeValue> putMap = new HashMap<String, AttributeValue>();
    putMap.put("url", new AttributeValue().withS(url));
    putMap.put("range", new AttributeValue().withS("cf"));
    
    putMap.put("storeName1", new AttributeValue().withS(storeName));
    putMap.put("count", new AttributeValue().withS("1"));
    putMap.put("attribute", new AttributeValue().withS(data));
            
        
    // create put object
    
    PutItemRequest putItemRequest = new PutItemRequest()
    .withTableName(tableName)
    .withItem(putMap);
    
    PutItemResult result = client.putItem(putItemRequest);

     }// try close 
		catch (AmazonServiceException ase) {
 
        }// catch close 
	
} // else close
}
	
	//--------------------------------------------------------------------------
    // Function for CombineTwoHashMaps
	//--------------------------------------------------------------------------
private static String CombineTwoHashMaps(String data1, String data2) {
	
	
	Gson g = new Gson();
	
    Map  temp1 = new HashMap();
    Map  temp2 = new HashMap();
    Map  temp3 = new HashMap();
    
    temp1 = g.fromJson(data1, Map.class);
    temp2 = g.fromJson(data2, Map.class);
    
	if(temp1.containsKey("attributes"))
	{
		Map  temp4 = new HashMap();
	    temp4 = g.fromJson(temp1.get("attributes").toString(), Map.class);
	    
	    if(temp2.containsKey("attributes"))
	    {
	    	Map  temp5 = new HashMap();
	    	temp5 = g.fromJson(temp2.get("attributes").toString(), Map.class);
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

private static Map<String, ArrayList<String>> quwoeryTable(String string) {
	 Map<String,ArrayList<String>> mapOfProductsInHash = new HashMap<String,ArrayList<String>>();
System.out.println(".........");

	 Map<String, AttributeValue> lastEvaluatedKey = null;
	 
	    do {
		
			Condition hashKeyCondition = new Condition()
		    .withComparisonOperator(ComparisonOperator.EQ.toString())
		    .withAttributeValueList(new AttributeValue().withS("color"));
		
			Map<String, Condition> keyConditions = new HashMap<String, Condition>();
			keyConditions.put("hash", hashKeyCondition);
			
			QueryRequest queryRequest = new QueryRequest()
		    .withTableName("HashContent")
		    .withKeyConditions(keyConditions)
		    .withLimit(50)
		    .withExclusiveStartKey(lastEvaluatedKey);
		
		    QueryResult result = client.query(queryRequest);
		    for (Map<String, AttributeValue> item : result.getItems()) {
		    	String hash=null;
		    	String range = null;
		    	String storeName = null;
		    	ArrayList<String> temp = new ArrayList<String>();
		    	Map<String, AttributeValue> attributeList=item;
		        for (Map.Entry<String, AttributeValue> item1 : attributeList.entrySet()) {
		            String attributeName = item1.getKey();
		            AttributeValue value = item1.getValue();
		            System.out.print(attributeName + " "
		                   + (value.getS() == null ? "" : "S=[" + value.getS() + "]"));
		            if(!attributeName.equals("count") && !attributeName.equals("hash") && !attributeName.equals("range"))
		            temp.add(value.getS());
		            
		         }//inner for close
		        
		       
		        mapOfProductsInHash.put(string,temp);   
		    }
		    
	    lastEvaluatedKey = result.getLastEvaluatedKey();
	} while (lastEvaluatedKey != null);
		//do-while close

		return mapOfProductsInHash;
	}


public static void scanTable()
{

	 Map<String, AttributeValue> lastKeyEvaluated = null;
	 
	    do {
	    	Condition scanFilterCondition = new Condition()
            .withComparisonOperator(ComparisonOperator.EQ.toString())
            .withAttributeValueList(new AttributeValue().withS("www.flipkart.com"));
    	
        Map<String, Condition> conditions = new HashMap<String, Condition>();
        conditions.put("storeName", scanFilterCondition);
			
	    	ScanRequest scanRequest = new ScanRequest()
		        .withTableName("ProductLink")
		        .withLimit(50)
		        .withScanFilter(conditions)
		        //give the list of attributes here in the below line of code
		        //.withAttributesToGet(Arrays.asList("attributename1","attribure_name2" and so on...))
		        .withExclusiveStartKey(lastKeyEvaluated);
		    
	        ScanResult result = client.scan(scanRequest);
		     
	        for (Map<String, AttributeValue> item : result.getItems()){
	        	String hash=null;
	        	String range = null;
	        	Map<String, AttributeValue> attributeList=item;
		        for (Map.Entry<String, AttributeValue> item1 : attributeList.entrySet()) {
		            String attributeName = item1.getKey();
		            AttributeValue value = item1.getValue();
		            System.out.print(attributeName + " "
		                    + (value.getS() == null ? "" : "S=[" + value.getS() + "]"));
		            

		            
		          }//inner for close
		        
		    	   // Move to next line
		        System.out.println();
		   // deleteItems1(cluster_id,tableName);
	        }//outer for close
		    lastKeyEvaluated = result.getLastEvaluatedKey();
		     
		} while (lastKeyEvaluated != null);
		//do-while close
}// scan function close
private static void createClient() throws IOException {
    AWSCredentials credentials = new PropertiesCredentials(
    		DynamoFunctions.class.getResourceAsStream("AwsCredentials.properties"));
    client = new AmazonDynamoDBClient(credentials);
   
}
public static void deleteItems(String storeName,String range, String tableName) 
{
try {
        Map<String, ExpectedAttributeValue> expectedValues = new HashMap<String, ExpectedAttributeValue>();
        HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put("status", new AttributeValue().withS(storeName));
		key.put("url", new AttributeValue().withS(range));
        DeleteItemRequest deleteItemRequest = new DeleteItemRequest()
            .withTableName(tableName)
            .withKey(key)
            .withExpected(expectedValues);
       DeleteItemResult result = client.deleteItem(deleteItemRequest);
        // Check the response.
         System.out.println(storeName+" is deleted");
                                
    }  catch (AmazonServiceException ase) {
                            System.err.println("Failed to get item after deletion " + tableName);
    } 
}//function close
}
