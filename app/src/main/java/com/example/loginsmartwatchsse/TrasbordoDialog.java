package com.example.loginsmartwatchsse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class TrasbordoDialog extends AppCompatDialogFragment {
    Button ok;
    String taskDescr;
    TextView kit_name;

    public TrasbordoDialog(String taskDescr) {
        this.taskDescr = taskDescr;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        //return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.trasbordo_dialog, null);
        //message = view.findViewById(R.id.message);
        ok = view.findViewById(R.id.ok);
        kit_name = view.findViewById(R.id.kit_name);

        builder.setView(view);
        kit_name.setText(taskDescr);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss();
                TrasbordoDialog.this.dismiss();

            }
        });
        return builder.create();
    }

}
