
package com.abhishek.retail.dto;

/**
 * @author GUPTAA6
 *
 */
public class DiamondTransactionItemDTO {

	private String itemName;
	private int quantity;
	private String piecePair;
	private Double goldWeight;
	private Double diamondWeightCarat;
	private int diamondPieceCount;
	private boolean certified;
	private Double itemPrice;
	
	public DiamondTransactionItemDTO() {}
	
	public DiamondTransactionItemDTO(
			String itemName,
			int quantity,
			String piecePair,
			Double goldWeight,
			Double diamondWeightCarat,
			int diamondPieceCount,
			boolean certified,
			Double itemPrice) {
		this.itemName = itemName;
		this.quantity = quantity;
		this.piecePair = piecePair;
		this.goldWeight = goldWeight;
		this.diamondWeightCarat = diamondWeightCarat;
		this.diamondPieceCount = diamondPieceCount;
		this.certified = certified;
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

	public String getPiecePair() {
		return piecePair;
	}

	public void setPiecePair(String piecePair) {
		this.piecePair = piecePair;
	}

	public Double getGoldWeight() {
		return goldWeight;
	}

	public void setGoldWeight(Double goldWeight) {
		this.goldWeight = goldWeight;
	}

	public Double getDiamondWeightCarat() {
		return diamondWeightCarat;
	}

	public void setDiamondWeightCarat(Double diamondWeightCarat) {
		this.diamondWeightCarat = diamondWeightCarat;
	}

	public int getDiamondPieceCount() {
		return diamondPieceCount;
	}

	public void setDiamondPieceCount(int diamondPieceCount) {
		this.diamondPieceCount = diamondPieceCount;
	}

	public boolean isCertified() {
		return certified;
	}

	public void setCertified(boolean certified) {
		this.certified = certified;
	}

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}
}
