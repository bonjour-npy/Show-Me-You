package com.example.photoshare.service;

import com.example.photoshare.adapter.PinlunOneAdapter;
import com.example.photoshare.model.pinlun1.PinLunOneModel;
import com.example.photoshare.model.pinlunback.PinLunBackModel;
import com.example.photoshare.model.share.ShareModel;
import com.example.photoshare.postentity.PinLun;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ShareService {

    @Headers({
            "appId:f42b952877c446b9aade2c8fa4d95942",
            "appSecret:13905b55d1e3124c746d49b2da962edf5133a"
    })
    @GET("member/photo/share")
    Call<ShareModel> sharedate(@Query("current") int current, @Query("size") int size, @Query("userId") String userId);

    @Headers({
            "appId:f42b952877c446b9aade2c8fa4d95942",
            "appSecret:13905b55d1e3124c746d49b2da962edf5133a"
    })
    @GET("member/photo/comment/first")
    Call<PinLunOneModel> pinlundate(@Query("shareId") String shareId);

    @Headers({
            "appId:f42b952877c446b9aade2c8fa4d95942",
            "appSecret:13905b55d1e3124c746d49b2da962edf5133a"
    })
    @POST("member/photo/comment/first")
    Call<PinLunBackModel> pinlunfabiao(@Body PinLun pinLun);

}
