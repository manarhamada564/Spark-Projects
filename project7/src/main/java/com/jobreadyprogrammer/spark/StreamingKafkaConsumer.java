package com.jobreadyprogrammer.spark;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

public class StreamingKafkaConsumer {

	public static void main(String[] args) throws StreamingQueryException {
		
		SparkSession spark = SparkSession.builder()
		        .appName("StreamingKafkaConsumer")
		        .master("local")
		        .getOrCreate();

		// Kafka Consumer
		Dataset<Row> messagesDf = spark.readStream()
		  .format("kafka")
		  .option("kafka.bootstrap.servers", "localhost:9092")
		  .option("subscribe", "test")
		  .load()
		  .selectExpr("CAST(value AS STRING)");
		
		Dataset<String> words = messagesDf
				  .as(Encoders.STRING())
				  .flatMap((FlatMapFunction<String, String>) x -> Arrays.asList(x.split(" ")).iterator(), Encoders.STRING());

		Dataset<Row> wordCounts = words.groupBy("value").count();		
		
		StreamingQuery query;
		try {
			query = wordCounts.writeStream()
					.outputMode("complete")
					.format("console")
					.start();
			query.awaitTermination();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}

}
