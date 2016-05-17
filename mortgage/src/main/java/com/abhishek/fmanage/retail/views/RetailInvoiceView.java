package com.abhishek.fmanage.retail.views;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.hene.flexibleoptiongroup.FlexibleOptionGroup;
import org.vaadin.hene.flexibleoptiongroup.FlexibleOptionGroupItemComponent;

import com.abhishek.fmanage.csv.utility.CustomShopSettingFileUtility;
import com.abhishek.fmanage.mortgage.data.bean.Customer;
import com.abhishek.fmanage.mortgage.data.container.CustomItemContainerInterface;
import com.abhishek.fmanage.mortgage.data.container.ItemContainerType;
import com.abhishek.fmanage.retail.bean.RetailTransactionBean;
import com.abhishek.fmanage.retail.data.container.DiamondItemContainer;
import com.abhishek.fmanage.retail.data.container.GeneralItemContainer;
import com.abhishek.fmanage.retail.data.container.GoldItemContainer;
import com.abhishek.fmanage.retail.data.container.SilverItemContainer;
import com.abhishek.fmanage.retail.form.PriceForm;
import com.abhishek.fmanage.retail.pdf.InvoiceGenerator;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ResourceReference;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;

public class RetailInvoiceView extends VerticalLayout implements View{

	private final Logger logger = LoggerFactory.getLogger(RetailInvoiceView.class);
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
	private PriceForm pfForm = new PriceForm(getPricePropertyItem());
	private HorizontalLayout priceLayout = new HorizontalLayout();
	private Panel p = new Panel();
	ComboBox staffNameComboBox = new ComboBox("Staff Name");
	private Customer cusBean = new Customer();
	private CheckBox includePrice = new CheckBox("Include Price", true);
	private FlexibleOptionGroup billType;
	private TextArea notes = new TextArea("Invoice Notes");
	TextField invoiceNumberTxt;
	private static final String TIN_NUMBER = CustomShopSettingFileUtility.getInstance().getTinNumber();
	
	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();
		goldBillingTable = getGoldBillingTable();
		silverBillingTable = getSilverItemTable();
		diamondBillingTable = getDiamondTable();
		generalBillingTable = getGeneralTable();
		
		
		VerticalLayout goldBillingLayout = getBillingLayout(goldBillingTable, goldItemContainer, ItemContainerType.GOLD);
		VerticalLayout silverBillingLayout = getBillingLayout(silverBillingTable, silverItemContainer, ItemContainerType.SILVER);
		VerticalLayout diamondBillingLayout = getBillingLayout(diamondBillingTable, diamondItemContainer, ItemContainerType.DIAMOND);
		VerticalLayout generalBillingLayout = getBillingLayout(generalBillingTable, generalItemContainer, ItemContainerType.GENERAL);

		retailViewVerticalLayout.addComponent(getUserDetailFormLayout());
		retailViewVerticalLayout.addComponent(goldBillingLayout);
		retailViewVerticalLayout.addComponent(silverBillingLayout);
		retailViewVerticalLayout.addComponent(diamondBillingLayout);
		retailViewVerticalLayout.addComponent(generalBillingLayout);
		retailViewVerticalLayout.addComponent(getCalculatedPriceLayout());
		retailViewVerticalLayout.setSpacing(true);
		retailViewVerticalLayout.setImmediate(true);
		p.setSizeFull();
		
		HorizontalLayout toolbarLayout = new HorizontalLayout();
		toolbarLayout.addComponent(getToolbar());
		toolbarLayout.setSizeUndefined();
		toolbarLayout.setSizeFull();
		toolbarLayout.addStyleName("sidebar");
		
