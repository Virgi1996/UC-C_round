package com.example.loginsmartwatchsse;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TestoProblema extends AppCompatActivity {
EditText testo_messaggio;
Button conferma;
String testo_mess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testo_problema);

        testo_messaggio = (EditText) findViewById(R.id.testo_messaggio);
        //conferma = (Button) findViewById(R.id.conferma);

        //testo_mess = String.valueOf(testo_messaggio);



        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(testo_messaggio.getText())) {
                    Toast.makeText(getApplicationContext(),
                            "Inserisci il testo", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent1 = new Intent (TestoProblema.this, SecondActivity.class);

                    //intent1.putExtra("TESTO_MESSAGGIO", testo_mess);
                    //startActivity(intent1);
                    //intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent1);
                    //finish();
                }
            }
        });
    }
}
