package com.webQ.controller;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.httpasyncclient.FiberCloseableHttpAsyncClient;
import co.paralleluniverse.fibers.httpclient.FiberHttpClient;
import co.paralleluniverse.fibers.httpclient.FiberHttpClientBuilder;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.webQ.Serializers.Serializer;
import com.webQ.model.ConstantTimer;
import com.webQ.model.DbTestPlan;
import com.webQ.model.HttpRequest;
import com.webQ.model.Output;
import com.webQ.model.RegexExtractor;
import com.webQ.model.Test;
import com.webQ.model.TestPlan;
import com.webQ.model.WsRequest;
import com.webQ.model.WsTestPlan;
import com.webQ.service.HelloWorldService;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;










/*import org.apache.log4j.Logger;*/
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.spec.PSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController implements Serializable {

	public final static Logger logger = Logger.getLogger(MainController.class);
	public static Boolean test = true;

	private List<TestPlan> testPlans = new ArrayList<TestPlan>();
	private List<DbTestPlan> dbTestPlans = new ArrayList<DbTestPlan>();
	private List<WsTestPlan> wsTestPlans = new ArrayList<WsTestPlan>();
	private static final HttpContext BASIC_RESPONSE_HANDLER = null;
	private final HelloWorldService helloWorldService;
	private List<Object> testPlan = new ArrayList<Object>();
	private List<Integer> httpreqlist = new ArrayList<Integer>();
	public static Map<String, List<String>> globalregexmap = new HashMap<String, List<String>>();
	private Test randomtest = new Test();
	private Test normaltest = new Test();
	public static List<Output> outputlist = new ArrayList<Output>();

	public static final CloseableHttpClient client = FiberHttpClientBuilder
			.create(10).setMaxConnPerRoute(100000).setMaxConnTotal(10000)
			.build();

	@Autowired
	public MainController(HelloWorldService helloWorldService)
			throws SuspendExecution {
		this.helloWorldService = helloWorldService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home() throws SuspendExecution, IOReactorException {

		testPlans.clear();
		dbTestPlans.clear();
		wsTestPlans.clear();
		testPlan.clear();
		httpreqlist.clear();
		randomtest = new Test();
		normaltest = new Test();
		globalregexmap.clear();
		outputlist.clear();
		test = true;

		return new ModelAndView("home", "command", new TestPlan());
	}

	@RequestMapping(value = "/consttimer", method = RequestMethod.POST)
	public @ResponseBody void home_page(
			@ModelAttribute("SpringWeb") ConstantTimer timer,
			@RequestParam("rownum") int rownum, BindingResult result)
			throws SuspendExecution {

		if (rownum == testPlan.size())
			testPlan.add(timer);
		else
			testPlan.set(rownum, timer);

	}

	@RequestMapping(value = "/regexextractor", method = RequestMethod.POST)
	public @ResponseBody void home_page(
			@ModelAttribute("SpringWeb") RegexExtractor regexex,
			@RequestParam("rownum") int rownum, BindingResult result)
			throws SuspendExecution {
		System.out.println(regexex.getRefName());
		System.out.println(regexex.getGlobal());
		if (rownum == testPlan.size())
			testPlan.add(regexex);
		else
			testPlan.set(rownum, regexex);

	}

	@RequestMapping(value = "/httpgetreq", method = RequestMethod.POST)
	public @ResponseBody void home_page(
			@ModelAttribute("SpringWeb") HttpRequest req,
			@RequestParam("rownum") int rownum) throws SuspendExecution {

		if (rownum == testPlan.size()) {
			testPlan.add(req);
			httpreqlist.add(rownum);
		} else
			testPlan.set(rownum, req);

		// TestPlan.displayPlan();

	}

	/*
	 * @RequestMapping(value = "/httppostreq", method = RequestMethod.POST)
	 * public @ResponseBody void home_page(@RequestBody String tabdata) throws
	 * SuspendExecution {
	 */
	@RequestMapping(value = "/httppostreq", method = RequestMethod.POST)
	public @ResponseBody void home_page(@RequestParam("tabdata") String tabdata)
			throws SuspendExecution {
		HttpRequest req = new HttpRequest();
		JSONObject obj = new JSONObject(tabdata);
		int rownum = obj.getInt("rownum");
		req.setUrl(obj.getString("url"));
		req.setHttpType(obj.getString("httpType"));
		req.setPostBody(obj.getString("postBody"));
		Map<String, String> postParamList = new HashMap<String, String>();
		System.out.println(req.getUrl());
		JSONArray paramtable = obj.getJSONArray("postParams");
		for (int i = 0; i < paramtable.length(); ++i) {
			JSONObject params = paramtable.getJSONObject(i);
			postParamList.put(params.get("Param Name").toString(),
					params.get("Param Value").toString());

		}
		req.setPostParamList(postParamList);

		if (rownum == testPlan.size()) {
			testPlan.add(req);
			httpreqlist.add(rownum);
		} else
			testPlan.set(rownum, req);

		// TestPlan.displayPlan();

	}

	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public @ResponseBody void home_page(@RequestParam("rownum") int rownum)
			throws SuspendExecution {
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
			@ModelAttribute("SpringWeb") TestPlan newTestPlan)
			throws SuspendExecution {
		newTestPlan.setTestPlan(testPlan);
		newTestPlan.setHttpreqlist(httpreqlist);
		/*
		 * System.out.println("Duration : " + newTestPlan.getDuration());
		 * System.out.println("Delay : " + newTestPlan.getStartDelay());
		 */
		// newTestPlan.displayPlan();
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
			@ModelAttribute("SpringWeb") TestPlan newTestPlan)
			throws SuspendExecution {

		newTestPlan.setTestPlan(testPlan);
		newTestPlan.setHttpreqlist(httpreqlist);

		// newTestPlan.displayPlan();
		/*
		 * for(int i=0;i<httpreqlist.size();i++)
		 * System.out.println(httpreqlist.get(i)); newTestPlan.displayPlan();
		 */
		testPlans.add(newTestPlan);

		testPlan = new ArrayList<Object>();

		httpreqlist = new ArrayList<Integer>();

	}

	@RequestMapping(value = "/savedbtestplan", method = RequestMethod.POST)
	public @ResponseBody void db_save_plan(@RequestParam("dbdatas") String datas)
			throws SuspendExecution {
		//System.out.println(newTestPlan.getQuery().length);
		DbTestPlan newTestPlan=new DbTestPlan();
		/*JSONObject obj = new JSONObject(dbdatas);*/
		String values[]=datas.split("---");
		//System.out.println(values.length);
		
		newTestPlan.setReqRate(Integer.parseInt(values[0]));
		newTestPlan.setDuration(Integer.parseInt(values[1]));
		newTestPlan.setStartDelay(Integer.parseInt(values[2]));
		newTestPlan.setDbUrl(values[3]);
		newTestPlan.setDbDriver(values[4]);
		newTestPlan.setUname(values[5]);
		newTestPlan.setPasswd(values[6]);
		newTestPlan.setMaxConnections(Integer.parseInt(values[7]));
		//List<String> queries=(List<String>) obj.get("queries");
		List<String> queries = new ArrayList<String>() ;
		for(int i=8;i<values.length;i++)
			queries.add(values[i]);
		
		newTestPlan.setQuery(queries);
		dbTestPlans.add(newTestPlan);
		
		//System.out.println(newTestPlan.getQuery());
		/*
		newTestPlan.setTestPlan(testPlan);
		newTestPlan.setHttpreqlist(httpreqlist);

		testPlans.add(newTestPlan);

		testPlan = new ArrayList<Object>();

		httpreqlist = new ArrayList<Integer>();*/

	}
	
	
	@RequestMapping(value = "/savewstestplan", method = RequestMethod.POST)
	public @ResponseBody void ws_save_plan(@RequestParam("wsdatas") String datas)
			throws SuspendExecution {
		//System.out.println(newTestPlan.getQuery().length);
		WsTestPlan newTestPlan=new WsTestPlan();
		/*JSONObject obj = new JSONObject(dbdatas);*/
		String values[]=datas.split("---");
		//System.out.println(values.length);
		
		newTestPlan.setReqRate(Integer.parseInt(values[0]));
		newTestPlan.setDuration(Integer.parseInt(values[1]));
		newTestPlan.setStartDelay(Integer.parseInt(values[2]));
		
		//List<String> queries=(List<String>) obj.get("queries");
		List<WsRequest> wsrequests = new ArrayList<WsRequest>() ;
		for(int i=3;i<values.length;i++){
			String invalues[]=values[i].split("___");
			WsRequest wsRequest=new WsRequest();
			wsRequest.setUrl(invalues[0]);
			wsRequest.setSoapAction(invalues[1]);
			wsRequest.setXml(invalues[2]);
			wsrequests.add(wsRequest);
			
		}
		newTestPlan.setTestPlan(wsrequests);
		wsTestPlans.add(newTestPlan);


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
		ModelAndView model = new ModelAndView();
		model.setViewName("index");
		model.addObject("title", helloWorldService.getTitle(name));
		model.addObject("msg", helloWorldService.getDesc());
		return model;
	}

	@RequestMapping(value = "/normalloadgen", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> execute(ModelMap model) throws SuspendExecution,
			InterruptedException, ExecutionException {
		test = true;
		File f = new File("/home/stanly/Project/LoadGenerator/loadgen.log");
		PrintWriter writer;
		try {
			writer = new PrintWriter(f);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		File folder = new File("webapps/LoadGen/resources/tmpFiles");
		if (folder.exists()) {
			logger.info(folder.getAbsolutePath());
			deleteFolder(folder);
		} else {
			logger.info(folder.getAbsolutePath());
			logger.info("folder not exists");
		}
		File file = new File("webapps/LoadGen/resources/tmpFiles");
		if (!file.exists()) {
			if (file.mkdir()) {
				logger.info("Directory is created!");
			} else {
				logger.info("Failed to create directory!");
			}
		}

		int rowstart = 0;
		if (logger.isInfoEnabled()) {
			logger.info("Starting");
		}
		int ArraySize = testPlans.size();
		Fiber[] fibers = new Fiber[ArraySize];
		// System.out.println("");
		logger.info("<------------------------LoadGen Starting-------------------------->");
		// System.out.println("");

		int i = 1;
		// System.out.println("size "+testPlans.size());
		for (TestPlan currtestplan : testPlans) {
			if (test) {
				System.out.println("Testplan" + i);

				currtestplan.setId(i);
				currtestplan.setRandom(0);
				currtestplan.setOutputrowstart(rowstart);
				rowstart += currtestplan.getHttpreqlist().size();
				i++;
				for (int j = 0; j < currtestplan.getHttpreqlist().size(); j++)
					outputlist.add(new Output());

				Fiber<Void> testplansfiber = new Fiber<Void>(() -> {
					currtestplan.execute(null);
				}).start();
				int index = i - 2;
				fibers[index] = testplansfiber;
			} else {
				test = true;
				break;
			}
		}
		for (Fiber fiber : fibers) {
			//try {
				if (!test)
					break;
				// System.out.println("hello");
				fiber.join();
				// System.out.println("da");

			/*} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		}
		// test=false;
		if (logger.isInfoEnabled()) {
			logger.info("Test Finished");
		}
		System.out.println("Test Finished");
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	static int log(int x, int base) {
		return (int) (Math.log(x) / Math.log(base));
	}

	@RequestMapping(value = "/randomloadgen", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> execute(@ModelAttribute("SpringWeb") Test newTest,
			ModelMap model) throws SuspendExecution, InterruptedException {
		test = true;
		File f = new File("/home/stanly/Project/LoadGenerator/loadgen.log");
		PrintWriter writer;
		try {
			writer = new PrintWriter(f);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		File folder = new File("webapps/LoadGen/resources/tmpFiles");
		if (folder.exists()) {
			logger.info(folder.getAbsolutePath());
			deleteFolder(folder);
		} else {
			logger.info(folder.getAbsolutePath());
			logger.info("folder not exists");
		}
		File file = new File("webapps/LoadGen/resources/tmpFiles");
		if (!file.exists()) {
			if (file.mkdir()) {
				logger.info("Directory is created!");
			} else {
				logger.info("Failed to create directory!");
			}
		}

		int rowstart = 0;
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
		int flag, newepochcount = 0;
		/*
		 * if (randomtest.getMaxreqRate() <= 500) flag = 0; else flag = 1;
		 */
		int totalepochs = randomtest.getMaxduration() / randomtest.getEpoch();
		/*
		 * if (flag == 0) newepochcount = randomtest.getMaxreqRate() / 2; else {
		 * newepochcount = log(randomtest.getMaxreqRate(), 2); newepochcount -=
		 * 2; }
		 * 
		 * totalepochs = Math.min(totalepochs, newepochcount);
		 */
		logger.info("Total Epochs: " + totalepochs);

		int currMaxReqRate = 0, remainingMix, newMix, currrateindex = 0;
		List<Integer> requestMix = new ArrayList<Integer>();
		List<List<TestPlan>> testPlanslist = new ArrayList<List<TestPlan>>();
		/*
		 * if (flag == 0) currMaxReqRate = 0; else currMaxReqRate = 4;
		 */
		for (int currepoch = 0; currepoch < totalepochs; currepoch++) {
			if (randomtest.getMaxreqRate() != 0) {
				/*
				 * if (flag == 0) currMaxReqRate += 2; else currMaxReqRate *= 2;
				 */
				currMaxReqRate = randInt(1, randomtest.getMaxreqRate());
			} else
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
			for (int i = 0; i < testPlans.size(); i++) {
				TestPlan copycurtest = copyTestPlan(testPlans.get(i));

				newtestPlans.add(copycurtest);
			}
			// newtestPlans=testPlans;
			for (int k = 0; k < testPlans.size(); k++) {

				logger.info("Current Request Rate" + requestMix.get(k));
				newtestPlans.get(k).setReqRate(requestMix.get(k));
				newtestPlans.get(k).setDuration(randomtest.getEpoch());
				newtestPlans.get(k).setId(k + 1);
			}
			testPlanslist.add(newtestPlans);

		}
		int ArraySize = testPlanslist.size() * testPlan.size();
		Fiber[] fibers = new Fiber[ArraySize];
		/* Fiber<Void> testplansfiber = new Fiber<Void>(() -> { */
		int k = 0;
		for (List<TestPlan> currtestPlans : testPlanslist) {

			int i = 1;
			for (TestPlan currtestplan : currtestPlans) {
				if (test) {
					/*
					 * System.out.println("outside testplan");
					 * currtestplan.displayPlan(); System.out.println("");
					 */
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
					currtestplan.setRandom(1);
					currtestplan.setId(i);

					currtestplan.setOutputrowstart(rowstart);
					rowstart += currtestplan.getHttpreqlist().size();
					i++;
					for (int j = 0; j < currtestplan.getHttpreqlist().size(); j++)
						outputlist.add(new Output());
					Fiber<Void> testplansfiber = new Fiber<Void>(() -> {

						/*
						 * System.out.println("outside testplan");
						 * currtestplan.displayPlan(); System.out.println("");
						 */

						// currrateindex++;

							currtestplan.execute(null);
						}).start();
					/*
					 * int index=(k*currtestPlans.size())+(i-2);
					 * fibers[index]=testplansfiber;
					 */
				} else {
					test = true;
					break;
				}
			}
			k++;
			Fiber.sleep(randomtest.getEpoch() * 1000);
			logger.info("");
		}
		/*
		 * for (Fiber fiber : fibers) { try { if (!test) break; fiber.join(); }
		 * catch (ExecutionException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); } }
		 */
		if (logger.isInfoEnabled()) {
			logger.info("Test Finished");
		}
		System.out.println("Test Finished");
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/dbloadgen", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> executedb(ModelMap model) throws SuspendExecution,
			InterruptedException, ExecutionException {
		test = true;

		File f = new File("/home/stanly/Project/LoadGenerator/loadgen.log");
		PrintWriter writer;
		try {
			writer = new PrintWriter(f);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		File folder = new File("webapps/LoadGen/resources/tmpFiles");
		if (folder.exists()) {
			logger.info(folder.getAbsolutePath());
			deleteFolder(folder);
		} else {
			logger.info(folder.getAbsolutePath());
			logger.info("folder not exists");
		}
		File file = new File("webapps/LoadGen/resources/tmpFiles");
		if (!file.exists()) {
			if (file.mkdir()) {
				logger.info("Directory is created!");
			} else {
				logger.info("Failed to create directory!");
			}
		}

		int ArraySize = dbTestPlans.size();
		Fiber[] fibers = new Fiber[ArraySize];
		// System.out.println("");
		logger.info("<------------------------LoadGen Starting-------------------------->");
		// System.out.println("");
		int rowstart = 0;
		int i = 1;
		// System.out.println("size "+testPlans.size());
		for (DbTestPlan currtestplan : dbTestPlans) {
			if (test) {
				System.out.println("Testplan" + i);

				currtestplan.setId(i);
				currtestplan.setRandom(0);
				currtestplan.setOutputrowstart(rowstart);
				rowstart += currtestplan.getQuery().size();
				i++;
				for (int j = 0; j < currtestplan.getQuery().size(); j++)
					outputlist.add(new Output());

				Fiber<Void> testplansfiber = new Fiber<Void>(() -> {
					currtestplan.execute(null);
				}).start();
				int index = i - 2;
				fibers[index] = testplansfiber;
			} else {
				test = true;
				break;
			}
		}
		for (Fiber fiber : fibers) {
		//	try {
				if (!test)
					break;
				// System.out.println("hello");
				fiber.join();
				// System.out.println("da");

		/*	} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		}
		// test=false;
		if (logger.isInfoEnabled()) {
			logger.info("Test Finished");
		}
		System.out.println("Test Finished");
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	
	@RequestMapping(value = "/wsloadgen", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> executews(ModelMap model) throws SuspendExecution,
			InterruptedException, ExecutionException {
		test = true;

		File f = new File("/home/stanly/Project/LoadGenerator/loadgen.log");
		PrintWriter writer;
		try {
			writer = new PrintWriter(f);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		File folder = new File("webapps/LoadGen/resources/tmpFiles");
		if (folder.exists()) {
			logger.info(folder.getAbsolutePath());
			deleteFolder(folder);
		} else {
			logger.info(folder.getAbsolutePath());
			logger.info("folder not exists");
		}
		File file = new File("webapps/LoadGen/resources/tmpFiles");
		if (!file.exists()) {
			if (file.mkdir()) {
				logger.info("Directory is created!");
			} else {
				logger.info("Failed to create directory!");
			}
		}

		int ArraySize = wsTestPlans.size();
		Fiber[] fibers = new Fiber[ArraySize];
		// System.out.println("");
		logger.info("<------------------------LoadGen Starting-------------------------->");
		// System.out.println("");
		int rowstart = 0;
		int i = 1;
		// System.out.println("size "+testPlans.size());
		for (WsTestPlan currtestplan : wsTestPlans) {
			if (test) {
				System.out.println("Testplan" + i);

				currtestplan.setId(i);
				currtestplan.setRandom(0);
				currtestplan.setOutputrowstart(rowstart);
				rowstart += currtestplan.getTestPlan().size();
				i++;
				for (int j = 0; j < currtestplan.getTestPlan().size(); j++)
					outputlist.add(new Output());

				Fiber<Void> testplansfiber = new Fiber<Void>(() -> {
					currtestplan.execute(null);
				}).start();
				int index = i - 2;
				fibers[index] = testplansfiber;
			} else {
				test = true;
				break;
			}
		}
		for (Fiber fiber : fibers) {
			//try {
				if (!test)
					break;
				// System.out.println("hello");
				fiber.join();
				// System.out.println("da");
/*
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		}
		// test=false;
		if (logger.isInfoEnabled()) {
			logger.info("Test Finished");
		}
		System.out.println("Test Finished");
		return new ResponseEntity<String>(HttpStatus.OK);

	}


	public TestPlan copyTestPlan(TestPlan orig) throws SuspendExecution {
		TestPlan copy = new TestPlan();

		copy.setDuration(orig.getDuration());
		copy.setId(orig.getId());
		copy.setHttpreqlist(orig.getHttpreqlist());
		copy.setReqRate(orig.getReqRate());
		copy.setStartDelay(orig.getStartDelay());
		copy.setTestPlan(orig.getTestPlan());
		return copy;

	}

	/*
	 * public static List<TestPlan> cloneList(List<TestPlan> list) {
	 * List<TestPlan> clone = new ArrayList<TestPlan>(list.size()); for(TestPlan
	 * item: list) clone.add((TestPlan) item.clone()); return clone; }
	 */
	@RequestMapping(value = "/randomfileloadgen", method = RequestMethod.POST)
	public void execute() throws SuspendExecution, InterruptedException {
		test = true;
		File f = new File("/home/stanly/Project/LoadGenerator/loadgen.log");
		PrintWriter writer;
		try {
			writer = new PrintWriter(f);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File folder = new File("webapps/LoadGen/resources/tmpFiles");
		if (folder.exists()) {
			logger.info(folder.getAbsolutePath());
			deleteFolder(folder);
		} else {
			logger.info(folder.getAbsolutePath());
			logger.info("folder not exists");
		}
		File file = new File("webapps/LoadGen/resources/tmpFiles");
		if (!file.exists()) {
			if (file.mkdir()) {
				logger.info("Directory is created!");
			} else {
				logger.info("Failed to create directory!");
			}
		}

		testPlans = randomtest.getTestPlans();
		int rowstart = 0;

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

		int flag, newepochcount = 0;
		/*
		 * if (randomtest.getMaxreqRate() <= 500) flag = 0; else flag = 1;
		 */
		int totalepochs = randomtest.getMaxduration() / randomtest.getEpoch();
		/*
		 * if (flag == 0) newepochcount = randomtest.getMaxreqRate() / 2; else {
		 * newepochcount = log(randomtest.getMaxreqRate(), 2); newepochcount -=
		 * 2; }
		 * 
		 * totalepochs = Math.min(totalepochs, newepochcount);
		 */
		logger.info("Total Epochs: " + totalepochs);

		int currMaxReqRate = 0, remainingMix, newMix, currrateindex = 0;
		List<Integer> requestMix = new ArrayList<Integer>();
		List<List<TestPlan>> testPlanslist = new ArrayList<List<TestPlan>>();
		/*
		 * if (flag == 0) currMaxReqRate = 0; else currMaxReqRate = 4;
		 */
		for (int currepoch = 0; currepoch < totalepochs; currepoch++) {
			if (randomtest.getMaxreqRate() != 0) {
				/*
				 * if (flag == 0) currMaxReqRate += 2; else currMaxReqRate *= 2;
				 */
				currMaxReqRate = randInt(1, randomtest.getMaxreqRate());
			}

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
			for (int i = 0; i < testPlans.size(); i++) {
				TestPlan copycurtest = copyTestPlan(testPlans.get(i));

				newtestPlans.add(copycurtest);
			}
			// newtestPlans=testPlans;
			for (int k = 0; k < testPlans.size(); k++) {

				logger.info("Current Request Rate" + requestMix.get(k));
				newtestPlans.get(k).setReqRate(requestMix.get(k));
				newtestPlans.get(k).setDuration(randomtest.getEpoch());
				newtestPlans.get(k).setId(k + 1);
			}
			testPlanslist.add(newtestPlans);

		}
		int ArraySize = testPlanslist.size() * testPlan.size();
		Fiber[] fibers = new Fiber[ArraySize];
		int k = 0;
		/* Fiber<Void> testplansfiber = new Fiber<Void>(() -> { */
		for (List<TestPlan> currtestPlans : testPlanslist) {

			int i = 1;
			for (TestPlan currtestplan : currtestPlans) {
				if (test) {

					currtestplan.setRandom(1);
					currtestplan.setId(i);

					currtestplan.setOutputrowstart(rowstart);
					rowstart += currtestplan.getHttpreqlist().size();
					i++;
					for (int j = 0; j < currtestplan.getHttpreqlist().size(); j++)
						outputlist.add(new Output());
					Fiber<Void> testplansfiber = new Fiber<Void>(() -> {

						/*
						 * System.out.println("outside testplan");
						 * currtestplan.displayPlan(); System.out.println("");
						 */

						currtestplan.execute(null);
					}).start();

				} else {
					test = true;
					break;
				}
			}
			k++;
			Fiber.sleep(randomtest.getEpoch() * 1000);
			logger.info("");
		}
		/*
		 * for (Fiber fiber : fibers) { try { if (!test) break; fiber.join(); }
		 * catch (ExecutionException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); } }
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
			@RequestParam("fileName") MultipartFile file)
			throws SuspendExecution {

		if (!file.isEmpty()) {
			try {
				testPlans.clear();
				testPlan.clear();
				dbTestPlans.clear();
				wsTestPlans.clear();
				httpreqlist.clear();
				randomtest = new Test();
				normaltest = new Test();
				globalregexmap.clear();
				outputlist.clear();
				test = true;
				File f = new File(
						"/home/stanly/Project/LoadGenerator/loadgen.log");
				PrintWriter writer;
				try {
					writer = new PrintWriter(f);
					writer.print("");
					writer.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				File folder = new File("webapps/LoadGen/resources/tmpFiles");
				if (folder.exists()) {
					logger.info(folder.getAbsolutePath());
					deleteFolder(folder);
				} else {
					logger.info(folder.getAbsolutePath());
					logger.info("folder not exists");
				}
				File dir = new File("webapps/LoadGen/resources/tmpFiles");
				if (!dir.exists()) {
					if (dir.mkdir()) {
						logger.info("Directory is created!");
					} else {
						logger.info("Failed to create directory!");
					}
				}
				byte[] bytes = file.getBytes();

				/*
				 * File dir = new File("webapps/LoadGen/resources/tmpFiles"); //
				 * System.out.println(dir.getPath()); if (!dir.exists())
				 * dir.mkdirs();
				 */

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + file.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				/*
				 * testPlans.clear(); globalregexmap.clear();
				 */
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
			@RequestParam("fileName") MultipartFile file)
			throws SuspendExecution {
		/*
		 * System.out.println(getClass().getClassLoader().getResource(
		 * "logging.properties"));
		 * System.out.println(MainController.class.getClassLoader
		 * ().getResource("logging.properties"));
		 */
		// System.out.println("file upload");
		if (!file.isEmpty()) {
			try {
				testPlans.clear();
				testPlan.clear();
				dbTestPlans.clear();
				wsTestPlans.clear();
				httpreqlist.clear();
				randomtest = new Test();
				normaltest = new Test();
				globalregexmap.clear();
				outputlist.clear();
				test = true;
				// Log File Creation
				File f = new File(
						"/home/stanly/Project/LoadGenerator/loadgen.log");
				PrintWriter writer;
				try {
					writer = new PrintWriter(f);
					writer.print("");
					writer.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				File folder = new File("webapps/LoadGen/resources/tmpFiles");
				if (folder.exists()) {
					logger.info(folder.getAbsolutePath());
					deleteFolder(folder);
				} else {
					logger.info(folder.getAbsolutePath());
					logger.info("folder not exists");
				}
				File dir = new File("webapps/LoadGen/resources/tmpFiles");
				if (!dir.exists()) {
					if (dir.mkdir()) {
						logger.info("Directory is created!");
					} else {
						logger.info("Failed to create directory!");
					}
				}

				byte[] bytes = file.getBytes();
				// System.out.println(file.getName());
				// System.out.println(file.getOriginalFilename());

				// Creating the directory to store file
				// String rootPath = System.getProperty("/home/stanly");
				/*
				 * File dir = new File("webapps/LoadGen/resources/tmpFiles"); //
				 * System.out.println(dir.getPath()); if (!dir.exists())
				 * dir.mkdirs();
				 */

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
				System.out.println(randomtest.getTestPlans().size());
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

	@RequestMapping(value = "/dbuploadFile", method = RequestMethod.POST)
	public @ResponseBody String dbuploadFile(
			@RequestParam("fileName") MultipartFile file)
			throws SuspendExecution {

		if (!file.isEmpty()) {
			try {
				testPlans.clear();
				testPlan.clear();
				dbTestPlans.clear();
				wsTestPlans.clear();
				httpreqlist.clear();
				randomtest = new Test();
				normaltest = new Test();
				globalregexmap.clear();
				outputlist.clear();
				test = true;
				File f = new File(
						"/home/stanly/Project/LoadGenerator/loadgen.log");
				PrintWriter writer;
				try {
					writer = new PrintWriter(f);
					writer.print("");
					writer.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				File folder = new File("webapps/LoadGen/resources/tmpFiles");
				if (folder.exists()) {
					logger.info(folder.getAbsolutePath());
					deleteFolder(folder);
				} else {
					logger.info(folder.getAbsolutePath());
					logger.info("folder not exists");
				}
				File dir = new File("webapps/LoadGen/resources/tmpFiles");
				if (!dir.exists()) {
					if (dir.mkdir()) {
						logger.info("Directory is created!");
					} else {
						logger.info("Failed to create directory!");
					}
				}
				byte[] bytes = file.getBytes();

				/*
				 * File dir = new File("webapps/LoadGen/resources/tmpFiles"); //
				 * System.out.println(dir.getPath()); if (!dir.exists())
				 * dir.mkdirs();
				 */

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + file.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				/*
				 * testPlans.clear(); globalregexmap.clear();
				 */
				dbTestPlans = Serializer.deserialzeDbTestPlanObject(dir
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
	
	
	@RequestMapping(value = "/wsuploadFile", method = RequestMethod.POST)
	public @ResponseBody String wsuploadFile(
			@RequestParam("fileName") MultipartFile file)
			throws SuspendExecution {

		if (!file.isEmpty()) {
			try {
				testPlans.clear();
				testPlan.clear();
				dbTestPlans.clear();
				wsTestPlans.clear();
				httpreqlist.clear();
				randomtest = new Test();
				normaltest = new Test();
				globalregexmap.clear();
				outputlist.clear();
				test = true;
				File f = new File(
						"/home/stanly/Project/LoadGenerator/loadgen.log");
				PrintWriter writer;
				try {
					writer = new PrintWriter(f);
					writer.print("");
					writer.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				File folder = new File("webapps/LoadGen/resources/tmpFiles");
				if (folder.exists()) {
					logger.info(folder.getAbsolutePath());
					deleteFolder(folder);
				} else {
					logger.info(folder.getAbsolutePath());
					logger.info("folder not exists");
				}
				File dir = new File("webapps/LoadGen/resources/tmpFiles");
				if (!dir.exists()) {
					if (dir.mkdir()) {
						logger.info("Directory is created!");
					} else {
						logger.info("Failed to create directory!");
					}
				}
				byte[] bytes = file.getBytes();

				/*
				 * File dir = new File("webapps/LoadGen/resources/tmpFiles"); //
				 * System.out.println(dir.getPath()); if (!dir.exists())
				 * dir.mkdirs();
				 */

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + file.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				/*
				 * testPlans.clear(); globalregexmap.clear();
				 */
				wsTestPlans = Serializer.deserialzeWsTestPlanObject(dir
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
	
	
	@RequestMapping(value = "/createsummary", method = RequestMethod.POST)
	public @ResponseBody String createSummary() throws Exception,
			SuspendExecution {
		Document document = new Document();
		float fntSize, lineSpacing;
		fntSize = 16f;
		lineSpacing = 10f;
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(
							"webapps/LoadGen/resources/tmpFiles/summary.pdf"));
			document.open();
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			Paragraph heading = new Paragraph(new Phrase(lineSpacing,
					"Test Summary", FontFactory.getFont(FontFactory.COURIER,
							fntSize)));
			heading.setAlignment(Element.ALIGN_CENTER);
			document.add(heading);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			for (int i = 0; i < outputlist.size(); i++) {

				Paragraph title = new Paragraph(outputlist.get(i).getRequest());
				document.add(Chunk.NEWLINE);
				title.setAlignment(Element.ALIGN_CENTER);
				document.add(title);
				PdfPTable table = new PdfPTable(5); // 2 columns.
				table.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.setWidthPercentage(90); // Width 100%
				table.setSpacingBefore(10f); // Space before table
				table.setSpacingAfter(10f); // Space after table
				float[] columnWidths = { 1f, 1f, 1f, 1f, 1f };
				table.setWidths(columnWidths);

				PdfPCell cell1 = new PdfPCell(new Paragraph("Request Rate"));
				cell1.setBorderColor(BaseColor.BLACK);
				cell1.setPaddingLeft(10);
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

				PdfPCell cell2 = new PdfPCell(new Paragraph("Duration"));
				cell2.setBorderColor(BaseColor.BLACK);
				cell2.setPaddingLeft(10);
				cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

				PdfPCell cell3 = new PdfPCell(new Paragraph(
						"Average Throughput"));
				cell3.setBorderColor(BaseColor.BLACK);
				cell3.setPaddingLeft(10);
				cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

				PdfPCell cell4 = new PdfPCell(new Paragraph(
						"Average Response Time"));
				cell4.setBorderColor(BaseColor.BLACK);
				cell4.setPaddingLeft(10);
				cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

				PdfPCell cell5 = new PdfPCell(new Paragraph("Error Rate"));
				cell5.setBorderColor(BaseColor.BLACK);
				cell5.setPaddingLeft(10);
				cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);

				PdfPCell cell6 = new PdfPCell(new Paragraph(outputlist.get(i)
						.getInputload()));
				cell6.setBorderColor(BaseColor.BLACK);
				cell6.setPaddingLeft(10);
				cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

				// System.out.println("durt"+ outputlist.get(i).getDuration());
				PdfPCell cell7 = new PdfPCell(new Paragraph(outputlist.get(i)
						.getDuration() + " sec"));
				cell7.setBorderColor(BaseColor.BLACK);
				cell7.setPaddingLeft(10);
				cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);

				PdfPCell cell8 = new PdfPCell(new Paragraph(outputlist.get(i)
						.getAvgThroughput()));
				cell8.setBorderColor(BaseColor.BLACK);
				cell8.setPaddingLeft(10);
				cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);

				PdfPCell cell9 = new PdfPCell(new Paragraph(outputlist.get(i)
						.getAvgResponsetime()));
				cell9.setBorderColor(BaseColor.BLACK);
				cell9.setPaddingLeft(10);
				cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);

				PdfPCell cell10 = new PdfPCell(new Paragraph(outputlist.get(i)
						.getErrorrate()));
				cell10.setBorderColor(BaseColor.BLACK);
				cell10.setPaddingLeft(10);
				cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);

				table.addCell(cell1);
				table.addCell(cell2);
				table.addCell(cell3);
				table.addCell(cell4);
				table.addCell(cell5);
				table.addCell(cell6);
				table.addCell(cell7);
				table.addCell(cell8);
				table.addCell(cell9);
				table.addCell(cell10);

				document.add(table);
				document.add(Chunk.NEWLINE);
				document.add(Chunk.NEWLINE);
				DefaultCategoryDataset tptgraph = new DefaultCategoryDataset();

				try (BufferedReader br = new BufferedReader(new FileReader(
						"webapps/LoadGen/resources/tmpFiles/"
								+ outputlist.get(i).getRequest() + "tpt.txt"))) {
					// tptgraph.addValue(0, "Throughput" , 0" );
					for (String line; (line = br.readLine()) != null;) {
						// process the line.
						String values[] = line.split(" ", 2);
						tptgraph.addValue(Integer.parseInt(values[1]),
								"Throughput", values[0]);

					}
					// line is not visible here.
				}

				JFreeChart lineChartObject = ChartFactory.createLineChart(
						"Time Vs Throughput", "Time", "Throughput", tptgraph,
						PlotOrientation.VERTICAL, true, true, false);

				int width = 640;
				int height = 480;
				File lineChart = new File("webapps/LoadGen/resources/tmpFiles/"
						+ outputlist.get(i).getRequest() + "tpt.jpeg");
				ChartUtilities.saveChartAsJPEG(lineChart, lineChartObject,
						width, height);

				
				DefaultCategoryDataset respgraph = new DefaultCategoryDataset();

				try (BufferedReader br = new BufferedReader(new FileReader(
						"webapps/LoadGen/resources/tmpFiles/"
								+ outputlist.get(i).getRequest() + "resp.txt"))) {

					for (String line; (line = br.readLine()) != null;) {
						// process the line.
						String values[] = line.split(" ", 2);
						respgraph.addValue(Integer.parseInt(values[1]),
								"Response Time", values[0]);

					}
					// line is not visible here.
				}

				lineChartObject = ChartFactory.createLineChart(
						"Time Vs Response Time", "Time", "Response Time",
						respgraph, PlotOrientation.VERTICAL, true, true, false);

				lineChart = new File("webapps/LoadGen/resources/tmpFiles/"
						+ outputlist.get(i).getRequest() + "resp.jpeg");
				ChartUtilities.saveChartAsJPEG(lineChart, lineChartObject,
						width, height);

				
				DefaultCategoryDataset errgraph = new DefaultCategoryDataset();

				try (BufferedReader br = new BufferedReader(new FileReader(
						"webapps/LoadGen/resources/tmpFiles/"
								+ outputlist.get(i).getRequest() + "err.txt"))) {

					for (String line; (line = br.readLine()) != null;) {
						// process the line.
						String values[] = line.split(" ", 2);
						errgraph.addValue(Integer.parseInt(values[1]),
								"Error Rate", values[0]);

					}
					// line is not visible here.
				}

				lineChartObject = ChartFactory.createLineChart(
						"Time Vs Error Rate", "Time", "Error Rate",
						errgraph, PlotOrientation.VERTICAL, true, true, false);

				lineChart = new File("webapps/LoadGen/resources/tmpFiles/"
						+ outputlist.get(i).getRequest() + "err.jpeg");
				ChartUtilities.saveChartAsJPEG(lineChart, lineChartObject,
						width, height);
				

			    table = new PdfPTable(3);
		        table.setWidthPercentage(100);
		        table.addCell(createImageCell("webapps/LoadGen/resources/tmpFiles/"
						+ outputlist.get(i).getRequest() + "tpt.jpeg"));
		        table.addCell(createImageCell("webapps/LoadGen/resources/tmpFiles/"
						+ outputlist.get(i).getRequest() + "resp.jpeg"));
		        table.addCell(createImageCell("webapps/LoadGen/resources/tmpFiles/"
						+ outputlist.get(i).getRequest() + "err.jpeg"));
		        document.add(table);
			}
			document.addAuthor("Stanly Thomas");
			document.addCreationDate();
			document.addCreator("LoadGen.com");
			document.addTitle("Test Summary");
			document.addSubject("The summary of the load test");
			document.close();
			writer.close();

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return "success";
	}
	
	public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        return cell;
    }

	@RequestMapping(value = "/normalsavetofile", method = RequestMethod.POST)
	public @ResponseBody String normalsaveToFile() throws Exception,
			SuspendExecution {
		
		File folder = new File("webapps/LoadGen/resources/tmpFiles");
		if (folder.exists()) {
			//logger.info(folder.getAbsolutePath());
			deleteFolder(folder);
		} else {
			//logger.info(folder.getAbsolutePath());
			//logger.info("folder not exists");
		}
		File file = new File("webapps/LoadGen/resources/tmpFiles");
		if (!file.exists()) {
			if (file.mkdir()) {
				//logger.info("Directory is created!");
			} else {
				//logger.info("Failed to create directory!");
			}
		}
		
		File downloadtest = new File(
				"webapps/LoadGen/resources/tmpFiles/test.xml");

		Serializer.serializeTestPlanObject(testPlans,
				downloadtest.getAbsolutePath());
	
		return downloadtest.getAbsolutePath();
	}

	@RequestMapping(value = "/randomsavetofile", method = RequestMethod.POST)
	public @ResponseBody String randomsaveToFile(
			@ModelAttribute("SpringWeb") Test newTest) throws Exception,
			SuspendExecution {
		File folder = new File("webapps/LoadGen/resources/tmpFiles");
		if (folder.exists()) {
			//logger.info(folder.getAbsolutePath());
			deleteFolder(folder);
		} else {
			//logger.info(folder.getAbsolutePath());
			//logger.info("folder not exists");
		}
		File file = new File("webapps/LoadGen/resources/tmpFiles");
		if (!file.exists()) {
			if (file.mkdir()) {
				//logger.info("Directory is created!");
			} else {
				//logger.info("Failed to create directory!");
			}
		}
		
		File downloadtest = new File(
				"webapps/LoadGen/resources/tmpFiles/test.xml");

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
	
	@RequestMapping(value = "/dbsavetofile", method = RequestMethod.POST)
	public @ResponseBody String dbsaveToFile() throws Exception,
			SuspendExecution {
		
		
		File folder = new File("webapps/LoadGen/resources/tmpFiles");
		if (folder.exists()) {
			//logger.info(folder.getAbsolutePath());
			deleteFolder(folder);
		} else {
			//logger.info(folder.getAbsolutePath());
			//logger.info("folder not exists");
		}
		File file = new File("webapps/LoadGen/resources/tmpFiles");
		if (!file.exists()) {
			if (file.mkdir()) {
				//logger.info("Directory is created!");
			} else {
				//logger.info("Failed to create directory!");
			}
		}
		
		File downloadtest = new File(
				"webapps/LoadGen/resources/tmpFiles/test.xml");
		//System.out.println("hi");
		Serializer.serializeDbTestPlanObject(dbTestPlans,
				downloadtest.getAbsolutePath());
		return downloadtest.getAbsolutePath();
	}

	@RequestMapping(value = "/wssavetofile", method = RequestMethod.POST)
	public @ResponseBody String wssaveToFile() throws Exception,
			SuspendExecution {
		
		
		File folder = new File("webapps/LoadGen/resources/tmpFiles");
		if (folder.exists()) {
			//logger.info(folder.getAbsolutePath());
			deleteFolder(folder);
		} else {
			//logger.info(folder.getAbsolutePath());
			//logger.info("folder not exists");
		}
		File file = new File("webapps/LoadGen/resources/tmpFiles");
		if (!file.exists()) {
			if (file.mkdir()) {
				//logger.info("Directory is created!");
			} else {
				//logger.info("Failed to create directory!");
			}
		}
		
		File downloadtest = new File(
				"webapps/LoadGen/resources/tmpFiles/test.xml");
		//System.out.println("hi");
		Serializer.serializeWsTestPlanObject(wsTestPlans,
				downloadtest.getAbsolutePath());
		return downloadtest.getAbsolutePath();
	}
	
	@RequestMapping(value = "/stop", method = RequestMethod.POST)
	public @ResponseBody void stopTest() throws SuspendExecution {
		outputlist.clear();
		test = false;

	}

	@RequestMapping(value = "/output", method = RequestMethod.POST)
	public @ResponseBody String outputTable() throws SuspendExecution {
		String tabledata = "<table class='table table-striped table-bordered table-condensed well' id='outtable'>";
		String firstrow = "<tr>";
		firstrow += "<td class='ui-helper-center' style='vertical-align: middle;'>Time</td>";
		firstrow += "<td class='ui-helper-center' style='vertical-align: middle;'>Request</td>";
		firstrow += "<td class='ui-helper-center' style='vertical-align: middle;'>Input Load</td>";
		firstrow += "<td class='ui-helper-center' style='vertical-align: middle;'>Avg Throughput</td>";
		firstrow += "<td class='ui-helper-center' style='vertical-align: middle;'>Cur Throughput</td>";
		firstrow += "<td class='ui-helper-center' style='vertical-align: middle;'>Cur Response Time</td>";
		firstrow += "<td class='ui-helper-center' style='vertical-align: middle;'>Error Rate</td>";
		firstrow += "<td class='ui-helper-center' style='vertical-align: middle;'>Graphs</td>";

		firstrow += "</tr>";

		tabledata += firstrow;
		for (int i = 0; i < outputlist.size(); i++) {
			String newrow = "<tr>";
			newrow += "<td class='ui-helper-center' style='vertical-align: middle;'>"
					+ outputlist.get(i).getTime() + "</td>";
			newrow += "<td class='ui-helper-center' style='vertical-align: middle;'>"
					+ outputlist.get(i).getRequest() + "</td>";
			newrow += "<td class='ui-helper-center' style='vertical-align: middle;'>"
					+ outputlist.get(i).getInputload() + "</td>";
			newrow += "<td class='ui-helper-center' style='vertical-align: middle;'>"
					+ outputlist.get(i).getAvgThroughput() + "</td>";
			newrow += "<td class='ui-helper-center' style='vertical-align: middle;'>"
					+ outputlist.get(i).getCurThroughput() + "</td>";
			newrow += "<td class='ui-helper-center' style='vertical-align: middle;'>"
					+ outputlist.get(i).getResponsetime() + "</td>";
			newrow += "<td class='ui-helper-center' style='vertical-align: middle;'>"
					+ outputlist.get(i).getErrorrate() + "</td>";
			newrow += "<td class='ui-helper-center' style='vertical-align: middle;'><input type='button' id='graphButton' class='btn btn-default btnGraph' value='Graphs'> </td>";
			newrow += "</tr>";
			tabledata += newrow;
		}
		tabledata += "</table>";
		// System.out.println(tabledata);
		return tabledata;
	}

	@RequestMapping(value = "/graph", method = RequestMethod.POST)
	public @ResponseBody String graph(@RequestParam("rownum") int rownum)
			throws SuspendExecution {
		String data = "";
		// for (int i = 0; i < outputlist.size(); i++) {
		/*
		 * data += outputlist.get(i).getCurThroughput().split(" ", 2)+" " +
		 * outputlist.get(i).getResponsetime().split(" ", 2)+" " +
		 * outputlist.get(i).getErrorrate().split("%", 2);
		 */
		rownum--;
		String tpt[] = outputlist.get(rownum).getCurThroughput().split(" ", 2);
		String rsp[] = outputlist.get(rownum).getResponsetime().split(" ", 2);
		String err[] = outputlist.get(rownum).getErrorrate().split("%", 2);
		String time[] = outputlist.get(rownum).getTime().split(" ", 2);
		// System.out.println(time);
		data = tpt[0] + " " + rsp[0] + " " + err[0] + " " + time[0] + time[1];
		// System.out.println(data);
		// System.out.println(rownum);
		// }
		return data;
	}

	public static void deleteFolder(File folder) throws SuspendExecution {
		File[] files = folder.listFiles();
		try {
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
		} catch (Exception e) {
			logger.info("failed to delete folder");
		}
	}

	public static int randInt(int min, int max) throws SuspendExecution {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

}
