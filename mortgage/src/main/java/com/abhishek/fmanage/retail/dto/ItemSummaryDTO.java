package com.abhishek.fmanage.retail.dto;

public class ItemSummaryDTO {
	
	private String itemName;
	private Long quantity;

	
	public ItemSummaryDTO(){}
	
	public ItemSummaryDTO(String itemName, long quantity){
		this.itemName = itemName;
		this.quantity = quantity;
	}
	
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
}
