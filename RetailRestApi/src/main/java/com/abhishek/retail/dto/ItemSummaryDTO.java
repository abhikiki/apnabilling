package com.abhishek.retail.dto;

public class ItemSummaryDTO {
	
	private String itemName;
	private Long quantity;
	private String piecePair;

	
	public ItemSummaryDTO(){}
	
	public ItemSummaryDTO(String itemName, long quantity, String piecePair){
		this.itemName = itemName;
		this.quantity = quantity;
		this.piecePair = piecePair;
	}
	
	
	public String getPiecePair() {
		return piecePair;
	}

	public void setPiecePair(String piecePair) {
		this.piecePair = piecePair;
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
