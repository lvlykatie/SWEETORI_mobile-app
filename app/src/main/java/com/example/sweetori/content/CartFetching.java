package com.example.sweetori.content;

import com.example.sweetori.dto.response.ResCartDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Path;
import com.example.sweetori.dto.request.ReqCartDetailDTO;

public interface CartFetching {
    @GET("api/carts")
    Call<ResCartDTO> getCart(@Query("filter") String filter);

    @POST("/api/add-to-cart")
    Call<Void> addCartDetail(@Body ReqCartDetailDTO cartDetailRequest);

    @DELETE("api/cart-details/{id}")
    Call<Void> deleteCartItem(@Path("id") int cartDetailId);
}
