package com.example.victo.rutasturisticas.Domain;

import java.io.Serializable;

/**
 * Created by victo on 8/6/2017.
 */

public class StartPoint implements Serializable
{

    private double latitude;
    private double longitude;
    private String name;

    public StartPoint() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
