package com.example.moviemate.model;

import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("original_title")
    private String originalTitle;


    public Movie(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }
}
