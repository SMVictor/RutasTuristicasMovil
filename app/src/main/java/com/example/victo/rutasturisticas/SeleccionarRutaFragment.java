package com.example.victo.rutasturisticas;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SeleccionarRutaFragment extends Fragment {

    private ListView rutasListView;
    private List<ArrayList<String>> rutas;
    private int posicion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_seleccionar_ruta, container, false);

        this.rutasListView = (ListView) view.findViewById(R.id.rutasListView);

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

        RutaAdapter rutaAdapter = new RutaAdapter(getActivity().getApplicationContext(), R.layout.row, rutas);
        rutasListView.setAdapter(rutaAdapter);

        return view;

    }
    public void verMapa(View view)
    {
        MapFragment fragment = new MapFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, fragment).addToBackStack(null).commit();

        Bundle data = new Bundle();
        data.putInt("idActivity", 1);
        data.putDouble("Lat", 9.878132);
        data.putDouble("Long",-83.635680);
        fragment.setArguments(data);
    }

    public class RutaAdapter extends ArrayAdapter {

        private List<ArrayList<String>> rutas;
        private int resources;
        private LayoutInflater inflater;
        private TextView ruta;
        private TextView descripcionRuta;;

        public RutaAdapter(Context context, int resource,  List<ArrayList<String>> objects) {

            super(context, resource, objects);
            rutas = objects;
            resources = resource;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
