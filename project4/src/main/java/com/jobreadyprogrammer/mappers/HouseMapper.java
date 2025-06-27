package com.jobreadyprogrammer.mappers;

import java.text.SimpleDateFormat;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Row;

import com.jobreadyprogrammer.pojos.House;
public class HouseMapper implements MapFunction<Row, House>{
	private static final long serialVersionUID = -2L;

	// Map the values from df rows to the actual properties of house obj
	@Override
	public House call(Row value) throws Exception {
		
		House h = new House();
		//csv columns
		h.setId(value.getAs("id"));
		h.setAddress(value.getAs("address"));
		h.setSqft(value.getAs("sqft"));
		h.setPrice(value.getAs("price"));
		//date format
		String vacancyDateString = value.getAs("vacantBy").toString();
		//reformat date format to yyyy-mm-dd
		if(vacancyDateString != null) {
			SimpleDateFormat parser = new SimpleDateFormat("yyyy-mm-dd");
			h.setVacantBy(parser.parse(vacancyDateString));
		}
		
		return h;
		
	}
	
}