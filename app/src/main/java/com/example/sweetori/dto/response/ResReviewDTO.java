package com.example.sweetori.dto.response;

public class ResReviewDTO {
    private int feedbackId;
    private String feedback;
    private double rate;
    private String createdAt;
    private User user;
    private Product product;

    public int getFeedbackId() {
        return feedbackId;
    }

    public String getFeedback() {
        return feedback;
    }

    public double getRate() {
        return rate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public Product getProduct() {
        return product;
    }

    public static class User {
        private String firstName;
        private String lastName;

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }

    public static class Product {
        private String productName;
        private String image;

        public String getProductName() {
            return productName;
        }

        public String getImage() {
            return image;
        }
    }
}
