

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;


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
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;


public class Functions {
	// function for scanning data
static AmazonDynamoDBClient client;
	public static void main(String[] args) throws IOException {
		createClient();
//		FileWrite("/home/amit/Desktop/a.txt", "content\n");
//        try {
//			MakeTarFile("/home/amit/Desktop/data","/home/amit/Desktop/test.txt.zip");
//		} catch (ArchiveException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//Functions.updateStatus1("status","busy","free","slaveIp",SlaveIp,"processingFileName",pathToUpdate,"FetchingStatus",client);
		//updateStatus1("status", "busy", "free", "slaveIp", "184.72.209.92", "processingFileName", "/home/data/", "FetchingStatus", client);
		//ReadDataFromDyanmo("ProductLink","status","notDone","url","storeName",201,client);
		String pathOfFile = "/home/amit/Desktop/a1.seq";
		Path path = new Path(pathOfFile);
		 Configuration conf = new Configuration();
		 org.apache.hadoop.fs.RawLocalFileSystem fs = new org.apache.hadoop.fs.RawLocalFileSystem();
		 fs.setConf(conf);
	      Text key = new Text();
	      Text value = new Text();
		 try {
					SequenceFile.Writer writer = SequenceFile.createWriter(fs,conf, path,
							    key.getClass(), value.getClass());
					for(int i = 0;i<10;i++)
					{
						String temp = Integer.toString(i);
						String temp1 = Integer.toString(i+1);
						 key = new Text(temp);
						 value = new Text(temp1);
						 
					writer.append(key, value);
					}
					writer.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
		}
		 	
	
	 private static void createClient() throws IOException {

			AWSCredentials credentials = new PropertiesCredentials(
					FetcherManager.class
							.getResourceAsStream("AwsCredentials.properties"));
			client = new AmazonDynamoDBClient(credentials);
		}

	public static Map<String,String> ReadDataFromDyanmo(String tableName,String hashName,String hashValue,String rangeName,String columnName,int dblimit,AmazonDynamoDBClient client) {
		//System.out.println("get");
		Map<String, String> mapToReturn = new HashMap<String, String>();

		Map<String, AttributeValue> lastEvaluatedKey = null;
		int countTObreak = 0;
		do {

		Condition hashKeyCondition = new Condition().withComparisonOperator(
				ComparisonOperator.EQ.toString()).withAttributeValueList(
				new AttributeValue().withS(hashValue));

		Map<String, Condition> keyConditions = new HashMap<String, Condition>();
		keyConditions.put(hashName, hashKeyCondition);
		//System.out.println("DB Limit : " + dblimit);
		QueryRequest queryRequest = new QueryRequest().withTableName(tableName)
				.withKeyConditions(keyConditions)
				.withExclusiveStartKey(lastEvaluatedKey).withLimit(250); // here changes are to be made as 250 for 2

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
//				 System.out.println(attributeName + " "
//				 + (value.getS() == null ? "" : "S=[" + value.getS() +
//				 "]"));
//			
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

			countTObreak++;
			if(countTObreak<=dblimit)
			mapToReturn.put(range, storeName);
			//System.out.println(countTObreak);
		}

		lastEvaluatedKey = result.getLastEvaluatedKey();
		
		} while (lastEvaluatedKey != null && countTObreak<=dblimit);

		 return mapToReturn;
	}// functin close
		
	// function for update status to done
    private static void updateStatusToDone(Map<String, String> parsedData,
			String tableName,AmazonDynamoDBClient client) {
		String hashName = "status";
		String hashValue = "doing";
		String newHashValue = "done";
		String rangeName = "url";
		String columnName = "storeName";

		for (Map.Entry<String, String> item : parsedData.entrySet()) {
			String[] DATA = item.getValue().toString().split("~!~");
			String storeName = DATA[0];
			String columnValue = storeName;
			String rangeValue = item.getKey();
			// functin for deleting item is called here
			deleteItem(hashName, hashValue, rangeName, rangeValue, tableName,client);
			// function for inserting item into table
			InsertIntoTable(hashName, newHashValue, rangeName, rangeValue,
					columnName, columnValue, tableName,client);

		}// for close
	}

	// function close
	public static void updateStatus(String hashName,String hashValue,String newHashValue,String rangeName,String rangeValue,
			String columnName,String columnValue, String tableName,AmazonDynamoDBClient client) {
		// functin for deleting item is called here
		
		deleteItem(hashName, hashValue, rangeName, rangeValue, tableName,client);
		// function for inserting item into table
		InsertIntoTable(hashName, newHashValue, rangeName, rangeValue,
				columnName, columnValue, tableName,client);
	} // function close

	// function for inserting into table
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
	
