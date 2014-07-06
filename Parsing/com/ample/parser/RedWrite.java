package com.ample.parser;

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

public class RedWrite {

	public static Configuration confh = null;

	public static void main(String[] args) {
		// getAllRecords("host_table");
		String cf[] = { "cf" };
		try {
			creatTable("test", cf, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// static {
	// confh = HBaseConfiguration.create();
	// }

	/**
	 * Create a table
	 */
	public static void creatTable(String tableName, String[] familys, String url)
			throws Exception {
		confh = HBaseConfiguration.create();

		confh.set("hbase.zookeeper.quorum", url);
		confh.set("hbase.zookeeper.property.clientPort", "2181");
		confh.set("hbase.master", url + ":60000");
		HBaseAdmin admin = new HBaseAdmin(confh);
		if (admin.tableExists(tableName)) {
			System.out.println("table already exists!");
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			for (int i = 0; i < familys.length; i++) {
				tableDesc.addFamily(new HColumnDescriptor(familys[i]));
			}
			admin.createTable(tableDesc);
			System.out.println("create table " + tableName + " ok.");
		}
	}

	/**
	 * Delete a table
	 */
	public static void deleteTable(String tableName) throws Exception {
		try {
			confh = HBaseConfiguration.create();

			confh.set("hbase.zookeeper.quorum", "ip-10-137-69-104.ec2.internal");
			confh.set("hbase.zookeeper.property.clientPort", "2181");
			confh.set("hbase.master", "ip-10-137-69-104.ec2.internal"
					+ ":60000");
			HBaseAdmin admin = new HBaseAdmin(confh);
			admin.disableTable(tableName);
			admin.deleteTable(tableName);
			System.out.println("delete table " + tableName + " ok.");
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
			String family, String qualifier, String value, String url)
			throws Exception {
		try {
			confh = HBaseConfiguration.create();

			confh.set("hbase.zookeeper.quorum", url);
			confh.set("hbase.zookeeper.property.clientPort", "2181");
			confh.set("hbase.master", url + ":60000");
			HTable table = new HTable(confh, tableName);
			Put put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier),
					Bytes.toBytes(value));
			table.put(put);
			// System.out.println("insert recored " + rowKey + " to table " +
			// tableName + " ok.");
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete a row
	 */
	public static void delRecord(String tableName, String rowKey)
			throws IOException {
		confh = HBaseConfiguration.create();

		confh.set("hbase.zookeeper.quorum", "ip-10-137-69-104.ec2.internal");
		confh.set("hbase.zookeeper.property.clientPort", "2181");
		confh.set("hbase.master", "ip-10-137-69-104.ec2.internal" + ":60000");
		HTable table = new HTable(confh, tableName);
		List<Delete> list = new ArrayList<Delete>();
		Delete del = new Delete(rowKey.getBytes());
		list.add(del);
		table.delete(list);
		System.out.println("del recored " + rowKey + " ok.");
		table.close();
	}

	/**
	 * Get a row
	 */

	/**
	 * Scan (or list) a table
	 */
	public static String getAllRecords(String tableName, String url,
			String host_name) {
		ResultScanner rs = null;
		String returnJson = new String();
		Map<String, String> hash = new HashMap<String, String>();
		try {
			confh = HBaseConfiguration.create();

			confh.set("hbase.zookeeper.quorum", url);
			confh.set("hbase.zookeeper.property.clientPort", "2181");
			confh.set("hbase.master", url + ":60000");
			HTable table = new HTable(confh, tableName);
			Scan s = new Scan();
			rs = table.getScanner(s);

			for (Result r : rs) {
				for (KeyValue kv1 : r.raw()) {

					if (new String(kv1.getRow()).equals(host_name)) {
						// System.out.println("Host Entity Info : "
						// + new String(kv1.getRow()));
						// System.out.println(new String(kv1.getValue()));

						// hash.put(new String(kv1.getRow()),
						// new String(kv1.getValue()));
						returnJson = new String(kv1.getValue());
					}// end of if
				}// end of inner for loop
			}// end of outer for loop
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
		return returnJson;
	}

	public static void Config_table(String TABLENAME, String family, String arg)
			throws IOException {
		confh = HBaseConfiguration.create();
		confh.set("hbase.zookeeper.quorum", arg);
		confh.set("hbase.zookeeper.property.clientPort", "2181");
		confh.set("hbase.master", arg + ":60000");
		HBaseAdmin admin = new HBaseAdmin(confh);
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

	public static String getOneRecord(String tableName, String rowKey,
			String column_name, String qualifier, String arg)
			throws IOException {
		String returnStoreName = null;
		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", arg);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", arg + ":60000");
		HTable table = new HTable(conf, tableName);
		Get get = new Get(Bytes.toBytes(rowKey));
		get.addColumn(Bytes.toBytes(column_name), Bytes.toBytes(qualifier));
		Result rs = table.get(get);
		for (KeyValue kv : rs.raw()) {
			// System.out.print(new String(kv.getRow()) + " ");
			// System.out.print(new String(kv.getFamily()) + ":");
			// System.out.print(new String(kv.getQualifier()) + " ");
			// System.out.print(kv.getTimestamp() + " ");
			returnStoreName = new String(kv.getValue());
			if (returnStoreName != null)
				break;
		}
		table.close();
		return returnStoreName;
	}

	public static void creatTablehasonefamily(String tableName, String family, String hbaseIP) throws IOException {
		confh = HBaseConfiguration.create();

		confh.set("hbase.zookeeper.quorum", hbaseIP);
		confh.set("hbase.zookeeper.property.clientPort", "2181");
		confh.set("hbase.master", hbaseIP + ":60000");
		HBaseAdmin admin = new HBaseAdmin(confh);
		if (admin.tableExists(tableName)) {
			System.out.println("table already exists!");
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
				tableDesc.addFamily(new HColumnDescriptor(family));
			admin.createTable(tableDesc);
			System.out.println("create table " + tableName + " ok.");
		}
		
	}
}