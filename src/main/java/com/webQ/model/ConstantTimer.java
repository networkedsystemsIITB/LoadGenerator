package com.webQ.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;

import com.webQ.controller.MainController;
import com.webQ.interfaces.Feature;

public class ConstantTimer implements Feature,Serializable {

	private String time;

	public String getTime() throws SuspendExecution{
		return time;
	}

	public void setTime(String time) throws SuspendExecution {
		this.time = time;
	}

	@Override
	public void execute(Response response) throws InterruptedException, SuspendExecution{
		// TODO Auto-generated method stub
		try {
			String curtime=this.getTime();
			String localregex="\\$([A-Za-z0-9]+)_([0-9]+)";
			String globalregex="\\#([A-Za-z0-9]+)_([0-9]+)";

			Pattern p = Pattern.compile(localregex);
			Matcher m = p.matcher(this.getTime().toString());
			Pattern q = Pattern.compile(globalregex);
			Matcher n = q.matcher(this.getTime().toString());
			Map<String, List<String>> regexmap = response.getRegexmap();
			
			if(m.find()){
				//System.out.println("inside timer m");
				String refname=m.group(1);
				int index=Integer.parseInt(m.group(2));
				
				for (Entry<String, List<String>> entry : regexmap.entrySet()) {
					
					if(entry.getKey().equals(refname)){
						curtime=this.getTime().replaceFirst(localregex, entry.getValue().get(index-1));
						
					    Expression e = new ExpressionBuilder(curtime)
				        .build();
					    double result = e.evaluate();
					    
					    curtime=String.valueOf(result);
					  
						break;
					}
				}
				
			}
			else if(n.find()){
				//System.out.println("inside timer n");
				String refname=n.group(1);
				int index=Integer.parseInt(n.group(2));
			//	System.out.println(MainController.globalregexmap);
				for (Entry<String, List<String>> entry : MainController.globalregexmap.entrySet()) {
					
					if(entry.getKey().equals(refname)){
						curtime=this.getTime().replaceFirst(globalregex, entry.getValue().get(index-1));
						
					    Expression e = new ExpressionBuilder(curtime)
				        .build();
					    double result = e.evaluate();
					    
					    curtime=String.valueOf(result);
					  
						break;
					}
				}
				
			}
			
			//System.out.println("Time: "+curtime);
			Fiber.sleep((long) (Float.parseFloat(curtime)));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
