package com.abhishek.fmanage.retail.restclient.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.retail.dto.ShopDTO;

public class RestRetailLoginService {

	private String userName;
	private String password;
	
	public RestRetailLoginService(String userName, String password){
		this.userName = userName;
		this.password = password;
	}

	public ShopDTO retailLogin(){
        Map<String, Object> paramMap = new  HashMap<String, Object>();
        paramMap.put("username", userName);
        paramMap.put("password", password);
        ShopDTO shopDto = new ShopDTO();
        shopDto.setUserId(userName);
        shopDto.setPassword(password);
        RestServiceUtil restUtil = new RestServiceUtil();
        HttpEntity<String> request = new HttpEntity<String>(restUtil.getHeaders(shopDto));
        RestTemplate restTemplate = new RestTemplate(); 
        ResponseEntity<ShopDTO> response = restTemplate.exchange(restUtil.getRestHostPortUrl() + "/login", HttpMethod.GET, request, ShopDTO.class);
        return response.getBody();
	}
}
