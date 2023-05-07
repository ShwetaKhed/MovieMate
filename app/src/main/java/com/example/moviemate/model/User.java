package com.example.moviemate.model;

import java.util.Date;

public class User {
    String email;
    String firstName;
    String lastName;
    String dob;
    String genrePreference;
    String theaterPreference;

    public User() {
    }

    public User(String email, String firstName, String lastName, String dob, String genrePreference, String theaterPreference) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
