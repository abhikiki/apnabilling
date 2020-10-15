package com.abhishek.fmanage.retail.restclient.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abhishek.fmanage.retail.RetailBillingType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.dto.SmsSettingDTO;

public class RestRetailSmsService {

	private ShopDTO shopDto;
	private RetailBillingType retailBillingType;
	
	public RestRetailSmsService(ShopDTO shopDto, RetailBillingType retailBillingType){
		this.shopDto = shopDto;
		this.retailBillingType = retailBillingType;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getCustomerContact(int shopId){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("shopId", shopId);
		
		HttpEntity<String> entityrequest = new HttpEntity<String>(new RestServiceUtil().getHeaders(shopDto, retailBillingType));
	    ResponseEntity<List> response = new RestTemplate().exchange(
	    		new RestServiceUtil().getRestHostPortUrl() + "/sms/contacts/{shopId}",
	        	HttpMethod.GET,
	        	entityrequest,
	        	List.class,
	        	paramMap);
		return response.getBody();
		
        //return new RestTemplate().getForObject(new RestServiceUtil().getRestHostPortUrl() + "/sms/contacts/{shopId}", List.class, paramMap);
	}
	
	public SmsSettingDTO getSmsSetting(int shopId){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("shopId", shopId);
		HttpEntity<String> entityrequest = new HttpEntity<String>(new RestServiceUtil().getHeaders(shopDto, retailBillingType));
		ResponseEntity<SmsSettingDTO> response = new RestTemplate().exchange(
	    		new RestServiceUtil().getRestHostPortUrl() + "/sms/setting/{shopId}",
	        	HttpMethod.GET,
	        	entityrequest,
	        	SmsSettingDTO.class,
	        	paramMap);
		return response.getBody();
	}
	
	public Integer updateSmsSetting(int shopId, SmsSettingDTO smsSettingdto){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("shopId", shopId);
		RestTemplate restTemplate = new RestTemplate();
        HttpEntity<SmsSettingDTO> entityrequest = new HttpEntity<SmsSettingDTO>(smsSettingdto, new RestServiceUtil().getHeaders(shopDto, retailBillingType));
        ResponseEntity<Integer> response = restTemplate.exchange(
        		new RestServiceUtil().getRestHostPortUrl() + "/sms/setting/{shopId}",
        		HttpMethod.POST,
        		entityrequest,
        		Integer.class,
        		paramMap);
		return response.getBody();
	}
}
