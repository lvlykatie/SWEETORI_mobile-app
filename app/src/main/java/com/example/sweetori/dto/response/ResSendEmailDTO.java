package com.example.sweetori.dto.response;

public class ResSendEmailDTO {
    private String otp;
    private String exp;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }
}
