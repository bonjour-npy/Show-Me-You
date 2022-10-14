package com.example.photoshare.service;

import com.example.photoshare.model.minelike.MineLikeModel;
import com.example.photoshare.model.shoucang.ShoucangModel;
import com.example.photoshare.model.user.UserModel;
import com.example.photoshare.postentity.DianZan;
import com.example.photoshare.postentity.ShareAdd;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MineService {
    @Headers({
            "appId:f42b952877c446b9aade2c8fa4d95942",
            "appSecret:13905b55d1e3124c746d49b2da962edf5133a"
    })
    @POST("member/photo/like")
    Call<ShoucangModel> dianzan(@Query("shareId") int password, @Query("userId") String username);
        //点赞
        @Headers({
                "appId:f42b952877c446b9aade2c8fa4d95942",
                "appSecret:13905b55d1e3124c746d49b2da962edf5133a"
        })
        @POST("member/photo/like/cancel")
        Call<ShoucangModel> undianzan(@Query("likeId") int likeId);
        //取消点赞
        @Headers({
                "appId:f42b952877c446b9aade2c8fa4d95942",
                "appSecret:13905b55d1e3124c746d49b2da962edf5133a"
        })
        @GET("member/photo/like")
        Call<MineLikeModel> minelike(@Query("userId") String userId);

}
