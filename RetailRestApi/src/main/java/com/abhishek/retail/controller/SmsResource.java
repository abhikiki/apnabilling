package com.abhishek.retail.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.abhishek.retail.dto.SmsSettingDTO;
import com.abhishek.retail.service.SmsService;

@RestController
@RequestMapping("/sms")
public class SmsResource {

	@Autowired
	private SmsService smsService;
	
	@RolesAllowed({"ADMIN"})
	@RequestMapping(value = "/contacts/{shopId}", method = RequestMethod.GET)
	public List<String> getCustomerContacts(@PathVariable int shopId) {
		return smsService.getCustomerContact(shopId);
	}
	
	@RolesAllowed({"STAFF", "ADMIN"})
	@RequestMapping(value = "/setting/{shopId}", method = RequestMethod.GET)
	public SmsSettingDTO getSmsSetting(@PathVariable int shopId) {
		SmsSettingDTO smsSetting = new SmsSettingDTO();
		List<SmsSettingDTO> smsSettingList = smsService.getSmsSetting(shopId);
		if(!smsSettingList.isEmpty()){
			smsSetting = smsSettingList.get(0);
		}
		return smsSetting;	
	}
	
	@RolesAllowed({"ADMIN"})
	@RequestMapping(value = "/setting/{shopId}", method = RequestMethod.POST)
	public int updateSmsSetting(@PathVariable int shopId, @RequestBody SmsSettingDTO smsSettingDto) {
		List<SmsSettingDTO> smsSettingList = smsService.getSmsSetting(shopId);
		if(!smsSettingList.isEmpty()){
			return smsService.updateSmsSetting(shopId, smsSettingDto);
		}else{
			return smsService.saveSmsSetting(shopId, smsSettingDto);
		}
	}
}
