package com.retail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.retail.dao.SmsDAO;
import com.retail.dto.SmsSettingDTO;

@Component
public class SmsService {

	@Autowired
	private SmsDAO smsDao;
	
	public List<String> getCustomerContact(int shopId){
		return smsDao.getCustomerContacts(shopId);
	}
	
	public List<SmsSettingDTO> getSmsSetting(int shopId){
		return smsDao.getSmsSetting(shopId);
	}
	
	public int saveSmsSetting(int shopId, SmsSettingDTO smsSettingDto){
		 return smsDao.saveSmsSetting(shopId, smsSettingDto);
	}
	
	public int  updateSmsSetting(int shopId, SmsSettingDTO smsSettingDto){
		 return smsDao.updateSmsSetting(shopId, smsSettingDto);
	}
}
