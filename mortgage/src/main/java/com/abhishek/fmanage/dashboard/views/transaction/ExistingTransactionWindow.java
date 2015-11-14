package com.abhishek.fmanage.dashboard.views.transaction;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.abhishek.fmanage.mortgage.data.bean.Customer;
import com.abhishek.fmanage.mortgage.data.bean.MortgageItem;
import com.abhishek.fmanage.mortgage.data.bean.MortgageTransaction;
import com.abhishek.fmanage.mortgage.data.container.MortgageItemViewContainer;
import com.avathartech.fastformfields.widgets.DecimalTextField;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * This class represents a window for all existing transactions
 * 
 * @author Abhishek Gupta
 *
 */
public class ExistingTransactionWindow extends Window {
	private static final String ITEM_WEIGHT_COL_NAME = "Item Weight";
	public static final String TRANSACTION_DETAIL = "Transaction Detail";
	public static final String TRANSACTION_DATE = "Transaction Date";
	public static final String CANCEL_BUTTON_LABEL_NAME = "Cancel";
	public static final String DEFAULT_AMOUNT_LENDED = "0.0";
	public static final String DEFAULT_INTEREST_RATE = "4.0";
	public static final long serialVersionUID = 1L;
	public static final String TOOLTIP = "Add Item";

	private PopupDateField transactionDatePopupDataField = new PopupDateField();
	private DecimalTextField interestRate = new DecimalTextField("Interest Rate(%)");
	private DecimalTextField amountLended = new DecimalTextField("Amount Lended(INR)");
	private DecimalTextField totalWeight = new DecimalTextField("Total Weight");
	private Table itemTable = new Table();

	private final FormLayout userDetailFormlayout = new FormLayout();
	private final CheckBox totalWeightCheckBox = new CheckBox("By Total Weight");
	private final Button deactivateBtn = new Button("Deactivate");

	public ExistingTransactionWindow(final MortgageTransaction mortgageTransaction, final boolean isNewTransaction) {
		super(TRANSACTION_DETAIL);
		center();
		setCloseShortcut(KeyCode.ESCAPE, null);
		setResizable(false);
		setWidth("90%");
		setHeight("90%");
		setClosable(false);
		addStyleName("no-vertical-drag-hints");
		addStyleName("no-horizontal-drag-hints");
		setModal(true);

		setUpTransactionDatePopupField();
		setupUserDetailFormLayout();

		HorizontalLayout hL = new HorizontalLayout();
		hL.addComponent(transactionDatePopupDataField);
		hL.addComponent(interestRate);
		hL.addComponent(amountLended);
		hL.addComponent(totalWeight);

		interestRate.setValue(DEFAULT_INTEREST_RATE);
		amountLended.setValue(DEFAULT_AMOUNT_LENDED);

		VerticalLayout vL = new VerticalLayout();
		vL.addComponent(hL);
		HorizontalLayout footer = new HorizontalLayout();
		footer.addStyleName("footer");
		footer.setWidth("100%");
		footer.setMargin(true);

		Button ok = new Button(CANCEL_BUTTON_LABEL_NAME);
		ok.addStyleName("wide");
		ok.addStyleName("default");
		ok.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		footer.addComponent(ok);
		footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);

		itemTable.setSizeFull();
		itemTable.addStyleName("borderless");
		itemTable.setSelectable(false);
		itemTable.setColumnCollapsingAllowed(false);
		itemTable.setColumnReorderingAllowed(true);
		itemTable.setFooterVisible(true);
		itemTable.setMultiSelect(false);
		itemTable.setSelectable(false);
		itemTable.setPageLength(5);

