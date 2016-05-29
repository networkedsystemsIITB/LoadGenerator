package com.webQ.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.paralleluniverse.fibers.SuspendExecution;

public class Test {

	
	private Integer maxreqRate;
	private Integer maxduration;
	private Integer epoch;
	private Boolean test = false;
	private String sessionId;
	private List<TestPlan> testPlans;
	private List<DbTestPlan> dbTestPlans;
	private List<WsTestPlan> wsTestPlans;
	private List<Output> outputlist;
	private Map<String, List<String>> globalregexmap;

	public List<Output> getOutputlist() {
		return outputlist;
	}

	public void setOutputlist(List<Output> outputlist) {
		this.outputlist = outputlist;
	}

	public Boolean getTest() {
		return test;
	}

	public void setTest(Boolean test) {
		this.test = test;
	}

	public Integer getMaxreqRate() throws SuspendExecution {
		return maxreqRate;
	}

	public void setMaxreqRate(Integer maxreqRate) throws SuspendExecution {
		this.maxreqRate = maxreqRate;
	}

	public Integer getMaxduration() throws SuspendExecution {
		return maxduration;
	}

	public void setMaxduration(Integer maxduration) throws SuspendExecution {
		this.maxduration = maxduration;
	}

	public Integer getEpoch() throws SuspendExecution {
		return epoch;
	}

	public void setEpoch(Integer epoch) throws SuspendExecution {
		this.epoch = epoch;
	}
	
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	public List<TestPlan> getTestPlans() throws SuspendExecution {
		return testPlans;
	}

	public void setTestPlans(List<TestPlan> testPlans) throws SuspendExecution {
		this.testPlans = testPlans;
	}

	public List<DbTestPlan> getDbTestPlans() {
		return dbTestPlans;
	}

	public void setDbTestPlans(List<DbTestPlan> dbTestPlans) {
		this.dbTestPlans = dbTestPlans;
	}

	public List<WsTestPlan> getWsTestPlans() {
		return wsTestPlans;
	}

	public void setWsTestPlans(List<WsTestPlan> wsTestPlans) {
		this.wsTestPlans = wsTestPlans;
	}

	public Map<String, List<String>> getGlobalregexmap() {
		return globalregexmap;
	}

	public void setGlobalregexmap(Map<String, List<String>> globalregexmap) {
		this.globalregexmap = globalregexmap;
	}

}
