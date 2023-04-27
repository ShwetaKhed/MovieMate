package com.example.moviemate.service;

import com.google.android.gms.common.api.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private RetrofitInterface retrofitInterface;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public RetrofitInterface getMyApi() {
        return retrofitInterface;
    }
}
