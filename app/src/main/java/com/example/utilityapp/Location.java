package com.example.utilityapp;

import java.io.Serializable;

/**
 * Created by outba on 19/03/2017.
 */

public class Location implements Serializable {

    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
