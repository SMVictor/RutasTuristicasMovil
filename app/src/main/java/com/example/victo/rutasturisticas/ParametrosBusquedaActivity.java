package com.example.victo.rutasturisticas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

public class ParametrosBusquedaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        Spinner tipoAcividadSpinner = (Spinner) findViewById(R.id.tipo_actividad_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterTipoActividad = ArrayAdapter.createFromResource(this,
                R.array.tiposActividad, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterTipoActividad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        tipoAcividadSpinner.setAdapter(adapterTipoActividad);

        Toast.makeText(ParametrosBusquedaActivity.this, "Tipo de Actividad: Tipo de actividades que podrá realizar en los sitios incluidos en la ruta", Toast.LENGTH_LONG).show();
        Toast.makeText(ParametrosBusquedaActivity.this, "Tipo de Actividad: Tipo de actividades que podrá realizar en los sitios incluidos en la ruta", Toast.LENGTH_LONG).show();

        Toast.makeText(ParametrosBusquedaActivity.this, "Costo Promedio: La ruta le presentará desde sitios económicos a otros de mayor costo según su elección", Toast.LENGTH_LONG).show();
        Toast.makeText(ParametrosBusquedaActivity.this, "Costo Promedio: La ruta le presentará desde sitios económicos a otros de mayor costo según su elección", Toast.LENGTH_LONG).show();
    }
    public void seleccionarRuta(View view){
        Intent intent = new Intent(this, SeleccionarRutasActivity.class);
        startActivity(intent);
    }
}
