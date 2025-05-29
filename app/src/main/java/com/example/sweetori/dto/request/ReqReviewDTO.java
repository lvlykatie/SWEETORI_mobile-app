
package com.example.sweetori.dto.request;

public class ReqReviewDTO {
    private String userName;
    private String avatarUrl;
    private float rate;
    private String feedback;
    private String date;// Thêm trường này

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ReqReviewDTO() {
        this.userName = userName;
        this.avatarUrl = avatarUrl;
        this.rate = rate;
        this.feedback = feedback;
        this.date = date;
    }
}