package com.example.sweetori.content;

import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.response.ResFeeAndDiscountDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FeeAndDiscountFetching {
    @GET("fee-and-discount/{orderId}")
    Call<APIResponse<List<ResFeeAndDiscountDTO>>> getFee(@Path("orderId") int orderId);
}
