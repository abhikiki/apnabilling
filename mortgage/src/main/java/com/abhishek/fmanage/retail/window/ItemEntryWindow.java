package com.abhishek.fmanage.retail.window;

import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.abhishek.fmanage.cache.ItemCache;
import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.restclient.service.RestRetailItemService;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ItemEntryWindow extends Window{
	TextField newItemTxtField = new TextField("Enter New Item");
	ListSelect select = new ListSelect("Existing Items");
	private ThemeResource resource = new ThemeResource("img/addButtonSmall.jpg");
	
	public ItemEntryWindow(String itemName){
		setCaption("Manage " + itemName + " Items");
		//addStyleName("mydasboardsummary");
		setModal(true);
		center();
		setResizable(false);
		setWidth("30%");
		setHeight("50%");
		setClosable(true);
		addStyleName("no-vertical-drag-hints");
		addStyleName("no-horizontal-drag-hints");
		HorizontalLayout windowHeaderHL = new HorizontalLayout();
		windowHeaderHL.setWidth("100%");
		windowHeaderHL.setHeight("5%");
		windowHeaderHL.addStyleName("smsBackLayout");
		VerticalLayout VL = new VerticalLayout();
		VL.setSizeUndefined();
		VL.setSpacing(true);
		VL.addComponent(windowHeaderHL);
		VL.addComponent(getExistingItem(itemName));
		setContent(VL);
	}

	private Image getAddImageBtn(String itemName) {
    	Image image = new Image("", resource);
        image.setHeight("20px");
        image.setWidth("20px");
        image.setDescription("Add Item");
        image.addClickListener((event -> openAddAmountWindow(newItemTxtField.getValue(), itemName)));
       	return image;
	}
	
	
	private void openAddAmountWindow(String itemName, String itemType) {
		if(StringUtils.isEmpty(itemName)){
			Notification.show("Please enter item name");
			return;
		}
		if(!ItemCache.getInstance().getItemMap().get(itemType).stream().map(item -> item.getItemName()).collect(Collectors.toSet()).contains(itemName.toUpperCase())){
			ItemCache.getInstance().addItem(itemName, itemType);
		}else{
			Notification.show("Item already exists");
			return;
		}
		ShopDTO shopDto =  (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);
		new RestRetailItemService(shopDto).addItem(shopDto.getShopId(), itemName, itemType);
		
			select.addItem(itemName.toUpperCase());
			Notification.show("Item Successfully added");
		
		
	}

	private Component getExistingItem(String itemName) {
		// Create the selection component
		select.setImmediate(true);
		select.setMultiSelect(true);
		// Add some items
		select.addItems(ItemCache.getInstance().getItemMap().get(itemName).stream().map(item -> item.getItemName()).collect(Collectors.toList()));
		

		// Show 5 items and a scrollbar if there are more
		select.setRows(15);
		HorizontalLayout mainHL = new HorizontalLayout();
		mainHL.setSpacing(true);
		mainHL.addComponent(select);
		mainHL.addComponent(newItemTxtField);
		Image img = getAddImageBtn(itemName);
		mainHL.addComponent(img);
		mainHL.setComponentAlignment(newItemTxtField, Alignment.MIDDLE_LEFT);
		mainHL.setComponentAlignment(img, Alignment.MIDDLE_CENTER);
		return mainHL;
	}

	private String getItemTypeName(String itemType) {
		String itemTypeName = "GOLD";
		if(itemType.equalsIgnoreCase("GOLD")){
			itemTypeName = "GOLD";
		}
		if(itemType.equalsIgnoreCase("SILVER")){
			itemTypeName = "SILVER";
		}
		if(itemType.equalsIgnoreCase("DIAMOND")){
			itemTypeName = "DIAMOND";
		}
		if(itemType.equalsIgnoreCase("GENERAL")){
			itemTypeName = "GENERAL";
		}
		return itemTypeName;
	}
}
