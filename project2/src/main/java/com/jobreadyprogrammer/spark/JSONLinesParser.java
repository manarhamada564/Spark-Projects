package com.jobreadyprogrammer.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class JSONLinesParser {
	
	 
	 
	  public void parseJsonLines() {
	    SparkSession spark = SparkSession.builder()
	        .appName("JSON Lines to Dataframe")
	        .master("local")
	        .getOrCreate();

//	    Dataset<Row> df = spark.read().format("json")
//		        .load("src/main/resources/simple.json"); //doc every row in low without separator
	 //here nested fields fields not printed in the table 
	    Dataset<Row> df2 = spark.read().format("json")
	    	.option("multiline", true) //docs that span multi lines
	        .load("src/main/resources/multiline.json");
	 
	    df2.show(5, 150);
	    df2.printSchema();
	  }
	
	
}



