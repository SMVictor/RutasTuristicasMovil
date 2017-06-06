package com.example.victo.rutasturisticas.Domain;

import java.io.Serializable;

/**
 * Created by victo on 4/6/2017.
 */

public class TypeActivity implements Serializable
{
    private int id;
    private String description;

    public TypeActivity() {}

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
}
