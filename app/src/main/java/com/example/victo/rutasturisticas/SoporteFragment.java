package com.example.victo.rutasturisticas; /**
 * Created by kefca on 06/06/2017.
 */

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.victo.rutasturisticas.Domain.SendMail;

public class SoporteFragment extends Fragment {
    //Declaring EditText
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextSubject;
    private EditText editTextMessage;

    //Send button
    private Button buttonSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View view= inflater.inflate(R.layout.fragment_soporte, container, false);

        //Initializing the views
        editTextName = (EditText) view.findViewById(R.id.editTextName);
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        editTextSubject = (EditText) view.findViewById(R.id.editTextSubject);
        editTextMessage = (EditText) view.findViewById(R.id.editTextMessage);

        buttonSend = (Button) view.findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() { // hago clic en el botón
            @Override
            public void onClick(View v) {
               sendEmail();
            }
        });
        return view;
    }
    private void sendEmail() {
        //Getting content for email
        String email = editTextEmail.getText().toString().trim();
        String subject = editTextSubject.getText().toString().trim();
        String message = editTextName.getText().toString().trim()+" Dice: "+editTextMessage.getText().toString().trim()+"\n Responder a:  "+editTextEmail.getText().toString().trim();;

        //Creating SendMail object
        SendMail sm = new SendMail(getActivity(),  subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

}
