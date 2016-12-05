package com.abhishek.fmanage.retail.views;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.abhishek.fmanage.mortgage.data.container.MortgageItemContainer;
import com.abhishek.fmanage.mortgage.data.container.MortgageItemType;
import com.abhishek.fmanage.mortgage.dto.MortgageTransactionDTO;
import com.abhishek.fmanage.mortgage.tables.MortgageItemTable;
import com.abhishek.fmanage.purchaserecord.data.bean.PurchasePayment.PaymentType;
import com.abhishek.fmanage.purchaserecord.data.container.PurchaseRecordPaymentContainer;
import com.abhishek.fmanage.purchaserecord.tables.PurchaseRecordPaymentTable;
import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.restclient.service.RestMortgageTransactionService;
import com.avathartech.fastformfields.widgets.DecimalTextField;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.MouseEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public class PurchaseRecordView extends VerticalLayout implements View {

	public static final String INDIAN_DATE_FORMAT = "dd/MM/yyyy";
	public static final String SELECT_DATE = "Select Date";
	private final PopupDateField billPopUpDate = new PopupDateField();
	private final TextField partyNameTxt = new TextField("Party Name");
	private final TextField itemNameTxt = new TextField("Item Name");
	private final DecimalTextField weightTxt = new DecimalTextField("Weight(gms)");
	private VerticalLayout paymentViewVerticalLayout = new VerticalLayout();
	private Table workingTable = new Table();
	//
	private Table checkPaymentTable = new Table();
	private Table rtgsPaymentTable = new Table();
	private Table neftPaymentTable = new Table();
	private Table debitCardPaymentTable = new Table();
	private Table creditCardPaymentTable = new Table();
	private Table otherModePaymentTable = new Table();
	private TextArea notes = new TextArea("Purchase Notes");
	private Button saveBtn = new Button("Save");
	private DecimalTextField cashPaymentTxt = new DecimalTextField("Cash Payment");

	//
	private PurchaseRecordPaymentContainer chequeContainer = new PurchaseRecordPaymentContainer(
			PaymentType.CHEQUE);
	private PurchaseRecordPaymentContainer rtgsContainer = new PurchaseRecordPaymentContainer(
			PaymentType.RTGS);
	private PurchaseRecordPaymentContainer neftContainer = new PurchaseRecordPaymentContainer(
			PaymentType.NEFT);
	private PurchaseRecordPaymentContainer creditCardContainer = new PurchaseRecordPaymentContainer(
			PaymentType.CREDITCARD);
	private PurchaseRecordPaymentContainer debitCardContainer = new PurchaseRecordPaymentContainer(
			PaymentType.DEBITCARD);
	private PurchaseRecordPaymentContainer otherModeContainer = new PurchaseRecordPaymentContainer(
			PaymentType.OTHER);

	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();
		HorizontalLayout toolbarLayout = new HorizontalLayout();
		toolbarLayout.addComponent(getToolbar());
		toolbarLayout.setSizeUndefined();
		toolbarLayout.setSizeFull();
		toolbarLayout.addStyleName("mytoolbar");
		//addComponent(toolbarLayout);

		//cash
		
		paymentViewVerticalLayout.addComponent(cashPaymentTxt);
		//cheque
		checkPaymentTable = new PurchaseRecordPaymentTable(chequeContainer,
				PaymentType.CHEQUE);
		VerticalLayout checkPaymentLayout = getPaymentLayout(checkPaymentTable,
				chequeContainer, PaymentType.CHEQUE);
		checkPaymentLayout.setSpacing(true);
		paymentViewVerticalLayout.addComponent(checkPaymentLayout);
		//neft
		neftPaymentTable = new PurchaseRecordPaymentTable(neftContainer,
				PaymentType.NEFT);
		VerticalLayout neftPaymentLayout = getPaymentLayout(neftPaymentTable,
				neftContainer, PaymentType.NEFT);
		neftPaymentLayout.setSpacing(true);
		paymentViewVerticalLayout.addComponent(neftPaymentLayout);
		//rtgs
		rtgsPaymentTable = new PurchaseRecordPaymentTable(rtgsContainer,
				PaymentType.RTGS);
		VerticalLayout rtgsPaymentLayout = getPaymentLayout(rtgsPaymentTable,
				neftContainer, PaymentType.RTGS);
		rtgsPaymentLayout.setSpacing(true);
		paymentViewVerticalLayout.addComponent(rtgsPaymentLayout);
		//debitcard
		debitCardPaymentTable = new PurchaseRecordPaymentTable(debitCardContainer,
				PaymentType.DEBITCARD);
		VerticalLayout debitCardPaymentLayout = getPaymentLayout(debitCardPaymentTable,
				debitCardContainer, PaymentType.DEBITCARD);
		debitCardPaymentLayout.setSpacing(true);
		paymentViewVerticalLayout.addComponent(debitCardPaymentLayout);
		//Credit card
		creditCardPaymentTable = new PurchaseRecordPaymentTable(creditCardContainer,
				PaymentType.CREDITCARD);
		VerticalLayout creditCardPaymentLayout = getPaymentLayout(creditCardPaymentTable,
				creditCardContainer, PaymentType.CREDITCARD);
		creditCardPaymentLayout.setSpacing(true);
		paymentViewVerticalLayout.addComponent(creditCardPaymentLayout);
		//other
		otherModePaymentTable = new PurchaseRecordPaymentTable(otherModeContainer,
				PaymentType.OTHER);
		VerticalLayout otherModePaymentLayout = getPaymentLayout(otherModePaymentTable,
				creditCardContainer, PaymentType.OTHER);
		otherModePaymentLayout.setSpacing(true);
		paymentViewVerticalLayout.addComponent(otherModePaymentLayout);
		VerticalLayout itemAndNotesLayoutVL = new VerticalLayout();
		itemAndNotesLayoutVL.setWidth("100%");
		itemAndNotesLayoutVL.addComponent(paymentViewVerticalLayout);
		itemAndNotesLayoutVL.addComponent(getNotesAndSaveTransactionLayout());
		itemAndNotesLayoutVL.setSpacing(true);
		itemAndNotesLayoutVL.setImmediate(true);
		
		VerticalSplitPanel vSplitPanel = new VerticalSplitPanel();
		vSplitPanel.setFirstComponent(toolbarLayout);
		vSplitPanel.setSecondComponent(itemAndNotesLayoutVL);
		vSplitPanel.setSizeFull();
		vSplitPanel.setSplitPosition(10, Unit.PERCENTAGE);
		Panel toolBarPanel = new Panel();
		toolBarPanel.setSizeUndefined();
		toolBarPanel.setWidth("100%");
		addComponent(vSplitPanel);

	}

	private Component getNotesAndSaveTransactionLayout() {
		HorizontalLayout notesAndSaveHL = new HorizontalLayout();
		notesAndSaveHL.setSizeFull();
		notesAndSaveHL.setSpacing(true);
		notes.setSizeFull();
		notes.setIcon(FontAwesome.COMMENTS);
		notesAndSaveHL.addComponent(notes);
		saveBtn = (Button) getSaveTransactionButton();
		notesAndSaveHL.addComponent(saveBtn);
		notesAndSaveHL.setComponentAlignment(saveBtn, Alignment.MIDDLE_LEFT);
		return notesAndSaveHL;
	}

	private Component getSaveTransactionButton() {
		saveBtn = new Button("Save");
		saveBtn.setIcon(FontAwesome.SAVE);
		saveBtn.setSizeUndefined();
		saveBtn.setImmediate(true);
		saveBtn.addStyleName("default");
		saveBtn.setData(this);
		saveBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				
				
			}
		});
		return saveBtn;
	}

	private VerticalLayout getPaymentLayout(Table paymentTable,
			PurchaseRecordPaymentContainer purchaseRecordContainer,
			PaymentType paymentType) {
		String buttonType = "Cheque";
		String buttonStyle = "";
		switch (paymentType) {
		case CHEQUE:
			buttonType = "CHEQUE";
			buttonStyle = "gold-table";
			break;
		case NEFT:
			buttonType = "NEFT";
			buttonStyle = "silver-table";
			break;
		case RTGS:
			buttonType = "RTGS";
			buttonStyle = "diamond-table";
			break;
		case DEBITCARD:
			buttonType = "DEBIT CARD";
			buttonStyle = "gold-table";
			break;
		case CREDITCARD:
			buttonType = "CREDIT CARD";
			buttonStyle = "silver-table";
			break;
		case OTHER: 
			buttonType = "OTHER MODE";
			buttonStyle = "diamond-table";
			break;
		}
		Button newBillBtn = new Button(buttonType);
		newBillBtn.setSizeUndefined();
		newBillBtn.addStyleName("icon-newbill");
		newBillBtn.addStyleName(buttonStyle);
		newBillBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				purchaseRecordContainer.addCustomItem(paymentType);
				paymentTable.setPageLength(paymentTable.size());
				paymentTable.setImmediate(true);
				paymentyItemTableCountValueChange(paymentType);
				paymentTable.setColumnFooter(
						PurchaseRecordPaymentContainer.DELETE,
						String.valueOf(paymentTable.size()));
				paymentTable.setColumnFooter(
						PurchaseRecordPaymentContainer.AMOUNT, String.format(
								"%.3f",
								((PurchaseRecordPaymentContainer) paymentTable
										.getContainerDataSource())
										.getTotalAmount()));
			}
		});
		VerticalLayout itemHolderLayout = new VerticalLayout();
		itemHolderLayout.addComponent(newBillBtn);
		itemHolderLayout.addComponent(paymentTable);
		return itemHolderLayout;
	}

	private void paymentyItemTableCountValueChange(PaymentType paymentType) {
		Collection<?> itemIdsList = null;
		workingTable = getWorkingTable(paymentType);
		itemIdsList = workingTable.getItemIds();

		for (Object itemId : itemIdsList) {
			Image deleteImage = (Image) ((workingTable.getItem(itemId)
					.getItemProperty(PurchaseRecordPaymentContainer.DELETE)
					.getValue()));
			deleteImage.addClickListener(new MouseEvents.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					PaymentType paymentType = (PaymentType) (((Image) event
							.getComponent()).getData());
					workingTable = getWorkingTable(paymentType);
					workingTable.setPageLength(workingTable.size());
					workingTable.setColumnFooter(
							PurchaseRecordPaymentContainer.DELETE, ""
									+ workingTable.size());

					workingTable.setColumnFooter(
							PurchaseRecordPaymentContainer.AMOUNT,
							String.format(
									"%.3f",
									((PurchaseRecordPaymentContainer) workingTable
											.getContainerDataSource())
											.getTotalAmount()));
					workingTable.setImmediate(true);
				}
			});
			handleAmountValueChangeListener(itemId, paymentType);
		}
	}

	private void handleAmountValueChangeListener(Object itemId,
			PaymentType paymentType) {
		DecimalTextField amountTxtField = new DecimalTextField();
		amountTxtField = (DecimalTextField) ((PurchaseRecordPaymentContainer) workingTable
				.getContainerDataSource()).getItem(itemId)
				.getItemProperty(PurchaseRecordPaymentContainer.AMOUNT)
				.getValue();
		amountTxtField.setImmediate(true);
		amountTxtField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				PaymentType paymentType = (PaymentType) ((TextField) event
						.getProperty()).getData();
				workingTable = getWorkingTable(paymentType);
				workingTable.setColumnFooter(PurchaseRecordPaymentContainer.AMOUNT,
						String.format("%.3f",
								((PurchaseRecordPaymentContainer) workingTable
										.getContainerDataSource())
										.getTotalAmount()));
			}

		});
	}

	private VerticalLayout getToolbar() {
		VerticalLayout toolbarMainLayout = new VerticalLayout();
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setWidth("100%");
		toolbar.setSpacing(true);
		toolbar.setMargin(true);

		Label title = new Label("Purchase Record");
		title.addStyleName("h1");
		title.setSizeUndefined();
		toolbar.addComponent(title);
		toolbar.setComponentAlignment(title, Alignment.MIDDLE_LEFT);

		Button newBillBtn = new Button("New Bill");
		newBillBtn.setSizeUndefined();
		newBillBtn.addStyleName("icon-newbill");
		newBillBtn.addStyleName("default");

		newBillBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo("/purchaserecord");
			}
		});
		toolbar.addComponent(newBillBtn);
		toolbar.setComponentAlignment(newBillBtn, Alignment.MIDDLE_RIGHT);

		billPopUpDate.setCaption("Billing Date");
		billPopUpDate.setImmediate(true);
		billPopUpDate.setInvalidAllowed(false);
		billPopUpDate.setLocale(new Locale("en", "IN"));
		billPopUpDate.setDateFormat(INDIAN_DATE_FORMAT);
		billPopUpDate.setTextFieldEnabled(false);
		billPopUpDate.setInputPrompt(SELECT_DATE);
		billPopUpDate.setValue(new Date());
		toolbar.addComponent(billPopUpDate);
		toolbar.setComponentAlignment(billPopUpDate, Alignment.MIDDLE_LEFT);

		partyNameTxt.setValue("");
		toolbar.addComponent(partyNameTxt);
		toolbar.setComponentAlignment(partyNameTxt, Alignment.MIDDLE_LEFT);

		itemNameTxt.setValue("");
		toolbar.addComponent(itemNameTxt);
		toolbar.setComponentAlignment(itemNameTxt, Alignment.MIDDLE_LEFT);

		weightTxt.setValue("0.000");
		toolbar.addComponent(weightTxt);
		toolbar.setComponentAlignment(weightTxt, Alignment.MIDDLE_LEFT);

		toolbarMainLayout.addComponent(toolbar);
		toolbarMainLayout.setSpacing(true);

		return toolbarMainLayout;
	}

	private Table getPaymentTable(
			PurchaseRecordPaymentContainer paymentContainer,
			PaymentType paymentType) {
		PurchaseRecordPaymentTable paymentTable = new PurchaseRecordPaymentTable(
				paymentContainer, paymentType);
		purchasePaymentTableCountValueChange(paymentType);
		paymentTable.setPageLength(paymentTable.size());
		paymentTable.setColumnFooter(PurchaseRecordPaymentContainer.DELETE, ""
				+ paymentTable.size());
		paymentTable.setColumnFooter(PurchaseRecordPaymentContainer.AMOUNT,
				String.format("%.3f",
						((PurchaseRecordPaymentContainer) paymentTable
								.getContainerDataSource()).getTotalAmount()));
		paymentTable.setImmediate(true);
		return paymentTable;
	}

	private void purchasePaymentTableCountValueChange(PaymentType paymentType) {
		Collection<?> itemIdsList = null;
		workingTable = getWorkingTable(paymentType);
		itemIdsList = workingTable.getItemIds();

		for (Object itemId : itemIdsList) {
			Image deleteImage = (Image) ((workingTable.getItem(itemId)
					.getItemProperty(PurchaseRecordPaymentContainer.DELETE)
					.getValue()));
			deleteImage.addClickListener(new MouseEvents.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					PaymentType paymentType = (PaymentType) (((Image) event
							.getComponent()).getData());
					workingTable = getWorkingTable(paymentType);
					workingTable.setPageLength(workingTable.size());
					workingTable.setColumnFooter(
							PurchaseRecordPaymentContainer.DELETE, ""
									+ workingTable.size());
					workingTable.setColumnFooter(
							PurchaseRecordPaymentContainer.AMOUNT,
							String.format(
									"%.3f",
									((PurchaseRecordPaymentContainer) workingTable
											.getContainerDataSource())
											.getTotalAmount()));
					workingTable.setImmediate(true);
				}
			});
		}
	}

	private Table getWorkingTable(PaymentType paymentType) {
		switch (paymentType) {
		case CHEQUE:
			workingTable = checkPaymentTable;
			break;
		case CREDITCARD:
			workingTable = creditCardPaymentTable;
			break;
		case DEBITCARD:
			workingTable = debitCardPaymentTable;
			break;
		case NEFT:
			workingTable = neftPaymentTable;
			break;
		case RTGS:
			workingTable = rtgsPaymentTable;
			break;
		case OTHER:
			workingTable = otherModePaymentTable;
			break;
		}
		return workingTable;
	}
}
