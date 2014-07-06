package IndexingScript;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import com.google.gson.Gson;

public class IndexInSolr {
	static Logger logger;

	public static void main(String[] args) {
		try {
			indexingInSolr("localhost", "localhost", "8984");
		} catch (IOException e) {

			e.printStackTrace();
		}
	} // main close

	// functin for indexing in solr
	public static void indexingInSolr(String masterIP, String solrUrl,
			String solrPort) throws IOException {
		createLogger();
		// this will continously run for checking a buffertable
		String tableName = "solr_data";
		int limit = 100;

		// code for scanning data from hbase table => "buffered_solr_data"
		// code for deleting data from hbase table => "buffered_solr_data"
		// code for indexing in solr and data will be fetched from
		// buffer_solr_data

		logger.info("Fetching from AmpleCluster is being started...");
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIP);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIP + ":60000");
		HTable table = null;
		try {
			table = new HTable(conf, tableName);
		} catch (IOException e2) {
			logger.info(e2.toString());
			e2.printStackTrace();
		}

		Scan scan = new Scan();
		scan.setBatch(100);

		// long d = (long) 100;
		ResultScanner ss = null;
		try {
			ss = table.getScanner(scan);
		} catch (IOException e2) {
			logger.info(e2.toString());
			e2.printStackTrace();
		}
		logger.info("Fetching from buffer_solr_data is finished.");
		logger.info("Indexing in Solr is started...");

		// making object of solr
		HttpSolrServer server = new HttpSolrServer("http://" + solrUrl + ":"
				+ solrPort + "/solr");
		int count = 0;

		for (Result r : ss) {

			for (KeyValue kv : r.raw()) {
				String row = new String(kv.getRow());
				String qualifier = new String(kv.getQualifier());
				// if (qualifier.equals("data"))
				{
					String value = new String(kv.getValue());
					logger.info("Solr indexing function is called here.");
					indexingInSolr(row, value, server);
					logger.info("record is added into Solr.");

					// logger.info("record is deleting from AmpleCluster in hbase...");
					// delRecord(row, "AmpleCluster", masterIP);
					// logger.info("record is deleting from AmpleCluster in hbase is finished.");

				}// inner for close

			}// outer for close

			count++;
			if (count % limit == 0) {
				try {
					try {
						server.commit();
					} catch (IOException e) {
						logger.info(e.toString());
						e.printStackTrace();
					}
				} catch (SolrServerException e) {
					logger.info(e.toString());
					e.printStackTrace();
				}
			}// if close

		} // for close
		try {
			server.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			table.close();
		} catch (IOException e1) {
			logger.info(e1.toString());
			e1.printStackTrace();
		}

	}// function close

	// --------------------------------------------------------------------------
	// This function deletes data from hbase
	// --------------------------------------------------------------------------
	private static void delRecord(String rowKey, String tableName,
			String masterIP) throws IOException {
		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", masterIP);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", masterIP + ":60000");
		HTable table = new HTable(conf, tableName);
		List<Delete> list = new ArrayList<Delete>();
		Delete del = new Delete(rowKey.getBytes());
		list.add(del);
		table.delete(list);
		table.close();
		logger.info(rowKey + " from solr_data is deleted.");

	}

	// function for creating logger
	private static void createLogger() {
		logger = Logger.getLogger("MyLog");
		FileHandler fh;
		try {

			// This block configure the logger with handler and formatter
			fh = new FileHandler("IndexingInSolr.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

			// the following statement is used to log any messages
			logger.info("IndexingInSolr Details");

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("while loop is started");

	}// function close

	// function for indexing in solr

	public static void indexingInSolr(String id, String value,
			HttpSolrServer server) {
		//System.out.println(value);
		Gson g = new Gson();
		Map<Object, Object> solrData = new HashMap<Object, Object>();
		solrData = g.fromJson(value, Map.class);

		// counter for doc information

		SolrInputDocument doc = new SolrInputDocument();

		doc.addField("cluster_id", id);

		for (Map.Entry<Object, Object> item2 : solrData.entrySet()) {
			if (item2.getKey().toString().equalsIgnoreCase("image")) {
				String image_img[] = item2.getValue().toString().split("~!~");
				for (int img = 0; img < image_img.length; img++) {
					image_img[img] = image_img[img].replace("\"", "");
					doc.addField("image", image_img[img]);

				}
						
					
						if (item2.getKey().toString().equalsIgnoreCase("product_title")) {
							doc.addField("product_title", item2.getValue());
			
						}
			
						if (item2.getKey().toString().equalsIgnoreCase("avg_price")) {
							doc.addField("avg_price", item2.getValue());
			
						}
						if (item2.getKey().toString().equalsIgnoreCase("min_price")) {
							doc.addField("min_price", item2.getValue());
			
						}
						if (item2.getKey().toString().equalsIgnoreCase("max_price")) {
							doc.addField("max_price", item2.getValue());
			
						}
				
						if (item2.getKey().toString().equalsIgnoreCase("counts")) {
							doc.addField("counts", item2.getValue());
			
						}
			
						if (item2.getKey().toString().equalsIgnoreCase("url")) {
							doc.addField("url", item2.getValue());
			
						}
						if (item2.getKey().toString().equalsIgnoreCase("description")) {
							doc.addField("description", item2.getValue());
			
						}
						if (item2.getKey().toString().equalsIgnoreCase("storeName")) {
							//System.out.println(item2.getValue());
							String temp = g.toJson(item2.getValue());
							doc.addField("storeName", temp);
						}
						if (item2.getKey().toString().equalsIgnoreCase("attributes")) {
							String temp = g.toJson(item2.getValue());
							doc.addField("attributes", temp);
			
						}
						if (item2.getKey().toString().equalsIgnoreCase("color")) {
							doc.addField("color", item2.getValue());
						}
						if (item2.getKey().toString().equalsIgnoreCase("brand")) {
							doc.addField("brand", item2.getValue());
						}
						if (item2.getKey().toString().equalsIgnoreCase("type")) {
							doc.addField("type", item2.getValue());
						}
						if (item2.getKey().toString().equalsIgnoreCase("price")) {
							doc.addField("price", item2.getValue());
						}
						if (item2.getKey().toString().equalsIgnoreCase("shipping_charges")) {
							doc.addField("shipping_charges", item2.getValue());
						}
						if (item2.getKey().toString().equalsIgnoreCase("shipping_time")) {
							doc.addField("shipping_time", item2.getValue());
						}
						if (item2.getKey().toString().equalsIgnoreCase("rating")) {
							doc.addField("rating", item2.getValue());
						}
			}// if closes
		}

		try {
			server.add(doc);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}// function is closed
}// class close
