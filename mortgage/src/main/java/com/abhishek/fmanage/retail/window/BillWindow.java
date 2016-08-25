package com.abhishek.fmanage.retail.window;

import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.dto.SmsSettingDTO;
import com.abhishek.fmanage.retail.dto.TransactionDTO;
import com.abhishek.fmanage.retail.pdf.InvoiceGeneratorInMemory;
import com.abhishek.fmanage.retail.restclient.service.RestSmsService;
import com.abhishek.fmanage.retail.restclient.service.RestTransactionService;
import com.abhishek.fmanage.retail.sms.SmsData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class BillWindow extends Window{
	
	public static final String ESTIMATE_BILL = "Estimate Bill";
	private  TransactionDTO retailTransaction;
	private VerticalLayout windowDataVL = new VerticalLayout();
	private Embedded e ;
	private TextArea message = new TextArea("Sms Content(160 characters max)");
	private ComboBox defaultTextCombo = new ComboBox();
	
	public BillWindow(long transId, TransactionDTO retailTransaction){
		this.retailTransaction = retailTransaction;
		windowDataVL.setSizeFull();
		HorizontalLayout windowHeaderHL = getWindowHeader(transId);
		if(transId != -1L){ // existing transaction
			windowDataVL.addComponent(windowHeaderHL);
		}
		e = getEmbeddedPDF(transId, retailTransaction);
		
		setCaption("Retail Bill");
		addStyleName("mydasboardsummary");
		setModal(true);
		center();
		setResizable(false);
		setWidth("90%");
		setHeight("90%");
		setClosable(true);
		addStyleName("no-vertical-drag-hints");
		addStyleName("no-horizontal-drag-hints");
		HorizontalLayout smsLayout = getContactAndMessage();
		windowDataVL.addComponent(smsLayout);
		windowDataVL.addComponent(e);
		if(transId != -1L){ // existing transaction
			windowDataVL.setExpandRatio(windowHeaderHL, 1);
			windowDataVL.setExpandRatio(smsLayout, 3);
			windowDataVL.setExpandRatio(e, 10);
			windowDataVL.addStyleName("smsBackLayout");
		}
		setContent(windowDataVL);
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
	
	
	private HorizontalLayout getContactAndMessage()
	{
		HorizontalLayout contactMessageHL = new HorizontalLayout();
		contactMessageHL.setSizeUndefined();
		contactMessageHL.setSpacing(true);
		contactMessageHL.setWidth("100%");
		contactMessageHL.setHeight("100%");
		contactMessageHL.setMargin(true);
		contactMessageHL.addStyleName("customer-layout");
		message.setMaxLength(160);
		message.setIcon(FontAwesome.COMMENTS); //
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
            	
               String contactNumber = retailTransaction.getCustomer().getContactNumber();
                if(!StringUtils.isEmpty(contactNumber) && (contactNumber.length() == 10) && !StringUtils.isEmpty(message.getValue())){
                		SmsSettingDTO smsSetting = getSmsSetting();
                		SmsData smsData = new SmsData();
                    	smsData.setUsername(StringUtils.isEmpty(smsSetting.getSmsUserId()) ? "" : smsSetting.getSmsUserId());
                 		smsData.setPassword(StringUtils.isEmpty(smsSetting.getSmsPassword()) ? "" : smsSetting.getSmsPassword());
                 		smsData.setSenderId(StringUtils.isEmpty(smsSetting.getSmsSenderId()) ? "" : smsSetting.getSmsSenderId());
                 		smsData.setMessage(message.getValue());
                 		smsData.setFlash("0");
                 		smsData.setRoute("2");
                 		smsData.setTo(contactNumber);
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
    		            		
    		               LinkedHashMap<String, String> response = rs.postForObject(smsSetting.getSmsGatewayUrl(), request, LinkedHashMap.class);
    		               String requestStatus = response.get("responseCode");
    		               if(requestStatus.equalsIgnoreCase("SUCCESS")){
    		            	   Notification.show("MessageID: " + response.get("MessageID") + " " + response.get("responseMessage"), Notification.Type.HUMANIZED_MESSAGE);
    		               }else{
    		            	   Notification.show(response.get("responseMessage"), Notification.Type.ERROR_MESSAGE);
    		               }

    					} catch (Exception ex) {
    						Notification.show("Please check your internet connection and sms gateway url(" + ex.getLocalizedMessage() + ")", Notification.Type.ERROR_MESSAGE);
    					}
                
                }else{
            		Notification.show("Please check contact number and message", Notification.Type.HUMANIZED_MESSAGE);
                }
        }});
		
		sendSmsButton.setWidth("100%");
		sendSmsButton.addStyleName("default");
		sendSmsButton.setIcon(FontAwesome.REFRESH);
		Component compo = getDefaultTextList();
		contactMessageHL.addComponent(compo);
		contactMessageHL.addComponent(sendSmsButton);
		contactMessageHL.setExpandRatio(compo, 2);
		contactMessageHL.setExpandRatio(message, 6);
		contactMessageHL.setExpandRatio(sendSmsButton, 1);
		contactMessageHL.setComponentAlignment(compo, Alignment.MIDDLE_LEFT);
		contactMessageHL.setComponentAlignment(sendSmsButton, Alignment.MIDDLE_LEFT);
		return contactMessageHL;
	}
	
	
	private Embedded getEmbeddedPDF(long transId,
			TransactionDTO retailTransaction) {
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);

		// Here we create a new StreamResource which downloads our StreamSource,
		// which is our pdf.
		InvoiceGeneratorInMemory inmem = new InvoiceGeneratorInMemory(transId, retailTransaction);
		String suffix = new Date().toString().replace(" ", "");
		StreamResource resource = new StreamResource(inmem, "Bill_" + suffix + ".pdf");
		// Set the right mime type
		resource.setMIMEType("application/pdf");
		resource.getStream().setParameter("Content-Disposition", "attachment; filename=" + "Bill_.pdf");
		e.setType(Embedded.TYPE_BROWSER);
		e.setWidth("100%"); //
		e.setHeight("90%");
		e.setSource(resource);
		return e;
	}

	private HorizontalLayout getWindowHeader(long transId) {
		HorizontalLayout windowHeaderHL = new HorizontalLayout();
		windowHeaderHL.setWidth("100%");
		windowHeaderHL.setHeight("5%");
		windowHeaderHL.addStyleName("smsContact");
		Button actionButton = new Button("Deactivate Bill");
		actionButton.addStyleName("default");
		actionButton.setIcon(FontAwesome.AUTOMOBILE);
		actionButton.setImmediate(true);
		if(!retailTransaction.isTransactionActive()){
			actionButton.setCaption("Activate Bill");
		}
		actionButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(retailTransaction.isTransactionActive()){
					//deactivate
					Boolean updateSuccess = new RestTransactionService().updateBillStatus(transId,"I");
					if(updateSuccess){
						retailTransaction.setTransactionActive(false);
						actionButton.setCaption("Activate Bill");
						windowDataVL.replaceComponent(e, e = getEmbeddedPDF(transId, retailTransaction));
						Notification.show("Bill Deactivated");
					}else{
						 Notification.show("Bill Deactivation failed");
					}
					actionButton.setImmediate(true);
				}else{
					//activate
					Boolean updateSuccess = new RestTransactionService().updateBillStatus(transId,"A");
					if(updateSuccess){
						retailTransaction.setTransactionActive(true);
						actionButton.setCaption("Deactivate Bill");
						windowDataVL.replaceComponent(e, e = getEmbeddedPDF(transId, retailTransaction));
						
						Notification.show("Bill Activated");
					}else{
						 Notification.show("Bill activation failed");
					}
					
					actionButton.setImmediate(true);
				}
			}
		});
		Label transIdLabel = new Label("<b>Transaction No: " + transId + "</b>", ContentMode.HTML);
		windowHeaderHL.addComponent(actionButton);
		windowHeaderHL.addComponent(transIdLabel);
		return windowHeaderHL;
	}
	
	private SmsSettingDTO getSmsSetting() {
		return new RestSmsService().getSmsSetting(getShopDto().getShopId());
	}

	private ShopDTO getShopDto() {
		return (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);
	}
}
