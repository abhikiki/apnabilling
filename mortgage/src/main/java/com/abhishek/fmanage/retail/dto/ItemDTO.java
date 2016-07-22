package com.abhishek.fmanage.retail.dto;

public class ItemDTO {

	private String itemName;
	private String owner;
	
	public ItemDTO(){}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
