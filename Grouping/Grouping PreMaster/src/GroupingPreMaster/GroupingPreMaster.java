package GroupingPreMaster;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;



import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class GroupingPreMaster {
	static AmazonDynamoDBClient client;
	static Logger logger;
	
	public static void main(String[] args) throws IOException {
		createClient();
		// in args[0] we will get masterIP for hbase
		System.out.println("=========================================================================");
		System.out.println("                          Grouping Pre Master                            ");
		System.out.println("=========================================================================");
		try
		{
		String masterIP = args[0];
		StartGroupingManager(masterIP);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("1. Enter master ip of hbase.");
		}// try close
	}

	// function for creating client
	private static void createClient() throws IOException {

		AWSCredentials credentials = new PropertiesCredentials(
				GroupingPreMaster.class
						.getResourceAsStream("AwsCredentials.properties"));
		client = new AmazonDynamoDBClient(credentials);

	} // create client is closed

	/*-----------------------------------------------------------------------------*/
	private static void StartGroupingManager(String masterIP) {

		createLogger();
		String columnFamily = "cf";
		
//		try {
//			createTableInhbase("buffered_parsed_data", columnFamily, masterIP);
//			logger.info("buffered_parsed_data is created.");
//		} catch (Exception e) {
//			// e.printStackTrace();
//			logger.info(e.toString());
//		}
		// crate table => group_data
		try 
		{
			Functions.createTableInhbase("parsed_data", columnFamily, masterIP,logger);
			logger.info("parsed_data is created.");
		} catch (Exception e) {
			// e.printStackTrace();
			logger.info(e.toString());
		}

		/*--------------------------------------------------------------------------------------------
		 * Code for Making HashContent table in hbase
		 * -------------------------------------------------------------------------------------------
		 * */
		try {
			Functions.createTableInhbase("HashContent", columnFamily, masterIP,logger);
			logger.info("HashContent is created.");
		} catch (Exception e) {
			// e.printStackTrace();
			logger.info(e.toString());
		}

		/*--------------------------------------------------------------------------------------------
		 * Code for Making HashContent table in hbase
		 * -------------------------------------------------------------------------------------------
		 * */
		try {
			logger.info("Creating HashCount table in hbase...");
			Functions.createHashCountTable("HashCount", "cf", masterIP,logger);
			logger.info("Creating HashCount table in hbase is finished.");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString() + " in Creating HashCount table in hbase.");
		}

		/*
		 * -------------------------------------------- 
		 * Code for scan HashCount
		 * table from Dynamodb and fill this in HashCount in hbase
		 * -------------------------------------------
		 */

		Map<String, String> hashCountMap = new HashMap<String, String>();
		String tableNameHashCount = "HashCount";

		// code for getting HashCount table from Dynamodb
		logger.info("scannig HashCountTable");
		hashCountMap = Functions.scanHashCount(tableNameHashCount,client);
		logger.info("scanning HashCount table is finished.");
		logger.info("Now inserting data in hbase in table => 'HashCount' is started..");
		if (!hashCountMap.isEmpty()) {
			Functions.InsertIntoHashCountInHbase(tableNameHashCount, masterIP,
					hashCountMap);
			logger.info("Now inserting data in hbase in table => 'HashCount' is finished..");
		}// if close
		else 
		{
			logger.info("There is no Data to insert in 'HashCount'(in dynamodb) to insert in Hbase table => 'HashCount'");
		}// else close

		
		} // function close

	// Function for creating logger
	private static void createLogger() {
			logger = Logger.getLogger("MyLog");
			FileHandler fh;
			try {
				// This block configure the logger with handler and formatter
				fh = new FileHandler("GroupingPreMaster.log");
				logger.addHandler(fh);
				SimpleFormatter formatter = new SimpleFormatter();
				fh.setFormatter(formatter);

				// the following statement is used to log any messages
				logger.info("Grouping Pre Manager Log File");

			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
