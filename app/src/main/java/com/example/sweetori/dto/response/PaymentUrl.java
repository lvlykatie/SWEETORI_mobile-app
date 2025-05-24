package com.example.sweetori.dto.response;

public class PaymentUrl {
    private String paymentUrl;

    public PaymentUrl() {
    }

    public PaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}