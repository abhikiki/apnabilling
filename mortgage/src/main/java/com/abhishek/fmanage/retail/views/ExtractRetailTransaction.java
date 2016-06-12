/**
 * 
 */
package com.abhishek.fmanage.retail.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.abhishek.fmanage.retail.data.container.DiamondItemContainer;
import com.abhishek.fmanage.retail.data.container.GeneralItemContainer;
import com.abhishek.fmanage.retail.data.container.GoldItemContainer;
import com.abhishek.fmanage.retail.data.container.SilverItemContainer;
import com.abhishek.fmanage.retail.dto.CustomerDTO;
import com.abhishek.fmanage.retail.dto.DiamondTransactionItemDTO;
import com.abhishek.fmanage.retail.dto.GeneralTransactionItemDTO;
import com.abhishek.fmanage.retail.dto.GoldTransactionItemDTO;
import com.abhishek.fmanage.retail.dto.PriceDTO;
import com.abhishek.fmanage.retail.dto.TransactionDTO;
import com.abhishek.fmanage.retail.dto.SilverTransactionItemDTO;
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
	private CustomerDTO customer;
	private PriceForm pfForm;
	private boolean isEstimateBill;
	private Date invoiceDate;
	private String vinNumber;
	private Long invoiceNumber;
	private String dealingStaffName;
	private Boolean includePrice;
	private String notes;
	private boolean isTransactionActive;
	
	
	public ExtractRetailTransaction(
			final Table goldBillingTable,
			final Table silverBillingTable,
			final Table diamondBillingTable,
			final Table generalBillingTable,
			final CustomerDTO customer,
			final PriceForm pfForm,
			final boolean isEstimateBill,
			final Date invoiceDate,
			final String vinNumber,
			final Long invoiceNumber,
			final String dealingStaffName,
			final Boolean includePrice,
			final String notes,
			final boolean isTransactionActive) {
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
		this.isTransactionActive = isTransactionActive;
	}

	public TransactionDTO extract() {
		PriceDTO priceBean = new PriceDTO();
		priceBean.setTotalItemsPrice(Double.valueOf(pfForm.totalItemPrice.getValue()));
		priceBean.setDiscount(Double.valueOf(pfForm.discountPrice.getValue()));
		priceBean.setVatCharge(Double.valueOf(pfForm.vatOnNewItemPrice.getValue()));
		priceBean.setOldPurchase(Double.valueOf(pfForm.oldPurchasePrice.getValue()));
		priceBean.setNetpayableAmount(Double.valueOf(pfForm.netAmountToPay.getValue()));
		priceBean.setAdvancePaymentAmount(Double.valueOf(pfForm.advancePayment.getValue()));
		priceBean.setBalanceAmount(Double.valueOf(pfForm.balanceAmount.getValue()));
		return new TransactionDTO(
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
				isTransactionActive);
	}

	private List<GeneralTransactionItemDTO> extractGeneralTransItem() {
		List<GeneralTransactionItemDTO> generalTransBeanList = new ArrayList<>();
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
				GeneralTransactionItemDTO generalTransBean = new GeneralTransactionItemDTO(
					itemName, quantity, piecePair, weight, pricePerPiecePair, itemPrice);
				generalTransBeanList.add(generalTransBean);
			}
		}
		return generalTransBeanList;
	}

	private List<DiamondTransactionItemDTO> extractDiamondTransItem() {
		List<DiamondTransactionItemDTO> diamondTransBeanList = new ArrayList<>();
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
				DiamondTransactionItemDTO diamondTransBean = new DiamondTransactionItemDTO(
					itemName, quantity, piecePair, goldWeight, diamondWeightCarat, diamondPieceCount, certified, itemPrice);
						diamondTransBeanList.add(diamondTransBean);
			}
			
		}
		return diamondTransBeanList;
	}

	private List<SilverTransactionItemDTO> extractSilverTransItem() {
		List<SilverTransactionItemDTO> silverTransBeanList = new ArrayList<>();
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
					SilverTransactionItemDTO bean = new SilverTransactionItemDTO(
						silverItemName, quantity, piecePair, weight, makingCharge, makingChargeType, silverRate, silverItemPrice);
					silverTransBeanList.add(bean);
			}
		}
		return silverTransBeanList;
	}

	private List<GoldTransactionItemDTO> extractGoldTransItem() {
		List<GoldTransactionItemDTO> goldTransBeanList = new ArrayList<>();
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
					GoldTransactionItemDTO bean = new GoldTransactionItemDTO(
							goldItemName, goldType, quantity, piecePair, weight, makingCharge, makingChargeType, goldRate, goldItemPrice);
					goldTransBeanList.add(bean);
			}
		}
		return goldTransBeanList;
	}
}
