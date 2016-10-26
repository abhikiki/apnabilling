package com.abhishek.fmanage.retail.restclient.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.retail.dto.SmsSettingDTO;

public class RestRetailSmsService {

	@SuppressWarnings("unchecked")
	public List<String> getCustomerContact(int shopId){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("shopId", shopId);
        return new RestTemplate().getForObject("http://localhost:8090/sms/contacts/{shopId}", List.class, paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public SmsSettingDTO getSmsSetting(int shopId){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("shopId", shopId);
        return new RestTemplate().getForObject("http://localhost:8090/sms/setting/{shopId}", SmsSettingDTO.class, paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public Integer updateSmsSetting(int shopId, SmsSettingDTO smsSettingdto){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("shopId", shopId);
        return new RestTemplate().postForObject(
        		"http://localhost:8090/sms/setting/{shopId}",
        		smsSettingdto,
				Integer.class,
				paramMap);
	}
}
