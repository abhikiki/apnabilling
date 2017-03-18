/**
 * 
 */
package com.abhishek.fmanage.retail.form;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.abhishek.fmanage.csv.utility.CustomShopSettingFileUtility;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * @author guptaa6
 *
 */
public class PriceForm extends FormLayout{

	private static final long serialVersionUID = -5016984459457685827L;
	public TextField totalItemPrice = new TextField("Sales Price", "0.0");
	public TextField vatOnNewItemPrice = new TextField("Vat(" + String.valueOf(CustomShopSettingFileUtility.getInstance().getVatPercentage()) + "%)", "0.0");
    public TextField oldPurchasePrice = new TextField("Old Purchase Price(-)", "0.0");
    public TextField discountPrice =  new TextField("Discount(-)", "0.0");
    public TextField netAmountToPay = new TextField("Net Payable Price", "0.0");
    public TextField advancePayment = new TextField("Advance Payment", "0.0");
    public TextField balanceAmount = new TextField("Balance", "0.0");
    public boolean isInvoiceEnabled = false;

    public PriceForm(Item item){
    	addStyleName("customer-layout");
    	StringToDoubleConverter plainDoubleConverter = CustomStringToDoubleConverter();
    	totalItemPrice.setConverter(plainDoubleConverter);
    	vatOnNewItemPrice.setConverter(plainDoubleConverter);
    	oldPurchasePrice.setConverter(plainDoubleConverter);
    	oldPurchasePrice.setIcon(FontAwesome.EDIT);
    	discountPrice.setConverter(plainDoubleConverter);
    	discountPrice.setIcon(FontAwesome.EDIT);
    	netAmountToPay.setConverter(plainDoubleConverter);
    	advancePayment.setConverter(plainDoubleConverter);
    	advancePayment.setIcon(FontAwesome.EDIT);
    	advancePayment.setEnabled(true);
		advancePayment.setIcon(FontAwesome.EDIT);
    	balanceAmount.setConverter(plainDoubleConverter);
    	addComponent(totalItemPrice);
    	addComponent(discountPrice);
    	addComponent(vatOnNewItemPrice);
    	addComponent(oldPurchasePrice);
    	addComponent(netAmountToPay);
    	addComponent(advancePayment);
    	addComponent(balanceAmount);
    	// Now bind the member fields to the item
        FieldGroup binder = new FieldGroup(item);
        binder.bindMemberFields(this);
        customNumericValidator(totalItemPrice);
        customNumericValidator(discountPrice);
        customNumericValidator(vatOnNewItemPrice);
        customNumericValidator(oldPurchasePrice);
        customNumericValidator(netAmountToPay);
        customNumericValidator(advancePayment);
        customNumericValidator(balanceAmount);
        
        netAmountToPay.setEnabled(false);
        netAmountToPay.addStyleName("visible");
       
        totalItemPrice.setEnabled(false);;
        totalItemPrice.setStyleName("visible");
        vatOnNewItemPrice.setEnabled(false);
        vatOnNewItemPrice.setStyleName("visible");
        balanceAmount.setEnabled(false);
        balanceAmount.setStyleName("visible");
        
    	totalItemPrice.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 5320925689852078782L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				vatOnNewItemPrice.setValue(String.format("%.2f", getVatPrice()));
				netAmountToPay.setValue(String.format("%.2f", getTotalNetAmount()));
			}
		});
    	
    	netAmountToPay.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 5320925689852078782L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				balanceAmount.setValue(String.format("%.2f", getBalanceAmount()));
			}
		});
    	
    	advancePayment.setValidationVisible(true);
    	advancePayment.addValidator(new DoubleRangeValidator("Must be number", 0.000, null));
    	advancePayment.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 8724248055526259500L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(NumberUtils.isNumber(getNumericTextValue(advancePayment))){
					balanceAmount.setValue(String.format("%.2f", getBalanceAmount()));
					balanceAmount.removeStyleName("v-textfield-fail");
					advancePayment.removeStyleName("v-textfield-fail");
					advancePayment.setComponentError(null);
				}else{
					balanceAmount.clear();
					balanceAmount.addStyleName("v-textfield-fail");
					advancePayment.addStyleName("v-textfield-fail");
				}
			}
		});

    	
    	oldPurchasePrice.setValidationVisible(true);
    	oldPurchasePrice.addValidator(new DoubleRangeValidator("Must be number", 0.000, null));
    	oldPurchasePrice.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 8724248055526259500L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(NumberUtils.isNumber(getNumericTextValue(oldPurchasePrice))){
					netAmountToPay.setValue(String.format("%.2f", getTotalNetAmount()));
					netAmountToPay.removeStyleName("v-textfield-fail");
					oldPurchasePrice.removeStyleName("v-textfield-fail");
					oldPurchasePrice.setComponentError(null);
					//oldPurchasePrice.setValue(getNumericTextValue(oldPurchasePrice));
				}else{
					netAmountToPay.clear();
					netAmountToPay.addStyleName("v-textfield-fail");
					oldPurchasePrice.addStyleName("v-textfield-fail");
					//oldPurchasePrice.setValue("0.0");
				}
				
			}
		});
    	discountPrice.addValidator(new DoubleRangeValidator("Must be number", 0.000, null));
    	discountPrice.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 8700432162173289060L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(NumberUtils.isNumber(getNumericTextValue(discountPrice))){
					vatOnNewItemPrice.setValue(String.format("%.2f", getVatPrice()));
					netAmountToPay.setValue(String.format("%.2f", getTotalNetAmount()));
					netAmountToPay.removeStyleName("v-textfield-fail");
					discountPrice.removeStyleName("v-textfield-fail");
					discountPrice.setComponentError(null);
				}else{
					netAmountToPay.clear();
					netAmountToPay.addStyleName("v-textfield-fail");
					discountPrice.addStyleName("v-textfield-fail");
				}
				
			}
		});
    	
    }

    private Double getBalanceAmount() {
		Double netAmount = getTotalNetAmount();
		Double balance = netAmount - Double.valueOf((getNumericTextValue(advancePayment)));
		return balance;
	}
    
	private StringToDoubleConverter CustomStringToDoubleConverter() {
		
		return new StringToDoubleConverter() {
 			private static final long serialVersionUID = -2654779837579321367L;
 			 @Override
 		    public Double convertToModel(String value,
 		            Class<? extends Double> targetType, Locale locale)
 		            throws ConversionException {
 		        Number n = convertToNumber(value, targetType, locale);
 		        return n == null ? 0.00f : n.doubleValue();
 		    }
 			
			protected java.text.NumberFormat getFormat(Locale locale) {
    	        NumberFormat format = super.getFormat(locale);
    	        format.setGroupingUsed(false);
    	        //format.
    	        return format;
    	    };
    	};
	}

    private void customNumericValidator(TextField field){
    	field.addValidator(
    		(value) -> {
    			if(!NumberUtils.isNumber(String.valueOf(value))){
    				field.addStyleName("v-textfield-fail");
    			}else{
    				field.setComponentError(null);
    				field.removeStyleName("v-textfield-fail");
    		}
    	});		
    }
   
	public Double getTotalNetAmount() {
		Double totalNetAmount = Double.valueOf((getNumericTextValue(totalItemPrice)))
			+ Double.valueOf((getNumericTextValue(vatOnNewItemPrice)))
			- Double.valueOf((getNumericTextValue(oldPurchasePrice)))
			- Double.valueOf((getNumericTextValue(discountPrice)));
		return  Math.round(totalNetAmount * 100.0) / 100.0;
	}
	
	private String getNumericTextValue(TextField numericField) {
		String value = StringUtils.remove(numericField.getValue(), ","); 
		if(StringUtils.isEmpty(value)){
			return "0.00";
		}else{
			return value;
		}
	}
	
	public double getVatPrice(){
		double vatPrice = 0.00f;
		if(isInvoiceEnabled)
		{
			double discountedPrice = Double.valueOf(getNumericTextValue(totalItemPrice)) - Double.valueOf(getNumericTextValue(discountPrice));
			//vatPrice = (1 * discountedPrice)/ 100.0;
			vatPrice = (CustomShopSettingFileUtility.getInstance().getVatPercentage() * discountedPrice)/ 100.0;
		}
		return  Math.round(vatPrice * 100.0) / 100.0;
	}
}
