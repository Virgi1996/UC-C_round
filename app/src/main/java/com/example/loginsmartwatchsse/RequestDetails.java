package com.example.loginsmartwatchsse;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RequestDetails extends AppCompatActivity{
    //GestureDetector.OnGestureListener
    float x1, x2;
    float y1, y2;
    private GestureDetector gestureDetector;
    private static int MIN_DISTANCE = 150;

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;


    TextView cobot_id, info;
    TextView kit_name;
    TextView solve_action_descr;
    TextView severity_level;
    TextView messaggio;
    Button aiuto;
    Button fine;
    Button procedimento;


    public static RequestDetails rq;
    private GestureDetectorCompat gestureDetectorCompat = null;

    /**String url_agv = "https://sseicosaf.cloud.reply.eu/solve_action";

     @Nullable
     @Override
     public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     ViewGroup rootView = (ViewGroup) inflater.inflate(
     R.layout.request_details, container, false);


     cobot_id = (TextView) rootView.findViewById(R.id.cobot_id);
     kit_name = (TextView) rootView.findViewById(R.id.kit_name);
     solve_action_descr = (TextView) rootView.findViewById(R.id.solve_action_descr);
     severity_level = (TextView) rootView.findViewById(R.id.severity_level);
     messaggio = (TextView) rootView.findViewById(R.id.messaggio);
     aiuto = (Button) rootView.findViewById(R.id.aiuto);
     fine = (Button) rootView.findViewById(R.id.fine);
     procedimento = (Button) rootView.findViewById(R.id.procedimento);

     rq = this;





     EventSource eventSource = new EventSource(url_agv, new EventHandler() {
     @Override
     public void onOpen() {
     Log.d("OPEN", "Open");
     }

     @Override
     public void onMessage(@NonNull com.example.loginsmartwatchsse.MessageEvent event) {
     // run on worker thread
     Log.d("MESSAGE", "Event: " + event.getData());
     try {
     Calendar calendar = Calendar.getInstance();
     SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SSS");
     String timeRicezione = format.format(calendar.getTime());
     System.out.println(timeRicezione);


     JSONObject jsonObject = new JSONObject(event.getData());
     Integer solve_action_ID = jsonObject.getInt("solve_action_id");
     Integer oper_ID = jsonObject.getInt("oper_id");
     String cobot_ID = jsonObject.getString("cobot_name");
     Integer severity = jsonObject.getInt("severity");
     String act_Aut = jsonObject.getString("solve_act_aut");
     String act_Desc = jsonObject.getString("solve_action_desc");
     String kit_Name = jsonObject.getString("kit_name");
     Integer task_ID = jsonObject.getInt("task_id");

     cobot_id.setText(cobot_ID);
     kit_name.setText(kit_Name);
     solve_action_descr.setText(act_Aut);

     if (severity == 1) {
     severity_level.setText("URGENZA: ALTA");
     } else {
     severity_level.setText("URGENZA: BASSA");
     }

     messaggio.setText("Messaggio: " + act_Desc);


     procedimento.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
     System.out.println("E' stato cliccato procedimento");


     OkHttpClient client = new OkHttpClient();
     String url = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/getSolveActionDet")
     .newBuilder()
     .addQueryParameter("solve_action_id", String.valueOf(solve_action_ID))
     .build().toString();

     Request request = new Request.Builder()
     .url(url)
     .build();

     client.newCall(request).enqueue(new okhttp3.Callback() {
     @Override
     public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
     e.printStackTrace();
     }

     @Override
     public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {
     if (response.isSuccessful()) {
     final String myResponse = response.body().string();
     try {
     JSONArray jsonArray = new JSONArray(myResponse);
     JSONObject jsonObject = jsonArray.getJSONObject(0);
     final String error_steps = jsonObject.getString("error_steps");

     //controllo se il campo error_steps è vuoto
     if (error_steps.isEmpty()){
     procedimento.setEnabled(false);
     }else{
     procedimento.setEnabled(true);
     Intent intent = new Intent (getActivity(), Procedimento.class);
     intent.putExtra("STEPS_STRING", error_steps);
     startActivity(intent);
     }

     } catch (JSONException e) {
     e.printStackTrace();
     }

     }
     }
     });

     }
     });


     fine.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {

     updateSolveActionOK(solve_action_ID, task_ID, timeRicezione);

     }
     });

     aiuto.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
     Intent intent = new Intent(getActivity(), Aiuto.class);
     intent.putExtra("COBOT_ID", cobot_ID);
     intent.putExtra("KIT_NAME", kit_Name);
     intent.putExtra("TASK_ID", task_ID);
     startActivity(intent);
     }
     });

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
     eventSource.connect();
     eventSource.close();


     return rootView;
     }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_details);

        cobot_id = (TextView) findViewById(R.id.cobot_id);
        kit_name = (TextView) findViewById(R.id.kit_name);
        solve_action_descr = (TextView) findViewById(R.id.solve_action_descr);
        severity_level = (TextView) findViewById(R.id.severity_level);
        messaggio = (TextView) findViewById(R.id.messaggio);
        aiuto = (Button) findViewById(R.id.aiuto);
        fine = (Button) findViewById(R.id.fine);
        procedimento = (Button) findViewById(R.id.procedimento);

        //info = (TextView) findViewById(R.id.info);

        rq = this;


        DetectSwipeGestureListener gestureListener = new DetectSwipeGestureListener();
        gestureListener.setActivity(RequestDetails.this);
        gestureDetectorCompat= new GestureDetectorCompat(RequestDetails.this, gestureListener);


        //this.gestureDetector = new GestureDetector(RequestDetails.this, this);
        //gestureDetector = new GestureDetector(getApplicationContext(), new GestureListener());

        Bundle data = getIntent().getExtras();
        final Integer solve_action_ID = data.getInt("SOLVE_ACTION_ID");
        final String cobot_ID = data.getString("COBOT_ID");
        final Integer severity = data.getInt("SEVERITY");
        final String act_aut = data.getString("ACT_AUT");
        final String act_desc = data.getString("ACT_DESC");
        final String kit_Name = data.getString("KIT_NAME");
        final Integer task_id = data.getInt("TASK_ID");
        final String time_ricezione = data.getString("TIME_RICEZIONE");


        cobot_id.setText(cobot_ID);
        kit_name.setText(kit_Name);
        solve_action_descr.setText(act_aut);

        if (severity == 1) {
            severity_level.setText("URGENZA: ALTA");
        } else {
            severity_level.setText("URGENZA: BASSA");
        }

        messaggio.setText("Messaggio: " + act_desc);


        //chiamata a getSolveActionDet



        procedimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("E' stato cliccato procedimento");


                OkHttpClient client = new OkHttpClient();
                String url = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/getSolveActionDet")
                        .newBuilder()
                        .addQueryParameter("solve_action_id", String.valueOf(solve_action_ID))
                        .build().toString();

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String myResponse = response.body().string();
                            try {
                                JSONArray jsonArray = new JSONArray(myResponse);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                final String error_steps = jsonObject.getString("error_steps");

                                //controllo se il campo error_steps è vuoto
                                if (error_steps.isEmpty()){
                                    procedimento.setEnabled(false);
                                }else{
                                    procedimento.setEnabled(true);
                                    Intent intent = new Intent (RequestDetails.this, Procedimento.class);
                                    intent.putExtra("STEPS_STRING", error_steps);
                                    startActivity(intent);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

            }
        });



        fine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateSolveActionOK(solve_action_ID, task_id, time_ricezione);

            }
        });

        aiuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestDetails.this, Aiuto.class);
                intent.putExtra("COBOT_ID", cobot_ID);
                intent.putExtra("KIT_NAME", kit_Name);
                intent.putExtra("TASK_ID", task_id);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("ontouch detected");
        gestureDetectorCompat.onTouchEvent(event);
        return true;
    }

    public void displayMessage(String message){
        info.setText(message);
    }



    public void updateSolveActionOK (int solve_action_id, int task_ID, String time_Ricezione){

        int solve_action_ID = solve_action_id;
        int task_id = task_ID;
        String time_ricezione = time_Ricezione;

        OkHttpClient client = new OkHttpClient();
        String url = HttpUrl.parse("http://icowms.cloud.reply.eu/Details/updateSolveActionOK")
                .newBuilder()
                .addQueryParameter("solve_action_id", String.valueOf(solve_action_ID))
                .build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("updateSolveActionOK E' STATO FATTO");
                    Intent intent = new Intent (RequestDetails.this, FineOper.class);
                    intent.putExtra("TASK_ID", task_id);
                    intent.putExtra("TIME_RICEZIONE", time_ricezione);
                    startActivity(intent);

                    //finish() c'era ma l'ho tolto per il fragment
                    //finish();

                }
            }
        });


    }


    /**@Override
    public boolean onTouch(View v, MotionEvent event) {
    return gestureDetector.onTouchEvent(event);
    }*/


    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;


        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                System.out.println("entrato in onflying");
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
        System.out.println("swipe right da dx a sx fatto - on swipe right");
        Intent i = new Intent (RequestDetails.this, OrderDetails_sospeso.class);
        startActivity(i);
    }

    public void onSwipeLeft() {
        System.out.println("swipe left da dx a sx fatto - on swipe right");
        Intent i = new Intent (RequestDetails.this, OrderDetails_sospeso.class);
        startActivity(i);
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

}