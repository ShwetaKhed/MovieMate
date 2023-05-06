package com.example.moviemate.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class UserMovies {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "original_title")
    @NonNull
    public String originalTitle;

    @ColumnInfo(name = "poster_path")
    @NonNull
    public String posterPath;

    @ColumnInfo(name="overview")
    public String overview;

    @ColumnInfo(name="id")
    @NonNull
    public String id;

}
