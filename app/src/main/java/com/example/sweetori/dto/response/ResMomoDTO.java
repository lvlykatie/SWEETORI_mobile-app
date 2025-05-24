package com.example.sweetori.dto.response;

public class ResMomoDTO {
    private String paymentUrl;

    public ResMomoDTO() {}

    public ResMomoDTO(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}