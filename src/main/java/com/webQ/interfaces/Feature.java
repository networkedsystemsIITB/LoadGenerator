package com.webQ.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.webQ.model.Output;
import com.webQ.model.Response;
import com.webQ.model.Test;

import co.paralleluniverse.fibers.SuspendExecution;

public interface Feature {

	public void execute(Response response,Test curtest) throws InterruptedException,
			SuspendExecution;
}
