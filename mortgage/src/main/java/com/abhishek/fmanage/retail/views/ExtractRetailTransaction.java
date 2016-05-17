/**
 * 
 */
package com.abhishek.fmanage.retail.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.abhishek.fmanage.mortgage.data.bean.Customer;
import com.abhishek.fmanage.retail.bean.DiamondTransactionItemBean;
import com.abhishek.fmanage.retail.bean.GeneralTransactionItemBean;
import com.abhishek.fmanage.retail.bean.GoldTransactionItemBean;
import com.abhishek.fmanage.retail.bean.PriceBean;
import com.abhishek.fmanage.retail.bean.RetailTransactionBean;
import com.abhishek.fmanage.retail.bean.SilverTransactionItemBean;
import com.abhishek.fmanage.retail.data.container.DiamondItemContainer;
import com.abhishek.fmanage.retail.data.container.GeneralItemContainer;
import com.abhishek.fmanage.retail.data.container.GoldItemContainer;
import com.abhishek.fmanage.retail.data.container.SilverItemContainer;
import com.abhishek.fmanage.retail.form.PriceForm;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

/**
 * @author GUPTAA6
 *
 */
public class ExtractRetailTransaction {

	private Table goldBillingTable;
	private Table silverBillingTable;
	private Table diamondBillingTable;
	private Table generalBillingTable;
	private Customer customer;
	private PriceForm pfForm;
	private boolean isEstimateBill;
	private Date invoiceDate;
	private String vinNumber;
	private Long invoiceNumber;
	private String dealingStaffName;
	private Boolean includePrice;
	private String notes;
	private boolean isInvoiceCancelled;
	
	
	public ExtractRetailTransaction(
			final Table goldBillingTable,
			final Table silverBillingTable,
			final Table diamondBillingTable,
			final Table generalBillingTable,
			final Customer customer,
			final PriceForm pfForm,
			final boolean isEstimateBill,
			final Date invoiceDate,
			final String vinNumber,
			final Long invoiceNumber,
			final String dealingStaffName,
			final Boolean includePrice,
			final String notes,
			final boolean isInvoiceCancelled) {
		this.goldBillingTable = goldBillingTable;
		this.silverBillingTable = silverBillingTable;
		this.diamondBillingTable = diamondBillingTable;
		this.generalBillingTable = generalBillingTable;
		this.customer = customer;
		this.pfForm = pfForm;
		this.isEstimateBill = isEstimateBill;
		this.invoiceDate = invoiceDate;
		this.vinNumber = vinNumber;
		this.invoiceNumber = invoiceNumber;
		this.dealingStaffName = dealingStaffName;
		this.includePrice = includePrice;
		this.notes = notes;
		this.isInvoiceCancelled = isInvoiceCancelled;
	}

	public RetailTransactionBean extract() {
		PriceBean priceBean = new PriceBean();
		priceBean.setTotalItemsPrice(Double.valueOf(pfForm.totalItemPrice.getValue()));
		priceBean.setDiscount(Double.valueOf(pfForm.discountPrice.getValue()));
		priceBean.setVatCharge(Double.valueOf(pfForm.vatOnNewItemPrice.getValue()));
		priceBean.setOldPurchase(Double.valueOf(pfForm.oldPurchasePrice.getValue()));
		priceBean.setNetpayableAmount(Double.valueOf(pfForm.netAmountToPay.getValue()));
		priceBean.setAdvancePaymentAmount(Double.valueOf(pfForm.advancePayment.getValue()));
		priceBean.setBalanceAmount(Double.valueOf(pfForm.balanceAmount.getValue()));
		return new RetailTransactionBean(
				extractGoldTransItem(),
				extractSilverTransItem(),
				extractDiamondTransItem(),
				extractGeneralTransItem(),
				customer,
				priceBean,
				isEstimateBill,
				invoiceDate,
				vinNumber,
				invoiceNumber,
				dealingStaffName,
				includePrice,
				notes,
				isInvoiceCancelled);
	}

