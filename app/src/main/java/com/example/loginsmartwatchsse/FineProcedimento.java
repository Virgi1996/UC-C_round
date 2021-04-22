package com.example.loginsmartwatchsse;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class FineProcedimento extends Activity {

    Button ok;
    Button annulla;
    Double delay;
    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;
    String url_events = "https://sseicosaf.cloud.reply.eu/events";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fine_oper);

        ok = (Button) findViewById(R.id.ok);
        annulla = (Button) findViewById(R.id.annulla);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.check,
                        (ViewGroup) findViewById(R.id.relativeLayout1));
                ImageView image = (ImageView) view.findViewById(R.id.checkbox);

                //Looper.prepare();
                Toast toast = new Toast(FineProcedimento.this);
                //Toast toast = new Toast(getApplicationContext());
                toast.setView(view);
                toast.setGravity(Gravity.FILL, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();


                Intent intent = new Intent(FineProcedimento.this, OrderDetails.class);
                finish();
                FineProcedimento.this.finish();
                Procedimento2.p2.finish();
                Procedimento.p.finish();
                RequestDetails.rq.finish();

            }

        });
        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FineProcedimento.this, Procedimento3.class);
                onBackPressed();
                //finish();
            }
        });
    }


}
