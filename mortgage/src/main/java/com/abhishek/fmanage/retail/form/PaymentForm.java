package com.abhishek.fmanage.retail.form;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang3.math.NumberUtils;

import com.avathartech.fastformfields.widgets.DecimalTextField;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.ui.FormLayout;

public class PaymentForm extends FormLayout{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public DecimalTextField totalCardPayment = new DecimalTextField("Card", "0.0");
	public DecimalTextField cashPayment = new DecimalTextField("Cash", "0.0");
    public DecimalTextField chequePayment = new DecimalTextField("Cheque", "0.0");
    public DecimalTextField rtgsPayment =  new DecimalTextField("Rtgs", "0.0");
    public DecimalTextField neftPayment = new DecimalTextField("Neft", "0.0");
    
    public PaymentForm(Item item){
    	addStyleName("customer-layout");
    	StringToDoubleConverter plainDoubleConverter = CustomStringToDoubleConverter();
    	totalCardPayment.setConverter(plainDoubleConverter);
    	cashPayment.setConverter(plainDoubleConverter);
    	chequePayment.setConverter(plainDoubleConverter);
    	rtgsPayment.setConverter(plainDoubleConverter);
    	neftPayment.setConverter(plainDoubleConverter);
    	
    	paymentValueChangeListener(totalCardPayment);
    	paymentValueChangeListener(cashPayment);
    	paymentValueChangeListener(chequePayment);
    	paymentValueChangeListener(rtgsPayment);
    	paymentValueChangeListener(neftPayment);
    	
    	addComponent(totalCardPayment);
    	addComponent(cashPayment);
    	addComponent(chequePayment);
    	addComponent(rtgsPayment);
    	addComponent(neftPayment);

    	// Now bind the member fields to the item
        FieldGroup binder = new FieldGroup(item);
        binder.bindMemberFields(this);
    }
    
    private void paymentValueChangeListener(DecimalTextField decimalTxt){
    	decimalTxt.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 516109943650543255L;

			public void valueChange(ValueChangeEvent event) {
				String value = (String) event.getProperty().getValue();
				if (NumberUtils.isNumber(value)) {
					decimalTxt.addStyleName("v-textfield-success");
				}else{
					decimalTxt.setValue("0.00");
				}
			}
		});
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
    
}
