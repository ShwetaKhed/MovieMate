package com.example.moviemate.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class LocationDetails implements Serializable {
    @SerializedName("place_name")
    private String place_name;

    @SerializedName("center")
    private List center;



    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setCenter(List center) {
        this.center = center;
    }

    public List getCenter() {
        return center;
    }



}
