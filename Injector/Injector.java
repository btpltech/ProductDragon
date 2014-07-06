import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.htmlcleaner.XPatherException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;

public class Injector {
	static AmazonDynamoDBClient client;

	public static void main(String[] args) throws IOException {

		createClient();
		System.out.println("Code For fetching MenuLinks started...");
		String pathForMenuLink = args[0];

		// String pathForFileCreation = "/home/amit/Desktop/";
		Logger logger = Logger.getLogger("MyLog");
		FileHandler fh;

		// this code will create log file
		try {

			// This block configure the logger with handler and formatter
			fh = new FileHandler("Injector.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

			// the following statement is used to log any messages
			logger.info("Injector Log File");

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {

			try {

				BufferedReader br = new BufferedReader(new FileReader(
						pathForMenuLink));

				String sCurrentLine = null;
				while ((sCurrentLine = br.readLine()) != null) {
					if (sCurrentLine.charAt(0) != '#'
							&& sCurrentLine.charAt(0) != '/') {
						String[] data = sCurrentLine.split(" ");
						String urlString = data[0];
						String linkXPath = data[1];
						try {
							ArrayList<String> ListForMenulinks = new ArrayList<String>();

							ManageFileForMenuLInks.IO.FileCustomWrite(
									pathForMenuLink, sCurrentLine, "/");

							logger.info("Fetching Menu link is started");

							ListForMenulinks = GetMenuLink.getMenuLink(
									urlString, linkXPath);

							logger.info("Fetching Menu link is finished");
							// EmitClass.printArrayList(ListForMenulinks);
							System.out.println(" ..." + ListForMenulinks);

							if (ListForMenulinks.size() != 0) {
								// here we will write code for inserting data
								// into dynamo db
								// EmitClass.printArrayList(ListForMenulinks);

								URL newURLGET = new URL(urlString);
								String newURL = newURLGET.getHost();

								// function is called
								System.out
										.println("Inserting data into MenuLink is started");

								// function is called

								logger.info("Inserting data into MenuLink is started");

								putItemIntoMenuLink("MenuLink", newURL, "url",
										ListForMenulinks);

								System.out
										.println("Inserting data into MenuLink is finished.");

								logger.info("Inserting data into MenuLink is finished");

								System.out
										.println("Inserting data into StoreLink is started");
								logger.info("Inserting data into StoreLink is started");
								putItemIntoStoreLink("StoreLink", "status",
										"url", urlString, "storeName", newURL);

								System.out
										.println("Inserting data into StoreLink is finished.");

								logger.info("Inserting data into StoreLink is finished.");

							}// if close
								// write # to the link completed
							ManageFileForMenuLInks.IO.FileCustomWrite(
									pathForMenuLink, sCurrentLine, "#");

						} catch (XPatherException e1) {

							e1.printStackTrace();
						}

					}

				} // while close for file reading
				br.close();

			} catch (IOException e) {
				e.printStackTrace();

			}

			// this is code for every 2 sec the while loop revokes
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}// while close

	}

	// function for creating client
	private static void createClient() throws IOException {

		AWSCredentials credentials = new PropertiesCredentials(
				Injector.class.getResourceAsStream("AwsCredentials.properties"));

		client = new AmazonDynamoDBClient(credentials);

	}

	// function for inserting into StoreLink
	private static void putItemIntoStoreLink(String tableName, String hashName,
			String rangeName, String rangeValue, String columnName,
			String columnValue) {
		try {

			Map<String, AttributeValue> item1 = new HashMap<String, AttributeValue>();
			item1.put(hashName, new AttributeValue().withS("notDone"));
			item1.put(rangeName, new AttributeValue().withS(rangeValue));
			item1.put(columnName, new AttributeValue().withS(columnValue));

			PutItemRequest putItemRequest1 = new PutItemRequest()
					.withTableName(tableName).withItem(item1);
			PutItemResult result1 = client.putItem(putItemRequest1);

			System.out.println(" record is inserted in StoreLink.");

		} catch (AmazonServiceException ase) {
			System.err.println("Create items failed.");
		}

	}

	// function for inserting into DynmoDb

	private static void putItemIntoMenuLink(String tableName, String urlString,
			String range, ArrayList<String> itemList) {
		try {

			for (int i = 0; i < itemList.size(); i++) {
				Map<String, AttributeValue> item1 = new HashMap<String, AttributeValue>();
				item1.put("storeName", new AttributeValue().withS(urlString));
				item1.put(range, new AttributeValue().withS(itemList.get(i)));
				item1.put("status", new AttributeValue().withS("notDone"));

				PutItemRequest putItemRequest1 = new PutItemRequest()
						.withTableName(tableName).withItem(item1);
				PutItemResult result1 = client.putItem(putItemRequest1);

				System.out.println((i + 1) + " record is inserted in dynamo.");
			}// for close

		} catch (AmazonServiceException ase) {
			System.err.println("Create items failed.");
		}

	}

}
