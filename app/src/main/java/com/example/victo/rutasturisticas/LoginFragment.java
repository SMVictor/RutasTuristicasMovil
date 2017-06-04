package com.example.victo.rutasturisticas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.victo.rutasturisticas.Modules.VolleyS;

public class LoginFragment extends Fragment {

    private TextView label;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private View view;
    private Button btnSeleccionarRutas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

}
