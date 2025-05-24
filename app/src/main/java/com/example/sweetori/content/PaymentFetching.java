package com.example.sweetori.content;

import com.example.sweetori.dto.request.ReqPaymentDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PaymentFetching {
    @POST("payments")
    Call<Void> addpayments(@Body ReqPaymentDTO paymentsRequest);
}
