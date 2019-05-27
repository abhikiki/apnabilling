package com.abhishek.fmanage.retail.dto;

public class GoldTypeWeightSaleSummaryDTO {
    private String goldType;
    private Double weight;

    public GoldTypeWeightSaleSummaryDTO(){}

    public String getGoldType() {
        return goldType;
    }

    public void setGoldType(String goldType) {
        this.goldType = goldType;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
