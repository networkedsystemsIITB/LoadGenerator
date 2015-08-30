package com.webQ.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import com.webQ.controller.MainController;
import com.webQ.interfaces.Feature;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.httpclient.FiberHttpClientBuilder;

public class HttpRequest implements Feature, Serializable {

	private String url;
	private static final HttpContext BASIC_RESPONSE_HANDLER = null;
	
	 /* final CloseableHttpClient client = FiberHttpClientBuilder.create(2)
	  .setMaxConnPerRoute(100).setMaxConnTotal(10).build();*/
	 
	private static final String USER_AGENT = null;
	private Map<String, String> postParamList = new HashMap<String, String>();
	private String httpType;
	private String postBody;

	public String getUrl() throws SuspendExecution {
		return url;
	}

	public void setUrl(String url) throws SuspendExecution {
		this.url = url;
	}

	public Map<String, String> getPostParamList() throws SuspendExecution {
		return postParamList;
	}

	public void setPostParamList(Map<String, String> postParamList)
			throws SuspendExecution {
		this.postParamList = postParamList;
	}

	public String getHttpType() throws SuspendExecution {
		return httpType;
	}

	public void setHttpType(String httpType) throws SuspendExecution {
		this.httpType = httpType;
	}

	public String getPostBody() throws SuspendExecution {
		return postBody;
	}

	public void setPostBody(String postBody) throws SuspendExecution {
		this.postBody = postBody;
	}

	@Override
	public void execute(Response resp) throws InterruptedException,
			SuspendExecution {
		try {
			String requrl = this.getUrl();
			String localregex = "\\$([A-Za-z0-9]+)_([0-9]+)";
			Pattern p = Pattern.compile(localregex);
			Matcher m = p.matcher(requrl.toString());
			String globalregex = "\\#([A-Za-z0-9]+)_([0-9]+)";
			Pattern q = Pattern.compile(globalregex);
			Matcher n = q.matcher(requrl.toString());
			Map<String, List<String>> regexmap = resp.getRegexmap();

			if (m.find()) {
				System.out.println("inside http m");
				String refname = m.group(1);
				int index = Integer.parseInt(m.group(2));
				for (Entry<String, List<String>> entry : regexmap.entrySet()) {
					if (entry.getKey().equals(refname)) {

						requrl = this.getUrl().replaceFirst(localregex,
								entry.getValue().get(index - 1));

						break;
					}
				}

			} else if (n.find()) {
				System.out.println("inside http n");
				String refname = n.group(1);
				int index = Integer.parseInt(n.group(2));
				for (Entry<String, List<String>> entry : MainController.globalregexmap
						.entrySet()) {
					if (entry.getKey().equals(refname)) {

						requrl = this.getUrl().replaceFirst(globalregex,
								entry.getValue().get(index - 1));

						break;
					}
				}

			}
			//System.out.println("request " + requrl);
		/*	CloseableHttpResponse response = null;*/

			if (this.getHttpType() == "POST") {
				HttpPost postrequest = new HttpPost(requrl);

				if (this.getPostBody().length() == 0) {
					List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
					for (Entry<String, String> entry : this.getPostParamList()
							.entrySet()) {

						urlParameters.add(new BasicNameValuePair(
								entry.getKey(), entry.getValue()));
					}
					postrequest.setEntity(new UrlEncodedFormEntity(
							urlParameters));

				} else {
					HttpEntity entity = new ByteArrayEntity(this.getPostBody()
							.getBytes("UTF-8"));
					postrequest.setEntity(entity);
					postrequest.setHeader("User-Agent", USER_AGENT);
				}
				System.out.println(Fiber.currentFiber().getName() + " "
						+ requrl);
				CloseableHttpResponse response=null;
				try  {
					response= MainController.client.execute(postrequest);
					System.out.println("Request: " + Fiber.currentFiber().getName()
							+ " " + response.getStatusLine());
					resp.setResponse(response);
					} catch (final Throwable t) {
						System.out.println("Exception in httppost");
						resp.setResponse(response);
					}
				/*response = MainController.client.execute(postrequest,
						BASIC_RESPONSE_HANDLER);
				System.out.println("Request: " + Fiber.currentFiber().getName()
						+ " " + response.getStatusLine());*/
			} else {
				HttpGet getrequest = new HttpGet(requrl);
				System.out.println(Fiber.currentFiber().getName() + " "
						+ requrl);
				CloseableHttpResponse response=null;
				try {
					response= MainController.client.execute(getrequest);
					System.out.println("Request: " + Fiber.currentFiber().getName()
							+ " " + response.getStatusLine());
					resp.setResponse(response);
					} catch (final Throwable t) {
						System.out.println("Exception in httpget");
						resp.setResponse(response);
					}
				/*MainController.client.wait();*/
/*				 response = MainController.client.execute(getrequest);
*/				
				/*response = MainController.client.execute(getrequest,
						BASIC_RESPONSE_HANDLER);*/
				/*System.out.println("Request: " + Fiber.currentFiber().getName()
						+ " " + response.getStatusLine());
*/
			}
			/*
			 * String xml = "<xml>xxxx</xml>"; HttpEntity entity = new
			 * ByteArrayEntity(xml.getBytes("UTF-8"));
			 * 
			 * HttpPost postrequest = new HttpPost(requrl);
			 * postrequest.setEntity(entity);
			 * postrequest.setHeader("User-Agent", USER_AGENT);
			 * 
			 * List<NameValuePair> urlParameters = new
			 * ArrayList<NameValuePair>(); urlParameters.add(new
			 * BasicNameValuePair("sn", "C02G8416DRJM")); urlParameters.add(new
			 * BasicNameValuePair("cn", "")); urlParameters.add(new
			 * BasicNameValuePair("locale", "")); urlParameters.add(new
			 * BasicNameValuePair("caller", "")); urlParameters.add(new
			 * BasicNameValuePair("num", "12345"));
			 * 
			 * postrequest.setEntity(new UrlEncodedFormEntity(urlParameters));
			 */

			/*
			 * MainController.logger.info(Fiber.currentFiber().getName()+" "+requrl
			 * );
			 */

			/*
			 * MainController.logger.info("Request: " +
			 * Fiber.currentFiber().getName() + " " + response
			 * .getStatusLine());
			 */

			/*
			 * System.out.println("Request: " + Fiber.currentFiber().getName() +
			 * " " +response.getFirstHeader("Jmeter"));
			 */
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
