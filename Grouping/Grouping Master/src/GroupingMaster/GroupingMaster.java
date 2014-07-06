package GroupingManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class GroupingMaster {
	static AmazonDynamoDBClient client;
	static Logger logger;
	static int limitforMakingTextFile;
	static int copyLimitFromDynamo;
	static String pathWhereToFileStore;
	public static String masterIp ;
	//static String infinite;
	
	public static void main(String[] args) throws IOException {
		createClient();
		// in args[0] we will get masterIP for hbase
		System.out.println("=========================================================================");
		System.out.println("                          Grouping Master                                ");
		System.out.println("=========================================================================");
		try
		{
		
		limitforMakingTextFile = Integer.parseInt(args[0]);
		copyLimitFromDynamo	   = Integer.parseInt(args[1]);
		pathWhereToFileStore   = args[2];
		masterIp = args[3];
		//infinite = args[3];
		StartGroupingManager();
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("1. limitforMakingTextFile eg 4000");
			System.out.println("2. copyLimitFromDynamo. eg 500");
			System.out.println("3. pathWhereToFileStore eg: /home/ubuntu/data");
			System.out.println("4. MasterIp");
			//System.out.println("4. infinite or once while loop type \"inf\" for infinite && \"1\" for once");
		}// try close
	}

	// function for creating client
	private static void createClient() throws IOException {

		AWSCredentials credentials = new PropertiesCredentials(
				GroupingMaster.class
						.getResourceAsStream("AwsCredentials.properties"));
		client = new AmazonDynamoDBClient(credentials);

	} // create client is closed

	/*-----------------------------------------------------------------------------*/
	private static void StartGroupingManager() {

		createLogger();
		//String columnFamily = "cf";
		String separator = "~!~";  // separator for concating storeName~!~url
//		try {
//			createTableInhbase("buffered_parsed_data", columnFamily, masterIP);
//			logger.info("buffered_parsed_data is created.");
//		} catch (Exception e) {
//			// e.printStackTrace();
//			logger.info(e.toString());
//		}
	
		try {
			logger.info("Creating AmpleCluster table in hbase...");

			Functions.createHashCountTable("AmpleCluster", "cf", masterIp,logger);

			logger.info("Creating AmpleCluster table in hbase is finished.");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString()
					+ " in Creating AmpleCluster table in hbase.");
		}

		/*
		 * Code for get data from AmpleCluster from Dynamodb for count
		 * information
		 */
		Functions.GetCountOfClusterNumberAndMakeEntryInAmpleClusterInHbase(
				masterIp, client, logger);

		int fileName = 1;
		new File(pathWhereToFileStore).mkdir(); // create dir data in /home/ubuntu/
		
		//======================================================================================
		//int check = 1;
		//while (check > 0)
		{
			
			Map<String, String> dataFromDynamodb = new HashMap<String, String>();
			logger.info("Fetching data from Dynamodb.");
			dataFromDynamodb = Functions.scanParsedProducts("ParsedProducts",limitforMakingTextFile,copyLimitFromDynamo,separator,client);

			logger.info("copying data of ParsedProducts into a ParsedProductsProcessing started.");
			String hashName = "storeName", rangeName = "url", columnName = "data", tableName = "BackupParsedProducts";

			Functions.insertIntoDynamodb_ParsedProducts_processing(dataFromDynamodb,
			 tableName, hashName, rangeName, columnName,client,logger);

			// Thread is called for Deleting data from ParsedProduct
			theThread t = new theThread(dataFromDynamodb.keySet(), client);
			 
			logger.info("copying data of ParsedProducts into a ParsedProductsProcessing finished.");

			Configuration conf = new Configuration();
			org.apache.hadoop.fs.RawLocalFileSystem fs = new org.apache.hadoop.fs.RawLocalFileSystem();
			fs.setConf(conf);
			Path path = new Path(pathWhereToFileStore+"/"+Integer.toString(fileName)+".seq");
			try {
				Text key = new Text();
				Text value = new Text();
				SequenceFile.Writer writer = SequenceFile.createWriter(fs,conf, path,
						    key.getClass(), value.getClass());
				for(Map.Entry<String, String> item : dataFromDynamodb.entrySet())
				{
					key = new Text(item.getKey());
					value = new Text(item.getValue());
					writer.append(key, value);
				}// for close
					
				writer.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
			
			 
			 //Call thread to delete items from ParsedProducts
			 //Update ParsedFileStore table for file just created
//			 logger.info("Inserting Entry in ParsedFileStore started...");
//			 Functions.WriteInDynamo("ParsedFileStore", "status", "fileName", "notDone", Integer.toString(fileName)+".seq", client);
//			 logger.info("Inserting Entry in ParsedFileStore finished");
//			 
//			 logger.info("Put file into hdfs started..");
//			 
//			 logger.info("Put file into hdfs started..");
//			 
			 
			 try 
			 {
				logger.info("Now Wating is started for Updating the links");
				logger.info("Wait for worker thread to complete");
			    t.join();  // join the thread
			    Thread.sleep(1000);
			 }
			 catch (InterruptedException e) {
		    	 System.out.println("Join Intruppted..");
			 }
			System.out.print("Check thread results");
			    	  if (t.status() != theThread.THREADPASS) {
			            logger.info("Unexpected thread status\n");
			    	  }
			 //before starting while loop delete file made right now
			 //File f = new File(pathWhereToFileStore+Integer.toString(fileName)+".seq");
			// Functions.deleteFile(pathWhereToFileStore+Integer.toString(fileName)+".seq",logger);			 //ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));
			 fileName++;
//			 if(!infinite.equals("inf"))
//				 check--;
		}// while close
		//======================================================================================	
		} // function close

	// Function for creating logger
	private static void createLogger() {
			logger = Logger.getLogger("MyLog");
			FileHandler fh;
			try {
				// This block configure the logger with handler and formatter
				fh = new FileHandler("GroupingManager.log");
				logger.addHandler(fh);
				SimpleFormatter formatter = new SimpleFormatter();
				fh.setFormatter(formatter);

				// the following statement is used to log any messages
				logger.info("GroupingManager Log File");

			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	static class theThread extends Thread {
	    public final static int THREADPASS     = 0;
	    public final static int THREADFAIL     = 1;
	    int         _status;
	    public Map<String,String> mapToReturn;
	    public AmazonDynamoDBClient client1;
	    public Set<String> set;
	    
	    public int status() {
	       return _status;
	    }
	    public theThread(Set<String> set, AmazonDynamoDBClient client) {
	       _status = THREADFAIL;
	       this.set = set;
	       this.client1 = client;
	    }
	    public void run() {
	    
	    Functions.deleteFromDynamoDbParsedProducts(set, "ParsedProducts", "storeName", "url", logger, client1);
	    _status = THREADPASS;
	    }// run close
	 }
}