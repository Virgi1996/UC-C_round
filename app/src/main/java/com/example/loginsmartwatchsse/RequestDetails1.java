package com.example.loginsmartwatchsse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class RequestDetails1 extends AppCompatDialogFragment {
    TextView solve_action_descr;
    TextView severity_level;
    TextView messaggio;
    Button aiuto;
    Button fine;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.request_details, null);

        solve_action_descr = view.findViewById(R.id.solve_action_descr);
        severity_level = view.findViewById(R.id.severity_level);
        messaggio = view.findViewById(R.id.messaggio);
        aiuto = view.findViewById(R.id.aiuto);
        fine = view.findViewById(R.id.fine);

        builder.setView(view);

        fine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (RequestDetails1.this.getActivity(), FineOper.class);
                startActivity(intent);
                RequestDetails1.this.dismiss();

            }
        });

        return builder.create();
    }

}
