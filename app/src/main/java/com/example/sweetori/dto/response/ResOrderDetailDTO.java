package com.example.sweetori.dto.response;

import java.util.List;

public class ResOrderDetailDTO {
    private int orderDetailsId;
    private int quantity;
    private double price;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
    private ResProductDTO.ProductData product;

    public ResOrderDetailDTO(int orderDetailsId, int quantity, double price, String createdAt, String createdBy, String updatedAt, String updatedBy, ResProductDTO.ProductData product) {
        this.orderDetailsId = orderDetailsId;
        this.quantity = quantity;
        this.price = price;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.product = product;
    }

    public int getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(int orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public ResProductDTO.ProductData getProduct() {
        return product;
    }

    public void setProduct(ResProductDTO.ProductData product) {
        this.product = product;
    }
}