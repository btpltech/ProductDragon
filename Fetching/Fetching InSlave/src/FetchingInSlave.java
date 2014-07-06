import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.jsoup.Jsoup;




import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.simpleworkflow.flow.annotations.NoWait;


public class FetchingInSlave {
	public final static int THREADGROUPSIZE   = 5;
	static int noOflaunchedThread;
	static AmazonDynamoDBClient client;
	public Map<String,String> mapOfUrls = new HashMap<String,String>();
	public String path = null;
	public static String nameOfSlave1 ;
	public static String logDir = "/home/ubuntu/log";
	
public static void main(String[] args) {
	/* These are args recevied
	  * 1. slaveIp
	  * 2. pathwithFileName for scp
	  * 3. path to scp
	  * 4. name of the file for scp
	  * */
	String SlaveIp = args[0]; // here we will get Ip for updating status to done
	String pathToUpdate = args[1]; // here we will get pathName from the script
	String dirNameFromWhereUrlsWillBeRead = args[2]; // here we will get the path from where to read
	String fileNameToRead = args[3]; // in this we will get fileName from where we will get urls
	//String nameOfSlave = args[4];  // it contains name of slave
	String[] data = fileNameToRead.split("\\.(?=[^\\.]+$)");
    //System.out.println(data[0]);
	String fileName = data[0];
    
	nameOfSlave1 = fileName;
	String dirName = "/home/ubuntu/data/"; // in this dir files will be created after fetching html
	String mkDirectory = "/home/ubuntu/data";

	//String logDir = "/home/ubuntu/log";
	
	
//	String dirName = "/home/amit/Desktop/data/";
//	String mkDirectory = "/home/amit/Desktop/data";

	
	new File(mkDirectory).mkdir(); // create dir data in /home/ubuntu/
	
	try {
		createClient();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	new File(logDir).mkdir(); // create dir log
	try {
		createClient();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	//logger
	String logName ="FetcherSlaveMaster";
	Logger logger = createLogger(logName);
	// make data dir in slave
	Map<String,String> mapOfUrlsAndStoreName = new HashMap<String,String>();
	logger.info("Getting urls from file ...");
	mapOfUrlsAndStoreName = Functions.FileRead(dirNameFromWhereUrlsWillBeRead+"/"+fileNameToRead);
//	System.out.println(mapOfUrlsAndStoreName);
	logger.info("Getting urls from file is finished.");

	Set<String> listofKeysinSet = new HashSet<String> ();
	listofKeysinSet = mapOfUrlsAndStoreName.keySet();
	
	//populate set
	ArrayList<String> listofKeys = new ArrayList<String>();
	for (String s : listofKeysinSet) {
		listofKeys.add(s);    
	}
	// get keyset of mapOfUrlsAndStoreName
	
	
	//------------------------------------------------------------------------------------
	//                               Code for MultiThreading
	//------------------------------------------------------------------------------------
	int noOfThread = 0;
	
	theThread     thread[] = new theThread[THREADGROUPSIZE]; // creating array of objects
	System.out.println("Above while");
	while(noOfThread < THREADGROUPSIZE)
	{
		Map<String,String> tempMap = new HashMap<String,String>();
		//for(int i = 0; i< 2 ;i++)
		for(int i = noOfThread*800; i< (noOfThread*800+800);i++)
		{	// creating map
			tempMap.put(listofKeys.get(i),mapOfUrlsAndStoreName.get(listofKeys.get(i)));
		}// for close
		logger.info("Launching Thread No "+noOfThread);
		System.out.println(noOfThread);
    	thread[noOfThread] = new theThread(dirName,tempMap,noOfThread,noOfThread,nameOfSlave1);
        thread[noOfThread].start();
    	noOfThread++;
		//System.out.println("loop is while");
	}// while for creating thread is finished.
	
	//---------------------------------------------------------------------
	//Code for Checking That all threds are completed
	//---------------------------------------------------------------------
	
	logger.info("Wait for worker threads to complete");
	
	int i = 0;
    for (i=0; i <THREADGROUPSIZE; ++i) {
    	try {
          thread[i].join();
    	}
       catch (InterruptedException e) {
          logger.info("Join interrupted");
       }
    }
    
    logger.info("Checking That all threads are finished or not ?");
    for (i=0; i <THREADGROUPSIZE; ++i) {
  	  if (thread[i].status() != theThread.THREADPASS) {
          logger.info("Unexpected thread status");
       }
    }
    
    //-------------------------------------------------------------------------------
    logger.info("All Threads Are Completed");
    //-------------------------------------------------------------------------------
    // code for making tar
//		    String[] data = fileNameToRead.split("\\.(?=[^\\.]+$)");
//		    //System.out.println(data[0]);
//			String fileName = data[0];
//		    //String tarLocation = dirNameFromWhereUrlsWillBeRead+"/"+fileName+".gzip";
			
		    //String tarFile = "/home/amit/Desktop/"+"amit"+".gzip";
			//Functions.WriteInDynamo("FetchedUrl","fileName","url",tarLocation,listofKeys,client);
		    
			//try 
			{
				File f1 = new File(dirName);
				logger.info("Getting the list of Files made after threading...");
				ArrayList<String> names = new ArrayList<String>(Arrays.asList(f1.list()));
				
				//System.out.println(names);
//				if(names.size()>0)
//				{
//				logger.info("Making tarFile "+tarLocation+"...");
//				Functions.MakeTarFile(dirName,tarLocation);
//				Functions.MakeTarFile(mkDirectory,tarLocation);
//				logger.info("Making tarFile "+tarLocation+" is finished.");
//				}
				
				logger.info("Uploading Files in S3...");
				Functions.UploadInS3("FetchedData",dirName,fileName);
				logger.info("Uploading Files in S3 is finished.");
				
				Functions.WriteInDynamo1("FetchedFileStore", "status", "fileName", "notDone",dirName, client);
				
				logger.info("Updating Status From busy to free of the slave : "+SlaveIp+" started...");
  			    try
  			    {
				logger.info("status"+"busy"+"free"+"slaveIp"+SlaveIp+"processingFileName"+pathToUpdate+"FetchingStatus");
				System.out.println(SlaveIp);
				Functions.updateStatus1("status","busy","free","slaveIp",SlaveIp,"processingFileName",pathToUpdate,"FetchingStatus",client);
				logger.info("Updating Status From busy to free of the slave : "+SlaveIp+" is finished.");
				System.out.println(SlaveIp+" done busy to free");
  			    }
			   catch(AmazonClientException e)
			   {
				   
			   }

			    logger.info("Now Deleting dir data in Slave :"+SlaveIp+" started...");
                Functions.deleteDir(mkDirectory);
                logger.info("Now Deleting dir data in Slave :"+SlaveIp+" is finished.");
                
                logger.info("Now Deleting dirNameFromWhereUrlsWillBeRead data in Slave :"+SlaveIp+" started...");
                Functions.deleteFile(dirNameFromWhereUrlsWillBeRead+"/"+fileNameToRead);
                logger.info("Now Deleting dirNameFromWhereUrlsWillBeRead data in Slave :"+SlaveIp+" is finished.");               
               
//                logger.info("Now Deleting zip file ...");
//                File f12 = new File(tarLocation);
//                f12.setWritable(true);
//                f12.setExecutable(true);
//                //Functions.deleteFile(tarLocation);
//                logger.info("Now Deleting zip file is deleted");
                
			} 
			//catch (ArchiveException e) 
			{
			} 
			//catch (IOException e) 
			{
			}
			// break if the no of Thread launched == no of threads finished.
	
	//startFetcher();
}// main close

//----------------------------------------------------------------------------------------------
// create client
private static void createClient() throws IOException {

		AWSCredentials credentials = new PropertiesCredentials(
				FetcherManager.class
						.getResourceAsStream("AwsCredentials.properties"));
		client = new AmazonDynamoDBClient(credentials);
	}// client close
// loggers

private static Logger createLogger(String logName) {
	Logger logger = Logger.getLogger(logName);
	FileHandler fh;
	try {

		// This block configure the logger with handler and formatter
		fh = new FileHandler(logName+".log");
		logger.addHandler(fh);
		SimpleFormatter formatter = new SimpleFormatter();
		fh.setFormatter(formatter);

		// the following statement is used to log any messages
		logger.info("FetchingSlave Log File");

	} catch (SecurityException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
return logger;
}// function close

//  static class
static class theThread extends Thread {
    public final static int THREADPASS     = 0;
    public final static int THREADFAIL     = 1;
    public static String dirName;
    public Map<String,String> tempMap;
    public static int noOfThread ;
    public static String nameOfSlave ;
    int         _status;

    public int status() {
       return _status;
    }
    public theThread(String dirName, Map<String, String> tempMap,int noOfThread, int noOfThread2,String nameofSlave) {
       _status = THREADFAIL;
       this.dirName = dirName;
       this.tempMap = tempMap;
       this.noOfThread = noOfThread2;
       this.nameOfSlave = nameofSlave;
    }
    public void run() {
       startFetcher1(dirName, tempMap, noOfThread,nameOfSlave);
       _status = THREADPASS;
    }// run close
    private static Logger createLogger1(String logName) {
    	Logger logger = Logger.getLogger(logName);
    	FileHandler fh;
    	try {

    		// This block configure the logger with handler and formatter
    		fh = new FileHandler(logDir+"/"+logName+".log");
    		logger.addHandler(fh);
    		SimpleFormatter formatter = new SimpleFormatter();
    		fh.setFormatter(formatter);

    		// the following statement is used to log any messages
    		logger.info("FetchingSlave Log File");

    	} catch (SecurityException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    return logger;
    }// function close
    public static void startFetcher1(String pathForFiles,Map<String,String> mapOfUrlsAndStoreName,int noOfThread, String nameOfSlave2) {
    	//String pathForFiles = "/home/ubuntu/data";
    	String logName = Integer.toString(noOfThread);
    	logName = "Slave"+logName;
    	Logger logger = createLogger1(logName);
    	
    	//int countForWriting = 0;
    	//String content = "";
    	if(!mapOfUrlsAndStoreName.isEmpty())
    	{
    		
    		//ArrayList<String> listOfFilesToMakeTar = new ArrayList<String>();
    		//int sizeOfMap = mapOfUrlsAndStoreName.keySet().size();
    		
    		String pathOfFile = pathForFiles+nameOfSlave2+System.currentTimeMillis()+".seq";
			File file = new File(pathOfFile);
			file.setExecutable(true);
			file.setWritable(true);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					System.out.println(e);
				}
			} 	
			int count = 0;
			 Configuration conf = new Configuration();
			 org.apache.hadoop.fs.RawLocalFileSystem fs = new org.apache.hadoop.fs.RawLocalFileSystem();
			 fs.setConf(conf);
			 Path path = new Path(pathOfFile);
			 try {
				Text key = new Text();
				Text value = new Text();
				SequenceFile.Writer writer = SequenceFile.createWriter(fs,conf, path,
						    key.getClass(), value.getClass());
				
					for(Map.Entry<String, String> item : mapOfUrlsAndStoreName.entrySet())
		    		{
		    			//System.out.println(item.getKey());
		    			String html = null;
		    			try {
		    			 html = Jsoup.connect(item.getKey()).get().html();
		    				logger.info("Fetching html for count  ="+count +" :"+item.getKey()+" "+ " in Thread no :"+noOfThread+" done");
		    			} catch (IOException e) {
		    			e.printStackTrace();
		    				logger.info("Fetching html for "+item.getKey()+" is failed"+ " in Thread no :"+noOfThread);
		    			}
		    			if(html!=null)
		    			{
		    					String separater = "~!~";
		    					key = new Text(item.getValue()+separater+item.getKey());
		    					value = new Text(html);
		    					writer.append(key, value);
		    					//		Functions.WriteInSequentialFile(pathOfFile,key,value);
		    			}// if close
		    		count++;
		    		}// for close
				writer.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
    	//code for making tar file and put it in S3
    	}// if close
    	logger.info("Thread no "+noOfThread+" is finished.");
    	noOflaunchedThread++;
    }// function close
 }
}