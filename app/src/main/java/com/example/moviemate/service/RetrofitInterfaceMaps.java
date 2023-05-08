package com.example.moviemate.service;

import com.example.moviemate.model.Location;
import com.example.moviemate.model.MovieResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterfaceMaps {

    String BASE_URL = "https://api.mapbox.com/geocoding/v5/mapbox.places/";


    @GET("malvern%20east.json?proximity=ip&access_token=pk.eyJ1Ijoic2h3ZXR6aW5nIiwiYSI6ImNsaDZieThwZjA0aWozcXFtYWdncDBrOGEifQ.lg0qOE0d64Rfeh305RM2eQ")
    Call<Location> getLatLong();

}