	private List<GeneralTransactionItemBean> extractGeneralTransItem() {
		List<GeneralTransactionItemBean> generalTransBeanList = new ArrayList<>();
		GeneralItemContainer con = (GeneralItemContainer)generalBillingTable.getContainerDataSource();
		for (Object obj : generalBillingTable.getItemIds()) {
			if(NumberUtils.isNumber(((TextField) (con.getItem(obj).getItemProperty(GeneralItemContainer.PRICE).getValue())).getValue()))
			{
				Double itemPrice = Double.valueOf(((TextField) (con.getItem(obj).getItemProperty(GeneralItemContainer.PRICE).getValue())).getValue());
				String itemName = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(GeneralItemContainer.ITEM_NAME).getValue()).getValue());
				int quantity = Integer.valueOf(((TextField) con.getItem(obj).getItemProperty(GeneralItemContainer.QUANTITY).getValue()).getValue());
				String piecePair = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(GeneralItemContainer.PIECE_PAIR).getValue()).getValue());
				Double pricePerPiecePair = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(GeneralItemContainer.PRICE_PER_PIECE_PAIR).getValue()).getValue());
				Double weight = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(GeneralItemContainer.WEIGHT).getValue()).getValue());
				GeneralTransactionItemBean generalTransBean = new GeneralTransactionItemBean(
					itemName, quantity, piecePair, weight, pricePerPiecePair, itemPrice);
				generalTransBeanList.add(generalTransBean);
			}
		}
		return generalTransBeanList;
	}

	private List<DiamondTransactionItemBean> extractDiamondTransItem() {
		List<DiamondTransactionItemBean> diamondTransBeanList = new ArrayList<>();
		DiamondItemContainer con = (DiamondItemContainer)diamondBillingTable.getContainerDataSource();
		for (Object obj : diamondBillingTable.getItemIds()) {
			if(NumberUtils.isNumber(((TextField) (con.getItem(obj).getItemProperty(DiamondItemContainer.PRICE).getValue())).getValue()))
			{
				Double itemPrice = Double.valueOf(((TextField) (con.getItem(obj).getItemProperty(DiamondItemContainer.PRICE).getValue())).getValue());
				String itemName = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(DiamondItemContainer.ITEM_NAME).getValue()).getValue());
				int quantity = Integer.valueOf(((TextField) con.getItem(obj).getItemProperty(DiamondItemContainer.QUANTITY).getValue()).getValue());
				String piecePair = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(DiamondItemContainer.PIECE_PAIR).getValue()).getValue());
				Double goldWeight = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(DiamondItemContainer.GOLD_WEIGHT).getValue()).getValue());
				Double diamondWeightCarat = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(DiamondItemContainer.DIAMOND_WEIGHT).getValue()).getValue());
				int diamondPieceCount = Integer.valueOf(((TextField) con.getItem(obj).getItemProperty(DiamondItemContainer.DIAMOND_PIECE).getValue()).getValue());
				String certificate = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(DiamondItemContainer.CERTIFICATE).getValue()).getValue());
				boolean certified = certificate.equalsIgnoreCase("Yes") ? true : false;
				DiamondTransactionItemBean diamondTransBean = new DiamondTransactionItemBean(
					itemName, quantity, piecePair, goldWeight, diamondWeightCarat, diamondPieceCount, certified, itemPrice);
						diamondTransBeanList.add(diamondTransBean);
			}
			
		}
		return diamondTransBeanList;
	}

	private List<SilverTransactionItemBean> extractSilverTransItem() {
		List<SilverTransactionItemBean> silverTransBeanList = new ArrayList<>();
		SilverItemContainer con = (SilverItemContainer) silverBillingTable.getContainerDataSource();
		for (Object obj : silverBillingTable.getItemIds()) {
			if(NumberUtils.isNumber(((TextField) (con.getItem(obj).getItemProperty(SilverItemContainer.PRICE).getValue())).getValue()))
			{
				Double silverItemPrice = Double.valueOf(((TextField) (con.getItem(obj).getItemProperty(SilverItemContainer.PRICE).getValue())).getValue());
				String silverItemName = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(SilverItemContainer.ITEM_NAME).getValue()).getValue());
				int quantity = Integer.valueOf(((TextField) con.getItem(obj).getItemProperty(SilverItemContainer.QUANTITY).getValue()).getValue());
				String piecePair = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(SilverItemContainer.PIECE_PAIR).getValue()).getValue());
				Double weight = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(SilverItemContainer.WEIGHT).getValue()).getValue());
				Double makingCharge = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(SilverItemContainer.MAKING_CHARGE).getValue()).getValue());
				String makingChargeType = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(SilverItemContainer.MAKING_CHARGE_TYPE).getValue()).getValue());
				Double silverRate = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(SilverItemContainer.SILVER_RATE).getValue()).getValue());
					SilverTransactionItemBean bean = new SilverTransactionItemBean(
						silverItemName, quantity, piecePair, weight, makingCharge, makingChargeType, silverRate, silverItemPrice);
					silverTransBeanList.add(bean);
			}
		}
		return silverTransBeanList;
	}

	private List<GoldTransactionItemBean> extractGoldTransItem() {
		List<GoldTransactionItemBean> goldTransBeanList = new ArrayList<>();
		GoldItemContainer con = (GoldItemContainer) goldBillingTable.getContainerDataSource();
		for (Object obj : goldBillingTable.getItemIds()) {
			if(NumberUtils.isNumber(((TextField) (con.getItem(obj).getItemProperty(GoldItemContainer.PRICE).getValue())).getValue()))
			{
				Double goldItemPrice = Double.valueOf(((TextField) (con.getItem(obj).getItemProperty(GoldItemContainer.PRICE).getValue())).getValue());
				String goldItemName = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(GoldItemContainer.ITEM_NAME).getValue()).getValue());
				String goldType = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(GoldItemContainer.GOLD_TYPE).getValue()).getValue());
				String piecePair = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(GoldItemContainer.PIECE_PAIR).getValue()).getValue());
				String makingChargeType = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(GoldItemContainer.MAKING_CHARGE_TYPE).getValue()).getValue());
				int quantity = Integer.valueOf(((TextField) con.getItem(obj).getItemProperty(GoldItemContainer.QUANTITY).getValue()).getValue());
				Double weight = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(GoldItemContainer.WEIGHT).getValue()).getValue());
				Double makingCharge = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(GoldItemContainer.MAKING_CHARGE).getValue()).getValue());
				Double goldRate = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(GoldItemContainer.GOLD_RATE).getValue()).getValue());
					GoldTransactionItemBean bean = new GoldTransactionItemBean(
							goldItemName, goldType, quantity, piecePair, weight, makingCharge, makingChargeType, goldRate, goldItemPrice);
					goldTransBeanList.add(bean);
			}
		}
		return goldTransBeanList;
	}
}
