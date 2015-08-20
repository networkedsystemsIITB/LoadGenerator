package com.webQ.controller;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.httpclient.FiberHttpClientBuilder;

import com.webQ.Serializers.Serializer;
import com.webQ.Validator.FileUploadValidator;
import com.webQ.model.ConstantTimer;
import com.webQ.model.FileUpload;
import com.webQ.model.HttpRequest;
import com.webQ.model.RegexExtractor;
import com.webQ.model.Test;
import com.webQ.model.TestPlan;
import com.webQ.service.HelloWorldService;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/*import org.apache.log4j.Logger;*/
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController implements Serializable {

	private List<TestPlan> testPlans = new ArrayList<TestPlan>();
	private static final HttpContext BASIC_RESPONSE_HANDLER = null;
	/*
	 * private final Logger logger =
	 * LoggerFactory.getLogger(MainController.class);
	 */
	public final static Logger logger = Logger.getLogger(MainController.class);

	/*
	 * private final Logger logger =
	 * LoggerFactory.getLogger("/home/stanly/loadgen.log");
	 */private final HelloWorldService helloWorldService;
	private List<Object> testPlan = new ArrayList<Object>();
	private List<Integer> httpreqlist = new ArrayList<Integer>();
	public static Boolean test = true;
	private Test randomtest = new Test();

	// private int testplancount;
	public static final CloseableHttpClient client = FiberHttpClientBuilder
			.create(2).setMaxConnPerRoute(100).setMaxConnTotal(10).build();

	FileUpload ufile;

	@Autowired
	public MainController(HelloWorldService helloWorldService) {
		this.helloWorldService = helloWorldService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home() {
		ufile = new FileUpload();
		testPlans.clear();
		testPlan.clear();
		httpreqlist.clear();
		File folder = new File("../../../LoadGen/resources/tmpFiles");
		if (folder.exists()) {
			logger.info(folder.getAbsolutePath());
			deleteFolder(folder);
		} else {
			logger.info(folder.getAbsolutePath());
			logger.info("relative resolution failed");
		}
		File file = new File("../../../LoadGen/resources/tmpFiles");
		if (!file.exists()) {
			if (file.mkdir()) {
				logger.info("Directory is created!");
			} else {
				logger.info("Failed to create directory!");
			}
		}
		// testplancount=0;
		// System.out.println(testPlan.size());
		return new ModelAndView("home", "command", new TestPlan());
	}

	@RequestMapping(value = "/consttimer", method = RequestMethod.POST)
	public @ResponseBody void home_page(
			@ModelAttribute("SpringWeb") ConstantTimer timer,
			@RequestParam("rownum") int rownum, BindingResult result) {
		// System.out.println(rownum);
		if (rownum == testPlan.size())
			testPlan.add(timer);
		else
			testPlan.set(rownum, timer);
		// TestPlan.displayPlan();
		// return "Constant Timer";
	}

	@RequestMapping(value = "/regexextractor", method = RequestMethod.POST)
	public @ResponseBody void home_page(
			@ModelAttribute("SpringWeb") RegexExtractor regexex,
			@RequestParam("rownum") int rownum, BindingResult result) {
		// System.out.println(rownum);
		if (rownum == testPlan.size())
			testPlan.add(regexex);
		else
			testPlan.set(rownum, regexex);
		// TestPlan.displayPlan();
		// return "Regex Extractor";
	}

	@RequestMapping(value = "/httpgetreq", method = RequestMethod.POST)
	public @ResponseBody void home_page(
			@ModelAttribute("SpringWeb") HttpRequest req,
			@RequestParam("rownum") int rownum, BindingResult result) {
		/*
		 * System.out.println(req.getUrl());
		 * System.out.println(req.getHttpType());
		 * System.out.println(req.getPostBody());
		 */

		if (rownum == testPlan.size()) {
			testPlan.add(req);
			httpreqlist.add(rownum);
		} else
			testPlan.set(rownum, req);

		// TestPlan.displayPlan();

	}

	@RequestMapping(value = "/httppostreq", method = RequestMethod.POST)
	public @ResponseBody void home_page(
			@ModelAttribute("SpringWeb") HttpRequest req, BindingResult result) {
		System.out.println("inside post");
		System.out.println(req.getHttpType());
		/*
		 * if (rownum == testPlan.size()) { testPlan.add(req);
		 * httpreqlist.add(rownum); } else testPlan.set(rownum, req);
		 */

		// TestPlan.displayPlan();

	}

	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public @ResponseBody void home_page(@RequestParam("rownum") int rownum) {
		// System.out.println(rownum);
		int flag = 0;
		if (rownum < testPlan.size()) {
			testPlan.remove(rownum);
			for (int i = 0; i < httpreqlist.size(); i++) {
				if (flag == 1) {
					httpreqlist.set(i, httpreqlist.get(i) - 1);
				}

				else if (httpreqlist.get(i) >= rownum) {
					if (httpreqlist.get(i) == rownum) {
						httpreqlist.remove(i);

					}
					i--;
					flag = 1;

				}

			}
		}
		// TestPlan.displayPlan();

	}

	@RequestMapping(value = "/savenormaltestplan", method = RequestMethod.POST)
	public @ResponseBody void home_page(
			@ModelAttribute("SpringWeb") TestPlan newTestPlan) {
		newTestPlan.setTestPlan(testPlan);
		newTestPlan.setHttpreqlist(httpreqlist);

		newTestPlan.displayPlan();
		/*
		 * for(int i=0;i<httpreqlist.size();i++)
		 * System.out.println(httpreqlist.get(i)); newTestPlan.displayPlan();
		 */
		testPlans.add(newTestPlan);

		testPlan = new ArrayList<Object>();

		httpreqlist = new ArrayList<Integer>();

	}

	@RequestMapping(value = "/saverandomtestplan", method = RequestMethod.POST)
	public @ResponseBody void home_page_random(
			@ModelAttribute("SpringWeb") TestPlan newTestPlan) {

		newTestPlan.setTestPlan(testPlan);
		newTestPlan.setHttpreqlist(httpreqlist);

		newTestPlan.displayPlan();
		/*
		 * for(int i=0;i<httpreqlist.size();i++)
		 * System.out.println(httpreqlist.get(i)); newTestPlan.displayPlan();
		 */
		testPlans.add(newTestPlan);

		testPlan = new ArrayList<Object>();

		httpreqlist = new ArrayList<Integer>();

	}

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String index(Map<String, Object> model) throws SuspendExecution,
			InterruptedException {
		Fiber.sleep(10);
		/* logger.debug("index() is executed!"); */
		model.put("title", helloWorldService.getTitle(""));
		model.put("msg", helloWorldService.getDesc());
		return "index";
	}

	@RequestMapping(value = "/hello/{name:.+}", method = RequestMethod.GET)
	public ModelAndView hello(@PathVariable("name") String name)
			throws SuspendExecution, InterruptedException {
		Fiber.sleep(10);
		/*
		 * logger.debug("hello() is executed - $name {}", name);
		 */ModelAndView model = new ModelAndView();
		model.setViewName("index");
		model.addObject("title", helloWorldService.getTitle(name));
		model.addObject("msg", helloWorldService.getDesc());
		return model;
	}

	public HttpResponse httpRequest(String url) throws InterruptedException,
			SuspendExecution {
		HttpGet request = new HttpGet(url);
		HttpResponse response = null;
		try {

			response = client.execute(request, BASIC_RESPONSE_HANDLER);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	public void constantTimer(long milliTimes) throws InterruptedException,
			SuspendExecution {
		Fiber.sleep(milliTimes);
	}

	public Matcher regexExtractor(String regex, String data) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(data);
		return m;
	}

	@RequestMapping(value = "/normalloadgen", method = RequestMethod.POST)
	public void execute(ModelMap model) throws SuspendExecution,
			InterruptedException {
		// PrintWriter out = response.getWriter();
		// System.out.println("dddddd");
		/*
		 * model.addAttribute("reqRate", load.getReqRate());
		 * model.addAttribute("duration", load.getDuration());
		 */

		// int run_wait = 1000000000 / load.getReqRate();

		/*
		 * value val = new value(); val.req=0; val.resp=0;
		 */
		/*
		 * System.out.println("Random Value" + " : " +newTest.getRandom());
		 * System.out.println("MaxReq Value" + " : " +newTest.getMaxreqRate());
		 * System.out.println("MaxDur Value" + " : " +newTest.getMaxduration());
		 * System.out.println("Epoch Value" + " : " +newTest.getEpoch());
		 * newTest.setTestPlans(testPlans);
		 */
		if (logger.isInfoEnabled()) {
			logger.info("Starting");
		}
		System.out.println("");
		System.out
				.println("<------------------------LoadGen Starting-------------------------->");
		System.out.println("");

		int i = 1;

		for (TestPlan currtestplan : testPlans) {
			if (test) {
				System.out.println("Testplan" + i);

				currtestplan.setFilenum(i);
				i++;
				Fiber<Void> testplansfiber = new Fiber<Void>(() -> {
					System.out.println("");
					currtestplan.displayPlan();
					System.out.println("");
					currtestplan.execute(null);
				}).start();
			} else {
				test = true;
				break;
			}
		}

		/*
		 * try { testplansfiber.join(); } catch (ExecutionException e1) { //
		 * TODO Auto-generated catch block e1.printStackTrace(); }
		 */
		if (logger.isInfoEnabled()) {
			logger.info("Test Finished");
		}
		System.out.println("Test Finished");

		// testPlans.clear();
		/*
		 * executor.setTestPlan(testPlan); executor.setHttpreqlist(httpreqlist);
		 * 
		 * 
		 * executor.displayPlan(); System.out.println(""); //TestPlan
		 * executor=new TestPlan();
		 * 
		 * executor.execute(null);
		 */
		/*
		 * for (int i = 0; i < load.getDuration(); ++i) {
		 * 
		 * for (int j = 0; j < load.getReqRate(); ++j) { Fiber<Void> f1 = new
		 * Fiber<Void>( () -> { try{
		 * 
		 * TestPlan executor=new TestPlan();
		 * 
		 * executor.execute(null);
		 */
		/*
		 * //HttpResponse tokengen_response =
		 * httpRequest("http://10.129.26.133:8000/proxy1?limit=100");
		 * //val.req++; HttpResponse tokengen_response =
		 * httpRequest("http://www.google.com"); HttpRequest obj=new
		 * HttpRequest(); System.out.println("Request: " +
		 * Fiber.currentFiber().getName() + " " + tokengen_response
		 * .getStatusLine()); obj.setUrl("http://www.google.com");
		 * obj.execute(null); Matcher m = regexExtractor(
		 * "JMeter: (.*?); url=(.+?),", tokengen_response.toString());
		 * 
		 * 
		 * if (m.find()) {
		 * 
		 * System.out.println("Request: " + Fiber.currentFiber().getName() +
		 * " ,Waittime:" + m.group(1) + " ,URL :" + m.group(2));
		 * constantTimer((long) (Float.parseFloat(m .group(1)) * 1000));
		 * HttpResponse tokencheck_response = httpRequest(m .group(2));
		 * //val.resp++; System.out.println("Request: " +
		 * Fiber.currentFiber().getName() + " " + tokencheck_response
		 * .getStatusLine());
		 * 
		 * 
		 * 
		 * 
		 * }
		 *//*
			 * } catch (Exception ex) {
			 * System.out.println(ex.getLocalizedMessage()); }
			 * 
			 * }).start();
			 * 
			 * Fiber.sleep(0, run_wait); }
			 * //System.out.println("Req: "+val.req/(
			 * i+1)+" "+"Resp: "+val.resp/(i+1)); }
			 */
	}

	@RequestMapping(value = "/randomloadgen", method = RequestMethod.POST)
	public void execute(@ModelAttribute("SpringWeb") Test newTest,
			ModelMap model) throws SuspendExecution, InterruptedException {

		newTest.setTestPlans(testPlans);

		randomtest = newTest;

		if (logger.isInfoEnabled()) {
			logger.info("Starting");
		}
		System.out.println("");
		System.out
				.println("<------------------------LoadGen Starting-------------------------->");
		System.out.println("");
		logger.info("MaxReq Value" + " : " + randomtest.getMaxreqRate());
		logger.info("MaxDur Value" + " : " + randomtest.getMaxduration());
		logger.info("Epoch Value" + " : " + randomtest.getEpoch());
		int filecount = 1;
		int totalepochs = randomtest.getMaxduration() / randomtest.getEpoch();
		logger.info("Total Epochs: " + totalepochs);

		int currMaxReqRate, remainingMix, newMix, currrateindex = 0;
		List<Integer> requestMix = new ArrayList<Integer>();
		List<List<TestPlan>> testPlanslist = new ArrayList<List<TestPlan>>();
		for (int currepoch = 0; currepoch < totalepochs; currepoch++) {
			if (randomtest.getMaxreqRate() != 0)
				currMaxReqRate = randInt(1, randomtest.getMaxreqRate());
			else
				currMaxReqRate = 0;
			remainingMix = currMaxReqRate;
			requestMix.clear();
			for (int i = 0; i < testPlans.size() - 1; ++i) {
				if (remainingMix != 0)
					newMix = randInt(1, remainingMix);
				else
					newMix = 0;
				requestMix.add(newMix);
				remainingMix -= newMix;
			}
			requestMix.add(remainingMix);
			Iterator<Integer> listIterator = requestMix.iterator();
			logger.info("Current Epoch : " + currepoch);
			logger.info("Current Total Request Rate : " + currMaxReqRate);
			currrateindex = 0;
			List<TestPlan> newtestPlans = new ArrayList<TestPlan>();
			for (int i=0;i<testPlans.size();i++){
				TestPlan copycurtest=copyTestPlan(testPlans.get(i));
				
				newtestPlans.add(copycurtest);
			}
			// newtestPlans=testPlans;
			for (int k = 0; k < testPlans.size(); k++) {

				logger.info("Current Request Rate" + requestMix.get(k));
				newtestPlans.get(k).setReqRate(requestMix.get(k));
				newtestPlans.get(k).setDuration(randomtest.getEpoch());
				newtestPlans.get(k).setFilenum(k + 1);
			}
			testPlanslist.add(newtestPlans);

		}
		int num=1;
		/* Fiber<Void> testplansfiber = new Fiber<Void>(() -> { */
		for (List<TestPlan> currtestPlans : testPlanslist) {
			System.out.println("Epoch Number : "+ num);
			logger.info("Epoch Number : "+ num);

			num++;
			for (TestPlan currtestplan : currtestPlans) {
				if (test) {
				/*	System.out.println("outside testplan");
					currtestplan.displayPlan();
					System.out.println("");*/
					/*
					 * System.out.println("Testplan" + filecount);
					 * 
					 * currtestplan.setFilenum(filecount);
					 */
					// filecount++;

					/*
					 * logger.info("Current Request Rate" +
					 * requestMix.get(currrateindex));
					 */
					/*
					 * currtestplan.setReqRate(listIterator.next());
					 * currtestplan.setDuration(randomtest.getEpoch());
					 */
					Fiber<Void> testplansfiber = new Fiber<Void>(() -> {
						/*System.out.println("outside testplan");
						currtestplan.displayPlan();
						System.out.println("");*/
						 //currrateindex++; 

						currtestplan.execute(null);
					}).start();
				} else {
					test = true;
					break;
				}
			}
			Fiber.sleep(randomtest.getEpoch()*1000);
			logger.info("");
		}
		/* } ).start(); */

		/*
		 * try { testplansfiber.join(); } catch (ExecutionException e1) { //
		 * TODO Auto-generated catch block e1.printStackTrace(); }
		 */
		if (logger.isInfoEnabled()) {
			logger.info("Test Finished");
		}
		System.out.println("Test Finished");
	}
	public TestPlan copyTestPlan(TestPlan orig){
		TestPlan copy=new TestPlan();
		copy.setDelay(orig.getDelay());
		copy.setDuration(orig.getDuration());
		copy.setFilenum(orig.getFilenum());
		copy.setHttpreqlist(orig.getHttpreqlist());
		copy.setReqRate(orig.getReqRate());
		copy.setStartDelay(orig.getStartDelay());
		copy.setTestPlan(orig.getTestPlan());
		return copy;
		
	}
	/*public static List<TestPlan> cloneList(List<TestPlan> list) {
	    List<TestPlan> clone = new ArrayList<TestPlan>(list.size());
	    for(TestPlan item: list) clone.add((TestPlan) item.clone());
	    return clone;
	}*/
	@RequestMapping(value = "/randomfileloadgen", method = RequestMethod.POST)
	public void execute() throws SuspendExecution, InterruptedException {

		if (logger.isInfoEnabled()) {
			logger.info("Starting");
		}
		System.out.println("");
		System.out
				.println("<------------------------LoadGen Starting-------------------------->");
		System.out.println("");
		logger.info("MaxReq Value" + " : " + randomtest.getMaxreqRate());
		logger.info("MaxDur Value" + " : " + randomtest.getMaxduration());
		logger.info("Epoch Value" + " : " + randomtest.getEpoch());
		int filecount = 1;
		int totalepochs = randomtest.getMaxduration() / randomtest.getEpoch();
		logger.info("Total Epochs: " + totalepochs);

		int currMaxReqRate, remainingMix, newMix, currrateindex = 0;
		List<Integer> requestMix = new ArrayList<Integer>();
		List<List<TestPlan>> testPlanslist = new ArrayList<List<TestPlan>>();
		for (int currepoch = 0; currepoch < totalepochs; currepoch++) {
			if (randomtest.getMaxreqRate() != 0)
				currMaxReqRate = randInt(1, randomtest.getMaxreqRate());
			else
				currMaxReqRate = 0;
			remainingMix = currMaxReqRate;
			requestMix.clear();
			for (int i = 0; i < testPlans.size() - 1; ++i) {
				if (remainingMix != 0)
					newMix = randInt(1, remainingMix);
				else
					newMix = 0;
				requestMix.add(newMix);
				remainingMix -= newMix;
			}
			requestMix.add(remainingMix);
			Iterator<Integer> listIterator = requestMix.iterator();
			logger.info("Current Epoch : " + currepoch);
			logger.info("Current Total Request Rate : " + currMaxReqRate);
			currrateindex = 0;
			List<TestPlan> newtestPlans = new ArrayList<TestPlan>();
			for (int i=0;i<testPlans.size();i++){
				TestPlan copycurtest=copyTestPlan(testPlans.get(i));
				
				newtestPlans.add(copycurtest);
			}
			// newtestPlans=testPlans;
			for (int k = 0; k < testPlans.size(); k++) {

				logger.info("Current Request Rate" + requestMix.get(k));
				newtestPlans.get(k).setReqRate(requestMix.get(k));
				newtestPlans.get(k).setDuration(randomtest.getEpoch());
				newtestPlans.get(k).setFilenum(k + 1);
			}
			testPlanslist.add(newtestPlans);

		}
		int num=1;
		/* Fiber<Void> testplansfiber = new Fiber<Void>(() -> { */
		for (List<TestPlan> currtestPlans : testPlanslist) {
			System.out.println("Epoch Number : "+ num);
			logger.info("Epoch Number : "+ num);

			num++;
			for (TestPlan currtestplan : currtestPlans) {
				if (test) {
				/*	System.out.println("outside testplan");
					currtestplan.displayPlan();
					System.out.println("");*/
					/*
					 * System.out.println("Testplan" + filecount);
					 * 
					 * currtestplan.setFilenum(filecount);
					 */
					// filecount++;

					/*
					 * logger.info("Current Request Rate" +
					 * requestMix.get(currrateindex));
					 */
					/*
					 * currtestplan.setReqRate(listIterator.next());
					 * currtestplan.setDuration(randomtest.getEpoch());
					 */
					Fiber<Void> testplansfiber = new Fiber<Void>(() -> {
						/*System.out.println("outside testplan");
						currtestplan.displayPlan();
						System.out.println("");*/
						 //currrateindex++; 

						currtestplan.execute(null);
					}).start();
				} else {
					test = true;
					break;
				}
			}
			Fiber.sleep(randomtest.getEpoch()*1000);
			logger.info("");
		}
		/* } ).start(); */

		/*
		 * try { testplansfiber.join(); } catch (ExecutionException e1) { //
		 * TODO Auto-generated catch block e1.printStackTrace(); }
		 */
		if (logger.isInfoEnabled()) {
			logger.info("Test Finished");
		}
		System.out.println("Test Finished");
	}

	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "/normaluploadFile", method = RequestMethod.POST)
	public @ResponseBody String normaluploadFile(
			@RequestParam("fileName") MultipartFile file) {

		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();

				File dir = new File("../../../LoadGen/resources/tmpFiles");
				// System.out.println(dir.getPath());
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + file.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				testPlans.clear();
				testPlans = Serializer.deserialzeTestPlanObject(dir
						.getAbsolutePath()
						+ File.separator
						+ file.getOriginalFilename());
				for (int i = 0; i < testPlans.size(); i++) {
					System.out.println(testPlans.get(i));
				}
				/*
				 * logger.info("Server File Location=" +
				 * serverFile.getAbsolutePath());
				 */
				return "You successfully uploaded file="
						+ file.getOriginalFilename();
			} catch (Exception e) {
				return "You failed to upload " + file.getOriginalFilename()
						+ " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + file.getOriginalFilename()
					+ " because the file was empty.";
		}
	}

	@RequestMapping(value = "/randomuploadFile", method = RequestMethod.POST)
	public @ResponseBody String randomuploadFile(
			@RequestParam("fileName") MultipartFile file) {
		/*
		 * System.out.println(getClass().getClassLoader().getResource(
		 * "logging.properties"));
		 * System.out.println(MainController.class.getClassLoader
		 * ().getResource("logging.properties"));
		 */
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				// System.out.println(file.getName());
				// System.out.println(file.getOriginalFilename());

				// Creating the directory to store file
				// String rootPath = System.getProperty("/home/stanly");
				File dir = new File("../../../LoadGen/resources/tmpFiles");
				// System.out.println(dir.getPath());
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + file.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				randomtest = Serializer.deserialzeTestObject(dir
						.getAbsolutePath()
						+ File.separator
						+ file.getOriginalFilename());

				return "You successfully uploaded file="
						+ file.getOriginalFilename();
			} catch (Exception e) {
				return "You failed to upload " + file.getOriginalFilename()
						+ " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + file.getOriginalFilename()
					+ " because the file was empty.";
		}
	}

	@RequestMapping(value = "/normalsavetofile", method = RequestMethod.POST)
	public @ResponseBody String normalsaveToFile() throws Exception {
		File downloadtest = new File(
				"../../../LoadGen/resources/tmpFiles/test.xml");

		Serializer.serializeTestPlanObject(testPlans,
				downloadtest.getAbsolutePath());
		/*
		 * List<TestPlan> testlist = new ArrayList<TestPlan>();
		 * testlist=Serializer
		 * .deserialzeAddress(downloadtest.getAbsolutePath()); for(int
		 * i=0;i<testlist.size();i++){ System.out.println(testlist.get(i)); }
		 */
		return downloadtest.getAbsolutePath();
	}

	@RequestMapping(value = "/randomsavetofile", method = RequestMethod.POST)
	public @ResponseBody String randomsaveToFile(
			@ModelAttribute("SpringWeb") Test newTest) throws Exception {
		File downloadtest = new File(
				"../../../LoadGen/resources/tmpFiles/test.xml");

		newTest.setTestPlans(testPlans);
		Serializer.serializeTestObject(newTest, downloadtest.getAbsolutePath());
		/*
		 * List<TestPlan> testlist = new ArrayList<TestPlan>();
		 * testlist=Serializer
		 * .deserialzeAddress(downloadtest.getAbsolutePath()); for(int
		 * i=0;i<testlist.size();i++){ System.out.println(testlist.get(i)); }
		 */
		return downloadtest.getAbsolutePath();
	}

	@RequestMapping(value = "/stop", method = RequestMethod.POST)
	public @ResponseBody void stopTest() {

		test = false;

	}

	public static void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}

	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}
