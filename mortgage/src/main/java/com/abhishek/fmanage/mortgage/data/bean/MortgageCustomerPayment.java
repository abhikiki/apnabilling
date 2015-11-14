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

import java.util.Date;

/**
 * Class containing fields related to payment done by the customer
 * 
 * @author Abhishek Gupta
 */
public class MortgageCustomerPayment
{
    // Amount paid by the customer
    private double amountPaid;

    // Date on which the customer paid
    private Date paymentDate;

    /**
     * Get the amount paid by the customer
     * 
     * @return amount paid by the customer
     */
    public double getAmountPaid()
    {
        return amountPaid;
    }

    /**
     * Set the amount paid by the customer
     * 
     * @param amountPaid amount paid by the customer
     */
    public void setAmountPaid(final double amountPaid)
    {
        this.amountPaid = amountPaid;
    }

    /**
     * Date on which the customer paid
     * 
     * @return date on which the customer paid
     */
    public Date getPaymentDate()
    {
        return paymentDate;
    }

    /**
     * Set the date on which the customer paid
     * 
     * @param paymentDate date on which the customer paid
     */
    public void setPaymentDate(final Date paymentDate)
    {
        this.paymentDate = paymentDate;
    }
}
