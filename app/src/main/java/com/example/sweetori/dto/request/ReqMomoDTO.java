package com.example.sweetori.dto.request;

public class ReqMomoDTO {
    private String orderInfo;
    private Long amount;

    public ReqMomoDTO(String orderInfo, Long amount) {
        this.orderInfo = orderInfo;
        this.amount = amount;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
