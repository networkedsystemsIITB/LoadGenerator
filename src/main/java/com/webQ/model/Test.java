package com.webQ.model;

import java.util.ArrayList;
import java.util.List;

import co.paralleluniverse.fibers.SuspendExecution;

public class Test {


	private Integer maxreqRate;
	private Integer maxduration;

	private Integer epoch;


	private List<TestPlan> testPlans = new ArrayList<TestPlan>();
	public Integer getMaxreqRate() throws SuspendExecution{
		return maxreqRate;
	}

	public void setMaxreqRate(Integer maxreqRate)throws SuspendExecution {
		this.maxreqRate = maxreqRate;
	}

	public Integer getMaxduration() throws SuspendExecution{
		return maxduration;
	}

	public void setMaxduration(Integer maxduration) throws SuspendExecution{
		this.maxduration = maxduration;
	}

	public Integer getEpoch()throws SuspendExecution {
		return epoch;
	}

	public void setEpoch(Integer epoch) throws SuspendExecution{
		this.epoch = epoch;
	}


	
	public List<TestPlan> getTestPlans() throws SuspendExecution {
		return testPlans;
	}

	public void setTestPlans(List<TestPlan> testPlans) throws SuspendExecution {
		this.testPlans = testPlans;
	}

}
