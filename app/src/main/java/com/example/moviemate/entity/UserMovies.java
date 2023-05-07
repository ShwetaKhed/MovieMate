package com.example.moviemate.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserMovies {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "user_email")
    @NonNull
    public String userEmail;
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

    @ColumnInfo(name="release_date")
    @NonNull
    public String releaseDate;

    public UserMovies (@NonNull String userEmail ,@NonNull String originalTitle, @NonNull String posterPath,
                       @NonNull String overview, @NonNull String id, @NonNull String releaseDate){

        if (userEmail == "" || userEmail == null){
            this.userEmail = "someone@example.com";
        }else{
            this.userEmail = userEmail;
        }

        if (originalTitle == "" || originalTitle == null){
            this.originalTitle = "Not available";
        }else{
            this.originalTitle = originalTitle;
        }

        if (posterPath == "" || posterPath == null){
            this.posterPath = "Not available";
        }else{
            this.posterPath = posterPath;
        }

        if (overview == "" || overview == null){
            this.overview = "Not available";
        }else{
            this.overview = posterPath;
        }

        if (id == "" || id == null){
            this.id = "Not available";
        }else{
            this.id = id;
        }

        if (releaseDate == "" || releaseDate == null){
            this.releaseDate = "Not available";
        }else{
            this.releaseDate = releaseDate;
        }

    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        if (id == "" || id == null){
            this.id = "Not available";
        }else{
            this.id = id;
        }
    }

    @NonNull
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(@NonNull String userEmail) {
        if (userEmail == "" || userEmail == null){
            this.userEmail = "someone@example.com";
        }else{
            this.userEmail = userEmail;
        }
    }

    @NonNull
    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(@NonNull String originalTitle) {
        if (originalTitle == "" || originalTitle == null){
            this.originalTitle = "Not available";
        }else{
            this.originalTitle = originalTitle;
        }
    }

    @NonNull
    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(@NonNull String posterPath) {
        if (posterPath == "" || posterPath == null){
            this.posterPath = "Not available";
        }else{
            this.posterPath = posterPath;
        }
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        if (overview == "" || overview == null){
            this.overview = "Not available";
        }else{
            this.overview = posterPath;
        }
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        if (id == "" || id == null){
            this.id = "Not available";
        }else{
            this.id = id;
        }
    }

    @NonNull
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(@NonNull String releaseDate) {
        if (releaseDate == "" || releaseDate == null){
            this.releaseDate = "Not available";
        }else{
            this.releaseDate = releaseDate;
        }
    }

    public UserMovies()
    {

    }
}
