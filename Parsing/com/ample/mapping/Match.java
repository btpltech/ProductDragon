package com.ample.mapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;

import com.google.gson.Gson;

public class Match {

	static String Raw_Actual_Attribute_TABLE = "raw_actual_attribute_name";
	static Gson g = new Gson();
	ValueCorrector vc = new ValueCorrector();

	@SuppressWarnings({ "unchecked", "resource", "static-access" })
	Map<String, String> matching(String attribute_hashmap, String arg)
			throws IOException {
		String key_raw = new String();
		String value_raw = new String();
		String key = new String();
		String value = new String();
		HashMap<String, String> inside_hashmap = new HashMap<String, String>();
		HashMap<String, String> final_hashmmap = new HashMap<String, String>();
		inside_hashmap = g.fromJson(attribute_hashmap, HashMap.class);

		// GETTING DATA FROM "raw_actual_parsing "
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", arg);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", arg + ":60000");
		HTable table = new HTable(conf, Raw_Actual_Attribute_TABLE);
		for (Entry<String, String> e1 : inside_hashmap.entrySet()) {
			key = new String(e1.getKey());
			value = new String(e1.getValue());

			Scan s = new Scan();
			ResultScanner ss = table.getScanner(s);
			int flag = 0;
			for (Result r : ss) {
				for (KeyValue kv : r.raw()) {
					key_raw = new String(kv.getRow());
					value_raw = new String(kv.getValue());

					if (key.equals(key_raw)) {
						Map<String, String> input = new HashMap<String, String>();
						input.put(value_raw, value);
						Map<String, String> output = vc
								.attributeValueCorrectorLib(input, arg);
						for (Entry<String, String> e2 : output.entrySet()) {
							final_hashmmap.put(e2.getKey(), e2.getValue());
						}
						flag++;
						break;
					}// inner else end

				}// inner for loop

			}// outer for loop
			if (flag == 0) {
				Map<String, String> input = new HashMap<String, String>();
				input.put(key, value);
				Map<String, String> output = vc.attributeValueCorrectorLib(
						input, arg);
				for (Entry<String, String> e2 : output.entrySet()) {
					final_hashmmap.put(e2.getKey(), e2.getValue());
				}
				// System.out.println(key + "\t" + key_raw + " are MATCHED ");

			}
		}// end of map loop
			// Map<String, String> n = new HashMap<String, String>();
			// n.put("Price", "INR8004.0");
			// Map<String, String> m = vc.attributeValueCorrectorLib(n, arg);
		System.out.println(",,,,,," + final_hashmmap);
		return final_hashmmap;
	}
}
