package com.example.victo.rutasturisticas.Domain;

import java.io.Serializable;

/**
 * Created by kefca on 06/06/2017.
 */

public class User implements Serializable
    {
        private String name;
        private String firstlastname;

        public User(String name, String firstlastname, String secondlastname, String profilphoto, int idroles, String email, String password) {
            this.name = name;
            this.firstlastname = firstlastname;
            this.secondlastname = secondlastname;
            this.profilphoto = profilphoto;
            this.idroles = idroles;
            this.email = email;
            this.password = password;
        }

        public User() {
        }

        private String secondlastname;
        private String profilphoto;
        private int idroles;
        private String email;
        private String password;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFirstlastname() {
            return firstlastname;
        }

        public void setFirstlastname(String firstlastname) {
            this.firstlastname = firstlastname;
        }

        public String getSecondlastname() {
            return secondlastname;
        }

        public void setSecondlastname(String secondlastname) {
            this.secondlastname = secondlastname;
        }

        public String getProfilphoto() {
            return profilphoto;
        }

        public void setProfilphoto(String profilphoto) {
            this.profilphoto = profilphoto;
        }

        public int getIdroles() {
            return idroles;
        }

        public void setIdroles(int idroles) {
            this.idroles = idroles;
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
    }
