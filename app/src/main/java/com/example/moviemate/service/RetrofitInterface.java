package com.example.moviemate.service;

import com.example.moviemate.model.MovieResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {
    //String BASE_URL = "https://simplifiedcoding.net/demos/";

    String BASE_URL = "https://api.themoviedb.org/3/";
    @GET("movie/now_playing?api_key=6f6d1b438fddb937dd48a7f88b87eae7")
    Call<MovieResult> getLatestMovies();

}
