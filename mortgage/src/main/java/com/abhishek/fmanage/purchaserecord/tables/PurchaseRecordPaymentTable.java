package com.abhishek.fmanage.purchaserecord.tables;

import com.abhishek.fmanage.mortgage.data.container.MortgageItemContainer;
import com.abhishek.fmanage.purchaserecord.data.bean.PurchasePayment;
import com.abhishek.fmanage.purchaserecord.data.container.PurchaseRecordPaymentContainer;
import com.vaadin.ui.Table;

public class PurchaseRecordPaymentTable extends Table{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public PurchaseRecordPaymentTable(PurchaseRecordPaymentContainer paymentContainer, PurchasePayment.PaymentType paymentType){
		setSizeFull();
		addStyleName("borderless");
		setSelectable(false);
		setColumnCollapsingAllowed(false);
		setColumnReorderingAllowed(true);
		setWidth("80%");
		setContainerDataSource(paymentContainer);
		addStyleName("diamond-table");
		addStyleName("diamond-table-header");
		addStyleName("diamond-table-footer");
		addStyleName("diamond-table-footer-container");
		setFooterVisible(true);
		setMultiSelect(false);
		setPageLength(0);
		setColumnWidth(PurchaseRecordPaymentContainer.DELETE, 70);
		setColumnWidth(PurchaseRecordPaymentContainer.AMOUNT, 180);
		setColumnWidth(PurchaseRecordPaymentContainer.BANK_NAME, 300);
		if(PurchasePayment.PaymentType.CHEQUE == paymentType){
			setColumnWidth(PurchaseRecordPaymentContainer.CHEQUENUMBER, 220);
		}else{
			setColumnWidth(PurchaseRecordPaymentContainer.TRANSACTION_REFERENCE_NUMBER, 220);
		}
		setColumnFooter(MortgageItemContainer.DELETE, ("Items=" + size()));
	}
}
