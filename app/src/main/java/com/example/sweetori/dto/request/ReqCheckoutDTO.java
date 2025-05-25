package com.example.sweetori.dto.request;

import java.util.List;

public class ReqCheckoutDTO {
    private int userId;
    private List<Integer> voucherIds;
    private List<ProductWithQuantity> productWithQuantityList;
    private int deliveryId;
    private int paymentId;

    public static class ProductWithQuantity {
        private int productId;
        private int quantity;

        public ProductWithQuantity() {
        }

        public ProductWithQuantity(int productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public ReqCheckoutDTO() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Integer> getVoucherIds() {
        return voucherIds;
    }

    public void setVoucherIds(List<Integer> voucherIds) {
        this.voucherIds = voucherIds;
    }

    public List<ProductWithQuantity> getProductWithQuantityList() {
        return productWithQuantityList;
    }

    public void setProductWithQuantityList(List<ProductWithQuantity> productWithQuantityList) {
        this.productWithQuantityList = productWithQuantityList;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
}
