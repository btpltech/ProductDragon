package GroupingScript;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.xpath.XPathExpressionException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.lib.NullOutputFormat;
import org.apache.hadoop.util.ToolRunner;



import GroupingManager.RedWrite;
import IndexingScript.IndexInSolr;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import com.google.gson.Gson;

public class CopyOfProductClustering extends Configured implements org.apache.hadoop.util.Tool 
{
	
	static Gson g = new Gson();
	static String INPUT_TABLE_NAME = "";
	static String OUTPUT_TABLE_NAME = "";
	static String OUTPUT_COLOMN_FAMILY = "";
	static String OUTPUT_QUALIFIR_NAME = "";
	static Logger logger;
	static AmazonDynamoDBClient client;

	public static class Mapp extends MapReduceBase implements
			Mapper<Text, Text, Text, Text> {
		String hostUrl = null;
		String family = null;
		String qualifier = null;
		String output_table_name = null;
		private Text word=new Text();
		private  Text html=new Text();
		File file ;
		FileOutputStream f;
		
		public void configure(JobConf job)
		{
		super.configure(job);
		hostUrl = job.get("hostUrl");
		family = job.get("family");
		qualifier = job.get("qualifier");
		output_table_name = job.get("output_table_name");
		
		logger = Logger.getLogger("MyLog");
				FileHandler fh;
				try {
					// This block configure the logger with handler and formatter
					fh = new FileHandler("GroupingSlave.log");
					logger.addHandler(fh);
					SimpleFormatter formatter = new SimpleFormatter();
					fh.setFormatter(formatter);
		
					// the following statement is used to log any messages
					logger.info("GroupingSlave Log File");
		
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		
//		
		public void map(Text key, Text value,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			String[] store_url=key.toString().split("~!~");
			String store=store_url[0];
			String product_url=store_url[1];
			System.out.println(store);
			System.out.println(product_url);
			GroupingScriptFunctions.GroupingFunction(product_url, value.toString(), store, hostUrl, logger);
			output.collect(new Text(), new Text());
		}
	}

	@Override
	public int run(String args[]) throws Exception {
    	
		JobConf conf = new JobConf(CopyOfProductClustering.class);
		conf.set("family", OUTPUT_COLOMN_FAMILY);
		conf.set("qualifier", OUTPUT_QUALIFIR_NAME);
		conf.set("hostUrl", args[1]);
		conf.set("output_table_name", OUTPUT_TABLE_NAME);
		conf.set("hbase.zookeeper.quorum", args[1]);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", args[1] + ":60000");

		conf.setJobName("Grouping");
		conf.setJarByClass(CopyOfProductClustering.class);
		conf.setMapperClass(Mapp.class);
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);
		conf.setNumReduceTasks(0);
		conf.setInputFormat(SequenceFileInputFormat.class);
		conf.setOutputFormat(NullOutputFormat.class);
		FileInputFormat.addInputPath(conf,new Path(args[0]));
		
		JobClient.runJob(conf);
		return 1;
	}
	public static void main(String[] args) throws Exception 
	{
		System.out.println("=================================================================================");
		System.out.println("|							   Product Grouping 								|");
		System.out.println("=================================================================================");
		//RedWrite.Config_table(OUTPUT_TABLE_NAME, OUTPUT_COLOMN_FAMILY, args[1]); // what is args[1]
		int c = 0;
		c = ToolRunner.run(new Configuration(), new CopyOfProductClustering(), args);
		//=====================================================================================
		AWSCredentials credentials = new PropertiesCredentials(
				CopyOfProductClustering.class
						.getResourceAsStream("AwsCredentials.properties"));
		client = new AmazonDynamoDBClient(credentials);
		createLogger();		

		logger.info("Now copy data from HashCount(hbase) to HashCount(Dynamodb)...");
		Functions.CopyDataFromHashCountToDynamodbHashCount("HashCount",
				args[1], client, logger);
		logger.info("copying data from HashCount(hbase) to HashCount(Dynamodb) is finished.");
		logger.info("Now copy data from HashContent(hbase) to HashContent(Dynamodb)...");
		Functions.CopyDataFromHashContentToDynamodbHashContent("HashContent",
				args[1], logger, client);
		logger.info("copying data from HashContent(hbase) to HashContent(Dynamodb) is finished.");
		logger.info("Now copy data from HashContent(hbase) to AmpleCluster(Dynamodb)...");
		Functions.CopyDataAmpleClusterToDynamodbAmpleCluster("AmpleCluster",
				args[1], logger, client);
		logger.info("copying data from HashContent(hbase) to AmpleCluster(Dynamodb) is finished.");

		// ------------------------------------------------------------------
		// Code for Making Entry in solr_data in hbase
		// ------------------------------------------------------------------
		RedWrite.Config_table("solr_data", "cf", args[1]);
		Functions.MakeEntryInSolrDataFromAmpleClusterInhbase(args[1]);

		// ------------------------------------------------------------------
		// Code for INndexing in Solr
		// ------------------------------------------------------------------
		try{
		String hbaseIp = args[1];
		String solrIp = args[2];
		String solrPort = args[3];
		IndexInSolr.indexingInSolr(hbaseIp, solrIp, solrPort);
		// ------------------------------------------------------------------
		// Code for copying data from solr_data hbase to dynamodb solr_data
		// ------------------------------------------------------------------
		logger.info("copying data from HashContent(hbase) to HashContent(Dynamodb) is finished.");
		logger.info("Now copy data from solr_data(hbase) to solr_data(Dynamodb)...");
		Functions.CopyDataAmpleClusterToDynamodbAmpleCluster("solr_data",
				args[1], logger, client);
		logger.info("copying data from solr_data(hbase) to solr_data(Dynamodb) is finished.");
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("1. fileName Location\n 2. hbaseIp\n 3. SolrIp\n 4. SolrPort");
		}
		// ---------------------------------------------------------------------------------------
		// Empty table parsed_data in hbase
		// ---------------------------------------------------------------------------------------
		//logger.info("Empty table 'parsed_data' from hbase started....");

		/**
		 * Get List of urls
		 * */
//		List<String> listofUrl = RedWrite.getAllRecords("parsed_data", args[0]);
//		for (int i = 0; i < listofUrl.size(); i++) {
//		RedWrite.delRecord("parsed_data", listofUrl.get(i), args[0]);
//		}
		//logger.info("Empty table 'parsed_data' from hbase is finished.");

		//=====================================================================================
		System.exit(c);
	}
		
	    // ------------------------------------------------------------------------------------------------
		// function for creating client
		// ------------------------------------------------------------------------------------------------
	private static void createClient() throws IOException {

		AWSCredentials credentials = new PropertiesCredentials(
				CopyOfProductClustering.class
						.getResourceAsStream("AwsCredentials.properties"));
		client = new AmazonDynamoDBClient(credentials);

	}
		// ------------------------------------------------------------------------------------------------
		// function for create log
		// ------------------------------------------------------------------------------------------------
		public static void createLogger() {
			logger = Logger.getLogger("MyLog");
			FileHandler fh;
			try {
				// This block configure the logger with handler and formatter
				fh = new FileHandler("GroupingSlave.log");
				logger.addHandler(fh);
				SimpleFormatter formatter = new SimpleFormatter();
				fh.setFormatter(formatter);
				// the following statement is used to log any messages
				logger.info("GroupingScript Log File");
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}// fn close
}