package com.example.sweetori.dto.response;

public class ResRegisterDTO {
    private int userId;
    private String username;
    private String email;

    // --------------- Getter và Setter ---------------
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}