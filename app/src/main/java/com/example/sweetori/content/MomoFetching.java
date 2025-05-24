package com.example.sweetori.content;

import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.request.ReqMomoDTO;
import com.example.sweetori.dto.response.ResMomoDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MomoFetching {
    @POST("momo")
    Call<APIResponse<ResMomoDTO>> momopayment(@Body ReqMomoDTO momoOrder);
}
