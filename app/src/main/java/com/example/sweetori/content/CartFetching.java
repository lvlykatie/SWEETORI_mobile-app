package com.example.sweetori.content;

import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.request.ReqAddToCartDTO;
import com.example.sweetori.dto.request.ReqCheckoutDTO;
import com.example.sweetori.dto.response.PaginationWrapper;
import com.example.sweetori.dto.response.ResCartDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Path;
import com.example.sweetori.dto.request.ReqCartDetailDTO;
import com.example.sweetori.dto.response.ResCartDetailDTO;

import java.util.List;

public interface CartFetching {
    @GET("carts/{userId}")
    Call<APIResponse<ResCartDTO>> getCart(@Path("userId") int userId);

    @POST("add-to-cart")
    Call<APIResponse<ResCartDTO>> addCartDetail(@Body ReqAddToCartDTO addToCartRequest);

    @POST("check-out")
    Call<Void> paymentCash(@Body ReqCheckoutDTO orderRequest);

    @DELETE("cart-details/{id}")
    Call<Void> deleteCartItem(@Path("id") int cartDetailId);

    @GET("cart-details")
    Call<APIResponse<PaginationWrapper<ResCartDetailDTO>>> getCartDetail(@Query("filter") List<String> filters);

    @PUT("cart-details")
    Call<Void> updateCartDetailQuantity(@Body ReqCartDetailDTO cartDetailRequest );
}
