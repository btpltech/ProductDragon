import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

public class Functions implements Runnable {

	private String scriptname;
	private int menulink_per_thread;
	private String file;
	private String store;
	private int no_of_menulink;

	private Logger logger;

	/*
	 * Main Function
	 */
	public static void main(String[] args) {
		try {

			Functions f = new Functions();
			// f.startProcessThread("ex.sh", "yeah..");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startProcessThread(String scriptname, String file,
			String store, int no_of_menulink, int menulink_per_thread,
			java.util.logging.Logger logger) {
		this.scriptname = scriptname;
		this.no_of_menulink = no_of_menulink;
		this.store = store;
		this.file = file;
		this.menulink_per_thread = menulink_per_thread;
		// this.total_menulink = total_menulink;
		this.logger = logger;
//		logger.info(scriptname + "is started with " + file);
		Thread t = new Thread(this, "Script Thread");
		System.out.println("Thread Started");
		t.start();
		if (t.isAlive()) {
			System.out.println("Thread Alive");
		}
	}

	public static void startProcess(String scriptname, String file,
			String store, int menulink_per_tread, int total_instances,
			Logger logger) throws IOException {
		ProcessBuilder pb = new ProcessBuilder("/home/ubuntu/" + scriptname,
				file, store, "" + menulink_per_tread, "" + total_instances);
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
			System.out.println(this.scriptname + " started");
			logger.info(this.scriptname + "is started " + this.file);
			Functions
					.startProcess(this.scriptname, this.file,
							this.store, this.no_of_menulink, this.menulink_per_thread,
							this.logger);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(this.scriptname + "Thread Ended");
		logger.info(this.scriptname + "is ended " + this.file);

	}
}