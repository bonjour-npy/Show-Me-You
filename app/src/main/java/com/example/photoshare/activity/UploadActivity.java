package com.example.photoshare.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.donkingliang.imageselector.utils.ImageSelector;
import com.example.photoshare.adapter.LoadImageAdapter;
import com.example.photoshare.databinding.ActivityUploadBinding;
import com.example.photoshare.model.loadphoto.LoadPhotoModel;
import com.example.photoshare.model.loadphoto.UploadAll;
import com.example.photoshare.postentity.ShareAdd;
import com.example.photoshare.service.PhotoService;
import com.example.photoshare.utils.RetrofitUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadActivity extends AppCompatActivity {

    private ActivityUploadBinding activityUploadBinding;

    private static final int REQUEST_CODE = 0x00000011;
    private static final int PERMISSION_WRITE_EXTERNAL_REQUEST_CODE = 0x00000012;

    private LoadImageAdapter mAdapter;

    private static final String TAG = "UploadActivity";

    private final Gson gson = new Gson();

    ArrayList<File> files = new ArrayList<File>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUploadBinding = ActivityUploadBinding.inflate(getLayoutInflater());
        View view = activityUploadBinding.getRoot();
        setContentView(view);

        activityUploadBinding.rvImage.setLayoutManager(new GridLayoutManager(UploadActivity.this, 3));
        mAdapter = new LoadImageAdapter(UploadActivity.this);
        activityUploadBinding.rvImage.setAdapter(mAdapter);

        int hasWriteExternalPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteExternalPermission == PackageManager.PERMISSION_GRANTED) {
            // ???????????????????????????????????????????????????app????????????????????????
            ImageSelector.preload(this);
        } else {
            // ??????????????????????????????
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_REQUEST_CODE);
        }

        getclock();
        //???????????????????????????
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && data != null) {

            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);

            for (String image : images) {
                files.add(new File(image));
            }
            Log.d(TAG, "onActivityResult: " + files);
            post();

            mAdapter.refresh(images);
        }
    }

    private void post() {
        new Thread(() -> {

            // url??????
            String url = "http://47.107.52.7:88/member/photo/image/upload";
            //-----------------------------????????????????????????--------------------------------------------
            Headers headers = new Headers.Builder()
                    .add("appId", "fd558310540f4ef9ad28d6c52e0015cf")
                    .add("appSecret", "13997193fbadd4e5c466e8bfc381f1882dacc")
                    .add("Accept", "application/json, text/plain, */*")
                    .build();
            // okhttp??????
            MediaType MEDIA_TYPE_PNG = MediaType.parse("application/image/png; charset=utf-8");
            MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);

            for (File file : files) {
                if (file.exists()) {
                    Log.i(TAG, file.getName());
                    mbody.addFormDataPart("fileList", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
                }
            }

            RequestBody requestBody = mbody.build();
            // ??????????????????
            Request request = new Request.Builder()
                    .url(url)
                    // ???????????????????????????
                    .headers(headers)
                    .post(requestBody)
                    .build();

            try {
                // OkHttp???????????????????????????????????????
                OkHttpClient client = new OkHttpClient();
                // ?????????????????????callback????????????
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: ");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Gson gson = new Gson();
                        LoadPhotoModel loadPhotoModel = gson.fromJson(response.body().string(), LoadPhotoModel.class);
                        Log.d(TAG, loadPhotoModel.getData().getImageUrlList().toString());
                        for (int i = 0; i < loadPhotoModel.getData().getImageUrlList().size(); i++) {
                            Log.d(TAG, loadPhotoModel.getData().getImageUrlList().get(i).toString());
                        }
                        SharedPreferences sp = getSharedPreferences("photo", MODE_PRIVATE);
                        sp.edit().putString("photo_id", loadPhotoModel.getData().getImageCode()).apply();
                        //?????????????????????????????????????????????????????????????????????

                    }
                });

                files.clear();
                //??????????????????????????????list????????????????????????????????????

            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void getclock() {

        activityUploadBinding.btnUnlimited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageSelector.builder()
                        .useCamera(true) // ????????????????????????
                        .setSingle(false)  //??????????????????
                        .canPreview(true) //??????????????????????????????,????????????true
                        .setMaxSelectCount(0) // ??????????????????????????????????????????0?????????????????????
                        .start(UploadActivity.this, REQUEST_CODE); // ????????????
            }
        });

        activityUploadBinding.btnClip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageSelector.builder()
                        .useCamera(true) // ????????????????????????
                        .setCrop(true)  // ???????????????????????????????????????
                        .setCropRatio(1.0f) // ????????????????????????,??????1.0f????????????????????????????????????
                        .setSingle(true)  //??????????????????
                        .canPreview(true) //??????????????????????????????,????????????true
                        .start(UploadActivity.this, REQUEST_CODE); // ????????????
            }
        });


        activityUploadBinding.btnTakeAndClip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageSelector.builder()
                        .setCrop(true) // ???????????????????????????????????????
                        .setCropRatio(1.0f) // ????????????????????????,??????1.0f????????????????????????????????????
                        .onlyTakePhoto(true)  // ???????????????????????????
                        .start(UploadActivity.this, REQUEST_CODE);
            }
        });

        activityUploadBinding.uploadAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhotoService photoService = RetrofitUtils.getInstance().getRetrofit().create(PhotoService.class);

                String title = activityUploadBinding.uploadTitle.getText().toString();
                String content = activityUploadBinding.uploadText.getText().toString();
                SharedPreferences sp_user = getSharedPreferences("user", MODE_PRIVATE);
                String user_id = sp_user.getString("id", "???????????????ID");
                SharedPreferences sp_photo = getSharedPreferences("photo", MODE_PRIVATE);
                String photo_id = sp_photo.getString("photo_id", "???????????????ID");
                //???????????? ????????????????????????ID?????????ID

                Log.d(TAG, "???????????????????????????");

                ShareAdd shareAdd = new ShareAdd();
                shareAdd.setContent(content);
                shareAdd.setpUserId(Double.parseDouble(user_id));
                shareAdd.setimageCode(Double.parseDouble(photo_id));
                shareAdd.setTitle(title);

                retrofit2.Call<UploadAll> call = photoService.uploadall(shareAdd);
                call.enqueue(new retrofit2.Callback<UploadAll>() {
                    @Override
                    public void onResponse(retrofit2.Call<UploadAll> call, retrofit2.Response<UploadAll> response) {
                        if (response.body().getCode() == 200) {
                            Log.d(TAG, "????????????" + user_id);
                            Toast toast = Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<UploadAll> call, Throwable t) {
                        Log.d(TAG, "onFailure: ????????????" + t);
                        t.printStackTrace();
                    }
                });
            }
        });

        activityUploadBinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoService photoService = RetrofitUtils.getInstance().getRetrofit().create(PhotoService.class);

                String title = activityUploadBinding.uploadTitle.getText().toString();
                String content = activityUploadBinding.uploadText.getText().toString();
                SharedPreferences sp_user = getSharedPreferences("user", MODE_PRIVATE);
                String user_id = sp_user.getString("id", "???????????????ID");
                SharedPreferences sp_photo = getSharedPreferences("photo", MODE_PRIVATE);
                String photo_id = sp_photo.getString("photo_id", "???????????????ID");
                //???????????? ????????????????????????ID?????????ID

                Log.d(TAG, "????????????");

                ShareAdd shareAdd = new ShareAdd();
                shareAdd.setContent(content);
                shareAdd.setpUserId(Double.parseDouble(user_id));
                shareAdd.setimageCode(Double.parseDouble(photo_id));
                shareAdd.setTitle(title);

                retrofit2.Call<UploadAll> call = photoService.save_photo(shareAdd);
                call.enqueue(new retrofit2.Callback<UploadAll>() {
                    @Override
                    public void onResponse(retrofit2.Call<UploadAll> call, retrofit2.Response<UploadAll> response) {
                        if (response.body().getCode() == 200) {
                            Log.d(TAG, "??????????????????" + user_id + " " + photo_id);
                            Intent intent = new Intent(UploadActivity.this, ShareActivity.class);
                            startActivity(intent);
                            Toast toast = Toast.makeText(getApplicationContext(), "??????????????????", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<UploadAll> call, Throwable t) {
                        Log.d(TAG, "onFailure: ??????????????????" + t);
                        t.printStackTrace();
                    }
                });
            }
        });

    }

}
