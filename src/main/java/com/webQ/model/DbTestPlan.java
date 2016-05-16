package com.webQ.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;

import com.google.common.util.concurrent.RateLimiter;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.webQ.controller.MainController;
import com.webQ.interfaces.Feature;

public class DbTestPlan implements Feature, Serializable, Cloneable {


	private Integer reqRate;
	private Integer duration;
	private List<String> query;
	private String dbUrl;
	private String dbDriver;
	private String uname;
	private String passwd;
	private Integer maxConnections;
	private Integer id;
	private Integer outputrowstart;
	private Integer random;
	private Integer startDelay = 0;
	private boolean running;
	private Integer runningcount;
	private BasicDataSource connectionPool;
	private List<Object> testPlan = new ArrayList<Object>();
	private SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");
	private final Logger logger = Logger.getLogger(MainController.class);
	private List<FileWriter> tptwriter=new ArrayList<FileWriter>();
	private List<FileWriter> respwriter=new ArrayList<FileWriter>();
	private List<FileWriter> errwriter=new ArrayList<FileWriter>();
	Map<Integer, List<Long>> reqsDetails = new HashMap<Integer, List<Long>>();

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbDriver() {
		return dbDriver;
	}

	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}



	public List<String> getQuery() {
		return query;
	}

	public void setQuery(List<String> query) {
		this.query = query;
	}

	public Integer getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(Integer maxConnections) {
		this.maxConnections = maxConnections;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOutputrowstart() {
		return outputrowstart;
	}

	public void setOutputrowstart(Integer outputrowstart) {
		this.outputrowstart = outputrowstart;
	}

	public Integer getRandom() {
		return random;
	}

	public void setRandom(Integer random) {
		this.random = random;
	}

	public Integer getStartDelay() {
		return startDelay;
	}

	public void setStartDelay(Integer startDelay) {
		this.startDelay = startDelay;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Integer getRunningcount() {
		return runningcount;
	}

	public void setRunningcount(Integer runningcount) {
		this.runningcount = runningcount;
	}

	public void displayPlan() throws SuspendExecution {
		int type;
		System.out.println("");
		System.out.println("TestPlan Details");
		System.out.println("");
		System.out.println("Request Rate : " + reqRate + "  " + "Duration : "
				+ duration);

		for (int i = 0; i < testPlan.size(); i++) {
				System.out.println("URL : "+ dbUrl);
				System.out.println("Driver : "+ dbDriver);
				System.out.println("Username : "+ uname);
				System.out.println("Password : "+ passwd);
				System.out.println("Max Connections : "+ maxConnections);
						}
		System.out.println("");
		for(int i=0;i<query.size();i++){
			System.out.println(query.get(i));
		}
				System.out.println("");
		}
	
	
		@Override
		public void execute(Response resp) throws InterruptedException,
				SuspendExecution {
			// TODO Auto-generated method stub

			this.displayPlan();
			tptwriter.clear();
			respwriter.clear();
			errwriter.clear();
			if (this.reqRate != 0) {
				
				this.running = true;
				this.reqsDetails.clear();
				

				String dbURL = this.getDbUrl();
				String username =this.getUname();
				String password = this.getPasswd();
				/*final List<String> dbqueries=new ArrayList<String>();
				dbqueries=this.getQuery();*/
				
				//int type;

				connectionPool = new BasicDataSource();
				connectionPool.setDriverClassName(this.getDbDriver());
				connectionPool.setUrl(dbURL);
				connectionPool.setInitialSize(1);
				connectionPool.setMaxTotal(this.getMaxConnections());
				connectionPool.setUsername(username);
				connectionPool.setPassword(password);
				
				
				
				for (int i = 0; i < this.getQuery().size(); i++) {
					Output outputrow = new Output();
					Date now = new Date();
					String strDate = sdf.format(now);
				
					outputrow.setRequest("TestPlan-" + this.getId() + "-Request-"
							+ (i + 1));
					//System.out.println("durat "+this.duration.toString());
					outputrow.setDuration(Integer.toString(this.duration));
					outputrow.setTime(strDate);
					outputrow.setInputload("0 reqs/sec");
					outputrow.setErrorrate("0%");
					outputrow.setAvgThroughput("0 reqs/sec");
					outputrow.setCurThroughput("0 reqs/sec");
					outputrow.setResponsetime("0 msec");
					outputrow.setAvgResponsetime("0 msec");
					try {
						tptwriter.add(new FileWriter("webapps/LoadGen/resources/tmpFiles/"+outputrow.getRequest()+"tpt.txt"));
						respwriter.add(new FileWriter("webapps/LoadGen/resources/tmpFiles/"+outputrow.getRequest()+"resp.txt"));
						errwriter.add(new FileWriter("webapps/LoadGen/resources/tmpFiles/"+outputrow.getRequest()+"err.txt"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.info("Error Creating file");
						e.printStackTrace();
					}

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
					// Per second response time
					temp.add((long) 0);
					// Request start time
					temp.add((long) -1);
					this.reqsDetails.put(i, temp);
					// totalresptimes.put(httpreqlist.get(i), (long) 0);
				}
				
				
				this.outputFiber();
				
	
		/*		
			try {

				Connection connection = connectionPool.getConnection();

				stmt = connection.createStatement();
				if (type == 1) {
					stmt.executeUpdate(dbqueries.get(j));
				} else {
					rs = stmt.executeQuery(dbqueries.get(j));
					while (rs.next()) {
						System.out.println(rs.getString("ip"));
					}
				}

			} catch (SQLException ex) {
				System.out.println("SQL Connection Error");
			} finally {

				try {
					if (rs != null)
						rs.close();
					if (dbCon != null)
						dbCon.close();
					if (stmt != null)
						stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
				*/
				
				
				
				Fiber.sleep(this.getStartDelay() * 1000);
				int fiberArraysize = this.duration * this.reqRate;
				Fiber[] fibers = new Fiber[fiberArraysize];
				// System.out.println("Array Size = " + fiberArraysize);
				final int num = this.duration * this.reqRate;
				// int runningcount=num;
				this.setRunningcount(num);
				final RateLimiter rl = RateLimiter.create(this.reqRate);
				Date now1 = new Date();
				logger.info("starttime " + sdf.format(now1));

				//this.outputFiber();
				final Semaphore sem = new Semaphore(1500000);
				int j=0;
				Fiber[] execfibers = new Fiber[num];
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
									for (k = 0; k < this.getQuery().size(); k++) {

											this.reqsDetails
													.get(k)
													.set(0,
															(this.reqsDetails
																	.get(k).get(0)) + 1);

											long startTime = System
													.currentTimeMillis();

											
											DbRequest dbreq=new DbRequest();
											dbreq.setQuery(this.getQuery().get(k));
											dbreq.setConnectionPool(connectionPool);
											dbreq.execute();
											long elapsedTime = System
													.currentTimeMillis()
													- startTime;
											//System.out.println(elapsedTime);
											/*if (currresp.getResponse()
													.getStatusLine().toString()
													.contains("OK")) {
												*/
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
												this.reqsDetails.get(k).set(
														5,
														this.reqsDetails.get(k)
																.get(5)
																+ elapsedTime);
/*
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
												this.reqsDetails.get(k).set(
														5,
														this.reqsDetails.get(k)
																.get(5));

											}*/
									
									}
								} catch (final Throwable t) {

								} 
								finally {
									// logger.info("inside finally");
									sem.release();
									//System.out.println(this.getRunningcount());
									this.setRunningcount(this.getRunningcount() - 1);
								}
							}).start();
					execfibers[j++] = testplansfiber;

				}
				for (Fiber currfiber : execfibers) {
					try {
						if (!this.running)
							break;
						//System.out.println("hello");
						currfiber.join();
						//System.out.println("da");
						
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				this.running=false;
			
				Date now2 = new Date();
				logger.info("endtime " + sdf.format(now2));

			}

			logger.info("TestPlan Finished");
		}

		private void outputFiber() throws InterruptedException, SuspendExecution {
			// TODO Auto-generated method stub
			
			final String FILE_HEADER = String.format(
					"%-30s%-25s%-18s%-18s%-18s%-23s%-10s", "Time", "Request",
					"Input Load", "Avg Througput", "Cur Througput",
					"Cur Response Time", "Error Rate");
			logger.info(FILE_HEADER);
				Fiber<Void> outputfiber = new Fiber<Void>(() -> {
				long i = 1;
				while (this.running) {
					Fiber.sleep(1000);
					printOutput(i);
					i++;
				}
				try {
					
					for (FileWriter fw:tptwriter) {
						fw.flush();
						fw.close();
					}
					for (FileWriter fw:respwriter) {
						fw.flush();
						fw.close();
					}
					for (FileWriter fw:errwriter) {
						fw.flush();
						fw.close();
					}
	/*				fileWriter.flush();

					fileWriter.close();*/

				} catch (IOException e) {

					logger.info("Error while flushing/closing fileWriter !!!");

					e.printStackTrace();

				}

			}).start();

		}

		private void printOutput(long timesec) throws InterruptedException,
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
					if(timesec!=1)
						tptwriter.get(l-1).append(Long.toString(timesec-1)+" "+entry.getValue().get(3).toString()+"\n");
					/*Long timediv=(long) Math.ceil((double)((System.currentTimeMillis()-(entry.getValue().get(5)))/(1000*1.0)));
					*/outputrow.setRequest("TestPlan-" + this.getId() + "-Request-"
							+ (l));
					outputrow.setTime(strDate);
					outputrow.setDuration(Integer.toString(this.duration));
					//System.out.println(timediv);
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
						if(timesec!=1)
							errwriter.get(l-1).append(Long.toString(timesec-1)+" "+df.format(errorrate).toString()+"\n");				
					}
					else{
						outputrow.setErrorrate("0%");
						if(timesec!=1)
							errwriter.get(l-1).append(Long.toString(timesec-1)+" 0"+"\n");				

					}
					if(entry.getValue().get(1)!=0&&entry.getValue().get(6)==-1)
						entry.getValue().set(6,timesec-1);
					long timediv=timesec-entry.getValue().get(6);
					avgThroughput = (entry.getValue().get(1)) /timediv;
					outputrow.setAvgThroughput(avgThroughput + " reqs/sec");
					outputrow.setCurThroughput(entry.getValue().get(3)
							+ " reqs/sec");

					if (entry.getValue().get(3)!=0){
						long rtime=entry.getValue().get(5)
								/ entry.getValue().get(3) ;
						outputrow.setResponsetime(rtime+ " msec");
						if(timesec!=1)
							respwriter.get(l-1).append(Long.toString(timesec-1)+" "+Long.toString(rtime)+"\n");

					}
					else{
						outputrow.setResponsetime("0 msec");
						if(timesec!=1)
							respwriter.get(l-1).append(Long.toString(timesec-1)+" 0"+"\n");

					}
					
					if (avgThroughput!=0)
						outputrow.setAvgResponsetime(entry.getValue().get(4)
								/ (avgThroughput*timediv) + " msec");
					else
						outputrow.setAvgResponsetime("0 msec");

					entry.getValue().set(3, (long) 0);
					entry.getValue().set(5, (long) 0);
					MainController.outputlist.set(this.getOutputrowstart() + l - 1,
							outputrow);
					String message = String.format(
							"%-30s%-25s%-18s%-18s%-18s%-23s%-10s",
							outputrow.getTime(), outputrow.getRequest(),
							outputrow.getInputload(), outputrow.getAvgThroughput(),
							outputrow.getCurThroughput(),
							outputrow.getResponsetime(), outputrow.getErrorrate());

					/*fileWriter.append(message);
					fileWriter.append("\n");*/
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
