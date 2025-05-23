package com.example.sweetori.content;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

import com.example.sweetori.dto.request.ReqWishlistDTO;

public interface WishlistFetching {
    @POST("add-favorites/{productId}")
    Call<Void> addToWishlist(@Path("productId") int productId);
}

