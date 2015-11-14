/**
 * 
 */
package com.abhishek.fmanage.dashboard.views.transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.abhishek.fmanage.mortgage.data.container.MortgageTransactionViewContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

/**
 * @author Abhishek Gupta
 * 
 */
public class MortgageTransactionFilter
{
    private static final String DATE_DELIMITER = "/";

    private static final String DATE_STORAGE_FORMAT = "yyyyMMdd";

    /** Container holding all transactions */
    private MortgageTransactionViewContainer transactionDataContainer;

    /** Text field for filter */
    private TextField filter;

    /** Date field for filtering transaction start date */
    private PopupDateField startPopUpDate;

    /** Date field for filtering transaction start date */
    private PopupDateField endPopUpDate;

    private Calendar cal = null;

    /**
     * Constructor
     * 
     * @param transactionDataContainer data container for transactions
     * @param filter text field for filtering based on name, address, amount lended and items
     * @param startPopUpDate Date field for filtering transaction start date
     * @param endPopUpDate Date field for filtering transaction end date
     */
    public MortgageTransactionFilter(
        final MortgageTransactionViewContainer transactionDataContainer,
        final TextField filter,
        final PopupDateField startPopUpDate,
        final PopupDateField endPopUpDate)
    {
        this.transactionDataContainer = transactionDataContainer;
        this.filter = filter;
        this.startPopUpDate = startPopUpDate;
        this.endPopUpDate = endPopUpDate;
    }

    public void setComponentFilters()
    {
        setTransactionTextFieldFilter();
        setStartEndDateFilter();
    }

