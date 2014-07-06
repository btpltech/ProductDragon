package com.ample.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public class demoForregex {

	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();

		/* for attributes */
		// String html =
		// "<tr><td>a1</td><td>v1</td></tr><tr><td>a2</td><td>v2</td></tr><tr><td>a3</td><td>v3</td></tr>";
		// String regex = "<tr><td>(.+?)</td><td>(.+?)</td></tr>regex1regex2";

		/* for others */
		String html = "<td>anshu</td>";
		String regex = "<td>(.+?)</td>regex1";

		map = RegexParse(html, "image", regex);
		for (Entry<String, String> entry : map.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = "
					+ entry.getValue());
		}
	}

	public static HashMap<String, String> RegexParse(String html, String name,
			String regex) {

		String line = html;
		String split[] = regex.split("regex");// contains gp no & regex( on left side)
		String pattern = split[0];// this regex tp apply

		System.out.println("Pattern : " + pattern);
		// System.out.println(split[0] + "---------------" + split[1]);

		Pattern r = Pattern.compile(pattern, Pattern.DOTALL);
		Matcher m = r.matcher(line);
		HashMap<String, String> map = new HashMap<String, String>();
		String object_contain = "";
		
		if (!name.equalsIgnoreCase("attributes")) {
			while (m.find()) {
            		if (name.equalsIgnoreCase("price")) {

					String product_price[] = m
							.group(Integer.parseInt(split[1])).split(",");
					String in_price = "";
					if (product_price.length == 1)
						in_price = product_price[0];
					else
						in_price = product_price[0] + product_price[1];

					object_contain = in_price;
					System.out.println("Price = " + in_price);

				} else if (name.equalsIgnoreCase("description")) {
					object_contain = object_contain
							+ m.group(Integer.parseInt(split[1].trim()))
							+ "~!~";
					// System.out.println("description : " + object_contain);

				} else if (name.equalsIgnoreCase("image")) {
					object_contain = object_contain
							+ m.group(Integer.parseInt(split[1].trim()))
							+ "~!~";
					// System.out.println("image : " + object_contain);

				} else {
					object_contain = object_contain
							+ m.group(Integer.parseInt(split[1].trim()));
					System.out.println("other value : " + object_contain);
				}
			} // end of while
		}// end of outer if

		/* Another while loop for getting the attributes */

		if (name.equalsIgnoreCase("attributes")) {
			Map<String, String> attribute = new HashMap<String, String>();
			// System.out.println(split[1] + "     " + split[2]);
			while (m.find()) {

				attribute.put(m.group(Integer.parseInt(split[1])),
						m.group(Integer.parseInt(split[2])));
				// System.out.println("Attributee list are : \n Key :"
				// + m.group(Integer.parseInt(split[1])) + "Value :"
				// + m.group(Integer.parseInt(split[2])));

			}// end of if
			if (!attribute.isEmpty()) {
				Gson attribute_gson = new Gson();
				object_contain = attribute_gson.toJson(attribute);
				// System.out.println("Attributes : " + object_contain);
			}
		}// end 0f while

		if (!object_contain.equals(""))
			map.put(name, object_contain);
		System.out.println("Map : " + map);
		return map;
	}
}
