package com.example.victo.rutasturisticas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.TimeZoneFormat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.victo.rutasturisticas.Modules.VolleyS;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{
    //declaración de variables globales
    private GoogleMap mMap;
    private int idActivity;
    private float lat,log;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        volley = VolleyS.getInstance(this.getApplicationContext());
        fRequestQueue = volley.getRequestQueue();
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.idActivity = Integer.parseInt(getIntent().getStringExtra("idActivity"));
        this.lat = Float.parseFloat(getIntent().getStringExtra("Lat"));
        this.log = Float.parseFloat(getIntent().getStringExtra("Long"));
    }//Fin del método onCreate

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.mMap = googleMap;
        this.setInitialLocationMap(); //Establecer la posición inicial
        this.setPointsInMap();
        this.createRoute();
    }//Fin del método

    /**
     * Método que establece la posición inicial del mapa
     * */
    public void setInitialLocationMap()
    {
        LatLng initialPosition = new LatLng(this.lat,this.log);
        this.mMap.addMarker(new MarkerOptions()
            .position(initialPosition)
            .title("Usted esta aquí")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition,13));
    }//Fin del método setInitialLocationMap

    public void setPointsInMap()
    {
        //Creamos las coordenadas
        LatLng marta  = new LatLng(9.786,-83.691);
        LatLng guayabo  = new LatLng(9.9727991,-83.6908688);
        LatLng museo  = new LatLng(9.9013114,-83.672462);

        //Agregamos los marcadores
        this.mMap.addMarker(new MarkerOptions()
                .title("Monumento Nacional Guayabo")
                .position(guayabo));
        this.mMap.addMarker(new MarkerOptions()
                .title("Refugio de Vida Silvestre La Marta")
                .position(marta));
        this.mMap.addMarker(new MarkerOptions()
                .title("Museo Omar Salazar Obando")
                .position(museo));
    }//Fin del método setPointsInMap

    public void createRoute()
    {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=9.878132,-83.635680&destination=9.9727991,-83.6908688&waypoints=9.9013114,-83.672462&key=AIzaSyDAJR9mkRkdrTsO5yjbBaGQxPjOzXuyfUQ";

        JsonObjectRequest request = new JsonObjectRequest
        (Request.Method.GET, url, "",
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
            request.setRetryPolicy(new DefaultRetryPolicy(
                    60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            fRequestQueue.add(request);
        }//Fin del if
    }//Fin del método
}//Fin de la clase