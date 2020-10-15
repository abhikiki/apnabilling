package com.abhishek.fmanage.retail.views;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.abhishek.fmanage.retail.RetailBillingType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.dto.SmsSettingDTO;
import com.abhishek.fmanage.retail.restclient.service.RestRetailSmsService;
import com.abhishek.fmanage.retail.sms.SmsData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.thirdparty.guava.common.collect.Iterables;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SmsView extends VerticalLayout implements View{

	private VerticalLayout retailViewVerticalLayout = new VerticalLayout();
	private TextField smsUserId = new TextField("Sms User Id");
	private PasswordField password = new PasswordField("Password");
	private TextField senderId = new TextField("Sms Sender Id");
	private TextField smsGatewayUrl = new TextField("Sms Gateway Url");
	private TextArea message = new TextArea("Sms Content(160 characters max)");
	private TextField customContactNumber = new TextField("Contact Number");
	private TwinColSelect twinCol = new TwinColSelect();
	private ComboBox defaultTextCombo = new ComboBox();
	//private ComboBox billingTypeCombo = new ComboBox("Billing Type");
	
	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();
		addStyleName("smsMessage");
		retailViewVerticalLayout.addComponent(getToolbar());
		retailViewVerticalLayout.addComponent(getSmsCredentials());
		retailViewVerticalLayout.addComponent(getCustomerContact());
		retailViewVerticalLayout.addComponent(getMessage());
		retailViewVerticalLayout.addStyleName("smsBackLayout");
		addComponent(retailViewVerticalLayout);
	}
	
	private ComboBox getDefaultTextList() {
		defaultTextCombo = new ComboBox("Default Sms");
		defaultTextCombo.setNullSelectionAllowed(false);
		defaultTextCombo.addValueChangeListener(defaultTextValueChangeListener());
		defaultTextCombo.addItem(" ");
		defaultTextCombo.addItem("Thanks for your visit to Apna Jewellery. Call or Whatsapp on 9693916916 for any query.");
		defaultTextCombo.addItem("Dear Customer, Apna Jewellery  would like to grow online through your clicks on https://www.facebook.com/apnajewelleryshop Whatsapp no. is 9693916916");
		defaultTextCombo.setValue(" ");
		defaultTextCombo.setWidth("90%");
		return defaultTextCombo;
	}
	
	private ValueChangeListener defaultTextValueChangeListener(){
		
		return new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				message.setValue(defaultTextCombo.getValue().toString());
			}
		};
	}
	
	private HorizontalLayout getCustomerContact(){
		HorizontalLayout customerContactHL = new HorizontalLayout();
		customerContactHL.setSizeUndefined();
		customerContactHL.setWidth("100%");
		customerContactHL.setHeight("100%");
		customerContactHL.addStyleName("customer-layout");
		customerContactHL.setSpacing(true);
		customerContactHL.setMargin(true);
		
		twinCol.setSizeUndefined();
		twinCol.setWidth("100%");
		twinCol.setHeight("100%");
		twinCol.setLeftColumnCaption("Available Contact Numbers");
		twinCol.setRightColumnCaption("Selected Contact Numbers");
		twinCol.setMultiSelect(true);
		//String billingTypeName = billingTypeCombo.getValue().toString();
		//RetailBillingType retailBillingType = "RetailBilling".equalsIgnoreCase(billingTypeName) ? RetailBillingType.retailbillingtype : RetailBillingType.registeredCustomertype;
		ShopDTO shopDto =  (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);
		List<String> contactList = new RestRetailSmsService(shopDto, null).getCustomerContact(getShopDto().getShopId());
		twinCol.addItems(contactList);
		twinCol.setImmediate(true);
		twinCol.setRows(15);
		Button addContactBtn = new Button("Add Contact", new ClickListener()
        {
            private static final long serialVersionUID = 1L;

            @Override
            
            public void buttonClick(ClickEvent event)
            {
            	String number = customContactNumber.getValue();
                if(!StringUtils.isEmpty(number) && NumberUtils.isDigits(number)){
                	if(contactList.contains(number)){
                		Notification.show("Number already exists in the list");
                	}else{
                		twinCol.addItem(number);
                		Notification.show("Number Added. Please move it to selected list");
                	}
                }
            }
        });
		
		addContactBtn.setWidth("100%");
		addContactBtn.addStyleName("default");
		addContactBtn.setIcon(FontAwesome.PLUS);

		
		customerContactHL.addComponent(twinCol);
		customerContactHL.addComponent(customContactNumber);
		customerContactHL.addComponent(addContactBtn);
		customerContactHL.setExpandRatio(twinCol, 4);
		customerContactHL.setExpandRatio(customContactNumber, 2f);
		customerContactHL.setExpandRatio(addContactBtn, 1);
		customerContactHL.setComponentAlignment(customContactNumber, Alignment.MIDDLE_LEFT);
		customerContactHL.setComponentAlignment(addContactBtn, Alignment.MIDDLE_LEFT);
		return customerContactHL;
	}
	
	private HorizontalLayout getSmsCredentials(){
		HorizontalLayout smsCredential = new HorizontalLayout();
		smsCredential.addStyleName("mydasboardsummary");
		smsCredential.setWidth("100%");
		smsCredential.setSpacing(true);
		smsCredential.setMargin(true);
		smsUserId.setIcon(FontAwesome.USER);
		SmsSettingDTO smsSetting = getSmsSetting();
		smsUserId.setValue(StringUtils.isEmpty(smsSetting.getSmsUserId()) ? "" : smsSetting.getSmsUserId());
		smsCredential.addComponent(smsUserId);
		password.setIcon(FontAwesome.LOCK);
		password.setValue(StringUtils.isEmpty(smsSetting.getSmsPassword()) ? "" : smsSetting.getSmsPassword());
		smsCredential.addComponent(password);
		senderId.setIcon(FontAwesome.SEND);
		senderId.setValue(StringUtils.isEmpty(smsSetting.getSmsSenderId()) ? "" : smsSetting.getSmsSenderId());
		smsCredential.addComponent(senderId);
		smsGatewayUrl.setIcon(FontAwesome.LINK);
		smsGatewayUrl.setSizeFull();
		
		smsGatewayUrl.setWidth("67%");
		smsGatewayUrl.setValue(StringUtils.isEmpty(smsSetting.getSmsGatewayUrl()) ? "" : smsSetting.getSmsGatewayUrl());
		
		final Button updateSmsSettingBtn = new Button("Update");
	    updateSmsSettingBtn.addStyleName("icon-search-1");
	    updateSmsSettingBtn.addStyleName("default");
	    updateSmsSettingBtn.addClickListener(new ClickListener()
	    	{
	            private static final long serialVersionUID = 1L;

	            @Override
	            public void buttonClick(ClickEvent event)
	            {
				//	String billingTypeName = billingTypeCombo.getValue().toString();
				//	RetailBillingType retailBillingType = "RetailBilling".equalsIgnoreCase(billingTypeName) ? RetailBillingType.retailbillingtype : RetailBillingType.registeredCustomertype;
	            	SmsSettingDTO smsSettingdto = new SmsSettingDTO();
	            	smsSettingdto.setSmsUserId(StringUtils.isEmpty(smsUserId.getValue()) ? "" : smsUserId.getValue());
	            	smsSettingdto.setSmsPassword(StringUtils.isEmpty(password.getValue()) ? "" : password.getValue());
	            	smsSettingdto.setSmsSenderId(StringUtils.isEmpty(senderId.getValue()) ? "" : senderId.getValue());
	            	smsSettingdto.setSmsGatewayUrl(StringUtils.isEmpty(smsGatewayUrl.getValue()) ? "" : smsGatewayUrl.getValue());
	            	new RestRetailSmsService(getShopDto(), null).updateSmsSetting(getShopDto().getShopId(), smsSettingdto);
	            }
	        });
		smsCredential.addComponent(smsGatewayUrl);
		smsCredential.addComponent(updateSmsSettingBtn);
		smsCredential.setExpandRatio(smsUserId, 1);
		smsCredential.setExpandRatio(password, 1);
		smsCredential.setExpandRatio(senderId, 1);
		smsCredential.setExpandRatio(smsGatewayUrl, 3);
		smsCredential.setExpandRatio(updateSmsSettingBtn, 1);
		return smsCredential;
	}

	private SmsSettingDTO getSmsSetting() {
		//String billingTypeName = billingTypeCombo.getValue().toString();
		//RetailBillingType retailBillingType = "RetailBilling".equalsIgnoreCase(billingTypeName) ? RetailBillingType.retailbillingtype : RetailBillingType.registeredCustomertype;
		return new RestRetailSmsService(getShopDto(), null).getSmsSetting(getShopDto().getShopId());
	}

	private ShopDTO getShopDto() {
		return (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);
	}
	
	private HorizontalLayout getMessage()
	{
		HorizontalLayout contactMessageHL = new HorizontalLayout();
		contactMessageHL.setSizeUndefined();
		contactMessageHL.setSpacing(true);
		contactMessageHL.addStyleName("smsMessage");
		contactMessageHL.setWidth("100%");
		contactMessageHL.setHeight("90%");
		contactMessageHL.setMargin(true);
		message.setMaxLength(160);
		message.setIcon(FontAwesome.COMMENTS);
		message.setSizeUndefined();
		message.setHeight("100%");
		message.setWidth("100%");
		contactMessageHL.addComponent(message);
		
		Button sendSmsButton = new Button("Send Sms", new ClickListener()
        {
            private static final long serialVersionUID = 1L;

            @Override
            
            public void buttonClick(ClickEvent event)
            {
            	
                Set<String> selectedContactNumberSet = (Set<String>) twinCol.getValue();
                if(!selectedContactNumberSet.isEmpty() && !StringUtils.isEmpty(message.getValue())){
                	for (List<String> partition : Iterables.partition(selectedContactNumberSet, 500)) {
                		String contactNumberList = String.join(",", partition);
                		SmsData smsData = new SmsData();
                    	smsData.setUsername(smsUserId.getValue());
                 		smsData.setPassword(password.getValue());
                 		smsData.setSenderId(senderId.getValue());
                 		smsData.setMessage(message.getValue());
                 		smsData.setFlash("0");
                 		smsData.setRoute("2");
                 		smsData.setTo(contactNumberList);
                 		smsData.setNonenglish("0");
                 		smsData.setSendDate("");
                 		
                 		ObjectMapper mapper = new ObjectMapper();
                 		String jsonInString;
    					try {
    						jsonInString = mapper.writeValueAsString(smsData);
    						RestTemplate rs = new RestTemplate();
    		                HttpHeaders headers = new HttpHeaders();
    		                headers.setContentType(MediaType.APPLICATION_JSON);
    		                HttpEntity<String> entity = new HttpEntity<String>(headers);
    		                HttpEntity request= new HttpEntity( jsonInString, headers );
    		            		
    		               LinkedHashMap<String, String> response = rs.postForObject(smsGatewayUrl.getValue(), request, LinkedHashMap.class);
    		               String requestStatus = response.get("responseCode");
    		               if(requestStatus.equalsIgnoreCase("SUCCESS")){
    		            	   Notification.show("MessageID: " + response.get("MessageID") + " " + response.get("responseMessage"), Notification.Type.HUMANIZED_MESSAGE);
    		               }else{
    		            	   Notification.show(response.get("responseMessage"), Notification.Type.ERROR_MESSAGE);
    		            	   break;
    		               }

    					} catch (Exception ex) {
    						Notification.show("Please check your internet connection and sms gateway url(" + ex.getLocalizedMessage() + ")", Notification.Type.ERROR_MESSAGE);
    						break;
    					}

                	}
                	            		
                }else{
                	Notification.show("Check you have selected the numbers and your sms message is not empty", Notification.Type.ERROR_MESSAGE);
                }
            }

			
        });
		
		sendSmsButton.setWidth("100%");
		sendSmsButton.addStyleName("default");
		sendSmsButton.setIcon(FontAwesome.REFRESH);
		Component compo = getDefaultTextList();
		contactMessageHL.addComponent(compo);
		contactMessageHL.addComponent(sendSmsButton);
		contactMessageHL.setExpandRatio(message, 6);
		contactMessageHL.setExpandRatio(compo, 2);
		contactMessageHL.setExpandRatio(sendSmsButton, 1);
		contactMessageHL.setComponentAlignment(compo, Alignment.MIDDLE_LEFT);
		contactMessageHL.setComponentAlignment(sendSmsButton, Alignment.MIDDLE_LEFT);
		return contactMessageHL;
	}
	private HorizontalLayout getToolbar() {
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setWidth("100%");
		toolbar.setSpacing(true);
		toolbar.setMargin(true);
		toolbar.addStyleName("mytoolbar");
		Label titleLabel = new Label("Short Message Service");
	    titleLabel.setId("crm-title");
	    titleLabel.setSizeUndefined();
	    titleLabel.addStyleName(ValoTheme.LABEL_H1);
	    titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	    toolbar.addComponent(titleLabel);

//        billingTypeCombo.addItem("RetailBilling");
//		billingTypeCombo.addItem("RegisteredBilling");
//		billingTypeCombo.setValue("RetailBilling");
//		billingTypeCombo.setNullSelectionAllowed(false);
		//toolbar.addComponent(billingTypeCombo);

		return toolbar;
	}
}
