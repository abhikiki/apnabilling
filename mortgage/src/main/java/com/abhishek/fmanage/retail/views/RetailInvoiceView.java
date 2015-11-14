package com.abhishek.fmanage.retail.views;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.vaadin.hene.flexibleoptiongroup.FlexibleOptionGroup;
import org.vaadin.hene.flexibleoptiongroup.FlexibleOptionGroupItemComponent;

import com.abhishek.fmanage.mortgage.data.bean.Customer;
import com.abhishek.fmanage.mortgage.data.container.CustomItemContainerInterface;
import com.abhishek.fmanage.mortgage.data.container.ItemContainerType;
import com.abhishek.fmanage.retail.data.container.DiamondItemContainer;
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
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
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

    public static final String INDIAN_DATE_FORMAT = "dd/MM/yyyy";
    public static final String SELECT_DATE = "Select Date";
    
	private static final long serialVersionUID = 1L;
	private GoldItemContainer goldItemContainer = new GoldItemContainer();
	private SilverItemContainer silverItemContainer = new SilverItemContainer();
	private DiamondItemContainer diamondItemContainer = new DiamondItemContainer();
	private Table goldBillingTable = new Table();
	private Table silverBillingTable = new Table();
	private Table diamondBillingTable = new Table(); 
    private final PopupDateField billPopUpDate = new PopupDateField();
	private VerticalLayout retailViewVerticalLayout = new VerticalLayout();
	private PriceForm pfForm = new PriceForm(getPricePropertyItem());
	private HorizontalLayout priceLayout = new HorizontalLayout();
	private Panel p = new Panel();
	private Customer cusBean = new Customer();
	private CheckBox includePrice = new CheckBox("Include Price", true);
	private FlexibleOptionGroup billType;
	TextArea notes = new TextArea("Invoice Notes");
	private static final String VIN_NUMBER = "10100310077";
	
	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();
		goldBillingTable = getGoldBillingTable();
		silverBillingTable = getSilverItemTable();
		diamondBillingTable = getDiamondTable();
		
		VerticalLayout goldBillingLayout = getBillingLayout(goldBillingTable, goldItemContainer, ItemContainerType.GOLD);
		VerticalLayout silverBillingLayout = getBillingLayout(silverBillingTable, silverItemContainer, ItemContainerType.SILVER);
		VerticalLayout diamondBillingLayout = getBillingLayout(diamondBillingTable, diamondItemContainer, ItemContainerType.DIAMOND);

		retailViewVerticalLayout.addComponent(getUserDetailFormLayout());
		retailViewVerticalLayout.addComponent(goldBillingLayout);
		retailViewVerticalLayout.addComponent(silverBillingLayout);
		retailViewVerticalLayout.addComponent(diamondBillingLayout);
		retailViewVerticalLayout.addComponent(getCalculatedPriceLayout());
		retailViewVerticalLayout.setSpacing(true);
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
		toolBarPanel.setWidth("100%");;
		addComponent(vSplitPanel);
	}

	private Component getCalculatedPriceLayout()
	{
		priceLayout.setSizeFull();
		priceLayout.setSpacing(true);
		priceLayout.addStyleName("sidebar");
        priceLayout.addComponent(pfForm);
        notes.setSizeFull();
        Button generateBillBtn = testpdf(cusBean);
        priceLayout.addComponent(notes);
        priceLayout.addComponent(includePrice);
        priceLayout.addComponent(generateBillBtn);
        priceLayout.setSpacing(true);
        priceLayout.setExpandRatio(notes, 1);
        priceLayout.setExpandRatio(includePrice, 0.8f);
        priceLayout.setExpandRatio(pfForm, 1);
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

		

		Panel customerInformationPanel = new Panel();
		customerInformationPanel.setWidth("100%");
		customerInformationPanel.setContent(customerInformationLayout);
		customerInformationPanel.setCaption("Customer Information");
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
        billPopUpDate.setCaption("Billing Date");
		billPopUpDate.setImmediate(true);
		billPopUpDate.setInvalidAllowed(false);
		billPopUpDate.setLocale(new Locale("en", "IN"));
		billPopUpDate.setDateFormat(INDIAN_DATE_FORMAT);
		billPopUpDate.setTextFieldEnabled(false);
		billPopUpDate.setInputPrompt(SELECT_DATE);
		billPopUpDate.setValue(new Date());
		toolbar.addComponent(billPopUpDate);

		Label tinLabel = new Label("<b>TIN VAT NO:</b> 10100310077", ContentMode.HTML);
		//tinLabel.setCaption("<b>VIN VAT:</b> 9198761234567");
		//tinLabel.setValue("<b>VIN VAT:</b> 9198761234567");
		tinLabel.setStyleName("vinHiddenLabel");
		tinLabel.setImmediate(true);
		// A single-select radio button group
		billType = new FlexibleOptionGroup();
		billType.addItems("Estimate Bill", "Invoice Bill");
		billType.setValue("Estimate Bill");
		
		billType.addValueChangeListener((value) -> {
			if(!value.getProperty().getValue().equals("Estimate Bill")){
				tinLabel.setStyleName("vinVisibleLabel");
				pfForm.isInvoiceEnabled = true;
				pfForm.vatOnNewItemPrice.setValue((String.format("%.3f", pfForm.getVatPrice())));
				pfForm.netAmountToPay.setValue(String.format("%.3f", pfForm.getTotalNetAmount()));
				//pfForm.vatOnNewItemPrice.setEnabled(true);
			}else{
				tinLabel.setStyleName("vinHiddenLabel");
				pfForm.isInvoiceEnabled = false;
				pfForm.vatOnNewItemPrice.setValue(String.format("%.3f", 0.000f));
				pfForm.netAmountToPay.setValue(String.format("%.3f", pfForm.getTotalNetAmount()));
				pfForm.vatOnNewItemPrice.setEnabled(false);
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
		toolbar.addComponent(optionGroupLayout);
		
		toolbar.addComponent(tinLabel);
		toolbar.setExpandRatio(newBillBtn, 1);
	    toolbar.setExpandRatio(billPopUpDate, 1);
	    toolbar.setExpandRatio(optionGroupLayout, 1);
	    toolbar.setExpandRatio(tinLabel, 1);
	    toolbar.setComponentAlignment(newBillBtn, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(billPopUpDate, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(optionGroupLayout, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(tinLabel, Alignment.MIDDLE_LEFT);
        return toolbar;
	}
	
	private Button testpdf(Customer cusBean) {
		Button generateBillBtn = new Button("Generate Bill");
		generateBillBtn.setSizeUndefined();
		generateBillBtn.addStyleName("sidebar");

		generateBillBtn.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				String fileName = "";
				try {
					boolean isEstimateBill = billType.getValue().equals("Estimate Bill");
					String invoiceDate = billPopUpDate.getValue().toString();
					 fileName = new InvoiceGenerator(
							 goldBillingTable,
							 silverBillingTable,
							 diamondBillingTable, 
							 pfForm, cusBean).createPdf(includePrice.getValue(), VIN_NUMBER, isEstimateBill, invoiceDate, notes);
				} catch (Exception e) {
					Notification.show("Error:" + e.getMessage());
					e.printStackTrace();
				}
				
				File pdfFile = new File(fileName);

//				BrowserFrame pdf = new BrowserFrame("Browser",
//						new FileResource(pdfFile));
//				pdf.setSizeFull();

				//pdf.setWidth("600px");
				//pdf.setHeight("400px");
				//browser.set
					
				
                Embedded pdf = new Embedded("", new FileResource(pdfFile));
                pdf.setMimeType("application/pdf");
                pdf.setType(Embedded.TYPE_BROWSER);
                pdf.setWidth("100%");
                pdf.setHeight("90%");
                
				 Window w = new Window("InvoiceBill");
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
        });
//		 ThemeResource resource = new ThemeResource("img/addButtonSmall.jpg");
//	        Image addNewItemimage = new Image("", resource);
//	        addNewItemimage.setHeight("30px");
//	        addNewItemimage.setWidth("30px");
//	        String description = "";
//	        addNewItemimage.setDescription(description);
//	        addNewItemimage.addClickListener(new MouseEvents.ClickListener()
//	        {
//				private static final long serialVersionUID = 1L;
//
//				@Override
//	            public void click(com.vaadin.event.MouseEvents.ClickEvent event)
//	            {
//					String fileName = "";
//					try {
//						boolean isEstimateBill = billType.getValue().equals("Estimate Bill");
//						String invoiceDate = billPopUpDate.getValue().toString();
//						 fileName = new InvoiceGenerator(
//								 goldBillingTable,
//								 silverBillingTable,
//								 diamondBillingTable, 
//								 pfForm, cusBean).createPdf(includePrice.getValue(), VIN_NUMBER, isEstimateBill, invoiceDate);
//					} catch (Exception e) {
//						Notification.show("Error:" + e.getMessage());
//						e.printStackTrace();
//					}
//					
//					File pdfFile = new File(fileName);
//
//	                Embedded pdf = new Embedded("", new FileResource(pdfFile));
//	                pdf.setMimeType("application/pdf");
//	                pdf.setType(Embedded.TYPE_BROWSER);
//	                pdf.setWidth("100%");
//	                pdf.setHeight("90%");
//	                
//					 Window w = new Window("InvoiceBill");
//					 w.setModal(true);
//					 w.center();
//					 w.setCloseShortcut(KeyCode.ESCAPE, null);
//					 w.setResizable(false);
//					 w.setWidth("90%");
//					 w.setHeight("90%");
//					 w.setClosable(true);
//					 w.addStyleName("no-vertical-drag-hints");
//					 w.addStyleName("no-horizontal-drag-hints");
//					 w.setContent(pdf);
//		             UI.getCurrent().addWindow(w);
//		             w.focus();
//			    }
//	        });
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
     }
        Button newBillBtn = new Button(buttonType);
        newBillBtn.setSizeUndefined();
        newBillBtn.addStyleName("icon-newbill");
        newBillBtn.addStyleName("sidebar");

        newBillBtn.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				container.addCustomItem();
				//table.setHeight(String.valueOf((table.getHeight() + 10)) + "px");
				table.setImmediate(true);
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
	
	private VerticalLayout getBillingLayout2(Table table, CustomItemContainerInterface container, ItemContainerType itemContainerType) {
		 ThemeResource resource = new ThemeResource("img/addButtonSmall.jpg");
	        Image addNewItemimage = new Image("", resource);
	        addNewItemimage.setHeight("30px");
	        addNewItemimage.setWidth("30px");
	        String description = "";
	        Label itemType = new Label();
	        switch(itemContainerType.ordinal()){
	        	case 0: description = "Add Gold Item";
	        			itemType = new Label("Gold");
	        		break;
	        	case 1: description = "Add Silver Item";
	        	itemType = new Label("Silver");
	        		break;
	        	case 2: description = "Add Diamond Item";
	        		itemType = new Label("Diamond");
	        }
	        addNewItemimage.setDescription(description);
	        addNewItemimage.addClickListener(new MouseEvents.ClickListener()
	        {
				private static final long serialVersionUID = 1L;

				@Override
	            public void click(com.vaadin.event.MouseEvents.ClickEvent event)
	            {
					container.addCustomItem();
					//table.setHeight(String.valueOf((table.getHeight() + 10)) + "px");
					table.setImmediate(true);
	                addCustomTableDataContainerPriceItemCountValueChange(table, container);
	                goldTableWeightContainerValueChange();
	                silverTableWeightContainerValueChange();
	                diamondTableWeightContainerValueChange();
	            }
	        });
	        
	        
		VerticalLayout itemHolderLayout = new VerticalLayout();
		//itemHolderLayout.setSizeFull();
		HorizontalLayout itemTypeToAddLayout = new HorizontalLayout();
		itemTypeToAddLayout.addComponent(addNewItemimage);
		itemTypeToAddLayout.addComponent(itemType);
		itemTypeToAddLayout.setSpacing(false);
		itemTypeToAddLayout.setComponentAlignment(addNewItemimage, Alignment.MIDDLE_LEFT);
		itemTypeToAddLayout.setComponentAlignment(itemType, Alignment.MIDDLE_LEFT);
		itemHolderLayout.addComponent(itemTypeToAddLayout);
	    itemHolderLayout.addComponent(table);
		itemHolderLayout.setSpacing(true);
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
	        diamondItemTable.addStyleName("diamond-table-header");
	        diamondItemTable.addStyleName("diamond-table-footer");
	        diamondItemTable.addStyleName("diamond-table-footer-container");
	        //diamondItemContainer.addCustomItem();
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
	        diamondItemTable.setPageLength(3);
	        diamondItemTable.setColumnWidth(DiamondItemContainer.ITEM_NAME, 180);
	        diamondItemTable.setColumnWidth(DiamondItemContainer.QUANTITY, 60);
	        diamondItemTable.setColumnWidth(DiamondItemContainer.GOLD_WEIGHT, 90);
	        diamondItemTable.setColumnWidth(DiamondItemContainer.DIAMOND_WEIGHT, 125);
	        String totalFormattedDiamondPrice = String.format("%.3f",((DiamondItemContainer)diamondItemTable.getContainerDataSource()).getTotal()); 
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
						String totalFormattedDiamondPrice = String.format("%.3f",((DiamondItemContainer)diamondItemTable.getContainerDataSource()).getTotal());
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
	        silverItemTable.setPageLength(4);
	        silverItemTable.setColumnWidth(SilverItemContainer.DELETE, 70);
	        silverItemTable.setColumnWidth(SilverItemContainer.ITEM_NAME, 180);
	        silverItemTable.setColumnWidth(SilverItemContainer.QUANTITY, 60);
	        silverItemTable.setColumnWidth(SilverItemContainer.WEIGHT, 70);
	        silverItemTable.setColumnWidth(SilverItemContainer.PIECE_PAIR, 100);
	        silverItemTable.setColumnWidth(SilverItemContainer.SILVER_RATE, 125);
	        silverItemTable.setColumnWidth(SilverItemContainer.MAKING_CHARGE, 95);
	        silverItemTable.setColumnWidth(SilverItemContainer.MAKING_CHARGE_TYPE, 105);
	        String totalFormattedSilverPrice = String.format("%.3f",((SilverItemContainer)silverItemTable.getContainerDataSource()).getTotal()); 
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
	        //goldItemContainer.addCustomItem();
	        goldItemTable.setContainerDataSource(goldItemContainer);
	        goldItemTable.addStyleName("gold-table-header");
	        goldItemTable.addStyleName("gold-table-footer");
	        goldItemTable.addStyleName("gold-table-footer-container");
	        //goldItemTable.addStyleName("billing-table-row");
	        goldItemTable.setVisibleColumns(new Object[] {
	        		GoldItemContainer.DELETE,
	        		GoldItemContainer.HALL_MARK_TYPE,
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
	        goldItemTable.setPageLength(4);
	        goldItemTable.setColumnWidth(GoldItemContainer.DELETE, 70);
	        goldItemTable.setColumnWidth(GoldItemContainer.ITEM_NAME, 180);
	        goldItemTable.setColumnWidth(GoldItemContainer.HALL_MARK_TYPE, 80);
	        goldItemTable.setColumnWidth(GoldItemContainer.QUANTITY, 60);
	        goldItemTable.setColumnWidth(GoldItemContainer.WEIGHT, 60);
	        goldItemTable.setColumnWidth(GoldItemContainer.MAKING_CHARGE, 95);
	        goldItemTable.setColumnWidth(GoldItemContainer.GOLD_RATE, 110);
	        goldItemTable.setColumnWidth(GoldItemContainer.MAKING_CHARGE_TYPE, 100);
	        goldItemTable.setColumnWidth(GoldItemContainer.PIECE_PAIR, 100);
	        goldItemTable.setColumnWidth(GoldItemContainer.PRICE, 110);
	        String totalFormattedGoldPrice = String.format("%.3f",((GoldItemContainer)goldItemTable.getContainerDataSource()).getTotal()); 
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
					pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondPrice()));
				}
			});
		    Image deleteImage = (Image)((goldItemContainer.getItem(obj).getItemProperty(DiamondItemContainer.DELETE).getValue()));
		    deleteImage.addClickListener(new MouseEvents.ClickListener()
	           {
	               @Override
	               public void click(com.vaadin.event.MouseEvents.ClickEvent event)
	               {
	            	   String totalFormattedGoldWeight = String.format("%.3f",(goldItemContainer).getTotalWeight()); 
	            	   goldBillingTable.setColumnFooter(GoldItemContainer.WEIGHT, totalFormattedGoldWeight);
					   pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondPrice()));
	               }
	           });
		}
	}
	
	private void silverTableWeightContainerValueChange()
	{
		Collection<?> itemIdsList = silverBillingTable.getItemIds();
		//silverBillingTable.setColumnFooter(SilverItemContainer.WEIGHT, ("Items=" + silverBillingTable.g));
		for (Object obj : itemIdsList){
			TextField itemTxtField = (TextField)((silverItemContainer.getItem(obj).getItemProperty(SilverItemContainer.WEIGHT).getValue()));
		    itemTxtField.setImmediate(true);
		    itemTxtField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalFormattedWeight = String.format("%.3f",(silverItemContainer).getTotalWeight()); 
					silverBillingTable.setColumnFooter(SilverItemContainer.WEIGHT, totalFormattedWeight);
					pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondPrice()));
					
				}
			});
		    Image deleteImage = (Image)((silverItemContainer.getItem(obj).getItemProperty(DiamondItemContainer.DELETE).getValue()));
		    deleteImage.addClickListener(new MouseEvents.ClickListener()
	           {
	               @Override
	               public void click(com.vaadin.event.MouseEvents.ClickEvent event)
	               {
	            	   String totalFormattedSilverWeight = String.format("%.3f",(silverItemContainer).getTotalWeight()); 
	            	   silverBillingTable.setColumnFooter(SilverItemContainer.WEIGHT, totalFormattedSilverWeight);
					   pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondPrice()));
	               }
	           });
		}
	}
	
	private void diamondTableWeightContainerValueChange()
	{
		Collection<?> itemIdsList = diamondBillingTable.getItemIds();
		//diamondBillingTable.setColumnFooter(DiamondItemContainer.GOLD_WEIGHT, ("Items=" + diamondBillingTable.size()));
		for (Object obj : itemIdsList){
			TextField itemTxtField = (TextField)((diamondItemContainer.getItem(obj).getItemProperty(DiamondItemContainer.GOLD_WEIGHT).getValue()));
		    itemTxtField.setImmediate(true);
		    itemTxtField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalFormattedWeight = String.format("%.3f",(diamondItemContainer).getTotalGoldWeight()); 
					diamondBillingTable.setColumnFooter(DiamondItemContainer.GOLD_WEIGHT, totalFormattedWeight);
					pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondPrice()));
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
					pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondPrice()));
				}
			});
		    Image deleteImage = (Image)((diamondItemContainer.getItem(obj).getItemProperty(DiamondItemContainer.DELETE).getValue()));
		    deleteImage.addClickListener(new MouseEvents.ClickListener()
	           {
	               @Override
	               public void click(com.vaadin.event.MouseEvents.ClickEvent event)
	               {
	            	   String totalFormattedGoldWeight = String.format("%.3f",(diamondItemContainer).getTotalGoldWeight()); 
					   diamondBillingTable.setColumnFooter(DiamondItemContainer.GOLD_WEIGHT, totalFormattedGoldWeight);
					   String totalFormattedDiamondWeight = String.format("%.2f",(diamondItemContainer).getTotalDiamondWeight()); 
					   diamondBillingTable.setColumnFooter(DiamondItemContainer.DIAMOND_WEIGHT, totalFormattedDiamondWeight);
					   pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondPrice()));
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
					String totalFormattedPrice = String.format("%.3f",((CustomItemContainerInterface)table.getContainerDataSource()).getTotal()); 
					table.setColumnFooter(GoldItemContainer.PRICE, "Sum=" + totalFormattedPrice);
					table.setColumnFooter(GoldItemContainer.DELETE, ("Items=" + table.size()));
					pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondPrice()));
				}
			});
		    Image deleteImage = (Image)(((IndexedContainer)container).getItem(obj).getItemProperty(GoldItemContainer.DELETE).getValue());
		    deleteImage.addClickListener(new MouseEvents.ClickListener()
	           {
	               @Override
	               public void click(com.vaadin.event.MouseEvents.ClickEvent event)
	               {
	            	   String totalFormattedPrice = String.format("%.3f",((CustomItemContainerInterface)table.getContainerDataSource()).getTotal()); 
	            	   table.setColumnFooter(GoldItemContainer.PRICE, "Sum=" + totalFormattedPrice);
	            	   table.setColumnFooter(GoldItemContainer.DELETE, ("Items=" + table.size()));
	            	   pfForm.totalItemPrice.setValue(String.format("%.3f", getTotalGoldSilverDiamondPrice()));
	               }
	           });
		}
	}
	
	double getTotalGoldSilverDiamondPrice()
	{
		double totalGoldPrice = ((CustomItemContainerInterface)goldItemContainer).getTotal();
		double totalSilverPrice = ((CustomItemContainerInterface)silverItemContainer).getTotal();
		double totalDiamondPrice = ((CustomItemContainerInterface)diamondItemContainer).getTotal();
		return totalGoldPrice + totalSilverPrice + totalDiamondPrice;
	}
}
