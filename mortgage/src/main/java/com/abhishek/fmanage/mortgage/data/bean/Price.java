/**
 * 
 */
package com.abhishek.fmanage.mortgage.data.bean;

/**
 * @author GUPTAA6
 *
 */
public class Price {
	private double totalItemPrice = 0.0f;
	private double vatOnNewItemPrice = 0.0f;
	private double oldPurchasePrice = 0.0f;
	private double discountPrice = 0.0f;
	private double netPayablePrice = 0.0f;
	
	public double getVatOnNewItemPrice() {
		return vatOnNewItemPrice;
	}
	public void setVatOnNewItemPrice(double vatOnNewItemPrice) {
		this.vatOnNewItemPrice = vatOnNewItemPrice;
	}

	public double getTotalItemPrice() {
		return totalItemPrice;
	}
	public void setTotalItemPrice(double totalItemPrice) {
		this.totalItemPrice = totalItemPrice;
	}
	public double getOldPurchasePrice() {
		return oldPurchasePrice;
	}
	public void setOldPurchasePrice(double oldPurchasePrice) {
		this.oldPurchasePrice = oldPurchasePrice;
	}

	public double getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}
	public double getNetPayablePrice() {
		return netPayablePrice;
	}
	public void setNetPayablePrice(double netPayablePrice) {
		this.netPayablePrice = netPayablePrice;
	}
	
}
