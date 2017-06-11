package com.example.victo.rutasturisticas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.victo.rutasturisticas.Modules.VolleyS;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Clase que se encarga de mostrar la información relacionada a un nodo en específico.
 * */
public class NodeFragment extends Fragment
{
    //Declaración de variables globales
    private View view;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private TextView nameNode;
    private int idNode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_node, container, false);
        this.volley = VolleyS.getInstance(getActivity().getApplicationContext());
        this.fRequestQueue = volley.getRequestQueue();
        this.idNode = getArguments().getInt("idNode");

        //Se recuperan los elementos de la vista
        this.nameNode = (TextView) view.findViewById(R.id.textViewName);

        //Se llenan los campos
        this.fillInformation();

        return this.view; //Se retorna la vista
    }//Fin del método onCreateView

    /**
     * Método que se encarga de llenar la información correspondiente al nodo para mostrarlo en pantalla
     * */
    public void fillInformation()
    {
        String url = "http://turritour.000webhostapp.com/api/getnode?id=" + this.idNode;
        JsonArrayRequest request = new JsonArrayRequest
                (url,
                        new Response.Listener<JSONArray>()
                        {
                            @Override
                            public void onResponse(JSONArray response)
                            {

                                try
                                {
                                    //Recorremos el JSONArray
                                    for(int i = 0; i< response.length();i++)
                                    {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        nameNode.setText(jsonObject.optString("name"));
                                    }//Fin del for
                                }
                                catch(Exception e){}

                            }//Fin del método onResponse
                        }, //Fin de la clase interna anonima
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                            }//Fin del método que se ejecuta si ocurre un error
                        }//Fin de la clase interna anonima
                );
        addToQueue(request);
    }//Fin del método fillInformation

    /**
     * Método que manda a ejecutar las solicitudes
     * */
    public void addToQueue(Request request)
    {
        if (request != null)
        {
            request.setTag(this);
            if (this.fRequestQueue == null)
                this.fRequestQueue = this.volley.getRequestQueue();
            request.setRetryPolicy(new DefaultRetryPolicy(60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            this.fRequestQueue.add(request);
        }//Fin del if request diferente null
    }//Fin del método
}//Fin de la clase