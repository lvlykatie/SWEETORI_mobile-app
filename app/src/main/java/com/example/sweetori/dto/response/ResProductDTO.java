package com.example.sweetori.dto.response;

import java.io.Serializable;
import java.util.List;

public class ResProductDTO {
    private PaginationWrapper.Meta meta;
    private List<ProductData> data;

    public static class ProductDataManager {
        private static ProductDataManager instance;
        private List<ResProductDTO.ProductData> productList;

        private ProductDataManager() {
        }

        public static synchronized ProductDataManager getInstance() {
            if (instance == null) {
                instance = new ProductDataManager();
            }
            return instance;
        }

        public List<ResProductDTO.ProductData> getProductList() {
            return productList;
        }

        public void setProductList(List<ResProductDTO.ProductData> productList) {
            this.productList = productList;
        }
    }

    public ResProductDTO(PaginationWrapper.Meta meta, List<ProductData> data) {
        this.meta = meta;
        this.data = data;
    }

    public PaginationWrapper.Meta getMeta() {
        return meta;
    }

    public void setMeta(PaginationWrapper.Meta meta) {
        this.meta = meta;
    }

    public List<ProductData> getData() {
        return data;
    }

    public void setData(List<ProductData> data) {
        this.data = data;
    }

    public static class ProductData implements Serializable {
        private int productId;
        private String productName;
        private String description;
        private String descriptionDetails;
        private String image;
        private String productCode;
        private String brand;
        private double sellingPrice;
        private double listPrice;
        private int quantity;
        private double avgRate;
        private ResDiscountDTO discount;

        public ProductData(int productId, String productName, String description, String descriptionDetails, String image,
                           String productCode, String brand, double sellingPrice, double listPrice, int quantity, double avgRate,
                           ResDiscountDTO discount) {
            this.productId = productId;
            this.productName = productName;
            this.description = description;
            this.descriptionDetails = descriptionDetails;
            this.image = image;
            this.productCode = productCode;
            this.brand = brand;
            this.sellingPrice = sellingPrice;
            this.listPrice = listPrice;
            this.quantity = quantity;
            this.avgRate = avgRate;
            this.discount = discount;
        }

        public ResDiscountDTO getDiscount() {
            return discount;
        }

        public void setDiscount(ResDiscountDTO discount) {
            this.discount = discount;
        }

        // Getters
        public int getProductId() { return productId; }
        public String getProductName() { return productName; }
        public String getDescription() { return description; }
        public String getDescriptionDetails() { return descriptionDetails; }
        public String getImage() { return image; }
        public String getProductCode() { return productCode; }
        public String getBrand() { return brand; }
        public double getSellingPrice() { return sellingPrice; }
        public double getListPrice() { return listPrice; }
        public int getQuantity() { return quantity; }
        public double getAvgRate() { return avgRate; }

        // Setters
        public void setProductId(int productId) { this.productId = productId; }
        public void setProductName(String productName) { this.productName = productName; }
        public void setDescription(String description) { this.description = description; }
        public void setDescriptionDetails(String descriptionDetails) { this.descriptionDetails = descriptionDetails; }
        public void setImage(String image) { this.image = image; }
        public void setProductCode(String productCode) { this.productCode = productCode; }
        public void setBrand(String brand) { this.brand = brand; }
        public void setSellingPrice(double sellingPrice) { this.sellingPrice = sellingPrice; }
        public void setListPrice(double listPrice) { this.listPrice = listPrice; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public void setAvgRate(double avgRate) { this.avgRate = avgRate; }
    }
}
