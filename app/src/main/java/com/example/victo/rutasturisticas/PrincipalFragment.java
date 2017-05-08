package com.example.victo.rutasturisticas;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class PrincipalFragment extends Fragment {
     Button boton;
    VideoView videoV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_principal, container, false);

        boton = (Button) view.findViewById(R.id.btnRutasTuristicas);
        videoV= (VideoView)view.findViewById(R.id.videoView);
        String videoPath ="android.resource://com.example.victo.rutasturisticas/" + R.raw.vturri;
        Uri uri= Uri.parse(videoPath);
        videoV.setVideoURI(uri);
        videoV.setMediaController(new MediaController(getActivity()));
        videoV.requestFocus();

        return view;
    }


}
