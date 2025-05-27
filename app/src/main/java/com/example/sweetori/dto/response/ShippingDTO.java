package com.example.sweetori.dto.response;

public class ShippingDTO {
    public String name;
    public double deliveryCost;
    public int deliveryId;

    public ShippingDTO(String name, double deliveryCost, int deliveryId) {
        this.name = name;
        this.deliveryCost = deliveryCost;
        this.deliveryId = deliveryId;
    }

    @Override
    public String toString() {
        return name;
    }
}
