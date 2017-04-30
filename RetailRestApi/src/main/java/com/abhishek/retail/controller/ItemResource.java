package com.abhishek.retail.controller;

import javax.annotation.security.RolesAllowed;

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
	
	@RolesAllowed({"STAFF", "ADMIN", "ADMIN_EXCLUDING_MORTGAGE"})
	@RequestMapping(value = "/{shopId}", method = RequestMethod.GET)
	public RetailItemStaffDTO getItemsDetail(@PathVariable long shopId) {
		return itemService.getItems(shopId);
	}
	
	@RolesAllowed({"ADMIN","ADMIN_EXCLUDING_MORTGAGE"})
	@RequestMapping(value = "/{shopId}/{itemName}/{container}", method = RequestMethod.POST)
	public void addItem(@PathVariable long shopId, @PathVariable String itemName, @PathVariable String container) {
		itemService.addItem(shopId, itemName, container);
	}
}
