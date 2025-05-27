package com.example.sweetori.dto.response;

import java.io.Serializable;

public class ResFeeAndDiscountDTO implements Serializable {
    private int fdId;
    private String description;
    private double amount;
    private ResOrderDTO order;

    public ResFeeAndDiscountDTO(int fdId, String description, double amount, ResOrderDTO order) {
        this.fdId = fdId;
        this.description = description;
        this.amount = amount;
        this.order = order;
    }

    public int getFdId() {
        return fdId;
    }

    public void setFdId(int fdId) {
        this.fdId = fdId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public ResOrderDTO getOrder() {
        return order;
    }

    public void setOrder(ResOrderDTO order) {
        this.order = order;
    }
}
