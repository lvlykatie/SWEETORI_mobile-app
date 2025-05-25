package com.example.sweetori.dto.response;

public class ResCartDetailDTO {
    private int cartDetailsId;
    private int quantity;
    private Cart cart;
    private ResProductDTO.ProductData product;

    public ResCartDetailDTO() {}

    public ResCartDetailDTO(int cartDetailsId, int quantity, Cart cart, ResProductDTO.ProductData product) {
        this.cartDetailsId = cartDetailsId;
        this.quantity = quantity;
        this.cart = cart;
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

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public ResProductDTO.ProductData getProduct() {
        return product;
    }

    public void setProduct(ResProductDTO.ProductData product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "ResCartDetailDTO{" +
                "cartDetailsId=" + cartDetailsId +
                ", quantity=" + quantity +
                ", cart=" + (cart != null ? cart.toString() : "null") +
                ", product=" + (product != null ? product.toString() : "null") +
                '}';
    }

    // LỚP NỘI BỘ CHO CART
    public static class Cart {
        private int cartId;

        public Cart() {}

        public Cart(int cartId) {
            this.cartId = cartId;
        }

        public int getCartId() {
            return cartId;
        }

        public void setCartId(int cartId) {
            this.cartId = cartId;
        }

        @Override
        public String toString() {
            return "Cart{" + "cartId=" + cartId + '}';
        }
    }
}
