package com.example.sweetori.content;

import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.request.ReqReviewDTO;
import com.example.sweetori.dto.response.PaginationWrapper;
import com.example.sweetori.dto.response.ResReviewDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FeedbackFetching {
    @GET("feedbacks")
    Call<APIResponse<PaginationWrapper<ResReviewDTO>>> getFeedback(@Query("filter") String filter);

    @POST("add-feedback/{productId}")
    Call<APIResponse<ResReviewDTO>> addFeedback(
            @Path("productId") int productId,
            @Body ReqReviewDTO reqReviewDTO
    );

}