package com.jobreadyprogrammer.spark;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.ReduceFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;

public class ArrayToDataset {

	public void start() {
		
		SparkSession spark = new SparkSession.Builder()
				.appName("Array To Dataset<String>")
				.master("local")
				.getOrCreate();
		
		String [] stringList = new String[] {"Banana", "Car", "Glass", "Banana", "Computer", "Car"};
		
		//convert array to list of strings
		List<String> data = Arrays.asList(stringList);
		
		//convert from my list to dataset
		Dataset<String> ds =  spark.createDataset(data, Encoders.STRING());
		//ds = ds.map((MapFunction<String, String>, Encoders.STRING()) // here we need static serializable map class
		ds = ds.map((MapFunction<String, String>) row -> "word: " + row, Encoders.STRING());
		ds.show(10);
		 //concate all data -> single string representation
		String stringValue = ds.reduce(new StringReducer());
		
		System.out.println(stringValue);
		
	}
	
	//Map/reduce -> Serializable class
	static class StringReducer implements ReduceFunction<String>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public String call(String v1, String v2) throws Exception {
			return v1 + v2;
		}
		
	}

}
