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
import com.webQ.model.TestPlan;
import com.webQ.service.HelloWorldService;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController implements Serializable{

	private List<TestPlan> testPlans = new ArrayList<TestPlan>();
	private static final HttpContext BASIC_RESPONSE_HANDLER = null;
	private final Logger logger = LoggerFactory.getLogger(MainController.class);
	private final HelloWorldService helloWorldService;
	private List<Object> testPlan = new ArrayList<Object>();
	private List<Integer> httpreqlist = new ArrayList<Integer>();
	public static Boolean test=true;

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

	@RequestMapping(value = "/httpreq", method = RequestMethod.POST)
	public @ResponseBody void home_page(
			@ModelAttribute("SpringWeb") HttpRequest req,
			@RequestParam("rownum") int rownum, BindingResult result) {

		if (rownum == testPlan.size()) {
			testPlan.add(req);
			httpreqlist.add(rownum);
		} else
			testPlan.set(rownum, req);

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

	@RequestMapping(value = "/savetestplan", method = RequestMethod.POST)
	public @ResponseBody void home_page(
			@ModelAttribute("SpringWeb") TestPlan newTestPlan) {
		newTestPlan.setTestPlan(testPlan);
		newTestPlan.setHttpreqlist(httpreqlist);
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
		logger.debug("index() is executed!");
		model.put("title", helloWorldService.getTitle(""));
		model.put("msg", helloWorldService.getDesc());
		return "index";
	}

	@RequestMapping(value = "/hello/{name:.+}", method = RequestMethod.GET)
	public ModelAndView hello(@PathVariable("name") String name)
			throws SuspendExecution, InterruptedException {
		Fiber.sleep(10);
		logger.debug("hello() is executed - $name {}", name);
		ModelAndView model = new ModelAndView();
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


	@RequestMapping(value = "/loadgen", method = RequestMethod.POST)
	public void execute(ModelMap model) throws SuspendExecution,
			InterruptedException {
		// PrintWriter out = response.getWriter();
		//System.out.println("dddddd");
		/*
		 * model.addAttribute("reqRate", load.getReqRate());
		 * model.addAttribute("duration", load.getDuration());
		 */

		// int run_wait = 1000000000 / load.getReqRate();

		/*
		 * value val = new value(); val.req=0; val.resp=0;
		 */
		System.out.println("");
		System.out
				.println("<------------------------LoadGen Starting-------------------------->");
		System.out.println("");
		// for(int i=0;i<testPlans.size();++i){
		int i = 1;
		/*Serializer objwriter=new Serializer();*/
		/*Serializer.serializeObject(testPlans,"/home/stanly/Project/LoadGenerator/src/main/webapp/resources/tmpFiles/objfile");
		List<TestPlan> testlist = new ArrayList<TestPlan>();
		testlist=Serializer.deserialzeAddress("/home/stanly/Project/LoadGenerator/src/main/webapp/resources/tmpFiles/objfile");*/
		for (TestPlan currtestplan : testPlans) {
			if(test){
			System.out.println("Testplan" + i);
			
			System.out.println("");
			currtestplan.displayPlan();
			System.out.println("");
			currtestplan.setFilenum(i);
			i++;
			Fiber<Void> testplansfiber = new Fiber<Void>(() -> {

				currtestplan.execute(null);
				}).start();
			}
			else{
				test=true;
				break;
			}
		}
		
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

    /**
     * Upload single file using Spring Controller
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public @ResponseBody
    String uploadFileHandler(@RequestParam("fileName") MultipartFile file) {

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                //System.out.println(file.getName());
               // System.out.println(file.getOriginalFilename());
 
                // Creating the directory to store file
               // String rootPath = System.getProperty("/home/stanly");
                File dir = new File("/home/stanly/Project/LoadGenerator/src/main/webapp/resources/tmpFiles");
                //System.out.println(dir.getPath());
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
                testPlans=Serializer.deserialzeAddress(dir.getAbsolutePath()
                        + File.separator + file.getOriginalFilename());
                for(int i=0;i<testPlans.size();i++){
            		System.out.println(testPlans.get(i));
            	}
                logger.info("Server File Location="
                        + serverFile.getAbsolutePath());
 
                return "You successfully uploaded file=" + file.getOriginalFilename();
            } catch (Exception e) {
                return "You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + file.getOriginalFilename()
                    + " because the file was empty.";
        }
    }
 


@RequestMapping(value = "/savetofile", method = RequestMethod.POST)
public @ResponseBody
String saveToFile() {
	File downloadtest=new File("/home/stanly/Project/LoadGenerator/src/main/webapp/resources/tmpFiles/test.wlg");
	//System.out.println(downloadtest.getAbsolutePath());
	Serializer.serializeObject(testPlans, downloadtest.getAbsolutePath());
	/*List<TestPlan> testlist = new ArrayList<TestPlan>();
	testlist=Serializer.deserialzeAddress(downloadtest.getAbsolutePath());
	for(int i=0;i<testlist.size();i++){
		System.out.println(testlist.get(i));
	}*/
    return downloadtest.getAbsolutePath();
    }

@RequestMapping(value = "/stop", method = RequestMethod.POST)
public @ResponseBody void stopTest() {

	test=false;

}

}

