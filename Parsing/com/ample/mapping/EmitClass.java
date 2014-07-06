package com.ample.mapping;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

public class EmitClass {
	public static int returnIntFromString(String str, String arg) {
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", arg);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", arg + ":60000");
		int res = 0;

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c < '0' || c > '9')
				continue;
			res = res * 10 + (c - '0');
		}
		return res;
	}

	public static double returnDoubleFromString(String str, String arg) {
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", arg);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", arg + ":60000");
		// String str = "120.463287";
		String res = " ";

		int countDot = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			if ((c < '0' || c > '9') && c != '.')
				continue;
			else {
				if (i > 1) {
					if ((str.charAt(i - 1) < '0' || str.charAt(i - 1) > '9')
							&& str.charAt(i - 1) != '.' && res != " ") {

						break;
					} else {
						if (c == '.') {
							if (countDot == 0 && res != " ") {
								res += c;
								// System.out.println("ind");
								countDot++;
							} else if (res != " ")
								break;
						} else {
							res += c;
							// System.out.println("indo");
						}

					}
				} else {
					if (c == '.' && res == " ")
						continue;
					else
						res += c;
					// System.out.println("in");
				}

			}
		}
		res = res.replaceAll(" ", "");

		return Double.parseDouble(res);
	}

	public static String stringFormatter(String findUnitToLowerCase, String arg) {
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", arg);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", arg + ":60000");

		findUnitToLowerCase = findUnitToLowerCase.replace(".", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("/", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("#", "");
		findUnitToLowerCase = findUnitToLowerCase.replace(",", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("=", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("&", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("-", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("+", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("*", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("~", "");
		findUnitToLowerCase = findUnitToLowerCase.replace(")", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("(", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("^", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("%", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("@", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("}", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("{", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("[", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("]", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("?", "");
		findUnitToLowerCase = findUnitToLowerCase.replace(">", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("<", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("<", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("'", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("\"", "");
		findUnitToLowerCase = findUnitToLowerCase.replace(";", "");
		findUnitToLowerCase = findUnitToLowerCase.replace(":", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("|", "");
		findUnitToLowerCase = findUnitToLowerCase.replace("\\", "");
		return findUnitToLowerCase;
	}
}
