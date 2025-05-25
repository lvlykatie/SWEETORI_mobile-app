package com.example.sweetori.content;

import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.request.ReqMomoDTO;
import com.example.sweetori.dto.request.ReqPaymentDTO;
import com.example.sweetori.dto.request.ReqZalopayDTO;
import com.example.sweetori.dto.response.ResMomoDTO;
import com.example.sweetori.dto.response.ResPaymentDTO;
import com.example.sweetori.dto.response.ResZalopayDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PaymentFetching {
    @POST("payments")
    Call<APIResponse<ResPaymentDTO>> addpayments(@Body ReqPaymentDTO paymentsRequest);
    @POST("zalopay")
    Call<APIResponse<ResZalopayDTO>> zalopayment(@Body ReqZalopayDTO zaloOrder);
}
