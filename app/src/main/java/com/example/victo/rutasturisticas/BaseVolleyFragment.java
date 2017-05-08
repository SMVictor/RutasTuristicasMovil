package com.example.victo.rutasturisticas;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.example.victo.rutasturisticas.Modules.VolleyS;

/******************************* LUEGO ELIMINO ESTA CLASE *****************************************/
/**
 * A simple {@link Fragment} subclass.
 */
public class BaseVolleyFragment extends Fragment
{
    //Declaración de variables globales
    private VolleyS volley;
    protected RequestQueue fRequestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        volley = VolleyS.getInstance(getActivity().getApplicationContext());
        fRequestQueue = volley.getRequestQueue();
    }//Fin del método onCreate
}//Fin de la la clase