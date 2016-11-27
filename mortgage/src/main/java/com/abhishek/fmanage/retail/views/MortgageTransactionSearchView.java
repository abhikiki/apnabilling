package com.abhishek.fmanage.retail.views;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;

import com.abhishek.fmanage.mortgage.data.container.MortgageTransactionViewContainer;
import com.abhishek.fmanage.mortgage.dto.MortgageTransactionSearchResultDTO;
import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.dto.TransactionSearchCriteriaDto;
import com.abhishek.fmanage.retail.restclient.service.RestMortgageTransactionService;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class MortgageTransactionSearchView extends VerticalLayout implements View{


    public static final String INDIAN_DATE_FORMAT = "dd/MM/yyyy";

    public static final String SELECT_DATE = "Select Date";

	public static final String ACTIVE_BILL_STATUS = "Active";
	public static final String INACTIVE_BILL_STATUS = "Inactive";
	
	public static final String ALL = "All";
	
    /**
     * Serial Id
     */
    private static final long serialVersionUID = 1L;

    /** Name of the transaction css style */
    private static final String TRANSACTION_STYLE_NAME = "transactions";

    /** Name of the toolbar css style */
    private static final String TOOLBAR_STYLE_NAME = "sidebar"; //"toolbar";

    /** pop up start date field prompt text when no date is selected*/
    public static final String POPUP_START_DATE_PROMPT_TEXT = "Start Date";

    /** pop up end date field prompt text when no date is selected*/
    public static final String POPUP_END_DATE_PROMPT_TEXT = "End Date";

    /** Transaction table */
    private Table transactionTable;

    /** Pop up start date field */
    private final PopupDateField startPopUpDate = new PopupDateField();

    /** Pop up end date field */
    private final PopupDateField endPopUpDate = new PopupDateField();

    private OptionGroup billStatus;
    
    /** Text field filter */
    private final TextField filter = new TextField();

    
    /** Data container for holding transactions */
    MortgageTransactionViewContainer mortgageViewContainer = new MortgageTransactionViewContainer();

    @Override
    public void enter(final ViewChangeEvent event)
    {
        setSizeFull();
        addStyleName(TRANSACTION_STYLE_NAME);

        
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidth("100%");
        toolbar.setSpacing(true);
        toolbar.setMargin(true);
        toolbar.addStyleName("mytoolbar");

        Label title = new Label("Transactions");
        title.addStyleName("h1");
        title.setSizeUndefined();
        toolbar.addComponent(title);
        toolbar.setComponentAlignment(title, Alignment.MIDDLE_LEFT);

        startPopUpDate.setCaption("Start Date");
        startPopUpDate.setImmediate(true);
        startPopUpDate.setInvalidAllowed(false);
        startPopUpDate.setLocale(new Locale("en", "IN"));
        startPopUpDate.setDateFormat(INDIAN_DATE_FORMAT);
        startPopUpDate.setTextFieldEnabled(false);
        startPopUpDate.setInputPrompt(SELECT_DATE);
        startPopUpDate.setValue(getStartDate(new Date(), 2));

        endPopUpDate.setCaption("End Date");
        endPopUpDate.setImmediate(true);
        endPopUpDate.setInvalidAllowed(false);
        endPopUpDate.setLocale(new Locale("en", "IN"));
        endPopUpDate.setDateFormat(INDIAN_DATE_FORMAT);
        endPopUpDate.setTextFieldEnabled(false);
        endPopUpDate.setInputPrompt(SELECT_DATE);
        endPopUpDate.setValue(new Date());

        transactionTable = getTransactionTable();
        setContainerFilters();

        Button resetFilterButton = new Button("Reset", new ClickListener()
        {
            private static final long serialVersionUID = 1L;

            @Override
            
            public void buttonClick(ClickEvent event)
            {
                filter.setValue("");
                startPopUpDate.setValue(getStartDate(new Date(), 2));
                endPopUpDate.setValue(new Date());
                billStatus.setValue(ACTIVE_BILL_STATUS);
                initializeData();
                setTableColumnFooters();
            }

			
        });
        resetFilterButton.setWidth("80%");
        resetFilterButton.addStyleName("default");
        resetFilterButton.setIcon(FontAwesome.REFRESH);
       
        toolbar.addComponent(startPopUpDate);
        toolbar.addComponent(endPopUpDate);
        toolbar.addComponent( billStatus = getBillStatusOptionGroup());
       
       
        toolbar.setExpandRatio(startPopUpDate, 1);
        toolbar.setExpandRatio(endPopUpDate, 1);
        toolbar.setExpandRatio(billStatus, 1);
     
        toolbar.setComponentAlignment(startPopUpDate, Alignment.MIDDLE_LEFT);
        toolbar.setComponentAlignment(endPopUpDate, Alignment.MIDDLE_LEFT);
        toolbar.setComponentAlignment(billStatus, Alignment.MIDDLE_LEFT);

        final Button searchTransactionBtn = new Button("Search");
        searchTransactionBtn.addStyleName("icon-search-1");
        searchTransactionBtn.addStyleName("default");
        searchTransactionBtn.addClickListener(new ClickListener()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event)
            {
            	filter.setValue(StringUtils.EMPTY);
            	mortgageViewContainer.removeAllContainerFilters();
            	mortgageViewContainer.removeAllItems();
            	ShopDTO shopDto = (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);
            	TransactionSearchCriteriaDto criteriaDto = new TransactionSearchCriteriaDto();
            	criteriaDto.setShopId(shopDto.getShopId());
            	criteriaDto.setStartDate(startPopUpDate.getValue());
            	criteriaDto.setEndDate(endPopUpDate.getValue());
            	criteriaDto.setBillStatus(convertBillStatus(billStatus.getValue().toString()));
            	List<MortgageTransactionSearchResultDTO> searchResultDtoList = new RestMortgageTransactionService(shopDto).findBills(criteriaDto);
            	mortgageViewContainer.addTransactionSearch(searchResultDtoList);
                setContainerFilters();
                transactionTable.setPageLength(searchResultDtoList.size());
                setTableColumnFooters();
            }
        });
        searchTransactionBtn.setEnabled(true);
        searchTransactionBtn.addStyleName("small");
        toolbar.addComponent(searchTransactionBtn);
        toolbar.setExpandRatio(searchTransactionBtn, 1);
        toolbar.setComponentAlignment(searchTransactionBtn, Alignment.MIDDLE_LEFT);
        toolbar.addComponent(filter);
        toolbar.setExpandRatio(filter, 1);
        toolbar.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
        toolbar.addComponent(resetFilterButton);
        toolbar.setExpandRatio(resetFilterButton, 1);
        toolbar.setComponentAlignment(resetFilterButton, Alignment.MIDDLE_RIGHT);
        Button exportBtn = getExportButton();
        toolbar.addComponent(exportBtn);
        toolbar.setExpandRatio(exportBtn, 1);
        toolbar.setComponentAlignment(exportBtn, Alignment.MIDDLE_RIGHT);
        
       // VerticalLayout toolBarWhole = new VerticalLayout();
       // toolBarWhole.addComponent(toolbar);
       // toolBarWhole.addComponent(getTransactionSearchByIdLayout());
        addComponent(toolbar);
        addComponent(transactionTable);
        
        setExpandRatio(transactionTable, 1);

        transactionActionHandler();
        transactionTable.setImmediate(true);
    }

	private void transactionActionHandler() {
		transactionTable.addActionHandler(new Handler()
        {
            private static final long serialVersionUID = 1L;
            private Action deleteTransactionAction = new Action("Delete Transaction");

            @Override
            public void handleAction(Action action, Object sender, Object target)
            {
                if (action == deleteTransactionAction)
                {
                	
                    Item item = ((Table) sender).getItem(target);
                    long transId =  (long) item.getItemProperty(MortgageTransactionViewContainer.TRANSID_COL_NAME).getValue();
                    
                    getDeleteTransactionWindow(transId, item);
                }
            }

            @Override
            public Action[] getActions(Object target, Object sender)
            {
                return new Action[] {deleteTransactionAction};
            }
        });
		transactionTable.addActionHandler(new Handler()
        {
            private static final long serialVersionUID = 1L;
            private Action inactivateTransactionAction = new Action("Inactivate Transaction");
            private Action activateTransactionAction = new Action("Activate Transaction");

            @Override
            public void handleAction(Action action, Object sender, Object target)
            {
            	if (action == activateTransactionAction)
                {
            		Item item = ((Table) sender).getItem(target);
                    long transId =  (long) item.getItemProperty(MortgageTransactionViewContainer.TRANSID_COL_NAME).getValue();
                    ShopDTO shopDto = (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);
                    new RestMortgageTransactionService(shopDto).updateBillStatus(transId, "A");
                    Item i = transactionTable.getContainerDataSource().getItem(item.getItemProperty(MortgageTransactionViewContainer.BILL_STATUS_COL_NAME).getValue());
                    item.getItemProperty(MortgageTransactionViewContainer.BILL_STATUS_COL_NAME).setValue("Active");
                    if(!billStatus.getValue().equals("All")){
                    	transactionTable.getContainerDataSource().removeItem((Integer) item.getItemProperty(MortgageTransactionViewContainer.ITEM_ID_HIDDEN_COL_NAME).getValue());
                    }
                    
                    Notification.show("Transaction[" + transId + "] activated");
                }
            }

            @Override
            public Action[] getActions(Object target, Object sender)
            {
            	return new Action[] {activateTransactionAction};
            }
        });
		transactionTable.addActionHandler(new Handler()
        {
            private static final long serialVersionUID = 1L;
            private Action inactivateTransactionAction = new Action("Inactivate Transaction");
            
            @Override
            public void handleAction(Action action, Object sender, Object target)
            {
                if (action == inactivateTransactionAction)
                {
                	Item item = ((Table) sender).getItem(target);
                    long transId =  (long) item.getItemProperty(MortgageTransactionViewContainer.TRANSID_COL_NAME).getValue();
                    ShopDTO shopDto = (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);
                    new RestMortgageTransactionService(shopDto).updateBillStatus(transId, "I");
                    item.getItemProperty(MortgageTransactionViewContainer.BILL_STATUS_COL_NAME).setValue("InActive");
                    if(!billStatus.getValue().equals("All")){
                    	transactionTable.getContainerDataSource().removeItem((Integer) item.getItemProperty(MortgageTransactionViewContainer.ITEM_ID_HIDDEN_COL_NAME).getValue());
                    }
                    Notification.show("Transaction[" + transId + "] inactivated");
                }
            }

            @Override
            public Action[] getActions(Object target, Object sender)
            {
            	return new Action[] {inactivateTransactionAction};
            }
        });
	}

    protected void getDeleteTransactionWindow(long transId, Item item) {
		Window // Create a new sub-window
		mywindow = new Window();
		mywindow.setModal(true);

		// Set window size.
		mywindow.setHeight("200px");
		mywindow.setWidth("400px");
		mywindow.center();
		mywindow.setClosable(false);
		mywindow.setResizable(false);
		
		// Set window position.
		mywindow.setPositionX(200);
		mywindow.setPositionY(50);
		VerticalLayout winVL = new VerticalLayout();
		winVL.setSizeFull();
		
		// Trivial logic for closing the sub-window
        Button cancelBtn = new Button("No");
        cancelBtn.addClickListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
                mywindow.close(); // Close the sub-window
            }
        });
        
        Button yesBtn = new Button("Yes");
        yesBtn.addClickListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
            	ShopDTO shopDto = (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);
            	new RestMortgageTransactionService(shopDto).deleteBill(transId);
                transactionTable.getContainerDataSource().removeItem((Integer) item.getItemProperty(MortgageTransactionViewContainer.ITEM_ID_HIDDEN_COL_NAME).getValue());
                mywindow.close(); // Close the sub-window
                Notification.show("Transaction No: " + transId + " deleted successfully");
            }
        });
        winVL.addComponent(new Label("<h2>Delete Transaction</h2>", ContentMode.HTML));
        winVL.addComponent(new Label(" "));
		winVL.addComponent(new Label("Are you sure you want to delete transaction No: " + transId));
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.addComponent(yesBtn);
		hl.addComponent(cancelBtn);
		winVL.addComponent(hl);
		winVL.setSpacing(true);
		mywindow.setContent(winVL);
		UI.getCurrent().addWindow(mywindow);
	}

	private Button getExportButton(){
    	final Button downloadButton = new Button("CSV", FontAwesome.DOWNLOAD);
    	downloadButton.addStyleName("default");
    	//downloadButton.setDisableOnClick(true);
    	downloadButton.addClickListener(new ClickListener()
           {
               private static final long serialVersionUID = 1L;

               @Override
               public void buttonClick(ClickEvent event)
               {
               		if(mortgageViewContainer.size() == 0){
               			Notification.show("No Data to download");
               		}
               }
           });
    	   
    	@SuppressWarnings("serial")
    	StreamSource source = new StreamSource()
    	{

    		@Override
    		public java.io.InputStream getStream()
    		{
    			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
    				CSVPrinter csvFilePrinter = null;
    				CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
    				BufferedWriter bufferedWriter = null;

    			try
    			{
    				bufferedWriter = new BufferedWriter(new OutputStreamWriter(arrayOutputStream));
    				csvFilePrinter = new CSVPrinter(bufferedWriter, csvFileFormat);
    				Object[] FILE_HEADER = { "TransDate","TransId", 
    					"BillType", "TransactionStatus", "CustomerName", "ContactNumber", "EmailId", "CustomerAddress", "GoldItems", "SilverItems",
    					"DiamondItems", "Sale Amount" };
    				csvFilePrinter.printRecord(FILE_HEADER);
    				List<MortgageTransactionSearchResultDTO> list = mortgageViewContainer.getTransactionFilteredSearchResultDtoList();
    				for(MortgageTransactionSearchResultDTO dto : list){
    					List<String> record = new ArrayList<String>(); 
    					//record.add(String.valueOf(dto.getTransDate()));
    					record.add(String.valueOf(dto.getTransId()));
    					//record.add(dto.getBillType());
    					record.add(dto.getTransactionStatus());
    					record.add(dto.getCustomerName());
    					record.add(dto.getContactNumber());
    					record.add(dto.getEmailId());
    					record.add(dto.getCustomerAddress());
    					record.add(dto.getGoldItems());
    					record.add(dto.getSilverItems());
    					record.add(dto.getDiamondItems());
    					//record.add(dto.getGeneralItems());
    					//record.add(String.valueOf(dto.getTotalItemsPrice()));
    					csvFilePrinter.printRecord(record);
    				}
    			}
    			catch (Throwable e)
    			{
    				//logger.error(e, e);
    				Notification.show(e.getMessage());
    			}
    			finally
    			{
    				try {
						bufferedWriter.flush();
						bufferedWriter.close();
	    				csvFilePrinter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				
    				//window.close();
    			}
    			return new ByteArrayInputStream(arrayOutputStream.toByteArray());
    			
    		}
    	};

    	StreamResource resource = new StreamResource(source, "BillingData" + ".csv");
    	resource.setMIMEType("text/csv");

    	FileDownloader fileDownloader = new FileDownloader(resource);
    	fileDownloader.setOverrideContentType(false);
    	
    	fileDownloader.extend(downloadButton);
    	return downloadButton;
    }
    
    
    private HorizontalLayout getTransactionSearchByIdLayout(){
    	 HorizontalLayout transIdSearchTooolbar = new HorizontalLayout();
    	 transIdSearchTooolbar.setSpacing(true);
    	 transIdSearchTooolbar.setMargin(true);
    	 transIdSearchTooolbar.addStyleName("mytoolbar");
    	 
    	

    	 Button exportBtn = getExportButton();
    	 transIdSearchTooolbar.addComponent(exportBtn);
    	 transIdSearchTooolbar.setComponentAlignment(exportBtn, Alignment.MIDDLE_LEFT);
    	 transIdSearchTooolbar.setExpandRatio(exportBtn, 1);
    	 transIdSearchTooolbar.setWidth("100%");
    	 
    	 HorizontalLayout hLDummy = new HorizontalLayout();
    	 hLDummy.setWidth("100%");
    	 hLDummy.addStyleName("mytoolbar");
    	 
    	 HorizontalLayout wholeHL = new HorizontalLayout();
    	 wholeHL.setWidth("100%");
    	 wholeHL.setSpacing(true);
    	 wholeHL.setMargin(false);
    	 wholeHL.addStyleName("mytoolbar");
    	 wholeHL.addComponent(transIdSearchTooolbar);
    	 wholeHL.addComponent(hLDummy);
    	 wholeHL.setExpandRatio(transIdSearchTooolbar, 4);
    	 wholeHL.setExpandRatio(hLDummy, 1);

    	 return wholeHL;
    }
    
    private void setTableColumnFooters() {
    	transactionTable.setColumnFooter(MortgageTransactionViewContainer.TRANSID_COL_NAME, ("Total Items=" + mortgageViewContainer.size()));
    	if(mortgageViewContainer.size() > 0){
    		transactionTable.setColumnFooter(MortgageTransactionViewContainer.AMOUNT_TO_PAY_COL_NAME, ("Total Amount to pay=" +
        		String.format("%.3f", mortgageViewContainer.getItemIds().stream().map(
                		item -> Double.valueOf(mortgageViewContainer.getItem(item).getItemProperty(
                				MortgageTransactionViewContainer.AMOUNT_TO_PAY_COL_NAME).getValue().toString())).reduce((price1, price2) -> price1 + price2).get())));
    		
    		transactionTable.setColumnFooter(MortgageTransactionViewContainer.MORTGAGE_AMOUNT_COL_NAME, ("Total Mortgage=" +
            		String.format("%.3f", mortgageViewContainer.getItemIds().stream().map(
                    		item -> Double.valueOf(mortgageViewContainer.getItem(item).getItemProperty(
                    				MortgageTransactionViewContainer.MORTGAGE_AMOUNT_COL_NAME).getValue().toString())).reduce((price1, price2) -> price1 + price2).get())));
    	}

	}
    
    private void setContainerFilters() {
    	filter.addTextChangeListener(new TextChangeListener()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void textChange(final TextChangeEvent event)
            {
                resetDataContainerFilters(event.getText());
                setTableColumnFooters();
            }

			
        });
        filter.setInputPrompt("Filter");
        filter.addShortcutListener(new ShortcutListener("Clear", KeyCode.ESCAPE, null)
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void handleAction(Object sender, Object target)
            {
                filter.setValue(StringUtils.EMPTY);
                mortgageViewContainer.removeAllContainerFilters();
            }
        });
		
	}
    
    private void resetDataContainerFilters(String text) {
    	mortgageViewContainer.removeAllContainerFilters();
    	mortgageViewContainer.addContainerFilter(getTextFilter(text));
		
	}

    /**
     * Get the text filter used for filtering name, address, items and amount lended
     * 
     * @param text text used for filtering name, address, items and amount lended
     * 
     * @return text used for filtering name, address, items and amount lended
     */
    private Filter getTextFilter(final String text)
    {
        return new Filter()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean passesFilter(final Object itemId, final Item item)
                throws UnsupportedOperationException
            {
                return (text == null || text.equals(StringUtils.EMPTY)) ? true :  
                    filterByProperty(
                    		MortgageTransactionViewContainer.CUSTOMER_NAME_COL_NAME, item, text)
                         || filterByProperty(MortgageTransactionViewContainer.CUSTOMER_ADDRESS_COL_NAME, item, text)
                         || filterByProperty(MortgageTransactionViewContainer.CUSTOMER_PHONE_COL_NAME, item, text)
                         || filterByProperty(MortgageTransactionViewContainer.EMAIL_ID_COL_NAME, item, text)
                         || filterByProperty(MortgageTransactionViewContainer.KEEPER_COL_NAME, item, text)
                         || filterByProperty(MortgageTransactionViewContainer.GOLDITEMS_COL_NAME, item, text)
                         || filterByProperty(MortgageTransactionViewContainer.SILVERITEMS_COL_NAME, item, text)
                         || filterByProperty(MortgageTransactionViewContainer.DIAMONDITEMS_COL_NAME, item, text)
                         || filterByProperty(MortgageTransactionViewContainer.MORTGAGE_AMOUNT_COL_NAME, item, text);
            }

            @Override
            public boolean appliesToProperty(final Object propertyId)
            {
                return (propertyId.equals(MortgageTransactionViewContainer.CUSTOMER_NAME_COL_NAME)
                    || propertyId.equals(MortgageTransactionViewContainer.CUSTOMER_PHONE_COL_NAME)
                    || propertyId.equals(MortgageTransactionViewContainer.EMAIL_ID_COL_NAME)
                    || propertyId.equals(MortgageTransactionViewContainer.CUSTOMER_ADDRESS_COL_NAME)
                    || propertyId.equals(MortgageTransactionViewContainer.KEEPER_COL_NAME)
                    || propertyId.equals(MortgageTransactionViewContainer.GOLDITEMS_COL_NAME)
                    || propertyId.equals(MortgageTransactionViewContainer.SILVERITEMS_COL_NAME)
                    || propertyId.equals(MortgageTransactionViewContainer.DIAMONDITEMS_COL_NAME)
                    || propertyId.equals(MortgageTransactionViewContainer.MORTGAGE_AMOUNT_COL_NAME))
                    ? true : false;
            }
        };
    }

    /**
     * Filter the items based on filter text
     * 
     * @param prop name of the property to filter
     * @param item item whose property needs to be checked
     * @param text text entered by user to filter
     * 
     * @return true if item filter condition matches false otherwise 
     */
    private boolean filterByProperty(final String prop, final Item item, final String text)
    {
        if (item != null
            && item.getItemProperty(prop) != null
            && item.getItemProperty(prop).getValue() != null)
        {
            String val = item.getItemProperty(prop).getValue().toString().trim().toLowerCase();
            ;
            //if (val.startsWith(text.toLowerCase().trim()))
            if (val.contains(text.toLowerCase().trim()))
            {
                return true;
            }
        }
        return false;
    }
	    
    private OptionGroup getBillStatusOptionGroup(){
    	billStatus = new OptionGroup("Bill Status");
    	billStatus.addItems(ACTIVE_BILL_STATUS, INACTIVE_BILL_STATUS, ALL);
    	billStatus.setValue(ACTIVE_BILL_STATUS);
   		return billStatus;
    }
    
    private Date getStartDate(Date endDate, int monthsBack){
    	LocalDateTime ldt = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
    	ldt = ldt.minusMonths(monthsBack);
    	return  Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    private Table getTransactionTable()
    {
    	transactionTable = new Table(){
            @Override
            protected String formatPropertyValue(Object rowId, Object colId, Property property) {
                                Object value = property.getValue();
                                if (value instanceof Double) {
                                    return String.format("%.3f", value);
                                }
                                return super.formatPropertyValue(rowId, colId, property);
                            }
            };
        transactionTable.setSizeFull();
        transactionTable.addStyleName("borderless");
        transactionTable.setSelectable(true);
        transactionTable.setColumnCollapsingAllowed(true);
        transactionTable.setColumnReorderingAllowed(true);
        initializeData();
        transactionTable.setContainerDataSource(mortgageViewContainer);
        transactionTable.setVisibleColumns(new Object[]{
        	MortgageTransactionViewContainer.TRANSID_COL_NAME,
        	MortgageTransactionViewContainer.BILL_STATUS_COL_NAME,
        	MortgageTransactionViewContainer.START_DATE_COL_NAME,
        	MortgageTransactionViewContainer.END_DATE_COL_NAME,
        	MortgageTransactionViewContainer.MORTGAGE_AMOUNT_COL_NAME,
        	MortgageTransactionViewContainer.DAYS_DIFF_COL_NAME,
        	MortgageTransactionViewContainer.MONTHS_DIFF_COL_NAME,
        	MortgageTransactionViewContainer.INTEREST_COL_NAME,
        	MortgageTransactionViewContainer.AMOUNT_TO_PAY_COL_NAME,
        	MortgageTransactionViewContainer.KEEPER_COL_NAME,
        	MortgageTransactionViewContainer.CUSTOMER_NAME_COL_NAME,
        	MortgageTransactionViewContainer.CUSTOMER_PHONE_COL_NAME,
        	MortgageTransactionViewContainer.EMAIL_ID_COL_NAME,
        	MortgageTransactionViewContainer.CUSTOMER_ADDRESS_COL_NAME,
        	MortgageTransactionViewContainer.GOLDITEMS_COL_NAME,
        	MortgageTransactionViewContainer.SILVERITEMS_COL_NAME,
        	MortgageTransactionViewContainer.DIAMONDITEMS_COL_NAME,});
        sortTable();

        transactionTable.setFooterVisible(true);
        transactionTable.setMultiSelect(false);
        transactionTable.setPageLength(10);
        setTableColumnFooters();
        return transactionTable;
    }

	private void initializeData() {
		mortgageViewContainer.removeAllContainerFilters();
		mortgageViewContainer.removeAllItems();
        ShopDTO shopDto = (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -2);
        Date startDate = cal.getTime();
        mortgageViewContainer = getTransactionSearchViewContainer(shopDto.getShopId(), startDate, new Date(), ALL, ACTIVE_BILL_STATUS);
	}

    private MortgageTransactionViewContainer getTransactionSearchViewContainer(int shopId, Date startDate, Date endDate, String billType, String billStatus)
    {
    	TransactionSearchCriteriaDto dto = new TransactionSearchCriteriaDto();
    	dto.setShopId(shopId);
    	dto.setStartDate(startDate);
    	dto.setEndDate(endDate);
    	dto.setBillStatus(convertBillStatus(billStatus));
    	ShopDTO shopDto = (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);
    	List<MortgageTransactionSearchResultDTO> searchResultDtoList = new RestMortgageTransactionService(shopDto).findBills(dto);
    	mortgageViewContainer.addTransactionSearch(searchResultDtoList);
        return mortgageViewContainer;
    }

    private void sortTable()
    {
        transactionTable.sort(
            new Object[] { MortgageTransactionViewContainer.START_DATE_COL_NAME},
            new boolean[] { false });
    }
    
 
    String convertBillStatus(String billStatus){
    	String convertedBillStatus ;
    	switch(billStatus){
    		case ACTIVE_BILL_STATUS : convertedBillStatus = "A";
    				break;
    		case INACTIVE_BILL_STATUS : convertedBillStatus = "I";
    				break;
    		default: convertedBillStatus = "All";
    	}
    	return convertedBillStatus;
    }

}
