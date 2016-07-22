package com.abhishek.fmanage.retail.data.container;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.abhishek.fmanage.mortgage.data.bean.MortgageItem;
import com.abhishek.fmanage.retail.dto.CustomerDTO;
import com.abhishek.fmanage.retail.dto.TransactionSearchResultDto;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;

public class RetailTransactionViewContainer extends IndexedContainer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    //public static final String MORTGAGE_TRANSACTION = "MORTGAGE_TRANSACTION";
    public static final String DATE_COL_NAME = "Date";
    public static final String TRANSID_COL_NAME = "Transaction No.";
    public static final String CUSTOMER_NAME_COL_NAME = "Name";
    public static final String CUSTOMER_PHONE_COL_NAME = "Phone No.";
    public static final String CUSTOMER_ADDRESS_COL_NAME = "Address";
    public static final String GOLDITEMS_COL_NAME = "Gold Items";
    public static final String SILVERITEMS_COL_NAME = "Silver Items";
    public static final String DIAMONDITEMS_COL_NAME = "Diamond Items";
    public static final String GENERALITEMS_COL_NAME = "General Items";
    public static final String SALE_AMOUNT_COL_NAME = "Sale Amount";
    public static final String BILL_TYPE_COL_NAME = "Bill Type";
    public static final String BILL_STATUS_COL_NAME = "Status";
    public static final String EMAIL_ID_COL_NAME = "Email Id";
    public static final String TRANSACTION_DETAIL_BTN_COL_NAME = "Detail Button";

    /**
     * {@link Constructor} for Transaction view container
     */
    public RetailTransactionViewContainer()
    {
      //  addContainerProperty(MORTGAGE_TRANSACTION, MortgageTransaction.class, new MortgageTransaction());
        addContainerProperty(DATE_COL_NAME, Date.class, new Date());
        addContainerProperty(TRANSID_COL_NAME, Long.class, 0L);
        addContainerProperty(BILL_TYPE_COL_NAME, String.class, "");
        addContainerProperty(BILL_STATUS_COL_NAME, String.class, "");
        addContainerProperty(CUSTOMER_NAME_COL_NAME, String.class, "");
        addContainerProperty(CUSTOMER_PHONE_COL_NAME, String.class, "");
        addContainerProperty(EMAIL_ID_COL_NAME, String.class, "");
        addContainerProperty(CUSTOMER_ADDRESS_COL_NAME, String.class, "");
        addContainerProperty(GOLDITEMS_COL_NAME, String.class, "");
        addContainerProperty(SILVERITEMS_COL_NAME, String.class, "");
        addContainerProperty(DIAMONDITEMS_COL_NAME, String.class, "");
        addContainerProperty(GENERALITEMS_COL_NAME, String.class, "");
        addContainerProperty(SALE_AMOUNT_COL_NAME, Double.class, 0.000d);
    }
    
    @SuppressWarnings("unchecked")
	public void addTransactionSearch(List<TransactionSearchResultDto> searchResultList){
    	for(TransactionSearchResultDto dto : searchResultList){
    		 Object mContainerRowId = addItem();
    	        Item item = getItem(mContainerRowId);
    	        if (item != null)
    	        {
    	            item.getItemProperty(DATE_COL_NAME).setValue(dto.getTransDate());
    	            item.getItemProperty(TRANSID_COL_NAME).setValue(dto.getTransId());
    	            item.getItemProperty(BILL_TYPE_COL_NAME).setValue(dto.getBillType().equals("E") ? "Estimate" : "Invoice");
    	            item.getItemProperty(BILL_STATUS_COL_NAME).setValue(dto.getTransactionStatus().equals("A") ? "Active" : "Inactive");
    	            item.getItemProperty(CUSTOMER_NAME_COL_NAME).setValue(StringUtils.isEmpty(dto.getCustomerName()) ? "" : dto.getCustomerName());
    	            item.getItemProperty(CUSTOMER_PHONE_COL_NAME).setValue(StringUtils.isEmpty(dto.getContactNumber()) ? "" : dto.getContactNumber());
    	            item.getItemProperty(EMAIL_ID_COL_NAME).setValue(StringUtils.isEmpty(dto.getEmailId()) ? "" : dto.getEmailId());
    	            item.getItemProperty(CUSTOMER_ADDRESS_COL_NAME).setValue(StringUtils.isEmpty(dto.getCustomerAddress()) ? "" : dto.getCustomerAddress());
    	            item.getItemProperty(GOLDITEMS_COL_NAME).setValue(StringUtils.isEmpty(dto.getGoldItems()) ? "" : dto.getGoldItems());
    	            item.getItemProperty(SILVERITEMS_COL_NAME).setValue(StringUtils.isEmpty(dto.getSilverItems()) ? "" : dto.getSilverItems());
    	            item.getItemProperty(DIAMONDITEMS_COL_NAME).setValue(StringUtils.isEmpty(dto.getDiamondItems()) ? "" : dto.getDiamondItems());
    	            item.getItemProperty(GENERALITEMS_COL_NAME).setValue(StringUtils.isEmpty(dto.getGeneralItems()) ? "" : dto.getGeneralItems());
    	            item.getItemProperty(SALE_AMOUNT_COL_NAME).setValue(dto.getTotalItemsPrice());
    	        }
    	}
    }
    
    public List<TransactionSearchResultDto> getTransactionFilteredSearchResultDtoList(){
    	List<TransactionSearchResultDto> searchList = new ArrayList<TransactionSearchResultDto>();
    	this.getItemIds().stream().forEach(
        		item -> {
        			TransactionSearchResultDto dto = new TransactionSearchResultDto();
        			dto.setTransDate((Date)this.getItem(item).getItemProperty(RetailTransactionViewContainer.DATE_COL_NAME).getValue());
        			dto.setTransId((long) this.getItem(item).getItemProperty(RetailTransactionViewContainer.TRANSID_COL_NAME).getValue());
        			dto.setBillType(String.valueOf(this.getItem(item).getItemProperty(RetailTransactionViewContainer.BILL_TYPE_COL_NAME).getValue()));
        			dto.setTransactionStatus(String.valueOf(this.getItem(item).getItemProperty(RetailTransactionViewContainer.BILL_STATUS_COL_NAME).getValue()));
        			dto.setCustomerName(String.valueOf(this.getItem(item).getItemProperty(RetailTransactionViewContainer.CUSTOMER_NAME_COL_NAME).getValue()));
        			dto.setContactNumber(String.valueOf(this.getItem(item).getItemProperty(RetailTransactionViewContainer.CUSTOMER_PHONE_COL_NAME).getValue()));
        			dto.setEmailId(String.valueOf(this.getItem(item).getItemProperty(RetailTransactionViewContainer.EMAIL_ID_COL_NAME).getValue()));
        			dto.setCustomerAddress(String.valueOf(this.getItem(item).getItemProperty(RetailTransactionViewContainer.CUSTOMER_ADDRESS_COL_NAME).getValue()));
        			dto.setGoldItems(String.valueOf(this.getItem(item).getItemProperty(RetailTransactionViewContainer.GOLDITEMS_COL_NAME).getValue()));
        			dto.setSilverItems(String.valueOf(this.getItem(item).getItemProperty(RetailTransactionViewContainer.SILVERITEMS_COL_NAME).getValue()));
        			dto.setDiamondItems(String.valueOf(this.getItem(item).getItemProperty(RetailTransactionViewContainer.DIAMONDITEMS_COL_NAME).getValue()));
        			dto.setGeneralItems(String.valueOf(this.getItem(item).getItemProperty(RetailTransactionViewContainer.GENERALITEMS_COL_NAME).getValue()));
        			dto.setTotalItemsPrice(Double.valueOf(String.valueOf(this.getItem(item).getItemProperty(RetailTransactionViewContainer.SALE_AMOUNT_COL_NAME).getValue())));
        			searchList.add(dto);
        		});
    	return searchList;
    }

}
