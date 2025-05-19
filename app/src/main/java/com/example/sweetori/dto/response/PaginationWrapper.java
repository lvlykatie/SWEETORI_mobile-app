package com.example.sweetori.dto.response;

import java.util.List;

public class PaginationWrapper<T> {
    private Meta meta;
    private List<T> data;

    public Meta getMeta() {
        return meta;
    }

    public List<T> getData() {
        return data;
    }

    public static class Meta {
        private int currentPage;
        private int pageSize;
        private int totalPages;
        private int total;

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
