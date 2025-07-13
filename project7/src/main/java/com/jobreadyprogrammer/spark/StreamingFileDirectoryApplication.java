package com.jobreadyprogrammer.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.StructType;
import static org.apache.spark.sql.functions.*;
import java.util.concurrent.TimeoutException;

public class StreamingFileDirectoryApplication {
	
	public static void main(String[] args) throws StreamingQueryException  {
	
		System.setProperty("hadoop.home.dir", "C:\\hadoop");
		SparkSession spark = SparkSession.builder()
		        .appName("StreamingFileDirectoryWordCount")
		        .master("local")
		        .getOrCreate();
		
		StructType userSchema = new StructType().add("date", "string").add("value", "float");
		
		Dataset<Row> stockData = spark
		  .readStream()
		  .option("sep", ",")
		  .schema(userSchema)
		  .csv("E:/Data-Engineering/Spark/udemy/data/Streaming");
		
		
		Dataset<Row> resultDf = stockData.groupBy("date").agg(avg(stockData.col("value")));
		
		StreamingQuery query;
		try {
			query = resultDf.writeStream()
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
