
package com.example.sweetori.dto.response;

public class ResDiscountDTO {
    private int discountId;
    private double discountPercentage;
    private String description;
    private String startDate;

    public ResDiscountDTO(int discountId, double discountPercentage, String description, String startDate, String endDate) {
        this.discountId = discountId;
        this.discountPercentage = discountPercentage;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private String endDate;

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
