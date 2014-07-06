package com.ample.parser;

import com.google.gson.Gson;

class Create {
	// String price =
	// " (<strong class=\"(.+?)\">Rs <span id=\"(.+?)\" itemprop=\"(.+?)\">)(.+?)(</span>)(</strong>)regex5";
	// String product_title = " (<h1 itemprop=\"name\">)(.+?)(</h1>)regex2";
	// String rating =
	// "<div id=\"ratings-(.+?)\" class=\"lfloat ratingStarsBelowTitle ratingStarsSmall\" style=\"(.+?)\" > <span style=\"(.+?)\">(.+?)</span>(.+?)</div>regex1";

	// String attributes =
	// "<tr>(.+?)<td  width='20%'>(.+?)</td>(.+?)<td>(.+?)</td>(.+?)</tr>regex2regex4";
	// String product_title = "<title>(.+?)</title>regex1";
	String product_title = "<h1 itemprop=\"name\">(.+?)</h1>regex1";
	String price = "<span class=\"price list old-price\" id=\"fk-mprod-list-id\">Rs.(.+?)</span>regex1";
}

public class CreateJson {
	public static void main(String[] args) {
		insertjson(args);
	}

	public static void show() {
		String a = RedWrite.getAllRecords("parsing_result", "localhost",
				"http://www.snapdeal.com/product/sony-xperia-zl/685813?");
		System.out.println(a);
	}

	public static void insertjson(String[] args) {
		String url = "localhost";
		try {
			// RedWrite.Config_table("entity_info", "cf", url);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Create c = new Create();
		Gson g = new Gson();
		String json = g.toJson(c);
		System.out.println(json);
		try {
			// RedWrite.addRecord("entity_info", "www.snapdeal.com", "cf",
			// "value", json, url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
