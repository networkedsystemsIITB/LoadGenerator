package com.webQ.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;

import co.paralleluniverse.fibers.SuspendExecution;

public class Response {

	private HttpResponse response;
	private Map<String, List<String>> regexmap;

	public Response() throws SuspendExecution{
		// TODO Auto-generated constructor stub
		
		this.regexmap = new HashMap<String, List<String>>();
	}

	public HttpResponse getResponse() throws SuspendExecution{
		return response;
	}

	public void setResponse(HttpResponse response)throws SuspendExecution {
		this.response = response;
	}

	public Map<String, List<String>> getRegexmap() throws SuspendExecution{
		return regexmap;
	}

	public void setRegexmap(Map<String, List<String>> regexmap) throws SuspendExecution{
		this.regexmap = regexmap;
	}
	public void displayResponse()throws SuspendExecution{
		System.out.println("Response: "+response);
		for (Map.Entry entry : regexmap.entrySet()) {
			System.out.println(entry.getValue());
		}
	}

}
