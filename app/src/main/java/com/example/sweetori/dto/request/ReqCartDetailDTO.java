package com.example.sweetori.dto.request;

import com.example.sweetori.dto.request.ReqCartDTO;

public class ReqCartDetailDTO {
    private int productId;
    private int quantity;

    public ReqCartDetailDTO(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