		VerticalSplitPanel vSplitPanel = new VerticalSplitPanel();
		vSplitPanel.setFirstComponent(toolbarLayout);
		vSplitPanel.setSecondComponent(retailViewVerticalLayout);
		vSplitPanel.setSizeFull();
		vSplitPanel.setSplitPosition(12, Unit.PERCENTAGE);
		Panel toolBarPanel = new Panel();
		toolBarPanel.setSizeUndefined();
		toolBarPanel.setWidth("100%");
		addComponent(vSplitPanel);
		//addComponent(new RetailInvoiceViewContent(new PopupDateField(), billType,  new Customer(), new GoldItemContainer(), 
			//	new SilverItemContainer(), new DiamondItemContainer(), new PriceForm(getPricePropertyItem()),
				//new CheckBox("Include Price", true), new TextArea("Invoice Notes")).generate());
	}

	private Table getGeneralTable() {
		 Table generalItemTable = new Table();
	        generalItemTable.setSizeFull();
	        generalItemTable.addStyleName("borderless");
	        generalItemTable.setSelectable(false);
	        generalItemTable.setColumnCollapsingAllowed(false);
	        generalItemTable.setColumnReorderingAllowed(true);
	        generalItemTable.setWidth("95%");
	        generalItemTable.addStyleName("general-table");
	        generalItemTable.addStyleName("general-table-header");
	        generalItemTable.addStyleName("general-table-footer");
	        generalItemTable.addStyleName("general-table-footer-container");
	        generalItemTable.setContainerDataSource(generalItemContainer);
	        generalItemTable.setVisibleColumns(new Object[] {
	        		GeneralItemContainer.DELETE,
	        		GeneralItemContainer.ITEM_NAME,
	        		GeneralItemContainer.QUANTITY,
	        		GeneralItemContainer.PIECE_PAIR,
	        		GeneralItemContainer.WEIGHT,
	        		GeneralItemContainer.PRICE_PER_PIECE_PAIR,
	        		GeneralItemContainer.PRICE
	            });
	        generalItemTable.setFooterVisible(true);
	        generalItemTable.setMultiSelect(false);
	        generalItemTable.setPageLength(0);
	        generalItemTable.setColumnWidth(GeneralItemContainer.ITEM_NAME, 180);
	        generalItemTable.setColumnWidth(GeneralItemContainer.QUANTITY, 60);
	        generalItemTable.setColumnWidth(GeneralItemContainer.WEIGHT, 90);
	        String totalFormattedDiamondPrice = String.format("%.3f",((GeneralItemContainer)generalItemTable.getContainerDataSource()).getTotalPrice()); 
	        generalItemTable.setColumnFooter(GeneralItemContainer.PRICE, totalFormattedDiamondPrice);
	        generalItemTable.setColumnFooter(GeneralItemContainer.DELETE, "Items=" + generalItemTable.size());
	        for (Object obj : generalItemTable.getItemIds()){
	        	TextField itemTxtField = (TextField)(generalItemContainer.getItem(obj).getItemProperty(GeneralItemContainer.PRICE).getValue());
	            itemTxtField.setImmediate(true);
	            itemTxtField.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						String totalFormattedDiamondPrice = String.format("%.3f",((GeneralItemContainer)generalItemTable.getContainerDataSource()).getTotalPrice());
						generalItemTable.setColumnFooter(GeneralItemContainer.PRICE, totalFormattedDiamondPrice);
					}
				});
	        }
	        //diamondTableWeightContainerValueChange();
	        addCustomTableDataContainerPriceItemCountValueChange(generalItemTable, generalItemContainer);
	        generalItemTable.setImmediate(true);
	        return generalItemTable;
	}

	private Component getCalculatedPriceLayout()
	{
		priceLayout.setSizeFull();
		priceLayout.setSpacing(true);
		priceLayout.addStyleName("sidebar");
        priceLayout.addComponent(pfForm);
        notes.setSizeFull();
        Button generateBillBtn = getGenerateBillBtn(cusBean);
        priceLayout.addComponent(notes);
        priceLayout.addComponent(includePrice);
        priceLayout.addComponent(generateBillBtn);
        priceLayout.setSpacing(true);
        priceLayout.setExpandRatio(notes, 1);
        priceLayout.setExpandRatio(includePrice, 0.8f);
        
        priceLayout.setExpandRatio(pfForm, 1.2f);
        priceLayout.setExpandRatio(generateBillBtn, 0.8f);
        return priceLayout;
	}

	private PropertysetItem getPricePropertyItem() {
		PropertysetItem item = new PropertysetItem();
        item.addItemProperty("totalItemPrice", new ObjectProperty<Double>(0.0));
        item.addItemProperty("vatOnNewItemPrice", new ObjectProperty<Double>(0.0));
        item.addItemProperty("oldPurchasePrice", new ObjectProperty<Double>(0.0));
        item.addItemProperty("discountPrice", new ObjectProperty<Double>(0.0));
        item.addItemProperty("netAmountToPay", new ObjectProperty<Double>(0.0));
		return item;
	}

	private Component getUserDetailFormLayout() {
		
		FormLayout personDetailFormlayout1 = new FormLayout();
		FormLayout personDetailFormlayout2 = new FormLayout();
		FormLayout personDetailFormlayout3 = new FormLayout();
		
		final BeanFieldGroup<Customer> binder = new BeanFieldGroup<Customer>(
				Customer.class);
		binder.setItemDataSource(cusBean);
		binder.setBuffered(false);
		
		personDetailFormlayout1.setEnabled(true);
		personDetailFormlayout1.addComponent(binder.buildAndBind("FirstName", "firstName"));
		personDetailFormlayout1.addComponent(binder.buildAndBind("LastName", "lastName"));
		personDetailFormlayout1.addComponent(binder.buildAndBind("ContactNumber", "contactNumber"));
		personDetailFormlayout1.addComponent(binder.buildAndBind("EmailId", "emailId"));
		
		personDetailFormlayout2.addComponent(binder.buildAndBind("Street Address1", "streetAddress1"));
		personDetailFormlayout2.addComponent(binder.buildAndBind("Street Address2", "streetAddress2"));
		personDetailFormlayout2.addComponent(binder.buildAndBind("City", "city"));
		personDetailFormlayout2.addComponent(binder.buildAndBind("State", "stateprovince"));
		
		personDetailFormlayout3.addComponent(binder.buildAndBind("Zipcode", "zipcode"));
		personDetailFormlayout3.addComponent(binder.buildAndBind("Country", "country"));
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
	
		customerInformationPanel.setCaption("<font size=\"2\" color=\"green\"><b>Customer Information</b></font>");
		customerInformationPanel.setCaptionAsHtml(true);
		//customerInformationPanel.addStyleName("customerInfoPanel");
		return customerInformationPanel;
	}


	
	private HorizontalLayout getToolbar(){
		HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidth("100%");
        toolbar.setSpacing(true);
        toolbar.setMargin(true);
        toolbar.addStyleName("toolbar");
        
       
        
        Button newBillBtn = new Button("New Bill");
        newBillBtn.setSizeUndefined();
        newBillBtn.addStyleName("icon-newbill");
        newBillBtn.addStyleName("sidebar");

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

		Label tinLabel = new Label("<b>TIN VAT NO:</b>" + TIN_NUMBER, ContentMode.HTML);
		tinLabel.setStyleName("vinHiddenLabel");
		tinLabel.setImmediate(true);

		invoiceNumberTxt = new TextField("Invoice Number");
		invoiceNumberTxt.setImmediate(true);
		invoiceNumberTxt.setVisible(false);
		invoiceNumberTxt.setRequired(true);
		invoiceNumberTxt.setValidationVisible(true);
		invoiceNumberTxt.setWidth("100%");
		
		// A single-select radio button group
		billType = new FlexibleOptionGroup();
		billType.addItems(ESTIMATE_BILL, INVOICE_BILL);
		billType.setValue(ESTIMATE_BILL);
		
		billType.addValueChangeListener((value) -> {
			if(!value.getProperty().getValue().equals(ESTIMATE_BILL)){
				tinLabel.setStyleName("vinVisibleLabel");
				pfForm.isInvoiceEnabled = true;
				pfForm.vatOnNewItemPrice.setValue((String.format("%.3f", pfForm.getVatPrice())));
				pfForm.netAmountToPay.setValue(String.format("%.3f", pfForm.getTotalNetAmount()));
				pfForm.advancePayment.setValue(String.format("%.3f", 0.000f));
				pfForm.advancePayment.setEnabled(false);
				pfForm.balanceAmount.setEnabled(false);
				invoiceNumberTxt.setVisible(true);
			}else{
				tinLabel.setStyleName("vinHiddenLabel");
				pfForm.isInvoiceEnabled = false;
				pfForm.vatOnNewItemPrice.setValue(String.format("%.3f", 0.000f));
				pfForm.netAmountToPay.setValue(String.format("%.3f", pfForm.getTotalNetAmount()));
				pfForm.vatOnNewItemPrice.setEnabled(false);
				pfForm.advancePayment.setEnabled(true);
				pfForm.balanceAmount.setEnabled(true);
				invoiceNumberTxt.setVisible(false);
				
			}
			});
		HorizontalLayout optionGroupLayout = new HorizontalLayout();
		for (Iterator<FlexibleOptionGroupItemComponent> iter = billType
		        .getItemComponentIterator(); iter.hasNext();) {
		    FlexibleOptionGroupItemComponent comp = iter.next();

		    // Add FlexibleOptionGroupItemComponents instead
		    optionGroupLayout.addComponent(comp);
		    optionGroupLayout.setStyleName("myLabel");
		    Label captionLabel = new Label();
		    captionLabel.setIcon(comp.getIcon());
		    captionLabel.setCaption(comp.getCaption());
		    captionLabel.setStyleName("myLabel");
		    optionGroupLayout.addComponent(captionLabel);
		    optionGroupLayout.setComponentAlignment(captionLabel, Alignment.MIDDLE_LEFT);
		}
//		ThemeResource logoItemImageResource = new ThemeResource("img/apnaJewelleryLogo.jpg");
//		final Image shopLogoImage = new Image("", logoItemImageResource);
//		shopLogoImage.setHeight("40px");
//		shopLogoImage.setWidth("40px");
//		shopLogoImage.setDescription("Logo");
		toolbar.addComponent(optionGroupLayout);
		toolbar.addComponent(tinLabel);
		toolbar.addComponent(invoiceNumberTxt);
		
		
		//toolbar.addComponent(shopLogoImage);
		toolbar.setExpandRatio(newBillBtn, 1);
		toolbar.setExpandRatio(staffListCombo, 1);
	    toolbar.setExpandRatio(billPopUpDate, 1);
	    toolbar.setExpandRatio(optionGroupLayout, 1);
	    //toolbar.setExpandRatio(shopLogoImage, 1.5f);
	    toolbar.setExpandRatio(tinLabel, 1);
	    toolbar.setExpandRatio(invoiceNumberTxt, 1);
	    toolbar.setComponentAlignment(newBillBtn, Alignment.MIDDLE_LEFT);
	    toolbar.setComponentAlignment(staffListCombo, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(billPopUpDate, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(optionGroupLayout, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(tinLabel, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(invoiceNumberTxt, Alignment.MIDDLE_LEFT);
		//toolbar.setComponentAlignment(shopLogoImage, Alignment.TOP_CENTER);
        return toolbar;
	}
	
	private ComboBox getStaffListComboBox() {
		
		staffNameComboBox.setNullSelectionAllowed(false);
		staffNameComboBox.addItem("STAFF");
		staffNameComboBox.setValue("STAFF");
		ArrayList<String> staffNameListFromCsvFile = (ArrayList<String>) CustomShopSettingFileUtility.getInstance().getStaffNameList();
		for(String staffName : staffNameListFromCsvFile){
			staffNameComboBox.addItem(staffName);
		}
		return staffNameComboBox;
	}

	private Button getGenerateBillBtn(Customer cusBean) {
		Button generateBillBtn = new Button("Generate Bill");
		generateBillBtn.setSizeUndefined();
		generateBillBtn.addStyleName("sidebar");
		generateBillBtn.setData(this);
		generateBillBtn.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				String fileName = "";
				try {
					boolean isEstimateBill = billType.getValue().equals(ESTIMATE_BILL);
					Date invoiceDate = billPopUpDate.getValue();
					String invoiceNumber = "0";
					if(!isEstimateBill)
					{
						invoiceNumber = invoiceNumberTxt.getValue().toString();
					}
					boolean isInvoiceCancelled = false;
					RetailTransactionBean retailTransaction = new ExtractRetailTransaction(
							goldBillingTable,
							 silverBillingTable,
							 diamondBillingTable,
							 generalBillingTable,
							 cusBean,
							 pfForm,
							 isEstimateBill,
							 invoiceDate,
							 TIN_NUMBER,
							 Long.parseLong(invoiceNumber),
							 staffNameComboBox.getValue().toString(), 
							 includePrice.getValue(), 
							 notes.getValue(), 
							 isInvoiceCancelled).extract();
					
					
					 fileName = new InvoiceGenerator(retailTransaction).createPdf();

				} catch (Exception e) {
					logger.error("Error generating invoice", e);
				}
				
				if(Page.getCurrent().getWebBrowser().isTouchDevice()){
					Resource pdf = new FileResource(new File(fileName));
				    setResource("help", pdf);
				    ResourceReference rr = ResourceReference.create(pdf, (RetailInvoiceView)generateBillBtn.getData(), "help");
				    Page.getCurrent().open(rr.getURL(), "blank_");
				}else{
				File pdfFile = new File(fileName);
                Embedded pdf = new Embedded("", new FileResource(pdfFile));
                pdf.setMimeType("application/pdf");
                pdf.setType(Embedded.TYPE_BROWSER);
                pdf.setWidth("100%");
                pdf.setHeight("90%");
                
                String windowName = "Invoice Bill";
                if(billType.getValue().equals(ESTIMATE_BILL)){
                	windowName = "Estimate Letter";
                }
			    Window w = new Window(windowName);
			    w.setModal(true);
				w.center();
				w.setCloseShortcut(KeyCode.ESCAPE, null);
				w.setResizable(false);
				w.setWidth("90%");
				w.setHeight("90%");
				w.setClosable(true);
				w.addStyleName("no-vertical-drag-hints");
				w.addStyleName("no-horizontal-drag-hints");
				w.setContent(pdf);
	            UI.getCurrent().addWindow(w);
	            w.focus();
			}
           }
        });
		return generateBillBtn;
	}

	private VerticalLayout getBillingLayout(Table table, CustomItemContainerInterface container, ItemContainerType itemContainerType)
	{
		String buttonType = "Gold";
        switch(itemContainerType.ordinal()){
     	case 0: buttonType = "Add Gold Item";
     		break;
     	case 1: buttonType = "Add Silver Item";
     		break;
     	case 2: buttonType = "Add Diamond Item";
     		break;
     	case 3: buttonType = "Add General Item";
     }
        Button newBillBtn = new Button(buttonType);
        newBillBtn.setSizeUndefined();
        newBillBtn.addStyleName("icon-newbill");
        newBillBtn.addStyleName("sidebar");

        newBillBtn.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				container.addCustomItem();
				table.setPageLength(table.size());
				addCustomTableDataContainerPriceItemCountValueChange(table, container);
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
		 Table diamondItemTable = new Table();
	        diamondItemTable.setSizeFull();
	        diamondItemTable.addStyleName("borderless");
	        diamondItemTable.setSelectable(false);
	        diamondItemTable.setColumnCollapsingAllowed(false);
	        diamondItemTable.setColumnReorderingAllowed(true);
	        diamondItemTable.setWidth("95%");
	        diamondItemTable.addStyleName("diamond-table");
	        diamondItemTable.addStyleName("diamond-table-header");
	        diamondItemTable.addStyleName("diamond-table-footer");
	        diamondItemTable.addStyleName("diamond-table-footer-container");
	        diamondItemTable.setContainerDataSource(diamondItemContainer);
	        diamondItemTable.setVisibleColumns(new Object[] {
	        		DiamondItemContainer.DELETE,
	        		DiamondItemContainer.ITEM_NAME,
	        		DiamondItemContainer.QUANTITY,
	        		DiamondItemContainer.PIECE_PAIR,
	        		DiamondItemContainer.GOLD_WEIGHT,
	        		DiamondItemContainer.DIAMOND_WEIGHT,
	        		DiamondItemContainer.DIAMOND_PIECE,
	        		DiamondItemContainer.CERTIFICATE,
	        		DiamondItemContainer.PRICE
	            });
	        diamondItemTable.setFooterVisible(true);
	        diamondItemTable.setMultiSelect(false);
	        diamondItemTable.setPageLength(0);
	        diamondItemTable.setColumnWidth(DiamondItemContainer.ITEM_NAME, 180);
	        diamondItemTable.setColumnWidth(DiamondItemContainer.QUANTITY, 60);
	        diamondItemTable.setColumnWidth(DiamondItemContainer.GOLD_WEIGHT, 90);
	        diamondItemTable.setColumnWidth(DiamondItemContainer.DIAMOND_WEIGHT, 125);
	        String totalFormattedDiamondPrice = String.format("%.3f",((DiamondItemContainer)diamondItemTable.getContainerDataSource()).getTotalPrice()); 
	        diamondItemTable.setColumnFooter(DiamondItemContainer.PRICE, totalFormattedDiamondPrice);
	        diamondItemTable.setColumnFooter(DiamondItemContainer.GOLD_WEIGHT, String.format("%.3f",diamondItemContainer.getTotalGoldWeight()));
	        diamondItemTable.setColumnFooter(DiamondItemContainer.DIAMOND_WEIGHT, String.format("%.2f",diamondItemContainer.getTotalDiamondWeight()));
	        diamondItemTable.setColumnFooter(DiamondItemContainer.DELETE, "Items=" + diamondItemTable.size());
	        for (Object obj : diamondItemTable.getItemIds()){
	        	TextField itemTxtField = (TextField)(diamondItemContainer.getItem(obj).getItemProperty(DiamondItemContainer.PRICE).getValue());
	            itemTxtField.setImmediate(true);
	            itemTxtField.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						String totalFormattedDiamondPrice = String.format("%.3f",((DiamondItemContainer)diamondItemTable.getContainerDataSource()).getTotalPrice());
						diamondItemTable.setColumnFooter(DiamondItemContainer.PRICE, totalFormattedDiamondPrice);
					}
				});
	        }
	        diamondTableWeightContainerValueChange();
	        addCustomTableDataContainerPriceItemCountValueChange(diamondItemTable, diamondItemContainer);
	        diamondItemTable.setImmediate(true);
	        return diamondItemTable;
	}

	private Table getSilverItemTable() {
		 Table silverItemTable = new Table();
	        silverItemTable.setSizeFull();
	        silverItemTable.addStyleName("borderless");
	        silverItemTable.setSelectable(false);
	        silverItemTable.setColumnCollapsingAllowed(false);
	        silverItemTable.setColumnReorderingAllowed(true);
	        silverItemTable.setWidth("95%");
	        //silverItemContainer.addCustomItem();
	        silverItemTable.addStyleName("silver-table");
	        silverItemTable.addStyleName("silver-table-header");
	        silverItemTable.addStyleName("silver-table-footer");
	        silverItemTable.addStyleName("silver-table-footer-container");
	        //silverItemTable.addStyleName("billing-table-row");
	        silverItemTable.setContainerDataSource(silverItemContainer);
	       
	        silverItemTable.setVisibleColumns(new Object[] {
	        		SilverItemContainer.DELETE,
	        		SilverItemContainer.ITEM_NAME,
	        		SilverItemContainer.QUANTITY,
	        		SilverItemContainer.PIECE_PAIR,
	        		SilverItemContainer.WEIGHT,
	        		SilverItemContainer.MAKING_CHARGE,
	        		SilverItemContainer.MAKING_CHARGE_TYPE,
	        		SilverItemContainer.SILVER_RATE,
	        		SilverItemContainer.PRICE
	            });
	        silverItemTable.setFooterVisible(true);
	        silverItemTable.setMultiSelect(false);
	        silverItemTable.setPageLength(0);
	        silverItemTable.setColumnWidth(SilverItemContainer.DELETE, 70);
	        silverItemTable.setColumnWidth(SilverItemContainer.ITEM_NAME, 180);
	        silverItemTable.setColumnWidth(SilverItemContainer.QUANTITY, 60);
	        silverItemTable.setColumnWidth(SilverItemContainer.WEIGHT, 70);
	        silverItemTable.setColumnWidth(SilverItemContainer.PIECE_PAIR, 100);
	        silverItemTable.setColumnWidth(SilverItemContainer.SILVER_RATE, 125);
	        silverItemTable.setColumnWidth(SilverItemContainer.MAKING_CHARGE, 95);
	        silverItemTable.setColumnWidth(SilverItemContainer.MAKING_CHARGE_TYPE, 105);
	        String totalFormattedSilverPrice = String.format("%.3f",((SilverItemContainer)silverItemTable.getContainerDataSource()).getTotalPrice()); 
	        silverItemTable.setColumnFooter(SilverItemContainer.PRICE, totalFormattedSilverPrice);
	        silverItemTable.setColumnFooter(SilverItemContainer.WEIGHT, String.format("%.3f",silverItemContainer.getTotalWeight()));
	        silverItemTable.setColumnFooter(SilverItemContainer.DELETE, ("Items=" + silverItemTable.size()));
	        silverTableWeightContainerValueChange();
	        addCustomTableDataContainerPriceItemCountValueChange(silverItemTable, silverItemContainer);
	        silverItemTable.setImmediate(true);
	        return silverItemTable;
	}

	private Table getGoldBillingTable() {
		 Table goldItemTable = new Table();
		  goldItemTable.setSizeFull();
	        goldItemTable.addStyleName("borderless");
	        goldItemTable.setSelectable(false);
	        goldItemTable.setColumnCollapsingAllowed(false);
	        goldItemTable.setColumnReorderingAllowed(true);
	        goldItemTable.setWidth("95%");
	        goldItemTable.setContainerDataSource(goldItemContainer);
	        goldItemTable.addStyleName("gold-table");
	        goldItemTable.addStyleName("gold-table-header");
	        goldItemTable.addStyleName("gold-table-footer");
	        goldItemTable.addStyleName("gold-table-footer-container");
	        goldItemTable.setVisibleColumns(new Object[] {
	        		GoldItemContainer.DELETE,
	        		GoldItemContainer.GOLD_TYPE,
	        		GoldItemContainer.ITEM_NAME,
	        		GoldItemContainer.QUANTITY,
	        		GoldItemContainer.PIECE_PAIR,
	        		GoldItemContainer.WEIGHT,
	        		GoldItemContainer.MAKING_CHARGE,
	        		GoldItemContainer.MAKING_CHARGE_TYPE,
	        		GoldItemContainer.GOLD_RATE,
	        		GoldItemContainer.PRICE
	            });
	        goldItemTable.setFooterVisible(true);
	        goldItemTable.setMultiSelect(false);
	        goldItemTable.setPageLength(0);
	        goldItemTable.setColumnWidth(GoldItemContainer.DELETE, 70);
	        goldItemTable.setColumnWidth(GoldItemContainer.ITEM_NAME, 180);
	        goldItemTable.setColumnWidth(GoldItemContainer.GOLD_TYPE, 80);
	        goldItemTable.setColumnWidth(GoldItemContainer.QUANTITY, 60);
	        goldItemTable.setColumnWidth(GoldItemContainer.WEIGHT, 60);
	        goldItemTable.setColumnWidth(GoldItemContainer.MAKING_CHARGE, 95);
	        goldItemTable.setColumnWidth(GoldItemContainer.GOLD_RATE, 110);
	        goldItemTable.setColumnWidth(GoldItemContainer.MAKING_CHARGE_TYPE, 100);
	        goldItemTable.setColumnWidth(GoldItemContainer.PIECE_PAIR, 100);
	        goldItemTable.setColumnWidth(GoldItemContainer.PRICE, 120);
	        String totalFormattedGoldPrice = String.format("%.3f",((GoldItemContainer)goldItemTable.getContainerDataSource()).getTotalPrice()); 
	        goldItemTable.setColumnFooter(GoldItemContainer.PRICE, totalFormattedGoldPrice);
	        goldItemTable.setColumnFooter(GoldItemContainer.WEIGHT, String.format("%.3f",goldItemContainer.getTotalWeight()));
	        goldItemTable.setColumnFooter(GoldItemContainer.DELETE, ("Items=" + goldItemTable.size()));
	        goldTableWeightContainerValueChange();
	        addCustomTableDataContainerPriceItemCountValueChange(goldItemTable, goldItemContainer);
	        goldItemTable.setImmediate(true);
	        return goldItemTable;
	}

	private void goldTableWeightContainerValueChange()
	{
		Collection<?> itemIdsList = goldBillingTable.getItemIds();
		for (Object obj : itemIdsList){
			TextField itemTxtField = (TextField)((goldItemContainer.getItem(obj).getItemProperty(GoldItemContainer.WEIGHT).getValue()));
		    itemTxtField.setImmediate(true);
		    itemTxtField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalFormattedWeight = String.format("%.3f",(goldItemContainer).getTotalWeight()); 
					goldBillingTable.setColumnFooter(GoldItemContainer.WEIGHT, totalFormattedWeight);
					pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondGeneralItemsPrice()));
				}
			});
		    Image deleteImage = (Image)((goldItemContainer.getItem(obj).getItemProperty(DiamondItemContainer.DELETE).getValue()));
		    deleteImage.addClickListener(new MouseEvents.ClickListener()
	           {
	               @Override
	               public void click(com.vaadin.event.MouseEvents.ClickEvent event)
	               {
            		   goldBillingTable.setPageLength(goldBillingTable.size());
	            	   String totalFormattedGoldWeight = String.format("%.3f",(goldItemContainer).getTotalWeight()); 
	            	   goldBillingTable.setColumnFooter(GoldItemContainer.WEIGHT, totalFormattedGoldWeight);
					   pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondGeneralItemsPrice()));
	               }
	           });
		}
	}
	
	private void silverTableWeightContainerValueChange()
	{
		Collection<?> itemIdsList = silverBillingTable.getItemIds();
		for (Object obj : itemIdsList){
			TextField itemTxtField = (TextField)((silverItemContainer.getItem(obj).getItemProperty(SilverItemContainer.WEIGHT).getValue()));
		    itemTxtField.setImmediate(true);
		    itemTxtField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalFormattedWeight = String.format("%.3f",(silverItemContainer).getTotalWeight()); 
					silverBillingTable.setColumnFooter(SilverItemContainer.WEIGHT, totalFormattedWeight);
					pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondGeneralItemsPrice()));
					
				}
			});
		    Image deleteImage = (Image)((silverItemContainer.getItem(obj).getItemProperty(DiamondItemContainer.DELETE).getValue()));
		    deleteImage.addClickListener(new MouseEvents.ClickListener()
	           {
	               @Override
	               public void click(com.vaadin.event.MouseEvents.ClickEvent event)
	               {
	            	   silverBillingTable.setPageLength(silverBillingTable.size());
	            	   String totalFormattedSilverWeight = String.format("%.3f",(silverItemContainer).getTotalWeight()); 
	            	   silverBillingTable.setColumnFooter(SilverItemContainer.WEIGHT, totalFormattedSilverWeight);
					   pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondGeneralItemsPrice()));
	               }
	           });
		}
	}
	
	private void diamondTableWeightContainerValueChange()
	{
		Collection<?> itemIdsList = diamondBillingTable.getItemIds();
		for (Object obj : itemIdsList){
			TextField itemTxtField = (TextField)((diamondItemContainer.getItem(obj).getItemProperty(DiamondItemContainer.GOLD_WEIGHT).getValue()));
		    itemTxtField.setImmediate(true);
		    itemTxtField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalFormattedWeight = String.format("%.3f",(diamondItemContainer).getTotalGoldWeight()); 
					diamondBillingTable.setColumnFooter(DiamondItemContainer.GOLD_WEIGHT, totalFormattedWeight);
					pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondGeneralItemsPrice()));
				}
			});
		    TextField diamondWtTextField = (TextField)((diamondItemContainer.getItem(obj).getItemProperty(DiamondItemContainer.DIAMOND_WEIGHT).getValue()));
		    diamondWtTextField.setImmediate(true);
		    diamondWtTextField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalFormattedWeight = String.format("%.2f",(diamondItemContainer).getTotalDiamondWeight()); 
					diamondBillingTable.setColumnFooter(DiamondItemContainer.DIAMOND_WEIGHT, totalFormattedWeight);
					pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondGeneralItemsPrice()));
				}
			});
		    Image deleteImage = (Image)((diamondItemContainer.getItem(obj).getItemProperty(DiamondItemContainer.DELETE).getValue()));
		    deleteImage.addClickListener(new MouseEvents.ClickListener()
	           {
	               @Override
	               public void click(com.vaadin.event.MouseEvents.ClickEvent event)
	               {
	            	   diamondBillingTable.setPageLength(diamondBillingTable.size());
	            	   String totalFormattedGoldWeight = String.format("%.3f",(diamondItemContainer).getTotalGoldWeight()); 
					   diamondBillingTable.setColumnFooter(DiamondItemContainer.GOLD_WEIGHT, totalFormattedGoldWeight);
					   String totalFormattedDiamondWeight = String.format("%.2f",(diamondItemContainer).getTotalDiamondWeight()); 
					   diamondBillingTable.setColumnFooter(DiamondItemContainer.DIAMOND_WEIGHT, totalFormattedDiamondWeight);
					   pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondGeneralItemsPrice()));
	               }
	           });
		}
	}
	
	private void addCustomTableDataContainerPriceItemCountValueChange(Table table, CustomItemContainerInterface container) {
		Collection<?> itemIdsList = table.getItemIds();
		table.setColumnFooter(GoldItemContainer.DELETE, ("Items=" + table.size()));
		for (Object obj : itemIdsList){
			TextField itemTxtField = (TextField)(((IndexedContainer)container).getItem(obj).getItemProperty(GoldItemContainer.PRICE).getValue());
		    itemTxtField.setImmediate(true);
		    itemTxtField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalFormattedPrice = String.format("%.3f",((CustomItemContainerInterface)table.getContainerDataSource()).getTotalPrice()); 
					table.setColumnFooter(GoldItemContainer.PRICE, "Sum=" + totalFormattedPrice);
					table.setColumnFooter(GoldItemContainer.DELETE, ("Items=" + table.size()));
					pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondGeneralItemsPrice()));
				}
			});
		    Image deleteImage = (Image)(((IndexedContainer)container).getItem(obj).getItemProperty(GoldItemContainer.DELETE).getValue());
		    deleteImage.addClickListener(new MouseEvents.ClickListener()
	           {
	               @Override
	               public void click(com.vaadin.event.MouseEvents.ClickEvent event)
	               {
	            	  table.setPageLength(table.size());
					   String totalFormattedPrice = String.format("%.3f",((CustomItemContainerInterface)table.getContainerDataSource()).getTotalPrice()); 
	            	   table.setColumnFooter(GoldItemContainer.PRICE, "Sum=" + totalFormattedPrice);
	            	   table.setColumnFooter(GoldItemContainer.DELETE, ("Items=" + table.size()));
	            	   pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondGeneralItemsPrice()));
	               }
	           });
		}
	}
	
	double getTotalGoldSilverDiamondGeneralItemsPrice()
	{	double totalGoldPrice = ((CustomItemContainerInterface)goldItemContainer).getTotalPrice();
		double totalSilverPrice = ((CustomItemContainerInterface)silverItemContainer).getTotalPrice();
		double totalDiamondPrice = ((CustomItemContainerInterface)diamondItemContainer).getTotalPrice();
		double totalGeneralPrice = ((CustomItemContainerInterface)generalItemContainer).getTotalPrice();
		return totalGoldPrice + totalSilverPrice + totalDiamondPrice + totalGeneralPrice;
	}
}
