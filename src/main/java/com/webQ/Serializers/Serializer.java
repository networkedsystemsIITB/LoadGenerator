package com.webQ.Serializers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.webQ.model.TestPlan;

public class Serializer {
	 public static void serializeObject(List<TestPlan> testPlans,String path){
		 
		  
		   try{
	 
			FileOutputStream fout = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fout);   
			
			oos.writeObject(testPlans);
			
			
			oos.close();
			
	 
		   }catch(Exception ex){
			   ex.printStackTrace();
		   }
	   }
	 public static List<TestPlan> deserialzeAddress(String path){
		 
		   TestPlan testplan;
		   List<TestPlan> testPlans=new ArrayList<TestPlan>();
	 
		   try{
	 
			   FileInputStream fin = new FileInputStream(path);
			   ObjectInputStream ois = new ObjectInputStream(fin);
			   testPlans =  (List<TestPlan>) ois.readObject();
			   ois.close();
	 
			   return testPlans;
	 
		   }catch(Exception ex){
			   ex.printStackTrace();
			   return null;
		   } 
	   } 

}
