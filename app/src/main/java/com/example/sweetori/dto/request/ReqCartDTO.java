package com.example.sweetori.dto.request;

public class ReqCartDTO {
    private int cartId;

    public ReqCartDTO(int cartId) {
        this.cartId = cartId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
