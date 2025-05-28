package com.example.sweetori.dto.response;
import java.time.ZonedDateTime;
import java.util.List;

public class ResVoucherDTO {
    private PaginationWrapper.Meta meta;
    private List<VoucherData> data;

    public ResVoucherDTO(PaginationWrapper.Meta meta, List<VoucherData> data) {
        this.meta = meta;
        this.data = data;
    }

    public PaginationWrapper.Meta getMeta() {
        return meta;
    }

    public void setMeta(PaginationWrapper.Meta meta) {
        this.meta = meta;
    }

    public List<VoucherData> getData() {
        return data;
    }

    public void setData(List<VoucherData> data) {
        this.data = data;
    }

    public static class VoucherData {
        private Integer voucherId;
        private String code;
        private Double discountAmount;
        private String validFrom;
        private String validTo;
        private List<ResUserDTO> users;

        // Dùng transient để tránh parse sâu (nếu không cần)
        private transient List<UserVoucherData> userVouchers;

        public VoucherData() {}

        public VoucherData(Integer voucherId, String code, Double discountAmount, String validFrom, String validTo, List<ResUserDTO> users) {
            this.voucherId = voucherId;
            this.code = code;
            this.discountAmount = discountAmount;
            this.validFrom = validFrom;
            this.validTo = validTo;
            this.users = users;
        }

        public Integer getVoucherId() { return voucherId; }
        public void setVoucherId(Integer voucherId) { this.voucherId = voucherId; }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public Double getDiscountAmount() { return discountAmount; }
        public void setDiscountAmount(Double discountAmount) { this.discountAmount = discountAmount; }

        public String getValidFrom() { return validFrom; }
        public void setValidFrom(String validFrom) { this.validFrom = validFrom; }

        public String getValidTo() { return validTo; }
        public void setValidTo(String validTo) { this.validTo = validTo; }

        public List<ResUserDTO> getUsers() { return users; }
        public void setUsers(List<ResUserDTO> users) { this.users = users; }

        public List<UserVoucherData> getUserVouchers() { return userVouchers; }
        public void setUserVouchers(List<UserVoucherData> userVouchers) { this.userVouchers = userVouchers; }
    }

    // Không tham chiếu ngược lại VoucherData trong lớp này
    public static class UserVoucherData {
        private Integer userVoucherId;
        private Integer userId;
        private Integer voucherId;

        public UserVoucherData() {}

        public UserVoucherData(Integer userVoucherId, Integer userId, Integer voucherId) {
            this.userVoucherId = userVoucherId;
            this.userId = userId;
            this.voucherId = voucherId;
        }

        public Integer getUserVoucherId() { return userVoucherId; }
        public void setUserVoucherId(Integer userVoucherId) { this.userVoucherId = userVoucherId; }

        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }

        public Integer getVoucherId() { return voucherId; }
        public void setVoucherId(Integer voucherId) { this.voucherId = voucherId; }
    }
}

