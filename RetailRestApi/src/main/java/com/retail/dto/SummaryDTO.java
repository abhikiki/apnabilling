package com.retail.dto;

import java.util.List;

public class SummaryDTO {

	public SummaryDTO(){}
	
	private Double totalSale;
	private Double totalGoldWeight;
	private Double totalSilverWeight;
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
	public List<ItemSummaryDTO> getGoldItemSummaryDtoList() {
		return goldItemSummaryDtoList;
	}
	public void setGoldItemSummaryDtoList(
			List<ItemSummaryDTO> goldItemSummaryDtoList) {
		this.goldItemSummaryDtoList = goldItemSummaryDtoList;
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
