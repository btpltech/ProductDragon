package com.ample.parser;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class demoForXpath {
	public static void main(String[] args) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map = XpathParse("html", "xpath", "price");

	}

	public static HashMap XpathParse(String html, String name, String xpath)
			throws XPathExpressionException, XPatherException {
		HashMap return_map = new HashMap();

		HtmlCleaner cleaner = new HtmlCleaner();
		CleanerProperties props = cleaner.getProperties();

		// set some properties to non-default values
		props.setTranslateSpecialEntities(true);
		props.setTransResCharsToNCR(true);
		props.setOmitComments(true);

		// do parsing
		// System.out.println(xpath);
		TagNode tagNode = new HtmlCleaner(props).clean(html);

		Object[] myNodes = tagNode.evaluateXPath(xpath);

		if (myNodes.length != 0) {
			String xpathResult = "";
			for (int i = 0; i < myNodes.length; i++)
				xpathResult = xpathResult + myNodes[i].toString() + "<br>";
			return_map.put(name, xpathResult);
			System.out.println(xpath);
		}

		return return_map;
	}
}
