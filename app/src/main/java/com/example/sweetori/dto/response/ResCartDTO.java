package com.example.sweetori.dto.response;

import java.io.Serializable;
import java.util.List;

public class ResCartDTO implements Serializable {
    private int cartId;
    private List<ResCartDetailDTO> listOfCartdetails;


    public ResCartDTO() {
    }

    public ResCartDTO(int cartId, List<ResCartDetailDTO> listOfCartdetails) {
        this.cartId = cartId;
        this.listOfCartdetails = listOfCartdetails;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public List<ResCartDetailDTO>  getListOfCartdetails() {
        return listOfCartdetails;
    }

    public void setListOfCartdetails(List<ResCartDetailDTO> listOfCartdetails) {
        this.listOfCartdetails = listOfCartdetails;
    }
}
