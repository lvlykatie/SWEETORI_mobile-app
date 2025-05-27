package com.example.sweetori.content;

import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.request.ReqPaymentDTO;
import com.example.sweetori.dto.request.ReqUserDTO;
import com.example.sweetori.dto.response.ResUserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserFetching {
    @GET("users/{id}")
    Call<APIResponse<ResUserDTO>> getUser(@Path("id") int userId);
    @PUT("update-profile")
    Call<APIResponse<ResUserDTO>> updateUser(@Body ReqUserDTO userRequest);
}
