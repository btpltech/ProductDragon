package com.ample.mapping;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public class CheckTableStatus {
	private static Configuration config = HBaseConfiguration.create();

	@SuppressWarnings("resource")
	public static void Config_table(String TABLENAME, String family, String arg)
			throws IOException {
		config = HBaseConfiguration.create();
		config.set("hbase.zookeeper.quorum", arg);
		config.set("hbase.zookeeper.property.clientPort", "2181");
		config.set("hbase.master", arg + ":60000");
		HBaseAdmin admin = new HBaseAdmin(config);
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
}
