package com.webQ.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.paralleluniverse.fibers.SuspendExecution;

import com.webQ.interfaces.Feature;

public class RegexExtractor implements Feature,Serializable {
	private String regex;
	private String refName;

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	@Override
	public void execute(Response resp) throws InterruptedException, SuspendExecution{
		// TODO Auto-generated method stub
		Pattern p = Pattern.compile(this.regex);
		Matcher m = p.matcher(resp.getResponse().toString());
		Map<String, List<String>> regexmap=resp.getRegexmap();
		
		
	
		
		List<String> allMatches = new ArrayList<String>();
		if (m.find()) {

			for (int i = 1; i <= m.groupCount(); i++) {

				allMatches.add(m.group(i));

			}

		}
		/*System.out.println("List value "+allMatches);*/
		regexmap.put(this.refName, allMatches);

		resp.setRegexmap(regexmap);
	}

}
