package com.example.moviemate;

public class User {
    String email;
    String username;
    String dob;
    String genrePreference;
    String theaterPreference;

    public User() {
    }

    public User(String email, String username, String dob, String genrePreference, String theaterPreference) {
        this.email = email;
        this.username = username;
        this.dob = dob;
        this.genrePreference = genrePreference;
        this.theaterPreference = theaterPreference;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGenrePreference() {
        return genrePreference;
    }

    public void setGenrePreference(String genrePreference) {
        this.genrePreference = genrePreference;
    }

    public String getTheaterPreference() {
        return theaterPreference;
    }

    public void setTheaterPreference(String theaterPreference) {
        this.theaterPreference = theaterPreference;
    }
}
