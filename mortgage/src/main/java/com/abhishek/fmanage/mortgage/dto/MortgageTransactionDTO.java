package com.abhishek.fmanage.mortgage.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.abhishek.fmanage.retail.dto.CustomerDTO;

public class MortgageTransactionDTO {
	private long transactionId = -1L;
	private Double amount;
	private Double interestRate;
	private String mortgageKeeper;
	private Date startDate;
	private Double totalGoldWeight = 0.000d;
	private Double totalSilverWeight = 0.000d;
	private List<MortgageItemDTO> goldItemList = new ArrayList<>();
	private List<MortgageItemDTO> silverItemList = new ArrayList<>();
	private List<MortgageItemDTO> diamondItemList = new ArrayList<>();
	private String notes;
	private boolean isActive;
	private CustomerDTO customerDto;

	public MortgageTransactionDTO(){}
	
	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public Double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}
	public String getMortgageKeeper() {
		return mortgageKeeper;
	}
	public void setMortgageKeeper(String mortgageKeeper) {
		this.mortgageKeeper = mortgageKeeper;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Double getTotalGoldWeight() {
		return totalGoldWeight;
	}

	public void setTotalGoldWeight(Double totalGoldWeight) {
		this.totalGoldWeight = totalGoldWeight;
	}

	public Double getTotalSilverWeight() {
		return totalSilverWeight;
	}

	public void setTotalSilverWeight(Double totalSilverWeight) {
		this.totalSilverWeight = totalSilverWeight;
	}

	public List<MortgageItemDTO> getGoldItemList() {
		return goldItemList;
	}
	public void setGoldItemList(List<MortgageItemDTO> goldItemList) {
		this.goldItemList = goldItemList;
	}
	public List<MortgageItemDTO> getSilverItemList() {
		return silverItemList;
	}
	public void setSilverItemList(List<MortgageItemDTO> silverItemList) {
		this.silverItemList = silverItemList;
	}
	public List<MortgageItemDTO> getDiamondItemList() {
		return diamondItemList;
	}
	public void setDiamondItemList(List<MortgageItemDTO> diamondItemList) {
		this.diamondItemList = diamondItemList;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public CustomerDTO getCustomerDto() {
		return customerDto;
	}
	public void setCustomerDto(CustomerDTO customerDto) {
		this.customerDto = customerDto;
	}
}
