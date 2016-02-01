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
import java.util.concurrent.Semaphore;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.log4j.Logger;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.httpclient.FiberHttpClient;
import co.paralleluniverse.strands.Strand;

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
	private boolean running;
	private Integer runningcount;

	private List<Integer> httpreqlist = new ArrayList<Integer>();
	Map<Integer, List<Long>> reqsDetails = new HashMap<Integer, List<Long>>();
	private SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");
	private FileWriter fileWriter = null;

	private List<Object> testPlan = new ArrayList<Object>();
	private final Logger logger = Logger.getLogger(MainController.class);

	public boolean isRunning() throws SuspendExecution {
		return running;
	}

	public void setRunning(boolean running) throws SuspendExecution {
		this.running = running;
	}

	public Integer getRunningcount() throws SuspendExecution {
		return runningcount;
	}

	public void setRunningcount(Integer runningcount) throws SuspendExecution {
		this.runningcount = runningcount;
	}

	public Integer getRandom() throws SuspendExecution {
		return random;
	}

	public void setRandom(Integer random) throws SuspendExecution {
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

	public void setId(Integer id) throws SuspendExecution {
		this.id = id;
	}

	public Integer getOutputrowstart() throws SuspendExecution {
		return outputrowstart;
	}

	public void setOutputrowstart(Integer outputrowstart)
			throws SuspendExecution {
		this.outputrowstart = outputrowstart;
	}

	public void displayPlan() throws SuspendExecution {
		int type;
		System.out.println("");
		System.out.println("TestPlan Details");
		System.out.println("");
		System.out.println("Request Rate : " + reqRate + "  " + "Duration : "
				+ duration);

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
				System.out.println("URL : "
						+ ((HttpRequest) testPlan.get(i)).getUrl());
				System.out.println("ReqType : "
						+ ((HttpRequest) testPlan.get(i)).getHttpType());
				System.out.println("PostParams : "
						+ ((HttpRequest) testPlan.get(i)).getPostParamList());
				System.out.println("PostBody : "
						+ ((HttpRequest) testPlan.get(i)).getPostBody());
				break;
			case 2:
				System.out.println("Timer : "
						+ ((ConstantTimer) testPlan.get(i)).getTime());
				break;
			case 3:
				System.out.println("Regex Name : "
						+ ((RegexExtractor) testPlan.get(i)).getRefName());
				System.out.println("Regex : "
						+ ((RegexExtractor) testPlan.get(i)).getRegex());
				break;
			default:
				System.out.println("Case Error");
			}

		}
		System.out.println("");
		System.out.println("HttpReq Array");
		for (int i = 0; i < httpreqlist.size(); i++) {
			System.out.println(httpreqlist.get(i));

		}
		System.out.println("");
	}

	@Override
	public void execute(Response resp) throws InterruptedException,
			SuspendExecution {
		// TODO Auto-generated method stub

		this.displayPlan();

		if (this.reqRate != 0) {
			this.running = true;
			this.reqsDetails.clear();
			for (int i = 0; i < httpreqlist.size(); i++) {
				Output outputrow = new Output();
				Date now = new Date();
				String strDate = sdf.format(now);
				outputrow.setRequest("TestPlan-" + this.getId() + "-Request-"
						+ (i + 1));
				outputrow.setTime(strDate);
				outputrow.setInputload("0 reqs/sec");
				outputrow.setErrorrate("0%");
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
				// System.out.println("Epoch Number : " + this.getId());
				logger.info("Epoch Number : " + this.getId());
			}
			int fiberArraysize = this.duration * this.reqRate;
			Fiber[] fibers = new Fiber[fiberArraysize];
			// System.out.println("Array Size = " + fiberArraysize);
			final int num = this.duration * this.reqRate;
			// int runningcount=num;
			this.setRunningcount(num);
			final RateLimiter rl = RateLimiter.create(this.reqRate);
			Date now1 = new Date();
			logger.info("starttime " + sdf.format(now1));

			this.outputFiber();
			final Semaphore sem = new Semaphore(1500000);
			for (int i = 0; i < num && MainController.test; i++) {
				rl.acquire();
				if (sem.availablePermits() == 0)
					logger.info("Maximum connections count reached, waiting...");
				sem.acquireUninterruptibly();

				Fiber<Void> testplansfiber=new Fiber<Void>(
						() -> {
							try {
								int k = 0;
								Response currresp = new Response();
								for (k = 0; k < this.testPlan.size(); k++) {

									if (this.reqsDetails.get(k) != null) {

										this.reqsDetails
												.get(k)
												.set(0,
														(this.reqsDetails
																.get(k).get(0)) + 1);

										long startTime = System
												.currentTimeMillis();

										((Feature) this.testPlan.get(k))
												.execute(currresp);
										long elapsedTime = System
												.currentTimeMillis()
												- startTime;
										if (currresp.getResponse()
												.getStatusLine().toString()
												.contains("OK")) {

											this.reqsDetails.get(k).set(
													1,
													this.reqsDetails.get(k)
															.get(1) + 1);

											this.reqsDetails.get(k).set(
													2,
													this.reqsDetails.get(k)
															.get(2));
											this.reqsDetails.get(k).set(
													3,
													this.reqsDetails.get(k)
															.get(3) + 1);
											this.reqsDetails.get(k).set(
													4,
													this.reqsDetails.get(k)
															.get(4)
															+ elapsedTime);

										} else {
											this.reqsDetails.get(k).set(
													1,
													this.reqsDetails.get(k)
															.get(1));

											this.reqsDetails.get(k).set(
													2,
													this.reqsDetails.get(k)
															.get(2) + 1);
											this.reqsDetails.get(k).set(
													3,
													this.reqsDetails.get(k)
															.get(3));
											this.reqsDetails.get(k).set(
													4,
													this.reqsDetails.get(k)
															.get(4));

										}
									} else {
										((Feature) this.testPlan.get(k))
												.execute(currresp);
									}
								}
							} catch (final Throwable t) {

							} finally {
								// logger.info("inside finally");
								sem.release();
								System.out.println(this.getRunningcount());
								this.setRunningcount(this.getRunningcount() - 1);
							}
						}).start();

			}
			System.out.println("here i am");
			//Fiber.sleep(this.duration * 1000);
			/*while (true) {
				//logger.info("Running Count "+this.getRunningcount());
				System.out.println("infinite");
				if (this.runningcount <= 0) {
					this.running = false;
					break;
				}
			}*/
			Date now2 = new Date();
			logger.info("endtime " + sdf.format(now2));

			/*
			 * if (!MainController.test) { MainController.test = true; }
			 */
			try {

				fileWriter.flush();

				fileWriter.close();

			} catch (IOException e) {

				logger.info("Error while flushing/closing fileWriter !!!");

				e.printStackTrace();

			}

		}

		logger.info("TestPlan Finished");
	}

	private void outputFiber() throws InterruptedException, SuspendExecution {
		// TODO Auto-generated method stub
		try {

			final String FILE_HEADER = String.format(
					"%-30s%-25s%-18s%-18s%-18s%-15s%-10s", "Time", "Request",
					"Input Load", "Avg Througput", "Cur Througput",
					"Response Time", "Error Rate");

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
			while (this.running) {
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
				/*
				 * outputrow.setInputload(this.getReqRate().toString() +
				 * " reqs/sec");
				 */
				outputrow.setInputload(entry.getValue().get(0) + " reqs/sec");
				entry.getValue().set(0, (long) 0);
				// System.out.println(entry.getValue().get(0)+
				// "    "+entry.getValue().get(2));
				if ((entry.getValue().get(1) + entry.getValue().get(2)) != 0) {
					double errorrate = ((1.00 * (entry.getValue().get(2))) / (entry
							.getValue().get(1) + entry.getValue().get(2))) * 100;
					outputrow.setErrorrate(df.format(errorrate) + "%");
				}
				avgThroughput = (entry.getValue().get(1)) / (timesec);
				outputrow.setAvgThroughput(avgThroughput + " reqs/sec");
				outputrow.setCurThroughput(entry.getValue().get(3)
						+ " reqs/sec");

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
			logger.info("Error in CsvFileWriter !!!");

			e.printStackTrace();
		}
	}

}
