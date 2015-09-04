package com.webQ.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;

import com.google.common.util.concurrent.RateLimiter;
import com.webQ.controller.MainController;
import com.webQ.interfaces.Feature;

public class TestPlan implements Feature, Serializable, Cloneable {

	private Integer reqRate;
	private Integer duration;

	private Integer id;
	private Integer outputrowstart;
	private Integer random;
	private Integer startDelay = 0;
	/*
	 * public HttpResponse response; public Map<String, List<String>> regexmap =
	 * new HashMap<String, List<String>>();
	 */
	private List<Integer> httpreqlist = new ArrayList<Integer>();
	Map<Integer, List<Long>> reqsDetails = new HashMap<Integer, List<Long>>();
	private SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");
	private FileWriter fileWriter = null;

	private List<Object> testPlan = new ArrayList<Object>();
	private final Logger logger = Logger.getLogger(MainController.class);

	/*
	 * public TestPlan(TestPlan obj) { this.reqRate=obj.reqRate;
	 * this.duration=obj.duration; this.filenum=obj.filenum;
	 * this.delay=obj.delay; this.startDelay=obj.startDelay;
	 * this.httpreqlist=obj.httpreqlist; this.testPlan=obj.testPlan;
	 * 
	 * // TODO Auto-generated constructor stub } public TestPlan() { // TODO
	 * Auto-generated constructor stub }
	 */

	public Integer getRandom() throws SuspendExecution{
		return random;
	}

	public void setRandom(Integer random) {
		this.random = random;
	}

	public List<Object> getTestPlan() throws SuspendExecution {
		return testPlan;
	}

	public void setTestPlan(List<Object> testPlan) throws SuspendExecution {
		this.testPlan = testPlan;
	}

	public List<Integer> getHttpreqlist() throws SuspendExecution {
		return httpreqlist;
	}

	public void setHttpreqlist(List<Integer> httpreqlist)
			throws SuspendExecution {
		this.httpreqlist = httpreqlist;
	}

	public Integer getReqRate() throws SuspendExecution {
		return reqRate;
	}

	public void setReqRate(Integer reqRate) throws SuspendExecution {
		this.reqRate = reqRate;
	}

	public Integer getDuration() throws SuspendExecution {
		return duration;
	}

	public void setDuration(Integer duration) throws SuspendExecution {
		this.duration = duration;
	}

	public Integer getStartDelay() throws SuspendExecution {
		return startDelay;
	}

	public void setStartDelay(Integer startDelay) throws SuspendExecution {
		this.startDelay = startDelay;
	}

	public Integer getId() throws SuspendExecution {
		return id;
	}

	public void setId(Integer id) throws SuspendExecution{
		this.id = id;
	}

	public Integer getOutputrowstart() throws SuspendExecution{
		return outputrowstart;
	}

	public void setOutputrowstart(Integer outputrowstart) throws SuspendExecution{
		this.outputrowstart = outputrowstart;
	}
	
	public void displayPlan() throws SuspendExecution {
		int type;
		System.out.println("TestPlan Details");
		System.out.println("");
		System.out.println("Request Rate : " + reqRate + "  " + "Duration : "
				+ duration);

		/*
		 * System.out.println("Delay : " + delay + "  " + "Start Delay : " +
		 * startDelay);
		 */
		for (int i = 0; i < testPlan.size(); i++) {
			if (testPlan.get(i).toString().contains("HttpRequest")) {
				type = 1;
			} else if (testPlan.get(i).toString().contains("ConstantTimer")) {
				type = 2;
			} else if (testPlan.get(i).toString().contains("RegexExtractor")) {
				type = 3;
			} else {
				type = 0;
			}
			switch (type) {
			case 1:
				System.out.println(((HttpRequest) testPlan.get(i)).getUrl());
				break;
			case 2:
				System.out.println(((ConstantTimer) testPlan.get(i)).getTime());
				break;
			case 3:
				System.out.println(((RegexExtractor) testPlan.get(i))
						.getRefName());
				System.out.println(((RegexExtractor) testPlan.get(i))
						.getRegex());
				break;
			default:
				System.out.println("Case Error");
			}

		}
		System.out.println("");
		/*
		 * System.out.println("HttpReq Array"); for (int i = 0; i <
		 * httpreqlist.size(); i++) { System.out.println(httpreqlist.get(i));
		 * 
		 * }
		 */
	}

