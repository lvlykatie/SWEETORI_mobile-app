package com.example.sweetori.dto.response;

public class ResVoucherDTO {
    private int voucherId;
    private String code;
    private String discountAmount;
    private String validFrom;
    private String validTo;

    public ResVoucherDTO(int voucherId, String code, String validFrom, String discountAmount, String validTo) {
        this.voucherId = voucherId;
        this.code = code;
        this.validFrom = validFrom;
        this.discountAmount = discountAmount;
        this.validTo = validTo;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }
}
