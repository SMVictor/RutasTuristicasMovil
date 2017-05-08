package com.example.victo.rutasturisticas.Modules;

/**
 * Created by yunen on 8/5/2017.
 */

public class TypeActivity
{
    //Deaclaración de variables globales
    private int id;
    private String name;

    public TypeActivity(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}//Fin de la clase