package com.abhishek.retail.dto;

import java.util.Date;

public class RetailTransactionDTO {

	private long shopId;
	private long transId;
	private Date transdate;
	private String billType;
	private String dealingStaffName = "STAFF";
	private String notes;
	private boolean isPriceToInclude;
	private boolean isTransactionActive;

	public RetailTransactionDTO(){
		
	}
	
	public long getShopId() {
		return shopId;
	}
	public void setShopId(long shopId) {
		this.shopId = shopId;
	}
	public long getTransId() {
		return transId;
	}
	public void setTransId(long transId) {
		this.transId = transId;
	}
	public Date getTransdate() {
		return transdate;
	}
	public void setTransdate(Date transdate) {
		this.transdate = transdate;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getDealingStaffName() {
		return dealingStaffName;
	}

	public void setDealingStaffName(String dealingStaffName) {
		this.dealingStaffName = dealingStaffName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isPriceToInclude() {
		return isPriceToInclude;
	}

	public void setPriceToInclude(boolean isPriceToInclude) {
		this.isPriceToInclude = isPriceToInclude;
	}

	public boolean isTransactionActive() {
		return isTransactionActive;
	}

	public void setTransactionActive(boolean isTransactionActive) {
		this.isTransactionActive = isTransactionActive;
	}
	
	
}
