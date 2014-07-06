package com.ample.mapping;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

public class ValueCorrector {
	public static void main(String[] args) {

		Map<String, String> input = new HashMap<String, String>();
		input.put("Price", "inr8090.0");
		Map<String, String> output = new HashMap<String, String>();

		output = attributeValueCorrectorLib(input, args[0]);
		System.out.println(output);
	} // void close

	static Map<String, String> attributeValueCorrectorLib(
			Map<String, String> input, String arg) {

		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", arg);
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.master", arg + ":60000");

		/*
		 * This lib is for currency,weight,length,camera
		 * 
		 * 1. Make attributeName to all in CapitalCase 2. Make units of
		 * attributeValue to LowerCase
		 */

		// Map to return
		Map<String, String> newMap = new HashMap<String, String>();

		/*
		 * Getting attributeName and attributeValue String
		 */
		String attributeValue = null;
		String attributeName = null;
		String inputAttributeName = null;

		for (Map.Entry<String, String> item : input.entrySet()) {
			attributeValue = item.getValue();
			attributeName = item.getKey();

		}
		inputAttributeName = attributeName;

		// cleaning Value String
		// attributeValue = attributeValue.replaceAll(" ", "");
		// attributeValue = attributeValue.replaceAll(",","");
		// attributeValue = attributeValue.replaceAll("=","");
		// attributeValue = attributeValue.replaceAll("/","");
		// attributeValue = attributeValue.replaceAll("#","");
		// System.out.println(attributeValue);

		/*
		 * Map for containing fixed Units
		 */
		Map<String, String> fixedUnit = new HashMap<String, String>();
		fixedUnit.put("PRICE", "USD");
		fixedUnit.put("LENGTH", "cm");
		fixedUnit.put("WEIGHT", "kg");
		fixedUnit.put("CAMERA", "mp");

		// Coverdion library will come here

		Map<String, Map<String, String>> UnitTable = new HashMap<String, Map<String, String>>();

		/*
		 * Price info
		 */
		Map<String, String> priceInfo = new HashMap<String, String>();

		priceInfo.put("inr", "65");
		priceInfo.put("usd", "1");
		priceInfo.put("$", "1");
		UnitTable.put("PRICE", priceInfo); // for price

		/*
		 * Length info
		 */
		Map<String, String> lengthInfo = new HashMap<String, String>();

		lengthInfo.put("cm", "1");
		lengthInfo.put("inch", "2.25");
		lengthInfo.put("kilometer", "100000");
		lengthInfo.put("km", "100000");
		lengthInfo.put("microinch", "0.00000254");
		lengthInfo.put("mm", "0.1");
		lengthInfo.put("milimeter", "0.1");
		lengthInfo.put("centimeter", "1");
		lengthInfo.put("mile", "160934.4");
		lengthInfo.put("yard", "91.44");
		lengthInfo.put("decimeter", "10");
		lengthInfo.put("decameter", "1000");
		lengthInfo.put("hectometer", "10000");

		UnitTable.put("LENGTH", lengthInfo); // for length
		/*
		 * Weight info
		 */
		Map<String, String> WeightInfo = new HashMap<String, String>();

		WeightInfo.put("centigram", "0.00001");
		WeightInfo.put("decigram", "0.0001");
		WeightInfo.put("dekagram", "0.01");
		WeightInfo.put("dram", "0.0017718451953");
		WeightInfo.put("grain", "0.00006479891");
		WeightInfo.put("gram", "0.001");
		WeightInfo.put("hectogram", "0.1");
		WeightInfo.put("lbs", "0.45359237");
		WeightInfo.put("longton", "1016.0469088");
		WeightInfo.put("megagram", "1000");
		WeightInfo.put("metricton", "1000");
		WeightInfo.put("milligram", "0.000001");
		WeightInfo.put("ounce", "0.028349523125");
		WeightInfo.put("pound", "0.45359237");
		WeightInfo.put("shortton", "907.18474");
		WeightInfo.put("kilogram", "1");
		WeightInfo.put("kg", "1");

		UnitTable.put("WEIGHT", WeightInfo); // for weight

		/*
		 * For camera info
		 */
		Map<String, String> CameraInfo = new HashMap<String, String>();
		CameraInfo.put("pixel", "0.000001");
		CameraInfo.put("megapixel", "1");
		CameraInfo.put("mp", "1");
		UnitTable.put("CAMERA", CameraInfo); // for camera and lens

		double price = EmitClass.returnDoubleFromString(attributeValue, arg);
		// System.out.println("p="+price);
		String[] findUnit = attributeValue.split("" + price);
		String[] findUnitUsingInt = attributeValue.split("" + (int) price);

		// System.out.println(findUnitUsingInt.length);
		// System.out.println(findUnit[0]);

		double newValue = 0;

		attributeName = attributeName.toUpperCase();

		for (int i = 0; i < findUnit.length; i++) {
			// System.out.println("i="+i+" "+findUnit[i]);

			// if(unit.length==1)
			{
				if (UnitTable.containsKey(attributeName)) {

					String findUnitToLowerCase = findUnit[i].toLowerCase();
					findUnitToLowerCase = EmitClass.stringFormatter(
							findUnitToLowerCase, arg);
					// System.out.println(findUnitToLowerCase);
					// System.out.println(findUnitToLowerCase);
					if (findUnitToLowerCase != "") {
						if (UnitTable.get(attributeName).containsKey(
								findUnitToLowerCase)) {
							{

								newValue = (price * Double
										.parseDouble(UnitTable.get(
												attributeName).get(
												findUnitToLowerCase)));

							}
						}
					}
				}
			} // if close

			if (newValue != 0.0)
				newMap.put(inputAttributeName,
						"" + newValue + " " + fixedUnit.get(attributeName));
			else {
				newMap.putAll(input);
				// System.out.println("a");
			}
		}// for loop end

		if (newValue == 0) {
			for (int i = 0; i < findUnitUsingInt.length; i++) {
				// System.out.println("i="+i+" "+findUnit[i]);

				// if(unit.length==1)
				{
					if (UnitTable.containsKey(attributeName)) {

						String findUnitToLowerCase = findUnitUsingInt[i]
								.toLowerCase();
						findUnitToLowerCase = EmitClass.stringFormatter(
								findUnitToLowerCase, arg);
						// System.out.println(findUnitToLowerCase);
						if (findUnitToLowerCase != "") {
							if (UnitTable.get(attributeName).containsKey(
									findUnitToLowerCase)) {
								{

									newValue = (price * Double
											.parseDouble(UnitTable.get(
													attributeName).get(
													findUnitToLowerCase)));

								}
							}
						}
					}
				} // if close

				if (newValue != 0.0)
					newMap.put(inputAttributeName, "" + newValue + " "
							+ fixedUnit.get(attributeName));
				else {
					newMap.putAll(input);

				}

			}// for loop close
		}// if for new value is closed
		System.out.println("......................................" + newMap);
		return newMap;
	}// fn closes
}
