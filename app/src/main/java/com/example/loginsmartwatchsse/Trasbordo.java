package com.example.loginsmartwatchsse;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Trasbordo extends AppCompatActivity {
    TextView kit_name;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trasbordo_dialog);

        kit_name = findViewById(R.id.kit_name);
        ok = findViewById(R.id.ok);

        Bundle data = getIntent().getExtras();
        String task_Descr = data.getString("TASK_DESCR");

        kit_name.setText(task_Descr);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Trasbordo.this.finish();
                finish();
            }
        });
    }
}