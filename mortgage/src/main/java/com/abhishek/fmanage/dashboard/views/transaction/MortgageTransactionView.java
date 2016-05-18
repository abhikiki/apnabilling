/**
 * 
 */
package com.abhishek.fmanage.dashboard.views.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.abhishek.fmanage.mortgage.data.bean.MortgageCustomerPayment;
import com.abhishek.fmanage.mortgage.data.bean.MortgageItem;
import com.abhishek.fmanage.mortgage.data.bean.MortgageTransaction;
import com.abhishek.fmanage.mortgage.data.container.MortgageTransactionViewContainer;
import com.abhishek.fmanage.retail.dto.CustomerDTO;
import com.vaadin.data.Item;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * View class for transactions.
 * 
 * @author Abhishek
 * 
 */
public class MortgageTransactionView extends VerticalLayout implements View
{

    public static final String INDIAN_DATE_FORMAT = "dd/MM/yyyy";

    public static final String SELECT_DATE = "Select Date";

    /**
     * Serial Id
     */
    private static final long serialVersionUID = 1L;

    /** Name of the transaction css style */
    private static final String TRANSACTION_STYLE_NAME = "transactions";

    /** Name of the toolbar css style */
    private static final String TOOLBAR_STYLE_NAME = "toolbar";

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

    /** Text field filter */
    private final TextField filter = new TextField();

    /** Data container for holding transactions */
    MortgageTransactionViewContainer dataContainer = getMortgageTransactionViewContainer();

    /** Class responsible for filtering transactions based of user selected input*/
    MortgageTransactionFilter mTransFilter;

