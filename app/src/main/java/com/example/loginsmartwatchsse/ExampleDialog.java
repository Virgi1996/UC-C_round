package com.example.loginsmartwatchsse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.json.JSONObject;

public class ExampleDialog extends AppCompatDialogFragment {
    private TextView message;
    Button completato;
    Button problema_persiste;
    EditText testo_messaggio;
    Integer agvID;
    Button conferma;

    public ExampleDialog(Integer agvID) {
        this.agvID = agvID;
    }

    //private ExampleDialogListener listener;
    //String url_popup = "https://sseicosaf.cloud.reply.eu/events";

    //AlertDialog alert;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        //return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        message = view.findViewById(R.id.message);
        completato = view.findViewById(R.id.completato);
        problema_persiste = view.findViewById(R.id.problema_persiste);

        builder.setView(view);
        message.setText("L'AGV " + agvID + " ha bisogno di assistenza.");

        completato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss();
                ExampleDialog.this.dismiss();

            }
        });
        problema_persiste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AlertDialog alert;

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater1 = getActivity().getLayoutInflater();

                View view1 = inflater1.inflate(R.layout.testo_problema, null);

                testo_messaggio = view1.findViewById(R.id.testo_messaggio);
                conferma = view1.findViewById(R.id.conferma);



                builder1.setView(view1);

                final AlertDialog alertDialog = builder1.create();

                conferma.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String messaggio = testo_messaggio.getText().toString();
                        System.out.println(messaggio);
                        dismiss();
                        //ExampleDialog.this.dismiss();
                        //builder1.show().dismiss();
                        ExampleDialog.this.dismiss();
                        alertDialog.dismiss();
                    }
                });

                testo_messaggio = view1.findViewById(R.id.testo_messaggio);
                conferma = view1.findViewById(R.id.conferma);
                alertDialog.show();
                //builder1.show();

            }
        });
        message = view.findViewById(R.id.message);
        completato = view.findViewById(R.id.completato);
        problema_persiste = view.findViewById(R.id.problema_persiste);
        //builder.show();
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        Resources res = getResources();

        //Buttons
        Button positive_button = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
        positive_button.setBackground(res.getDrawable(R.drawable.round_button));
        positive_button.setBackgroundResource(R.drawable.round_button);


    }


}
