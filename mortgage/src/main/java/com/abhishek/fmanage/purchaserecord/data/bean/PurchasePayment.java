package com.abhishek.fmanage.purchaserecord.data.bean;

public class PurchasePayment {

	private PaymentType paymentType;
	private String referenceNumber;
	private Double amount;
	private String bankName;

	public PurchasePayment(PaymentType paymentType, String referenceNumber, Double amount, String bankName) {
		this.paymentType = paymentType;
		this.referenceNumber = referenceNumber;
		this.amount = amount;
		this.bankName = bankName;
	}
	
	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public enum PaymentType {
	    CASH, CHEQUE, RTGS, NEFT, DEBITCARD, CREDITCARD, OTHER
	}
}
