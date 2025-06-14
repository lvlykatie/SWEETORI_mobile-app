package com.example.sweetori.content;

import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.response.ResCartDTO;
import com.example.sweetori.dto.response.ResOrderDTO;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderFetching {
    @GET("order-by-user/{userId}")
    Call<APIResponse<List<ResOrderDTO>>> getOrder(@Path("userId") int userId);

    @PUT("orders")
    Call<APIResponse<ResOrderDTO>> updateOrderStatus(@Body Map<String, Object> body);

}
