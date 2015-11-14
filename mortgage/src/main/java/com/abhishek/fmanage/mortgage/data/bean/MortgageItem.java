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
 * Class representing fields of the item
 *  
 * @author Abhishek Gupta
 *
 */
public class MortgageItem
{
    // Unique identifier of the item
    private long itemId;

    // Name of the item
    private String itemName;

    // Weight of the item
    private double itemWeight;

    // Description of the item
    private String itemDescription;

    /**
     * Get the unique identifier of the item
     * 
     * @return unique identifier of the item
     */
    public long getItemId()
    {
        return itemId;
    }

    /**
     * Set the unique identifier of the item
     * 
     * @param itemId unique identifier of the item
     */
    public void setItemId(final long itemId)
    {
        this.itemId = itemId;
    }

    /**
     * Get the name of the item
     * 
     * @return name of the item
     */
    public String getItemName()
    {
        return itemName;
    }

    /**
     * Set the name of the item
     * 
     * @param itemName name of the item
     */
    public void setItemName(final String itemName)
    {
        this.itemName = itemName;
    }

    /**
     * Get the description of the item
     * 
     * @return description of the item
     */
    public String getItemDescription()
    {
        return itemDescription;
    }

    /**
     * Set the description of the item
     * 
     * @param itemDescription description of the item
     */
    public void setItemDescription(final String itemDescription)
    {
        this.itemDescription = itemDescription;
    }

    /**
     * Get the weight of the item
     * 
     * @return weight of the item
     */
    public double getItemWeight()
    {
        return itemWeight;
    }

    /**
     * Set the weight of the item
     * 
     * @param itemWeight weight of the item
     */
    public void setItemWeight(final double itemWeight)
    {
        this.itemWeight = itemWeight;
    }
}
