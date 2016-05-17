/**
 * 
 */
package com.abhishek.fmanage.retail.bean;

import java.util.Date;
import java.util.List;

import com.abhishek.fmanage.mortgage.data.bean.Customer;

/**
 * @author GUPTAA6
 *
 */
public class RetailTransactionBean {

	private List<GoldTransactionItemBean> goldTransactionItemBeanList;
	private List<SilverTransactionItemBean> silverTransactionItemBeanList;
	private List<DiamondTransactionItemBean> diamondTransactionItemBeanList;
	private List<GeneralTransactionItemBean> generalTransactionItemBeanList;
	private Customer customer;
	private PriceBean priceBean;
	private boolean isEstimateBill;
	private Date invoiceDate;
	private String vinNumber;
	private Long invoiceNumber;
	private String dealingStaffName;
	private Boolean includePrice;
	private String notes;
	private boolean isInvoiceCancelled;
	
	public RetailTransactionBean() {
	}
	
	public RetailTransactionBean(
		List<GoldTransactionItemBean> goldTransactionItemBeanList,
		List<SilverTransactionItemBean> silverTransactionItemBeanList,
		List<DiamondTransactionItemBean> diamondTransactionItemBeanList,
		List<GeneralTransactionItemBean> generalTransactionItemBeanList,
		Customer customer,
		PriceBean priceBean,
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

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
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

	public List<GoldTransactionItemBean> getGoldTransactionItemBeanList() {
		return goldTransactionItemBeanList;
	}

	public List<SilverTransactionItemBean> getSilverTransactionItemBeanList() {
		return silverTransactionItemBeanList;
	}

	public List<DiamondTransactionItemBean> getDiamondTransactionItemBeanList() {
		return diamondTransactionItemBeanList;
	}

	public List<GeneralTransactionItemBean> getGeneralTransactionItemBeanList() {
		return generalTransactionItemBeanList;
	}

	public PriceBean getPriceBean() {
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
