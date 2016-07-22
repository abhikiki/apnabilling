package com.retail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.retail.dao.SellingItemsDAO;
import com.retail.dto.SellingItemsDTO;

@Component
public class ItemService {

	@Autowired
	private SellingItemsDAO sellingItemDao;
	
	public SellingItemsDTO getItems(long shopId){
		SellingItemsDTO sellingItemDto = new SellingItemsDTO();
		sellingItemDto.setGoldItemsList(sellingItemDao.getGoldItems(shopId));
		sellingItemDto.setSilverItemsList(sellingItemDao.getSilverItems(shopId));
		sellingItemDto.setDiamondItemsList(sellingItemDao.getDiamondItems(shopId));
		sellingItemDto.setGeneralItemsList(sellingItemDao.getGeneralItems(shopId));
		return sellingItemDto;
	}
}
