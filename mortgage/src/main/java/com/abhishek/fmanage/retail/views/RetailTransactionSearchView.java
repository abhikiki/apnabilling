/**
 * 
 */
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
import org.apache.commons.lang3.math.NumberUtils;

import com.abhishek.fmanage.retail.data.container.RetailTransactionViewContainer;
import com.abhishek.fmanage.retail.dto.RetailTaxInvoiceDTO;
import com.abhishek.fmanage.retail.dto.ShopDTO;
import com.abhishek.fmanage.retail.dto.TransactionDTO;
import com.abhishek.fmanage.retail.dto.TransactionSearchCriteriaDto;
import com.abhishek.fmanage.retail.dto.TransactionSearchResultDto;
import com.abhishek.fmanage.retail.restclient.service.RestRetailTransactionService;
import com.abhishek.fmanage.retail.window.BillWindow;
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

/**
 * View class for transactions.
 * 
 * @author Abhishek
 * 
 */
public class RetailTransactionSearchView extends VerticalLayout implements View
{

    public static final String INDIAN_DATE_FORMAT = "dd/MM/yyyy";

    public static final String SELECT_DATE = "Select Date";

    public static final String INVOICE_BILL = "Invoice Bill";
	public static final String ESTIMATE_BILL = "Estimate Bill";
	
	public static final String ACTIVE_BILL_STATUS = "Active";
	public static final String INACTIVE_BILL_STATUS = "Inactive";
	
	public static final String ALL = "All";
	
    /**
     * Serial Id
     */
    private static final long serialVersionUID = 1L;

    /** Name of the transaction css style */
    private static final String TRANSACTION_STYLE_NAME = "transactions";

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

    private OptionGroup billType;
    private OptionGroup billStatus;
    
    /** Text field filter */
    private final TextField filter = new TextField();

    private final TextField transIdTextField = new TextField();
    
    private final TextField invoiceIdTextField = new TextField();
    
    /** Data container for holding transactions */
    RetailTransactionViewContainer retailViewContainer = new RetailTransactionViewContainer();

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
                transIdTextField.setValue("");
                invoiceIdTextField.setValue("");
                startPopUpDate.setValue(getStartDate(new Date(), 2));
                endPopUpDate.setValue(new Date());
                billType.setValue(ALL);
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
        toolbar.addComponent( billType = getBillTypeOptionGroup());
        toolbar.addComponent( billStatus = getBillStatusOptionGroup());
       
       
        toolbar.setExpandRatio(startPopUpDate, 1);
        toolbar.setExpandRatio(endPopUpDate, 1);
        toolbar.setExpandRatio(billType, 1);
        toolbar.setExpandRatio(billStatus, 1);
     
        toolbar.setComponentAlignment(startPopUpDate, Alignment.MIDDLE_LEFT);
        toolbar.setComponentAlignment(endPopUpDate, Alignment.MIDDLE_LEFT);
        toolbar.setComponentAlignment(billType, Alignment.MIDDLE_LEFT);
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
                retailViewContainer.removeAllContainerFilters();
            	retailViewContainer.removeAllItems();
            	ShopDTO shopDto = (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);
            	billType.getValue();
            	billStatus.getValue();
            	TransactionSearchCriteriaDto criteriaDto = new TransactionSearchCriteriaDto();
            	criteriaDto.setShopId(shopDto.getShopId());
            	criteriaDto.setStartDate(startPopUpDate.getValue());
            	criteriaDto.setEndDate(endPopUpDate.getValue());
            	criteriaDto.setBillType(convertBillType(billType.getValue().toString()));
            	criteriaDto.setBillStatus(convertBillStatus(billStatus.getValue().toString()));
            	List<TransactionSearchResultDto> searchResultDtoList = new RestRetailTransactionService(shopDto).findBills(criteriaDto);
                retailViewContainer.addTransactionSearch(searchResultDtoList);
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
        
        VerticalLayout toolBarWhole = new VerticalLayout();
        toolBarWhole.addComponent(toolbar);
        toolBarWhole.addComponent(getTransactionSearchByIdLayout());
        addComponent(toolBarWhole);
        addComponent(transactionTable);
        

//        Date endDate = new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.MONTH, -2);
//        Date startDate = cal.getTime();
//        
//        addComponent(new GoldItemSummaryChart().getChart(new RestSummaryService().getRetailSummary(startDate, endDate).getGoldItemSummaryDtoList()));

        setExpandRatio(transactionTable, 1);

