package com.example.sweetori.dto.response;

public class ResUserDTO {
    private int userId;
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
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
    private ResVoucherDTO voucher;

    public ResUserDTO(String username, String email, String avatar, String firstName, String lastName, String refreshToken, String password, String gender, String buyingAddress, String phoneNumber, String shippingAddress, Boolean activate, String activateCode, String otp, String otpExp, String createdAt, String createdBy, String updatedAt, ResVoucherDTO voucher, String updatedBy) {
        this.username = username;
        this.email = email;
        this.avatar = avatar;
        this.firstName = firstName;
        this.lastName = lastName;
        this.refreshToken = refreshToken;
        this.password = password;
        this.gender = gender;
        this.buyingAddress = buyingAddress;
        this.phoneNumber = phoneNumber;
        this.shippingAddress = shippingAddress;
        this.activate = activate;
        this.activateCode = activateCode;
        this.otp = otp;
        this.otpExp = otpExp;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.voucher = voucher;
        this.updatedBy = updatedBy;
    }

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ResVoucherDTO getVoucher() {
        return voucher;
    }

    public void setVoucher(ResVoucherDTO voucher) {
        this.voucher = voucher;
    }
}
