/**
 * 
 */
package com.abhishek.fmanage.retail.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.abhishek.fmanage.retail.bean.DiamondTransactionItemBean;
import com.abhishek.fmanage.retail.bean.GeneralTransactionItemBean;
import com.abhishek.fmanage.retail.bean.GoldTransactionItemBean;
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

	private RetailTransactionBean retailTransBean = new RetailTransactionBean();
	private Table goldBillingTable;
	private Table silverBillingTable;
	private Table diamondBillingTable;
	private Table generalBillingTable;
	private PriceForm pfForm;
	private boolean isEstimateBill;
	private Date invoiceDate;
	
	public ExtractRetailTransaction(
			final Table goldBillingTable,
			final Table silverBillingTable,
			final Table diamondBillingTable,
			final Table generalBillingTable,
			final PriceForm pfForm,
			final boolean isEstimateBill,
			final Date invoiceDate) {
		this.generalBillingTable = goldBillingTable;
		this.silverBillingTable = silverBillingTable;
		this.diamondBillingTable = diamondBillingTable;
		this.generalBillingTable = generalBillingTable;
		this.pfForm = pfForm;
		this.isEstimateBill = isEstimateBill;
		this.invoiceDate = invoiceDate;
	}

	public RetailTransactionBean extract() {
		List<GoldTransactionItemBean> goldTransBeanList = extractGoldTransItem();
		List<SilverTransactionItemBean> silverTransBeanList = extractSilverTransItem();
		List<DiamondTransactionItemBean> diamondTransBeanList = extractDiamondTransItem();
		List<GeneralTransactionItemBean> generalTransBeanList = extractGeneralTransItem();
		return retailTransBean;
	}

	private List<GeneralTransactionItemBean> extractGeneralTransItem() {
		List<GeneralTransactionItemBean> generalTransBeanList = new ArrayList<>();
		GeneralItemContainer con = (GeneralItemContainer)generalBillingTable.getContainerDataSource();
		for (Object obj : generalBillingTable.getItemIds()) {
			
		}
		return generalTransBeanList;
	}

	private List<DiamondTransactionItemBean> extractDiamondTransItem() {
		List<DiamondTransactionItemBean> diamondTransBeanList = new ArrayList<>();
		DiamondItemContainer con = (DiamondItemContainer)diamondBillingTable.getContainerDataSource();
		for (Object obj : diamondBillingTable.getItemIds()) {
			Double itemPrice = Double.valueOf(((TextField) (con.getItem(obj).getItemProperty(DiamondItemContainer.PRICE).getValue())).getValue());
			String itemName = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(DiamondItemContainer.ITEM_NAME).getValue()).getValue());
			int quantity = Integer.valueOf(((TextField) con.getItem(obj).getItemProperty(DiamondItemContainer.QUANTITY).getValue()).getValue());
			String piecePair = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(DiamondItemContainer.PIECE_PAIR).getValue()).getValue());
			Double goldWeight = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(DiamondItemContainer.GOLD_WEIGHT).getValue()).getValue());
			Double diamondWeightCarat = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(DiamondItemContainer.DIAMOND_WEIGHT).getValue()).getValue());
			int diamondPieceCount = Integer.valueOf(((TextField) con.getItem(obj).getItemProperty(DiamondItemContainer.DIAMOND_PIECE).getValue()).getValue());
			String certificate = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(DiamondItemContainer.CERTIFICATE).getValue()).getValue());
			boolean certified = certificate.equalsIgnoreCase("Yes") ? true : false;
			if(itemPrice > 0.0)
			{
				DiamondTransactionItemBean diamondTransBean = new DiamondTransactionItemBean(
					itemName, quantity, piecePair, goldWeight, diamondWeightCarat, diamondPieceCount, certified, itemPrice);
			}
			
		}
		return diamondTransBeanList;
	}

	private List<SilverTransactionItemBean> extractSilverTransItem() {
		List<SilverTransactionItemBean> silverTransBeanList = new ArrayList<>();
		SilverItemContainer con = (SilverItemContainer) silverBillingTable.getContainerDataSource();
		for (Object obj : silverBillingTable.getItemIds()) {
			Double silverItemPrice = Double.valueOf(((TextField) (con.getItem(obj).getItemProperty(SilverItemContainer.PRICE).getValue())).getValue());
			String silverItemName = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(SilverItemContainer.ITEM_NAME).getValue()).getValue());
			int quantity = Integer.valueOf(((TextField) con.getItem(obj).getItemProperty(SilverItemContainer.QUANTITY).getValue()).getValue());
			String piecePair = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(SilverItemContainer.PIECE_PAIR).getValue()).getValue());
			Double weight = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(SilverItemContainer.WEIGHT).getValue()).getValue());
			Double makingCharge = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(SilverItemContainer.MAKING_CHARGE).getValue()).getValue());
			String makingChargeType = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(SilverItemContainer.MAKING_CHARGE_TYPE).getValue()).getValue());
			Double silverRate = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(SilverItemContainer.SILVER_RATE).getValue()).getValue());
			if(silverItemPrice > 0.0){
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
			Double goldItemPrice = Double.valueOf(((TextField) (con.getItem(obj).getItemProperty(GoldItemContainer.PRICE).getValue())).getValue());
			String goldItemName = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(GoldItemContainer.ITEM_NAME).getValue()).getValue());
			String goldType = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(GoldItemContainer.GOLD_TYPE).getValue()).getValue());
			String piecePair = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(GoldItemContainer.PIECE_PAIR).getValue()).getValue());
			String makingChargeType = String.valueOf(((ComboBox) con.getItem(obj).getItemProperty(GoldItemContainer.MAKING_CHARGE_TYPE).getValue()).getValue());
			int quantity = Integer.valueOf(((TextField) con.getItem(obj).getItemProperty(GoldItemContainer.QUANTITY).getValue()).getValue());
			Double weight = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(GoldItemContainer.WEIGHT).getValue()).getValue());
			Double makingCharge = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(GoldItemContainer.MAKING_CHARGE).getValue()).getValue());
			Double goldRate = Double.valueOf(((TextField) con.getItem(obj).getItemProperty(GoldItemContainer.GOLD_RATE).getValue()).getValue());
			if(goldItemPrice > 0.0){
				GoldTransactionItemBean bean = new GoldTransactionItemBean(
						goldItemName, goldType, quantity, piecePair, weight, makingCharge, makingChargeType, goldRate, goldItemPrice);
				goldTransBeanList.add(bean);
			}
		}
		return goldTransBeanList;
	}
}
