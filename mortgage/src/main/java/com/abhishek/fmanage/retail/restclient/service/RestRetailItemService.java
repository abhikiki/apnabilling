package com.abhishek.fmanage.retail.restclient.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.retail.dto.RetailItemStaffDTO;

public class RestRetailItemService {

	public RetailItemStaffDTO getItems(final long shopId){
        Map<String, Object> paramMap = new  HashMap<String, Object>();
        paramMap.put("shopId", shopId);
        return new RestTemplate().getForObject("http://localhost:8090/item/{shopId}", RetailItemStaffDTO.class, paramMap);
	}
	
	public long addItem(final long shopId, final String itemName, String container){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("shopId", shopId);
		paramMap.put("itemname", itemName);
		paramMap.put("container", container);
		return new RestTemplate()
				.postForObject(
						"http://localhost:8090/item/{shopId}/{itemname}/{container}",
						null,
						Long.class, paramMap);
	}
}
