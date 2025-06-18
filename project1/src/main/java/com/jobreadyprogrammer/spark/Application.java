package com.jobreadyprogrammer.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.*;

import java.util.Properties;

public class Application {
	
	public static void main(String args[]) {
		//read data from csv and load it in db
		
		//create session to connect with master
		SparkSession spark = new SparkSession.Builder()
				.appName("csv to db") //session name
				.master("local") //where the master
				.getOrCreate();
		
		//get data save it in data structure dataset row  <dataframe>
		Dataset<Row> df = spark.read().format("csv")
			.option("header", true) //file have header or not
			.load("src/main/resources/name_and_comments.txt"); //from real hdfs, s3
		
		//df.show(3);
		//T1
		df = df.withColumn("fullName", concat(df.col("first_name"),lit(" "),df.col("last_name"))); //imutable structure
		// T2
		df = df.filter(df.col("comment").rlike("\\d+")).orderBy(df.col("fullName").asc()); //rows that have digit using regx
		//df.show();
		// Write to destination
		String dbConnectionUrl = "jdbc:postgresql://localhost/spark_data"; // database
		Properties prop = new Properties();
		prop.setProperty("driver", "org.postgresql.Driver");
		prop.setProperty("user", "postgres");
		prop.setProperty("password", "0000"); 
		df.write()
			.mode(SaveMode.Overwrite)
			.jdbc(dbConnectionUrl, "project1", prop);
		}
}