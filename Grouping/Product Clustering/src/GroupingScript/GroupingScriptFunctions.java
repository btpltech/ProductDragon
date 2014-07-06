package GroupingScript;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import GroupingManager.RedWrite;
import GroupingScript.SortMap.ValueComparator;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.gson.Gson;

public class GroupingScriptFunctions {

	//static AmazonDynamoDBClient client;
	//static Logger logger;

	// ------------------------------------------------------------------------------------------------
	// Function for Grouping is written here |
	// ------------------------------------------------------------------------------------------------

	public static void GroupingFunction(String url, String data,
			String storeName, String masterIp,  Logger logger) throws IOException {


		logger.info("inside GroupingFunction...");
		Gson g = new Gson();
		Map productMap = new HashMap();
		productMap = g.fromJson(data, Map.class);
		if (productMap.containsKey("attributes")) {
			Map<String, String> attributeMap = new HashMap<String, String>();
			attributeMap = g.fromJson(productMap.get("attributes").toString(),
					Map.class);
			logger.info("here we have got attribue Map");
			logger.info("Now we are getting probabilities of every attribute from 'HashCount' table in hbase...");
			// this map is to store probability for every key in attributeMap
			Map<String, String> probabilityMapForProduct = new HashMap<String, String>();
			for (Map.Entry<String, String> item : attributeMap.entrySet()) {
				String rowKey = item.getKey();
				// pass this key in getValueOfRowKey From hbase function
				try {
					String count = Functions1.getValueOfRowKey("HashCount",
							rowKey, "cf", "count", masterIp);
					if (count != null) {
						// this map is to store probability for every key in
						// attributeMap
						probabilityMapForProduct.put(rowKey, count);
					}// if under try is close
					else {
						// this map is to store probability for every key in
						// attributeMap
						probabilityMapForProduct.put(rowKey, "1");
						// in this part we have to add a entry in HashCount
						// table
						logger.info("adding entry in HashCount table in hbase started...");
						Functions1.InsertIntoHashCount(rowKey, "HashCount",
								"cf", "count", masterIp, logger, "1");
						logger.info("adding entry in HashCount table in hbase is finished.");
					}// else under try is close
				}// try close
				catch (IOException e) {
					logger.info(e.toString());
				}// catch close
			}// for close
			logger.info("probabilities of every attribute from 'HashCount' table in hbase is got.");
			// code for sort the probabilities in probabilityMapForProduct
			logger.info("sort the probabilities in probabilityMapForProduct...");
			// -------------------------------------------------------------------------------------
			// Code for sorting values of Map probabilityMapForProduct
			// -------------------------------------------------------------------------------------
			Map<String, Integer> tempMapForSoring = new HashMap<String, Integer>();
			// conversion
			for (Map.Entry<String, String> item : probabilityMapForProduct
					.entrySet()) {
				tempMapForSoring.put(item.getKey(),
						Integer.parseInt(item.getValue()));
			}// for close

			// call function to sort
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			ValueComparator vc = new ValueComparator(map);
			TreeMap<String, Integer> sorted = new TreeMap<String, Integer>(vc);

			for (Map.Entry<String, Integer> item : tempMapForSoring.entrySet()) {
				map.put(item.getKey(), item.getValue());
			}
			sorted.putAll(map);
			// System.out.println(sorted);

			logger.info("sort the probabilities in probabilityMapForProduct is done");
			logger.info("loop for Hashes one by one according to priority...");
			// flag will be set if no entry is founf
			int clusterFoundFlag = 0;

			// it will see that if the below loop goes >2 times then it will break;
			int breakLoop = 0;
			for (Entry<String, Integer> item : sorted.entrySet()) {
				// For every key we will look up in HashContent table First

				// here TaskStatus Class..............
				ArrayList<String> listOfClusterNo = Functions1
						.FindKeyInHashContent("HashContent", item.getKey(),
								masterIp, logger);

				
				// Make Entry in AmpleCluster in hbase
				logger.info("Making entry in AmpleCluster in hbase...");

				//-----------------------------------------------------------------
				// Here we are entering data in AmpleCluster in hbase from DynamoDb
				//-----------------------------------------------------------------
				
				
				//Again TaskStatus Class is involved...........
				//System.out.println(listOfClusterNo+" in AmpleCluster");
				
				TaskStatus.WriteInAmpleClusterTaskStatus("AmpleClusterTaskStatus", "cf", listOfClusterNo, "task", "notDone", masterIp);    	
					while(true)
					{
						Boolean status = TaskStatus.FindStatusOfAmpleClusterStatus("AmpleClusterTaskStatus",masterIp);
						
						if(status)
						{
							break;
						}//End of if
					}// while close
			    
			    // delete records from AmpleClusterTaskStatus
				for(int i = 0; i<listOfClusterNo.size();i++)
				{
					RedWrite.delRecord("AmpleClusterTaskStatus", listOfClusterNo.get(i), masterIp);
				}// for close
				

				logger.info("Making entry in AmpleCluster in hbase is done,");

				if (listOfClusterNo.size() != 0) {
					// if the size is not zero then do the core work

					for (int i = 0; i < listOfClusterNo.size(); i++) {
						// this function will return true if match occurred
						Boolean matchStatus = false;
						logger.info("Product Grouping Started..");
						matchStatus = Functions1.MatchClusterWithParsedData(
								"AmpleCluster", listOfClusterNo.get(i), data,
								logger, masterIp, storeName);
						logger.info("Product Grouping finished for one product");
						
						if (matchStatus) {
							clusterFoundFlag++;
							// we are breaking the loop cause match occurred
							break;
						}
					}// inner for close
				} // if close

				//
				if (clusterFoundFlag == 1) {
					// if match happens then break the main for loop
					break;
				}
				
				breakLoop++;
				if(breakLoop>2) //  check that if the breakLoop is >2 (reason: comparing must not be done on more that 2 levels)
					break;
		    } // for close
			logger.info("loop for Hashes one by one according to priority is finished.");
			/*
			 * Points to rem 1. i
			 */
			if (clusterFoundFlag == 0) {
				// acquire new clusterCount from AmpleCluster
				// Make entry in AmpleCluster clusterCount+1
				// make entry in HashContent table for every Attribute using
				// probabilityMapForProduct
				System.out.println("Cluster Number Not Found");
				logger.info("Acquiring Cluster Number From AmpleCluster....");

				String clusterNo = Functions1
						.FindClusterNumberUsingAmpleClusterInHbase(
								"AmpleCluster", logger, masterIp);
				System.out.println("Cluster Number : " + clusterNo);
				/**
				 * Inserting new cluster in the ample cluster table
				 */
				// modify data before inserting in AmpleCluster in hbase
				data = Functions1.modifyData(data, storeName);
				System.out.println(data);
				Functions1.EnrtyInAmpleClusterInhbase2("AmpleCluster", masterIp,
						logger, data, clusterNo);

				logger.info("Cluster Number is acquired.");

				logger.info("Making entry in HashContent table for Every Attribute for Clustet :"
						+ clusterNo);
				for (Map.Entry<String, String> item : probabilityMapForProduct
						.entrySet()) {
					// in this list only one entry is done. due to generality of
					// calling function
					ArrayList<String> listclusterNo = new ArrayList<String>();
					listclusterNo.add(clusterNo);
					Functions1.InsertInHashContentInHbaseFirstTime(
							item.getKey(), listclusterNo, "HashContent", "cf",
							masterIp, logger);

				}// for close

				logger.info("Making entry in HashContent table for Every Attribute for Clustet :"
						+ clusterNo + " is done.");
			}// if close
				// -------------------------------------------------------------------------------------
		}// if close
		else {
			// code for if the attributes key is notfound then make entry in
			// others
			// make a new cluster for others and make entry in HashContent and
			// in HashCount too in front of others
			// modify data before inserting in AmpleCluster in hbase
			data = Functions1.modifyData(data, storeName);
			String clusterNo = Functions1
					.FindClusterNumberUsingAmpleClusterInHbase("AmpleCluster",
							logger, masterIp);

			Functions1.EnrtyInAmpleClusterInhbase2("AmpleCluster", masterIp,
					logger, data, clusterNo);

			/**
			 * Put this cluster into others attribute name
			 */
			logger.info("attributes key was not found in ProductJson.");
		}
	}// function close

	// ------------------------------------------------------------------------------------------------
	// function for creating client
	// ------------------------------------------------------------------------------------------------
	public static void createClient() throws IOException {
//		AWSCredentials credentials = new PropertiesCredentials(
//				GroupingScriptFunctions.class
//						.getResourceAsStream("AwsCredentials.properties"));
//		client = new AmazonDynamoDBClient(credentials);

	}

	// ------------------------------------------------------------------------------------------------
	// function for create log
	// ------------------------------------------------------------------------------------------------
	public static void createLogger() {
//		logger = Logger.getLogger("MyLog");
//		FileHandler fh;
//		try {
//
//			// This block configure the logger with handler and formatter
//			fh = new FileHandler("GroupingScriptFunction.log");
//			logger.addHandler(fh);
//			SimpleFormatter formatter = new SimpleFormatter();
//			fh.setFormatter(formatter);
//
//			// the following statement is used to log any messages
//			logger.info("GroupingScript Log File");
//
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}// fn close

}
