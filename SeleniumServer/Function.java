import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;


public class Function implements Runnable {
	private String SlaveIP;
	private String scriptname;
	private Logger logger;
	public void run() {
		logger.info(this.scriptname + "is ended " + this.SlaveIP);
		try {
			Function.startProcess(this.scriptname, this.SlaveIP,logger);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info(this.scriptname + "is started " + this.SlaveIP);

	}

	public void startProcessThread(String scriptname, String slaveIP, Logger logger2) {
		this.scriptname = scriptname;
		this.SlaveIP=slaveIP;
		// this.total_menulink = total_menulink;
		this.logger = logger2;
//		logger.info(scriptname + "is started with " + file);
		Thread t = new Thread(this, "Script Thread");
		System.out.println("Thread Started");
		t.start();
		if (t.isAlive()) {
			System.out.println("Thread Alive");
		}
	}
	public static void startProcess(String scriptname, String slaveIP,
			Logger logger) throws IOException {
		ProcessBuilder pb = new ProcessBuilder("/home/ubuntu/" + scriptname,slaveIP);
		// ProcessBuilder pb = new ProcessBuilder("/home/anjali/Desktop/ex.sh",
		// totalSlaves);
		Process p = pb.start();
//		p = 
		InputStream is = p.getInputStream();
		String err = IOUtils.toString(is, "UTF-8");
		System.out.println("...." + err);

	}

}
