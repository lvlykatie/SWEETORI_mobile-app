package com.example.sweetori.dto.response;

public class ResCartDetailDTO {
    private int cartDetailsId;
    private int quantity;
    private ResProductDTO.ProductData product;

    public ResCartDetailDTO() {
    }

    public ResCartDetailDTO(int cartDetailsId, int quantity, ResProductDTO.ProductData product) {
        this.cartDetailsId = cartDetailsId;
        this.quantity = quantity;
        this.product = product;
    }

    public int getCartDetailsId() {
        return cartDetailsId;
    }

    public void setCartDetailsId(int cartDetailsId) {
        this.cartDetailsId = cartDetailsId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ResProductDTO.ProductData getProduct() {
        return product;
    }

    public void setProduct(ResProductDTO.ProductData product) {
        this.product = product;
    }
}
