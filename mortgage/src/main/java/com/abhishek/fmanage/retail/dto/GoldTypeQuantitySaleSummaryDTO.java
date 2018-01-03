package com.abhishek.fmanage.retail.dto;

/**
 * Created by GUPTAA6 on 12/17/2017.
 */
public class GoldTypeQuantitySaleSummaryDTO {
    private String goldType;
    private int quantity;

    public GoldTypeQuantitySaleSummaryDTO(){}

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getGoldType() {
        return goldType;
    }

    public void setGoldType(String goldType) {
        this.goldType = goldType;
    }
}
