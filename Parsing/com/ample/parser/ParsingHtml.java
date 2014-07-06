package com.ample.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.lib.NullOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.apache.nutch.protocol.Content;
import org.apache.nutch.util.HadoopFSUtil;
import org.apache.nutch.util.NutchConfiguration;
import org.htmlcleaner.XPatherException;
import com.google.gson.Gson;

public class ParsingHtml extends Configured implements org.apache.hadoop.util.Tool 
{
	
	static Gson g = new Gson();
	// static String INPUT_TABLE_NAME = "host_table";
	static String OUTPUT_TABLE_NAME = "raw_parsing_result";
	static String OUTPUT_COLOMN_FAMILY = "attributes";
	static String OUTPUT_QUALIFIR_NAME = "name";

	public static class Mapp extends MapReduceBase implements
			Mapper<Text, Content, Text, Text> {
		String hostUrl = null;
		String family = null;
		String qualifier = null;
		String output_table_name = null;

		public void configure(JobConf job) {
			hostUrl = job.get("hostUrl");
			family = job.get("family");
			qualifier = job.get("qualifier");
			output_table_name = job.get("output_table_name");
		}

		public void map(Text key, Content value,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {

			// System.out.println("Host URL : " + hostUrl);
			// System.out.println("Family : " + family);
			// System.out.println("Qualifier : " + qualifier);
			// System.out.println("Output table name : " + output_table_name);
			// System.out.println("Key : " + key.toString());
			String html = "";
			URL urls = new URL(key.toString());
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new ByteArrayInputStream(value.getContent())));
			String line;
			while ((line = reader.readLine()) != null) {
				html = html + line;
			}
			// System.out.println("Host : " + urls.getHost());
			/*
			 * Get url host name
			 */
			System.out.println("URL : " + key.toString());
			String storeName = RedWrite.getOneRecord("product_page_url",
					key.toString(), "cf", "storeName", hostUrl);
			if (storeName != null) {
				System.out.println("Store Name : " + storeName);
				String getRegex = RedWrite.getAllRecords("entity_info",
						hostUrl, storeName);
				System.out.println("Regex : " + getRegex);
				Gson reg = new Gson();
				Map<String, String> rs = reg.fromJson(getRegex.toString(),
						Map.class);
				HashMap values_retrieved_hashmap = new HashMap();

				if (!rs.isEmpty()) {
					for (Map.Entry<String, String> kv1 : rs.entrySet()) {
						HashMap values_retrieved_hashmap1 = new HashMap();
						if (new String(kv1.getValue()).contains("regex")) {
							// System.out.println("regex");
							String regexName = new String(kv1.getKey());
							String regexVal = new String(kv1.getValue());
							if(regexName!=null && regexVal!=null)
							{
							values_retrieved_hashmap1 = demoForregex
									.RegexParse(html, regexName,regexVal);
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
								  values_retrieved_hashmap2 = demoForXpath.XpathParse(html,xpathName,xpathVal);
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

						/* Inserting the values after arsing into table */
						// RedWrite.addRecord("raw_parsing_result", url_string,
						// "data", "name", product_json, hostUrl);
						// Put put = new Put(url_string.getBytes());
						// put.add("attributes".getBytes(), "name".getBytes(),
						// product_json.getBytes());
						try 
						{
                                	RedWrite.addRecord(output_table_name,
									key.toString(), family, qualifier,
									product_json, hostUrl);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}// end of store if
			output.collect(new Text(), new Text());
		}
	}

	@Override
	public int run(String args[]) throws Exception {
    	
		JobConf conf = new JobConf(ParsingHtml.class);
		conf.set("family", OUTPUT_COLOMN_FAMILY);
		conf.set("qualifier", OUTPUT_QUALIFIR_NAME);
//		conf.set("hostUrl", args[0]);
//		conf.set("output_table_name", OUTPUT_TABLE_NAME);
//		conf.set("hbase.zookeeper.quorum", args[0]);
//		conf.set("hbase.zookeeper.property.clientPort", "2181");
//		conf.set("hbase.master", args[0] + ":60000");

		conf.setJobName("Parsing HTML");
		conf.setJarByClass(ParsingHtml.class);
		conf.setMapperClass(Mapp.class);
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setInputFormat(SequenceFileInputFormat.class);
		conf.setOutputFormat(NullOutputFormat.class);
		// Reading the total path
		Configuration conf1 = NutchConfiguration.create();
		final FileSystem fs = FileSystem.get(conf1);
		
		// args[1] segment path
		
		FileStatus[] fstats = fs.listStatus(new Path(args[1]),
				HadoopFSUtil.getPassDirectoriesFilter(fs));
		Path[] files = HadoopFSUtil.getPaths(fstats);
		
		if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
				FileInputFormat.addInputPath(conf, new Path(
						files[files.length - 1], "content"));
			}
        }
		
		JobClient.runJob(conf);
		return 1;
	}
	public static void main(String[] args) throws Exception 
	{
		System.out.println("Parser Started ........");
//		RedWrite.Config_table(OUTPUT_TABLE_NAME, OUTPUT_COLOMN_FAMILY, args[0]);
		int c = 0;
		c = ToolRunner.run(new Configuration(), new ParsingHtml(), args);
		System.exit(c);
	}

//	//Reduce function
//	public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
//		String hostUrl = null;
//		String family = null;
//		String qualifier = null;
//		String output_table_name = null;
//		private Text word=new Text();
//		private  Text html=new Text();
//		public void configure(JobConf job)
//		{
//		super.configure(job);
//		hostUrl = job.get("hostUrl");
//		family = job.get("family");
//		qualifier = job.get("qualifier");
//		output_table_name = job.get("output_table_name");
//		}
//		
////		
//			      @SuppressWarnings("unchecked")
//				public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
////			     
//			    	  String[] store_url=key.toString().split("~!~");
//			    	 while(values.hasNext()){
//			    		 word=values.next();
//			    		 
//			    	 }
//			    	 html.set(word);
////			    	    System.out.println(word);
////						String storeName = RedWrite.getOneRecord("product_page_url",
////								key.toString(), "cf", "storeName", hostUrl);
//			    	    String storeName=store_url[0];
//						if (storeName != null) {
//							System.out.println("Store Name : " + storeName);
//							String getRegex = RedWrite.getAllRecords("entity_info",
//									hostUrl, storeName);
//							System.out.println("Regex : " + getRegex);
//							Gson reg = new Gson();
//							Map<String, String> rs = reg.fromJson(getRegex.toString(),
//									Map.class);
//							HashMap values_retrieved_hashmap = new HashMap();
//
//							if (!rs.isEmpty()) {
//								for (Map.Entry<String, String> kv1 : rs.entrySet()) {
//									HashMap values_retrieved_hashmap1 = new HashMap();
//									if (new String(kv1.getValue()).contains("regex")) {
//////										// System.out.println("regex");
//										String regexName = new String(kv1.getKey());
//										String regexVal = new String(kv1.getValue());
//										if(regexName!=null && regexVal!=null)
//										{
//										values_retrieved_hashmap1 = demoForregex
//												.RegexParse(word.toString(), regexName,regexVal);
//										}// if close
//									}
//									/* for Xpath */
//									HashMap values_retrieved_hashmap2 = new HashMap();
//									// System.out.println("Regex Name : " + kv1.getKey() +
//									// "         "
//									// + "Value : " + kv1.getValue());
//									if (new String(kv1.getValue()).contains("xpath")) {
//										try {
//											String xpathName = new String(kv1.getKey());
//											String xpathVal = new String(kv1.getValue());
//											if(xpathName!=null && xpathVal!=null)
//											{
//											  //calling function for evaluating using xpath	
//											  values_retrieved_hashmap2 = demoForXpath.XpathParse(word.toString(),xpathName,xpathVal);
//											}// if close
//										} catch (XPathExpressionException e) {
//											
//											e.printStackTrace();
//										} catch (XPatherException e) {
//										
//											e.printStackTrace();
//										}
//									}
//
//									values_retrieved_hashmap
//											.putAll(values_retrieved_hashmap1);
//
//									values_retrieved_hashmap
//											.putAll(values_retrieved_hashmap2);
//
//								}// end of outer for
//							}// end of outer if
////
////							/* checking if hashMap is empty or not */
//							if (!values_retrieved_hashmap.isEmpty()) {
//								HashMap<String, String> url = new HashMap<String, String>();
//								url.put("url", key.toString());
//								values_retrieved_hashmap.putAll(url);
////
////								/* Converting HashMap to json */
//								Gson g = new Gson();
//								String product_json = g.toJson(values_retrieved_hashmap);
////								/* Retrieving the result and Storing in the parsing table */
////								// System.out.println("Json : " + product_json);
//								try {
//									try 
//									{
//			                                	RedWrite.addRecord(output_table_name,
//												store_url[1], family, qualifier,
//												product_json, hostUrl);
//			    	    
//									} catch (Exception e) {
//										e.printStackTrace();
//									}
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//							}
//						}// end of store if
//			        output.collect(new Text(), new Text());
//			      }
//			    }
//}
}