package com.example.hp.imageupload;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("api/upload")
    Call<ImageClass> uploadImage(@Field("image") String image);
}
