package com.example.sweetori.dto.response;

import java.io.Serializable;
import java.util.List;

public class ResUserVoucherDTO implements Serializable {

    private PaginationWrapper.Meta meta;
    private List<UserVoucherData> data;

    public PaginationWrapper.Meta getMeta() {
        return meta;
    }

    public void setMeta(PaginationWrapper.Meta meta) {
        this.meta = meta;
    }

    public List<UserVoucherData> getData() {
        return data;
    }

    public void setData(List<UserVoucherData> data) {
        this.data = data;
    }

    public static class UserVoucherData implements Serializable {
        private int id;
        private boolean used;
        private ResUserDTO user;
        private ResVoucherDTO.VoucherData voucher;

        public UserVoucherData(int id, boolean used, ResUserDTO user, ResVoucherDTO.VoucherData voucher) {
            this.id = id;
            this.used = used;
            this.user = user;
            this.voucher = voucher;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public ResUserDTO getUser() {
            return user;
        }

        public void setUser(ResUserDTO user) {
            this.user = user;
        }

        public ResVoucherDTO.VoucherData getVoucher() {
            return voucher;
        }

        public void setVoucher(ResVoucherDTO.VoucherData voucher) {
            this.voucher = voucher;
        }
    }}