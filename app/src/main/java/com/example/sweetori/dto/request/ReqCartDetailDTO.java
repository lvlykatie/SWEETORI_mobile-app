package com.example.sweetori.dto.request;

public class ReqCartDetailDTO {
    private int cartDetailsId;
    private int quantity;
    private Cart cart;
    private Product product;

    public ReqCartDetailDTO() {
    }

    public ReqCartDetailDTO(int cartDetailsId, int quantity, Cart cart, Product product) {
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public static class Cart {
        private int cartId;

        public Cart() {
        }

        public Cart(int cartId) {
            this.cartId = cartId;
        }

        public int getCartId() {
            return cartId;
        }

        public void setCartId(int cartId) {
            this.cartId = cartId;
        }
    }

    public static class Product {
        private int productId;

        public Product() {
        }

        public Product(int productId) {
            this.productId = productId;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }
    }
}
