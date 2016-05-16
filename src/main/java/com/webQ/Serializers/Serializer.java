package com.webQ.Serializers;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.webQ.model.DbTestPlan;
import com.webQ.model.Test;
import com.webQ.model.TestPlan;

public class Serializer {
	public static void serializeTestObject(Test test, String path)
			throws Exception {

		XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
				new FileOutputStream(path)));
		encoder.writeObject(test);
		encoder.close();

		/*
		 * try{
		 * 
		 * FileOutputStream fout = new FileOutputStream(path);
		 * ObjectOutputStream oos = new ObjectOutputStream(fout);
		 * 
		 * oos.writeObject(testPlans);
		 * 
		 * 
		 * oos.close();
		 * 
		 * 
		 * }catch(Exception ex){ ex.printStackTrace(); }
		 */
	}

	public static Test deserialzeTestObject(String path)
			throws FileNotFoundException {
		XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(
				new FileInputStream(path)));
		Test test = new Test();
		test = (Test) decoder.readObject();
		decoder.close();
		return test;
		/*
		 * TestPlan testplan; List<TestPlan> testPlans=new
		 * ArrayList<TestPlan>();
		 * 
		 * try{
		 * 
		 * FileInputStream fin = new FileInputStream(path); ObjectInputStream
		 * ois = new ObjectInputStream(fin); testPlans = (List<TestPlan>)
		 * ois.readObject(); ois.close();
		 * 
		 * return testPlans;
		 * 
		 * }catch(Exception ex){ ex.printStackTrace(); return null; }
		 */
	}

	public static void serializeTestPlanObject(List<TestPlan> testPlans,
			String path) throws Exception {

		XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
				new FileOutputStream(path)));
		encoder.writeObject(testPlans);
		encoder.close();

		/*
		 * try{
		 * 
		 * FileOutputStream fout = new FileOutputStream(path);
		 * ObjectOutputStream oos = new ObjectOutputStream(fout);
		 * 
		 * oos.writeObject(testPlans);
		 * 
		 * 
		 * oos.close();
		 * 
		 * 
		 * }catch(Exception ex){ ex.printStackTrace(); }
		 */
	}

	public static List<TestPlan> deserialzeTestPlanObject(String path)
			throws FileNotFoundException {
		XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(
				new FileInputStream(path)));
		List<TestPlan> testPlans = new ArrayList<TestPlan>();
		testPlans = (List<TestPlan>) decoder.readObject();
		decoder.close();
		return testPlans;
		/*
		 * TestPlan testplan; List<TestPlan> testPlans=new
		 * ArrayList<TestPlan>();
		 * 
		 * try{
		 * 
		 * FileInputStream fin = new FileInputStream(path); ObjectInputStream
		 * ois = new ObjectInputStream(fin); testPlans = (List<TestPlan>)
		 * ois.readObject(); ois.close();
		 * 
		 * return testPlans;
		 * 
		 * }catch(Exception ex){ ex.printStackTrace(); return null; }
		 */
	}

	public static void serializeDbTestPlanObject(List<DbTestPlan> testPlans,
			String path) throws Exception {

		XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
				new FileOutputStream(path)));
		encoder.writeObject(testPlans);
		encoder.close();
		
	}
	
	public static List<DbTestPlan> deserialzeDbTestPlanObject(String path)
			throws FileNotFoundException {
		XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(
				new FileInputStream(path)));
		List<DbTestPlan> testPlans = new ArrayList<DbTestPlan>();
		testPlans =(List<DbTestPlan>) decoder.readObject();
		decoder.close();
		return testPlans;
	}
	
}
