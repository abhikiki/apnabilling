package com.abhishek.fmanage.retail.dto;

import java.util.Date;

public class DateRangeCriteriaDTO {

	public DateRangeCriteriaDTO(){}
	
	private Date startDate;
	private Date endDate;
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}
