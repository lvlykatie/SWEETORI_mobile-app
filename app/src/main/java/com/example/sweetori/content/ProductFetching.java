package com.example.sweetori.content;

import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.response.ResProductDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductFetching {
    @GET("products")
    Call<APIResponse<ResProductDTO>> getAllProducts();

}
