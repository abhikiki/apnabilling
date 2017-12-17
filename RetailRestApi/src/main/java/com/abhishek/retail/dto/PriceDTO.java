/**
 * 
 */
package com.abhishek.retail.dto;

/**
 * @author GUPTAA6
 *
 */
public class PriceDTO {

	private Double vatPercentage = 3.0d;// default
	private Double totalItemsPrice;
	private Double discount;
	private Double vatCharge;
	private Double oldPurchase;
	private Double netpayableAmount;
	private Double advancePaymentAmount;
	Double balanceAmount;
	
	public PriceDTO() {
		// TODO Auto-generated constructor stub
	}

	public PriceDTO(
		Double vatPercentage,
		Double totalItemsPrice,
		Double discount,
		Double vatCharge,
		Double oldPurchase,
		Double netpayableAmount,
		Double advancePaymentAmount,
		Double balanceAmount) {
		this.vatPercentage = vatPercentage;
		this.totalItemsPrice = totalItemsPrice;
		this.discount = discount;
		this.vatCharge = vatCharge;
		this.oldPurchase = oldPurchase;
		this.netpayableAmount = netpayableAmount;
		this.advancePaymentAmount = advancePaymentAmount;
		this.balanceAmount = balanceAmount;
	}
	
	
	public Double getVatPercentage() {
		return vatPercentage;
	}

	public void setVatPercentage(Double vatPercentage) {
		this.vatPercentage = vatPercentage;
	}

	public Double getTotalItemsPrice() {
		return totalItemsPrice;
	}

	public void setTotalItemsPrice(Double totalItemsPrice) {
		this.totalItemsPrice = totalItemsPrice;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getVatCharge() {
		return vatCharge;
	}

	public void setVatCharge(Double vatCharge) {
		this.vatCharge = vatCharge;
	}

	public Double getOldPurchase() {
		return oldPurchase;
	}

	public void setOldPurchase(Double oldPurchase) {
		this.oldPurchase = oldPurchase;
	}

	public Double getNetpayableAmount() {
		return netpayableAmount;
	}

	public void setNetpayableAmount(Double netpayableAmount) {
		this.netpayableAmount = netpayableAmount;
	}

	public Double getAdvancePaymentAmount() {
		return advancePaymentAmount;
	}

	public void setAdvancePaymentAmount(Double advancePaymentAmount) {
		this.advancePaymentAmount = advancePaymentAmount;
	}

	public Double getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	
}
