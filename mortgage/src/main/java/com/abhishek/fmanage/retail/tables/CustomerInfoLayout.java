package com.abhishek.fmanage.retail.tables;

import com.abhishek.fmanage.retail.dto.CustomerDTO;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

public class CustomerInfoLayout {
	private CustomerDTO cusBean;
	private final BeanFieldGroup<CustomerDTO> binder = new BeanFieldGroup<CustomerDTO>(CustomerDTO.class);
	public CustomerInfoLayout(CustomerDTO cusBean){
		this.cusBean = cusBean;
	}
	
	public Component getUserDetailFormLayout() {

		FormLayout personDetailFormlayout1 = new FormLayout();
		FormLayout personDetailFormlayout2 = new FormLayout();
		FormLayout personDetailFormlayout3 = new FormLayout();

		binder.setItemDataSource(cusBean);
		binder.setBuffered(false);

		personDetailFormlayout1.setEnabled(true);
		personDetailFormlayout1.addComponent(binder.buildAndBind("FirstName",
				"firstName"));
		personDetailFormlayout1.addComponent(binder.buildAndBind("LastName",
				"lastName"));
		personDetailFormlayout1.addComponent(binder.buildAndBind(
				"ContactNumber", "contactNumber"));
		personDetailFormlayout1.addComponent(binder.buildAndBind("EmailId",
				"emailId"));

		personDetailFormlayout2.addComponent(binder.buildAndBind(
				"Street Address1", "streetAddress1"));
		personDetailFormlayout2.addComponent(binder.buildAndBind(
				"Street Address2", "streetAddress2"));
		personDetailFormlayout2.addComponent(binder
				.buildAndBind("City", "city"));
		personDetailFormlayout2.addComponent(binder.buildAndBind("State",
				"stateprovince"));

		personDetailFormlayout3.addComponent(binder.buildAndBind("Zipcode",
				"zipcode"));
		personDetailFormlayout3.addComponent(binder.buildAndBind("Country",
				"country"));
		personDetailFormlayout1.setImmediate(true);
		personDetailFormlayout2.setImmediate(true);
		personDetailFormlayout3.setImmediate(true);
		personDetailFormlayout1.setData(binder);
		personDetailFormlayout2.setData(binder);
		personDetailFormlayout3.setData(binder);

		binder.setBuffered(false);

		HorizontalLayout customerInformationLayout = new HorizontalLayout();
		customerInformationLayout.addComponent(personDetailFormlayout1);
		customerInformationLayout.addComponent(personDetailFormlayout2);
		customerInformationLayout.addComponent(personDetailFormlayout3);
		customerInformationLayout.setExpandRatio(personDetailFormlayout1, 1);
		customerInformationLayout.setExpandRatio(personDetailFormlayout2, 1);
		customerInformationLayout.setExpandRatio(personDetailFormlayout3, 1);
		customerInformationLayout.setSpacing(true);
		customerInformationLayout.setWidth("100%");
		customerInformationLayout.addStyleName("customer-layout");

		Panel customerInformationPanel = new Panel();
		customerInformationPanel.setWidth("100%");
		customerInformationPanel.setContent(customerInformationLayout);

		customerInformationPanel
				.setCaption("<font size=\"2\" color=\"green\"><b>Customer Information</b></font>");
		customerInformationPanel.setCaptionAsHtml(true);
		return customerInformationPanel;
	}
	
	public void setCustomerDataSource(CustomerDTO cusBean){
		this.binder.setItemDataSource(cusBean);
	}
}
