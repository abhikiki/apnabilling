/**
 * 
 */
package com.abhishek.retail.dto;

/**
 * @author GUPTAA6
 *
 */
public class SilverTransactionItemDTO {


	private String itemName;
	private int quantity;
	private String piecepair;
	private Double weight;
	private Double makingCharge;
	private String makingChargeType;
	private Double silverRate;
	private Double silverItemPrice;


	public SilverTransactionItemDTO() {}
	
	public SilverTransactionItemDTO(
			String itemName,
			int quantity,
			String piecepair,
			Double weight,
			Double makingCharge,
			String makingChargeType,
			Double silverRate,
			Double silverItemPrice ) {
		this.itemName = itemName;
		this.quantity = quantity;
		this.piecepair = piecepair;
		this.weight = weight;
		this.makingCharge = makingCharge;
		this.makingChargeType = makingChargeType;
		this.silverRate = silverRate;
		this.silverItemPrice = silverItemPrice;
	}
	
	public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public String getPiecepair() {
		return piecepair;
	}


	public void setPiecepair(String piecepair) {
		this.piecepair = piecepair;
	}


	public Double getWeight() {
		return weight;
	}


	public void setWeight(Double weight) {
		this.weight = weight;
	}


	public Double getMakingCharge() {
		return makingCharge;
	}


	public void setMakingCharge(Double makingCharge) {
		this.makingCharge = makingCharge;
	}


	public String getMakingChargeType() {
		return makingChargeType;
	}


	public void setMakingChargeType(String makingChargeType) {
		this.makingChargeType = makingChargeType;
	}


	public Double getSilverRate() {
		return silverRate;
	}


	public void setSilverRate(Double silverRate) {
		this.silverRate = silverRate;
	}


	public Double getSilverItemPrice() {
		return silverItemPrice;
	}


	public void setSilverItemPrice(Double silverItemPrice) {
		this.silverItemPrice = silverItemPrice;
	}
}
