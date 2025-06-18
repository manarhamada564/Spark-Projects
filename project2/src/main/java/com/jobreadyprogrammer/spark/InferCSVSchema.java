package com.jobreadyprogrammer.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class InferCSVSchema {
	
	public void printSchema() {
		SparkSession spark = SparkSession.builder()
		        .appName("Complex CSV to Dataframe")
		        .master("local")
		        .getOrCreate();
		 
		    Dataset<Row> df = spark.read().format("csv") //
		        .option("header", "true") //there is a header
		        .option("multiline", true) 
		        .option("sep", ";") //separator
		        .option("quote", "^") //consider ^ as quotes
		        .option("dateFormat", "M/d/y") 
		        .option("inferSchema", true) //configure out what data types of columns should be
		        .load("src/main/resources/amazonProducts.txt");
		 
		    System.out.println("Excerpt of the dataframe content:");
		    df.show(7);
		    //df.show(7, 90); // truncate after 90 chars
		    System.out.println("Dataframe's schema:");
		    df.printSchema();
	}
	
}