    /**
     * Method to set transaction text filter
     */
    private void setTransactionTextFieldFilter()
    {
        filter.addTextChangeListener(new TextChangeListener()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void textChange(final TextChangeEvent event)
            {
                resetDataContainerFilters(
                    event.getText(), startPopUpDate.getValue(), endPopUpDate.getValue());
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
                transactionDataContainer.removeAllContainerFilters();
            }
        });
    }

    /**
     * Method to set transaction start and end date filters
     */
    private void setStartEndDateFilter()
    {
        addPopupDateValueChangeListener(startPopUpDate);
        addPopupDateValueChangeListener(endPopUpDate);
    }

    /**
     * Method to add date change value listener
     * 
     * @param popUpDate pop up date field
     */
    private void addPopupDateValueChangeListener(final PopupDateField popUpDate)
    {
        popUpDate.addValueChangeListener(new ValueChangeListener()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(ValueChangeEvent event)
            {
                resetDataContainerFilters(
                    filter.getValue(), startPopUpDate.getValue(), endPopUpDate.getValue());
            }
        });
    }

    /**
     * Method to reset all filters associated with the data container
     */
    void resetDataContainerFilters(
        final String filterValue,
        final Date startPopupDateValue,
        final Date endPopupDateValue)
    {
        transactionDataContainer.removeAllContainerFilters();
        transactionDataContainer.addContainerFilter(getTextFilter(filterValue));
        transactionDataContainer.addContainerFilter(getStartDateFilter(startPopupDateValue));
        transactionDataContainer.addContainerFilter(getEndDateFilter(endPopupDateValue));
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
                         || filterByProperty(MortgageTransactionViewContainer.ITEMS_COL_NAME,
                                item, text)
                         || filterByProperty(
                                MortgageTransactionViewContainer.CUSTOMER_ADDRESS_COL_NAME,
                                item,
                                text)
                         || filterByProperty(
                                MortgageTransactionViewContainer.AMOUNT_LENDED_COL_NAME, item, text);
            }

            @Override
            public boolean appliesToProperty(final Object propertyId)
            {
                return (propertyId.equals(MortgageTransactionViewContainer.CUSTOMER_NAME_COL_NAME)
                    || propertyId.equals(MortgageTransactionViewContainer.ITEMS_COL_NAME)
                    || propertyId.equals(MortgageTransactionViewContainer.CUSTOMER_ADDRESS_COL_NAME)
                    || propertyId.equals(MortgageTransactionViewContainer.AMOUNT_LENDED_COL_NAME))
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
            if (val.startsWith(text.toLowerCase().trim()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Filter the items based on user selected transaction start date
     * 
     * @param startDate pop up start date selected by user
     * 
     * @return start date filter
     */
    private Filter getStartDateFilter(final Date startDate)
    {
        return new Filter()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean passesFilter(final Object itemId, final Item item)
                throws UnsupportedOperationException
            {
                return (startDate == null) ? true : filterStartDateByProperty(
                    MortgageTransactionViewContainer.DATE_COL_NAME, item, startDate); 
            }

            @Override
            public boolean appliesToProperty(Object propertyId)
            {
                return (propertyId.equals(MortgageTransactionViewContainer.DATE_COL_NAME)) ?
                    true : false;
            }
        };
    }

    /**
     * Filter the items based on user selected transaction end date
     * 
     * @param endDate pop up end date selected by user
     * 
     * @return end date filter
     */
    private Filter getEndDateFilter(final Date endDate)
    {
        return new Filter()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean passesFilter(final Object itemId, final Item item)
            throws UnsupportedOperationException
            {
                return (endDate == null) ? true : filterEndDateByProperty(
                    MortgageTransactionViewContainer.DATE_COL_NAME, item, endDate); 
            }

            @Override
            public boolean appliesToProperty(final Object propertyId)
            {
                return (propertyId.equals(MortgageTransactionViewContainer.DATE_COL_NAME)) ?
                    true : false;
            }
        };
    }

    /**
     * Filter transaction item by user selected end date
     * 
     * @param dateColName transaction date column name in the data container
     * @param item        item of the container
     * @param endDate     transaction end date selected by the user
     * 
     * @return true if item date is less than or equal to user selected transaction end date
     */
    private boolean filterEndDateByProperty(
        final String dateColName, final Item item, final Date endDate)
    {
        if (item == null
            || item.getItemProperty(dateColName) == null
            || item.getItemProperty(dateColName).getValue() == null
            || endDate == null)
        {
            return false;
        }
        String val = item.getItemProperty(dateColName).getValue().toString().trim().toLowerCase();
        if (!val.equals(MortgageTransactionView.POPUP_END_DATE_PROMPT_TEXT))
        {
            String[] itemDateArr = val.split(DATE_DELIMITER);
            Calendar calInstance = getCalendarInstance();
            calInstance.set(
                Integer.parseInt(itemDateArr[2]),
                Integer.parseInt(itemDateArr[1]) - 1,
                Integer.parseInt(itemDateArr[0]));
            DateFormat df = new SimpleDateFormat(DATE_STORAGE_FORMAT);
            String endDateStr = df.format(endDate);
            String itemDateStr = df.format(calInstance.getTime());
            return (Integer.parseInt(itemDateStr) <= Integer.parseInt(endDateStr)) ? true : false; 
        }
        return true;
    }

    /**
     * Filter transaction item by user selected start date
     * 
     * @param dateColName transaction date column name in the data container
     * @param item        item of the container
     * @param endDate     transaction start date selected by the user
     * 
     * @return true if item date is greater than or equal to user selected transaction start date
     */
    private boolean filterStartDateByProperty(
        final String dateColName,
        final Item item,
        final Date startDate)
    {
        if (item == null 
            || item.getItemProperty(dateColName) == null
            || item.getItemProperty(dateColName).getValue() == null
            || startDate == null)
        {
            return false;
        }
        String val = item.getItemProperty(dateColName).getValue().toString().trim().toLowerCase();
        if (!val.equals(MortgageTransactionView.POPUP_START_DATE_PROMPT_TEXT))
        {
            String[] itemDateArr = val.split(DATE_DELIMITER);
            Calendar calInstance = getCalendarInstance();
            calInstance.set(
                Integer.parseInt(itemDateArr[2]),
                Integer.parseInt(itemDateArr[1]) - 1,
                Integer.parseInt(itemDateArr[0]));
            DateFormat df = new SimpleDateFormat(DATE_STORAGE_FORMAT);
            String startDateStr = df.format(startDate);
            String itemDateStr = df.format(calInstance.getTime());
            return (Integer.parseInt(itemDateStr) >= Integer.parseInt(startDateStr)) ? true : false;
        }
        return true;
    }

    /**
     * Get the calendar instance
     * 
     * @return instance of a calendar
     */
    private Calendar getCalendarInstance()
    {
        if(cal == null)
        {
            cal = Calendar.getInstance();
        }
        return cal;
    }
}
