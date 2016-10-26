package com.abhishek.fmanage.mortgage.data.container;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.abhishek.fmanage.mortgage.dto.MortgageTransactionSearchResultDTO;
import com.abhishek.fmanage.retail.data.container.RetailTransactionViewContainer;
import com.abhishek.fmanage.retail.dto.TransactionSearchResultDto;
import com.abhishek.fmanage.retail.views.MortgageView;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

public class MortgageTransactionViewContainer extends IndexedContainer{

	private static final long serialVersionUID = 1L;
	
	public static final String ITEM_ID_HIDDEN_COL_NAME = "Item Id";
    public static final String TRANSID_COL_NAME = "Transaction No.";
	public static final String START_DATE_COL_NAME = "Start Date";
    public static final String END_DATE_COL_NAME = "End Date";
    public static final String MORTGAGE_AMOUNT_COL_NAME = "Mortgage Amount";
    public static final String DAYS_DIFF_COL_NAME = "Days Difference";
    public static final String MONTHS_DIFF_COL_NAME = "Months Difference";
    public static final String INTEREST_COL_NAME = "Interest(%)";
    public static final String AMOUNT_TO_PAY_COL_NAME = "Amount to pay";
    public static final String BILL_STATUS_COL_NAME = "Status";
    public static final String KEEPER_COL_NAME = "Keeper";
    public static final String CUSTOMER_NAME_COL_NAME = "Name";
    public static final String CUSTOMER_PHONE_COL_NAME = "Phone No.";
    public static final String EMAIL_ID_COL_NAME = "Email Id";
    public static final String CUSTOMER_ADDRESS_COL_NAME = "Address";
    public static final String GOLDITEMS_COL_NAME = "Gold Items";
    public static final String SILVERITEMS_COL_NAME = "Silver Items";
    public static final String DIAMONDITEMS_COL_NAME = "Diamond Items";
    
    public static final String TRANSACTION_DETAIL_BTN_COL_NAME = "Detail Button";

    /**
     * {@link Constructor} for Transaction view container
     */
    public MortgageTransactionViewContainer(){
    	addContainerProperty(ITEM_ID_HIDDEN_COL_NAME, Integer.class, 0);
    	addContainerProperty(TRANSID_COL_NAME, Long.class, 0L);
    	addContainerProperty(BILL_STATUS_COL_NAME, String.class, "");
        addContainerProperty(START_DATE_COL_NAME, Date.class, new Date());
        addContainerProperty(END_DATE_COL_NAME, Date.class, new Date());
        addContainerProperty(MORTGAGE_AMOUNT_COL_NAME, Double.class, 0.000d);
        addContainerProperty(DAYS_DIFF_COL_NAME, Integer.class, 0);
        addContainerProperty(MONTHS_DIFF_COL_NAME, Double.class, 0.000d);
        addContainerProperty(INTEREST_COL_NAME, Double.class, 0.000d);
        addContainerProperty(AMOUNT_TO_PAY_COL_NAME, Double.class, 0.000d);
        addContainerProperty(KEEPER_COL_NAME, String.class, "");
        addContainerProperty(CUSTOMER_NAME_COL_NAME, String.class, "");
        addContainerProperty(CUSTOMER_PHONE_COL_NAME, String.class, "");
        addContainerProperty(EMAIL_ID_COL_NAME, String.class, "");
        addContainerProperty(CUSTOMER_ADDRESS_COL_NAME, String.class, "");
        addContainerProperty(GOLDITEMS_COL_NAME, String.class, "");
        addContainerProperty(SILVERITEMS_COL_NAME, String.class, "");
        addContainerProperty(DIAMONDITEMS_COL_NAME, String.class, "");
    }
    
