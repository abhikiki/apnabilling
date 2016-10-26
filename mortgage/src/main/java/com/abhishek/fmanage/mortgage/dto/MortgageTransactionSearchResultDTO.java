package com.abhishek.fmanage.mortgage.dto;

import java.util.Date;

public class MortgageTransactionSearchResultDTO {
	private long transId;
	private String transactionStatus;
	private Date transactionStartDate;
	private Date endDate;
	private int daysDiff;
	private Double monthsDiff;
	private Double interestRate;
	private Double amount;
	private Double AmountToPay;
	private String keeperName;
	private String customerName;
	private String contactNumber;
	private String customerAddress;
	private String emailId;
	private String totalGoldWeight;
	private String totalSilverWeight;
	private String silverItems;
	private String diamondItems;
	private String goldItems;
	private String notes;
	
	public long getTransId() {
		return transId;
	}
	public void setTransId(long transId) {
		this.transId = transId;
	}
	
	
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public Date getTransactionStartDate() {
		return transactionStartDate;
	}
	public void setTransactionStartDate(Date transactionStartDate) {
		this.transactionStartDate = transactionStartDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getDaysDiff() {
		return daysDiff;
	}
	public void setDaysDiff(int daysDiff) {
		this.daysDiff = daysDiff;
	}
	public Double getMonthsDiff() {
		return monthsDiff;
	}
	public void setMonthsDiff(Double monthsDiff) {
		this.monthsDiff = monthsDiff;
	}
	public Double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}
	public Double getAmountToPay() {
		return AmountToPay;
	}
	public void setAmountToPay(Double amountToPay) {
		AmountToPay = amountToPay;
	}
	public String getKeeperName() {
		return keeperName;
	}
	public void setKeeperName(String keeperName) {
		this.keeperName = keeperName;
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
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public String getTotalGoldWeight() {
		return totalGoldWeight;
	}
	public void setTotalGoldWeight(String totalGoldWeight) {
		this.totalGoldWeight = totalGoldWeight;
	}
	public String getTotalSilverWeight() {
		return totalSilverWeight;
	}
	public void setTotalSilverWeight(String totalSilverWeight) {
		this.totalSilverWeight = totalSilverWeight;
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
	public String getGoldItems() {
		return goldItems;
	}
	public void setGoldItems(String goldItems) {
		this.goldItems = goldItems;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