	@Override
	public void execute(Response resp) throws InterruptedException,
			SuspendExecution {
		// TODO Auto-generated method stub

		try {
			// Delimiter used in CSV file

			// CSV file header

			final String FILE_HEADER = String.format(
					"%-30s%-25s%-18s%-18s%-18s%-15s%-10s", "Time", "Request",
					"Input Load", "Avg Througput", "Cur Througput",
					"Response Time", "Error Rate");
			/*
			 * final String FILE_HEADER =
			 * "Time\tRequest\tInput Load\tAvg Througput\tCur Througput\tResponse Time\tError Rate"
			 * ;
			 */fileWriter = new FileWriter("/home/stanly/output" + this.getId()
					+ ".csv");

			fileWriter.append(FILE_HEADER.toString());
			logger.info(FILE_HEADER);
			// Add a new line separator after the header

			fileWriter.append("\n");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to create file");
			e.printStackTrace();
		}

		System.out.println("");
		this.displayPlan();
		System.out.println("");
		if (this.reqRate != 0) {
			int run_wait = 1000000000 / this.reqRate;
			// Map<Integer, List<Long>> reqsDetails = new HashMap<Integer,
			// List<Long>>();
			// Map<Integer, Long> totalresptimes = new HashMap<Integer, Long>();
			this.reqsDetails.clear();
			for (int i = 0; i < httpreqlist.size(); i++) {
				Output outputrow = new Output();
				Date now = new Date();
				String strDate = sdf.format(now);
				outputrow.setRequest("TestPlan-" + this.getId() + "-Request-"
						+ (i + 1));
				outputrow.setTime(strDate);
				outputrow.setInputload(this.getReqRate().toString()
						+ " reqs/sec");
				outputrow.setErrorrate("0.00%");
				outputrow.setAvgThroughput("0 reqs/sec");
				outputrow.setCurThroughput("0 reqs/sec");
				outputrow.setResponsetime("0 msec");
				MainController.outputlist.set(this.getOutputrowstart() + i,
						outputrow);
				List<Long> temp = new ArrayList<Long>();
				
				// Send Request Rate
				temp.add((long) 0);
				// Success Request count
				temp.add((long) 0);
				// Error Request count
				temp.add((long) 0);
				// Current Throughput
				temp.add((long) 0);
				// Response Time
				temp.add((long) 0);
				this.reqsDetails.put(httpreqlist.get(i), temp);
				// totalresptimes.put(httpreqlist.get(i), (long) 0);
			}

			/*
			 * System.out.println("Duration : "+ duration);
			 * System.out.println("Reqrate : "+ reqRate);
			 */
			Fiber.sleep(this.getStartDelay() * 1000);
			if (this.random == 1) {
				System.out.println("Epoch Number : " + this.getId());
				logger.info("Epoch Number : " + this.getId());
			}
			int fiberArraysize = this.duration * this.reqRate;
			Fiber[] fibers = new Fiber[fiberArraysize];
			System.out.println("Array Size = " + fiberArraysize);
			final int num = this.duration * this.reqRate;

			final RateLimiter rl = RateLimiter.create(this.reqRate);
			Date now1 = new Date();
			logger.info("starttime " + sdf.format(now1));
			outputFiber();
			  for (int i = 0; i < this.duration && MainController.test; ++i) {
			  for (int j = 0; j < this.reqRate && MainController.test; ++j) {
			 

			/*outputFiber();
			for (int i = 0; i < num && MainController.test; i++) {
				rl.acquire();*/
				Fiber<Void> testplanfiber = new Fiber<Void>(
						() -> {
							int k = 0;
							try {
								
								Response currresp = new Response();
								for (k = 0; k < this.testPlan.size(); k++) {
									/*List<Long> temp = new ArrayList<Long>();*/
									long startTime = System.currentTimeMillis();
									//long currate = this.reqsDetails.get(k).get(0);
								if (this.reqsDetails.get(k) != null) {
									
										this.reqsDetails.get(k).set(0,(this.reqsDetails.get(k).get(0))+1);
										//temp.add(this.reqsDetails.get(k).get(0)+1);
									}
									((Feature) this.testPlan.get(k))
											.execute(currresp);

									if (this.reqsDetails.get(k) != null) {
										if (currresp.getResponse()
												.getStatusLine().toString()
												.contains("OK")) {
											List<Long> temp = new ArrayList<Long>();
											temp.add(this.reqsDetails.get(k)
													.get(0));
											temp.add(this.reqsDetails.get(k)
													.get(1) + 1);

											long elapsedTime = System
													.currentTimeMillis()
													- startTime;
											temp.add(this.reqsDetails.get(k)
													.get(2));
											temp.add(this.reqsDetails.get(k)
													.get(3) + 1);
											temp.add(this.reqsDetails.get(k)
													.get(4) + elapsedTime);
											
											
											this.reqsDetails.put(k, temp);

										} else {
											List<Long> temp = new ArrayList<Long>();
											temp.add(this.reqsDetails.get(k)
													.get(0));
											temp.add(this.reqsDetails.get(k)
													.get(1));
											temp.add(this.reqsDetails.get(k)
													.get(2) + 1);
											temp.add(this.reqsDetails.get(k)
													.get(3));
											temp.add(this.reqsDetails.get(k)
													.get(4));
											this.reqsDetails.put(k, temp);
										}
									}

								}
							} catch (Exception ex) {
								System.out.println(ex.getLocalizedMessage());
								List<Long> temp = new ArrayList<Long>();
								temp.add(this.reqsDetails.get(k).get(0));
								temp.add(this.reqsDetails.get(k).get(1));
								temp.add(this.reqsDetails.get(k).get(2) + 1);
								temp.add(this.reqsDetails.get(k).get(3));
								temp.add(this.reqsDetails.get(k).get(4));
								this.reqsDetails.put(k, temp);
							}

						}).start();

				
				//fibers[i] = testplanfiber;
				
				Fiber.sleep(0,run_wait);
			}
			  
			  
			  }
			Date now2 = new Date();
			logger.info("endtime " + sdf.format(now2));
			/*
			 * for (int i = this.duration; (i < this.duration + 50) &&
			 * MainController.test; i++) { Fiber.sleep(1000); int l = 1; try {
			 * for (Map.Entry<Integer, List<Long>> entry : reqsDetails
			 * .entrySet()) { DecimalFormat df = new DecimalFormat();
			 * df.setMaximumFractionDigits(2); Output outputrow = new Output();
			 * Date now = new Date(); String strDate = sdf.format(now); Long
			 * currThroughput; outputrow.setRequest("TestPlan-" + this.getId() +
			 * "-Request-" + (l)); outputrow.setTime(strDate);
			 * outputrow.setInputload(this.getReqRate().toString() +
			 * " reqs/sec"); // System.out.println(entry.getValue().get(0)+ //
			 * "    "+entry.getValue().get(2)); if ((entry.getValue().get(0) +
			 * entry.getValue().get(2)) != 0) { double errorrate = ((1.00 *
			 * (entry.getValue() .get(2))) / (entry.getValue().get(0) + entry
			 * .getValue().get(2))) * 100;
			 * outputrow.setErrorrate(df.format(errorrate) + "%"); }
			 * 
			 * currThroughput = (entry.getValue().get(0)) / (i + 1); outputrow
			 * .setAvgThroughput(currThroughput + " reqs/sec");
			 * outputrow.setCurThroughput(entry.getValue().get(3) .toString() +
			 * " reqs/sec"); entry.getValue().set(3, (long) 0); if
			 * (currThroughput != 0)
			 * outputrow.setResponsetime(entry.getValue().get(1) /
			 * (currThroughput * (i + 1)) + " msec"); else
			 * outputrow.setResponsetime("0 msec");
			 * MainController.outputlist.set(this.getOutputrowstart() + l - 1,
			 * outputrow);
			 * 
			 * String message = String.format(
			 * "%-30s%-25s%-18s%-18s%-18s%-15s%-10s", outputrow.getTime(),
			 * outputrow.getRequest(), outputrow.getInputload(),
			 * outputrow.getAvgThroughput(), outputrow.getCurThroughput(),
			 * outputrow.getResponsetime(), outputrow.getErrorrate());
			 * 
			 * fileWriter.append(message); fileWriter.append("\n");
			 * 
			 * logger.info(message);
			 * 
			 * l++; }
			 * 
			 * } catch (IOException e) { // TODO Auto-generated catch block
			 * System.out.println("Error in CsvFileWriter !!!");
			 * 
			 * e.printStackTrace(); }
			 * 
			 * }
			 */

		/*	for (Fiber fiber : fibers) {
				try {
					if (!MainController.test)
						break;
					System.out.println(fiber.currentFiber().getName()
							+ " Waiting");
					fiber.join();
				} catch (ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}*/
			/*
			 * int p=0; for (Fiber fiber : fibers){
			 * 
			 * System.out.println("Fiber Name "+fiber.currentFiber().getName()+
			 * " index= "+p); p++; }
			 */
			if (!MainController.test) {
				MainController.test = true;
			}
			try {

				fileWriter.flush();

				fileWriter.close();

			} catch (IOException e) {

				System.out
						.println("Error while flushing/closing fileWriter !!!");

				e.printStackTrace();

			}

			/*
			 * try { testplanfiber.join(); } catch (ExecutionException e1) {
			 * e1.printStackTrace(); }
			 */

		}

		System.out.println("Finished");
	}

