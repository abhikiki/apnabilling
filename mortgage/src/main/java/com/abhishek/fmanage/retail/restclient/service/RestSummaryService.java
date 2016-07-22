package com.abhishek.fmanage.retail.restclient.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.retail.dto.DateRangeCriteriaDTO;
import com.abhishek.fmanage.retail.dto.SummaryDTO;

public class RestSummaryService {

	public RestSummaryService(){}
	
	public SummaryDTO getRetailSummary(Date startDate, Date endDate){
		SummaryDTO summary = new SummaryDTO();
		final String uri = "http://localhost:8090/summary/retailsummary";
    	DateRangeCriteriaDTO dd = new DateRangeCriteriaDTO();
    	dd.setStartDate(startDate);
    	dd.setEndDate(endDate);
    	
        URI url = null;
		try {
			url = new URI(uri);
			summary =  new RestTemplate().postForObject(url, dd, SummaryDTO.class);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return summary;
        
	}
}
