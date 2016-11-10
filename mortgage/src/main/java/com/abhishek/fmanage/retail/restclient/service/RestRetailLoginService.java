package com.abhishek.fmanage.retail.restclient.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.retail.dto.ShopDTO;

public class RestRetailLoginService {

	public RestRetailLoginService(){}

	public ShopDTO retailLogin(final String userName, final String passWord){
        Map<String, Object> paramMap = new  HashMap<String, Object>();
        paramMap.put("username", userName);
        paramMap.put("password", passWord);
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        RestTemplate restTemplate = new RestTemplate(); 
        ResponseEntity<ShopDTO> response = restTemplate.exchange("http://localhost:8090/login", HttpMethod.GET, request, ShopDTO.class);
        return response.getBody();
        //return new RestTemplate().getForObject("http://localhost:8090/login/{username}/{password}", ShopDTO.class, paramMap);
	}
	
	private static HttpHeaders getHeaders(){
        String plainCredentials="staff:staff";
        String base64Credentials = new String(Base64.encode(plainCredentials.getBytes()));
         
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
