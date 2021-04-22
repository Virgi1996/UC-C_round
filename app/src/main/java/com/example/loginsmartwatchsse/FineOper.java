package com.example.loginsmartwatchsse;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FineOper extends Activity {
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

        Bundle data = getIntent().getExtras();
        final Integer task_id = data.getInt("TASK_ID");
        final String time_ricezione = data.getString("TIME_RICEZIONE");


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SSS");
                    //String time_finito = "Tempo finito: " + format.format(calendar.getTime());
                    String time_finito = format.format(calendar.getTime());
                    System.out.println(time_finito);

                    Date time1;
                    Date time2;
                    SimpleDateFormat dates = new SimpleDateFormat("HH:mm:ss:SSS");

                    time1 = dates.parse(time_ricezione);
                    time2 = dates.parse(time_finito);

                    long difference = Math.abs(time1.getTime() - time2.getTime());
                    double delay = difference / (1000);

                    System.out.println("tempo ricezione: " + time_ricezione);
                    System.out.println("tempo finito: " + time_finito);
                    System.out.println("tempo differenza: " + delay + "secondi");

                    LayoutInflater inflater = getLayoutInflater();
                    View view = inflater.inflate(R.layout.check,
                            (ViewGroup) findViewById(R.id.relativeLayout1));
                    ImageView image = (ImageView) view.findViewById(R.id.checkbox);

                    /**LayoutInflater inflater = getLayoutInflater();
                     View view = inflater.inflate(R.layout.checkmark,
                     (ViewGroup) findViewById(R.id.relativeLayout1));
                     ImageView checkbox = (ImageView) view.findViewById(R.id.checkbox);

                     Drawable drawable = checkbox.getDrawable();
                     if (drawable instanceof AnimatedVectorDrawableCompat) {
                     avd = (AnimatedVectorDrawableCompat) drawable;
                     avd.start();
                     } else if (drawable instanceof AnimatedVectorDrawable) {
                     avd2 = (AnimatedVectorDrawable) drawable;
                     avd2.start();
                     }*/



                    //Looper.prepare();
                    Toast toast = new Toast(FineOper.this);
                    //Toast toast = new Toast(getApplicationContext());
                    toast.setView(view);
                    toast.setGravity(Gravity.FILL, 0, 0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();


                    /**Intent intent = new Intent(FineOper.this, OrderDetails.class);
                     finish();*/

                    // provare a inserire un intent qui per aprire Order Details e rifare il processo


                    OkHttpClient client = new OkHttpClient();
                    String url = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/updateStatusOk")
                            .newBuilder()
                            .addQueryParameter("task_id", String.valueOf(task_id))
                            .addQueryParameter("delay", String.valueOf(delay))
                            .build().toString();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    System.out.println("3333333333");
                    client.newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                            e.printStackTrace();
                        }
                        @Override
                        public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {
                            if (response.isSuccessful()) {

                                System.out.println("22222222222");
                                /**LayoutInflater inflater = getLayoutInflater();
                                 View view = inflater.inflate(R.layout.check,
                                 (ViewGroup) findViewById(R.id.relativeLayout1));
                                 ImageView image = (ImageView) view.findViewById(R.id.checkbox);

                                 Looper.prepare();
                                 //Toast toast = new Toast(FineOper.this);
                                 Toast toast = new Toast(getApplicationContext());
                                 toast.setView(view);
                                 toast.setGravity(Gravity.FILL, 0, 0);
                                 toast.setDuration(Toast.LENGTH_SHORT);
                                 toast.show();
                                 finish();
                                 finishActivity(1);*/




                                FineOper.this.finish();
                                RequestDetails.rq.finish();
                                RequestDialog1.rd1.finish();

                                finishActivity(1);


                                System.out.println("444444444");

                            }
                        }
                    });



                    //questa parte ascolta gli eventi riguardanti il completamento dei tasks di AGV
                    EventSource eventSource1 = new EventSource(url_events, new EventHandler() {
                        @Override
                        public void onOpen() {
                            Log.d("OPEN", "Open");
                        }
                        @Override
                        public void onMessage(@NonNull com.example.loginsmartwatchsse.MessageEvent event) {
                            // run on worker thread
                            Log.d("MESSAGE", "Event_task: " + event.getData());
                            try {
                                JSONObject jsonObject = new JSONObject(event.getData());
                                Integer task_ID = jsonObject.getInt("task_id");
                                String kit_Name = jsonObject.getString("kit_name");
                                String det_short_id = jsonObject.getString("det_short_id");
                                String status = jsonObject.getString("status");

                                //inserendo oper_id!=null, legge solo gli eventi riguardanti task dell'operatore
                                //non task degli AGV

                                if (task_ID == task_id) {
                                    if (status.equals("OK")) {
                                        //qui fare autorefresh
                                        Intent intent = new Intent(FineOper.this, OrderDetails.class);
                                        finish();
                                        overridePendingTransition(0, 0);

                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(@Nullable Exception e) {
                            // run on worker thread
                            Log.w("ERROR", e);
                        }
                    });
                    eventSource1.connect();
                    eventSource1.close();




                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        });
        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FineOper.this, RequestDetails.class);
                onBackPressed();
                //finish();
            }
        });
    }


}
