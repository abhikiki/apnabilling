package com.retail.dto;

import java.util.Date;

public class TransactionSearchResultDto {
	private long transId;
	private Date transDate;
	private String billType;
	private String transactionStatus;
	private String goldItems;
	private String silverItems;
	private String diamondItems;
	private String generalItems;
	private String customerName;
	private String contactNumber;
	private String emailId;
	private String customerAddress;
	private Double totalItemsPrice;
	
	public TransactionSearchResultDto(){}
	
	public long getTransId() {
		return transId;
	}
	public void setTransId(long transId) {
		this.transId = transId;
	}
	public Date getTransDate() {
		return transDate;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	
	public String getGoldItems() {
		return goldItems;
	}

	public void setGoldItems(String goldItems) {
		this.goldItems = goldItems;
	}

	public String getSilverItems() {
		return silverItems;
	}

	public void setSilverItems(String silverItems) {
		this.silverItems = silverItems;
	}

	public String getDiamondItems() {
		return diamondItems;
	}

	public void setDiamondItems(String diamondItems) {
		this.diamondItems = diamondItems;
	}

	public String getGeneralItems() {
		return generalItems;
	}

	public void setGeneralItems(String generalItems) {
		this.generalItems = generalItems;
	}

	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public Double getTotalItemsPrice() {
		return totalItemsPrice;
	}
	public void setTotalItemsPrice(Double totalItemsPrice) {
		this.totalItemsPrice = totalItemsPrice;
	}
	
	
}
