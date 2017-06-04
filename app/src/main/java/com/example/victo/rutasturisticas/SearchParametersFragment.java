package com.example.victo.rutasturisticas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.example.victo.rutasturisticas.Modules.VolleyS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

public class SearchParametersFragment extends Fragment {

    private TextView label;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private View view;
    private Button btnSelectRoutes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Inicializamos las variables necesarias para consumir Web API
        this.volley = VolleyS.getInstance(this.getContext());
        this.fRequestQueue = volley.getRequestQueue();

        view = inflater.inflate(R.layout.fragment_search_parameters, container, false);

        Spinner distanciaMaximaSpinner = (Spinner) view.findViewById(R.id.spMaxDistance);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterDistanciaMaxima = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.distances, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterDistanciaMaxima.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        distanciaMaximaSpinner.setAdapter(adapterDistanciaMaxima);

        Spinner duracionMaximaSpinner = (Spinner) view.findViewById(R.id.spMaxDuration);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterDuracionMaxima = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.times, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterDuracionMaxima.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        duracionMaximaSpinner.setAdapter(adapterDuracionMaxima);

        Spinner costoPromedioSpinner = (Spinner) view.findViewById(R.id.spAverageCost);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterCostoPromedio = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.costs, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterCostoPromedio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        costoPromedioSpinner.setAdapter(adapterCostoPromedio);

        Toast.makeText(getActivity(), "Tipo de Actividad: Tipo de actividades que podrá realizar en los sitios incluidos en la ruta", Toast.LENGTH_LONG).show();

        Toast.makeText(getActivity(), "Costo Promedio: La ruta le presentará desde sitios económicos a otros de mayor costo según su elección", Toast.LENGTH_LONG).show();

        this.label = (TextView) view.findViewById(R.id.textView8);
        this.btnSelectRoutes = (Button) view.findViewById(R.id.btnSelectRoutes);

        this.btnSelectRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seleccionarRuta(view);
            }
        });

        fillActivitySpinner();

        return view;
    }

    public void seleccionarRuta(View view)
    {
        SelectRouteFragment fragment = new SelectRouteFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, fragment).addToBackStack(null).commit();

        //Bundle data = new Bundle();
        //data.putSerializable("Pedido", pedidos.get(posicion));
        //fragment.setArguments(data);

    }

    /**
     * Método que llena el spinner de activity
     * */
    public void fillActivitySpinner()
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
                                    }//Fin del for
                                }
                                catch(Exception e){}

                                Spinner acvitiesSpinner = (Spinner) view.findViewById(R.id.spTypeActivity);
                                ArrayAdapter spinnerAdapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item ,activities);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                acvitiesSpinner.setAdapter(spinnerAdapter);
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
}
