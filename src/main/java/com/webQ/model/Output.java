package com.webQ.model;

import co.paralleluniverse.fibers.SuspendExecution;

public class Output {

	private String time;
	private String request;
	private String inputload;
	private String avgThroughput;
	private String curThroughput;
	private String responsetime;
	private String errorrate;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getInputload() {
		return inputload;
	}

	public void setInputload(String inputload) {
		this.inputload = inputload;
	}

	public String getResponsetime() {
		return responsetime;
	}

	public void setResponsetime(String responsetime) {
		this.responsetime = responsetime;
	}

	public String getErrorrate() {
		return errorrate;
	}

	public void setErrorrate(String errorrate) {
		this.errorrate = errorrate;
	}

	public String getAvgThroughput() {
		return avgThroughput;
	}

	public void setAvgThroughput(String avgThroughput) {
		this.avgThroughput = avgThroughput;
	}

	public String getCurThroughput() {
		return curThroughput;
	}

	public void setCurThroughput(String curThroughput) {
		this.curThroughput = curThroughput;
	}

}
