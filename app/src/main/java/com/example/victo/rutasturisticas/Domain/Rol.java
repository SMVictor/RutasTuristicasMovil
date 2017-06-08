package com.example.victo.rutasturisticas.Domain;

import java.io.Serializable;

/**
 * Created by victo on 8/6/2017.
 */

public class Rol implements Serializable
{
    private int idRole;
    private String name;

    public Rol() {
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
