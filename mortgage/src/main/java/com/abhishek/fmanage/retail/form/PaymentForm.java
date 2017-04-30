package com.abhishek.fmanage.retail.form;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang3.math.NumberUtils;

import com.abhishek.fmanage.purchaserecord.data.bean.PurchasePayment.PaymentType;
import com.avathartech.fastformfields.widgets.DecimalTextField;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

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
    private DecimalTextField payment = new DecimalTextField("Amount", "0.00");
    private ThemeResource resource = new ThemeResource("img/addButtonSmall.jpg");
    
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
    	
    	HorizontalLayout cardPaymentLayoutHL = new HorizontalLayout();
    	cardPaymentLayoutHL.setSpacing(true);
    	cardPaymentLayoutHL.addComponent(totalCardPayment);
    	cardPaymentLayoutHL.addComponent(getAddImageBtn(PaymentType.CREDITCARD));
    	addComponent(cardPaymentLayoutHL);
      	//addComponent(totalCardPayment);
  
    	HorizontalLayout cashPaymentLayoutHL = new HorizontalLayout();
    	cashPaymentLayoutHL.setSpacing(true);
    	cashPaymentLayoutHL.addComponent(cashPayment);
    	cashPaymentLayoutHL.addComponent(getAddImageBtn(PaymentType.CASH));
    	addComponent(cashPaymentLayoutHL);
       	//addComponent(cashPayment);
    
    	HorizontalLayout chequePaymentLayoutHL = new HorizontalLayout();
    	chequePaymentLayoutHL.setSpacing(true);
    	chequePaymentLayoutHL.addComponent(chequePayment);
    	chequePaymentLayoutHL.addComponent(getAddImageBtn(PaymentType.CHEQUE));
    	addComponent(chequePaymentLayoutHL);
    	//addComponent(chequePayment);
    	
    	HorizontalLayout rtgsPaymentLayoutHL = new HorizontalLayout();
    	rtgsPaymentLayoutHL.setSpacing(true);
    	rtgsPaymentLayoutHL.addComponent(rtgsPayment);
    	rtgsPaymentLayoutHL.addComponent(getAddImageBtn(PaymentType.RTGS));
    	addComponent(rtgsPaymentLayoutHL);
    	//addComponent(rtgsPayment);
    	
    	HorizontalLayout neftPaymentLayoutHL = new HorizontalLayout();
    	neftPaymentLayoutHL.setSpacing(true);
    	neftPaymentLayoutHL.addComponent(neftPayment);
    	neftPaymentLayoutHL.addComponent(getAddImageBtn(PaymentType.NEFT));
    	addComponent(neftPaymentLayoutHL);
    	//addComponent(neftPayment);

    	// Now bind the member fields to the item
        FieldGroup binder = new FieldGroup(item);
        binder.bindMemberFields(this);
    }
    
    private Image getAddImageBtn(PaymentType paymentType) {
    	Image image = new Image("", resource);
        image.setHeight("20px");
        image.setWidth("20px");
        image.setDescription("Add Payment");
        image.addClickListener((event -> openAddAmountWindow(paymentType)));
       	return image;
	}
    
    private Window openAddAmountWindow(PaymentType paymentType) {
    	Window bw = new Window(getWindowname(paymentType));
        bw.setModal(true);
        bw.setWidth("35%");
        bw.setHeight("30%");
        bw.setResizable(false);
        bw.setClosable(true);
 		bw.setPosition(700, 200);
 		bw.addStyleName("no-vertical-drag-hints");
 		bw.addStyleName("no-horizontal-drag-hints");
 		
 		
 		VerticalLayout vl = new VerticalLayout();
 		vl.setSpacing(true);
 		vl.addComponent(payment);
 		HorizontalLayout hl = new HorizontalLayout();
 		Button saveBtn = new Button("Save");
 		saveBtn.setSizeUndefined();
 		saveBtn.setImmediate(true);
 		saveBtn.addStyleName("default");
 		saveBtn.setData(paymentType);
 		saveBtn.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				double amt = NumberUtils.isNumber(payment.getValue()) ? Double.valueOf(payment.getValue()) : 0.00;
				PaymentType pType = (PaymentType) saveBtn.getData();
				if(pType == PaymentType.CREDITCARD){
					totalCardPayment.setValue(String.valueOf(Double.valueOf(totalCardPayment.getValue()) + amt));
				}
				if(pType == PaymentType.CASH){
					cashPayment.setValue(String.valueOf(Double.valueOf(cashPayment.getValue()) + amt));
				}
				if(pType == PaymentType.CHEQUE){
					chequePayment.setValue(String.valueOf(Double.valueOf(chequePayment.getValue()) + amt));
				}
				if(pType == PaymentType.NEFT){
					neftPayment.setValue(String.valueOf(Double.valueOf(neftPayment.getValue()) + amt));
				}
				if(pType == PaymentType.RTGS){
					rtgsPayment.setValue(String.valueOf(Double.valueOf(rtgsPayment.getValue()) + amt));
				}
				payment.setValue("0.00");
				UI.getCurrent().removeWindow(bw);
			}
 		});
 		
 		Button cancelBtn = new Button("Cancel");
 		cancelBtn.setSizeUndefined();
 		cancelBtn.setImmediate(true);
 		cancelBtn.addStyleName("default");
 		cancelBtn.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().removeWindow(bw);
			}
 		});
 		hl.setSpacing(true);
 		hl.addComponent(saveBtn);
 		hl.addComponent(cancelBtn);
 		vl.addComponent(hl);
 		bw.setContent(vl);
        UI.getCurrent().addWindow(bw);
 		bw.focus();
 		return bw;
	}

	private String getWindowname(PaymentType paymentType) {
		if(paymentType == PaymentType.CREDITCARD){
			return "CARD";
		}
		if(paymentType == PaymentType.CASH){
			return "CASH";
		}
		if(paymentType == PaymentType.CHEQUE){
			return "CHEQUE";
		}
		if(paymentType == PaymentType.NEFT){
			return "NEFT";
		}
		if(paymentType == PaymentType.RTGS){
			return "RTGS";
		}
		return "Payment";
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
