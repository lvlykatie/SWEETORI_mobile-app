package com.example.sweetori.dto.response;

public class ShippingDTO {
    public String name;
    public int distanceKm;

    public ShippingDTO(String name, int distanceKm) {
        this.name = name;
        this.distanceKm = distanceKm;
    }

    @Override
    public String toString() {
        return name;
    }
}
