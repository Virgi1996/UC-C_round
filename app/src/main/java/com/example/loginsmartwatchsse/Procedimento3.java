package com.example.loginsmartwatchsse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Procedimento3 extends AppCompatActivity {
    TextView number, step;
    ImageView indietro, avanti, chiudi;
    Button invia;

    public static Activity p3;

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

        p3=this;

        Bundle data = getIntent().getExtras();
        final String step3 = data.getString("STEP3");

        number.setText("3");
        step.setText(step3);
        indietro.setVisibility(View.VISIBLE);
        avanti.setVisibility(View.INVISIBLE);
        invia.setVisibility(View.VISIBLE);

        indietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Procedimento3.this, Procedimento2.class);
                finish();
            }
        });

        invia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Procedimento3.this, FineProcedimento.class);
                startActivity(intent);
                finish();
            }
        });
        chiudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Procedimento3.this, RequestDetails.class);
                finish();
            }
        });
    }
}
