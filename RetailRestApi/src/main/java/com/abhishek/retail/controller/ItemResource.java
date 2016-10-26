package com.abhishek.retail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.abhishek.retail.dto.RetailItemStaffDTO;
import com.abhishek.retail.service.ItemService;

@RestController
@RequestMapping("/item")
public class ItemResource {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping(value = "/{shopId}", method = RequestMethod.GET)
	public RetailItemStaffDTO findBill(@PathVariable long shopId) {
		return itemService.getItems(shopId);
	}
	
	@RequestMapping(value = "/{shopId}/{itemname}/{container}", method = RequestMethod.POST)
	public long addItem(@PathVariable long shopId, @PathVariable String itemName, @PathVariable String container) {
		return itemService.addItem(shopId, itemName, container);
	}
}
