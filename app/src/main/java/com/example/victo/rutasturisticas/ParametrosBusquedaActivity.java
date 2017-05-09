package com.example.victo.rutasturisticas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.victo.rutasturisticas.Modules.TypeActivity;
import com.example.victo.rutasturisticas.Modules.VolleyS;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

public class ParametrosBusquedaActivity extends AppCompatActivity
{
    //Declaración de variables
    private TextView label;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        volley = VolleyS.getInstance(this.getApplicationContext());
        fRequestQueue = volley.getRequestQueue();
        setContentView(R.layout.activity_parametros_busqueda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //regresar...
                finish();
            }
        });

        Spinner distanciaMaximaSpinner = (Spinner) findViewById(R.id.distancia_maxima_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterDistanciaMaxima = ArrayAdapter.createFromResource(this,
                R.array.distancias, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterDistanciaMaxima.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        distanciaMaximaSpinner.setAdapter(adapterDistanciaMaxima);

        Spinner duracionMaximaSpinner = (Spinner) findViewById(R.id.duracion_maxima_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterDuracionMaxima = ArrayAdapter.createFromResource(this,
                R.array.tiempos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterDuracionMaxima.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        duracionMaximaSpinner.setAdapter(adapterDuracionMaxima);

        Spinner costoPromedioSpinner = (Spinner) findViewById(R.id.costo_promedio_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterCostoPromedio = ArrayAdapter.createFromResource(this,
                R.array.costos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterCostoPromedio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        costoPromedioSpinner.setAdapter(adapterCostoPromedio);

        Toast.makeText(ParametrosBusquedaActivity.this, "Tipo de Actividad: Tipo de actividades que podrá realizar en los sitios incluidos en la ruta", Toast.LENGTH_LONG).show();
        Toast.makeText(ParametrosBusquedaActivity.this, "Tipo de Actividad: Tipo de actividades que podrá realizar en los sitios incluidos en la ruta", Toast.LENGTH_LONG).show();

        Toast.makeText(ParametrosBusquedaActivity.this, "Costo Promedio: La ruta le presentará desde sitios económicos a otros de mayor costo según su elección", Toast.LENGTH_LONG).show();
        Toast.makeText(ParametrosBusquedaActivity.this, "Costo Promedio: La ruta le presentará desde sitios económicos a otros de mayor costo según su elección", Toast.LENGTH_LONG).show();

        this.label = (TextView) findViewById(R.id.textView8);
        fillActivitySpinner();
    }

    public void seleccionarRuta(View view)
    {
        Intent intent = new Intent(this, SeleccionarRutasActivity.class);
        startActivity(intent);
    }//Fin del método

    /**
     * Método que llena el spinner de activity
     * */
    public void fillActivitySpinner()
    {
        String url = "http://turritourapi.000webhostapp.com/activity";
        JsonArrayRequest request = new JsonArrayRequest
        (url,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        LinkedList activities = new LinkedList();

                        try
                        {
                            //Recorremos el JSONArray
                            for(int i = 0; i< response.length();i++)
                            {
                                JSONObject jsonObject = response.getJSONObject(i);
                                activities.add(jsonObject.optString("name"));
                            }//Fin del for
                        }
                        catch(Exception e){}

                        Spinner acvitiesSpinner = (Spinner) findViewById(R.id.tipo_actividad_spinner);
                        ArrayAdapter spinnerAdapter = new ArrayAdapter(ParametrosBusquedaActivity.this, android.R.layout.simple_spinner_item ,activities);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        acvitiesSpinner.setAdapter(spinnerAdapter);
                    }//Fin del método onResponse
                }, //Fin de la clase interna anonima
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {}
                }//Fin de la clase interna anonima
        );
        addToQueue(request);
    }//Fin del método

    /**
     * Método que manda a ejecutar las solicitudes
     * */
    public void addToQueue(Request request)
    {
        if (request != null)
        {
            request.setTag(this);
            if (fRequestQueue == null)
                fRequestQueue = volley.getRequestQueue();
            request.setRetryPolicy(new DefaultRetryPolicy(
                    60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            fRequestQueue.add(request);
        }//Fin del if
    }//Fin del método
}//Fin de la clase