/**
 * 
 */
package com.abhishek.fmanage.retail.bean;

import java.util.Date;
import java.util.List;

/**
 * @author GUPTAA6
 *
 */
public class RetailTransactionBean {

	private List<GoldTransactionItemBean> goldTransactionItemBeanList;
	private List<SilverTransactionItemBean> silverTransactionItemBeanList;
	private List<DiamondTransactionItemBean> diamondTransactionItemBeanList;
	private List<GeneralTransactionItemBean> generalTransactionItemBeanList;
	private PriceBean priceBean;
	private boolean isEstimateBill;
	private Date invoiceDate;
	private String vinNumber;

	public RetailTransactionBean() {
	}
	
	public RetailTransactionBean(
		List<GoldTransactionItemBean> goldTransactionItemBeanList,
		List<SilverTransactionItemBean> silverTransactionItemBeanList,
		List<DiamondTransactionItemBean> diamondTransactionItemBeanList,
		List<GeneralTransactionItemBean> generalTransactionItemBeanList,
		PriceBean priceBean,
		boolean isEstimateBill,
		Date invoiceDate,
		String vinNumber)
	{
		this.goldTransactionItemBeanList = goldTransactionItemBeanList;
		this.silverTransactionItemBeanList = silverTransactionItemBeanList;
		this.diamondTransactionItemBeanList = diamondTransactionItemBeanList;
		this.generalTransactionItemBeanList = generalTransactionItemBeanList;
		this.priceBean = priceBean;
		this.isEstimateBill = isEstimateBill;
		this.invoiceDate = invoiceDate;
		this.vinNumber = vinNumber;
		
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
}
