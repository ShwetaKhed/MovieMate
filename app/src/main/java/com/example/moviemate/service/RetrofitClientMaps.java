package com.example.moviemate.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientMaps {
    private static RetrofitClientMaps instance = null;
    private RetrofitInterfaceMaps retrofitInterfaceMaps;

    private RetrofitClientMaps() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RetrofitInterfaceMaps.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterfaceMaps = retrofit.create(RetrofitInterfaceMaps.class);
    }

    public static synchronized RetrofitClientMaps getInstance() {
        if (instance == null) {
            instance = new RetrofitClientMaps();
        }
        return instance;
    }

    public RetrofitInterfaceMaps getMyApi() {
        return retrofitInterfaceMaps;
    }
}
