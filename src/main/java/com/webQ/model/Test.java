package com.webQ.model;

import java.util.ArrayList;
import java.util.List;

public class Test {


	private Integer maxreqRate;
	private Integer maxduration;

	private Integer epoch;


	private List<TestPlan> testPlans = new ArrayList<TestPlan>();
	public Integer getMaxreqRate() {
		return maxreqRate;
	}

	public void setMaxreqRate(Integer maxreqRate) {
		this.maxreqRate = maxreqRate;
	}

	public Integer getMaxduration() {
		return maxduration;
	}

	public void setMaxduration(Integer maxduration) {
		this.maxduration = maxduration;
	}

	public Integer getEpoch() {
		return epoch;
	}

	public void setEpoch(Integer epoch) {
		this.epoch = epoch;
	}


	
	public List<TestPlan> getTestPlans() {
		return testPlans;
	}

	public void setTestPlans(List<TestPlan> testPlans) {
		this.testPlans = testPlans;
	}

}
