package com.abhishek.fmanage.purchaserecord.data.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PurchaseRecordBean {

	private long purchaseId = -1;
	private Date purchaseDate;
	private String partyName;
	private String itemName;
	private Double weight;
	private Double cashPayment;
	private List<PurchasePayment> chequePayment = new ArrayList<PurchasePayment>();
	private List<PurchasePayment> rtgsPayment = new ArrayList<PurchasePayment>();
	private List<PurchasePayment> neftPayment = new ArrayList<PurchasePayment>();
	private List<PurchasePayment> debitCardPayment = new ArrayList<PurchasePayment>();
	private List<PurchasePayment> creditCardPayment = new ArrayList<PurchasePayment>();
	private List<PurchasePayment> otherModePayment = new ArrayList<PurchasePayment>();
	private String notes;
	
	public long getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(long purchaseId) {
		this.purchaseId = purchaseId;
	}
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getCashPayment() {
		return cashPayment;
	}
	public void setCashPayment(Double cashPayment) {
		this.cashPayment = cashPayment;
	}
	public List<PurchasePayment> getChequePayment() {
		return chequePayment;
	}
	public void setChequePayment(List<PurchasePayment> chequePayment) {
		this.chequePayment = chequePayment;
	}
	public List<PurchasePayment> getRtgsPayment() {
		return rtgsPayment;
	}
	public void setRtgsPayment(List<PurchasePayment> rtgsPayment) {
		this.rtgsPayment = rtgsPayment;
	}
	public List<PurchasePayment> getNeftPayment() {
		return neftPayment;
	}
	public void setNeftPayment(List<PurchasePayment> neftPayment) {
		this.neftPayment = neftPayment;
	}
	public List<PurchasePayment> getDebitCardPayment() {
		return debitCardPayment;
	}
	public void setDebitCardPayment(List<PurchasePayment> debitCardPayment) {
		this.debitCardPayment = debitCardPayment;
	}
	public List<PurchasePayment> getCreditCardPayment() {
		return creditCardPayment;
	}
	public void setCreditCardPayment(List<PurchasePayment> creditCardPayment) {
		this.creditCardPayment = creditCardPayment;
	}
	public List<PurchasePayment> getOtherModePayment() {
		return otherModePayment;
	}
	public void setOtherModePayment(List<PurchasePayment> otherModePayment) {
		this.otherModePayment = otherModePayment;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
}
