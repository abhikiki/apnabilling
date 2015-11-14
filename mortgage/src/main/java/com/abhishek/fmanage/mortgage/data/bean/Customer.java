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

/**
 * Class for storing customer information
 * 
 * @author Abhishek Gupta
 * 
 */
public class Customer extends AbstractPerson
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Rating of the customer
    private int rating;

    /**
     * Get the rating of the customer
     * 
     * @return rating of the customer
     */
    public int getRating()
    {
        return rating;
    }

    /**
     * Set the rating of the customer
     * 
     * @param rating of the customer
     */
    public void setRating(final int rating)
    {
        this.rating = rating;
    }
}
