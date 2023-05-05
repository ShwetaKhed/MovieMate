package com.example.moviemate.service;

import com.example.moviemate.model.MovieResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {

    String BASE_URL = "https://api.themoviedb.org/3/";

    String API_KEY = "6f6d1b438fddb937dd48a7f88b87eae7";
    @GET("movie/now_playing?api_key=" + API_KEY)
    Call<MovieResult> getLatestMovies();

    @GET("movie/top_rated?api_key=6f6d1b438fddb937dd48a7f88b87eae7&page=2")
    Call<MovieResult> getPopularMovies();

    @GET("movie/top_rated")
    Call<MovieResult> getPopularMovies1(@Query("api_key") String api_key, @Query("page") String page);

}
