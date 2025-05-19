package com.example.sweetori.dto.request;

public class ReqUserDTO {
    private int userId;

    public ReqUserDTO(int userId) {
        this.userId = userId;
    }

    public int getUsertId() {
        return userId;
    }

    public void setUsertId(int userId) {
        this.userId = userId;
    }
}
