package com.abhishek.fmanage.retail.restclient.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.retail.dto.ShopDTO;

public class RestLoginService {

	public RestLoginService(){}

	public ShopDTO retailLogin(final String userName, final String passWord){
        Map<String, Object> paramMap = new  HashMap<String, Object>();
        paramMap.put("username", userName);
        paramMap.put("password", passWord);
        return new RestTemplate().getForObject("http://localhost:8090/login/{username}/{password}", ShopDTO.class, paramMap);
	}
}
