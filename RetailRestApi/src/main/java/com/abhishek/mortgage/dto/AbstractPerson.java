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
package com.abhishek.mortgage.dto;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * This abstract class represents a person. Sub class like user or customer can inherit this class
 * to add addition fields like user or customer rating etc.
 * 
 * @author Abhishek Gupta
 */
public class AbstractPerson implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Unique identifier of the person
    private long Id;

    // First name of the person
    @NotEmpty
    @Size(min=1, max=40, message = "First Name should not be empty")
    private String firstName = "";

    // Last name of the person
    @NotNull
    @Size(min=1, max=30)
    private String lastName = "";

    // Contact number of the person
    
    @Digits(integer=10, fraction = 0, message = "Contact Number should be number")
    private String contactNumber = "";

    
    // Email id of the person
    @Email(message = "Email id should be valid or left empty")
    private String emailId = "";

    // Street address of the person
    @NotNull
    private String streetAddress1 = "";

    // Street address of the person
    @NotNull
    private String streetAddress2 = "";

    // City of the person
    @NotNull
    private String city = "Patna";

    // State or province of the person
    @NotNull
    private String stateprovince = "Bihar";

    // zipcode of the person address
    private String zipcode = "";

    // Country of the person
    @NotNull
    private String country = "India";

    // Description about the person
    @Null
    private String description = "";

    /**
     * Get the unique identifier
     * 
     * @return unique identifier
     */
    public long getId()
    {
        return Id;
    }

    /**
     * Set the unique identifier
     * 
     * @param id unique identifier
     */
    public void setId(final long id)
    {
        Id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Get the first name
     * 
     * @param firstName first name
     */
    public void setFirstName(final String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Get the last name
     * 
     * @return last name
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Set the last name
     * 
     * @param lastName last name
     */
    public void setLastName(final String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Get the contact number
     * 
     * @return contact number
     */
    public String getContactNumber()
    {
        return contactNumber;
    }

    /**
     * Set the contact number
     * 
     * @param contactNumber contact number
     */
    public void setContactNumber(final String contactNumber)
    {
        this.contactNumber = contactNumber;
    }

    /**
     * Get the email id
     * 
     * @return email id
     */
    public String getEmailId()
    {
        return emailId;
    }

    /**
     * Set the email id
     * 
     * @param emailId email id
     */
    public void setEmailId(final String emailId)
    {
        this.emailId = emailId;
    }

    /**
     * Get the street address 1
     * 
     * @return street address 1
     */
    public String getStreetAddress1()
    {
        return streetAddress1;
    }

    /*
     * Set the street address1
     */
    public void setStreetAddress1(final String streetAddress1)
    {
        this.streetAddress1 = streetAddress1;
    }

    /**
     * Get the street address2
     * 
     * @return street address2
     */
    public String getStreetAddress2()
    {
        return streetAddress2;
    }

    /**
     * Set the street address2
     * 
     * @param address2 street address2
     */
    public void setStreetAddress2(final String streetAddress2)
    {
        this.streetAddress2 = streetAddress2;
    }

    public String getCity()
    {
        return city;
    }

    /**
     * Set the city
     * 
     * @param city city which the user, person or customer belongs to
     */
    public void setCity(final String city)
    {
        this.city = city;
    }

    /**
     * Get the state or province
     * 
     * @return state or province
     */
    public String getStateprovince()
    {
        return stateprovince;
    }

    /**
     * Set the state or province
     * 
     * @param stateprovince state or province
     */
    public void setStateprovince(final String stateprovince)
    {
        this.stateprovince = stateprovince;
    }

    /**
     * Get the zipcodes
     * 
     * @return zip code
     */
    public String getZipcode()
    {
        return zipcode;
    }

    /**
     * Set the zipcode
     * 
     * @param zipcode zip code
     */
    public void setZipcode(final String zipcode)
    {
        this.zipcode = zipcode;
    }

    /**
     * Get the country
     * 
     * @return country
     */
    public String getCountry()
    {
        return country;
    }

    /**
     * Set the country to which the person, user or customer belongs to
     * 
     * @param country country to which the person, user or customer belongs to
     */
    public void setCountry(final String country)
    {
        this.country = country;
    }

    /**
     * Get the description of person, user or customer
     * 
     * @return description of the person, user or customer
     */
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
