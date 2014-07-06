package IndexInSolr;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;



import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.gson.Gson;

public class SolrIndex {
static AmazonDynamoDBClient client;
static Logger logger;
	public static void main(String[] args) {
	try
	{
		String solrUrl = args[0];
		String solrPort = args[1];
		int limit = Integer.parseInt(args[2]);
		try 
		{
			createClient();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// function is called here
		createLogger();
		//while(true)
		Functions.scanDataFromParsedProductsAndIndexInSolr("ParsedProducts",limit,client,solrUrl,solrPort,logger);
		
	}
	catch(ArrayIndexOutOfBoundsException e){
		System.out.println("One of the args are not entered..");
		System.out.println("1. solrIP");
		System.out.println("2. solrPort");
		System.out.println("3. dynamoScanLimit");
	}
}
//function for creating client
	private static void createClient() throws IOException {

		AWSCredentials credentials = new PropertiesCredentials(
				SolrIndex.class
						.getResourceAsStream("AwsCredentials.properties"));
		client = new AmazonDynamoDBClient(credentials);
} // create client is closed

	// this function
		private static void createLogger() {
			logger = Logger.getLogger("MyLog");
			FileHandler fh;
			try {

				// This block configure the logger with handler and formatter
				fh = new FileHandler("SolrIndex.log");
				logger.addHandler(fh);
				SimpleFormatter formatter = new SimpleFormatter();
				fh.setFormatter(formatter);

				// the following statement is used to log any messages
				logger.info("SolrIndex Log File");

			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
}
