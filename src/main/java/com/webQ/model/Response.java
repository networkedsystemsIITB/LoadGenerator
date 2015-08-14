package com.webQ.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;

public class Response {

	private HttpResponse response;
	private Map<String, List<String>> regexmap;

	public Response() {
		// TODO Auto-generated constructor stub
		
		this.regexmap = new HashMap<String, List<String>>();
	}

	public HttpResponse getResponse() {
		return response;
	}

	public void setResponse(HttpResponse response) {
		this.response = response;
	}

	public Map<String, List<String>> getRegexmap() {
		return regexmap;
	}

	public void setRegexmap(Map<String, List<String>> regexmap) {
		this.regexmap = regexmap;
	}
	public void displayResponse(){
		System.out.println("Response: "+response);
		for (Map.Entry entry : regexmap.entrySet()) {
			System.out.println(entry.getValue());
		}
	}

}
