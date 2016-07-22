package com.abhishek.fmanage.retail.restclient.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.retail.dto.SellingItemsDTO;

public class RestItemService {

	public SellingItemsDTO getItems(final long shopId){
        Map<String, Object> paramMap = new  HashMap<String, Object>();
        paramMap.put("shopId", shopId);
        return new RestTemplate().getForObject("http://localhost:8090/item/{shopId}", SellingItemsDTO.class, paramMap);
	}
}
