package com.example.sweetori.dto.request;

import java.util.List;

public class ReqReviewDTO {
    private String userName;
    private String avatarUrl;
    private float rating;
    private String comment;
    private String date;

    public ReqReviewDTO(String userName, String avatarUrl, float rating, String comment, String date, List<String> imageUrls) {
        this.userName = userName;
        this.avatarUrl = avatarUrl;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public float getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
