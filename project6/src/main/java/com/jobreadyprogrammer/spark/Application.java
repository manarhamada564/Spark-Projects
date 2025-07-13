package com.jobreadyprogrammer.spark;

import java.beans.Encoder;
import java.io.File;
import java.util.Arrays;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.ml.feature.StopWordsRemover;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.*;


public class Application {

	public static void main(String[] args) {
		
			
		SparkSession spark = SparkSession.builder()
		        .appName("Learning Spark SQL Dataframe API")
		        .master("local")
		        .getOrCreate();
		
//		 String redditFile = "s3n://fileredditcommentstask2sparkwithjava/Reddit_2011-large";
		//check file is exist
		 File file = new File("E:/Data-Engineering/Spark/udemy/Reddit_2007-small.json");
		 System.out.println("File exists: " + file.exists());
		 //read file
//		 String redditFile = "E:/Data-Engineering/Spark/udemy/Reddit_2007-small.json"; // <- change your file location		 
		 
		 Dataset<Row> redditDf = spark.read().format("json")
		        .option("inferSchema", "true")
		        .option("header", true)
		        .load(redditFile);
		    
		    redditDf = redditDf.select("body");
		    
		    Dataset<String> wordsDs = redditDf.flatMap((FlatMapFunction<Row, String>)
		    		r -> Arrays.asList(r.toString().replace("\n", "").replace("\r", "").trim().toLowerCase()
		    				.split(" ")).iterator(),
		    		Encoders.STRING());
		    redditDf.show();
		    
		    Dataset<Row> wordsDf = wordsDs.toDF();
		    Dataset<Row> boringWordsDf = spark.createDataset(Arrays.asList(WordUtils.stopWords), Encoders.STRING()).toDF();
		    //remove stopwords
			//wordsDf = wordsDf.except(boringWordsDf); //remove duplicates and the count 'll be wrong solve it using join

		    wordsDf = wordsDf.join(boringWordsDf, wordsDf.col("value").equalTo(boringWordsDf.col("value")), "leftanti");
		    wordsDf = wordsDf.groupBy("value").count();
		    //top 5 count words
		    wordsDf.orderBy(desc("count")).show(5);
		    
	}
	
}
