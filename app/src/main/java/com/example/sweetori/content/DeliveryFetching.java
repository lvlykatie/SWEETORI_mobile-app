package com.example.sweetori.content;

import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.response.PaginationWrapper;
import com.example.sweetori.dto.response.ResDeliveryDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

// Đây phải là interface, không phải class!
public interface DeliveryFetching {
    @GET("deliveries")
    Call<APIResponse<PaginationWrapper<ResDeliveryDTO>>> getdelivery();
    @GET("deliveries/{id}")
    Call<APIResponse<ResDeliveryDTO>> getdeliveryByIs(@Path("id") int deliveryId);
}