	private void outputFiber() throws InterruptedException, SuspendExecution {
		// TODO Auto-generated method stub
		try {
			// Delimiter used in CSV file

			// CSV file header

			final String FILE_HEADER = String.format(
					"%-30s%-25s%-18s%-18s%-18s%-15s%-10s", "Time", "Request",
					"Input Load", "Avg Througput", "Cur Througput",
					"Response Time", "Error Rate");
			/*
			 * final String FILE_HEADER =
			 * "Time\tRequest\tInput Load\tAvg Througput\tCur Througput\tResponse Time\tError Rate"
			 * ;
			 */
			fileWriter = new FileWriter("/home/stanly/output" + this.getId()
					+ ".csv");

			fileWriter.append(FILE_HEADER.toString());
			logger.info(FILE_HEADER);
			// Add a new line separator after the header

			fileWriter.append("\n");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to create file");
			e.printStackTrace();
		}
		Fiber<Void> outputfiber = new Fiber<Void>(() -> {
			int i = 1;
			while (true) {
				Fiber.sleep(1000);
				printOutput(i);
				i++;
			}

		}).start();

	}

	private void printOutput(int timesec) throws InterruptedException,
			SuspendExecution {
		int l = 1;
		try {
			for (Map.Entry<Integer, List<Long>> entry : this.reqsDetails
					.entrySet()) {
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2);
				Output outputrow = new Output();
				Date now = new Date();
				String strDate = sdf.format(now);
				Long avgThroughput;
				outputrow.setRequest("TestPlan-" + this.getId() + "-Request-"
						+ (l));
				outputrow.setTime(strDate);
				/*outputrow.setInputload(this.getReqRate().toString()
						+ " reqs/sec");*/
				outputrow.setInputload(entry.getValue().get(0)
						+ " reqs/sec");
				// System.out.println(entry.getValue().get(0)+
				// "    "+entry.getValue().get(2));
				if ((entry.getValue().get(1) + entry.getValue().get(2)) != 0) {
					double errorrate = ((1.00 * (entry.getValue().get(2))) / (entry
							.getValue().get(1) + entry.getValue().get(2))) * 100;
					outputrow.setErrorrate(df.format(errorrate) + "%");
				}
				avgThroughput = (entry.getValue().get(1)) / (timesec);
				outputrow.setAvgThroughput(avgThroughput + " reqs/sec");
				outputrow.setCurThroughput(entry.getValue().get(3).toString()
						+ " reqs/sec");
				entry.getValue().set(0, (long) 0);
				entry.getValue().set(3, (long) 0);
				if (avgThroughput != 0)
					outputrow.setResponsetime(entry.getValue().get(4)
							/ (avgThroughput * (timesec)) + " msec");
				else
					outputrow.setResponsetime("0 msec");
				MainController.outputlist.set(this.getOutputrowstart() + l - 1,
						outputrow);

				String message = String.format(
						"%-30s%-25s%-18s%-18s%-18s%-15s%-10s",
						outputrow.getTime(), outputrow.getRequest(),
						outputrow.getInputload(), outputrow.getAvgThroughput(),
						outputrow.getCurThroughput(),
						outputrow.getResponsetime(), outputrow.getErrorrate());

				fileWriter.append(message);
				fileWriter.append("\n");

				logger.info(message);

				l++;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in CsvFileWriter !!!");

			e.printStackTrace();
		}
	}

	
}
