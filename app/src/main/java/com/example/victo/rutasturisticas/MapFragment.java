package com.example.victo.rutasturisticas;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.victo.rutasturisticas.Domain.StartPoint;
import com.example.victo.rutasturisticas.Modules.VolleyS;
import com.example.victo.rutasturisticas.Utilities.MyLinkedList;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class MapFragment extends SupportMapFragment  implements OnMapReadyCallback
{
    //Declaración de variables globales
    private GoogleMap mMap;
    private int idActivity;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private MyLinkedList route;
    private StartPoint startPoint;
    private String urlAPIDirections;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        volley = VolleyS.getInstance(getActivity().getApplicationContext());
        fRequestQueue = volley.getRequestQueue();
        getMapAsync(this);

        //Se obtienen las coordenadas de la ruta a dibujar y de la coordenada inicial.
        route = (MyLinkedList) getArguments().getSerializable("route");
        startPoint = (StartPoint) getArguments().getSerializable("startpoint");

        //Se verifica que la lista no esté vacía.
        if(route.size() > 0)
        {
            this.urlAPIDirections = "https://maps.googleapis.com/maps/api/directions/json?origin="+startPoint.getLatitude()+
                    ","+startPoint.getLongitude()+"&destination="+route.getNode((route.size()-1)).node.getLatitude()+
                    ","+route.getNode((route.size()-1)).node.getLongitude();
        }//Fin del if
        return  rootView;
    }//Fin del método onCreateView

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.mMap = googleMap;
        this.mMap.setOnMarkerClickListener
                (
                        new GoogleMap.OnMarkerClickListener()
                        {
                            @Override
                            public boolean onMarkerClick(Marker marker)
                            {
                                //Obtenemos el identificador del nodo
                                Integer idNode = (Integer) marker.getTag();

                                //Verifica si el id del nodo es diferente a cero
                                if(idNode != 0)
                                {
                                    NodeFragment fragment = new NodeFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.contenedor, fragment).addToBackStack(null).commit();
                                }//Fin del if
                                return false;
                            }//Fin del método onMarkerClick
                        }//Fin de la clase Interna Anonima
                );
        this.setInitialLocationMap(); //Establece la posición inicial

        //Se verifica que la lista no esté vacía.
        if(route.size() > 0)
        {
            this.setPointsInMap();
            this.createRoute();
        }//Fin del if
    }//Fin del método


    /**
     * Método que establece la posición inicial del mapa
     * */
    public void setInitialLocationMap()
    {
        //Se establece la posición inicial (Latitud y Longitud)
        LatLng initialPosition = new LatLng(this.startPoint.getLatitude(),this.startPoint.getLongitude());
        this.mMap.addMarker(new MarkerOptions()
                .position(initialPosition)
                .title(this.startPoint.getName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).setTag(0);
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition,13));
    }//Fin del método setInitialLocationMap

    /**
     * Método que se encarga de agregar marcadores al mapa
     * */
    public void setPointsInMap()
    {
        //Se pregunta si la ruta posee paradas
        if(route.size() > 1)
        {
            this.urlAPIDirections += "&waypoints=";

            for(int i = 0; i < route.size(); i++)
            {
                this.mMap.addMarker(new MarkerOptions()
                        .title(route.getNode(i).node.getName())
                        .position(new LatLng(route.getNode(i).node.getLatitude(),route.getNode(i).node.getLongitude())))
                        .setTag(route.getNode(i).node.getId());
                this.urlAPIDirections += route.getNode(i).node.getLatitude() + "," + route.getNode(i).node.getLongitude();
                if(i != (route.size()-1)){this.urlAPIDirections += "|";}
            }//Fin del for
        }//Fin del if
        else
        {
            this.mMap.addMarker(new MarkerOptions()
                    .title(route.getNode(0).node.getName())
                    .position(new LatLng(route.getNode(0).node.getLatitude(),route.getNode(0).node.getLongitude())))
                    .setTag(route.getNode(0).node.getId());
        }//Fin del else
        this.urlAPIDirections += "&key=AIzaSyDAJR9mkRkdrTsO5yjbBaGQxPjOzXuyfUQ";
    }//Fin del método setPointsInMap

    /**
     * Método que se encarga de crear las rutas
     * */
    public void createRoute()
    {
        JsonObjectRequest request = new JsonObjectRequest
            (Request.Method.GET, this.urlAPIDirections, "",
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            try
                            {
                                JSONArray jsonRoutes = response.getJSONArray("routes");

                                JSONObject jsonRoute = jsonRoutes.getJSONObject(0);
                                JSONObject jsonOverviewPolyline = jsonRoute.getJSONObject("overview_polyline");
                                String points = jsonOverviewPolyline.getString("points");
                                List<LatLng> listNodes = PolyUtil.decode(points);
                                PolylineOptions po = new PolylineOptions();
                                po.color(Color.BLUE);
                                po.addAll(listNodes);
                                mMap.addPolyline(po);
                            }//Fin del try
                            catch(Exception e){}
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {}
                    }
            );

        addToQueue(request);
    }//Fin del método createRoute

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
            request.setRetryPolicy(new DefaultRetryPolicy(60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            fRequestQueue.add(request);
        }//Fin del if request diferente null
    }//Fin del método
}//Fin de la clase mapa