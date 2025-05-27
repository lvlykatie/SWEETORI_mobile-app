package com.example.sweetori.dto.response;

import java.io.Serializable;
import java.util.List;

public class ResOrderDTO implements Serializable {
    private int orderId;
    private String date;
    private String buyingAddress;
    private String shippingAddress;
    private double total;
    private String status;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
    private List<ResOrderDetailDTO> listOfOrderdetails;
    private ResUserDTO user;
    private ResDeliveryDTO delivery;
    private ResPaymentDTO payment;

    public ResOrderDTO(int orderId, String date, String buyingAddress, String shippingAddress, double total, String status, String createdAt, String createdBy, String updatedAt, String updatedBy, List<ResOrderDetailDTO> listOfOrderdetails, ResUserDTO user, ResDeliveryDTO delivery, ResPaymentDTO payment) {
        this.orderId = orderId;
        this.date = date;
        this.buyingAddress = buyingAddress;
        this.shippingAddress = shippingAddress;
        this.total = total;
        this.status = status;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.listOfOrderdetails = listOfOrderdetails;
        this.user = user;
        this.delivery = delivery;
        this.payment = payment;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBuyingAddress() {
        return buyingAddress;
    }

    public void setBuyingAddress(String buyingAddress) {
        this.buyingAddress = buyingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<ResOrderDetailDTO> getListOfOrderdetails() {
        return listOfOrderdetails;
    }

    public void setListOfOrderdetails(List<ResOrderDetailDTO> listOfOrderdetails) {
        this.listOfOrderdetails = listOfOrderdetails;
    }

    public ResUserDTO getUser() {
        return user;
    }

    public void setUser(ResUserDTO user) {
        this.user = user;
    }

    public ResDeliveryDTO getDelivery() {
        return delivery;
    }

    public void setDelivery(ResDeliveryDTO delivery) {
        this.delivery = delivery;
    }

    public ResPaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(ResPaymentDTO payment) {
        this.payment = payment;
    }
    public class OrderDataHolder {
        private List<ResOrderDTO> orderList;
        public List<ResOrderDTO> getOrderList() { return orderList; }
        public void setOrderList(List<ResOrderDTO> orderList) { this.orderList = orderList; }
    }
}

