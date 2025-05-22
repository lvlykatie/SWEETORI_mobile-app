package com.example.sweetori.dto.response;

import java.util.List;

public class ResCartDTO {
    private int statusCode;
    private String error;
    private String message;
    private CartData data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public CartData getData() {
        return data;
    }

    public static class CartData {
        private Meta meta;
        private List<Cart> data;

        public Meta getMeta() {
            return meta;
        }

        public List<Cart> getData() {
            return data;
        }
    }

    public static class Meta {
        private int currentPage;
        private int pageSize;
        private int totalPages;
        private int total;

        public int getCurrentPage() {
            return currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public int getTotal() {
            return total;
        }
    }

    public static class Cart {
        private int cartId;
        private List<CartDetail> listOfCartdetails;
        private User user;

        public int getCartId() {
            return cartId;
        }

        public List<CartDetail> getListOfCartdetails() {
            return listOfCartdetails;
        }

        public User getUser() {
            return user;
        }
    }

    public static class CartDetail {
        private int cartDetailsId;
        private int quantity;
        private Product product;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }


        public int getCartDetailsId() {
            return cartDetailsId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }
    }

    public static class Product {
        private int productId;
        private String productName;
        private String image;
        private String brand;
        private String productCode;
        private String description;
        private String descriptionDetails;
        private double listPrice;
        private double sellingPrice;
        private int quantity;
        private double avgRate;
        private String discount;
        private String createdAt;
        private String createdBy;
        private String updatedAt;
        private String updatedBy;

        public int getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public String getImage() {
            return image;
        }

        public String getBrand() {
            return brand;
        }

        public String getProductCode() {
            return productCode;
        }

        public String getDescription() {
            return description;
        }

        public String getDescriptionDetails() {
            return descriptionDetails;
        }

        public double getListPrice() {
            return listPrice;
        }

        public double getSellingPrice() {
            return sellingPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getAvgRate() {
            return avgRate;
        }

        public String getDiscount() {
            return discount;
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
    }

    public static class User {
        private int userId;
        private String avatar;
        private String firstName;
        private String lastName;
        private String username;
        private String password;
        private String refreshToken;
        private String gender;
        private String email;
        private String phoneNumber;
        private String buyingAddress;
        private String shippingAddress;
        private Boolean activate;
        private String activateCode;
        private String otp;
        private String otpExp;
        private String createdAt;
        private String createdBy;
        private String updatedAt;
        private String updatedBy;
        private List<Object> vouchers;

        public int getUserId() {
            return userId;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public String getGender() {
            return gender;
        }

        public String getEmail() {
            return email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getBuyingAddress() {
            return buyingAddress;
        }

        public String getShippingAddress() {
            return shippingAddress;
        }

        public Boolean getActivate() {
            return activate;
        }

        public String getActivateCode() {
            return activateCode;
        }

        public String getOtp() {
            return otp;
        }

        public String getOtpExp() {
            return otpExp;
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

        public List<Object> getVouchers() {
            return vouchers;
        }
    }
}
