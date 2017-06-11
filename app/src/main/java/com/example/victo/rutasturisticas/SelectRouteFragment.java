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
import android.widget.Toast;

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
        //It is obtained the routes to be drawn. In addition, the latitude and longitude of the startpoint
        northwestRoute = (MyLinkedList) getArguments().getSerializable("northwestRoute");
        northeastRoute = (MyLinkedList) getArguments().getSerializable("northeastRoute");
        southwestRoute = (MyLinkedList) getArguments().getSerializable("southwestRoute");
        southeastRoute = (MyLinkedList) getArguments().getSerializable("southeastRoute");
        startPoint = (StartPoint) getArguments().getSerializable("startpoint");

        // It is loaded the list of routes whit the values to show in the layout with the next routes
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

        // It is loaded the list of routes in the layout
        RouteAdapter rutaAdapter = new RouteAdapter(getActivity().getApplicationContext(), R.layout.row, routes);
        routesListView.setAdapter(rutaAdapter);

        return view;

    }
    /*
    * This method takes the selected route and starting point and sends them to the map view.
    */
    public void verMapa(View view)
    {
        Toast.makeText(getActivity(), "Mostrando ruta...", Toast.LENGTH_LONG).show();

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

    /*Internal class. Receives an array that will be iterate and for each element it will take actions in recurrent form.*/
    public class RouteAdapter extends ArrayAdapter {

        private List<ArrayList<String>> routes;
        private int resources;
        private LayoutInflater inflater;
        private TextView route;
        private TextView routeDescription;;

        public RouteAdapter(Context context, int resource,  List<ArrayList<String>> objects) {

            super(context, resource, objects);
            routes = objects;
            resources = resource;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        // Iterator method
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){

                convertView = inflater.inflate(resources, null);
            }

            //For each element in the list it will be taken the number and route description,
            // and this values will be load in the listview of the layout.
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
