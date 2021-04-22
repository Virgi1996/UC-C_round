package com.example.loginsmartwatchsse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RequestDialog1 extends AppCompatActivity {
    Integer solveActionID;
    String cobotID;
    Integer Severity;
    String actAut;
    String actDesc;
    String kitName;
    Integer taskID;
    String timeRicezione;


    TextView  solve_action_descr;
    TextView cobot_id;
    TextView severity_level;
    Button ok;
    String tipo_urgenza;

    /**public RequestDialog1(String cobotID, Integer Severity, String actionDescr) {
     this.cobotID = cobotID;
     this.Severity = Severity;
     this.actionDescr = actionDescr;
     }*/

    public static Activity rd1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_dialog);

        solve_action_descr = (TextView) findViewById(R.id.solve_action_descr);
        cobot_id = (TextView) findViewById(R.id.cobot_id);
        severity_level = (TextView) findViewById(R.id.severity_level);
        ok = (Button) findViewById(R.id.ok);

        rd1 = this;

        Bundle data = getIntent().getExtras();
        final Integer solve_action_ID = data.getInt("SOLVE_ACTION_ID");
        final String cobotID = data.getString("COBOT_ID");
        final Integer Severity = data.getInt("SEVERITY");
        final String act_Aut = data.getString("ACT_AUT");
        final String act_Desc = data.getString("ACT_DESC");
        final String kit_Name = data.getString("KIT_NAME");
        final Integer task_ID = data.getInt("TASK_ID");
        final String timeRicezione = data.getString("TIME_RICEZIONE");

        solve_action_descr.setText(act_Desc);
        cobot_id.setText(cobotID);
        if (Severity == 1) {
            severity_level.setText("URGENZA: ALTA");
        } else {
            severity_level.setText("URGENZA: BASSA");
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(RequestDialog1.this, RequestDetails.class);
                Intent intent = new Intent(RequestDialog1.this, RequestDetails.class);
                intent.putExtra("SOLVE_ACTION_ID", solve_action_ID);
                intent.putExtra("COBOT_ID", cobotID);
                intent.putExtra("SEVERITY", Severity);
                intent.putExtra("ACT_AUT", act_Aut);
                intent.putExtra("ACT_DESC", act_Desc);
                intent.putExtra("KIT_NAME", kit_Name);
                intent.putExtra("TASK_ID", task_ID);
                intent.putExtra("TIME_RICEZIONE", timeRicezione);
                startActivityForResult(intent, 1);
                //startActivity(intent);
                finish();
                System.out.println("finito request dialog");


                //startActivityForResult(intent, 1);


            }
        });

    }
}
