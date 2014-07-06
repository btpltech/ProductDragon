import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class GetMenuLink {

	public static void main(String[] args) throws XPatherException {
		System.out.println("Code started...");
		String pathForMenuLink = "Menu.txt";

		ArrayList<String> site_urls = ManageFileForMenuLInks.IO
				.FileRead(pathForMenuLink);
		// EmitClass.printArrayList(site_urls);

		for (int i = 0; i < site_urls.size(); i++) {
			if (site_urls.get(i) != "" && site_urls.get(i).charAt(0) != '#'
					&& site_urls.get(i).charAt(0) != '/') {

				ManageFileForMenuLInks.IO.FileCustomWrite(pathForMenuLink,
						site_urls.get(i), "/");

				String[] store_data = site_urls.get(i).split("ample");
				String urlString = store_data[0];
				String linkXpath = store_data[1];
				// function is called here

				ArrayList<String> getMenulinkList = getMenuLink(urlString,
						linkXpath);

				if (getMenulinkList.size() != 0) {
					URL get_url = null;
					try {
						get_url = new URL(urlString);
					} catch (MalformedURLException e) {

					} // if close
					String file_name = get_url.getHost();

					String content = null, path = "" + file_name;
					content = urlString + "\n";
					for (int j = 0; j < getMenulinkList.size(); j++) {
						content = getMenulinkList.get(j) + "\n";
					}// for close

					ManageFileForMenuLInks.IO.FileWrite(path, content);
					// write # if the link's menuLinks are fetched.

					ManageFileForMenuLInks.IO.FileCustomWrite(pathForMenuLink,
							site_urls.get(i), "#");

				} // if close
			}// if close

		}
		// EmitClass.printArrayList(getMenuLink("http://www.yebhi.com/index.aspx","//*[@id=\"floatmenubar\"]"));
	}

	public static ArrayList<String> getMenuLink(String urlString,
			String linkXpath) throws XPatherException {
		linkXpath = linkXpath + "//a/@href";

		ArrayList<String> menuLinks = new ArrayList<String>();
		try {
			// get URL content
			HtmlCleaner cleaner = new HtmlCleaner();
			String siteUrl = urlString;
			TagNode tagNode = cleaner.clean(new URL(siteUrl));
			Object[] myNodes = tagNode.evaluateXPath(linkXpath);
			URL url1 = new URL(urlString);
			String hostName = url1.getHost();

			if (myNodes.length != 0) {

				for (int i = 0; i < myNodes.length; i++) {
					// TagNode childrenTagNode = (TagNode)myNodes[i];
					if (!myNodes[i].toString().contains("javascript")) {
						try {
							URL urlCheck = new URL(myNodes[i].toString());
							String tempHostName = urlCheck.getHost();
							if (hostName.equals(tempHostName)) {
								menuLinks.add(myNodes[i].toString());
								// System.out.println(myNodes[i].toString());

							}
						}// try close
						catch (MalformedURLException e1) {
							menuLinks.add("http://" + hostName
									+ myNodes[i].toString());
							// System.out.println(myNodes[i].toString());

						}// catch close

					}// if close
				}// for close

			}// 1 for close

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return menuLinks;
	}// main close
}// class close
