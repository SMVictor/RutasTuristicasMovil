package com.example.victo.rutasturisticas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Clase que se encarga de mostrar la información relacionada a un nodo en específico.
 * */
public class NodeFragment extends Fragment
{
    //Declaración de variables globales
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_node, container, false);

        return this.view; //Se retorna la vista
    }//Fin del método onCreateView
}//Fin de la clase