    @SuppressWarnings("unchecked")
  	public void addTransactionSearch(List<MortgageTransactionSearchResultDTO> searchResultList){
      	for(MortgageTransactionSearchResultDTO dto : searchResultList){
      		 Object mContainerRowId = addItem();
      	        Item item = getItem(mContainerRowId);
      	        if (item != null)
      	        {
      	        	item.getItemProperty(ITEM_ID_HIDDEN_COL_NAME).setValue(mContainerRowId);
      	        	item.getItemProperty(TRANSID_COL_NAME).setValue(dto.getTransId());
      	        	item.getItemProperty(BILL_STATUS_COL_NAME).setValue(dto.getTransactionStatus().equals("A") ? "Active" : "Inactive");
      	        	item.getItemProperty(START_DATE_COL_NAME).setValue(dto.getTransactionStartDate());
      	            item.getItemProperty(END_DATE_COL_NAME).setValue(dto.getEndDate());
      	            item.getItemProperty(MORTGAGE_AMOUNT_COL_NAME).setValue(dto.getAmount());
      	            item.getItemProperty(DAYS_DIFF_COL_NAME).setValue(dto.getDaysDiff());
      	            item.getItemProperty(MONTHS_DIFF_COL_NAME).setValue(dto.getMonthsDiff());
      	            item.getItemProperty(INTEREST_COL_NAME).setValue(dto.getInterestRate());
      	            item.getItemProperty(MORTGAGE_AMOUNT_COL_NAME).setValue(dto.getAmount());
      	            item.getItemProperty(AMOUNT_TO_PAY_COL_NAME).setValue(dto.getAmountToPay());
      	            item.getItemProperty(KEEPER_COL_NAME).setValue(dto.getKeeperName());
      	            item.getItemProperty(CUSTOMER_NAME_COL_NAME).setValue(StringUtils.isEmpty(dto.getCustomerName()) ? "" : dto.getCustomerName());
    	            item.getItemProperty(CUSTOMER_PHONE_COL_NAME).setValue(StringUtils.isEmpty(dto.getContactNumber()) ? "" : dto.getContactNumber());
    	            item.getItemProperty(EMAIL_ID_COL_NAME).setValue(StringUtils.isEmpty(dto.getEmailId()) ? "" : dto.getEmailId());
    	            item.getItemProperty(CUSTOMER_ADDRESS_COL_NAME).setValue(StringUtils.isEmpty(dto.getCustomerAddress()) ? "" : dto.getCustomerAddress());
    	            item.getItemProperty(GOLDITEMS_COL_NAME).setValue(StringUtils.isEmpty(dto.getGoldItems()) ? "" : dto.getGoldItems());
      	            item.getItemProperty(SILVERITEMS_COL_NAME).setValue(StringUtils.isEmpty(dto.getSilverItems()) ? "" : dto.getSilverItems());
      	            item.getItemProperty(DIAMONDITEMS_COL_NAME).setValue(StringUtils.isEmpty(dto.getDiamondItems()) ? "" : dto.getDiamondItems());
      	        }
      		}
      }
    
    public List<MortgageTransactionSearchResultDTO> getTransactionFilteredSearchResultDtoList(){
    	List<MortgageTransactionSearchResultDTO> searchList = new ArrayList<MortgageTransactionSearchResultDTO>();
    	this.getItemIds().stream().forEach(
        		item -> {
        			MortgageTransactionSearchResultDTO dto = new MortgageTransactionSearchResultDTO();
        			dto.setTransId((long) this.getItem(item).getItemProperty(MortgageTransactionViewContainer.TRANSID_COL_NAME).getValue());
        			dto.setTransactionStatus(String.valueOf(this.getItem(item).getItemProperty(MortgageTransactionViewContainer.BILL_STATUS_COL_NAME).getValue()));
        			dto.setTransactionStartDate((Date)this.getItem(item).getItemProperty(MortgageTransactionViewContainer.START_DATE_COL_NAME).getValue());
        			dto.setEndDate((Date)this.getItem(item).getItemProperty(MortgageTransactionViewContainer.END_DATE_COL_NAME).getValue());
        			dto.setAmount(Double.valueOf(String.valueOf(this.getItem(item).getItemProperty(MortgageTransactionViewContainer.MORTGAGE_AMOUNT_COL_NAME).getValue())));
        			dto.setDaysDiff(Integer.valueOf(String.valueOf(this.getItem(item).getItemProperty(MortgageTransactionViewContainer.DAYS_DIFF_COL_NAME).getValue())));
        			dto.setAmountToPay(Double.valueOf(String.valueOf(this.getItem(item).getItemProperty(MortgageTransactionViewContainer.AMOUNT_TO_PAY_COL_NAME).getValue())));
        			dto.setKeeperName(String.valueOf(this.getItem(item).getItemProperty(MortgageTransactionViewContainer.KEEPER_COL_NAME).getValue()));
        			dto.setCustomerName(String.valueOf(this.getItem(item).getItemProperty(MortgageTransactionViewContainer.CUSTOMER_NAME_COL_NAME).getValue()));
        			dto.setContactNumber(String.valueOf(this.getItem(item).getItemProperty(MortgageTransactionViewContainer.CUSTOMER_PHONE_COL_NAME).getValue()));
        			dto.setEmailId(String.valueOf(this.getItem(item).getItemProperty(MortgageTransactionViewContainer.EMAIL_ID_COL_NAME).getValue()));
        			dto.setCustomerAddress(String.valueOf(this.getItem(item).getItemProperty(MortgageTransactionViewContainer.CUSTOMER_ADDRESS_COL_NAME).getValue()));
        			dto.setGoldItems(String.valueOf(this.getItem(item).getItemProperty(MortgageTransactionViewContainer.GOLDITEMS_COL_NAME).getValue()));
        			dto.setSilverItems(String.valueOf(this.getItem(item).getItemProperty(MortgageTransactionViewContainer.SILVERITEMS_COL_NAME).getValue()));
        			dto.setDiamondItems(String.valueOf(this.getItem(item).getItemProperty(MortgageTransactionViewContainer.DIAMONDITEMS_COL_NAME).getValue()));
        			searchList.add(dto);
        		});
    	return searchList;
    }
}
