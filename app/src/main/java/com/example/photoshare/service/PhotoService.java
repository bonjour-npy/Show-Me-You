package com.example.photoshare.service;

import androidx.lifecycle.LiveData;

import com.example.photoshare.model.caogao.CaoGaoModel;
import com.example.photoshare.model.fabu.FabuModel;
import com.example.photoshare.model.loadphoto.LoadPhotoModel;
import com.example.photoshare.model.loadphoto.UploadAll;
import com.example.photoshare.model.user.UserModel;
import com.example.photoshare.postentity.FaBu;
import com.example.photoshare.postentity.ShareAdd;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PhotoService {

    @Headers({
            "Accept:application/json, text/plain, */*",
            "Content-Type:application/json"
    })
    @POST("member/photo/image/upload")
    Call<LoadPhotoModel> uploadphoto(@Query("fileList") ArrayList<File> fileList );
    //上传图片文件，获取图片组唯一标识码

    @POST("member/photo/share/add")
    Call<UploadAll> uploadall(@Body ShareAdd shareAdd);
    //用户码和图片标识码保存用户图片分享

    @POST("member/photo/share/change")
    Call<UploadAll> change_photo(@Body ShareAdd shareAdd);
    //发布用户分享的动态

    //1561985398480179200
    @GET("member/photo/share/save")
    Call<CaoGaoModel> save_photo(@Query("current") int current,@Query("size") int size,@Query("userId") String userId);
    //保存用户分享的动态

    @POST("member/photo/share/save")
    Call<UploadAll> save_photo(@Body ShareAdd shareAdd);

    @GET("member/photo/share/detail")
    Call<FabuModel> get_caogaodatail(@Query("shareId") String current, @Query("userId") String userId);

    @POST("member/photo/share/change")
    Call<UserModel> fabu(@Body FaBu faBu);

    @GET("member/photo/share/myself")
    Call<CaoGaoModel> myself(@Query("current") int current,@Query("size") int size,@Query("userId") String userId);
    //current=3&size=5&userId=1561985398480179200"
}