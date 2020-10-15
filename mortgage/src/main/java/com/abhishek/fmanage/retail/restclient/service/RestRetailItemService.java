package com.abhishek.fmanage.retail.restclient.service;

import java.util.HashMap;
import java.util.Map;

import com.abhishek.fmanage.retail.RetailBillingType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.retail.dto.RetailItemStaffDTO;
import com.abhishek.fmanage.retail.dto.ShopDTO;

public class RestRetailItemService {

	private ShopDTO shopDto = null;
	private RetailBillingType retailBillingType;

	public RestRetailItemService(ShopDTO shopDto, RetailBillingType retailBillingType){

		this.shopDto = shopDto;
		this.retailBillingType = retailBillingType;
	}
	
	public RetailItemStaffDTO getItems(final long shopId){
        Map<String, Object> paramMap = new  HashMap<String, Object>();
        paramMap.put("shopId", shopId);
        RestServiceUtil restUtil = new RestServiceUtil();
        HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders(shopDto, retailBillingType));
        RestTemplate restTemplate = new RestTemplate(); 
        ResponseEntity<RetailItemStaffDTO> response = restTemplate.exchange(restUtil.getRestHostPortUrl() + "/item/{shopId}", HttpMethod.GET, request, RetailItemStaffDTO.class, paramMap);
        return response.getBody();
	}
	
	public void addItem(final long shopId, final String itemName, String container){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("shopId", shopId);
		paramMap.put("itemname", itemName);
		paramMap.put("container", container);
		
		RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entityrequest = new HttpEntity<String>(new RestServiceUtil().getHeaders(shopDto, retailBillingType));
        ResponseEntity<Long> response = restTemplate.exchange(
        		new RestServiceUtil().getRestHostPortUrl() + "/item/{shopId}/{itemname}/{container}",
        		HttpMethod.POST,
        		entityrequest,
        		Long.class,
        		paramMap);
	}
}
