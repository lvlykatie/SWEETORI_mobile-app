package com.example.sweetori.content;

import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.response.ResUserVoucherDTO;
import com.example.sweetori.dto.response.ResVoucherDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VoucherFetching {
    @GET("vouchers")
    Call<APIResponse<ResVoucherDTO>> getAllVouchers();

    // Trả về APIResponse chứa ResUserVoucherDTO, không phải List<ResUserVoucherDTO>
    @GET("user-voucher")
    Call<APIResponse<ResUserVoucherDTO>> getVouchersByUser(@Query("filter") String filter);
}
