package com.abhishek.fmanage.retail.restclient.service;

import java.util.Date;

import com.abhishek.fmanage.retail.RetailBillingType;
import com.abhishek.retail.dto.SummaryDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.retail.dto.DateRangeCriteriaDTO;
import com.abhishek.fmanage.retail.dto.ShopDTO;

public class RestRetailSummaryService<D> {

	private ShopDTO shopDto = null;
	private RetailBillingType retailBillingType;
	public RestRetailSummaryService(ShopDTO shopDto, RetailBillingType retailBillingType){

		this.shopDto = shopDto;
		this.retailBillingType = retailBillingType;
	}
	
	public SummaryDTO getRetailSummary(Date startDate, Date endDate){
		final String url = new RestServiceUtil().getRestHostPortUrl() + "/summary/retailsummary";
    	DateRangeCriteriaDTO dateRangeCriteriaDto = new DateRangeCriteriaDTO();
    	dateRangeCriteriaDto.setStartDate(startDate);
    	dateRangeCriteriaDto.setEndDate(endDate);
	    RestTemplate restTemplate = new RestTemplate();
        HttpEntity<DateRangeCriteriaDTO> entityrequest = new HttpEntity<DateRangeCriteriaDTO>(dateRangeCriteriaDto, new RestServiceUtil().getHeaders(shopDto, retailBillingType));
        ResponseEntity<SummaryDTO> response = restTemplate.exchange(url, HttpMethod.POST, entityrequest, SummaryDTO.class);
		return response.getBody();
	}
}
