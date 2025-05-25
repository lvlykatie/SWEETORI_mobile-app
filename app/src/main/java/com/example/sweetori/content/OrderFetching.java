package com.example.sweetori.content;

import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.response.ResCartDTO;
import com.example.sweetori.dto.response.ResOrderDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OrderFetching {
    @GET("orders/{userId}")
    Call<APIResponse<ResOrderDTO>> getOrder(@Path("userId") int userId);
}
