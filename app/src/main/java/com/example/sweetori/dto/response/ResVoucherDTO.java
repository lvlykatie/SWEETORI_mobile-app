package com.example.sweetori.dto.response;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class ResVoucherDTO implements Serializable{
    private PaginationWrapper.Meta meta;
    private List<ResVoucherDTO.VoucherData> data;

    public static class VoucherDataManager {
        private static ResVoucherDTO.VoucherDataManager instance;
        private List<ResVoucherDTO.VoucherData> voucherList;

        private VoucherDataManager() {
        }

        public static synchronized ResVoucherDTO.VoucherDataManager getInstance() {
            if (instance == null) {
                instance = new ResVoucherDTO.VoucherDataManager();
            }
            return instance;
        }

        public List<ResVoucherDTO.VoucherData> VoucherList() {
            return voucherList;
        }

        public void VoucherList(List<ResVoucherDTO.VoucherData> VoucherList) {
            this.voucherList = voucherList;
        }
    }

    public ResVoucherDTO(PaginationWrapper.Meta meta, List<ResVoucherDTO.VoucherData> data) {
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

    public static class VoucherData implements Serializable {
        private Integer voucherId;
        private String code;
        private Double discountAmount;
        private String validFrom;
        private String validTo;
        private List<ResUserDTO> users;
        private  List<ResUserVoucherDTO> userVouchers;//

        // Constructors

        public VoucherData(Integer voucherId, String code, Double discountAmount, String validFrom, String validTo, List<ResUserDTO> users, List<ResUserVoucherDTO> userVouchers) {
            this.voucherId = voucherId;
            this.code = code;
            this.discountAmount = discountAmount;
            this.validFrom = validFrom;
            this.validTo = validTo;
            this.users = users;
            this.userVouchers = userVouchers;
        }

        public Integer getVoucherId() {
            return voucherId;
        }

        public void setVoucherId(Integer voucherId) {
            this.voucherId = voucherId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Double getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(Double discountAmount) {
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

        public List<ResUserDTO> getUsers() {
            return users;
        }

        public void setUsers(List<ResUserDTO> users) {
            this.users = users;
        }

        public List<ResUserVoucherDTO> getUserVouchers() {
            return userVouchers;
        }

        public void setUserVouchers(List<ResUserVoucherDTO> userVouchers) {
            this.userVouchers = userVouchers;
        }
    }
}
