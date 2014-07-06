package GroupingScript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class IO {
	
	public static void FileWriteInAmpleCluster(String data) {
		File file = new File("/home/ubuntu/AmpleCluster.txt");
		try {
			FileOutputStream fop = new FileOutputStream(file);
		
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// get the content in bytes
			byte[] contentInBytes = data.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		}

	public static Boolean FileReadAmpleCluster() {
		Boolean status = false;
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("/home/ubuntu/AmpleCluster.txt"));
		
            
			String sCurrentLine;
     
			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				if(sCurrentLine.equals("finish"))
				{
				  	status  = true;
				}
			
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return status;
	}

	// Function for reading a file
	public  static Boolean FileReadHashContent() {
		Boolean status = false;
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("/home/ubuntu/HashContent.txt"));
		
            
			String sCurrentLine;
     
			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				if(sCurrentLine.equals("finish"))
				{
				  	status  = true;
				}
			
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return status;
	}

	// Function for writing in a file
	public static void FileWriteHashContent(String data) {
		File file = new File("/home/ubuntu/HashContent.txt");
		try {
			FileOutputStream fop = new FileOutputStream(file);
		
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// get the content in bytes
			byte[] contentInBytes = data.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static String ReadFromAmpleClusterFile() {
		
		
		
		//creating a file
		File file = new File("/home/ubuntu/AmpleCluster.txt");
		try {
		
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String data = null;
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("/home/ubuntu/AmpleCluster.txt"));
		
            
			String sCurrentLine;
     
			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				if(sCurrentLine != null)
				{
				  	data = sCurrentLine;
				}
			
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return data;
	}// fn close
	
      public static String ReadHashContent() {
    	
    	  
    	File file = new File("/home/ubuntu/HashContent.txt");
  		try {
  		
  			// if file doesn't exists, then create it
  			if (!file.exists()) {
  				file.createNewFile();
  			}
  			// get the content in bytes
  			 
  			
  		} catch (IOException e) {
  			e.printStackTrace();
  		}
  		
		String data = null;
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("/home/ubuntu/HashContent.txt"));
		    String sCurrentLine;
            
		    while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				if(sCurrentLine != null)
				{
				  	data = sCurrentLine;
				}
			
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return data;
	}// fn close
	
	

}
