package com.abhishek.fmanage.retail.window;

import java.util.Date;

import com.abhishek.fmanage.retail.dto.TransactionDTO;
import com.abhishek.fmanage.retail.pdf.InvoiceGeneratorInMemory;
import com.abhishek.fmanage.retail.restclient.service.RestTransactionService;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class BillWindow extends Window{
	
	public static final String ESTIMATE_BILL = "Estimate Bill";
	private  TransactionDTO retailTransaction;
	private VerticalLayout windowDataVL = new VerticalLayout();
	private Embedded e ;
	public BillWindow(long transId, TransactionDTO retailTransaction){
		this.retailTransaction = retailTransaction;
		windowDataVL.setSizeFull();
		HorizontalLayout windowHeaderHL = getWindowHeader(transId);
		if(transId != -1L){ // existing transaction
			windowDataVL.addComponent(windowHeaderHL);
		}
		e = getEmbeddedPDF(transId, retailTransaction);
		
		setCaption("Retail Bill");
		setModal(true);
		center();
		setResizable(false);
		setWidth("90%");
		setHeight("90%");
		setClosable(true);
		addStyleName("no-vertical-drag-hints");
		addStyleName("no-horizontal-drag-hints");
		windowDataVL.addComponent(e);
		if(transId != -1L){ // existing transaction
			windowDataVL.setExpandRatio(windowHeaderHL, 1);
			windowDataVL.setExpandRatio(e, 12);
		}
		setContent(windowDataVL);
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
		e.setWidth("100%");
		e.setHeight("90%");
		e.setSource(resource);
		return e;
	}

	private HorizontalLayout getWindowHeader(long transId) {
		HorizontalLayout windowHeaderHL = new HorizontalLayout();
		windowHeaderHL.setWidth("100%");
		windowHeaderHL.setHeight("5%");
		windowHeaderHL.addStyleName("sidebar");
		Button actionButton = new Button("Deactivate Bill");
		actionButton.addStyleName("sidebar");
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
		windowHeaderHL.addComponent(actionButton);
		return windowHeaderHL;
	}
}
