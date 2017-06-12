package com.example.victo.rutasturisticas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.victo.rutasturisticas.Domain.Node;
import com.example.victo.rutasturisticas.Domain.StartPoint;
import com.example.victo.rutasturisticas.Domain.TypeActivity;
import com.example.victo.rutasturisticas.Modules.VolleyS;
import com.example.victo.rutasturisticas.Utilities.MyLinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class SearchParametersFragment extends Fragment
{

    private TextView label;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private View view;
    private Button btnSelectRoutes;
    private Spinner spStartPoint;
    private Spinner spTypesActivities;
    private Spinner spMaxDistance;
    private Spinner spMaxDuration;
    private Spinner spAverageCost;
    private LinkedList<Node> nodes;
    private LinkedList<TypeActivity>typeActivitiesList;
    private StartPoint startPoint;
    private LinkedList<StartPoint>startsPoints;
    private LocationManager mlocManager;
    private Localization local;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //It is initialized the necessary variables to consume the Web API
        this.volley = VolleyS.getInstance(this.getContext());
        this.fRequestQueue = volley.getRequestQueue();

        view = inflater.inflate(R.layout.fragment_search_parameters, container, false);

        //It is obtained the variables supplied through the user interface.
        label = (TextView) view.findViewById(R.id.Tipo);
        btnSelectRoutes = (Button) view.findViewById(R.id.btnSelectRoutes);
        spStartPoint = (Spinner) view.findViewById(R.id.spStartPoint);
        spTypesActivities = (Spinner) view.findViewById(R.id.spTypeActivity);
        spMaxDistance = (Spinner) view.findViewById(R.id.spMaxDistance);
        spMaxDuration = (Spinner) view.findViewById(R.id.spMaxDuration);
        spAverageCost = (Spinner) view.findViewById(R.id.spAverageCost);
        nodes = new LinkedList<Node>();
        typeActivitiesList = new LinkedList<TypeActivity>();
        startPoint = new StartPoint();
        startsPoints = new LinkedList<StartPoint>();
        // LocationListener Manager
        mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //Lacation Listeber Object
        local = new Localization();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> maxDistanceAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.distances, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        maxDistanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spMaxDistance.setAdapter(maxDistanceAdapter);

        ArrayAdapter<CharSequence> maxDurationAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.times, android.R.layout.simple_spinner_item);
        maxDurationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMaxDuration.setAdapter(maxDurationAdapter);

        ArrayAdapter<CharSequence> averageCostAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
            R.array.costs, android.R.layout.simple_spinner_item);
        averageCostAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAverageCost.setAdapter(averageCostAdapter);

        ArrayAdapter<CharSequence> startPointAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.startPoints, android.R.layout.simple_spinner_item);
        startPointAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStartPoint.setAdapter(startPointAdapter);

        Toast.makeText(getActivity(), "Tipo de Actividad: Tipo de actividades que podrá realizar en los sitios incluidos en la ruta", Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity(), "Costo Promedio: La ruta le presentará desde sitios económicos a otros de mayor costo según su elección", Toast.LENGTH_LONG).show();

        this.btnSelectRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectRoutes(view);
            }
        });

        fillActivitiesSpinner();
        fillStartPointsSpinner();

        return view;
    }

    /*
    *The next two methods it are responsible for load the spinners of the types activities
    * and start points respectively.
    */
    public void fillActivitiesSpinner()
    {
        String url = "http://turritour.000webhostapp.com/api/allactivity";
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

                                        //In order to stock the list of TypeActivities for futere use.
                                        TypeActivity activity = new TypeActivity();
                                        activity.setId(jsonObject.optInt("idtypeactivities"));
                                        activity.setDescription(jsonObject.optString("name"));
                                        typeActivitiesList.add(activity);
                                    }//Fin del for
                                }
                                catch(Exception e){}

                                ArrayAdapter spinnerAdapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item ,activities);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spTypesActivities.setAdapter(spinnerAdapter);
                            }//Fin del método onResponse
                        }, //Fin de la clase interna anonima
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                label.setText(error.toString());
                            }//Fin del método que se ejecuta si ocurre un error
                        }//Fin de la clase interna anonima
                );
        addToQueue(request);
    }//Fin del método

    public void fillStartPointsSpinner()
    {
        String url = "http://turritour.000webhostapp.com/api/getstartpoints";
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

                                        //In order to stock the list of TypeActivities for futere use.
                                        StartPoint startPoint = new StartPoint();
                                        startPoint.setLatitude(jsonObject.optDouble("latitude"));
                                        startPoint.setLongitude(jsonObject.optDouble("longitude"));
                                        startPoint.setName(jsonObject.optString("name"));
                                        startsPoints.add(startPoint);
                                    }//Fin del for
                                }
                                catch(Exception e){}

                                ArrayAdapter spinnerAdapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item ,activities);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spStartPoint.setAdapter(spinnerAdapter);
                            }//Fin del método onResponse
                        }, //Fin de la clase interna anonima
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                label.setText(error.toString());
                            }//Fin del método que se ejecuta si ocurre un error
                        }//Fin de la clase interna anonima
                );
        addToQueue(request);
    }//Fin del método
    /*
    *Method responsible for determinate if start point is the current user position or another
    * start point, and it takes the acctions for every case.
    */
    public void selectRoutes(View view)
    {
        Toast.makeText(getActivity(), "Creando rutas...", Toast.LENGTH_LONG).show();

        /*Code to obtain the coordinates of the start point supplied for the user.*/

        // In case that the user wants to use his/her actual position as start point.
        String itemSelected = spStartPoint.getItemAtPosition(spStartPoint.getSelectedItemPosition()).toString();

        if("Tú ubicación".equalsIgnoreCase(itemSelected))
        {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
                Toast.makeText(getActivity(), "Por favor proceda a realizar nuevamente sú solicitud", Toast.LENGTH_LONG).show();
            }
            else
            {
                locationStart();
            }
        }
        //In case that the user wants to user another startpoint
        //we obtain the startpoint selected into the spStartPoint and we recover its coordinates of the database.
        else
        {
            for (StartPoint startPoint: startsPoints)
            {
                if(startPoint.getName().equalsIgnoreCase(itemSelected))
                {
                    this.startPoint = startPoint;
                }
            }
            getNodes();
        }
    }
    /*
   *Method responsible for determinate the current user position coordinates.
   */
    private void locationStart()
    {
        //It is verified if the user grants the permissions necessary to use her/his current position.
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }

        //It is verified if the gps is activated.
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        // It is initialized the LocationListener Manager
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) local);
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }
    //This method obtains the street address from latitude and longitude and stored in the "startPoint" object.
    public void setLocation(Location loc)
    {
        //The street address is obtained from latitude and longitude and stored in the "startPoint" object.
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    startPoint.setName(DirCalle.getAddressLine(0));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /* Here begins the Class Location */
    public class Localization implements LocationListener
    {

        // This method is executed each time the GPS receives new coordinates due to the
        // detection of a change of location.
        @Override
        public void onLocationChanged(Location loc) {

            startPoint.setLatitude(loc.getLatitude());// It gets and stores the current latitude in startPoint object
            startPoint.setLongitude(loc.getLongitude());// It gets and stores the current longitude in startPoint object
            setLocation(loc);
            getNodes();
        }

        @Override
        public void onProviderDisabled(String provider) {
            // This method is executed when the GPS is deactivated
            Toast.makeText(getActivity(), "GPS Desactivado", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            // This method is executed when the GPS is activated
            Toast.makeText(getActivity(), "GPS Activado", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
    /**
     * This method load the list of nodes contained in the database in the LinkedList nodes.
     * For that, it consumes a Web API.
     * */
    public void getNodes()
    {
        String url = "https://turritour.000webhostapp.com/api/getnodes";
        JsonArrayRequest request = new JsonArrayRequest
                (url,
                        new Response.Listener<JSONArray>()
                        {
                            @Override
                            public void onResponse(JSONArray response)
                            {
                                Node node = new Node();
                                try
                                {
                                    //Recorremos el JSONArray
                                    for(int i = 0; i< response.length();i++)
                                    {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        node = new Node();
                                        node.setId(jsonObject.optInt("idnodes"));
                                        node.setLatitude(jsonObject.optDouble("latitude"));
                                        node.setLongitude(jsonObject.optDouble("longitude"));
                                        node.getCategory().setId(jsonObject.optInt("idcategories"));
                                        node.getTypeActivity().setId(jsonObject.optInt("idtypeactivity"));
                                        node.setCost(jsonObject.optString("value"));
                                        node.setName(jsonObject.optString("name"));
                                        node.setInformation(jsonObject.optString("information"));
                                        node.setSlogan(jsonObject.optString("slogan"));
                                        node.setLogo(jsonObject.optString("pathlogo"));
                                        node.setVideoPhotoPath(jsonObject.optString("pathvideoimage"));
                                        node.setFacebookLink(jsonObject.optString("urlfacebook"));
                                        node.setWebSiteLink(jsonObject.optString("urlweb"));
                                        nodes.add(node);
                                    }//Fin del for
                                }
                                catch(Exception e){}
                                filterByDistance();
                            }//Fin del método onResponse
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                label.setText(error.toString());
                            }//Fin del método que se ejecuta si ocurre un error
                        }
                );
        addToQueue(request);
    }
    /**
     * This method filter the nodes that is in the range of distance and duration from the start point..
     * */
    public void filterByDistance()
    {
        LinkedList<Node> nodesDistanceAndDuration = new LinkedList<Node>();

        for (Node node:nodes)
        {
            Location locationA = new Location("punto A");

            locationA.setLatitude(startPoint.getLatitude());
            locationA.setLongitude(startPoint.getLongitude());

            Location locationB = new Location("punto B");

            locationB.setLatitude(node.getLatitude());
            locationB.setLongitude(node.getLongitude());

            float distance = locationA.distanceTo(locationB);
            distance /= 1000;
            float time = (distance/40)*60;

            int itemSelectedDistance = Integer.parseInt(spMaxDistance.getItemAtPosition(spMaxDistance.getSelectedItemPosition()).toString());
            int itemSelectedDuration = Integer.parseInt(spMaxDuration.getItemAtPosition(spMaxDuration.getSelectedItemPosition()).toString());

            if(distance<=itemSelectedDistance && time<=itemSelectedDuration)
            {
                nodesDistanceAndDuration.add(node);
            }

        }
        filterByEuclides(nodesDistanceAndDuration);
    }
    /**
     *This method filter the nodes that have the less distance than the others according the Euclid's method.
     * */
    public void filterByEuclides(LinkedList<Node> nodesList)
    {
        //Se obtienen los datos suministrados por el estudiante
        float distance = Float.parseFloat(spMaxDistance.getItemAtPosition(spMaxDistance.getSelectedItemPosition()).toString());
        float duration = Float.parseFloat(spMaxDuration.getItemAtPosition(spMaxDuration.getSelectedItemPosition()).toString());
        String cost = spAverageCost.getItemAtPosition(spAverageCost.getSelectedItemPosition()).toString();
        String typeActivity = spTypesActivities.getItemAtPosition(spTypesActivities.getSelectedItemPosition()).toString();
        MyLinkedList nodesForRoutesList = new MyLinkedList();


        // Se compara, los datos suministrados con los de cada estudiante
        for (Node node:nodesList)
        {
            Location locationA = new Location("punto A");

            locationA.setLatitude(startPoint.getLatitude());
            locationA.setLongitude(startPoint.getLongitude());

            Location locationB = new Location("punto B");

            locationB.setLatitude(node.getLatitude());
            locationB.setLongitude(node.getLongitude());

            float nodeDistance = locationA.distanceTo(locationB);
            nodeDistance /= 1000;
            float nodeDuration = (distance/40)*60;

            // Se aplica la fórmula de distancia euclidiana para obtener la distancia con el registro actual de la base de datos
            double newNodeDistance = Math.sqrt(Math.pow((distance-nodeDistance), 2)+Math.pow((duration-nodeDuration), 2)+Math.pow(( getTypeActivityValue(typeActivity)-node.getTypeActivity().getId()), 2)+Math.pow((getCostValue(cost)-getCostValue(node.getCost())), 2));
            nodesForRoutesList.orderedInsert(newNodeDistance, node);
        }
        nodesForRoutesList.cut(20);
        orderByDistance(nodesForRoutesList);
    }
    /**
     * This method sorts the nodes in the list according the distance from the start point.
     * */
    public void orderByDistance(MyLinkedList nodesList)
    {
        MyLinkedList nodesOrderByDistance = new MyLinkedList();

        for (int i=0; i<nodesList.size(); i++)
        {
            Location locationA = new Location("punto A");

            locationA.setLatitude(startPoint.getLatitude());
            locationA.setLongitude(startPoint.getLongitude());

            Location locationB = new Location("punto B");

            locationB.setLatitude(nodesList.getNode(i).node.getLatitude());
            locationB.setLongitude(nodesList.getNode(i).node.getLongitude());

            float nodeDistance = locationA.distanceTo(locationB);
            nodesOrderByDistance.orderedInsert(nodeDistance, nodesList.getNode(i).node);
        }
        divNodesIntoFourRoutes(nodesOrderByDistance);
    }
    /**
     * This method divide the list of nodes in 4 subroutes.
     * */
    public void divNodesIntoFourRoutes(MyLinkedList finalNodes)
    {
        MyLinkedList northwestRoute = new MyLinkedList();
        MyLinkedList northeastRoute = new MyLinkedList();
        MyLinkedList southwestRoute = new MyLinkedList();
        MyLinkedList southeastRoute = new MyLinkedList();

        for (int i=0; i<finalNodes.size();i++)
        {
            if(finalNodes.getNode(i).node.getLatitude()<=startPoint.getLatitude() && finalNodes.getNode(i).node.getLongitude()>=startPoint.getLongitude())
            {
                northwestRoute.orderedInsert(finalNodes.getNode(i).distance, finalNodes.getNode(i).node);
            }
            else if(finalNodes.getNode(i).node.getLatitude()<startPoint.getLatitude() && finalNodes.getNode(i).node.getLongitude()<startPoint.getLongitude())
            {
                southwestRoute.orderedInsert(finalNodes.getNode(i).distance, finalNodes.getNode(i).node);
            }
            else if(finalNodes.getNode(i).node.getLatitude()>startPoint.getLatitude() && finalNodes.getNode(i).node.getLongitude()>startPoint.getLongitude())
            {
                northeastRoute.orderedInsert(finalNodes.getNode(i).distance, finalNodes.getNode(i).node);
            }
            else
                {
                    southeastRoute.orderedInsert(finalNodes.getNode(i).distance, finalNodes.getNode(i).node);
                }
        }

        mlocManager.removeUpdates(local);
        SelectRouteFragment fragment = new SelectRouteFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, fragment).addToBackStack(null).commit();
        Bundle data = new Bundle();
        data.putSerializable("northwestRoute", northwestRoute);
        data.putSerializable("northeastRoute", northeastRoute);
        data.putSerializable("southwestRoute", southwestRoute);
        data.putSerializable("southeastRoute", southeastRoute);
        data.putSerializable("startpoint", startPoint);
        fragment.setArguments(data);
    }
    /**
     *The method that executes the requests.
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
    public int getTypeActivityValue(String value)
    {
        int classe = 0;

        for (TypeActivity typeActivity:typeActivitiesList)
        {
            if(typeActivity.getDescription().equalsIgnoreCase(value)){
                classe=typeActivity.getId();
                break;
            }
        }
        return classe;
    }
    public int getCostValue(String value)
    {
        LinkedList<String> diferentValues = new LinkedList<String>();
        int position = 0;

        for (Node node:nodes)
        {
            if(diferentValues.isEmpty())
            {
                diferentValues.add(node.getCost());
            }
            else
            {
                boolean exits = false;
                for (String positionValue:diferentValues)
                {
                    if(positionValue.equalsIgnoreCase(node.getCost()))
                    {
                        exits=true;
                    }
                }
                if(!exits)
                {
                    diferentValues.add(node.getCost());
                }
            }
        }
        for (int i=0; i<diferentValues.size(); i++)
        {
            if(diferentValues.get(i).equalsIgnoreCase(value))
            {
                position=i;
                break;
            }
        }
        return position;
    }
}