import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class FetcherManager
{
	static AmazonDynamoDBClient client;
	static Logger logger = Logger.getLogger("MyLog");

 public static void main(String[] args) {
	try {
		createClient();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	// start function FetcherManager
	try
	{
	String limitToMakeFiles = args[0];
	String howMuchTogetFromDynamo = args[1];
	String whileloop = args[2];
	String path = args[3];
	String secondaryPath = args[4];
	String pathToScp = args[5];
	//String numberofservers = args[6];
	FetcherManagerFunction(limitToMakeFiles,howMuchTogetFromDynamo,whileloop, path, secondaryPath,pathToScp);
	}
	catch(ArrayIndexOutOfBoundsException e)
	{
		System.out.println("Probabily some of the following args are missing");
		System.out.println("1. how many data you want to fetch from dynamodb eg in total = 4000");
		System.out.println("2. limit of query from Dyanmodb Table eg = 500");
		System.out.println("3. How many times while loop will run eg for infinite type infinite");
		System.out.println("4. path where files will be stores after getting links from ProductLink eg: /home/ubuntu/data/");
		System.out.println("5. Second path where files will be stores after getting links from ProductLink eg: /home/ubuntu/data");
		System.out.println("6. path to send in server for copying eg: /home/ubuntu");
		//System.out.println("7. Number of servers");
	}
}	// main close
 
 // create client
 private static void createClient() throws IOException {

		AWSCredentials credentials = new PropertiesCredentials(
				FetcherManager.class
						.getResourceAsStream("AwsCredentials.properties"));
		client = new AmazonDynamoDBClient(credentials);
	}

//-------------------------------------------------------------------------------------------------
 // fetcher manager
 public static void FetcherManagerFunction(String limitToMakeFiles,String howMuchTogetFromDynamo,String whileLoop,String path,String secondaryPath, String pathToScp)
 {
	 System.out.println("----------------------------------------------------------------------------------------------");
	 System.out.println("                                   Fetching Master                                                                    ");
	 System.out.println("----------------------------------------------------------------------------------------------");
	 String fileName = "1";
	 //String path = "/home/ubuntu/data/";
	 String tableName = "ProductLink",hashName = "status",rangeName = "url",columnName = "storeName",hashValue ="notDone";
	 //String pathToScp = "/home/ubuntu";
	 createLogger();
	 
	 // make dir data
	 new File(secondaryPath).mkdir();// create dir data in /home/ubuntu
	 logger.info("While loop is started..");
	 // its a testing face
	 int check1 = 2;
	 while(check1>0)
	 {
		 theThread t = null; // creating instance for class
	     Map<String,String> mapReturn = new HashMap<String,String>();		

		 Map<String,String> mapOfSlaves123 = new HashMap<String,String>();
		 mapOfSlaves123 = Functions.ReadFromDyanmo("FetchingStatus","free",client);
		 if(mapOfSlaves123.keySet().size() > 0)
		 {	 logger.info("Getting data from ProductLink Table ( in dynamo)");
			 mapReturn = Functions.ReadDataFromDyanmo(tableName,hashName,hashValue,rangeName,columnName,Integer.parseInt(limitToMakeFiles),Integer.parseInt(howMuchTogetFromDynamo),client);
		 
		 
		 if(!mapReturn.isEmpty())
		 {
			 logger.info("Getting data from ProductLink Table is finished ( in dynamo)");
			 Set<String> urls;
			 urls = mapReturn.keySet();
			 if(urls.size() >= Integer.parseInt(limitToMakeFiles))
			 {
				 logger.info("We got data(urls) = "+limitToMakeFiles+" whose status is notDone");
				 logger.info("Changing status from doing to done in ProductLink table...");
				 int counter = 0;
				 
				 logger.info("Writing in "+path+fileName+".txt"+" is started ...");
				 for(Map.Entry<String, String> item : mapReturn.entrySet())
				 {
				     String content = item.getKey()+" "+item.getValue()+"\n";
				     if(counter == 100)
				     {
					     
						 logger.info("Writing in "+path+fileName+".txt"+" is finished for "+counter+" records.");
					     counter = 0;
					     
					 }
				     counter++;
				     
				     Functions.FileWrite(path+fileName+".txt", content);
					 
				   //update status from doing to done
				   //Functions.updateStatus("status", "notDone","doing","url",item.getKey(),"storeName",item.getValue(), tableName,client);
				 }// for close
				 // Calling thread to update the links from notDone to 
				  t = new theThread(mapReturn,client);
				 // write in file
				 logger.info("Changing status from doing to done is finished");
				 //check that any server is free or not
				 Map<String,String> mapOfSlaves = new HashMap<String,String>();
				 mapOfSlaves = Functions.ReadFromDyanmo("FetchingStatus","free",client);
				 if(!mapOfSlaves.isEmpty())
				 {
					 logger.info("We have "+mapOfSlaves.keySet().size()+" free servers at this time");
					 File f = new File(path);
					 logger.info("We are getting the list of Files currently available in data dir...");
					 ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));
					 //System.out.println(names);
					 logger.info("We are getting the list of Files currently"+names.size()+" available in data dir is finished");
					 //Fetch the fileNames in path dir
					 // make entry in dynamo that the server with IP is busy
					 
							 for(Map.Entry<String, String> item : mapOfSlaves.entrySet())
							 {
								 System.out.println(item.getValue());
								 if(item.getValue()!=null)
								 {
								 logger.info("File at "+item.getValue()+" is deleting...");
								 //System.out.println("Inside loop of for for deleting files..."+item.getValue());
								 File newFile = new File(item.getValue());
								 newFile.setExecutable(true);
								 newFile.setWritable(true);
								 Functions.deleteFile(item.getValue(),logger);
								 }// if close
					 } // for close
					 
					 Map<String,String> mapOfSlaves1 = new HashMap<String,String>();
					 mapOfSlaves1 = Functions.ReadFromDyanmo("FetchingStatus","busy",client);
					 if(!mapOfSlaves1.isEmpty())
					 {
							 for(Map.Entry<String, String> item : mapOfSlaves1.entrySet())
							 {
								 System.out.println(item.getValue());
								 if(item.getValue()!=null)
								 {
									 
								 logger.info("File at "+item.getValue()+" is deleting...");
								 //System.out.println("Inside loop of for for deleting files..."+item.getValue());
								 File newFile = new File(item.getValue());
								 newFile.setExecutable(true);
								 newFile.setWritable(true);
								 Functions.deleteFile(item.getValue(),logger);
								 }// if close
							 } // for close
					}// if close
					File f1 = new File(path);
					ArrayList<String> names1 = new ArrayList<String>(Arrays.asList(f1.list()));
					//names =  names1;
					//System.out.println("names1 : "+names1);
					if(names1.size() > 0 && mapOfSlaves.keySet().size() > 0)
					 {
					 // scp it in the script using process builder
					 //Ipof slave and pathName
						 int i = 0;
						 // loop for assigning files to servers
						 int check = 0;
						 System.out.println("Inside processing started stage...");
						 for(Map.Entry<String, String> item : mapOfSlaves.entrySet())
						 {
							 if(check<names1.size())
							 {
								 check++;
							 }
							 else
								 break;  // quit loop cuase you have not files right now to send
							 // remove the file before assigning
//							 if(item.getValue()!=null)
//							 {
//							 logger.info("File at "+item.getValue()+" is deleting...");
//							 File newFile = new File(item.getValue());
//							 newFile.setExecutable(true);
//							 newFile.setWritable(true);
//							 
//							 Functions.deleteFile(item.getValue());
//							 String name = newFile.getName();
//							 names.remove(name); // remove it from arraylist too
//							 logger.info("File at "+item.getValue()+" is deleted.");
//							 }// if close
							 
							 logger.info("Process builder is called... for slave "+item.getKey());
							 // three args to send 
							 /*
							  * 1. slaveIp
							  * 2. pathwithFileName for scp
							  * 3. path to scp
							  * 4. name of the file for scp
							  * */
							 logger.info("Updating status of slave :"+item.getKey()+" from free to busy");
							 Functions.updateStatus("status","free","busy","slaveIp",item.getKey(),"processingFileName",path+names1.get(i),"FetchingStatus",client);
							 logger.info("Updating status of slave :"+item.getKey()+" from free to busy is done");
							 ProcessThread p = new ProcessThread();
							 p.startProcessThread("/home/ubuntu/startFetcherSlave.sh",item.getKey(),path+names1.get(i),pathToScp,names1.get(i));
							 try 
							 {
								Thread.sleep(10000);
							 } catch (InterruptedException e) 
							 {
								e.printStackTrace();
							 }
							 //Functions.ProcessBuilder("/home/ubuntu/startFetcherSlave.sh",item.getKey(),path+names.get(i),pathToScp,names.get(i));
							 i++;
							 //nameofSlave++;  // it defines name of slave
						 }// for close
					 }// if there are files in dir
				 }// there are free slaves
				 else
				 {
					 logger.info("There are no free Slaves...");
				 }
				//increment for changing name of file
					fileName = Integer.toString((Integer.parseInt(fileName)+1));
			}// if the size is > 5000
		 }// if close
		 else
		 {
			 logger.info("No data is received from ProductLink Table( in dynamo)");
		 }
		 //waiting of for loop started
		
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
		 
		System.out.print("Check all thread's results");
		    	  if (t.status() != theThread.THREADPASS) {
		            System.out.print("Unexpected thread status\n");
		    	  }
		    	  
		logger.info("Now Wating is started for Updating the links is done...");
		logger.info("while loop is called again...");
//			try {
//				logger.info("Thread Sleep started for  ")
//				Thread.sleep(30000);
//			} catch (InterruptedException e) {
//				
//				e.printStackTrace();
//			}
		// decrement round for testing purpose
	 if(!whileLoop.equalsIgnoreCase("infinite"))
	 {
		 System.out.println("While will run only for limited number .");
		 check1--;
	 }
		 }// if close for checking that the servers are not free
	 }// while close
 }// fetcher manager close
//-------------------------------------------------------------------------------------------------
 // loggers
private static void createLogger() {
	FileHandler fh;
	try {

		// This block configure the logger with handler and formatter
		fh = new FileHandler("NewFetcherManager.log");
		logger.addHandler(fh);
		SimpleFormatter formatter = new SimpleFormatter();
		fh.setFormatter(formatter);

		// the following statement is used to log any messages
		logger.info("FetcherManager Log File");

	} catch (SecurityException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}

}// function close
static class theThread extends Thread {
    public final static int THREADPASS     = 0;
    public final static int THREADFAIL     = 1;
    int         _status;
    public Map<String,String> mapToReturn;
    public AmazonDynamoDBClient client1;
    public int status() {
       return _status;
    }
    public theThread(Map<String,String> mapToReturn, AmazonDynamoDBClient client) {
       _status = THREADFAIL;
       this.mapToReturn = mapToReturn;
       this.client1 = client;
    }
    public void run() {
    String tableName = "ProductLink";
    for(Map.Entry<String, String> item : mapToReturn.entrySet())
    {
    	Functions.updateStatus("status", "notDone","doing","url",item.getKey(),"storeName",item.getValue(), tableName,client1);
    }// for close
    _status = THREADPASS;
    }// run close
 }
}// class close