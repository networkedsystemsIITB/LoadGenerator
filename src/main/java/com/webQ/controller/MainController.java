package com.webQ.controller;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.httpclient.FiberHttpClientBuilder;

import com.webQ.features.ConstantTimer;
import com.webQ.features.HttpRequest;
import com.webQ.features.RegexExtractor;
import com.webQ.features.TestPlan;
import com.webQ.model.Load;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class MainController {
	
	public class value{
		public int getReq() {
			return req;
		}
		public void setReq(int req) {
			this.req = req;
		}
		public int getResp() {
			return resp;
		}
		public void setResp(int resp) {
			this.resp = resp;
		}
		private int req;
		private int resp;
	}
	//private List<Object> testPlan = new ArrayList<Object>();
	private static final HttpContext BASIC_RESPONSE_HANDLER = null;
	private final Logger logger = LoggerFactory.getLogger(MainController.class);
	private final HelloWorldService helloWorldService;
	//private List<Object> testPlan = new ArrayList<Object>();
	public static final CloseableHttpClient client = FiberHttpClientBuilder.create(2)
			.setMaxConnPerRoute(100).setMaxConnTotal(10).build();


	@Autowired
	public MainController(HelloWorldService helloWorldService) {
		this.helloWorldService = helloWorldService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home() {

		return new ModelAndView("home", "command", new Load());
	}

	@RequestMapping(value = "/consttimer", method = RequestMethod.POST)
	public @ResponseBody void home_page(
			@ModelAttribute("SpringWeb") ConstantTimer timer,@RequestParam("rownum") int rownum,
			BindingResult result) {
		//System.out.println(rownum);
		if(rownum==TestPlan.testPlan.size())
			TestPlan.testPlan.add(timer);
		else
			TestPlan.testPlan.set(rownum,timer);
		//TestPlan.displayPlan();
		//return "Constant Timer";
	}

	@RequestMapping(value = "/regexextractor", method = RequestMethod.POST)
	public @ResponseBody void home_page(
			@ModelAttribute("SpringWeb") RegexExtractor regexex,@RequestParam("rownum") int rownum,
			BindingResult result) {
		//System.out.println(rownum);
		if(rownum==TestPlan.testPlan.size())
			TestPlan.testPlan.add(regexex);
		else
			TestPlan.testPlan.set(rownum,regexex);
		//TestPlan.displayPlan();
		//return "Regex Extractor";
	}

	@RequestMapping(value = "/httpreq", method = RequestMethod.POST)
	public @ResponseBody void home_page(
			@ModelAttribute("SpringWeb") HttpRequest req,@RequestParam("rownum") int rownum, BindingResult result) {
		
		
		if(rownum==TestPlan.testPlan.size())
			TestPlan.testPlan.add(req);
		else
			TestPlan.testPlan.set(rownum,req);
		//TestPlan.displayPlan();
		

	}

	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public @ResponseBody void home_page(@RequestParam("rownum") int rownum) {
		//System.out.println(rownum);
		
			if(rownum<TestPlan.testPlan.size())
				TestPlan.testPlan.remove(rownum);
			//TestPlan.displayPlan();
		
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
	public void execute(@ModelAttribute("SpringWeb") Load load, ModelMap model)
			throws SuspendExecution, InterruptedException {
	//	PrintWriter out = response.getWriter();

		/*model.addAttribute("reqRate", load.getReqRate());
		model.addAttribute("duration", load.getDuration());*/
		
		
		int run_wait = 1000000000 / load.getReqRate();
		
	

		/*value val = new value();
		val.req=0;
		 val.resp=0;*/
		System.out.println("");
		System.out.println("<------------------------LoadGen Starting-------------------------->");
		System.out.println("");
		System.out.println("Testplan");
		System.out.println("");
		TestPlan.displayPlan();
		System.out.println("");
		for (int i = 0; i < load.getDuration(); ++i) {
			 
			for (int j = 0; j < load.getReqRate(); ++j) {
				Fiber<Void> f1 = new Fiber<Void>(
						() -> {
						try{
							
								TestPlan executor=new TestPlan();
								
								executor.execute(null);
							/*	//HttpResponse tokengen_response = httpRequest("http://10.129.26.133:8000/proxy1?limit=100");
								//val.req++;
							HttpResponse tokengen_response = httpRequest("http://www.google.com");
							HttpRequest obj=new HttpRequest();
							System.out.println("Request: "
									+ Fiber.currentFiber().getName()
									+ " "
									+ tokengen_response
											.getStatusLine());
							obj.setUrl("http://www.google.com");
							obj.execute(null);
								Matcher m = regexExtractor(
										"JMeter: (.*?); url=(.+?),",
										tokengen_response.toString());
								
								
								if (m.find()) {
									
									System.out.println("Request: "
											+ Fiber.currentFiber().getName()
											+ " ,Waittime:" + m.group(1)
											+ " ,URL :" + m.group(2));
									constantTimer((long) (Float.parseFloat(m
											.group(1)) * 1000));
									HttpResponse tokencheck_response = httpRequest(m
											.group(2));
									//val.resp++;
									System.out.println("Request: "
											+ Fiber.currentFiber().getName()
											+ " "
											+ tokencheck_response
													.getStatusLine());
									
									
									
									
								}
*/
							} catch (Exception ex) {
								System.out.println(ex.getLocalizedMessage());
							}

						}).start();

				Fiber.sleep(0, run_wait);
			}
			//System.out.println("Req: "+val.req/(i+1)+" "+"Resp: "+val.resp/(i+1));
		}
	}
	
	
	
}
