package ManageFileForMenuLInks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class IO {

	public static void main(String[] args) {

		String content = "http://flipkart.comample0\nhttp://snapdeal.comample0";
		String path = "";

		// FileWrite(path,content);
		FileRead(path);

	}

	public static ArrayList<String> FileRead(String path) {
		ArrayList<String> links = new ArrayList<String>();
		try {

			BufferedReader br = new BufferedReader(new FileReader(path));

			String sCurrentLine = null;
			while ((sCurrentLine = br.readLine()) != null) {
				links.add(sCurrentLine);

			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return links;
	}// read function is closed

	public static void FileWrite(String path, String content) {
		File file = new File(path);
		try {
			FileOutputStream fop = new FileOutputStream(file);
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			System.out.println("Content :\"" + content
					+ "\" is written in File: " + path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}// write function is closed

	public static void FileCustomWrite(String pathForMenuLink,
			String whatToFind, String whatToappend) {

		ArrayList<String> links = new ArrayList<String>();
		String content = "";

		try {

			BufferedReader br = new BufferedReader(new FileReader(
					pathForMenuLink));
			String sCurrentLine = null;
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.contains(whatToFind)) {
					if (sCurrentLine.charAt(0) != '#'
							&& sCurrentLine.charAt(0) != '/')
						content += whatToappend + sCurrentLine;
					if (sCurrentLine.charAt(0) == '/'
							&& whatToappend.equals("#")) {
						String sub_str = sCurrentLine.substring(1);
						content += whatToappend + sub_str;
					}

				} else
					content += sCurrentLine;
			} // while close
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// now writing in file
		File file = new File(pathForMenuLink);
		try {
			FileOutputStream fop = new FileOutputStream(file);
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}