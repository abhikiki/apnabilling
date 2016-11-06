package com.abhishek.fmanage.mortgage.dto;

public class MortgageItemDTO {

	private String itemName;
	private Double weight;
	private String piecePair="";
	private Double quantity = 1.0d;
	private Double diamondGoldWeight = 0.000d;
	private Double diamondDiamondWeight = 0.000d;
	
	public MortgageItemDTO(String itemName, Double weight, Double diamondGoldWeight, Double diamondDiamondWeight, String piecePair, Double quantity){
		this.itemName = itemName;
		this.weight = weight;
		this.diamondGoldWeight = diamondGoldWeight;
		this.diamondDiamondWeight = diamondDiamondWeight;
		this.piecePair = piecePair;
		this.quantity = quantity;
	}
	
	
	public Double getQuantity() {
		return quantity;
	}


	public void setQuantity(Double quantity) {
		this.quantity = quantity;
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
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getDiamondGoldWeight() {
		return diamondGoldWeight;
	}

	public void setDiamondGoldWeight(Double diamondGoldWeight) {
		this.diamondGoldWeight = diamondGoldWeight;
	}

	public Double getDiamondDiamondWeight() {
		return diamondDiamondWeight;
	}

	public void setDiamondDiamondWeight(Double diamondDiamondWeight) {
		this.diamondDiamondWeight = diamondDiamondWeight;
	}
}
