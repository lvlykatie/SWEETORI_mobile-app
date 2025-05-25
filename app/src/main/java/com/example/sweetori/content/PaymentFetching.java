package com.example.sweetori.content;

import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.request.ReqPaymentDTO;
import com.example.sweetori.dto.response.ResPaymentDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PaymentFetching {
    @POST("payments")
    Call<APIResponse<ResPaymentDTO>> addpayments(@Body ReqPaymentDTO paymentsRequest);
}
