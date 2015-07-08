package com.webQ.controller;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.httpasyncclient.FiberCloseableHttpAsyncClient;
import co.paralleluniverse.fibers.httpclient.FiberHttpClientBuilder;

import com.google.common.util.concurrent.RateLimiter;
import com.webQ.model.Load;
import com.webQ.service.HelloWorldService;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Future;

@Controller
public class MyClass {

	private static final HttpContext BASIC_RESPONSE_HANDLER = null;
	private final Logger logger = LoggerFactory.getLogger(MyClass.class);
	private final HelloWorldService helloWorldService;

	@Autowired
	public MyClass(HelloWorldService helloWorldService) {
		this.helloWorldService = helloWorldService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home() {
		return new ModelAndView("home", "command", new Load());
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

	@RequestMapping(value = "loadgen", method = RequestMethod.POST)
	public String hello(@ModelAttribute("SpringWeb") Load load, ModelMap model)
			throws SuspendExecution, InterruptedException {

		model.addAttribute("reqRate", load.getReqRate());
		model.addAttribute("duration", load.getDuration());
		System.out.println("doneeeeeeeeee");
		HttpGet request = new HttpGet(
				"http://10.129.26.133:8000/proxy1?limit=515000");
		final CloseableHttpClient client = FiberHttpClientBuilder.create(2).
				setMaxConnPerRoute(load.getReqRate()).setMaxConnTotal(10).build();
		
		for (int i = 0; i < load.getDuration(); ++i) {
			for (int j = 0; j < load.getReqRate(); ++j) {
				Fiber<Void> f1 = new Fiber<Void>(() -> {

					try {

						// Future<HttpResponse> future = client.execute(request,
						// null);
						HttpResponse response1 = client.execute(request,
								BASIC_RESPONSE_HANDLER);
						// HttpResponse response1 = future.get();
						Header headers = response1.getFirstHeader("Jmeter");
						String[] str = headers.getValue().split(";");
						String[] str1 = str[1].split("=", 2);
						System.out.println("Request:"
								+ Fiber.currentFiber().getName()
								+ " ,Waittime:" + str[0] + " ,URL :" + str1[1]);
						Fiber.sleep((long) (Float.parseFloat(str[0]) * 1000));
						HttpGet request1 = new HttpGet(str1[1]);
					
						HttpResponse response2 = client.execute(request1,
								BASIC_RESPONSE_HANDLER);

						System.out.println("Request: "
								+ Fiber.currentFiber().getName() + " "
								+ response2);

						// System.out.println(request.getRequestLine() + "->" +
						// response1.getStatusLine());
					} catch (Exception ex) {
						System.out.println(ex.getLocalizedMessage());
					}

				}).start();
			}
			Fiber.sleep(1000);
		}
		
		return "output";
	}

}
