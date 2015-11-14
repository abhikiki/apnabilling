/**
 * 
 */
package com.abhishek.fmanage.mortgage.data.container;

import java.lang.reflect.Constructor;
import java.util.List;

import com.abhishek.fmanage.mortgage.data.bean.Customer;
import com.abhishek.fmanage.mortgage.data.bean.MortgageItem;
import com.abhishek.fmanage.mortgage.data.bean.MortgageTransaction;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;

/**
 * @author Abhishek
 * 
 */
public class MortgageTransactionViewContainer extends IndexedContainer
{
    /**
     * Serial Id
     */
    private static final long serialVersionUID = 1L;
    public static final String MORTGAGE_TRANSACTION = "MORTGAGE_TRANSACTION";
    public static final String DATE_COL_NAME = "Date";
    public static final String CUSTOMER_NAME_COL_NAME = "Name";
    public static final String AMOUNT_LENDED_COL_NAME = "Amount Lended";
    public static final String TOTAL_ITEM_WEIGHT_COL_NAME = "Total Weight";
    public static final String INTEREST_RATE_COL_NAME = "Interest Rate(%)";
    public static final String ITEMS_COL_NAME = "Items";
    public static final String CONTACT_NUMBER_COL_NAME = "Contact Number";
    public static final String EMAIL_ID_COL_NAME = "Email Id";
    public static final String CUSTOMER_ADDRESS_COL_NAME = "Address";
    public static final String TRANSACTION_DETAIL_BTN_COL_NAME = "Transaction Detail Button";

    /**
     * {@link Constructor} for Transaction view container
     */
    public MortgageTransactionViewContainer()
    {
        addContainerProperty(MORTGAGE_TRANSACTION, MortgageTransaction.class, new MortgageTransaction());
        addContainerProperty(DATE_COL_NAME, String.class, "99999999");
        addContainerProperty(CUSTOMER_NAME_COL_NAME, String.class, "");
        addContainerProperty(AMOUNT_LENDED_COL_NAME, String.class, "");
        addContainerProperty(TOTAL_ITEM_WEIGHT_COL_NAME, String.class, "");
        addContainerProperty(ITEMS_COL_NAME, String.class, "");
        addContainerProperty(CUSTOMER_ADDRESS_COL_NAME, String.class, "");
        addContainerProperty(TRANSACTION_DETAIL_BTN_COL_NAME, Button.class, "Detail");
    }

    /**
     * Method to add a mortgage transaction in the container
     * 
     * @param mortgageTransaction mortgage transaction to add in the container
     */
    @SuppressWarnings("unchecked")
    public void addMortgageTransaction(final MortgageTransaction mortgageTransaction)
    {
        Object mContainerRowId = addItem();
        Item item = getItem(mContainerRowId);
        if (item != null)
        {
            item.getItemProperty(MORTGAGE_TRANSACTION).setValue(mortgageTransaction);
            item.getItemProperty(DATE_COL_NAME).setValue("14/01/2014");
            Customer customer = mortgageTransaction.getCustomer();
            item.getItemProperty(CUSTOMER_NAME_COL_NAME).setValue(customer.getFirstName() + " " + customer.getLastName());
            item.getItemProperty(AMOUNT_LENDED_COL_NAME).setValue(String.valueOf(mortgageTransaction.getAmountPaidToCustomer()));
            item.getItemProperty(TOTAL_ITEM_WEIGHT_COL_NAME).setValue(String.valueOf(mortgageTransaction.getTotalItemWeight()));

            List<MortgageItem> mItemList = mortgageTransaction.getItemList();
            StringBuilder mItemStringBuilder = new StringBuilder("");
            for(MortgageItem mItem : mItemList)
            {
                mItemStringBuilder.append(mItem.getItemName());
            }
            item.getItemProperty(ITEMS_COL_NAME).setValue(mItemStringBuilder.toString());
            item.getItemProperty(CUSTOMER_ADDRESS_COL_NAME).setValue(
                customer.getStreetAddress1() + " " + customer.getStreetAddress2());
        }
    }
}
