package com.example.victo.rutasturisticas;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

public class PrincipalFragment extends Fragment {
     Button boton;
    VideoView videoV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_principal, container, false);

        boton = (Button) view.findViewById(R.id.btnRutasTuristicas);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarRutas(view);
            }
        });

        videoV= (VideoView)view.findViewById(R.id.videoView);
        String videoPath ="android.resource://com.example.victo.rutasturisticas/" + R.raw.vturri;
        Uri uri= Uri.parse(videoPath);
        videoV.setVideoURI(uri);
        videoV.setMediaController(new MediaController(getActivity()));
        videoV.requestFocus();
        videoV.start();
        return view;
    }

    public void generarRutas(View view)
    {
        SearchParametersFragment fragment = new SearchParametersFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, fragment).addToBackStack(null).commit();
    }//Fin del método
}
