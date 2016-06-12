/**
 * 
 */
package com.retail.dto;

/**
 * @author GUPTAA6
 *
 */
public class GoldTransactionItemDTO {

	private String goldItemName;
	private String goldType;
	private int quantity;
	private String piecePair;
	private Double weight;
	private Double makingCharge;
	private String makingChargeType;
	private Double goldRate;
	private Double goldItemPrice;
	
	public GoldTransactionItemDTO() {}

	public GoldTransactionItemDTO(
		String goldItemName,
		String goldType,
		int quantity,
		String piecePair,
		Double weight,
		Double makingCharge,
		String makingChargeType,
		Double goldRate,
		Double goldItemPrice){
		this.goldItemName = goldItemName;
		this.goldType = goldType;
		this.quantity = quantity;
		this.piecePair = piecePair;
		this.weight = weight;
		this.makingCharge = makingCharge;
		this.makingChargeType = makingChargeType;
		this.goldRate = goldRate;
		this.goldItemPrice = goldItemPrice;
	}
	
	
	public String getGoldItemName() {
		return goldItemName;
	}


	public void setGoldItemName(String goldItemName) {
		this.goldItemName = goldItemName;
	}


	public String getGoldType() {
		return goldType;
	}


	public void setGoldType(String goldType) {
		this.goldType = goldType;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public String getPiecePair() {
		return piecePair;
	}


	public void setPiecePair(String piecePair) {
		this.piecePair = piecePair;
	}


	public Double getWeight() {
		return weight;
	}


	public void setWeight(Double weight) {
		this.weight = weight;
	}


	public String getMakingChargeType() {
		return makingChargeType;
	}


	public void setMakingChargeType(String makingChargeType) {
		this.makingChargeType = makingChargeType;
	}


	public Double getMakingCharge() {
		return makingCharge;
	}


	public void setMakingCharge(Double makingCharge) {
		this.makingCharge = makingCharge;
	}


	public Double getGoldRate() {
		return goldRate;
	}


	public void setGoldRate(Double goldRate) {
		this.goldRate = goldRate;
	}


	public Double getGoldItemPrice() {
		return goldItemPrice;
	}


	public void setGoldItemPrice(Double goldItemPrice) {
		this.goldItemPrice = goldItemPrice;
	}
}