        transactionTable.addActionHandler(new Handler()
        {
            private static final long serialVersionUID = 1L;
            private Action transactionDetailAction = new Action("Transaction Details");

            @Override
            public void handleAction(Action action, Object sender, Object target)
            {
                if (action == transactionDetailAction)
                {
                    Item item = ((Table) sender).getItem(target);
                    long transId =  (long) item.getItemProperty(
                        RetailTransactionViewContainer.TRANSID_COL_NAME).getValue();
                    ShopDTO shopDto = (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);
                    BillWindow bw = new BillWindow(transId, new RestRetailTransactionService(shopDto).getBill(transId));
            		UI.getCurrent().addWindow(bw);
            		bw.focus();
                }
            }

            @Override
            public Action[] getActions(Object target, Object sender)
            {
                return new Action[] {transactionDetailAction};
            }
        });
        transactionTable.setImmediate(true);
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
               		if(retailViewContainer.size() == 0){
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
    					"DiamondItems", "GeneralItems", "Sale Amount" };
    				csvFilePrinter.printRecord(FILE_HEADER);
    				List<TransactionSearchResultDto> list = retailViewContainer.getTransactionFilteredSearchResultDtoList();
    				for(TransactionSearchResultDto dto : list){
    					List<String> record = new ArrayList<String>(); 
    					record.add(String.valueOf(dto.getTransDate()));
    					record.add(String.valueOf(dto.getTransId()));
    					record.add(dto.getBillType());
    					record.add(dto.getTransactionStatus());
    					record.add(dto.getCustomerName());
    					record.add(dto.getContactNumber());
    					record.add(dto.getEmailId());
    					record.add(dto.getCustomerAddress());
    					record.add(dto.getGoldItems());
    					record.add(dto.getSilverItems());
    					record.add(dto.getDiamondItems());
    					record.add(dto.getGeneralItems());
    					record.add(String.valueOf(dto.getTotalItemsPrice()));
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
    	 transIdTextField.setInputPrompt("Transaction No.");
    	 
    	 Button transSearchByIdButton = new Button("Transaction Number", new ClickListener()
         {
             private static final long serialVersionUID = 1L;

             @Override
             
             public void buttonClick(ClickEvent event)
             {
            	if(NumberUtils.isDigits(String.valueOf(transIdTextField.getValue()))){
            		long transId = NumberUtils.toLong(String.valueOf(transIdTextField.getValue()));
            		ShopDTO shopDto = (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);
            		TransactionDTO transDto = new RestRetailTransactionService(shopDto).getBill(transId);
            		if(transDto != null){
            			BillWindow bw = new BillWindow(transId, transDto);
                 		UI.getCurrent().addWindow(bw);
                 		bw.focus();
            		}else{
                		Notification.show("Transaction Not Found");
            		}
            	}else{
            		Notification.show("Transaction Number is not valid");
            	}
            	
             }
         });
    	 
    	 invoiceIdTextField.setInputPrompt("Invoice No.");
    	 Button invoiceSearchByIdButton = new Button("Invoice Number", new ClickListener()
         {
             private static final long serialVersionUID = 1L;

             @Override
             
             public void buttonClick(ClickEvent event)
             {
            	if(NumberUtils.isDigits(String.valueOf(invoiceIdTextField.getValue()))){
            		long invoiceId = NumberUtils.toLong(String.valueOf(invoiceIdTextField.getValue()));
            		ShopDTO shopDto = (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);
            		RetailTaxInvoiceDTO retailTaxInvoiceDto = new RestRetailTransactionService(shopDto).getBillByInvoiceId(invoiceId);
            		if(retailTaxInvoiceDto == null){
            			Notification.show("Invoice Not Found");
            		}else{
            			long transId = retailTaxInvoiceDto.getTransId();
                		TransactionDTO transDto = new RestRetailTransactionService(shopDto).getBill(transId);
                		BillWindow bw = new BillWindow(transId, transDto);
                 		UI.getCurrent().addWindow(bw);
                 		bw.focus();
            		}
            	}else{
            		Notification.show("Invoice Number is not valid");
            	}
            	
             }
         });

    	 transSearchByIdButton.addStyleName("default");
    	 invoiceSearchByIdButton.addStyleName("default");
    	 Button exportBtn = getExportButton();
    	 transSearchByIdButton.addStyleName("icon-search-1");
    	 invoiceSearchByIdButton.addStyleName("icon-search-1");
    	 transIdSearchTooolbar.addComponent(transIdTextField);
    	 transIdSearchTooolbar.addComponent(transSearchByIdButton);
    	 transIdSearchTooolbar.addComponent(exportBtn);
    	 transIdSearchTooolbar.addComponent(invoiceIdTextField);
    	 transIdSearchTooolbar.addComponent(invoiceSearchByIdButton);
    	 transIdSearchTooolbar.setComponentAlignment(transIdTextField, Alignment.MIDDLE_LEFT);
    	 transIdSearchTooolbar.setComponentAlignment(transSearchByIdButton, Alignment.MIDDLE_LEFT);
    	 transIdSearchTooolbar.setComponentAlignment(exportBtn, Alignment.MIDDLE_LEFT);
    	 transIdSearchTooolbar.setComponentAlignment(invoiceIdTextField, Alignment.MIDDLE_LEFT);
    	 transIdSearchTooolbar.setComponentAlignment(invoiceSearchByIdButton, Alignment.MIDDLE_LEFT);
    	 transIdSearchTooolbar.setExpandRatio(transIdTextField, 1);
    	 transIdSearchTooolbar.setExpandRatio(transSearchByIdButton, 1);
    	 transIdSearchTooolbar.setExpandRatio(exportBtn, 1);
    	 transIdSearchTooolbar.setExpandRatio(invoiceIdTextField, 1);
    	 transIdSearchTooolbar.setExpandRatio(invoiceSearchByIdButton, 1);
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
    	transactionTable.setColumnFooter(RetailTransactionViewContainer.DATE_COL_NAME, ("Total Items=" + retailViewContainer.size()));
    	if(retailViewContainer.size() > 0){
    		transactionTable.setColumnFooter(RetailTransactionViewContainer.CUSTOMER_ADDRESS_COL_NAME, ("Total Sale=" +
        		String.format("%.3f", retailViewContainer.getItemIds().stream().map(
                		item -> Double.valueOf(retailViewContainer.getItem(item).getItemProperty(
    	        				RetailTransactionViewContainer.SALE_AMOUNT_COL_NAME).getValue().toString())).reduce((price1, price2) -> price1 + price2).get())));
    	}else{
    		transactionTable.setColumnFooter(RetailTransactionViewContainer.CUSTOMER_ADDRESS_COL_NAME, ("Total Sale=0.000"));
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
                retailViewContainer.removeAllContainerFilters();
            }
        });
		
	}
    
    private void resetDataContainerFilters(String text) {
		retailViewContainer.removeAllContainerFilters();
		retailViewContainer.addContainerFilter(getTextFilter(text));
		
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
                    		RetailTransactionViewContainer.CUSTOMER_NAME_COL_NAME, item, text)
                         || filterByProperty(RetailTransactionViewContainer.CUSTOMER_ADDRESS_COL_NAME, item, text)
                         || filterByProperty(RetailTransactionViewContainer.CUSTOMER_PHONE_COL_NAME, item, text)
                         || filterByProperty(RetailTransactionViewContainer.EMAIL_ID_COL_NAME, item, text);
            }

            @Override
            public boolean appliesToProperty(final Object propertyId)
            {
                return (propertyId.equals(RetailTransactionViewContainer.CUSTOMER_NAME_COL_NAME)
                    || propertyId.equals(RetailTransactionViewContainer.CUSTOMER_PHONE_COL_NAME)
                    || propertyId.equals(RetailTransactionViewContainer.EMAIL_ID_COL_NAME)
                    || propertyId.equals(RetailTransactionViewContainer.CUSTOMER_ADDRESS_COL_NAME))
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
	private OptionGroup getBillTypeOptionGroup(){
    	billType = new OptionGroup("Bill Type");
    	billType.addItems(ESTIMATE_BILL, INVOICE_BILL, ALL);
		billType.setValue(ALL);
		return billType;
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
        transactionTable.setContainerDataSource(retailViewContainer);
        sortTable();

        transactionTable.setFooterVisible(true);
        transactionTable.setMultiSelect(false);
        transactionTable.setPageLength(10);
        setTableColumnFooters();
        return transactionTable;
    }

	private void initializeData() {
		retailViewContainer.removeAllContainerFilters();
		retailViewContainer.removeAllItems();
        ShopDTO shopDto = (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -2);
        Date startDate = cal.getTime();
        retailViewContainer = getTransactionSearchViewContainer(shopDto.getShopId(), startDate, new Date(), ALL, ACTIVE_BILL_STATUS);
	}

    private RetailTransactionViewContainer getTransactionSearchViewContainer(int shopId, Date startDate, Date endDate, String billType, String billStatus)
    {
    	TransactionSearchCriteriaDto dto = new TransactionSearchCriteriaDto();
    	dto.setShopId(shopId);
    	dto.setStartDate(startDate);
    	dto.setEndDate(endDate);
    	dto.setBillType(convertBillType(billType));
    	dto.setBillStatus(convertBillStatus(billStatus));
    	ShopDTO shopDto = (ShopDTO) getUI().getSession().getAttribute(ShopDTO.class);
    	List<TransactionSearchResultDto> searchResultDtoList = new RestRetailTransactionService(shopDto).findBills(dto);
        retailViewContainer.addTransactionSearch(searchResultDtoList);
        return retailViewContainer;
    }

    private void sortTable()
    {
        transactionTable.sort(
            new Object[] { RetailTransactionViewContainer.DATE_COL_NAME },
            new boolean[] { false });
    }
    
    String convertBillType(String billType){
    	String convertedBill ;
    	switch(billType){
    		case ESTIMATE_BILL : convertedBill = "E";
    				break;
    		case INVOICE_BILL : convertedBill = "I";
    				break;
    		default: convertedBill = "All";
    	}
    	return convertedBill;
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
