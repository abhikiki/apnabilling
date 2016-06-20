package com.retail;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class MyPoc {

	public static void main(String []args){
	    
	    	Date endDate = new Date();
	    	LocalDateTime ldt = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
	    	ldt = ldt.minusMonths(1);
	    	System.out.println(Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()));
	    
	}
}
