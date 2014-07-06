import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;

public class ManualAndAutoPagination_final {
	// static valriable for links from manualpagination
	
//	static AmazonDynamoDBClient client;
	static Logger logger;

//	private static void createClient() throws IOException {
//
//		AWSCredentials credentials = new PropertiesCredentials(
//				ManualAndAutoPagination_final.class
//						.getResourceAsStream("AwsCredentials.properties"));
//		client = new AmazonDynamoDBClient(credentials);
//		// calling function
//
//	}

	public static void main(String[] args) throws Exception {

//		createClient();

		String filename = args[0];
		String store = args[1];
		int no_of_menulink_per_file=Integer.parseInt(args[2]);
		int no_of_menulink_per_thread=Integer.parseInt(args[3]);
		String IP=args[4];
		
		final int THREADGROUPSIZE   = no_of_menulink_per_file/no_of_menulink_per_thread;
		ArrayList<String> listofMenulinks = thread_pagination.FileRead(filename);
		String host_url="http://"+store+"#";
		List<String> urllist =new ArrayList<String>();
		CreateLogger(filename);
		int count=0;
	     theThread     thread[] = new theThread[THREADGROUPSIZE];
	     
		//------------------------------------------------------------------------------
		// Code for MultiThreading 
		//------------------------------------------------------------------------------
	     int i = 1;
		for (String getLine : listofMenulinks) {

			String urlString = getLine;
			if (!urlString.startsWith("#") && !urlString.equals(host_url)) {
				if (count != no_of_menulink_per_thread) {

					urllist.add(urlString);
					count++;

				} else {
					if(i<THREADGROUPSIZE){
					count = 0;	
					logger.info("Thread started for urllist"+i);
					// pass the args in constructor
					thread[i] = new theThread(urllist,store,logger);
			        thread[i].start();
		            i++;			
					urllist.removeAll(urllist);
					urllist.add(urlString);
					}
					else{break;}

				}// else close
			}// main if close
		}// for close
		
		
		//-----------------------------------------------------------------------------
		//  					Wait when All threads are finished
		//-----------------------------------------------------------------------------
	     
		System.out.print("Wait for worker threads to complete\n");
	      for (int j=1; j <=i; ++j) {
	    	  try {
	            thread[j].join();
	    	  }
	          catch (InterruptedException e) {
	        	  logger.info("Join interrupted");
	          }
	      }
//	      Thread.sleep(600000);
	      
	      System.out.print("Check all thread's results\n");
	      for (int j=1; j <=i; ++j) {
	    	  if (thread[i].status() != theThread.THREADPASS) {
	    		  logger.info("Unexpected thread status");
		     }
	      }
	      
	    //-----------------------------------------------------------------------------
		//  					Code when All threads are finished
		//-----------------------------------------------------------------------------
	     
//	      thread_pagination.updateStatusofStoreLink(host,store);
	      System.out.print("Testcase completed\n");
	 
		
}// main close

	
	public static ArrayList<String> DecideAndGetLinksFromMenuLink(
			String urlString, String urlType) {

		// If urlType is 1 then hostName will be added to the links else not |
		System.out.println("Manual Pagination is called...");
		ArrayList<String> ProducPagetLinks = null;
		try {
			ProducPagetLinks = LinksUsingManualPagination(urlString, urlType);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (ProducPagetLinks.size() == 0) {
			// call Scrolling function for links
			System.out
					.println("There are no Links found using Manual Pagination.");
			System.out.println("Now Scroll Pagination Funtion is called...");
			ProducPagetLinks = LinksUsingAutoPagination(urlString, urlType);

		} // if close

		thread_pagination.printArrayList(ProducPagetLinks);
		return ProducPagetLinks;
	} // Decide function is closed

	// function for auto pagination
	public static ArrayList<String> LinksUsingAutoPagination(String urlString,
			String urlType) {

		WebDriver driver = new FirefoxDriver();

		driver.get(urlString);
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		List<WebElement> listOfLinksAfter;
		double sizeBeforejs = driver.getPageSource().length();
		double sizeAfterjs = 0;
		double pixel = 25000;

		while ((sizeAfterjs != sizeBeforejs)
				&& (jse.executeScript("return document.readyState", "")
						.equals("complete"))) {
			jse.executeScript("window.scrollBy(0," + (int) pixel + ")", "");
			driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			sizeAfterjs = driver.getPageSource().length();

			pixel += 200000;

			jse.executeScript("window.scrollBy(0," + (int) (pixel + 25000)
					+ ")", "");
			driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sizeBeforejs = driver.getPageSource().length();

			// System.out.println(sizeAfterjs+" "+sizeBeforejs);

		}// while close

		// code for extracting required hrefs

		listOfLinksAfter = (List<WebElement>) driver.findElements(By
				.tagName("a"));

		int priorityForText = 1;
		int priorityForImg = 4;
		ArrayList<String> ProductPageLinkArrayList = new ArrayList<String>();
		Map<String, Integer> StoreLinks = new HashMap<String, Integer>();

		if (listOfLinksAfter.size() != 0) {
			for (int i = 0; i < listOfLinksAfter.size(); i++) {
				String href = listOfLinksAfter.get(i).getAttribute("href");
				if (href != null) {
					WebElement contentOfImg = listOfLinksAfter.get(i)
							.findElement(By.xpath("//img"));
					String imgSrc = contentOfImg.getAttribute("src");
					if (imgSrc != null) {
						/*
						 * Put here code for image categorization
						 */
						if (StoreLinks.containsKey(href)) {
							StoreLinks.put(href, StoreLinks.get(href)
									+ priorityForImg);
						}// 4 if close
						else {
							StoreLinks.put(href, priorityForImg);
						}

					}// 3 if close
					else {
						if (StoreLinks.containsKey(href)) {
							StoreLinks.put(href.toString(),
									StoreLinks.get(href) + priorityForText);
						}// 4 if close
						else {
							StoreLinks.put(href, priorityForText);
						}

					}

				}// 2 if close
			}// 1 for close
		}// if close

		driver.quit();

		// select the required links i.e. probability >=4
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		String hostName = url.getHost();
		for (Map.Entry<String, Integer> item : StoreLinks.entrySet()) {
			if (item.getValue() >= 4 && item.getKey().length() > 4) {
				// if (Integer.parseInt(urlType) == 0
				// && !item.getKey().toString().contains("javascript"))
				// ProductPageLinkArrayList.add(item.getKey());
				// else
				if (!item.getKey().contains("javascript")) {
					try {
						URL urlCheck = new URL(item.getKey());
						String tempHostName = urlCheck.getHost();
						if (hostName.equals(tempHostName)) {
							ProductPageLinkArrayList.add(item.getKey());
							// System.out.println(myNodes[i].toString());

						}
					}// try close
					catch (MalformedURLException e1) {
						ProductPageLinkArrayList.add("http://" + hostName
								+ item.getKey());
						// System.out.println(myNodes[i].toString());

					}// catch close
						// ProductPageLinkArrayList.add("http://" + hostName
						// + item.getKey());
				}

			} // if close

		}// for close

		return ProductPageLinkArrayList;

	}

	// function for manual pagination
	public static ArrayList<String> LinksUsingManualPagination(
			String urlString, String urlType) throws InterruptedException {

		WebDriver driver = new FirefoxDriver();
		// this list stores the links of product_page
		/*-----------------------------------------------------------------
		 * List of Used variables
		 */
		ArrayList<String> productLinkListFinal = new ArrayList<String>();
		int priorityForText = 1;
		int priorityForImg = 4;
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		String hostName = url.getHost();
		// ------------------------------------------------------------------

		// page is get here using the url

		driver.get(urlString);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		try {

			// Now check that Next link is present or not
			Boolean aTagFlag = driver.findElement(
					By.xpath("//a[contains(text(), 'Next')]")).isDisplayed();
			if (aTagFlag) {

				WebElement aTag = driver.findElement(By
						.xpath("//a[contains(text(), 'Next')]"));
				/*-------------------------------------------------*/

				// click the next buttons
				// printArrayList(ProductPageLinks);

				driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
				// System.out.println(aTag.getText());
				aTag.click();
				// System.out.println("-----------------------------------------------------------");
				// System.out.println("this is first time when the next link is clicked");
				// System.out.println("-----------------------------------------------------------");

				// Thread.sleep(5000);
				// Get the current url
				// Condition for checking that url changes or not on clicking
				// next goes here
				String newUrlString = driver.getCurrentUrl();
				// driver.switchTo().activeElement();
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				// System.out.println(ProductPageLinks);
				if (urlString.equals(newUrlString)) {
					int terminationFlag = 0;
					while (true) {
						// System.out.println("While loop is on");

						/*
						 * code for getting links goes hereAgain same code is
						 * written because we the page is changed by on clicking
						 * next button above
						 */
						// --------------------------------------------------------------------------
						List<WebElement> listOfLinksAfter1;
						Map<String, Integer> StoreLinks1 = new HashMap<String, Integer>();
						// code for extracting required hrefs
						listOfLinksAfter1 = (List<WebElement>) driver
								.findElements(By.tagName("a"));
						if (listOfLinksAfter1.size() != 0) {
							for (int i1 = 0; i1 < listOfLinksAfter1.size(); i1++) {
								String href = listOfLinksAfter1.get(i1)
										.getAttribute("href");
								if (href != null) {
									WebElement contentOfImg = listOfLinksAfter1
											.get(i1).findElement(
													By.xpath("//img"));
									String imgSrc = contentOfImg
											.getAttribute("src");
									if (imgSrc != null) {
										if (StoreLinks1.containsKey(href)) {
											StoreLinks1.put(href,
													StoreLinks1.get(href)
															+ priorityForImg);
										}// 4 if close
										else {
											StoreLinks1.put(href,
													priorityForImg);
										}

									}// 3 if close
									else {
										if (StoreLinks1.containsKey(href)) {
											StoreLinks1.put(href.toString(),
													StoreLinks1.get(href)
															+ priorityForText);
										}// 4 if close
										else {
											StoreLinks1.put(href,
													priorityForText);
										}

									}

								}// 2 if close
							}// 1 for close
						}// if close

						// Refining links according to requirements
						ArrayList<String> ProductPageLinkArrayList1 = new ArrayList<String>();
						for (Map.Entry<String, Integer> item : StoreLinks1
								.entrySet()) {
							if (item.getValue() >= 4
									&& item.getKey().length() > 4) {
								if (!item.getKey().contains("javascript")) {

									try {
										URL urlCheck = new URL(item.getKey());
										String tempHostName = urlCheck
												.getHost();
										if (hostName.equals(tempHostName)) {
											ProductPageLinkArrayList1.add(item
													.getKey());
											// System.out.println(myNodes[i].toString());

										}
									}// try close
									catch (MalformedURLException e1) {
										ProductPageLinkArrayList1.add("http://"
												+ hostName + item.getKey());
										// System.out.println(myNodes[i].toString());

									}// catch close
										// ProductPageLinkArrayList.add("http://"
										// + hostName
										// + item.getKey());

								}

								// ProductPageLinkArrayList1
								// .add(item.getKey());
								// else
								// if (!item.getKey().contains("javascript"))
								// ProductPageLinkArrayList1.add("http://"
								// + hostName + item.getKey());

							} // if close

						}// for close

						// ProductPageLinks.addAll(ProductPageLinkArrayList);
						productLinkListFinal.addAll(ProductPageLinkArrayList1);

						// click the next buttons

						// code for switching the current content to another one
						driver.manage().timeouts()
								.implicitlyWait(30, TimeUnit.SECONDS);
						// --------------------------------------------------------------------------
						/* here goes loop termination condition */
						try {
							Boolean aTagFlag2 = driver.findElement(
									By.xpath("//a[contains(text(), 'Next')]"))
									.isDisplayed();
							if (aTagFlag2) {
								WebElement aTag2 = driver
										.findElement(By
												.xpath("//a[contains(text(), 'Next')]"));
								// System.out.println("I got here");
								aTag2.click();
								driver.manage().timeouts()
										.implicitlyWait(30, TimeUnit.SECONDS);
							}

						} catch (NoSuchElementException e) {
							// System.out.println("Element is find here..");
							try {
								// System.out.println("this is what I do");
								Boolean aTagFlag2 = driver.findElement(
										By.xpath("//a[contains(text(), '>')]"))
										.isDisplayed();
								if (aTagFlag2) {

									// System.out.println("I got here");
									WebElement aTag2 = driver
											.findElement(By
													.xpath("//a[contains(text(), '>')]"));
									aTag2.click();
									driver.manage()
											.timeouts()
											.implicitlyWait(30,
													TimeUnit.SECONDS);
								}
							} catch (NoSuchElementException e1) {
								terminationFlag = 1;
								// driver.close();
							}
						}
						// till here
						// ---------------------------------------------------------------------------------
						// if terminationFlag is true then while loop is
						// breaked.
						// System.out.println(terminationFlag);
						if (terminationFlag == 1) {
							// driver.close();
							break;
						}
					}// while close
				}// if close for url is not changing on clicking next
				else {
					// printArrayList(ProductPageLinks);
					// System.out.println("Recursive Call is being made...");
					// driver.close();
					List<WebElement> listOfLinksAfter;
					// code for extracting required hrefs

					listOfLinksAfter = (List<WebElement>) driver
							.findElements(By.tagName("a"));

					Map<String, Integer> StoreLinks = new HashMap<String, Integer>();

					if (listOfLinksAfter.size() != 0) {
						for (int i1 = 0; i1 < listOfLinksAfter.size(); i1++) {
							driver.manage().timeouts()
									.implicitlyWait(20, TimeUnit.SECONDS);
							String href = listOfLinksAfter.get(i1)
									.getAttribute("href");
							if (href != null) {
								WebElement contentOfImg = listOfLinksAfter.get(
										i1).findElement(By.xpath("//img"));
								String imgSrc = contentOfImg
										.getAttribute("src");
								if (imgSrc != null) {
									if (StoreLinks.containsKey(href)) {
										StoreLinks.put(href,
												StoreLinks.get(href)
														+ priorityForImg);
									}// 4 if close
									else {
										StoreLinks.put(href, priorityForImg);
									}

								}// 3 if close
								else {
									if (StoreLinks.containsKey(href)) {
										StoreLinks.put(href.toString(),
												StoreLinks.get(href)
														+ priorityForText);
									}// 4 if close
									else {
										StoreLinks.put(href, priorityForText);
									}

								}

							}// 2 if close
						}// 1 for close
					}// if close

					// Refining links according to requirements
					ArrayList<String> ProductPageLinkArrayList = new ArrayList<String>();

					for (Map.Entry<String, Integer> item : StoreLinks
							.entrySet()) {
						if (item.getValue() >= 4 && item.getKey().length() > 4) {
							if (!item.getKey().contains("javascript")) {
								try {
									URL urlCheck = new URL(item.getKey());
									String tempHostName = urlCheck.getHost();
									if (hostName.equals(tempHostName)) {
										ProductPageLinkArrayList.add(item
												.getKey());

									}
								}// try close
								catch (MalformedURLException e1) {
									ProductPageLinkArrayList.add("http://"
											+ hostName + item.getKey());

								}// catch close

								// ProductPageLinkArrayList.add(item.getKey());
							}
							// else
							// if (!item.getKey().toString()
							// .contains("javascript"))
							// ProductPageLinkArrayList.add("http://"
							// + hostName + item.getKey());

						} // if close

					}// for close

					// ProductPageLinks.addAll(ProductPageLinkArrayList);
					productLinkListFinal.addAll(ProductPageLinkArrayList);
					productLinkListFinal.addAll(LinksUsingManualPagination(
							newUrlString, urlType));

				} // else close for changing url when click on next

			} // if close

		} // try close
		catch (org.openqa.selenium.NoSuchElementException e) {
			if (productLinkListFinal.size() == 0) {
				try {

					Boolean aTagFlag = driver.findElement(
							By.xpath("//*[contains(text(), '>')]"))
							.isDisplayed();

					if (aTagFlag) {
						WebElement aTag = driver.findElement(By
								.xpath("//*[contains(text(), '>')]"));
						// click the next buttons

						aTag.click();

						// Get the current url
						// Condition for checking that url changes or not on
						// clicking next goes here
						String newUrlString = driver.getCurrentUrl();
						if (urlString == newUrlString) {
							int terminationFlag = 0;
							while (true) {
								// System.out.println("While loop is on");
								// System.out.println("Ajax is on for manual pagination");
								/*
								 * code for getting links goes hereAgain same
								 * code is written because we the page is
								 * changed by on clicking next button above
								 */
								// --------------------------------------------------------------------------

								List<WebElement> listOfLinksAfter1;
								Map<String, Integer> StoreLinks1 = new HashMap<String, Integer>();
								// code for extracting required hrefs
								listOfLinksAfter1 = (List<WebElement>) driver
										.findElements(By.tagName("a"));
								if (listOfLinksAfter1.size() != 0) {
									for (int i1 = 0; i1 < listOfLinksAfter1
											.size(); i1++) {
										String href = listOfLinksAfter1.get(i1)
												.getAttribute("href");
										if (href != null) {
											WebElement contentOfImg = listOfLinksAfter1
													.get(i1).findElement(
															By.xpath("//img"));
											String imgSrc = contentOfImg
													.getAttribute("src");
											if (imgSrc != null) {
												if (StoreLinks1
														.containsKey(href)) {
													StoreLinks1
															.put(href,
																	StoreLinks1
																			.get(href)
																			+ priorityForImg);
												}// 4 if close
												else {
													StoreLinks1.put(href,
															priorityForImg);
												}

											}// 3 if close
											else {
												if (StoreLinks1
														.containsKey(href)) {
													StoreLinks1
															.put(href
																	.toString(),
																	StoreLinks1
																			.get(href)
																			+ priorityForText);
												}// 4 if close
												else {
													StoreLinks1.put(href,
															priorityForText);
												}

											}

										}// 2 if close
									}// 1 for close
								}// if close

								// Refining links according to requirements
								ArrayList<String> ProductPageLinkArrayList1 = new ArrayList<String>();
								for (Map.Entry<String, Integer> item : StoreLinks1
										.entrySet()) {
									if (item.getValue() >= 4
											&& item.getKey().length() > 4) {
										if (!item.getKey().contains(
												"javascript")) {
											try {
												URL urlCheck = new URL(
														item.getKey());
												String tempHostName = urlCheck
														.getHost();
												if (hostName
														.equals(tempHostName)) {
													ProductPageLinkArrayList1
															.add(item.getKey());

												}
											}// try close
											catch (MalformedURLException e1) {
												ProductPageLinkArrayList1
														.add("http://"
																+ hostName
																+ item.getKey());

											}// catch close

										}
										// ProductPageLinkArrayList1.add(item
										// .getKey());
										// else if (!item.getKey().toString()
										// .contains("javascript"))
										// ProductPageLinkArrayList1
										// .add("http://" + hostName
										// + item.getKey());

									} // if close*?/

								}// for close

								// ProductPageLinks.addAll(ProductPageLinkArrayList);
								productLinkListFinal
										.addAll(ProductPageLinkArrayList1);

								// click the next buttons

								// code for switching the current content to
								// another one
								driver.manage().timeouts()
										.implicitlyWait(30, TimeUnit.SECONDS);
								// --------------------------------------------------------------------------
								/* here goes loop termination condition */
								try {
									Boolean aTagFlag2 = driver
											.findElement(
													By.xpath("//a[contains(text(), 'Next')]"))
											.isDisplayed();
									if (aTagFlag2) {
										WebElement aTag2 = driver
												.findElement(By
														.xpath("//a[contains(text(), 'Next')]"));
										System.out.println("I got here");
										aTag2.click();
										driver.manage()
												.timeouts()
												.implicitlyWait(30,
														TimeUnit.SECONDS);
									}

								} catch (NoSuchElementException e1123) {
									System.out
											.println("Element is find here..");
									try {
										// System.out.println("this is what I do");
										Boolean aTagFlag2 = driver
												.findElement(
														By.xpath("//a[contains(text(), '>')]"))
												.isDisplayed();
										if (aTagFlag2) {

											// System.out.println("I got here");
											WebElement aTag2 = driver
													.findElement(By
															.xpath("//a[contains(text(), '>')]"));
											aTag2.click();
											driver.manage()
													.timeouts()
													.implicitlyWait(30,
															TimeUnit.SECONDS);
										}
									} catch (NoSuchElementException e1) {
										terminationFlag = 1;
										// driver.close();
									}
								}
								// till here
								// ---------------------------------------------------------------------------------
								// if terminationFlag is true then while loop is
								// breaked.
								// System.out.println(terminationFlag);
								if (terminationFlag == 1) {
									// driver.close();
									break;
								}
							}// while close
						}// if close for url is not changing on clicking next
						else {
							// printArrayList(ProductPageLinks);
							// System.out.println("Recursive Call is being made...");
							// driver.close();
							/*-------------------------------------------------*/
							List<WebElement> listOfLinksAfter;
							// code for extracting required hrefs
							listOfLinksAfter = (List<WebElement>) driver
									.findElements(By.tagName("a"));

							Map<String, Integer> StoreLinks = new HashMap<String, Integer>();
							if (listOfLinksAfter.size() != 0) {
								for (int i1 = 0; i1 < listOfLinksAfter.size(); i1++) {

									String href = listOfLinksAfter.get(i1)
											.getAttribute("href");
									if (href != null) {
										WebElement contentOfImg = listOfLinksAfter
												.get(i1).findElement(
														By.xpath("//img"));
										String imgSrc = contentOfImg
												.getAttribute("src");
										if (imgSrc != null) {
											if (StoreLinks.containsKey(href)) {
												StoreLinks
														.put(href,
																StoreLinks
																		.get(href)
																		+ priorityForImg);
											}// 4 if close
											else {
												StoreLinks.put(href,
														priorityForImg);
											}

										}// 3 if close
										else {
											if (StoreLinks.containsKey(href)) {
												StoreLinks
														.put(href.toString(),
																StoreLinks
																		.get(href)
																		+ priorityForText);
											}// 4 if close
											else {
												StoreLinks.put(href,
														priorityForText);
											}

										}

									}// 2 if close
								}// 1 for close
							}// if close

							/*-------------------------------------------------*/
							// Refining links according to requirements
							ArrayList<String> ProductPageLinkArrayList = new ArrayList<String>();

							for (Map.Entry<String, Integer> item : StoreLinks
									.entrySet()) {
								if (item.getValue() >= 4
										&& item.getKey().length() > 4) {
									if (!item.getKey().toString()
											.contains("javascript")) {
										try {
											URL urlCheck = new URL(
													item.getKey());
											String tempHostName = urlCheck
													.getHost();
											if (hostName.equals(tempHostName)) {
												ProductPageLinkArrayList
														.add(item.getKey());

											}
										}// try close
										catch (MalformedURLException e1) {
											ProductPageLinkArrayList
													.add("http://" + hostName
															+ item.getKey());

										}// catch close

									}// inner if close

								} // if close

							}// for close
							productLinkListFinal
									.addAll(ProductPageLinkArrayList);

							/*-------------------------------------------------*/

							// recursion is called here
							productLinkListFinal
									.addAll(LinksUsingManualPagination(
											newUrlString, urlType));
						} // else close for changing url when click on next

					} // if close

				} catch (NoSuchElementException e1) {
					System.out.println("No Elements");
					// driver.close();
					// return productLinkListFinal;

				}
			}// if close
		} finally {

			driver.close();
		}
		// System.out.println("ProductPageLinkList is retuned.");
		return productLinkListFinal;
	}// function for manual pagination is closed
	public static void CreateLogger(String abc) {
		logger = Logger.getLogger("MyLog");
		FileHandler fh;
		try {

			// This block configure the logger with handler and formatter
			fh = new FileHandler(abc + ".log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

			// the following statement is used to log any messages
			logger.info("SeleniumSlave Log File");

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
//-----------------------------------------------------------------------
	 
	 
	   static class theThread extends Thread {
	      public final static int THREADPASS     = 0;
	      public final static int THREADFAIL     = 1;
	      int         _status;
	      public static List<String> urllist;
	      public static String store;
	      public static int no_of_urllist;
	      public static Logger logger;
	      public static AmazonDynamoDBClient client;

	  	private static void createClient() throws IOException {

	  		AWSCredentials credentials = new PropertiesCredentials(
	  				ManualAndAutoPagination_final.class
	  						.getResourceAsStream("AwsCredentials.properties"));
	  		client = new AmazonDynamoDBClient(credentials);
	  		// calling function

	  	}
	      
	      public int status() {
	         return _status;
	      }
	      public theThread(List<String> urllist, String store, Logger logger) {
	         _status = THREADFAIL;
	         this.urllist = urllist;
	         this.store = store;
	         this.logger = logger;
//	         this.no_of_urllist=i;
//	         this.client=client;
	         
	      }
	      public void CallPaginationFunction(List<String> urllist, String store,int no_of_urllist,
	  			Logger logger) {
//	  
			ArrayList<String> AllThread_AProducPagetLinks = new ArrayList<String>();
	  		
	  		for (int i = 0; i < urllist.size(); i++) {
	  			ArrayList<String> ProducPagetLinks = new ArrayList<String>();
	  			try{
	  				logger.info(" Pagination started for menulink "+urllist.get(i));
	  			ProducPagetLinks = DecideAndGetLinksFromMenuLink(
	  					urllist.get(i), "0");
	  			logger.info("Total product of menulink  "+urllist.get(i)+"  is  "+ProducPagetLinks.size());
	  			logger.info("Pagination ended for menulink"+urllist.get(i));
	  			
	  			}catch(Exception e){
	  				logger.info("error occur in Menulink "+urllist.get(i) + "  is  "+e.toString());
	  				
	  			}
	  			AllThread_AProducPagetLinks.addAll(ProducPagetLinks);
//	  			ProducPagetLinks.removeAll(ProducPagetLinks);	  			
//				
	  		}
//	  		System.out.println();
	  		logger.info("Inserting into dynamo started with list size "+AllThread_AProducPagetLinks.size());
	  		try {
				createClient();
			} catch (IOException e) {
				e.printStackTrace();
			}
	  		for (int j = 0; j < AllThread_AProducPagetLinks.size(); j++) {
	  			
	  			Map<String, AttributeValue> return_hashMap = new HashMap<String, AttributeValue>();
	  		
	  			  				return_hashMap.put("status",
	  			  						new AttributeValue().withS("notDone"));
	  			  				return_hashMap.put("url",
	  			  						new AttributeValue().withS(AllThread_AProducPagetLinks.get(j)));
	  		
	  			  				return_hashMap.put("storeName",
	  			  						new AttributeValue().withS(store));
	  			  				try {
	  			  					PutItemRequest putItemRequest1 = new PutItemRequest()
	  			  							.withTableName("ProductLink").withItem(
	  			  									return_hashMap);
	  			  					PutItemResult result1 = client.putItem(putItemRequest1);
	  		
	  			  				} 
	  			  				catch (AmazonServiceException ase) {
	  			  					System.err.println("Create items failed.");
//	  			  					logger.info("item failed "+AllThread_AProducPagetLinks.get(j));
	  			  				}
	  			  				}
	  		logger.info("Inserting into dynamo ended with storelist size "+AllThread_AProducPagetLinks.size());	  	
	  		
	  	}
	      public void run() {
	         CallPaginationFunction(urllist,store,no_of_urllist,logger);
	         _status = THREADPASS;
	      }
	   }
}// class close