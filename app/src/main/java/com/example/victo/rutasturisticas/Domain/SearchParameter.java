package com.example.victo.rutasturisticas.Domain;

/**
 * Created by victo on 11/6/2017.
 */

public class SearchParameter
{
    private int typeActivity;
    private int max_distance;
    private int max_diration;
    private int cost;
    private String email;
    private String clase;

    public SearchParameter() {}

    public int getTypeActivity() {
        return typeActivity;
    }

    public void setTypeActivity(int typeActivity) {
        this.typeActivity = typeActivity;
    }

    public int getMax_distance() {
        return max_distance;
    }

    public void setMax_distance(int max_distance) {
        this.max_distance = max_distance;
    }

    public int getMax_diration() {
        return max_diration;
    }

    public void setMax_diration(int max_diration) {
        this.max_diration = max_diration;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }
}
