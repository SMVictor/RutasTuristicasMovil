package com.example.victo.rutasturisticas.Domain;

import java.io.Serializable;

/**
 * Created by victo on 4/6/2017.
 */

public class Category implements Serializable
{
    private int id;
    private String description;
    private String image;

    public Category() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
