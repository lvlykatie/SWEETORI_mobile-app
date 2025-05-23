package com.example.sweetori.content;

import com.example.sweetori.APIResponse;
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
    @GET("carts/{userId}")
    Call<APIResponse<ResCartDTO>> getCart(@Path("userId") int userId);

    @POST("add-to-cart")
    Call<Void> addCartDetail(@Body ReqCartDetailDTO cartDetailRequest);

    @DELETE("cart-details/{id}")
    Call<Void> deleteCartItem(@Path("id") int cartDetailId);
}