		final MortgageItemViewContainer mItemViewContainer = new MortgageItemViewContainer();
		itemTable.setContainerDataSource(mItemViewContainer);
		itemTable.setVisibleColumns(new Object[] {
				"Delete",
				"Item Name",
				ITEM_WEIGHT_COL_NAME,
				"Item Description"});
		itemTable.setFooterVisible(true);
		itemTable.setColumnFooter("Delete", "Total Items: " + String.valueOf(itemTable.size()));
		itemTable.setColumnFooter(ITEM_WEIGHT_COL_NAME, "Total Weight: " + "100");

		final CheckBox switchEditable = new CheckBox("Editable");
		switchEditable
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					public void valueChange(ValueChangeEvent event) {
						itemTable.setEditable(((Boolean) event.getProperty()
								.getValue()).booleanValue());
						itemTable.setImmediate(true);
						itemTable.setEnabled(((Boolean) event.getProperty()
								.getValue()).booleanValue());
					}
				});

		if (!isNewTransaction) {
			populateComponentData(mortgageTransaction, mItemViewContainer);
		} else {
			mItemViewContainer.addMortgageItem(totalWeightCheckBox.getValue(), "", DEFAULT_AMOUNT_LENDED, "");
		}
		ThemeResource resource = new ThemeResource("img/addButtonSmall.jpg");
		Image image = new Image("", resource);
		image.setHeight("40px");
		image.setWidth("40px");
		image.setDescription(TOOLTIP);
		image.addClickListener(new MouseEvents.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
				mItemViewContainer.addMortgageItem(
						totalWeightCheckBox.getValue(), "",
						DEFAULT_AMOUNT_LENDED, "");
			}
		});

		vL.addComponent(image);
		vL.addComponent(itemTable);
		vL.setWidth("90%");
		vL.setHeight("90%");
		Panel pL = new Panel();
		VerticalLayout mainLayout = new VerticalLayout();

		Button saveBtn = new Button("Save");
		saveBtn.addStyleName("wide");
		saveBtn.addStyleName("default");
		saveBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Component c = userDetailFormlayout.getComponent(0);
				((TextField) c).setRequired(true);
				((TextField) c).setImmediate(true);
				Notification.show("Not implemented: " + ((TextField) c).getValue());
			}
		});
		totalWeightCheckBox.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				totalWeight.setEnabled(totalWeightCheckBox.getValue());
				if (totalWeightCheckBox.getValue()) {
					List<?> li = mItemViewContainer.getItemIds();
					for (int i = 0; i < li.size(); i++) {
						DecimalTextField itemWeight = (DecimalTextField) mItemViewContainer
								.getItem(li.get(i))
								.getItemProperty(ITEM_WEIGHT_COL_NAME).getValue();
						itemWeight.setEnabled(false);
					}
				} else {
					List<?> li = mItemViewContainer.getItemIds();
					for (int i = 0; i < li.size(); i++) {
						DecimalTextField itemWeight = (DecimalTextField) mItemViewContainer
								.getItem(li.get(i))
								.getItemProperty(ITEM_WEIGHT_COL_NAME).getValue();
						itemWeight.setEnabled(true);
					}
				}
			}
		});

		deactivateBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				mortgageTransaction.setTransactionStatus(false);
				mortgageTransaction.setTransactionClosedDate("");

			}
		});

		vL.addComponent(getSaveCancelHorizontalLayout(ok, saveBtn));
		mainLayout.addComponent(getTopHorizontalLayout(vL, switchEditable));
		pL.setContent(mainLayout);
		setContent(pL);
	}

	private HorizontalLayout getTopHorizontalLayout(final VerticalLayout verticalLayout, final CheckBox switchEditable) {
		HorizontalLayout topHorizontalLayout = new HorizontalLayout();
		topHorizontalLayout.setSizeFull();
		topHorizontalLayout.addComponent(userDetailFormlayout);
		topHorizontalLayout.addComponent(verticalLayout);
		topHorizontalLayout.addComponent(switchEditable);
		topHorizontalLayout.addComponent(totalWeightCheckBox);
		topHorizontalLayout.addComponent(deactivateBtn);
		topHorizontalLayout.setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
		topHorizontalLayout.setExpandRatio(userDetailFormlayout, 1);
		topHorizontalLayout.setExpandRatio(verticalLayout, 3);
		return topHorizontalLayout;
	}

	private HorizontalLayout getSaveCancelHorizontalLayout(final Button okBtn, final Button saveBtn) {
		HorizontalLayout saveCancelLayout = new HorizontalLayout();
		saveCancelLayout.addComponent(saveBtn);
		saveCancelLayout.addComponent(okBtn);
		saveCancelLayout.setComponentAlignment(saveBtn, Alignment.BOTTOM_LEFT);
		saveCancelLayout.setComponentAlignment(okBtn, Alignment.BOTTOM_RIGHT);
		saveCancelLayout.setExpandRatio(saveBtn, 1);
		saveCancelLayout.setExpandRatio(okBtn, 1);
		return saveCancelLayout;
	}

	private void setupUserDetailFormLayout() {
		final BeanFieldGroup<Customer> binder = new BeanFieldGroup<Customer>(
				Customer.class);
		binder.setItemDataSource(new Customer());
		binder.setBuffered(false);

		userDetailFormlayout.setEnabled(true);
		userDetailFormlayout.addComponent(binder.buildAndBind("FirstName", "firstName"));
		userDetailFormlayout.addComponent(binder.buildAndBind("LastName", "lastName"));
		userDetailFormlayout.addComponent(binder.buildAndBind("ContactNumber", "contactNumber"));
		userDetailFormlayout.addComponent(binder.buildAndBind("EmailId", "emailId"));
		userDetailFormlayout.addComponent(binder.buildAndBind("Street Address1", "streetAddress1"));
		userDetailFormlayout.addComponent(binder.buildAndBind("Street Address2", "streetAddress2"));
		userDetailFormlayout.addComponent(binder.buildAndBind("City", "city"));
		userDetailFormlayout.addComponent(binder.buildAndBind("State", "stateprovince"));
		userDetailFormlayout.addComponent(binder.buildAndBind("Zipcode", "zipcode"));
		userDetailFormlayout.addComponent(binder.buildAndBind("Country", "country"));
		userDetailFormlayout.setImmediate(true);
		userDetailFormlayout.setData(binder);
	}

	private void setUpTransactionDatePopupField() {
		transactionDatePopupDataField.setCaption(TRANSACTION_DATE);
		transactionDatePopupDataField.setImmediate(true);
		transactionDatePopupDataField.setInvalidAllowed(false);
		transactionDatePopupDataField.setLocale(new Locale("en", "IN"));
		transactionDatePopupDataField.setDateFormat(MortgageTransactionView.INDIAN_DATE_FORMAT);
		transactionDatePopupDataField.setTextFieldEnabled(false);
		transactionDatePopupDataField.setInputPrompt(MortgageTransactionView.SELECT_DATE);
		transactionDatePopupDataField.setValue(new java.util.Date());
	}

	private void populateComponentData(final MortgageTransaction mTrans,
			final MortgageItemViewContainer mItemViewContainer) {
		@SuppressWarnings("unchecked")
		BeanFieldGroup<Customer> binder = (BeanFieldGroup<Customer>) userDetailFormlayout.getData();
		binder.setItemDataSource(mTrans.getCustomer());

		transactionDatePopupDataField.setValue(new Date(989));
		interestRate.setValue(String.valueOf(mTrans.getInterestRateAssignedToCustomer()));
		amountLended.setValue(String.valueOf(mTrans.getAmountPaidToCustomer()));
		List<MortgageItem> mItemList = mTrans.getItemList();
		for (MortgageItem mItem : mItemList) {
			mItemViewContainer.addMortgageItem(
					false,
					mItem.getItemName(),
					String.valueOf(mItem.getItemWeight()),
					mItem.getItemDescription());
		}
	}
}