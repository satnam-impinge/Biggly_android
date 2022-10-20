package com.example.biggly.retrofit;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author PA1810.
 */
public class APIClient {

    private static Retrofit retrofit = null;

    public static final String BASE_URL = "https://bigg.ly/";

    public static final String MEDIA_URL = "";

    public static Retrofit getClient(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    //    httpClient.addInterceptor(logging);


        httpClient.addNetworkInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {

                Request.Builder requestBuilder = chain.request().newBuilder();
//                SharedPreference.fetchPrefenceData(context, PreferenceData.TOKEN);
//
//                if (!TextUtils.isEmpty(SharedPreference.fetchPrefenceData(context, PreferenceData.TOKEN))) {
//                    requestBuilder.header("Authorization", "Bearer " + SharedPreference.fetchPrefenceData(context, PreferenceData.TOKEN));
//            // requestBuilder.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiMDVmYmQ3ZGZkNGFiMThjNDE5ZWY1N2E2MjJkNGE4ZGU2MmEyYWNlMWI0ZTYwMGNmODhlODZiOTgzYTc1NzhiN2M4YTMyN2UxNDhkMmFhZmYiLCJpYXQiOjE2MzM2OTc4MTQsIm5iZiI6MTYzMzY5NzgxNCwiZXhwIjoxNjY1MjMzODE0LCJzdWIiOiIyMSIsInNjb3BlcyI6W119.GW2t0apamom8a6pEgIWee-fCQunL0KFS8XPDlyyFINpSQlkeeT3pygdIqqVAJtQIV1_RsGhUf6wjv6Z17dm1Fa-QMWh1Z7WjGEPMQ2LiBaoQwg7BumvtYi9byHINWzQFuUza5-QTAyjrvAOEUNvb4zuUijQ7vIDeHHmjkhzUMKXmPjMfK3ss3WKkUwcIw-GKQbtY--0gthO7y6vZwgHMNMaKXUjwD5vrbmy_-5JAYJ_d5ZtyDIIZwXQ9IluDTW8_-jo8HcOHyH0jX40OXDPFm0PjMdm3VnEO1yBSzxkD_W3DtC9js6K3kS8kXZ8fJKDlt0NE1sA-udywmPejiwaVyDArirlnLRC5IGWJQxg5WoXcFB_QSgsWJYgKo0F-nJxBa5YL-u9iyqeuGTGm4fiLOg1YmBSe_vtCoE_n3XSp0dr4KoYAmUhSnBjIksZvmbuIUQTAv4iG4GIYhNvGF8HOLHS0CRv3XNw_ABjmTdKQ91yaY7A2OWasKAG8GqWt0KvDmJjhCh0Rt3_i7HOcHq3_0fqpW9-RehowIwSV9HdBlgmInQkBlMWSuoSamQHG_4z_wcK6iV1Zet-BmfCM6jXBHQgpXE9RJ0TVE2_n_uLUpQ9pZUQ5qr2v68LXTXBzmbkNTSVUpwKkzl9JZJd20by71t_7iTG-Yoxw8VxnAwG4jjU");
//                }
//                Log.e("TOKEN", "" + SharedPreference.fetchPrefenceData(context, PreferenceData.TOKEN));
                return chain.proceed(requestBuilder.build());
            }
        });


        //AIzaSyD7sDOOUyWIAGMNxLXmqCSh4dFytqfitE4
        //cf38b415e5874049a6ea92cf59919479
        httpClient.connectTimeout(100, TimeUnit.MINUTES);
        httpClient.readTimeout(100, TimeUnit.MINUTES);
        httpClient.writeTimeout(100, TimeUnit.MINUTES);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(/*BuildConfig.BASE_URL*/BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }

        return retrofit;

    }
}