    @Override
    public void enter(final ViewChangeEvent event)
    {
        setSizeFull();
        addStyleName(TRANSACTION_STYLE_NAME);

        transactionTable = getMortgageTransactionTable();

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidth("100%");
        toolbar.setSpacing(true);
        toolbar.setMargin(true);
        toolbar.addStyleName(TOOLBAR_STYLE_NAME);
        addComponent(toolbar);

        Label title = new Label("All Transactions");
        title.addStyleName("h1");
        title.setSizeUndefined();
        toolbar.addComponent(title);
        toolbar.setComponentAlignment(title, Alignment.MIDDLE_LEFT);

        startPopUpDate.setCaption("Transaction Start Date");
        startPopUpDate.setImmediate(true);
        startPopUpDate.setInvalidAllowed(false);
        startPopUpDate.setLocale(new Locale("en", "IN"));
        startPopUpDate.setDateFormat(INDIAN_DATE_FORMAT);
        startPopUpDate.setTextFieldEnabled(false);
        startPopUpDate.setInputPrompt(SELECT_DATE);

        endPopUpDate.setCaption("Transaction End Date");
        endPopUpDate.setImmediate(true);
        endPopUpDate.setInvalidAllowed(false);
        endPopUpDate.setLocale(new Locale("en", "IN"));
        endPopUpDate.setDateFormat(INDIAN_DATE_FORMAT);
        endPopUpDate.setTextFieldEnabled(false);
        endPopUpDate.setInputPrompt(SELECT_DATE);

        mTransFilter = new MortgageTransactionFilter(
            dataContainer, filter, startPopUpDate, endPopUpDate);
        mTransFilter.setComponentFilters();
        Button resetFilterButton = new Button("Reset Filters", new ClickListener()
        {
            private static final long serialVersionUID = 1L;

            @Override
            
            public void buttonClick(ClickEvent event)
            {
                dataContainer.removeAllContainerFilters();
                filter.setValue("");
                startPopUpDate.setValue(null);
                endPopUpDate.setValue(null);
            }
        });
        toolbar.addComponent(startPopUpDate);
        toolbar.addComponent(endPopUpDate);
        toolbar.addComponent(filter);
        toolbar.addComponent(resetFilterButton);
        toolbar.setExpandRatio(startPopUpDate, 1);
        toolbar.setExpandRatio(endPopUpDate, 1);
        toolbar.setExpandRatio(filter, 1);
        toolbar.setExpandRatio(resetFilterButton, 1);
        toolbar.setComponentAlignment(startPopUpDate, Alignment.MIDDLE_LEFT);
        toolbar.setComponentAlignment(endPopUpDate, Alignment.MIDDLE_LEFT);
        toolbar.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
        toolbar.setComponentAlignment(resetFilterButton, Alignment.MIDDLE_LEFT);

        final Button newTransactionBtn = new Button("New Transaction");
        newTransactionBtn.addClickListener(new ClickListener()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event)
            {
                Window w = new ExistingTransactionWindow(new MortgageTransaction(), true);
                UI.getCurrent().addWindow(w);
                w.focus();
            }
        });
        newTransactionBtn.setEnabled(true);
        newTransactionBtn.addStyleName("small");
        toolbar.addComponent(newTransactionBtn);
        toolbar.setComponentAlignment(newTransactionBtn, Alignment.MIDDLE_LEFT);

        addComponent(transactionTable);
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
                    MortgageTransaction mTrans = (MortgageTransaction) item.getItemProperty(
                        MortgageTransactionViewContainer.MORTGAGE_TRANSACTION).getValue();
                    Window w = new ExistingTransactionWindow(mTrans, false);
                    UI.getCurrent().addWindow(w);
                    w.focus();
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

    private Table getMortgageTransactionTable()
    {
        transactionTable = new Table();
        transactionTable.setSizeFull();
        transactionTable.addStyleName("borderless");
        transactionTable.setSelectable(true);
        transactionTable.setColumnCollapsingAllowed(true);
        transactionTable.setColumnReorderingAllowed(true);
        dataContainer.removeAllContainerFilters();
        transactionTable.setContainerDataSource(dataContainer);
        sortTable();

        transactionTable.setVisibleColumns(new Object[] {
            MortgageTransactionViewContainer.DATE_COL_NAME,
            MortgageTransactionViewContainer.CUSTOMER_NAME_COL_NAME,
            MortgageTransactionViewContainer.AMOUNT_LENDED_COL_NAME,
            MortgageTransactionViewContainer.TOTAL_ITEM_WEIGHT_COL_NAME,
            MortgageTransactionViewContainer.CUSTOMER_ADDRESS_COL_NAME,
            MortgageTransactionViewContainer.ITEMS_COL_NAME
            });
        transactionTable.setFooterVisible(true);
        transactionTable.setMultiSelect(false);
        transactionTable.setPageLength(10);

        return transactionTable;
    }

    private MortgageTransactionViewContainer getMortgageTransactionViewContainer()
    {
    	//Currently this is generating test data. We will get this data via rest service
        MortgageTransactionViewContainer mViewContainer = new MortgageTransactionViewContainer();
        for (int i = 0; i < 300; i++)
        {
            MortgageTransaction mTrans = new MortgageTransaction();
            mTrans.setTransactionId(1);
            mTrans.setTransactionOpenedDate("20140114");
            mTrans.setAmountPaidToCustomer(500210.10);
            CustomerDTO cus = new CustomerDTO();
            cus.setZipcode("77054" + i);
            cus.setStreetAddress1(i + "Bakerganj");
            cus.setStreetAddress2(i + "Nageshwar colony");
            cus.setCity(i + "Patna");
            cus.setCountry(i + "India");
            cus.setEmailId(i + "abhikiki@gmail.com");
            cus.setFirstName(i + "Abhishek");
            cus.setLastName(i + "Gupta");
            cus.setContactNumber(i + "2146001469");
            cus.setId(i + 1);
            cus.setStateprovince(i + "Bihar");
            cus.setRating(i + 5);
            mTrans.setCustomer(cus);

            mTrans.setCustomerPaymentList(new ArrayList<MortgageCustomerPayment>());
            mTrans.setInterestRateAssignedToCustomer(4.5);

            List<MortgageItem> mItemList = new ArrayList<>();
            MortgageItem mItem1 = new MortgageItem();
            mItem1.setItemId(1);
            mItem1.setItemName("i + Earning/Gold");
            mItem1.setItemWeight(i + 24.60);
            mItem1.setItemDescription(i+ "Gold hai bhai");
            mItemList.add(mItem1);
            MortgageItem mItem2 = new MortgageItem();
            mItem2.setItemId(2);
            mItem2.setItemName("PROSEUREKA");
            mItem2.setItemWeight(i + 24.60);
            mItem2.setItemDescription(i+ "Gold hai bhai");
            mItemList.add(mItem2);
            mTrans.setItemList(mItemList);
            mTrans.setTotalItemWeight(i+ 141);
            mTrans.setTransactionNotes(i + "Customer if mochaat");
            mTrans.setTransactionStatus(true);

            mViewContainer.addMortgageTransaction(mTrans);
        }
        return mViewContainer;
    }

    private void sortTable()
    {
        transactionTable.sort(
            new Object[] { MortgageTransactionViewContainer.CUSTOMER_NAME_COL_NAME },
            new boolean[] { false });
    }
}
