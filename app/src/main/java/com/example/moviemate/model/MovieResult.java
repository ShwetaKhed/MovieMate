package com.example.moviemate.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResult {

    @SerializedName("results")
    private List<Movie> results = null;



    public MovieResult(List<Movie> results) {
        this.results = results;
    }

    public List<Movie> getResults() {
        return results;
    }

}
