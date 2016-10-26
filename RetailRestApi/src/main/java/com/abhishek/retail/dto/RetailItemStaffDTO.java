package com.abhishek.retail.dto;

import java.util.ArrayList;
import java.util.List;

public class RetailItemStaffDTO {

	private List<ItemDTO> goldItemsList = new ArrayList<>();
	private List<ItemDTO> silverItemsList = new ArrayList<>();
	private List<ItemDTO> diamondItemsList = new ArrayList<>();
	private List<ItemDTO> generalItemsList = new ArrayList<>();
	private List<ItemDTO> staffList = new ArrayList<>();

	public RetailItemStaffDTO(){}

	public List<ItemDTO> getGoldItemsList() {
		return goldItemsList;
	}

	public void setGoldItemsList(List<ItemDTO> goldItemsList) {
		this.goldItemsList = goldItemsList;
	}

	public List<ItemDTO> getSilverItemsList() {
		return silverItemsList;
	}

	public void setSilverItemsList(List<ItemDTO> silverItemsList) {
		this.silverItemsList = silverItemsList;
	}

	public List<ItemDTO> getDiamondItemsList() {
		return diamondItemsList;
	}

	public void setDiamondItemsList(List<ItemDTO> diamondItemsList) {
		this.diamondItemsList = diamondItemsList;
	}

	public List<ItemDTO> getGeneralItemsList() {
		return generalItemsList;
	}

	public void setGeneralItemsList(List<ItemDTO> generalItemsList) {
		this.generalItemsList = generalItemsList;
	}

	public List<ItemDTO> getStaffList() {
		return staffList;
	}

	public void setStaffList(List<ItemDTO> staffList) {
		this.staffList = staffList;
	}
	
	
}
