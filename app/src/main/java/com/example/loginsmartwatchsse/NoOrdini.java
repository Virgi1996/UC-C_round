package com.example.loginsmartwatchsse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NoOrdini extends AppCompatActivity {
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_ordini);

        ok = (Button) findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //qui si apre la schermata con la lista degli ordini
                Intent intent_noOrdini = new Intent(NoOrdini.this, OrderList.class);
                finish();
            }
        });
    }
}
