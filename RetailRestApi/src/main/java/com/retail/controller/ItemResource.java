package com.retail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.retail.dto.SellingItemsDTO;
import com.retail.service.ItemService;

@RestController
@RequestMapping("/item")
public class ItemResource {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping(value = "/{shopId}", method = RequestMethod.GET)
	public SellingItemsDTO findBill(@PathVariable long shopId) {
		return itemService.getItems(shopId);
	}
}
