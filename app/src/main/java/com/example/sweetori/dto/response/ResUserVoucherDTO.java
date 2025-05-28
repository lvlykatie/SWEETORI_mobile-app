package com.example.sweetori.dto.response;

import java.io.Serializable;

public class ResUserVoucherDTO implements Serializable {
    private int id;
    private boolean used;

    public ResUserVoucherDTO(int id, boolean used) {
        this.id = id;
        this.used = used;
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
}
