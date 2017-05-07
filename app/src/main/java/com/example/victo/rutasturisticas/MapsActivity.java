package com.example.victo.rutasturisticas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


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
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); //Tipo de mapa
        this.setInitialLocationMap();
        this.setPointsInMap();
    }//Fin del método

    /**
     * Método que establece la posición inicial del mapa
     * */
    public void setInitialLocationMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            this.mMap.setMyLocationEnabled(true); //Botón para ubicar el mapa en nuestra posición
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }
    }//Fin del método setInitialLocationMap

    public void setPointsInMap()
    {
        //Creamos las coordenadas
        LatLng marta  = new LatLng(9.786,-83.691);
        LatLng guayabo  = new LatLng(9.970,-83.690);
        LatLng museo  = new LatLng(9.901,-83.672);

        //Agregamos los marcadores
        this.mMap.addMarker(new MarkerOptions().title("Monumento Nacional Guayabo").snippet("El Monumento Nacional Guayabo es un área protegida de Costa Rica, conocida principalmente porque en ella se encuentra uno de los sitios arqueológicos prehispánicos más antiguos e importantes del país.").position(guayabo));
        this.mMap.addMarker(new MarkerOptions().title("Refugio de Vida Silvestre La Marta").snippet("Refugio de Vida Silvestre La Marta").position(marta));
        this.mMap.addMarker(new MarkerOptions().title("Museo Omar Salazar Obando").snippet("Museo.").position(museo));
    }//Fin del método setPointsInMap
}//Fin de la clase