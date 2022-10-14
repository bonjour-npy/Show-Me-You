package com.example.photoshare.service;

import com.example.photoshare.model.user.UserModel;
import com.example.photoshare.postentity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {

    @Headers({
            "appId:f42b952877c446b9aade2c8fa4d95942",
            "appSecret:13905b55d1e3124c746d49b2da962edf5133a"
    })
    @POST("member/photo/user/register")
    Call<UserModel> register(@Body User user);

    @Headers({
            "appId:f42b952877c446b9aade2c8fa4d95942",
            "appSecret:13905b55d1e3124c746d49b2da962edf5133a"
            })
    @POST("member/photo/user/login")
    Call<UserModel> login(@Query("password") int password,@Query("username") int username);

}
