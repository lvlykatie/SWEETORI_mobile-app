package com.example.sweetori.dto.response;

import java.util.List;

public class PaginationWrapper<T> {
    private Meta meta;
    private List<T> data;

    public PaginationWrapper(Meta meta, List<T> data) {
        this.meta = meta;
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public static class Meta {
        private int currentPage;
        private int pageSize;
        private int totalPages;
        private int total;

        public Meta(int currentPage, int pageSize, int totalPages, int total) {
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalPages = totalPages;
            this.total = total;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public int getTotal() {
            return total;
        }
    }
}
