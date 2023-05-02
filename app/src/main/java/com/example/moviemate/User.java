package com.example.moviemate;

import java.util.Date;

public class User {
    String email;
    String firstName;

    String lastName;
    String dob;
    String genrePreference;
    String theaterPreference;

    Date dateOfBirth;

    public User() {
    }

    public User(String email, String firstName, String dob, String genrePreference, String theaterPreference) {
        this.email = email;
        this.firstName = firstName;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
