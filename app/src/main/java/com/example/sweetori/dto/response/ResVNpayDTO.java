package com.example.sweetori.dto.response;

public class ResVNpayDTO {
    private String paymentUrl;

    public ResVNpayDTO() {}

    public ResVNpayDTO(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}
