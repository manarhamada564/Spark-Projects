package com.jobreadyprogrammer.spark;

import java.util.Arrays;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;


public class StreamingSocketApplication {

	public static void main(String[] args) throws StreamingQueryException {
		System.setProperty("hadoop.home.dir", "C:\\hadoop");
			
		// First start a socket connection at 9999 using this: ncat -lk 9999
		SparkSession spark = SparkSession.builder()
		        .appName("StreamingSocketWordCount")
		        .master("local")
		        .getOrCreate();
		
		System.out.println("Spark version: " + spark.version());
		// Create DataFrame representing the stream of input lines from connection to localhost:9999
		
		Dataset<Row> lines = spark
		  .readStream()
		  .format("socket")
		  .option("host", "localhost")
		  .option("port", 9999)
		  .load();
		
		Dataset<String> words = lines
				  .as(Encoders.STRING())
				  .flatMap((FlatMapFunction<String, String>) x -> Arrays.asList(x.split(" ")).iterator(), Encoders.STRING());
		
		Dataset<Row> wordCounts = words.groupBy("value").count();
		System.out.print("-------query-----------");
		StreamingQuery query = wordCounts.writeStream()
				.outputMode("complete")
				.format("console")
				.start();
		
		query.awaitTermination();
		query.stop();
	}

	
	
}
