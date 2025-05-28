package com.example.sweetori.dto.response;

public class ResWishListDTO {
    private int favoriteId;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
    private ResUserDTO user;
    private ResProductDTO.ProductData product;

    // Getters
    public int getFavoriteId() {
        return favoriteId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public ResUserDTO getUser() {
        return user;
    }

    public ResProductDTO.ProductData getProduct() {
        return product;
    }

    // Setters
    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUser(ResUserDTO user) {
        this.user = user;
    }

    public void setProduct(ResProductDTO.ProductData product) {
        this.product = product;
    }
}
