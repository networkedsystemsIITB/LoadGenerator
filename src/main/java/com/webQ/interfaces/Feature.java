package com.webQ.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.webQ.model.Response;

import co.paralleluniverse.fibers.SuspendExecution;

public interface Feature {

	public void execute(Response response) throws InterruptedException,
			SuspendExecution;
}
