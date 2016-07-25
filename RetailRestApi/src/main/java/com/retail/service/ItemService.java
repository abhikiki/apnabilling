package com.retail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.retail.dao.RetailItemStaffDAO;
import com.retail.dto.RetailItemStaffDTO;

@Component
public class ItemService {

	@Autowired
	private RetailItemStaffDAO retailItemStaffDao;
	
	public RetailItemStaffDTO getItems(long shopId){
		RetailItemStaffDTO retailItemStaffDto = new RetailItemStaffDTO();
		retailItemStaffDto.setGoldItemsList(retailItemStaffDao.getGoldItems(shopId));
		retailItemStaffDto.setSilverItemsList(retailItemStaffDao.getSilverItems(shopId));
		retailItemStaffDto.setDiamondItemsList(retailItemStaffDao.getDiamondItems(shopId));
		retailItemStaffDto.setGeneralItemsList(retailItemStaffDao.getGeneralItems(shopId));
		retailItemStaffDto.setStaffList(retailItemStaffDao.getStaffList(shopId));
		return retailItemStaffDto;
	}
	
	public long addItem(long shopId, String itemName, String container){
		return retailItemStaffDao.addItem(shopId, itemName, container);
	}
}
