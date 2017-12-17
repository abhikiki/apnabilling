package com.abhishek.fmanage.retail.dto;

import java.util.List;

public class SummaryDTO {

	public SummaryDTO(){}
	
	private Double totalSale;
	private Double totalGoldWeight;
	private Double totalSilverWeight;
	private Double totalVat;
	private Double totalCardPayment;
	private Double totalCashPayment;
	private Double totalChequePayment;
	private Double totalNeftPayment;
	private Double totalRtgsPayment;
	private List<ItemSummaryDTO> goldItemSummaryDtoList;
	private List<ItemSummaryDTO> silverItemSummaryDtoList;
	private List<ItemSummaryDTO> diamondItemSummaryDtoList;
	private List<ItemSummaryDTO> generalItemSummaryDtoList;

	public Double getTotalSale() {
		return totalSale;
	}
	public void setTotalSale(Double totalSale) {
		this.totalSale = totalSale;
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
	
	public Double getTotalVat() {
		return totalVat;
	}
	public void setTotalVat(Double totalVat) {
		this.totalVat = totalVat;
	}
	public List<ItemSummaryDTO> getGoldItemSummaryDtoList() {
		return goldItemSummaryDtoList;
	}
	public void setGoldItemSummaryDtoList(
			List<ItemSummaryDTO> goldItemSummaryDtoList) {
		this.goldItemSummaryDtoList = goldItemSummaryDtoList;
	}

	public Double getTotalCardPayment() {
		return totalCardPayment;
	}

	public void setTotalCardPayment(Double totalCardPayment) {
		this.totalCardPayment = totalCardPayment;
	}

	public Double getTotalCashPayment() {
		return totalCashPayment;
	}

	public void setTotalCashPayment(Double totalCashPayment) {
		this.totalCashPayment = totalCashPayment;
	}

	public Double getTotalChequePayment() {
		return totalChequePayment;
	}

	public void setTotalChequePayment(Double totalChequePayment) {
		this.totalChequePayment = totalChequePayment;
	}

	public Double getTotalNeftPayment() {
		return totalNeftPayment;
	}

	public void setTotalNeftPayment(Double totalNeftPayment) {
		this.totalNeftPayment = totalNeftPayment;
	}

	public Double getTotalRtgsPayment() {
		return totalRtgsPayment;
	}

	public void setTotalRtgsPayment(Double totalRtgsPayment) {
		this.totalRtgsPayment = totalRtgsPayment;
	}

	public List<ItemSummaryDTO> getSilverItemSummaryDtoList() {
		return silverItemSummaryDtoList;
	}
	public void setSilverItemSummaryDtoList(
			List<ItemSummaryDTO> silverItemSummaryDtoList) {
		this.silverItemSummaryDtoList = silverItemSummaryDtoList;
	}
	public List<ItemSummaryDTO> getDiamondItemSummaryDtoList() {
		return diamondItemSummaryDtoList;
	}
	public void setDiamondItemSummaryDtoList(
			List<ItemSummaryDTO> diamondItemSummaryDtoList) {
		this.diamondItemSummaryDtoList = diamondItemSummaryDtoList;
	}
	public List<ItemSummaryDTO> getGeneralItemSummaryDtoList() {
		return generalItemSummaryDtoList;
	}
	public void setGeneralItemSummaryDtoList(
			List<ItemSummaryDTO> generalItemSummaryDtoList) {
		this.generalItemSummaryDtoList = generalItemSummaryDtoList;
	}
}
