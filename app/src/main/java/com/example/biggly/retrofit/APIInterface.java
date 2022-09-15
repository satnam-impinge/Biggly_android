package com.example.biggly.retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


/**
 * @author PA1810.
 */


// for api calling stuff

public interface APIInterface {



    @Multipart
    @POST("transfer")
    Call<ResponseModel> createEvent(
            @Part("email_to") RequestBody email_to,
            @Part("email_from") RequestBody email_from,
            @Part("message") RequestBody message,
            @Part("bot") RequestBody bot,
            @Part("uploadagreeterms") boolean uploadagreeterms,

            @Part  MultipartBody.Part[] imageFile);

}