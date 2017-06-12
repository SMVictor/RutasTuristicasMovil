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
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.example.victo.rutasturisticas.Domain.Node;
import com.example.victo.rutasturisticas.Domain.SearchParameter;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fragmentManager = getSupportFragmentManager();

    private Intent intent;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private TextView tvEmail;
    private TextView tvPassword;
    private Button btnSignin;
    private NavigationView navigationView;
    private TextView label;
    private boolean session;
    private LocationManager mlocManager;
    private Localization local;
    private StartPoint startPoint;
    private LinkedList<Node> nodes;
    private SearchParameter searchParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //It is initialized the necessary variables to consume the Web API
        this.volley = VolleyS.getInstance(this);
        this.fRequestQueue = volley.getRequestQueue();
        // LocationListener Manager
        mlocManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //Lacation Listeber Object
        local = new Localization();
        startPoint = new StartPoint();
        nodes = new LinkedList<Node>();
        searchParameter = new SearchParameter();

        //It is loaded the principal fragment in the container of the main activity.
        fragmentManager.beginTransaction().replace(R.id.contenedor, new PrincipalFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_english) {}
        if (id == R.id.action_spanish) {}
        if (id == R.id.action_german) {}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //It is loaded the corresponding fragment that must handle the request of the user.
        switch (id){

            case R.id.home:
                fragmentManager.beginTransaction().replace(R.id.contenedor, new PrincipalFragment()).addToBackStack(null).commit();
                break;

            case R.id.support:
                fragmentManager.beginTransaction().replace(R.id.contenedor, new SoporteFragment()).addToBackStack(null).commit();
                break;

            case R.id.generateRoutes:
                fragmentManager.beginTransaction().replace(R.id.contenedor, new SearchParametersFragment()).addToBackStack(null).commit();
                break;

            case R.id.login:
                if(!this.session)
                {
                    fragmentManager.beginTransaction().replace(R.id.contenedor, new LoginFragment()).addToBackStack(null).commit();
                }
                else
                    {
                        Toast.makeText(getApplicationContext(), "Ya tienes una sesión iniciada ", Toast.LENGTH_LONG).show();
                    }
                break;

            case R.id.register:
                fragmentManager.beginTransaction().replace(R.id.contenedor, new RegistrarFragment()).addToBackStack(null).commit();
                break;

            case R.id.logout:
                if(this.session)
                {
                    /*
                    *In case that the user logs out, we set the profile photo and username with
                    * the default, and we change the session variable to false.
                    */
                    View hView = navigationView.getHeaderView(0);
                    TextView tvUserNameHeader = (TextView)hView.findViewById(R.id.tvUserNameHeader);
                    ImageView ivUserNameHeader = (ImageView) hView.findViewById(R.id.ivUserNameHeader);
                    ivUserNameHeader.setImageResource(R.drawable.logo);
                    tvUserNameHeader.setText("Usuario");
                    session = false;
                    fragmentManager.beginTransaction().replace(R.id.contenedor, new PrincipalFragment()).addToBackStack(null).commit();
                    Toast.makeText(getApplicationContext(), "Hasta Luego", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Aún no has iniciado sesión ", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.recommended_routes:

                if(!session)// Represent that the session variable is "false", because the user has not logged in.
                {
                    fragmentManager.beginTransaction().replace(R.id.contenedor, new LoginFragment()).addToBackStack(null).commit();
                    Toast.makeText(getApplicationContext(), "Debes iniciar sesión", Toast.LENGTH_LONG).show();
                }
                else
                {
                    createRoutes();
                }
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * This method is responsible for verify that the username and password supplied are congruent
     * with the data stored in the database. Further, it is responsible for set the profil photo and
     * username whit correspondig data to the current user, and change the session variable to "true".
     */
    public void authenticateUser(View view)
    {
        this.tvEmail = (TextView) findViewById(R.id.tvEmail);
        this.tvPassword = (TextView) findViewById(R.id.tvPassword);

        // It is obtained the user with email address supplied
        String url = "http://turritour.000webhostapp.com/api/getuserbyemail/?userEmail="+this.tvEmail.getText();
        JsonArrayRequest request = new JsonArrayRequest
                (url,
                        new Response.Listener<JSONArray>()
                        {
                            @Override
                            public void onResponse(JSONArray response)
                            {
                                LinkedList users = new LinkedList();

                                try
                                {
                                    //Recorremos el JSONArray
                                    for(int i = 0; i< response.length();i++)
                                    {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        users.add(jsonObject);
                                    }//Fin del for
                                }
                                catch(Exception e){}

                                //It is verified that the user address exists.
                                if(!users.isEmpty())
                                {
                                    JSONObject jsonObject = (JSONObject) users.get(0);
                                    String userPassword = jsonObject.optString("password");
                                    String userName = jsonObject.optString("name");
                                    String password = tvPassword.getText()+"";

                                    //It is verified that the user password is ok.
                                    if(userPassword.equals(password))
                                    {
                                        View hView = navigationView.getHeaderView(0);
                                        TextView tvUserNameHeader = (TextView)hView.findViewById(R.id.tvUserNameHeader);

                                        //it is changed the profile photo and username in the navigation bar.
                                        try {
                                            Glide.with(getApplicationContext()).load(jsonObject.optString("profilphoto")).into((ImageView) hView.findViewById(R.id.ivUserNameHeader));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        tvUserNameHeader.setText(userName);

                                        // it is changed the session variable to true
                                        session = true;
                                        fragmentManager.popBackStack();
                                        Toast.makeText(getApplicationContext(), "Bienvenido "+userName, Toast.LENGTH_LONG).show();

                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecto", Toast.LENGTH_LONG).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Correo electrónico no encontrado", Toast.LENGTH_LONG).show();
                                }

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
    public void createRoutes()
    {
        Toast.makeText(this, "Creando rutas...", Toast.LENGTH_LONG).show();

        /*Code to obtain the coordinates of the start point supplied for the user.*/

        // Actual position as start point.

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            Toast.makeText(this, "Por favor proceda a realizar nuevamente sú solicitud", Toast.LENGTH_LONG).show();
        }
        else
        {
            locationStart();
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        // It is initialized the LocationListener Manager
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) local);
    }
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
            Toast.makeText(getApplicationContext(), "GPS Desactivado", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            // This method is executed when the GPS is activated
            Toast.makeText(getApplicationContext(), "GPS Activado", Toast.LENGTH_LONG).show();
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
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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

        String url = "http://turritour.000webhostapp.com/api/getsearchparametersbyemail/?userEmail=sofisofi@outlook.com";
        JsonArrayRequest request = new JsonArrayRequest
                (url,
                        new Response.Listener<JSONArray>()
                        {
                            @Override
                            public void onResponse(JSONArray response)
                            {

                                try
                                {
                                    //Recorremos el JSONArray
                                    for(int i = 0; i< response.length();i++)
                                    {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        searchParameter.setTypeActivity(jsonObject.getInt("type_activity"));
                                        searchParameter.setMax_distance(jsonObject.getInt("max_distance"));
                                        searchParameter.setMax_diration(jsonObject.getInt("max_duration"));
                                        searchParameter.setCost(jsonObject.getInt("average_cost"));
                                        searchParameter.setEmail(jsonObject.getString("user_email"));
                                        searchParameter.setClase(jsonObject.getString("class"));

                                    }//Fin del for
                                }
                                catch(Exception e){}
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

                                    if(distance<=searchParameter.getMax_diration() && time<=searchParameter.getMax_diration())
                                    {
                                        nodesDistanceAndDuration.add(node);
                                    }

                                }
                                filterByEuclides(nodesDistanceAndDuration);
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
    }
    /**
     *This method filter the nodes that have the less distance than the others according the Euclid's method.
     * */
    public void filterByEuclides(LinkedList<Node> nodesList)
    {
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
            float nodeDuration = (nodeDistance/40)*60;

            // Se aplica la fórmula de distancia euclidiana para obtener la distancia con el registro actual de la base de datos
            double newNodeDistance = Math.sqrt(Math.pow((searchParameter.getMax_diration()-nodeDistance), 2)+Math.pow((searchParameter.getMax_diration()-nodeDuration), 2)+Math.pow(( searchParameter.getTypeActivity()-node.getTypeActivity().getId()), 2)+Math.pow((searchParameter.getCost()-getCostValue(node.getCost())), 2));
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
        fragmentManager.beginTransaction().replace(R.id.contenedor, fragment).addToBackStack(null).commit();
        Bundle data = new Bundle();
        data.putSerializable("northwestRoute", northwestRoute);
        data.putSerializable("northeastRoute", northeastRoute);
        data.putSerializable("southwestRoute", southwestRoute);
        data.putSerializable("southeastRoute", southeastRoute);
        data.putSerializable("startpoint", startPoint);
        fragment.setArguments(data);
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
