package com.example.sweetori.content;

import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.request.ReqLoginDTO;
import com.example.sweetori.dto.response.ResLoginDTO;
import com.example.sweetori.dto.request.ReqRegisterDTO;
import com.example.sweetori.dto.response.ResRegisterDTO;
import com.example.sweetori.dto.response.ResSendEmailDTO;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface AuthFetching {

    @POST("auth/sign-in")
    Call<APIResponse<ResLoginDTO>> login(@Body ReqLoginDTO reqLoginDTO);

    @POST("auth/sign-out")
    Call<Void> logout();

    @POST("auth/register")
    Call<APIResponse<ResRegisterDTO>> register(@Body ReqRegisterDTO reqRegisterDTO);

    @POST("auth/forgot-passwd")
    Call<APIResponse<ResSendEmailDTO>> sendOTP(@Query("email") String email);

    @POST("auth/verify-otp")
    Call<ResponseBody> verifyOTP(@Query("email") String email, @Query("otp") String otp);

    @POST("auth/change-forgot-passwd")
    Call<ResponseBody> changePassword(
            @Query("email") String email,
            @Query("otp") String otp,
            @Body Map<String, String> requestBody
    );

}