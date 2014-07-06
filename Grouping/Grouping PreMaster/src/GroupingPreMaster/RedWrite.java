package GroupingPreMaster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.amazonaws.services.dynamodb.model.AttributeValue;

public class RedWrite {

	private static Configuration conf = null;

	public static void creatTable(String tableName, String[] familys, String arg)
			throws Exception {

		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", arg);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", arg + ":60000");
		HBaseAdmin admin = new HBaseAdmin(conf);
		if (admin.tableExists(tableName)) {
			System.out.println("table already exists!");
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			for (int i = 0; i < familys.length; i++) {
				tableDesc.addFamily(new HColumnDescriptor(familys[i]));
			}
			admin.createTable(tableDesc);
			admin.close();
			System.out.println("create table " + tableName + " ok.");
		}
	}

	/**
	 * Delete a table
	 */
	public static void deleteTable(String tableName, String arg)
			throws Exception {
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", arg);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", arg + ":60000");
		try {
			HBaseAdmin admin = new HBaseAdmin(conf);
			admin.disableTable(tableName);
			admin.deleteTable(tableName);
			System.out.println("delete table " + tableName + " ok.");
			admin.close();
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Put (or insert) a row
	 */
	public static void addRecord(String tableName, String rowKey,
			String family, String qualifier, String product_json, String arg)
			throws Exception {
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", arg);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", arg + ":60000");
		try {
			HTable table = new HTable(conf, tableName);
			Put put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier),
					Bytes.toBytes(product_json));
			table.put(put);
			table.close();
			// System.out.println("Record Added to hbase");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete a row
	 * @param masterIp 
	 */
	public static void delRecord(String tableName, String rowKey, String range, String arg)
			throws IOException {
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", arg);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", arg + ":60000");
		HTable table = new HTable(conf, tableName);
		List<Delete> list = new ArrayList<Delete>();
		Delete del = new Delete(Bytes.toBytes(rowKey));
		del.deleteFamily(Bytes.toBytes(range));
		list.add(del);
		table.delete(list);
		table.close();
		//System.out.println("del recored " + rowKey + " ok.");
	}

	/**
	 * Get a row
	 */
	@SuppressWarnings("resource")
	public static String getOneRecord(String tableName, String rowKey,
			String qualifier, String column_name, String arg)
			throws IOException {

		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", arg);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", arg + ":60000");
		HTable table = new HTable(conf, tableName);
		Get get = new Get(rowKey.getBytes());
		get.addColumn(Bytes.toBytes(qualifier), Bytes.toBytes(column_name));
		Result rs = table.get(get);
		for (KeyValue kv : rs.raw()) {
			// System.out.print(new String(kv.getRow()) + " " );
			// System.out.print(new String(kv.getFamily()) + ":" );
			// System.out.print(new String(kv.getQualifier()) + " " );
			// System.out.print(kv.getTimestamp() + " " );
			return new String(kv.getValue());
		}
		table.close();
		return "Sorry!";
	}

	public static Map<String, AttributeValue> getAllAttributes(
			String tableName, String rowKey, String family, String arg)
			throws IOException {

		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", arg);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", arg + ":60000");

		Map<String, AttributeValue> return_hashMap = new HashMap<String, AttributeValue>();
		HTable table = new HTable(conf, tableName);
		Get get = new Get(rowKey.getBytes());
		get.addFamily(Bytes.toBytes(family));
		Result rs = table.get(get);
		for (KeyValue kv : rs.raw()) {
			// System.out.print(new String(kv.getRow()) + " " );
			// System.out.print(new String(kv.getFamily()) + ":" );
			// System.out.print(new String(kv.getQualifier()) + " " );
			// System.out.print(kv.getTimestamp() + " " );
			return_hashMap.put(new String(kv.getQualifier()),
					new AttributeValue().withS(new String(kv.getValue())));
		}
		table.close();
		return return_hashMap;
	}

	/**
	 * Scan (or list) a table
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<String> getAllRecords(String tableName, String arg) {

		List<String> list = new ArrayList<String>();

		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", arg);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", arg + ":60000");
		try {
			HTable table = new HTable(conf, tableName);
			Scan s = new Scan();
			ResultScanner ss = table.getScanner(s);
			for (Result r : ss) {
				for (KeyValue kv : r.raw()) {
					String row;
					row = new String(kv.getRow());
					list.add(row);
					break;
				}
				// System.out.println("---------------------");

			}
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("resource")
	public static void Config_table(String TABLENAME, String family, String arg)
			throws IOException {
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", arg);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", arg + ":60000");
		HBaseAdmin admin = new HBaseAdmin(conf);
		if (admin.tableExists(TABLENAME)) {
			System.out.println("table already exists!");
			admin.disableTable(TABLENAME);
			admin.deleteTable(TABLENAME);
			System.out.println("deleted table " + TABLENAME + " ok.");
		}

		HTableDescriptor tableDesc = new HTableDescriptor(TABLENAME);
		tableDesc.addFamily(new HColumnDescriptor(family));
		admin.createTable(tableDesc);
		System.out.println("created table " + TABLENAME + " ok.");
	}

	public static int getBufferedCount(String tableName, String masterIp) {
		ResultScanner rs = null;
		int count = 0;
		try {
			Configuration conf = null;
			conf = HBaseConfiguration.create();

			conf.set("hbase.zookeeper.quorum", masterIp);
			conf.set("hbase.zookeeper.property.clientPort", "2181");
			conf.set("hbase.master", masterIp + ":60000");
			HTable table = new HTable(conf, tableName);
			Scan s = new Scan();
			rs = table.getScanner(s);

			for (Result r : rs) {
				for (KeyValue kv1 : r.raw()) {
					count++;
				}// end of inner for loop
			}// end of outer for loop
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Total Number of rows :" + count);
		return count;
	}
}
