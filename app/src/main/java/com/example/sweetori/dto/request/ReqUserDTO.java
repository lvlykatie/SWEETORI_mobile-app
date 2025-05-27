package com.example.sweetori.dto.request;

public class ReqUserDTO {
    private String username;
    private String email;
    private String avatar;
    private String firstName;
    private String lastName;
    private String password;
    private String refreshToken;
    private String gender;
    private String phoneNumber;
    private String buyingAddress;
    private String shippingAddress;
    private Boolean activate;
    private String activateCode;
    private String otp;
    private String otpExp;

    public ReqUserDTO() {
    }

    public ReqUserDTO(String firstName, String lastName, String phoneNumber, String email, String buyingAddress ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.buyingAddress = buyingAddress;

    }

    public ReqUserDTO(String username, String email, String avatar, String firstName, String lastName, String password,
                      String refreshToken, String gender, String phoneNumber, String buyingAddress,
                      String shippingAddress, Boolean activate, String activateCode, String otp, String otpExp) {
        this.username = username;
        this.email = email;
        this.avatar = avatar;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.refreshToken = refreshToken;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.buyingAddress = buyingAddress;
        this.shippingAddress = shippingAddress;
        this.activate = activate;
        this.activateCode = activateCode;
        this.otp = otp;
        this.otpExp = otpExp;
    }

    // Getters và Setters

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBuyingAddress() {
        return buyingAddress;
    }

    public void setBuyingAddress(String buyingAddress) {
        this.buyingAddress = buyingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Boolean getActivate() {
        return activate;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    public String getActivateCode() {
        return activateCode;
    }

    public void setActivateCode(String activateCode) {
        this.activateCode = activateCode;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOtpExp() {
        return otpExp;
    }

    public void setOtpExp(String otpExp) {
        this.otpExp = otpExp;
    }
}
