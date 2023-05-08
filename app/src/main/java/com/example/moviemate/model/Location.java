package com.example.moviemate.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Location {

    @SerializedName("features")
    private List<LocationDetails> features = null;



    public Location(List<LocationDetails> features) {
        this.features = features;
    }

    public List<LocationDetails> getLocationDetails() {
        return features;
    }

}