	// function for writing 
	public static void FileWrite(String path,String content)
	{
//			
		File file = new File(path);
//			file.setWritable(true);
//			
//			try {
//				FileOutputStream fop = new FileOutputStream(file);
//			
//				// if file doesn't exists, then create it
//				if (!file.exists()) {
//					file.createNewFile();
//				}
////				// get the content in bytes
////				byte[] contentInBytes = content.getBytes();
////				fop.write(contentInBytes);
////				fop.flush();
////				fop.close();
//				//true = append file
//	    		FileWriter fileWritter = new FileWriter(file,true);
//	    	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
//	    	        bufferWritter.write(content);
//	    	        bufferWritter.close();
//				//System.out.println("Content :\""+content+"\" is written in File: "+path);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		try
		{
		    String filename= path;
		    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
		    fw.write(content);//appends the string to the file
		    fw.close();
		}
		catch(IOException ioe)
		{
		    System.err.println("IOException: " + ioe.getMessage());
		}
	}// write function is closed
	// function for fileRead
	public static Map<String,String> FileRead(String path)
	{
		Map<String,String> mapOfUrlsAndStoreName = new HashMap<String,String>();
	String[] data = null;
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(path));
         	String sCurrentLine;
     
			while ((sCurrentLine = br.readLine()) != null) {
			 data = sCurrentLine.split(" ");
			 //make map
			 // in data[0] url and in data[1] storeName
			 mapOfUrlsAndStoreName.put(data[0], data[1]);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
 return mapOfUrlsAndStoreName;
	}// read function is closed

