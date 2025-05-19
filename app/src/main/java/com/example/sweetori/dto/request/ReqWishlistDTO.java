package com.example.sweetori.dto.request;

public class ReqWishlistDTO {
    private int productId;

    public ReqWishlistDTO(int productId) {
        this.productId = productId;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
