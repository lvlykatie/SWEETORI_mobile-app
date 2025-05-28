package com.example.sweetori.content;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.response.PaginationWrapper;
import com.example.sweetori.dto.response.ResWishListDTO;

public interface WishlistFetching {
    @GET("favorites")
    Call<APIResponse<PaginationWrapper<ResWishListDTO>>> getwishlist(@Query("filter") String userId);
    @POST("add-favorites/{productId}")
    Call<Void> addToWishlist(@Path("productId") int productId);
    @DELETE("favorites/{favoritesId}")
    Call<Void> deleteWishlistItem(@Path("favoritesId") int favoritesId);
}

