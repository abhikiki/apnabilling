package com.abhishek.fmanage.retail.restclient.service;

import java.util.Date;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.retail.dto.DateRangeCriteriaDTO;
import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.dto.SummaryDTO;

public class RestRetailSummaryService<D> {

	private ShopDTO shopDto = null;
	public RestRetailSummaryService(ShopDTO shopDto){
		this.shopDto = shopDto;
	}
	
	public SummaryDTO getRetailSummary(Date startDate, Date endDate){
		final String url = new RestServiceUtil().getRestHostPortUrl() + "/summary/retailsummary";
    	DateRangeCriteriaDTO dateRangeCriteriaDto = new DateRangeCriteriaDTO();
    	dateRangeCriteriaDto.setStartDate(startDate);
    	dateRangeCriteriaDto.setEndDate(endDate);
	    RestTemplate restTemplate = new RestTemplate();
        HttpEntity<DateRangeCriteriaDTO> entityrequest = new HttpEntity<DateRangeCriteriaDTO>(dateRangeCriteriaDto, new RestServiceUtil().getHeaders(shopDto));
        ResponseEntity<SummaryDTO> response = restTemplate.exchange(url, HttpMethod.POST, entityrequest, SummaryDTO.class);
		return response.getBody();
	}
}
