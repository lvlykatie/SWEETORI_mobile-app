package com.example.sweetori.dto.response;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class ResPaymentDTO implements Serializable {
    private int paymentId;
    private String name;
    private String description;
    private Double paymentCost;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;

    public ResPaymentDTO() {
    }

    public ResPaymentDTO(int paymentId, String name, String description, Double paymentCost,
                         String createdAt, String createdBy,
                         String updatedAt, String updatedBy) {
        this.paymentId = paymentId;
        this.name = name;
        this.description = description;
        this.paymentCost = paymentCost;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
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

    public Double getPaymentCost() {
        return paymentCost;
    }

    public void setPaymentCost(Double paymentCost) {
        this.paymentCost = paymentCost;
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
