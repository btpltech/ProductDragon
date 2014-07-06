import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

public class ProcessThread implements Runnable {

	static String ipAddress; 
	static String pathToUpdate;
	static String pathToScp;
	static String fileName;
	static String scriptName;
	/*
	 * Main Function
	 */
	public static void main(String[] args) {
		try {

			ProcessThread f = new ProcessThread();
			// f.startProcessThread("ex.sh", "yeah..");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startProcessThread(String scriptName,String ipAddress, String pathToUpdate,
			String pathToScp, String fileName) {
		
		this.ipAddress = ipAddress;
		this.pathToUpdate = pathToUpdate;
		this.pathToScp = pathToScp;
		this.fileName = fileName;
		this.scriptName = scriptName;	
//		logger.info(scriptname + "is started with " + file);
		Thread t = new Thread(this, "Script Thread");
		System.out.println("Thread Started");
		t.start();
		if (t.isAlive()) {
			System.out.println("Thread Alive");
		}
	}
//
	
	public static void startProcess(String scriptName,String ipAddress, String pathToUpdate,
			String pathToScp, String fileName) throws IOException {
		ProcessBuilder pb = new ProcessBuilder(scriptName,ipAddress,pathToUpdate,pathToScp,fileName);
		// ProcessBuilder pb = new ProcessBuilder("/home/anjali/Desktop/ex.sh",
		// totalSlaves);
		Process p = null;
		p = pb.start();
		InputStream is = p.getInputStream();
		String err = IOUtils.toString(is, "UTF-8");
		System.out.println("...." + err);

	}

	@Override
	public void run() {
		
		try {
			System.out.println(scriptName + " started");
			
			ProcessThread
					.startProcess(scriptName,ipAddress,pathToUpdate,pathToScp,fileName);

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(scriptName + "Thread Ended");
		

	}
}
