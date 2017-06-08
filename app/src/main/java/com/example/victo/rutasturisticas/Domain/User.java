package com.example.victo.rutasturisticas.Domain;

import java.io.Serializable;

/**
 * Created by victo on 8/6/2017.
 */

public class User implements Serializable
{
    private String email;
    private String password;
    private String name;
    private String firstLastName;
    private String secondLastName;
    private String profilphoto;
    private Rol rol;

    public User(Rol rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getProfilphoto() {
        return profilphoto;
    }

    public void setProfilphoto(String profilphoto) {
        this.profilphoto = profilphoto;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
