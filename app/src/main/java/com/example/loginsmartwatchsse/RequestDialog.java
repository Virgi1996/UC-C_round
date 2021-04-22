package com.example.loginsmartwatchsse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Date;

public class RequestDialog extends AppCompatDialogFragment {
    Integer solveActionID;
    String cobotID;
    Integer Severity;
    String actAut;
    String actDesc;
    String kitName;
    Integer taskID;
    //Date timeRicezione;
    String timeRicezione;

    TextView solve_act_aut;
    TextView cobot_id;
    TextView severity_level;
    Button ok;
    String tipo_urgenza;

    public RequestDialog(Integer solveActionID, String cobotID, Integer Severity, String actAut, String actDesc, String kitName, Integer taskID, String timeRicezione) {
        this.solveActionID = solveActionID;
        this.cobotID = cobotID;
        this.Severity = Severity;
        this.actAut = actAut;
        this.actDesc = actDesc;
        this.kitName = kitName;
        this.taskID = taskID;
        this.timeRicezione = timeRicezione;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        //return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.request_dialog, null);
        solve_act_aut = view.findViewById(R.id.solve_action_descr);
        cobot_id = view.findViewById(R.id.cobot_id);
        severity_level = view.findViewById(R.id.severity_level);
        ok = view.findViewById(R.id.ok);

        builder.setView(view);
        solve_act_aut.setText(actAut);
        cobot_id.setText(cobotID);
        if (Severity == 1) {
            severity_level.setText("Urgenza: alta");
        } else {
            severity_level.setText("Urgenza: bassa");
        }

        final AlertDialog alertDialog = builder.create();
        //view.setBackgroundResource(R.drawable.rectangle_btn_grey);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestDialog.this.getActivity(), RequestDetails.class);
                intent.putExtra("SOLVE_ACTION_ID", solveActionID);
                intent.putExtra("COBOT_ID", cobotID);
                intent.putExtra("KIT_NAME", kitName);
                intent.putExtra("SOLVE_ACT_AUT", actAut);
                intent.putExtra("SEVERITY", Severity);
                intent.putExtra("SOLVE_ACT_DESC", actDesc);
                intent.putExtra("TASK_ID", taskID);
                intent.putExtra("TIME_RICEZIONE", timeRicezione);
                startActivity(intent);
                RequestDialog.this.dismiss();
                alertDialog.cancel();
                System.out.println("il Request Dialog è stato chiuso");
                //alertDialog.dismiss();
            }
        });
        return builder.create();
    }

}
