/**
 * 
 */
package com.abhishek.fmanage.retail.dto;

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
	private Date invoiceDate;
	private String vinNumber;
	private Long invoiceNumber;
	private String dealingStaffName;
	private Boolean includePrice;
	private String notes;
	private boolean isInvoiceCancelled;
	
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
		boolean isInvoiceCancelled)
	{
		this.goldTransactionItemBeanList = goldTransactionItemBeanList;
		this.silverTransactionItemBeanList = silverTransactionItemBeanList;
		this.diamondTransactionItemBeanList = diamondTransactionItemBeanList;
		this.generalTransactionItemBeanList = generalTransactionItemBeanList;
		this.customer = customer;
		this.priceBean = priceBean;
		this.isEstimateBill = isEstimateBill;
		this.invoiceDate = invoiceDate;
		this.vinNumber = vinNumber;
		this.invoiceNumber = invoiceNumber;
		this.dealingStaffName = dealingStaffName;
		this.includePrice = includePrice;
		this.notes = notes;
		this.isInvoiceCancelled = isInvoiceCancelled;
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

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public String getVinNumber() {
		return vinNumber;
	}

	public boolean isInvoiceCancelled() {
		return isInvoiceCancelled;
	}

	public void setInvoiceCancelled(boolean isInvoiceCancelled) {
		this.isInvoiceCancelled = isInvoiceCancelled;
	}
}
