package com.abhishek.fmanage.retail.dto;

public class RetailAdvanceBillDTO {

	private long shopId;
	private long transId;
	private long advanceBillId;
	
	public RetailAdvanceBillDTO(){}
	
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
	public long getAdvanceBillId() {
		return advanceBillId;
	}
	public void setAdvanceBillId(long advanceBillId) {
		this.advanceBillId = advanceBillId;
	}
	
	
}
