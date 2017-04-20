package com.example.utilityapp;

import java.io.Serializable;


class Location implements Serializable {

    private String city;

    String getCity() {
        return city;
    }

    void setCity(String city) {
        this.city = city;
    }
}
