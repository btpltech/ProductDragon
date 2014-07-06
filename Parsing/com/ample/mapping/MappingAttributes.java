package com.ample.mapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.ToolRunner;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.gson.Gson;

public class MappingAttributes extends Configured implements
		org.apache.hadoop.util.Tool {
//	static AmazonDynamoDBClient client;
//	private static void createClient() throws IOException {
//		
//				AWSCredentials credentials = new PropertiesCredentials(
//						InsertIntoDynamo.class
//								.getResourceAsStream("AwsCredentials.properties"));
//				client = new AmazonDynamoDBClient(credentials);
//				// calling function
//		
//			}

	static String INPUT_TABLE_NAME = "raw_parsing_result";
	static String COLUMN_FAMILY = "attributes";
	static String QUALIFIER = "name";
	static String OUTPUT_TABLE_NAME = "parsing_result";
	static String RAW_ACTUAL_ATTRIBUTE_TABLE_NAME = "raw_actual_attribute_name";

	@Override
	public int run(String args[]) throws Exception {

		Job job = new Job(getConf());
		Configuration conf = job.getConfiguration();
		conf.set("hbase.zookeeper.quorum", args[0]);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", args[0] + ":60000");
		conf.set("hostUrl", args[0]);
		conf.set("family", "attributes");
		conf.set("qualifier", "name");

		job.setJobName("Raw Actual Attributes Mapping");
		job.setJarByClass(MappingAttributes.class);

		Scan scan = new Scan();

		TableMapReduceUtil.initTableMapperJob(INPUT_TABLE_NAME, scan,
				Mapper.class, ImmutableBytesWritable.class, Put.class, job);
		TableMapReduceUtil.initTableReducerJob(OUTPUT_TABLE_NAME, null, job);
		boolean jobSucceeded = job.waitForCompletion(true);
		if (jobSucceeded) {
			return 0;
		} else {
			return -1;
		}

	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		Logger logger = Logger.getLogger("MyLog");
		FileHandler fh;
		try {

			// This block configure the logger with handler and formatter
			fh = new FileHandler("Mapping.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

			// the following statement is used to log any messages
			logger.info("Mapping Log File");

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Program of Attribute Name Mapping started");
		logger.info("creating table");
		CheckTableStatus chk = new CheckTableStatus();
		chk.Config_table(OUTPUT_TABLE_NAME, COLUMN_FAMILY, args[0]);
		logger.info("creating table ended");
		int c = 0;
		logger.info("job running started");
		c = ToolRunner.run(new Configuration(), new MappingAttributes(), args);
		
		logger.info("job running ended");
		System.out.println("Inserting into dynamo is started.");
//		logger.info("Inserting into dynamo is started");
//		InsertIntoDynamo in =new InsertIntoDynamo();
//		AWSCredentials credentials = new PropertiesCredentials(
//				MappingAttributes.class
//						.getResourceAsStream("AwsCredentials.properties"));
//		AmazonDynamoDBClient client= new AmazonDynamoDBClient(credentials);
//		in.Insert(args[0],logger,client);
//		logger.info("Inserting into dynamo is finished");
		System.out.println("Inserting into dynamo is finished.....");
		System.exit(c);
	}

	static class Mapper extends TableMapper<ImmutableBytesWritable, Put> {
		String hostUrl = new String();
		String family = new String();
		String qualifier = new String();

		static Gson g = new Gson();
		static Match match = new Match();// class for matching attribute

		public void setup(Context context) {
			hostUrl = context.getConfiguration().get("hostUrl");
			family = context.getConfiguration().get("family");
			qualifier = context.getConfiguration().get("qualifier");
		}

		@Override
		public void map(ImmutableBytesWritable row, Result result,
				Context context) throws IOException, InterruptedException {
			// extract userKey from the compositeKey (userId + counter)

			for (KeyValue kv : result.raw()) {
				String key = new String(kv.getRow());
				String value = new String(kv.getValue());
				String qualifier=new String(kv.getQualifier());
				String qualifier_key=qualifier+"!ample!"+key;
				// System.out.println(key);
				// System.out.println(value);
				context.write(row,
						mappingofattributename(new Text(qualifier_key), new Text(value)));
			}
			// System.out.println(".............");
		}

		@SuppressWarnings("unchecked")
		private Put mappingofattributename(Text store_producturl, Text json_value)
				throws IOException {
			HashMap<String, String> whole_input_hashmap = new HashMap<String, String>();
			HashMap<String, String> final_output_hashmap = new HashMap<String, String>();
			Map<String, String> final_inside_hashmap = new HashMap<String, String>();
			whole_input_hashmap = g.fromJson(json_value.toString(),
					HashMap.class);
			for (Entry<String, String> e : whole_input_hashmap.entrySet()) {
				if (e.getKey().equals("attributes")) {
					final_inside_hashmap = match
							.matching(e.getValue(), hostUrl);
					String final_json = g.toJson(final_inside_hashmap);
					final_output_hashmap.put("attributes", final_json);

				} else {

					final_output_hashmap.put(e.getKey(), e.getValue());
				}
			}
			String final_output_json = g.toJson(final_output_hashmap);
			String[] store_product=store_producturl.toString().split("!ample!");
			Put put = new Put(Bytes.toBytes(store_product[1].toString()));
			put.add(family.getBytes(), store_product[0].getBytes(),
					final_output_json.getBytes());
			return put;
		}
	}
}
