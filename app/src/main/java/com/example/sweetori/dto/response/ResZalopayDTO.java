package com.example.sweetori.dto.response;

public class ResZalopayDTO {
    private String paymentUrl;

    public ResZalopayDTO() {}

    public ResZalopayDTO(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}
