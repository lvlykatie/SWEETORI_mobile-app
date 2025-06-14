package com.example.sweetori.dto.request;

import java.time.OffsetDateTime;

public class ReqPaymentDTO {
    private String name;
    private String description;
    private Long paymentCost;
    private String createdAt;



    public ReqPaymentDTO() {}
    public ReqPaymentDTO(String name, String description, Long paymentCost, String createdAt) {
        this.name = name;
        this.description = description;
        this.paymentCost = paymentCost;
        this.createdAt = createdAt;

    }

    // Getter và Setter
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

    public Long getPaymentCost() {
        return paymentCost;
    }

    public void setPaymentCost(Long paymentCost) {
        this.paymentCost = paymentCost;
    }
}

