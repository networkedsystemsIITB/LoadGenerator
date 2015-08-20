package com.webQ.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;

import com.webQ.controller.MainController;
import com.webQ.interfaces.Feature;

public class TestPlan implements Feature, Serializable, Cloneable {

	private Integer reqRate;
	private Integer duration;

	private Integer filenum;

	private Integer delay = 0;
	private Integer startDelay = 0;
	/*
	 * public HttpResponse response; public Map<String, List<String>> regexmap =
	 * new HashMap<String, List<String>>();
	 */
	private List<Integer> httpreqlist = new ArrayList<Integer>();

	private List<Object> testPlan = new ArrayList<Object>();
	
	/*public TestPlan(TestPlan obj) {
		this.reqRate=obj.reqRate;
		this.duration=obj.duration;
		this.filenum=obj.filenum;
		this.delay=obj.delay;
		this.startDelay=obj.startDelay;
		this.httpreqlist=obj.httpreqlist;
		this.testPlan=obj.testPlan;
		
		// TODO Auto-generated constructor stub
	}
	public TestPlan() {
		// TODO Auto-generated constructor stub
	}*/
	public Integer getFilenum() {
		return filenum;
	}

	public void setFilenum(Integer filenum) {
		this.filenum = filenum;
	}

	public List<Object> getTestPlan() {
		return testPlan;
	}

	public void setTestPlan(List<Object> testPlan) {
		this.testPlan = testPlan;
	}

	public List<Integer> getHttpreqlist() {
		return httpreqlist;
	}

	public void setHttpreqlist(List<Integer> httpreqlist) {
		this.httpreqlist = httpreqlist;
	}

	public Integer getReqRate() {
		return reqRate;
	}

	public void setReqRate(Integer reqRate) {
		this.reqRate = reqRate;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getStartDelay() {
		return startDelay;
	}

	public void setStartDelay(Integer startDelay) {
		this.startDelay = startDelay;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public void displayPlan() {
		int type;
		System.out.println("TestPlan Details");
		System.out.println("");
		System.out.println("Request Rate : " + reqRate + "  " + "Duration : "
				+ duration);

		/*System.out.println("Delay : " + delay + "  " + "Start Delay : "
				+ startDelay);*/
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
		/*System.out.println("HttpReq Array");
		for (int i = 0; i < httpreqlist.size(); i++) {
			System.out.println(httpreqlist.get(i));

		}*/
	}

	@Override
	public void execute(Response resp) throws InterruptedException,
			SuspendExecution {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		FileWriter fileWriter = null;
		final Logger logger = Logger.getLogger(MainController.class);
		try {
			// Delimiter used in CSV file

			// CSV file header

			final String FILE_HEADER = "Time,Http Request,Througput(Requests/Sec)";
			fileWriter = new FileWriter("/home/stanly/output" + filenum
					+ ".csv");

			fileWriter.append(FILE_HEADER.toString());

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
		if(this.reqRate!=0){
		int run_wait = 1000000000 / this.reqRate;
		Map<Integer, Integer> totalreqs = new HashMap<Integer, Integer>();
		for (int i = 0; i < httpreqlist.size(); i++) {
			totalreqs.put(httpreqlist.get(i), 0);
		}
		
		/*
		 * System.out.println("Duration : "+ duration);
		 * System.out.println("Reqrate : "+ reqRate);
		 */
		for (int i = 0; i < this.duration && MainController.test; ++i) {

			for (int j = 0; j < this.reqRate && MainController.test; ++j) {
				Fiber<Void> testplanfiber = new Fiber<Void>(() -> {
					try {
						// displayPlan();
						Response currresp = new Response();
						for (int k = 0; k < this.testPlan.size(); k++) {

							((Feature) this.testPlan.get(k)).execute(currresp);
							/*
							 * System.out.println(Fiber.currentFiber().getName())
							 * ; currresp.displayResponse();
							 */
							if (totalreqs.get(k) != null) {
								totalreqs.put(k, totalreqs.get(k) + 1);
							}

						}
					} catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage());
					}

				}).start();

				Fiber.sleep(0, run_wait);
			}

			int l = 1;
			try {
				for (Map.Entry entry : totalreqs.entrySet()) {
					Date now = new Date();
					String strDate = sdf.format(now);
					String message = strDate
							+ ","
							+ "Http Request "
							+ filenum
							+ "-"
							+ l
							+ " : "+ "Throughput = "
							+ String.valueOf(((Integer) entry.getValue())
									/ (i + 1))+"reqs/sec";
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
			/*
			 * l = 1; for (Map.Entry entry : totalreqs.entrySet()) {
			 * System.out.println("Http Request " + (l) + ": " + ((Integer)
			 * entry.getValue()) / (i + 1));
			 * 
			 * l++; }
			 */

		}
		if (!MainController.test) {
			MainController.test = true;
		}
		try {

			fileWriter.flush();

			fileWriter.close();

		} catch (IOException e) {

			System.out.println("Error while flushing/closing fileWriter !!!");

			e.printStackTrace();

		}
		/*try {
			testplanfiber.join();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		}

		System.out.println("Finished");
	}
}