	// check that slaves are free 
public static Map<String,String> ReadFromDyanmo(String tableName, String status, AmazonDynamoDBClient client) {
	
	Map<String,String> mapOfSlaves = new HashMap<String,String>();
	
	Condition hashKeyCondition = new Condition().withComparisonOperator(
			ComparisonOperator.EQ.toString()).withAttributeValueList(
			new AttributeValue().withS(status));

	Map<String, Condition> keyConditions = new HashMap<String, Condition>();
	keyConditions.put("status", hashKeyCondition);
	
	QueryRequest queryRequest = new QueryRequest().withTableName(tableName)
			.withKeyConditions(keyConditions);
			

	QueryResult result = client.query(queryRequest);
	for (Map<String, AttributeValue> item : result.getItems()) {
		String slaveIp = null;
		String processingFileName = null;
		Map<String, AttributeValue> attributeList = item;
		for (Map.Entry<String, AttributeValue> item1 : attributeList
				.entrySet()) {
			String attributeName = item1.getKey();
			AttributeValue value = item1.getValue();
			// System.out.print(attributeName + " "
			// + (value.getS() == null ? "" : "S=[" + value.getS() +
			// "]"));
			if (attributeName.equals("slaveIp")) {
				slaveIp = value.getS();
			}
			if (attributeName.equals("processingFileName")) {
				processingFileName = value.getS();
			}

		}// inner for close
mapOfSlaves.put(slaveIp,processingFileName);
	
		
	}// main for close

	return mapOfSlaves;
	}// fn close
//process builder
public static void ProcessBuilder(String scriptData, String string, String string2, String pathToScp, String string3) {
	ProcessBuilder pb = new ProcessBuilder(scriptData,string,string2,pathToScp,string3);
		Process p = null;
	try {
		p = pb.start();
		} 
	catch (IOException e) 
		{
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		try {
			while((line = reader.readLine()) != null)
			{
				System.out.println(line);
			}// while close
			}// try close 
		catch (IOException e) 
		{
		e.printStackTrace();
	}// catch close
	
}

// function for making tar file
//public static void MakeTarFile(String dirName, String tarLocation) throws ArchiveException, IOException {
//
//	 MakeZip zippy = new MakeZip();
//     try {
//         zippy.zipDir(dirName,tarLocation);
//     } catch(IOException e2) {
//         System.err.println(e2);
//     }
//}// fn close


// function for uploading file in S3
public static void UploadInS3(String string, String dirName, String fileName) {

	String accessKey = "AKIAJMAEQRZSCGU3JZTA";
	String secretKey = "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";
    String bucketName = "FetchedData";
    //String key = fileName;
    
	AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
    AmazonS3 s3 = new AmazonS3Client(credentials);
	Region usWest2 = Region.getRegion(Regions.US_EAST_1);
	s3.setRegion(usWest2);
    System.out.println("Uploading a new object to S3 from a file\n");
//    File f1 = new File(dirName);
//    ArrayList<String> names = new ArrayList<String>(Arrays.asList(f1.list()));
//    //System.out.println(names);
    
    
    File f2 = new File(dirName);
    ArrayList<String> names = new ArrayList<String>(Arrays.asList(f2.list()));
    System.out.println(names);
    for(int i = 0 ; i< names.size(); i++)
    {
    	System.out.println("Loading...");
    	System.out.println("Uploading a new object to S3 from a file"+dirName+names.get(i));
    	s3.putObject(bucketName,names.get(i), new File(dirName+names.get(i)));
    	System.out.println("Uploading a new object to S3 from a file is done");
    }
    
}
// function for removing files
public static void deleteFile(String path) {
	try{
		 
		File file = new File(path);
        file.setExecutable(true);
        file.setWritable(true);
        file.setReadable(true);
		if(file.delete()){
			System.out.println(file.getName() + " is deleted!");
		}else{
			System.out.println("Delete operation is failed.");
		}

	}catch(Exception e){

		e.printStackTrace();

	}
}

// Function for inserting in Dynmo
public static void WriteInDynamo(String tableName, String hashName, String rangeName,
		String hashValue, ArrayList<String> listofKeys, AmazonDynamoDBClient client) {
	try {
    	 for(int i = 0; i<listofKeys.size();i++)
		 {
			Map<String, AttributeValue> item1 = new HashMap<String, AttributeValue>();
	        item1.put(hashName, new AttributeValue().withS(hashValue));
	        item1.put(rangeName, new AttributeValue().withS(listofKeys.get(i)));
	        
	        PutItemRequest putItemRequest1 = new PutItemRequest()
	        .withTableName(tableName)
	        .withItem(item1);
	        PutItemResult result1 = client.putItem(putItemRequest1);
			 
		 }// for close
            } catch (AmazonServiceException ase) {
            	System.out.println(ase);
                System.err.println("Create items failed.");
    }
}// fn close

// function for WriteInDynmo
public static void WriteInDynamo1(String tableName, String hashName,
		String rangeName, String hashValue, String rangeValue,
		AmazonDynamoDBClient client) {
	File f1 = new File(rangeValue);
    ArrayList<String> names = new ArrayList<String>(Arrays.asList(f1.list()));
    for(int i = 0 ; i< names.size(); i++)
    {
	try {
			Map<String, AttributeValue> item1 = new HashMap<String, AttributeValue>();
	        item1.put(hashName, new AttributeValue().withS(hashValue));
	        item1.put(rangeName, new AttributeValue().withS(names.get(i)));
	        
	        PutItemRequest putItemRequest1 = new PutItemRequest()
	        .withTableName(tableName)
	        .withItem(item1);
	        PutItemResult result1 = client.putItem(putItemRequest1);
       } catch (AmazonServiceException ase) {
           	//System.out.println(ase);
               System.err.println("Create items failed.");
   }
    }// for close
}
// Function for Updating Status
public static void updateStatus1(String hashName, String hashValue, String newHashValue,
		String rangeName, String rangeValue, String columnName, String columnValue,
		String tableName, AmazonDynamoDBClient client2) {
	//System.out.println("Inside Function Busy To Free");
	deleteItem(hashName, hashValue, rangeName, rangeValue, tableName, client2);
	InsertIntoTable(hashName, newHashValue, rangeName, rangeValue, columnName, columnValue, tableName, client2);

}// fn close
// Function for deleting dir
public static void deleteDir(String mkDirectory) {
	try 
	{
		File file = new File(mkDirectory);
		// changin permission//
		file.setExecutable(true);
		file.setWritable(true);
		FileUtils.deleteDirectory(new File(mkDirectory));
	} 
	catch (IOException e) 
	{
		e.printStackTrace();
	}
}// fn close
static String getBits(byte b)
{
    String result = "";
    for(int i = 0; i < 8; i++)
        result += (b & (1 << i)) == 0 ? "0" : "1";
    return result;
}

private static void TextToBinaryConversion(String from,String path) throws IOException {

	try {
        StringBuilder sb = new StringBuilder();
        File file = new File(path);
        DataInputStream input = new DataInputStream( new FileInputStream( file ) );
        try {
            while( true ) {
                //sb.append( Integer.toBinaryString( input.readByte() ) );
                FileWrite(path+"1", Integer.toBinaryString( input.readByte() ));
            }
        } catch( EOFException eof ) {
        } catch( IOException e ) {
            e.printStackTrace();
        }
        //System.out.println(sb.toString());
    } catch( FileNotFoundException e2 ) {
        e2.printStackTrace();
    }
	
}
public static void compress(String src, String dest) throws java.io.IOException
{
    java.util.zip.GZIPOutputStream out = new java.util.zip.GZIPOutputStream(new java.io.FileOutputStream(dest));
    java.io.FileInputStream in = new java.io.FileInputStream(src);
    byte[] buf = new byte[1024];
    int len;
    while ((len = in.read(buf)) > 0)
    {
        out.write(buf, 0, len);
    }
    in.close();
    // Complete the GZIP file
    out.finish();
    out.close();
}
public static void WriteInSequentialFile(String pathOfFile, Text key, Text value) {
	 Configuration conf = new Configuration();
	 org.apache.hadoop.fs.RawLocalFileSystem fs = new org.apache.hadoop.fs.RawLocalFileSystem();
	 fs.setConf(conf);
	 Path path = new Path(pathOfFile);
	 try {
		SequenceFile.Writer writer = SequenceFile.createWriter(fs,conf, path,
				    key.getClass(), value.getClass());
		writer.append(key, value);
		writer.close();
	} catch (IOException e) {
		//e.printStackTrace();
	}
}// fn close

}// class close
