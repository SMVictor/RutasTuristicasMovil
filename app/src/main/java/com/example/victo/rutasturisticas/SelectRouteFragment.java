package com.example.victo.rutasturisticas;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.victo.rutasturisticas.Domain.StartPoint;
import com.example.victo.rutasturisticas.Utilities.MyLinkedList;
import java.util.ArrayList;
import java.util.List;

public class SelectRouteFragment extends Fragment {

    private ListView routesListView;
    private List<ArrayList<String>> routes;
    private int pos;
    private MyLinkedList northwestRoute;
    private MyLinkedList northeastRoute;
    private MyLinkedList southwestRoute;
    private MyLinkedList southeastRoute;
    private StartPoint startPoint;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_select_route, container, false);

        routesListView = (ListView) view.findViewById(R.id.lvRoutes);
        //We obtain the routes to be drawn. In addition, the latitude and longitude of the startpoint
        northwestRoute = (MyLinkedList) getArguments().getSerializable("northwestRoute");
        northeastRoute = (MyLinkedList) getArguments().getSerializable("northeastRoute");
        southwestRoute = (MyLinkedList) getArguments().getSerializable("southwestRoute");
        southeastRoute = (MyLinkedList) getArguments().getSerializable("southeastRoute");
        startPoint = (StartPoint) getArguments().getSerializable("startpoint");

        routes = new ArrayList<ArrayList<String>>();

        ArrayList<String> routeOne = new ArrayList<String>();
        routeOne.add("1");
        routeOne.add("Ruta Noroeste");


        ArrayList<String> routeTwo = new ArrayList<String>();
        routeTwo.add("2");
        routeTwo.add("Ruta Noreste");

        ArrayList<String> routeThree = new ArrayList<String>();
        routeThree.add("3");
        routeThree.add("Ruta Suroeste");

        ArrayList<String> routeFour = new ArrayList<String>();
        routeFour.add("4");
        routeFour.add("Ruta Sureste");

        routes.add(routeOne);
        routes.add(routeTwo);
        routes.add(routeThree);
        routes.add(routeFour);

        RutaAdapter rutaAdapter = new RutaAdapter(getActivity().getApplicationContext(), R.layout.row, routes);
        routesListView.setAdapter(rutaAdapter);

        return view;

    }
    public void verMapa(View view)
    {
        MapFragment fragment = new MapFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, fragment).addToBackStack(null).commit();

        Bundle data = new Bundle();

        switch (pos)
        {
            case 0:
                data.putSerializable("route", northwestRoute);
                break;
            case 1:
                data.putSerializable("route", northeastRoute);
                break;
            case 2:
                data.putSerializable("route", southwestRoute);
                break;
            default:
                data.putSerializable("route", southeastRoute);
                break;
        }
        data.putSerializable("startpoint", startPoint);
        fragment.setArguments(data);
    }

    public class RutaAdapter extends ArrayAdapter {

        private List<ArrayList<String>> routes;
        private int resources;
        private LayoutInflater inflater;
        private TextView route;
        private TextView routeDescription;;

        public RutaAdapter(Context context, int resource,  List<ArrayList<String>> objects) {

            super(context, resource, objects);
            routes = objects;
            resources = resource;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){

                convertView = inflater.inflate(resources, null);
            }

            route = (TextView) convertView.findViewById(R.id.ruta);
            routeDescription = (TextView) convertView.findViewById(R.id.descripcionRuta);

            route.setText(routes.get(position).get(0));
            routeDescription.setText(routes.get(position).get(1));

            routeDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    pos = position;
                    verMapa(view);
                }
            });
            return convertView;
        }
    }
}
