/**
 * 
 */
package com.abhishek.fmanage.retail.dto;

/**
 * @author GUPTAA6
 *
 */
public class GeneralTransactionItemDTO {

	private String itemName;
	private int quantity;
	private String piecepair;
	private Double weight;
	private Double pricePerPiecepair;
	private Double itemPrice;
	
	public GeneralTransactionItemDTO() {}

	public GeneralTransactionItemDTO(
		String itemName,
		int quantity,
		String piecepair,
		Double weight,
		Double pricePerPiecepair,
		Double itemPrice) {
		this.itemName = itemName;
		this.quantity = quantity;
		this.piecepair = piecepair;
		this.weight = weight;
		this.pricePerPiecepair = pricePerPiecepair;
		this.itemPrice = itemPrice;
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
	public Double getPricePerPiecepair() {
		return pricePerPiecepair;
	}
	public void setPricePerPiecepair(Double pricePerPiecepair) {
		this.pricePerPiecepair = pricePerPiecepair;
	}
	public Double getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}
}
