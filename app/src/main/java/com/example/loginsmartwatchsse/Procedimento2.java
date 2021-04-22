package com.example.loginsmartwatchsse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Procedimento2 extends AppCompatActivity {
    TextView number, step;
    ImageView indietro, avanti, chiudi;
    Button invia;

    public static Activity p2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.procedimento1);

        number = findViewById(R.id.number);
        step = findViewById(R.id.step);
        indietro = findViewById(R.id.indietro);
        avanti = findViewById(R.id.avanti);
        invia = findViewById(R.id.invia);
        chiudi = findViewById(R.id.chiudi);

        p2=this;

        Bundle data = getIntent().getExtras();
        final Integer steps_todo = data.getInt("STEPS_TODO");
        final String step2 = data.getString("STEP2");
        final String step3 = data.getString("STEP3");

        number.setText("2");
        step.setText(step2);
        indietro.setVisibility(View.VISIBLE);
        avanti.setVisibility(View.VISIBLE);
        invia.setVisibility(View.INVISIBLE);

        indietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Procedimento2.this, Procedimento.class);
                finish();
            }
        });

        avanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Procedimento2.this, Procedimento3.class);
                intent.putExtra("STEP3", step3);
                startActivity(intent);

            }
        });
        chiudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Procedimento2.this, RequestDetails.class);
                finish();
            }
        });

    }
}
