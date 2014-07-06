package com.ample.parser;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.nutch.protocol.Content;
import org.apache.nutch.util.HadoopFSUtil;
import org.apache.nutch.util.NutchConfiguration;
import org.htmlcleaner.XPatherException;

import com.ample.parser.RedWrite;
import com.ample.parser.demoForXpath;
import com.ample.parser.demoForregex;
import com.google.gson.Gson;

public class ParseSequencialFile extends Configured implements org.apache.hadoop.util.Tool 
{
	
	static Gson g = new Gson();
	// static String INPUT_TABLE_NAME = "host_table";
	static String OUTPUT_TABLE_NAME = "raw_parsing_result";
	static String OUTPUT_COLOMN_FAMILY = "attributes";
	static String OUTPUT_QUALIFIR_NAME = "name";
	static String TABLE="failedproduct";
	static String FAMILY="cf";
	static Logger logger;

	public static class Mapp extends MapReduceBase implements
			Mapper<Text, Text, Text, Text> {
		String hostUrl = null;
		String family = null;
		String qualifier = null;
		String output_table_name = null;
		String TABLE=null;
		String FAMILY=null;
		private Text word=new Text();
		private  Text html=new Text();
		File file ;
		FileOutputStream f;
//		Logger logger;
		
		public void configure(JobConf job)
		{
		super.configure(job);
		hostUrl = job.get("hostUrl");
		family = job.get("family");
		qualifier = job.get("qualifier");
		output_table_name = job.get("output_table_name");
		TABLE=job.get("TABLE");
		FAMILY=job.get("FAMILY");
//		file = new File("/home/ubuntu/UnParsed_url.txt");
//		try {
//			 f = new FileOutputStream(file);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        logger = Logger.getLogger("MyLog");
//		
//		FileHandler fh;
//		try {
//            // This block configure the logger with handler and formatter
//			fh = new FileHandler("Parsing.log");
//			logger.addHandler(fh);
//			SimpleFormatter formatter = new SimpleFormatter();
//			fh.setFormatter(formatter);
//            // the following statement is used to log any messages
//
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		

		}
		
//		

		@SuppressWarnings("unchecked")
		public void map(Text key, Text value,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			String[] store_url=key.toString().split("~!~");
			String store=store_url[0];
			String product_url=store_url[1];
//			System.out.println(store);
//			System.out.println(product_url);
			if (store != null) {
			
				System.out.println("Store Name : " + store);
//				logger.info("Parsing started for url  "+product_url  );
				String getRegex = RedWrite.getAllRecords("entity_info",
						hostUrl, store);
				System.out.println("Regex : " + getRegex);
				Gson reg = new Gson();
				HashMap values_retrieved_hashmap = new HashMap();
				if(!getRegex.equals("")){
				Map<String, String> rs = reg.fromJson(getRegex.toString(),
						Map.class);
				
				if (!rs.equals(null)&&!rs.equals("")) {
					for (Map.Entry<String, String> kv1 : rs.entrySet()) {
						HashMap values_retrieved_hashmap1 = new HashMap();
						if (new String(kv1.getValue()).contains("regex")) {
							// System.out.println("regex");
							String regexName = new String(kv1.getKey());
							String regexVal = new String(kv1.getValue());
							if(regexName!=null && regexVal!=null)
							{
							values_retrieved_hashmap1 = demoForregex.RegexParse(value.toString(), regexName,regexVal);
							}// if close
						}
						/* for Xpath */
						HashMap values_retrieved_hashmap2 = new HashMap();
						// System.out.println("Regex Name : " + kv1.getKey() +
						// "         "
						// + "Value : " + kv1.getValue());
						if (new String(kv1.getValue()).contains("xpath")) {
							try {
								String xpathName = new String(kv1.getKey());
								String xpathVal = new String(kv1.getValue());
								if(xpathName!=null && xpathVal!=null)
								{
								  //calling function for evaluating using xpath	
								  values_retrieved_hashmap2 = demoForXpath.XpathParse(value.toString(),xpathName,xpathVal);
								}// if close
							} catch (XPathExpressionException e) {
								
								e.printStackTrace();
							} catch (XPatherException e) {
							
								e.printStackTrace();
							}
						}

						values_retrieved_hashmap
								.putAll(values_retrieved_hashmap1);

						values_retrieved_hashmap
								.putAll(values_retrieved_hashmap2);

					}// end of outer for
				}// end of outer if
				}//end of regex check if
//				logger.info("Parsing ended  for url  "+product_url  );
				/* checking if hashMap is empty or not */
				if (!values_retrieved_hashmap.isEmpty()) {
					HashMap<String, String> url = new HashMap<String, String>();
					url.put("url", key.toString());
					values_retrieved_hashmap.putAll(url);

					/* Converting HashMap to json */
					Gson g = new Gson();
					String product_json = g.toJson(values_retrieved_hashmap);
					/* Retrieving the result and Storing in the parsing table */
					// System.out.println("Json : " + product_json);
					try {
						try 
						{
//							logger.info("Inserting into hbase table started for url  "+product_url  );
                                	RedWrite.addRecord(output_table_name,
                                			product_url, family, store,
									product_json, hostUrl);
//                                	logger.info("Inserting into hbase table  "+product_url  );
//                                	RedWrite.addRecord(output_table_name,
//                                			store_url[1], family, qualifier,
//									product_json, hostUrl);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else{
					try {
						RedWrite.addRecord(TABLE,
								product_url, FAMILY, store,
						"failed", hostUrl);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
									
				}
				
			}// end of store if
			
			/*
			 * Get url host name
			 */
//			System.out.println("URL : " + key.toString());

			output.collect(new Text(), new Text());
		}
	}

	@Override
	public int run(String args[]) throws Exception {
    	
		JobConf conf = new JobConf(ParseSequencialFile.class);
		conf.set("family", OUTPUT_COLOMN_FAMILY);
		conf.set("qualifier", OUTPUT_QUALIFIR_NAME);
		conf.set("hostUrl", args[1]);
		conf.set("output_table_name", OUTPUT_TABLE_NAME);
		conf.set("hbase.zookeeper.quorum", args[1]);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", args[1] + ":60000");
//		conf.set("mapred.child.java.opts", "-Xmx4096m");
		conf.set("TABLE",TABLE);
		conf.set("FAMILY",FAMILY);

		conf.setJobName("Parsing HTML");
		conf.setJarByClass(ParseSequencialFile.class);
		conf.setMapperClass(Mapp.class);
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);
		conf.setNumReduceTasks(0);
		conf.setInputFormat(SequenceFileInputFormat.class);
		conf.setOutputFormat(NullOutputFormat.class);
		FileInputFormat.addInputPath(conf,new Path(args[0]));
        
//		conf.set("logger",logger);
		
		JobClient.runJob(conf);
		return 1;
	}
	public static void main(String[] args) throws Exception 
	{
		System.out.println("Parser Started ........");
		RedWrite.Config_table(OUTPUT_TABLE_NAME, OUTPUT_COLOMN_FAMILY, args[1]);
		int c = 0;
		c = ToolRunner.run(new Configuration(), new ParseSequencialFile(), args);
		System.exit(c);
//		InsertIntoDynamo.Insert(args[1]);
	}
}