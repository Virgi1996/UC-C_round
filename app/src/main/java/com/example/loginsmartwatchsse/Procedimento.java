package com.example.loginsmartwatchsse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Procedimento extends AppCompatActivity {
    TextView number, step;
    ImageView indietro, avanti, chiudi;
    Button invia;
    String passaggio = null;

    public static Activity p;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.procedimento1);

        number = findViewById(R.id.number);
        step = findViewById(R.id.step);
        indietro = findViewById(R.id.indietro);
        avanti = findViewById(R.id.avanti);
        invia = findViewById(R.id.invia);
        chiudi = findViewById(R.id.chiudi);
        p = this;

        //ArrayList<String> steps_list = new ArrayList<>();

        Bundle data = getIntent().getExtras();
        final String error_steps = data.getString("STEPS_STRING");

        Character carattereCercato = ';';
        int contatore = 0;
        for(int i=0; i<error_steps.length(); i++) {
            if(error_steps.charAt(i) == carattereCercato) {
                contatore++;
            }
        }
        //alla fine del ciclo for ho il numero di punti e virgola presenti
        //quindi gli steps saranno uguali a contatore + 1
        int steps_todo = contatore + 1;
        //imposto le diverse interfacce per i tre casi
        if (steps_todo == 1){
            number.setText("1");
            indietro.setVisibility(View.INVISIBLE);
            avanti.setVisibility(View.INVISIBLE);
            invia.setVisibility(View.VISIBLE);

            invia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Procedimento.this, FineOper.class);
                    finish();
                }
            });

        }/**else if (steps_todo == 2){
         String[] steps = error_steps.split(";");
         String step1 = steps[0];
         String step2 = steps[1];

         number.setText("1");
         step.setText(step1);
         indietro.setVisibility(View.INVISIBLE);
         avanti.setVisibility(View.VISIBLE);
         invia.setVisibility(View.INVISIBLE);

         avanti.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        Intent intent = new Intent(Procedimento.this, Procedimento2.class);
        intent.putExtra("STEPS_TODO", steps_todo);
        intent.putExtra("STEP2", step2);
        startActivity(intent);
        }
        });

         }*/else if (steps_todo == 3){
            String[] steps = error_steps.split(";");
            String step1 = steps[0];
            String step2 = steps[1];
            String step3 = steps[2];

            number.setText("1");
            step.setText(step1);
            indietro.setVisibility(View.INVISIBLE);
            avanti.setVisibility(View.VISIBLE);
            invia.setVisibility(View.INVISIBLE);

            avanti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Procedimento.this, Procedimento2.class);
                    intent.putExtra("STEPS_TODO", steps_todo);
                    intent.putExtra("STEP2", step2);
                    intent.putExtra("STEP3", step3);
                    startActivity(intent);

                }
            });

        }

        chiudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Procedimento.this, RequestDetails.class);
                finish();
            }
        });
    }
}
