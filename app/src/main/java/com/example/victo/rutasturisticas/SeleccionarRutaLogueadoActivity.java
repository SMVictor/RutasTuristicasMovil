package com.example.victo.rutasturisticas;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeleccionarRutaLogueadoActivity extends AppCompatActivity {

    private ListView rutasListView;
    private List<ArrayList<String>> rutas;
    private int posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_rutas);
        //rutasListView = (ListView) findViewById(R.id.rutasListView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //regresar...
                finish();
            }
        });

        rutasListView = (ListView) findViewById(R.id.rutasListView);
    }
    public void onStart (){

        super.onStart();

        rutas = new ArrayList<ArrayList<String>>();

        ArrayList<String> prueba1 = new ArrayList<String>();
        prueba1.add("1");
        prueba1.add("Ruta de Aventura: En esta ruta podrá encontrar difrentes sitios donde practicar actividades al aire libre y deportes extrmos.");


        ArrayList<String> prueba2 = new ArrayList<String>();
        prueba2.add("2");
        prueba2.add("Ruta de senderismo: En esta ruta podrá recorrer diferentes senderos naturales de la zona de turrialba, apreciar la flora y fauna del lugar y estar en armonía con la naturaleza.");

        ArrayList<String> prueba3 = new ArrayList<String>();
        prueba3.add("3");
        prueba3.add("Ruta de senderismo: En esta ruta podrá recorrer diferentes senderos naturales de la zona de turrialba, apreciar la flora y fauna del lugar y estar en armonía con la naturaleza.");

        rutas.add(prueba1);
        rutas.add(prueba2);
        rutas.add(prueba3);

        RutaAdapter rutaAdapter = new RutaAdapter(getApplicationContext(), R.layout.row, rutas);
        rutasListView.setAdapter(rutaAdapter);
    }

    public void verMapa(View view)
    {
        Intent intent = new Intent(this, MapsActivity.class);
        //Valores que el activity mapa recibe en el otro lado
        intent.putExtra("idActivity","1"); //Lo cambia aquí por el id que isted considere
        intent.putExtra("Lat","9.878132"); //Latitud inicial
        intent.putExtra("Long","-83.635680"); //Longitud inicial
        startActivity(intent);
    }

    public class RutaAdapter extends ArrayAdapter {

        private List<ArrayList<String>> rutas;
        private int resources;
        private LayoutInflater inflater;
        private TextView ruta;
        private TextView descripcionRuta;;

        public RutaAdapter(Context context, int resource, List<ArrayList<String>> objects) {

            super(context, resource, objects);
            rutas = objects;
            resources = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){

                convertView = inflater.inflate(resources, null);
            }

            ruta = (TextView) convertView.findViewById(R.id.ruta);
            descripcionRuta = (TextView) convertView.findViewById(R.id.descripcionRuta);

            ruta.setText(rutas.get(position).get(0));
            descripcionRuta.setText(rutas.get(position).get(1));

            descripcionRuta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    posicion = position;
                    verMapa(view);
                }
            });
            return convertView;
        }
    }
}
