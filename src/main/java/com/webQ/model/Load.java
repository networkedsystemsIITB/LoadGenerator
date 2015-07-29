package com.webQ.model;

public class Load {
	private Integer reqRate;
	private Integer duration;
	private String httpUrl;

	public Integer getReqRate() {
		return reqRate;
	}

	public void setReqRate(Integer reqRate) {
		this.reqRate = reqRate;
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

}
