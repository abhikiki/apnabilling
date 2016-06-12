/**
 * 
 */
package com.retail.dto;

import java.util.Date;
import java.util.List;

/**
 * @author GUPTAA6
 *
 */
public class TransactionDTO {

	private List<GoldTransactionItemDTO> goldTransactionItemBeanList;
	private List<SilverTransactionItemDTO> silverTransactionItemBeanList;
	private List<DiamondTransactionItemDTO> diamondTransactionItemBeanList;
	private List<GeneralTransactionItemDTO> generalTransactionItemBeanList;
	private CustomerDTO customer;
	private PriceDTO priceBean;
	private boolean isEstimateBill;
	private Date transactionDate;
	private String vinNumber;
	private Long invoiceNumber = -1L;
	private String dealingStaffName;
	private Boolean includePrice;
	private String notes;
	private boolean isTransactionActive;
	
	public TransactionDTO() {
	}
	
	public TransactionDTO(
		List<GoldTransactionItemDTO> goldTransactionItemBeanList,
		List<SilverTransactionItemDTO> silverTransactionItemBeanList,
		List<DiamondTransactionItemDTO> diamondTransactionItemBeanList,
		List<GeneralTransactionItemDTO> generalTransactionItemBeanList,
		CustomerDTO customer,
		PriceDTO priceBean,
		boolean isEstimateBill,
		Date invoiceDate,
		String vinNumber,
		Long invoiceNumber,
		String dealingStaffName,
		final Boolean includePrice,
		final String notes,
		boolean isTransactionActive)
	{
		this.goldTransactionItemBeanList = goldTransactionItemBeanList;
		this.silverTransactionItemBeanList = silverTransactionItemBeanList;
		this.diamondTransactionItemBeanList = diamondTransactionItemBeanList;
		this.generalTransactionItemBeanList = generalTransactionItemBeanList;
		this.customer = customer;
		this.priceBean = priceBean;
		this.isEstimateBill = isEstimateBill;
		this.transactionDate = invoiceDate;
		this.vinNumber = vinNumber;
		this.invoiceNumber = invoiceNumber;
		this.dealingStaffName = dealingStaffName;
		this.includePrice = includePrice;
		this.notes = notes;
		this.isTransactionActive = isTransactionActive;
	}

	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Boolean getIncludePrice() {
		return includePrice;
	}

	public void setIncludePrice(Boolean includePrice) {
		this.includePrice = includePrice;
	}

	public CustomerDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

	public Long getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getDealingStaffName() {
		return dealingStaffName;
	}

	public void setDealingStaffName(String dealingStaffName) {
		this.dealingStaffName = dealingStaffName;
	}

	
	public void setGoldTransactionItemBeanList(
			List<GoldTransactionItemDTO> goldTransactionItemBeanList) {
		this.goldTransactionItemBeanList = goldTransactionItemBeanList;
	}

	public void setSilverTransactionItemBeanList(
			List<SilverTransactionItemDTO> silverTransactionItemBeanList) {
		this.silverTransactionItemBeanList = silverTransactionItemBeanList;
	}

	public void setDiamondTransactionItemBeanList(
			List<DiamondTransactionItemDTO> diamondTransactionItemBeanList) {
		this.diamondTransactionItemBeanList = diamondTransactionItemBeanList;
	}

	public void setGeneralTransactionItemBeanList(
			List<GeneralTransactionItemDTO> generalTransactionItemBeanList) {
		this.generalTransactionItemBeanList = generalTransactionItemBeanList;
	}

	public void setPriceBean(PriceDTO priceBean) {
		this.priceBean = priceBean;
	}

	public void setEstimateBill(boolean isEstimateBill) {
		this.isEstimateBill = isEstimateBill;
	}


	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public void setVinNumber(String vinNumber) {
		this.vinNumber = vinNumber;
	}

	public List<GoldTransactionItemDTO> getGoldTransactionItemBeanList() {
		return goldTransactionItemBeanList;
	}

	public List<SilverTransactionItemDTO> getSilverTransactionItemBeanList() {
		return silverTransactionItemBeanList;
	}

	public List<DiamondTransactionItemDTO> getDiamondTransactionItemBeanList() {
		return diamondTransactionItemBeanList;
	}

	public List<GeneralTransactionItemDTO> getGeneralTransactionItemBeanList() {
		return generalTransactionItemBeanList;
	}

	public PriceDTO getPriceBean() {
		return priceBean;
	}

	public boolean isEstimateBill() {
		return isEstimateBill;
	}

	public String getVinNumber() {
		return vinNumber;
	}

	public boolean isTransactionActive() {
		return isTransactionActive;
	}

	public void setTransactionActive(boolean isTransactionActive) {
		this.isTransactionActive = isTransactionActive;
	}
}
