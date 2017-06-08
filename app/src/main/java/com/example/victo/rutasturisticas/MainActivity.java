package com.example.victo.rutasturisticas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.victo.rutasturisticas.Modules.VolleyS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

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
        //se carga el fragment principal en el contenedor del mainActivity

        //Inicializamos las variables necesarias para consumir Web API
        this.volley = VolleyS.getInstance(this);
        this.fRequestQueue = volley.getRequestQueue();

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
                    View hView = navigationView.getHeaderView(0);
                    TextView tvUserNameHeader = (TextView)hView.findViewById(R.id.tvUserNameHeader);
                    ImageView ivUserNameHeader = (ImageView) hView.findViewById(R.id.ivUserNameHeader);
                    ivUserNameHeader.setImageResource(R.drawable.logo);// Cargar foto del hosting
                    tvUserNameHeader.setText("Usuario");//sustituir a Sophia por userName
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
                if(!session)
                {
                    fragmentManager.beginTransaction().replace(R.id.contenedor, new LoginFragment()).addToBackStack(null).commit();
                    Toast.makeText(getApplicationContext(), "Debes iniciar sesión", Toast.LENGTH_LONG).show();
                }
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void authenticateUser(View view)
    {
        this.tvEmail = (TextView) findViewById(R.id.tvEmail);
        this.tvPassword = (TextView) findViewById(R.id.tvPassword);

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

                                if(!users.isEmpty())
                                {
                                    JSONObject jsonObject = (JSONObject) users.get(0);
                                    String userPassword = jsonObject.optString("password");
                                    String userName = jsonObject.optString("name");
                                    String password = tvPassword.getText()+"";

                                    if(userPassword.equals(password))
                                    {
                                        View hView = navigationView.getHeaderView(0);
                                        TextView tvUserNameHeader = (TextView)hView.findViewById(R.id.tvUserNameHeader);
                                        try {
                                            Glide.with(getApplicationContext()).load(jsonObject.optString("profilphoto")).into((ImageView) hView.findViewById(R.id.ivUserNameHeader));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        tvUserNameHeader.setText(userName);//sustituir a Sophia por userName
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
}
