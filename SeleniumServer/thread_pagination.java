import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;

public class thread_pagination {

	static AmazonDynamoDBClient client;
	
	static void deleteItem(String hashName, String hashValue,
			String rangeName, String rangeValue, String tableName) {

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
			System.out.println(rangeValue + " is deleted");

		} catch (AmazonServiceException ase) {
			System.err
					.println("Failed to get item after deletion " + tableName);
		}
	}// function close

	static void InsertIntoTable(String tableName,
			Map<String, AttributeValue> return_hashMap) {
		try {
			// Map<String, AttributeValue> item1 = new HashMap<String,
			// AttributeValue>();
			// item1.put(hashName, new AttributeValue().withS(newHashValue));
			// item1.put(rangeName, new AttributeValue().withS(rangeValue));
			// item1.put(columnName, new AttributeValue().withS(columnValue));

			PutItemRequest putItemRequest1 = new PutItemRequest()
					.withTableName(tableName).withItem(return_hashMap);
			PutItemResult result1 = client.putItem(putItemRequest1);

		} catch (AmazonServiceException ase) {
			System.err.println("Create items failed.");
		}

	}// function closes

	public static ArrayList<String> FileRead(String path) {
		ArrayList<String> listofmenuLinks = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				if (!sCurrentLine.equals("")) {
					if (sCurrentLine.charAt(0) != '#') {
						listofmenuLinks.add(sCurrentLine);
						// System.out.println(sCurrentLine);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return listofmenuLinks;
	}// read function is closed

	public static void printArrayList(ArrayList<String> productPageLink) {
		for (int i = 0; i < productPageLink.size(); i++)
			System.out.println(productPageLink.get(i));
		// System.out.println("--------------------------------------------------------------------------------------");
	}// fn close
	public static String fileread(String path) {
		// ArrayList<String> listofmenuLinks = new ArrayList<String>();
		String host_name = new String();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null&&!sCurrentLine.equals("")&&!sCurrentLine.equals(null)) {
				if (sCurrentLine.startsWith("#")) {
					host_name = sCurrentLine;
					
//					System.out.println(sCurrentLine);
//					host_name = example.gethost(sCurrentLine);

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return host_name;
	}// read function is closed
	
}
