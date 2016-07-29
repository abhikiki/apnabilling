package com.abhishek.fmanage.retail.views;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.abhishek.fmanage.cache.ItemCache;
import com.abhishek.fmanage.mortgage.data.container.CustomItemContainerInterface;
import com.abhishek.fmanage.mortgage.data.container.ItemContainerType;
import com.abhishek.fmanage.retail.data.container.DiamondItemContainer;
import com.abhishek.fmanage.retail.data.container.GeneralItemContainer;
import com.abhishek.fmanage.retail.data.container.GoldItemContainer;
import com.abhishek.fmanage.retail.data.container.SilverItemContainer;
import com.abhishek.fmanage.retail.dto.CustomerDTO;
import com.abhishek.fmanage.retail.dto.DiamondTransactionItemDTO;
import com.abhishek.fmanage.retail.dto.GeneralTransactionItemDTO;
import com.abhishek.fmanage.retail.dto.GoldTransactionItemDTO;
import com.abhishek.fmanage.retail.dto.ItemDTO;
import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.dto.SilverTransactionItemDTO;
import com.abhishek.fmanage.retail.dto.TransactionDTO;
import com.abhishek.fmanage.retail.form.PriceForm;
import com.abhishek.fmanage.retail.restclient.response.BillCreationResponse;
import com.abhishek.fmanage.retail.restclient.service.RestTransactionService;
import com.abhishek.fmanage.retail.tables.CustomerInfoLayout;
import com.abhishek.fmanage.retail.tables.DiamondItemTable;
import com.abhishek.fmanage.retail.tables.GeneralItemTable;
import com.abhishek.fmanage.retail.tables.GoldItemTable;
import com.abhishek.fmanage.retail.tables.SilverItemTable;
import com.abhishek.fmanage.retail.window.BillWindow;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.event.MouseEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public class RetailInvoiceView extends VerticalLayout implements View {

	private final Logger logger = LoggerFactory
			.getLogger(RetailInvoiceView.class);
	private static final String INVOICE_BILL = "Invoice Bill";
	public static final String ESTIMATE_BILL = "Estimate Bill";
	public static final String INDIAN_DATE_FORMAT = "dd/MM/yyyy";
	public static final String SELECT_DATE = "Select Date";

	private static final long serialVersionUID = 1L;
	private GoldItemContainer goldItemContainer = new GoldItemContainer();
	private SilverItemContainer silverItemContainer = new SilverItemContainer();
	private DiamondItemContainer diamondItemContainer = new DiamondItemContainer();
	private GeneralItemContainer generalItemContainer = new GeneralItemContainer();
	private Table goldBillingTable = new Table();
	private Table silverBillingTable = new Table();
	private Table diamondBillingTable = new Table();
	private Table generalBillingTable = new Table();
	private final PopupDateField billPopUpDate = new PopupDateField();
	private VerticalLayout retailViewVerticalLayout = new VerticalLayout();
	private Component customerLayout;
	private PriceForm pfForm = new PriceForm(getPricePropertyItem());
	private HorizontalLayout priceLayout = new HorizontalLayout();
	private ComboBox staffNameComboBox = new ComboBox("Staff Name");
	private CustomerDTO cusBean = new CustomerDTO();
	private CheckBox includePrice = new CheckBox("Include Price", true);
	private OptionGroup billType;
	private TextArea notes = new TextArea("Invoice Notes");
	private TextField transactionSearchTxt;
	long transId = -1L;
	private ShopDTO shopDto;
	private Button generateBillBtn;

	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();
		goldBillingTable = getGoldBillingTable();
		silverBillingTable = getSilverItemTable();
		diamondBillingTable = getDiamondTable();
		generalBillingTable = getGeneralTable();

		shopDto =  (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);

		VerticalLayout goldBillingLayout = getBillingLayout(goldBillingTable, goldItemContainer, ItemContainerType.GOLD);
		VerticalLayout silverBillingLayout = getBillingLayout(silverBillingTable, silverItemContainer, ItemContainerType.SILVER);
		VerticalLayout diamondBillingLayout = getBillingLayout(diamondBillingTable, diamondItemContainer, ItemContainerType.DIAMOND);
		VerticalLayout generalBillingLayout = getBillingLayout(generalBillingTable, generalItemContainer, ItemContainerType.GENERAL);
		customerLayout = new CustomerInfoLayout(cusBean).getUserDetailFormLayout();
		retailViewVerticalLayout.addComponent(customerLayout);
		retailViewVerticalLayout.addComponent(goldBillingLayout);
		retailViewVerticalLayout.addComponent(silverBillingLayout);
		retailViewVerticalLayout.addComponent(diamondBillingLayout);
		retailViewVerticalLayout.addComponent(generalBillingLayout);
		retailViewVerticalLayout.addComponent(getCalculatedPriceLayout());
		retailViewVerticalLayout.addStyleName("customer-layout");
		retailViewVerticalLayout.setSpacing(true);
		retailViewVerticalLayout.setImmediate(true);

		HorizontalLayout toolbarLayout = new HorizontalLayout();
		toolbarLayout.addComponent(getToolbar());
		toolbarLayout.setSizeUndefined();
		toolbarLayout.setSizeFull();
		toolbarLayout.addStyleName("mytoolbar");

		VerticalSplitPanel vSplitPanel = new VerticalSplitPanel();
		vSplitPanel.setFirstComponent(toolbarLayout);
		vSplitPanel.setSecondComponent(retailViewVerticalLayout);
		vSplitPanel.setSizeFull();
		vSplitPanel.setSplitPosition(14, Unit.PERCENTAGE);
		Panel toolBarPanel = new Panel();
		toolBarPanel.setSizeUndefined();
		toolBarPanel.setWidth("100%");
		addComponent(vSplitPanel);
	}

	private Table getGeneralTable() {
		GeneralItemTable generalItemTable = new GeneralItemTable(
				generalItemContainer);
		addCustomTableDataContainerPriceItemCountValueChange(generalItemTable,
				generalItemContainer);
		generalItemTable.setImmediate(true);
		return generalItemTable;
	}

	private Component getCalculatedPriceLayout() {
		priceLayout.setSizeFull();
		priceLayout.setSpacing(true);
		priceLayout.addStyleName("customer-layout");
		priceLayout.addComponent(pfForm);
		notes.setSizeFull();
		notes.setIcon(FontAwesome.COMMENTS);
		Button generateBillBtn = getGenerateBillBtn();
		Button deleteBillBtn = getDeleteBillBtn();
		priceLayout.addComponent(notes);
		priceLayout.addComponent(includePrice);
		priceLayout.addComponent(generateBillBtn);
		// priceLayout.addComponent(deleteBillBtn);
		priceLayout.setSpacing(true);
		priceLayout.setExpandRatio(notes, 1);
		priceLayout.setExpandRatio(includePrice, 0.8f);

		priceLayout.setExpandRatio(pfForm, 1.2f);
		priceLayout.setExpandRatio(generateBillBtn, 0.8f);
		return priceLayout;
	}

	private Button getDeleteBillBtn() {
		Button deleteBillBtn = new Button("Delete Bill");
		deleteBillBtn.setSizeUndefined();
		deleteBillBtn.addStyleName("default");
		deleteBillBtn.setData(this);
		deleteBillBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				new RestTemplate().delete(
						"http://localhost:8090/bill/deletebill/{transId}",
						transId);
				transId = -1L;
			}
		});
		return deleteBillBtn;
	}

	private PropertysetItem getPricePropertyItem() {
		PropertysetItem item = new PropertysetItem();
		item.addItemProperty("totalItemPrice", new ObjectProperty<Double>(0.0));
		item.addItemProperty("vatOnNewItemPrice", new ObjectProperty<Double>(
				0.0));
		item.addItemProperty("oldPurchasePrice",
				new ObjectProperty<Double>(0.0));
		item.addItemProperty("discountPrice", new ObjectProperty<Double>(0.0));
		item.addItemProperty("netAmountToPay", new ObjectProperty<Double>(0.0));
		return item;
	}

	private HorizontalLayout getToolbar() {
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setWidth("100%");
		toolbar.setSpacing(true);
		toolbar.setMargin(true);
		//toolbar.addStyleName("toolbar");

		Button newBillBtn = new Button("New Bill");
		newBillBtn.setSizeUndefined();
		newBillBtn.addStyleName("icon-newbill");
		newBillBtn.addStyleName("default");

		newBillBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo("/retailbilling");
			}
		});
		toolbar.addComponent(newBillBtn);
		toolbar.setComponentAlignment(newBillBtn, Alignment.MIDDLE_LEFT);

		ComboBox staffListCombo = getStaffListComboBox();
		toolbar.addComponent(staffListCombo);
		toolbar.setComponentAlignment(staffListCombo, Alignment.MIDDLE_LEFT);

		billPopUpDate.setCaption("Billing Date");
		billPopUpDate.setImmediate(true);
		billPopUpDate.setInvalidAllowed(false);
		billPopUpDate.setLocale(new Locale("en", "IN"));
		billPopUpDate.setDateFormat(INDIAN_DATE_FORMAT);
		billPopUpDate.setTextFieldEnabled(false);
		billPopUpDate.setInputPrompt(SELECT_DATE);
		billPopUpDate.setValue(new Date());
		toolbar.addComponent(billPopUpDate);

		Label tinLabel = new Label("<font color=\"#D5CDCB\"><b>TIN VAT NO:</b>" + shopDto.getTinNumber() + "</font>", ContentMode.HTML);
		//tinLabel.addStyleName("mytoolbar");
		tinLabel.setImmediate(true);

		transactionSearchTxt = new TextField("Transaction Number");
		transactionSearchTxt.setImmediate(true);
		// transactionSearchTxt.setVisible(false);
		transactionSearchTxt.setRequired(false);
		transactionSearchTxt.setValidationVisible(true);
		transactionSearchTxt.setWidth("60%");

		// A single-select radio button group
		billType = new OptionGroup("Bill Type");
		billType.addItems(ESTIMATE_BILL, INVOICE_BILL);
		billType.setValue(ESTIMATE_BILL);

		billType.addValueChangeListener((value) -> {
			if (!value.getProperty().getValue().equals(ESTIMATE_BILL)) {
				tinLabel.setValue("<font color=\"blue\"><b>TIN VAT NO:</b>" + shopDto.getTinNumber() + "</font>");
				pfForm.isInvoiceEnabled = true;
				pfForm.vatOnNewItemPrice.setValue((String.format("%.3f",
						pfForm.getVatPrice())));
				pfForm.netAmountToPay.setValue(String.format("%.3f",
						pfForm.getTotalNetAmount()));
				pfForm.advancePayment.setValue(String.format("%.3f", 0.000f));
				pfForm.advancePayment.setEnabled(false);
				pfForm.advancePayment.setIcon(null);
				//pfForm.balanceAmount.setEnabled(false);
			} else {
				tinLabel.setValue("<font color=\"#D5CDCB\"><b>TIN VAT NO:</b>" + shopDto.getTinNumber() + "</font>");
				pfForm.isInvoiceEnabled = false;
				pfForm.vatOnNewItemPrice.setValue(String.format("%.3f", 0.000f));
				pfForm.netAmountToPay.setValue(String.format("%.3f",
						pfForm.getTotalNetAmount()));
				//pfForm.vatOnNewItemPrice.setEnabled(false);
				pfForm.advancePayment.setEnabled(true);
				pfForm.advancePayment.setIcon(FontAwesome.EDIT);
				//pfForm.balanceAmount.setEnabled(true);

			}
		});
		HorizontalLayout optionGroupLayout = new HorizontalLayout();
		billType.addItems(ESTIMATE_BILL, INVOICE_BILL);
		billType.setValue(ESTIMATE_BILL);
		optionGroupLayout.addComponent(billType);
		optionGroupLayout.setComponentAlignment(billType, Alignment.MIDDLE_LEFT);

		Button autoFillTransBtn = new Button("AutoFill");
		autoFillTransBtn.setSizeUndefined();
		autoFillTransBtn.addStyleName("icon-search-1");
		autoFillTransBtn.addStyleName("default");
		autoFillTransBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				if (!StringUtils.isEmpty(transactionSearchTxt.getValue())
						&& NumberUtils.isDigits(transactionSearchTxt.getValue())) {
					transId = Long.parseLong(transactionSearchTxt
							.getValue());
					TransactionDTO tDto = new RestTransactionService()
							.getBill(transId);
					if (tDto != null) {
						cusBean = tDto.getCustomer();
						retailViewVerticalLayout.replaceComponent(customerLayout, customerLayout = new CustomerInfoLayout(cusBean).getUserDetailFormLayout());
						reloadGoldItems(tDto);
						reloadSilverItems(tDto);
						reloadDiamondItems(tDto);
						reloadGeneralItems(tDto);
						staffNameComboBox.addItem(tDto.getDealingStaffName());
						staffNameComboBox.setValue(tDto.getDealingStaffName());
						billPopUpDate.setValue(tDto.getTransactionDate());
						billType.setValue(tDto.isEstimateBill() ? ESTIMATE_BILL : INVOICE_BILL);
						pfForm.oldPurchasePrice.setValue(String.valueOf(tDto.getPriceBean().getOldPurchase()));
						pfForm.discountPrice.setValue(String.valueOf(tDto.getPriceBean().getDiscount()));
						pfForm.balanceAmount.setValue(String.valueOf(tDto.getPriceBean().getBalanceAmount()));
						notes.setValue(tDto.getNotes());
						includePrice.setValue(tDto.getIncludePrice());
						transId =-1L;
						generateBillBtn.setCaption("Generate Bill");
					} else {
						goldItemContainer.removeAllItems();
						goldBillingTable.setColumnFooter(GoldItemContainer.WEIGHT, "0.000");
						goldBillingTable.setColumnFooter(GoldItemContainer.PRICE, "0.000");
						goldBillingTable.setPageLength(goldBillingTable.size());
						goldBillingTable.setColumnFooter(GoldItemContainer.DELETE, ("Items=" + goldBillingTable.size()));
						
						silverItemContainer.removeAllItems();
						silverBillingTable.setColumnFooter(SilverItemContainer.WEIGHT, "0.000");
						silverBillingTable.setColumnFooter(SilverItemContainer.PRICE, "0.000");
						silverBillingTable.setPageLength(silverBillingTable.size());
						silverBillingTable.setColumnFooter(SilverItemContainer.DELETE, ("Items=" + silverBillingTable.size()));
						
						diamondItemContainer.removeAllItems();
						diamondBillingTable.setColumnFooter(DiamondItemContainer.PRICE, "0.000");
						diamondBillingTable.setColumnFooter(DiamondItemContainer.GOLD_WEIGHT, "0.000");
						diamondBillingTable.setColumnFooter(DiamondItemContainer.DIAMOND_WEIGHT, "0.00");
						diamondBillingTable.setPageLength(diamondBillingTable.size());
						diamondBillingTable.setColumnFooter(DiamondItemContainer.DELETE, ("Items=" + diamondBillingTable.size()));
						
						generalItemContainer.removeAllItems();
						generalBillingTable.setColumnFooter(GeneralItemContainer.PRICE, "0.000");
						generalBillingTable.setPageLength(generalBillingTable.size());
						generalBillingTable.setColumnFooter(GeneralItemContainer.DELETE, ("Items=" + generalBillingTable.size()));
						
						cusBean = new CustomerDTO();
						retailViewVerticalLayout.replaceComponent(customerLayout, customerLayout = new CustomerInfoLayout(cusBean).getUserDetailFormLayout());
						priceLayout.replaceComponent(pfForm, pfForm = new PriceForm(getPricePropertyItem()));
						billType.setValue(ESTIMATE_BILL);
						includePrice.setValue(true);
						notes.setValue("");
						staffNameComboBox.addItem("STAFF");
						staffNameComboBox.setValue("STAFF");
						billPopUpDate.setValue(new Date());
						Notification.show("Transaction " + transId  + " not found");
					}
				} else {
					Notification.show("Transaction Number is not valid");
				}
				generateBillBtn.setCaption("Generate Bill");
			}

			
		});

		toolbar.addComponent(optionGroupLayout);
		toolbar.addComponent(transactionSearchTxt);
		toolbar.addComponent(autoFillTransBtn);
		toolbar.addComponent(tinLabel);

		toolbar.setExpandRatio(newBillBtn, 1);
		toolbar.setExpandRatio(staffListCombo, 1);
		toolbar.setExpandRatio(billPopUpDate, 1);
		toolbar.setExpandRatio(optionGroupLayout, 1);
		toolbar.setExpandRatio(tinLabel, 1);
		toolbar.setExpandRatio(transactionSearchTxt, 1);
		toolbar.setExpandRatio(autoFillTransBtn, 1);
		toolbar.setComponentAlignment(newBillBtn, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(staffListCombo, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(billPopUpDate, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(optionGroupLayout, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(tinLabel, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(tinLabel, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(transactionSearchTxt, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(autoFillTransBtn, Alignment.MIDDLE_LEFT);
		return toolbar;
	}

	private void reloadGeneralItems(TransactionDTO tDto){
		generalItemContainer.removeAllItems();
		List<GeneralTransactionItemDTO> generalItemBeanList = tDto.getGeneralTransactionItemBeanList();
		generalItemContainer.addCustomItem(generalItemBeanList);
		addCustomTableDataContainerPriceItemCountValueChange(generalBillingTable, generalItemContainer);
		if(!generalItemBeanList.isEmpty()){
			for (int i = 0; i < generalItemBeanList.size(); i++) {
				TextField itemPriceTxtField = (TextField) ((generalItemContainer.getItem(generalItemContainer.getIdByIndex(i)).getItemProperty(DiamondItemContainer.PRICE).getValue()));
				itemPriceTxtField.setValue("-1.0");
				itemPriceTxtField.setValue(String.valueOf(generalItemBeanList.get(i).getItemPrice()));
			}

		}
	}
	
	private void reloadDiamondItems(TransactionDTO tDto){
		diamondItemContainer.removeAllItems();
		List<DiamondTransactionItemDTO> diamondItemBeanList = tDto.getDiamondTransactionItemBeanList();
		diamondItemContainer.addCustomItem(diamondItemBeanList);
		addCustomTableDataContainerPriceItemCountValueChange(diamondBillingTable, diamondItemContainer);
		diamondTableWeightContainerValueChange();
		if (!diamondItemBeanList.isEmpty()) {
			for (int i = 0; i < diamondItemBeanList.size(); i++) {
				TextField itemPriceTxtField = (TextField) ((diamondItemContainer.getItem(diamondItemContainer.getIdByIndex(i)).getItemProperty(DiamondItemContainer.PRICE).getValue()));
				itemPriceTxtField.setValue("-1.0");
				itemPriceTxtField.setValue(String.valueOf(diamondItemBeanList.get(i).getItemPrice()));
				TextField itemGoldWeightTxtField = (TextField) ((diamondItemContainer.getItem(diamondItemContainer.getIdByIndex(i)).getItemProperty(DiamondItemContainer.GOLD_WEIGHT)
						.getValue()));
				itemGoldWeightTxtField.setValue("999999.0");
				itemGoldWeightTxtField.setValue(String.valueOf(diamondItemBeanList.get(i).getGoldWeight()));
				
				TextField itemDiamondWeightTxtField = (TextField) ((diamondItemContainer.getItem(diamondItemContainer.getIdByIndex(i)).getItemProperty(DiamondItemContainer.DIAMOND_WEIGHT)
						.getValue()));
				itemDiamondWeightTxtField.setValue("999999.0");
				itemDiamondWeightTxtField.setValue(String.valueOf(diamondItemBeanList.get(i).getDiamondWeightCarat()));
			}
		}
		
	}
	
	private void reloadSilverItems(TransactionDTO tDto){
		silverItemContainer.removeAllItems();
		List<SilverTransactionItemDTO> silverItemBeanList = tDto.getSilverTransactionItemBeanList();
		silverItemContainer.addCustomItem(silverItemBeanList);
		addCustomTableDataContainerPriceItemCountValueChange(silverBillingTable, silverItemContainer);
		silverTableWeightContainerValueChange();
		if (!silverItemBeanList.isEmpty()) {

			for (int i = 0; i < silverItemBeanList.size(); i++) {

				TextField itemPriceTxtField = (TextField) ((silverItemContainer.getItem(silverItemContainer.getIdByIndex(i))
						.getItemProperty(SilverItemContainer.PRICE).getValue()));
				itemPriceTxtField.setValue("-1.0");
				itemPriceTxtField.setValue(String.valueOf(silverItemBeanList.get(i).getSilverItemPrice()));

				TextField itemWeightTxtField = (TextField) ((silverItemContainer.getItem(silverItemContainer.getIdByIndex(i)).getItemProperty(SilverItemContainer.WEIGHT)
						.getValue()));
				itemWeightTxtField.setValue("999999.0");
				itemWeightTxtField.setValue(String.valueOf(silverItemBeanList.get(i).getWeight()));
			}
		}
	}
	
	
	private void reloadGoldItems(TransactionDTO tDto) {
		goldItemContainer.removeAllItems();
		List<GoldTransactionItemDTO> goldItemBeanList = tDto.getGoldTransactionItemBeanList();
		goldItemContainer.addCustomItem(goldItemBeanList);
		addCustomTableDataContainerPriceItemCountValueChange(goldBillingTable, goldItemContainer);
		goldTableWeightContainerValueChange();
		if (!goldItemBeanList.isEmpty()) {

			for (int i = 0; i < goldItemBeanList.size(); i++) {

				TextField itemPriceTxtField = (TextField) ((goldItemContainer
						.getItem(
								goldItemContainer
										.getIdByIndex(i))
						.getItemProperty(
								GoldItemContainer.PRICE)
						.getValue()));
				itemPriceTxtField.setValue("-1.0");
				itemPriceTxtField.setValue(String.valueOf(goldItemBeanList
						.get(i).getGoldItemPrice()));

				TextField itemWeightTxtField = (TextField) ((goldItemContainer
						.getItem(
								goldItemContainer
										.getIdByIndex(i))
						.getItemProperty(
								GoldItemContainer.WEIGHT)
						.getValue()));
				itemWeightTxtField.setValue("999999.0");
				itemWeightTxtField.setValue(String.valueOf(tDto
						.getGoldTransactionItemBeanList()
						.get(i).getWeight()));
			}
		}
	}
	private ComboBox getStaffListComboBox() {

		staffNameComboBox.setNullSelectionAllowed(false);
		staffNameComboBox.addItem("STAFF");
		staffNameComboBox.setValue("STAFF");
		List<ItemDTO> itemDTOList = ItemCache.getInstance().getItemMap().get("STAFF");
		for (ItemDTO itemDto : itemDTOList) {
			staffNameComboBox.addItem(itemDto.getItemName());
		}
		return staffNameComboBox;
	}

	private Button getGenerateBillBtn() {
		generateBillBtn = new Button("Generate Bill");
		generateBillBtn.setSizeUndefined();
		generateBillBtn.setImmediate(true);
		generateBillBtn.addStyleName("default");
		generateBillBtn.setData(this);
		generateBillBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			TransactionDTO retailTransaction = null;

			public void buttonClick(ClickEvent event) {
				try {
					boolean isEstimateBill = billType.getValue().equals(
							ESTIMATE_BILL);
					String invoiceNumber = "-1";
					boolean isTransactionActive = true;
					retailTransaction = new ExtractRetailTransaction(
							goldBillingTable, silverBillingTable,
							diamondBillingTable, generalBillingTable, cusBean,
							pfForm, isEstimateBill, billPopUpDate.getValue(),
							shopDto.getTinNumber(), Long.parseLong(invoiceNumber),
							staffNameComboBox.getValue().toString(),
							includePrice.getValue(), notes.getValue(),
							isTransactionActive).extract();
					ShopDTO shopDto =  (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);

					if (transId == -1L) {
						BillCreationResponse response = new RestTransactionService()
								.createBill(shopDto, retailTransaction);
						retailTransaction.setInvoiceNumber(response
								.getInvoiceId());
						transId = response.getTransId();

					} else {// update
						retailTransaction.setEstimateBill(isEstimateBill);
						retailTransaction.setTransactionDate(billPopUpDate
								.getValue());
						BillCreationResponse response = new RestTransactionService()
								.updateBill(transId, shopDto.getShopId(),
										retailTransaction);
						retailTransaction.setInvoiceNumber(response
								.getInvoiceId());
					}
				} catch (Exception e) {
					logger.error("Error generating invoice", e);
				}

				if (Page.getCurrent().getWebBrowser().isTouchDevice()) {
					// Resource pdf = new FileResource(new File(fileName));
					// setResource("help", pdf);
					// ResourceReference rr = ResourceReference.create(pdf,
					// (RetailInvoiceView) generateBillBtn.getData(),
					// "help");
					// Page.getCurrent().open(rr.getURL(), "blank_");
					getBillWindow(transId, retailTransaction);
				} else {
					getBillWindow(transId, retailTransaction);
				}
				generateBillBtn.setCaption("Update Bill");
			}
		});
		return generateBillBtn;
	}

	private void getBillWindow(long transId, TransactionDTO retailTransaction) {

		BillWindow bw = new BillWindow(transId, retailTransaction);
		UI.getCurrent().addWindow(bw);
		bw.focus();
	}

	private VerticalLayout getBillingLayout(Table table,
			CustomItemContainerInterface container,
			ItemContainerType itemContainerType) {
		String buttonType = "Gold";
		String buttonStyle = "";
		switch (itemContainerType.ordinal()) {
		case 0:
			buttonType = "Gold Item";
			buttonStyle = "gold-table";
			break;
		case 1:
			buttonType = "Silver Item";
			buttonStyle = "silver-table";
			break;
		case 2:
			buttonType = "Diamond Item";
			buttonStyle = "diamond-table";
			break;
		case 3:
			buttonType = "General Item";
			buttonStyle = "general-table";
		}
		Button newBillBtn = new Button(buttonType);
		newBillBtn.setSizeUndefined();
		newBillBtn.addStyleName("icon-newbill");
		newBillBtn.addStyleName(buttonStyle);

		newBillBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				container.addCustomItem();
				table.setPageLength(table.size());
				addCustomTableDataContainerPriceItemCountValueChange(table,
						container);
				goldTableWeightContainerValueChange();
				silverTableWeightContainerValueChange();
				diamondTableWeightContainerValueChange();

			}
		});
		VerticalLayout itemHolderLayout = new VerticalLayout();
		itemHolderLayout.addComponent(newBillBtn);
		itemHolderLayout.addComponent(table);
		return itemHolderLayout;
	}

	private Table getDiamondTable() {
		DiamondItemTable diamondItemTable = new DiamondItemTable(
				diamondItemContainer);
		diamondTableWeightContainerValueChange();
		addCustomTableDataContainerPriceItemCountValueChange(diamondItemTable,
				diamondItemContainer);
		diamondItemTable.setImmediate(true);
		return diamondItemTable;
	}

	private Table getSilverItemTable() {
		SilverItemTable silverItemTable = new SilverItemTable(
				silverItemContainer);
		silverTableWeightContainerValueChange();
		addCustomTableDataContainerPriceItemCountValueChange(silverItemTable,
				silverItemContainer);
		silverItemTable.setImmediate(true);
		return silverItemTable;
	}

	private Table getGoldBillingTable() {
		GoldItemTable goldItemTable = new GoldItemTable(goldItemContainer);
		goldTableWeightContainerValueChange();
		addCustomTableDataContainerPriceItemCountValueChange(goldItemTable,
				goldItemContainer);
		goldItemTable.setImmediate(true);
		return goldItemTable;
	}

	private void goldTableWeightContainerValueChange() {
		Collection<?> itemIdsList = goldBillingTable.getItemIds();
		for (Object obj : itemIdsList) {
			TextField itemTxtField = (TextField) ((goldItemContainer.getItem(
					obj).getItemProperty(GoldItemContainer.WEIGHT).getValue()));
			itemTxtField.setImmediate(true);
			itemTxtField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalFormattedWeight = String.format("%.3f",
							(goldItemContainer).getTotalWeight());
					goldBillingTable.setColumnFooter(GoldItemContainer.WEIGHT,
							totalFormattedWeight);
					pfForm.totalItemPrice.setValue(String.format("%.3f",
							getTotalGoldSilverDiamondGeneralItemsPrice()));
				}
			});
			Image deleteImage = (Image) ((goldItemContainer.getItem(obj)
					.getItemProperty(DiamondItemContainer.DELETE).getValue()));
			deleteImage.addClickListener(new MouseEvents.ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					goldBillingTable.setPageLength(goldBillingTable.size());
					String totalFormattedGoldWeight = String.format("%.3f",
							(goldItemContainer).getTotalWeight());
					goldBillingTable.setColumnFooter(GoldItemContainer.WEIGHT,
							totalFormattedGoldWeight);
					pfForm.totalItemPrice.setValue(String.format("%.3f",
							getTotalGoldSilverDiamondGeneralItemsPrice()));
				}
			});
		}
	}

	private void silverTableWeightContainerValueChange() {
		Collection<?> itemIdsList = silverBillingTable.getItemIds();
		for (Object obj : itemIdsList) {
			TextField itemTxtField = (TextField) ((silverItemContainer.getItem(
					obj).getItemProperty(SilverItemContainer.WEIGHT).getValue()));
			itemTxtField.setImmediate(true);
			itemTxtField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalFormattedWeight = String.format("%.3f",
							(silverItemContainer).getTotalWeight());
					silverBillingTable.setColumnFooter(
							SilverItemContainer.WEIGHT, totalFormattedWeight);
					pfForm.totalItemPrice.setValue(String.format("%.3f",
							getTotalGoldSilverDiamondGeneralItemsPrice()));

				}
			});
			Image deleteImage = (Image) ((silverItemContainer.getItem(obj)
					.getItemProperty(DiamondItemContainer.DELETE).getValue()));
			deleteImage.addClickListener(new MouseEvents.ClickListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					silverBillingTable.setPageLength(silverBillingTable.size());
					String totalFormattedSilverWeight = String.format("%.3f",
							(silverItemContainer).getTotalWeight());
					silverBillingTable.setColumnFooter(
							SilverItemContainer.WEIGHT,
							totalFormattedSilverWeight);
					pfForm.totalItemPrice.setValue(String.format("%.3f",
							getTotalGoldSilverDiamondGeneralItemsPrice()));
				}
			});
		}
	}

	private void diamondTableWeightContainerValueChange() {
		Collection<?> itemIdsList = diamondBillingTable.getItemIds();
		for (Object obj : itemIdsList) {
			TextField itemTxtField = (TextField) ((diamondItemContainer
					.getItem(obj).getItemProperty(
							DiamondItemContainer.GOLD_WEIGHT).getValue()));
			itemTxtField.setImmediate(true);
			itemTxtField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalFormattedWeight = String.format("%.3f",
							(diamondItemContainer).getTotalGoldWeight());
					diamondBillingTable.setColumnFooter(
							DiamondItemContainer.GOLD_WEIGHT,
							totalFormattedWeight);
					pfForm.totalItemPrice.setValue(String.format("%.3f",
							getTotalGoldSilverDiamondGeneralItemsPrice()));
				}
			});
			TextField diamondWtTextField = (TextField) ((diamondItemContainer
					.getItem(obj).getItemProperty(
							DiamondItemContainer.DIAMOND_WEIGHT).getValue()));
			diamondWtTextField.setImmediate(true);
			diamondWtTextField
					.addValueChangeListener(new ValueChangeListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void valueChange(ValueChangeEvent event) {
							String totalFormattedWeight = String.format("%.2f",
									(diamondItemContainer)
											.getTotalDiamondWeight());
							diamondBillingTable.setColumnFooter(
									DiamondItemContainer.DIAMOND_WEIGHT,
									totalFormattedWeight);
							pfForm.totalItemPrice.setValue(String
									.format("%.3f",
											getTotalGoldSilverDiamondGeneralItemsPrice()));
						}
					});
			Image deleteImage = (Image) ((diamondItemContainer.getItem(obj)
					.getItemProperty(DiamondItemContainer.DELETE).getValue()));
			deleteImage.addClickListener(new MouseEvents.ClickListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					diamondBillingTable.setPageLength(diamondBillingTable
							.size());
					String totalFormattedGoldWeight = String.format("%.3f",
							(diamondItemContainer).getTotalGoldWeight());
					diamondBillingTable.setColumnFooter(
							DiamondItemContainer.GOLD_WEIGHT,
							totalFormattedGoldWeight);
					String totalFormattedDiamondWeight = String.format("%.2f",
							(diamondItemContainer).getTotalDiamondWeight());
					diamondBillingTable.setColumnFooter(
							DiamondItemContainer.DIAMOND_WEIGHT,
							totalFormattedDiamondWeight);
					pfForm.totalItemPrice.setValue(String.format("%.3f",
							getTotalGoldSilverDiamondGeneralItemsPrice()));
				}
			});
		}
	}

	private void addCustomTableDataContainerPriceItemCountValueChange(
			Table table, CustomItemContainerInterface container) {
		Collection<?> itemIdsList = table.getItemIds();
		table.setColumnFooter(GoldItemContainer.DELETE,
				("Items=" + table.size()));
		for (Object obj : itemIdsList) {
			TextField itemTxtField = (TextField) (((IndexedContainer) container)
					.getItem(obj).getItemProperty(GoldItemContainer.PRICE)
					.getValue());
			itemTxtField.setImmediate(true);
			itemTxtField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalFormattedPrice = String.format("%.3f",
							((CustomItemContainerInterface) table
									.getContainerDataSource()).getTotalPrice());
					table.setColumnFooter(GoldItemContainer.PRICE, "Sum="
							+ totalFormattedPrice);
					table.setColumnFooter(GoldItemContainer.DELETE,
							("Items=" + table.size()));
					pfForm.totalItemPrice.setValue(String.format("%.3f",
							getTotalGoldSilverDiamondGeneralItemsPrice()));
				}
			});
			Image deleteImage = (Image) (((IndexedContainer) container)
					.getItem(obj).getItemProperty(GoldItemContainer.DELETE)
					.getValue());
			deleteImage.addClickListener(new MouseEvents.ClickListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = -1429589095114426407L;

				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					table.setPageLength(table.size());
					String totalFormattedPrice = String.format("%.3f",
							((CustomItemContainerInterface) table
									.getContainerDataSource()).getTotalPrice());
					table.setColumnFooter(GoldItemContainer.PRICE, "Sum="
							+ totalFormattedPrice);
					table.setColumnFooter(GoldItemContainer.DELETE,
							("Items=" + table.size()));
					pfForm.totalItemPrice.setValue(String.format("%.3f",
							getTotalGoldSilverDiamondGeneralItemsPrice()));
				}
			});
		}
	}

	double getTotalGoldSilverDiamondGeneralItemsPrice() {
		double totalGoldPrice = ((CustomItemContainerInterface) goldItemContainer)
				.getTotalPrice();
		double totalSilverPrice = ((CustomItemContainerInterface) silverItemContainer)
				.getTotalPrice();
		double totalDiamondPrice = ((CustomItemContainerInterface) diamondItemContainer)
				.getTotalPrice();
		double totalGeneralPrice = ((CustomItemContainerInterface) generalItemContainer)
				.getTotalPrice();
		return totalGoldPrice + totalSilverPrice + totalDiamondPrice
				+ totalGeneralPrice;
	}
}
