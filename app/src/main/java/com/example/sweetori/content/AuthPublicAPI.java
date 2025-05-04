// AuthPublicAPI.java
package com.example.sweetori.content;

import com.example.sweetori.dto.request.ReqSendEmailDTO;
import com.example.sweetori.dto.response.ResSendEmailDTO;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthPublicAPI {
    @POST("forgot-password")
    Call<ResSendEmailDTO> sendOTP(@Query("email") String email);
}
