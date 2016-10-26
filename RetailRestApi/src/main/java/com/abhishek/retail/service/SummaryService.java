package com.abhishek.retail.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.abhishek.retail.dao.SummaryDAO;
import com.abhishek.retail.dto.SummaryDTO;

@Component
public class SummaryService {

	@Autowired
	private SummaryDAO summaryDao;
	
	public SummaryDTO getRetailSummary(Date startDate, Date endDate){
		return summaryDao.getSummary(startDate, endDate);
	}
}
