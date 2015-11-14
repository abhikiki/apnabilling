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
 * This class holds transaction details of the customer associated with the mortgage module
 * 
 * @author Abhishek Gupta
 *
 */
public class MortgageTransaction extends AbstractTransaction
{
    // Date on which the transaction was closed yyyymmdd format
    private String transactionClosedDate;

    // Current state of the transaction. True means transaction is active else inactive/closed
    private boolean transactionStatus;

    // Amount paid to customer by lender for the mortgage
    private double amountPaidToCustomer;

    // Amount paid to customer by lender for the mortgage in words
    private String amountPaidToCustomerInWords;

    // Percent customer has to pay for the mortgage amount
    private double interestRateAssignedToCustomer;

    // Total weight of all the items
    private double totalItemWeight;

    // Description/notes about the transaction
    private String transactionNotes;

    // List of customer payment details
    private List<MortgageCustomerPayment> customerPaymentList = new ArrayList<>();

    /**
     * Get the date on which the transaction was closed yyyymmdd
     * 
     * @return date on which the transaction was closed
     */
    public String getTransactionClosedDate()
    {
        return transactionClosedDate;
    }

    /**
     * Set the date on which the transaction was closed yyyymmdd
     * 
     * @param transactionClosedDate date on which the transaction was closed
     */
    public void setTransactionClosedDate(final String transactionClosedDate)
    {
        this.transactionClosedDate = transactionClosedDate;
    }

    /**
     * Get the current status of the transaction.
     * 
     * @return true if transaction is active else false
     */
    public boolean getTransactionStatus()
    {
        return transactionStatus;
    }

    /**
     * Set the current status of the transaction. True means active. False means inactive/closed
     * 
     * @param transactionStatus set the current status of the transaction
     */
    public void setTransactionStatus(final boolean transactionStatus)
    {
        this.transactionStatus = transactionStatus;
    }

    /**
     * Get the amount paid to customer by lender for the mortgage
     * 
     * @return amount paid to customer by lender for the mortgage
     */
    public double getAmountPaidToCustomer()
    {
        return amountPaidToCustomer;
    }

    /**
     * Set the amount paid to customer by lender for the mortgage
     * 
     * @param amountPaidToCustomer amount paid to customer by lender for the mortgage
     */
    public void setAmountPaidToCustomer(final double amountPaidToCustomer)
    {
        this.amountPaidToCustomer = amountPaidToCustomer;
    }


    /**
     * Get the percent/interest rate customer has to pay for the mortgage amount
     * 
     * @return percent/interest rate customer has to pay for the mortgage amount
     */
    public double getInterestRateAssignedToCustomer()
    {
        return interestRateAssignedToCustomer;
    }

    /**
     * Set the percent/interest rate customer has to pay for the mortgage amount
     * 
     * @param interestRateAssignedToCustomer percent/interest rate customer has to pay for the
     *                                       mortgage amount
     */
    public void setInterestRateAssignedToCustomer(final double interestRateAssignedToCustomer)
    {
        this.interestRateAssignedToCustomer = interestRateAssignedToCustomer;
    }

    /**
     * Get the description/notes about the transaction
     * 
     * @return description/notes about the transaction
     */
    public String getTransactionNotes()
    {
        return transactionNotes;
    }

    /**
     * Set the description/notes about the transaction
     * 
     * @param transactionNotes description/notes about the transaction
     */
    public void setTransactionNotes(final String transactionNotes)
    {
        this.transactionNotes = transactionNotes;
    }

    /**
     * Get the total weight of the items
     * 
     * @return get the total weight of the items
     */
    public double getTotalItemWeight()
    {
        return totalItemWeight;
    }

    /**
     * Set the total weight of all the items
     * 
     * @param totalItemWeight total weight of all the items
     */
    public void setTotalItemWeight(final double totalItemWeight)
    {
        this.totalItemWeight = totalItemWeight;
    }

    /**
     * Get the list of payments done by the customer
     * 
     * @return list of payments done by the customer
     */
    public List<MortgageCustomerPayment> getCustomerPaymentList()
    {
        return customerPaymentList;
    }

    /**
     * Set the list of payments done by the customer
     * 
     * @param customerPaymentList list of payments done by the customer
     */
    public void setCustomerPaymentList(final List<MortgageCustomerPayment> customerPaymentList)
    {
        this.customerPaymentList = customerPaymentList;
    }

    /**
     * Get the total amount paid to customer by lender in words
     * 
     * @return total amount paid to customer by lender in words
     */
    public String getAmountPaidToCustomerInWords()
    {
        return amountPaidToCustomerInWords;
    }

    /**
     * Set the total amount paid to customer by lender in words
     * s
     * @param amountPaidToCustomerInWords total amount paid to customer by lender in words
     */
    public void setAmountPaidToCustomerInWords(final String amountPaidToCustomerInWords)
    {
        this.amountPaidToCustomerInWords = amountPaidToCustomerInWords;
    }
}
