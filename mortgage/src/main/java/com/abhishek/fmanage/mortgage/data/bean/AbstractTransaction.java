/*
 * $Revision$
 *
 * Copyright (c) 2013 by Abhishek's Company Inc.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * Abhishek's Company Inc ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it
 * only in accordance with the terms of the license agreement you entered
 * into with Abhishek's Company.
 */

package com.abhishek.fmanage.mortgage.data.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for transaction. This class holds transaction fields common to all module
 * transaction
 * 
 * @author Abhishek Gupta
 * 
 */
public class AbstractTransaction
{
    // Unique identifier of the transaction
    private long transactionId;

    // Customer associated with this transaction
    private Customer customer;

    // List of items associated with this transaction
    private List<MortgageItem> itemList = new ArrayList<>();

    // Date on which the transaction happened yyyymmdd
    private String transactionOpenedDate;

    /**
     * Get the unique identifier of the transaction 
     * 
     * @return unique identifier of the transaction
     */
    public long getTransactionId()
    {
        return transactionId;
    }

    /**
     * Set the unique identifier of the transaction
     * 
     * @param transactionId unique identifier of the transaction
     */
    public void setTransactionId(final long transactionId)
    {
        this.transactionId = transactionId;
    }

    /**
     * Get the {@link Customer} instance
     * 
     * @return the {@link Customer} associated with the transaction
     */
    public Customer getCustomer()
    {
        return customer;
    }

    /**
     * Set the {@link Customer} associated with this transaction
     * 
     * @param customer {@link Customer} associated with this transaction
     */
    public void setCustomer(final Customer customer)
    {
        this.customer = customer;
    }

    /**
     * Get the list of items associated with this transaction
     * 
     * @return list of items associated with this transaction
     */
    public List<MortgageItem> getItemList()
    {
        return itemList;
    }

    /**
     * Set the list of items associated with this item
     * 
     * @param itemList list of items associated with this item
     */
    public void setItemList(final List<MortgageItem> itemList)
    {
        this.itemList = itemList;
    }

    /**
     * Get the date on which the transaction happened yyyymmdd
     * 
     * @return date on which the transaction happened
     */
    public String getTransactionOpenedDate()
    {
        return transactionOpenedDate;
    }

    /**
     * Set the date on which the transaction happened yyyymmdd
     *  
     * @param transactionOpenedDate date on which the transaction happened
     */
    public void setTransactionOpenedDate(final String transactionOpenedDate)
    {
        this.transactionOpenedDate = transactionOpenedDate;
    }
}
