package com.example.sweetori.content;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import com.example.sweetori.dto.request.ReqCartDetailDTO;

public interface CartDetailFetching {
    @POST("/api/add-to-cart")
    Call<Void> addCartDetail(@Body ReqCartDetailDTO cartDetailRequest);
}
