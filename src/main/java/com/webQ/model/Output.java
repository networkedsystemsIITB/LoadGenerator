package com.webQ.model;

import co.paralleluniverse.fibers.SuspendExecution;

public class Output {
	private int totalreqs;

	public int getTotalreqs() throws SuspendExecution {
		return totalreqs;
	}

	public void setTotalreqs(int totalreqs) throws SuspendExecution{
		this.totalreqs = totalreqs;
	}

}
