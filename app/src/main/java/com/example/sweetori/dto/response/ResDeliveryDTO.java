package com.example.sweetori.dto.response;

public class ResDeliveryDTO {
    private int deliveryId;
    private String name;
    private String description;
    private double shippingCost;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;

    public ResDeliveryDTO() {
    }

    public ResDeliveryDTO(int deliveryId, String name, String description, double shippingCost,
                          String createdAt, String createdBy, String updatedAt, String updatedBy) {
        this.deliveryId = deliveryId;
        this.name = name;
        this.description = description;
        this.shippingCost = shippingCost;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
