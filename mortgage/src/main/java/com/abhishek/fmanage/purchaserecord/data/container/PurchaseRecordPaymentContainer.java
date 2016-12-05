package com.abhishek.fmanage.purchaserecord.data.container;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.abhishek.fmanage.purchaserecord.data.bean.PurchasePayment;
import com.avathartech.fastformfields.widgets.DecimalTextField;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextField;

public class PurchaseRecordPaymentContainer extends IndexedContainer implements CustomPurchaseRecordItemContainerInterface{

	private static final long serialVersionUID = 1L;
    public static final String DELETE = "Delete";
    public static final String AMOUNT = "Amount(INR)";
    public static final String CHEQUENUMBER = "Check Number";
    public static final String TRANSACTION_REFERENCE_NUMBER = "Transaction Reference No.";
    public static final String BANK_NAME = "Bank Name";
    public static final ThemeResource removeItemImageResource = new ThemeResource("img/removeButtonSmall.jpg");
    
    public PurchasePayment.PaymentType purchasePaymentType;
    
    public PurchaseRecordPaymentContainer(PurchasePayment.PaymentType purchasePaymentType)
    {
    	this.purchasePaymentType = purchasePaymentType;
        addContainerProperty(DELETE, Image.class, new Image());
        addContainerProperty(AMOUNT, DecimalTextField.class, new DecimalTextField());
        if(PurchasePayment.PaymentType.CHEQUE.equals(purchasePaymentType)){
        	addContainerProperty(CHEQUENUMBER, TextField.class, new TextField());
        }else{
        	addContainerProperty(TRANSACTION_REFERENCE_NUMBER, TextField.class, new TextField());
        }
        addContainerProperty(BANK_NAME, TextField.class, new TextField());
    }
    
    @SuppressWarnings("unchecked")
    
	public Object addCustomItem(PurchasePayment.PaymentType purchasePaymentType){
    	Object itemRowId = addItem();
		Item item = getItem(itemRowId);
		if (item != null) {
			item.getItemProperty(DELETE).setValue(getRemoveItemImage(itemRowId, purchasePaymentType));
			item.getItemProperty(AMOUNT).setValue(getAmount(itemRowId, purchasePaymentType));
			if(PurchasePayment.PaymentType.CHEQUE.equals(purchasePaymentType)){
				item.getItemProperty(CHEQUENUMBER).setValue(getTransactionReferenceNumber(itemRowId, purchasePaymentType));
			}else{
				item.getItemProperty(TRANSACTION_REFERENCE_NUMBER).setValue(getTransactionReferenceNumber(itemRowId, purchasePaymentType));
			}
			item.getItemProperty(BANK_NAME).setValue(getTransactionReferenceNumber(itemRowId, purchasePaymentType));
		}
		return itemRowId;
    }
    
    private Object getTransactionReferenceNumber(Object itemRowId, PurchasePayment.PaymentType purchasePaymentType) {
    	TextField transReferenceNumberTxt = new TextField("");
    	transReferenceNumberTxt.setData(purchasePaymentType);
    	transReferenceNumberTxt.setImmediate(true);
    	transReferenceNumberTxt.setRequired(true);
    	transReferenceNumberTxt.setValidationVisible(true);
    	transReferenceNumberTxt.setWidth("100%");
    	
    	return transReferenceNumberTxt;
		
	}

	private Object getAmount(final Object currentItemId, PurchasePayment.PaymentType purchasePaymentType) {
    	DecimalTextField amount = new DecimalTextField();
		amount.setImmediate(true);
		amount.setRequired(true);
		amount.setValidationVisible(true);
		amount.setWidth("100%");
		amount.setData(purchasePaymentType);
		amount.setValue("0.00");
		amount.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 516109943650543255L;

			public void valueChange(ValueChangeEvent event) {
				String value = (String) event.getProperty().getValue();
				if (NumberUtils.isNumber(value)) {
					amount.addStyleName("v-textfield-success");
				}else{
					amount.setValue("0.00");
				}
			}
		});
		return amount;
    }
    
	@Override
    public Double getTotalAmount() {
		double totalAmount = 0.00f;
		List<Object> itemIdsList = getAllItemIds();
		for (Object obj : itemIdsList) {
			DecimalTextField amountTxtField = (DecimalTextField) getItem(obj).getItemProperty(AMOUNT).getValue();
			String amount = amountTxtField.getValue();
			totalAmount += NumberUtils.isNumber(amount) ? NumberUtils.toDouble(amount) : 0.00f;
		}
		return totalAmount;
	}
	
    private Image getRemoveItemImage(final Object itemRowId, PurchasePayment.PaymentType purchasePaymentType) {
		final Image image = new Image("", removeItemImageResource);
		image.setHeight("20px");
		image.setWidth("20px");
		image.setDescription("Remove Item");
		image.setData(purchasePaymentType);
		image.addClickListener((event -> removeItem(itemRowId)));
		image.setImmediate(true);
		return image;
	}
}
