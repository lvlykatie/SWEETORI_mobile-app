package com.example.sweetori.dto.response;

import java.util.List;

public class ResProductDTO {
    private Meta meta;
    private List<ProductData> data;

    public ResProductDTO(Meta meta, List<ProductData> productData) {
        this.meta = meta;
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<ProductData> getProductData() {
        return data;
    }

    public void setProductData(List<ProductData> data) {
        this.data = data;
    }


    public class Meta {
        private int currentPage, totalPages, pageSize, total;

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getTotal() {
            return total;
        }

        public Meta(int currentPage, int totalPages, int pageSize, int total) {
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.pageSize = pageSize;
            this.total = total;
        }
    }
    public class ProductData {
        private int productId;
        private String title;
        private String description;
        private String descriptionDetails;
        private String image;
        private String isbn;
        private String author;
        private double sellingPrice;
        private double listPrice;
        private int quantity;
        private double avgRate;

        public ProductData(int productId, String title, String description, String descriptionDetails, String image, String isbn, String author, double sellingPrice, double listPrice, int quantity, double avgRate) {
            this.productId = productId;
            this.title = title;
            this.description = description;
            this.descriptionDetails = descriptionDetails;
            this.image = image;
            this.isbn = isbn;
            this.author = author;
            this.sellingPrice = sellingPrice;
            this.listPrice = listPrice;
            this.quantity = quantity;
            this.avgRate = avgRate;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setDescriptionDetails(String descriptionDetails) {
            this.descriptionDetails = descriptionDetails;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setSellingPrice(double sellingPrice) {
            this.sellingPrice = sellingPrice;
        }

        public void setListPrice(double listPrice) {
            this.listPrice = listPrice;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void setAvgRate(double avgRate) {
            this.avgRate = avgRate;
        }

        public int getProductId() {
            return productId;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getDescriptionDetails() {
            return descriptionDetails;
        }

        public String getImage() {
            return image;
        }

        public String getIsbn() {
            return isbn;
        }

        public String getAuthor() {
            return author;
        }

        public double getSellingPrice() {
            return sellingPrice;
        }

        public double getListPrice() {
            return listPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getAvgRate() {
            return avgRate;
        }
    }
}
