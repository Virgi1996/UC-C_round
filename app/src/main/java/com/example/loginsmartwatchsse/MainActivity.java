package com.example.loginsmartwatchsse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.support.wearable.activity.WearableActivity;
import android.text.style.TtsSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import androidx.core.net.ConnectivityManagerCompat;

import com.google.android.gms.wearable.MessageEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends WearableActivity {



    EditText username, password;
    Button login;
    String url_events = "https://sseicosaf.cloud.reply.eu/events";
    TextView mTextview, logintext;
    DigitalClock clock;
    boolean pr_order_completed= true;
    private String TAG = MainActivity.class.getSimpleName();
    ArrayList<Integer> n_order = new ArrayList<>();
    ArrayList<Integer> s_order = new ArrayList<>();
    int completed = 0;
    int to_do = 0;
    boolean breakLoop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        clock = findViewById(R.id.clock);
        logintext = findViewById(R.id.logintext);

        ArrayList<Integer> n_order = new ArrayList<>();
        ArrayList<Integer> s_order = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        /**DetectSwipeGestureListener gestureListener = new DetectSwipeGestureListener();
         gestureListener.setActivity(this);
         gestureDetectorCompat= new GestureDetectorCompat(this, gestureListener);*/



        /**Calendar calendar = Calendar.getInstance();
         SimpleDateFormat format = new SimpleDateFormat("HH:mm");
         //String timeRicezione = "Tempo ricezione: " + format.format(calendar.getTime());
         String time = format.format(calendar.getTime());
         System.out.println(time);

         TimeZone.setDefault(TimeZone.getTimeZone("Europe/Rome"));
         SimpleDateFormat date_format = new SimpleDateFormat("h:mm a");
         Date date = new Date();
         String current_date_time = date_format.format(date);
         System.out.println("current date time" + current_date_time);

         DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.ITALY);
         System.out.println(df.format(new Date()));*/

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = username.getText().toString();
                String pass = password.getText().toString();


                if (user.isEmpty()){
                    username.setError(getText(R.string.a11y_no_data));
                    username.requestFocus();
                } else if (((user.equals("mrossi")) && (pass.equals("icosaf123"))) ||
                        ((user.equals("giuviv")) && (pass.equals("pass123")))) {

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(50, TimeUnit.SECONDS)
                            .writeTimeout(50, TimeUnit.SECONDS)
                            .readTimeout(50, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .build();

                    String url = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/getOrderbyOper")
                            .newBuilder()
                            .addQueryParameter("login", String.valueOf(user))
                            .build().toString();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                try {
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SSS");
                                    String timeRicezione = format.format(calendar.getTime());

                                    final String myResponse = response.body().string();
                                    JSONArray jsonArray = new JSONArray(myResponse);

                                    //questo ciclo for serve per ottenere due liste:
                                    //una riferita all'elenco ordini e un'altra all'elenco degli status_id degli ordini
                                    for (int k = 0; k < jsonArray.length(); k++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(k);
                                        final int orderID = jsonObject.getInt("order_id");
                                        final int operatorID = jsonObject.getInt("operator_id");
                                        final int orderstatusID = jsonObject.getInt("order_status_id");
                                        final String order_description = jsonObject.getString("order_description");
                                        n_order.add(orderID);
                                        s_order.add(orderstatusID);

                                    }
                                    //dopo il ciclo for ho due colonne, una con gli id degli ordini da fare e un'altra
                                    //con i rispettivi order_status_id degli ordini
                                    if ((user.equals("mrossi")) && (pass.equals("icosaf123"))) {

                                        int i = 0;
                                        //while (i<jsonArray.length()) {
                                        if (i < jsonArray.length()) {
                                            //for (int i=0; i<jsonArray.length(); i++) {
                                            System.out.println(i);
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            final String operatorName = jsonObject.getString("operator_name");
                                            final int operatorID = jsonObject.getInt("operator_id");
                                            final int orderID = jsonObject.getInt("order_id");

                                            Intent intent = new Intent(MainActivity.this, OrderDetails.class);
                                            intent.putExtra("USERNAME", user);
                                            intent.putExtra("OPERATOR_ID", operatorID);
                                            intent.putExtra("ORDER_LIST", n_order);
                                            intent.putExtra("ORDER_STATUS_LIST", s_order);
                                            startActivity(intent);

                                            i++;
                                        }
                                    } else if ((user.equals("giuviv")) && (pass.equals("pass123"))){



                                        int i = 0;
                                        //while (i<jsonArray.length()) {
                                        if (i < jsonArray.length()) {
                                            //for (int i=0; i<jsonArray.length(); i++) {
                                            System.out.println(i);
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            final String operatorName = jsonObject.getString("operator_name");
                                            final int operatorID = jsonObject.getInt("operator_id");
                                            final int orderID = jsonObject.getInt("order_id");

                                            //con getOrder() method riesco a prendere il primo task del primo ordine che deve essere
                                            //eseguito e lo passerò come parametro ad OrderList_uca.java
                                            getOrder(operatorID, user, s_order, n_order);

                                            /**Intent intent = new Intent(MainActivity.this, OrderList_uca.class);
                                             intent.putExtra("USERNAME", user);
                                             intent.putExtra("OPERATOR_ID", operatorID);
                                             //intent.putExtra("ORDER_LIST", n_order);
                                             //intent.putExtra("ORDER_STATUS_LIST", s_order);
                                             startActivity(intent);*/

                                            i++;
                                        }
                                    }

                                } catch (final JSONException e) {
                                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Looper.prepare();
                                            Toast.makeText(getApplicationContext(),
                                                    "Json parsing error: " + e.getMessage(),
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),
                            "Username o password  non corretti. Riprova.", Toast.LENGTH_SHORT).show();

                    //System.out.println("Incorrect username or password. Try again.");
                }


                /**if ((user.equals("mrossi")) && (pass.equals("i"))) {

                 OkHttpClient client = new OkHttpClient.Builder()
                 .connectTimeout(50, TimeUnit.SECONDS)
                 .writeTimeout(50, TimeUnit.SECONDS)
                 .readTimeout(50, TimeUnit.SECONDS)
                 .retryOnConnectionFailure(true)
                 .build();

                 String url = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/getOrderbyOper")
                 .newBuilder()
                 //.addQueryParameter("login", "mrossi")
                 .addQueryParameter("login", String.valueOf(user))
                 //.addEncodedQueryParameter("login", UserName)
                 .build().toString();
                 Request request = new Request.Builder()
                 .url(url)
                 .build();

                 client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                try {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SSS");
                String timeRicezione = format.format(calendar.getTime());

                final String myResponse = response.body().string();
                JSONArray jsonArray = new JSONArray(myResponse);

                for (int k = 0; k < jsonArray.length(); k++) {
                JSONObject jsonObject = jsonArray.getJSONObject(k);
                final int orderID = jsonObject.getInt("order_id");
                final int orderstatusID = jsonObject.getInt("order_status_id");
                final String order_description = jsonObject.getString("order_description");
                n_order.add(orderID);
                s_order.add(orderstatusID);
                }
                //dopo il ciclo for ho due colonne, una con gli id degli ordini da fare e un'altra
                //con i rispettivi order_status_id degli ordini

                int i = 0;
                //while (i<jsonArray.length()) {
                if (i < jsonArray.length()) {
                //for (int i=0; i<jsonArray.length(); i++) {
                System.out.println(i);
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                final String operatorName = jsonObject.getString("operator_name");
                final int operatorID = jsonObject.getInt("operator_id");
                final int orderID = jsonObject.getInt("order_id");

                Intent intent = new Intent(MainActivity.this, OrderDetails.class);
                intent.putExtra("USERNAME", user);
                intent.putExtra("OPERATOR_ID", operatorID);
                intent.putExtra("ORDER_LIST", n_order);
                intent.putExtra("ORDER_STATUS_LIST", s_order);
                startActivity(intent);

                i++;
                }

                } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                @Override
                public void run() {
                Looper.prepare();
                Toast.makeText(getApplicationContext(),
                "Json parsing error: " + e.getMessage(),
                Toast.LENGTH_LONG)
                .show();
                }
                });
                }
                }
                }
                });
                }else if ((user.equals("giuviv")) && (pass.equals("pass123"))) {
                getOrders_UCA(user);
                } else {
                Toast.makeText(getApplicationContext(),
                "Username o password  non corretti. Riprova.", Toast.LENGTH_SHORT).show();

                //System.out.println("Incorrect username or password. Try again.");
                }*/
            }
        });
        // Enables Always-on
        setAmbientEnabled();


    }




    private void getOrder(int operator_ID, String Username, ArrayList<Integer> S_order, ArrayList<Integer> N_order){
        int operator_id = operator_ID;
        String username = Username;
        ArrayList<Integer> s_order = new ArrayList<>(S_order);
        ArrayList<Integer> n_order = new ArrayList<>(N_order);

        System.out.println("L'S_ORDER CHE VIENE DALLA CLASSE è : " + s_order);


        int i =0;
        pr_order_completed = true;
        //if ((n_order.size())==(s_order.size())) {
        //for (int i=0; i<n_order.size(); i++) {
        while ((i<n_order.size())&&(pr_order_completed)) {
            System.out.println("L'INDICATORE i E' UGUALE A " + i);
            System.out.println("N ORDER è UGUALE A: " + n_order);
            System.out.println("S ORDER è UGUALE A: " + s_order);
            pr_order_completed = false;
            int order_status_id = s_order.get(i);

            //controllare l'order status id, se è = 6 niente, se è 1 oppure 2...allora mostrarlo a schermo
            if ((order_status_id == 1) ||(order_status_id == 2) || (order_status_id == 3)) {
                int order_id = n_order.get(i);

                //adesso inserire questo order_id nella chiamata per vedere i task id di questo order
                //OkHttpClient client = new OkHttpClient();
                OkHttpClient client1 = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(50, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build();
                String url1 = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/getTaskListOper")
                        .newBuilder()
                        .addQueryParameter("oper_id", String.valueOf(operator_id))
                        .addQueryParameter("order_id", String.valueOf(order_id))
                        .build().toString();

                Request request1 = new Request.Builder()
                        .url(url1)
                        .build();

                client1.newCall(request1).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                final String myResponse = response.body().string();
                                JSONArray jsonArray = new JSONArray(myResponse);
                                completed = 0;
                                System.out.println("QUESTO é L'ORDINE N: " + order_id);
                                //int j=0;
                                //while (j < jsonArray.length()){


                                for (int j = 0; j <= jsonArray.length(); j++) {
                                    while ((!breakLoop)&&(j <= jsonArray.length())) {
                                        try {
                                            if (pr_order_completed == true){
                                                j=0;
                                            }

                                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                            final Integer task_ID = jsonObject1.getInt("task_id");
                                            final String det_short_ID = jsonObject1.getString("det_short_id");
                                            final String task_Descr = jsonObject1.getString("task_descr");
                                            final Integer task_status_ID = jsonObject1.getInt("task_status_id");

                                            if (task_status_ID == 2) {
                                                completed++;
                                                System.out.println("Questo det_short_id ha status id 2 : " + det_short_ID);
                                            }
                                            else if (task_status_ID == 1) {
                                                to_do++;
                                                breakLoop = true;
                                                System.out.println("Questo det_short_id ha status id 1 : " + det_short_ID);
                                                /**Intent intent = new Intent(MainActivity.this, OrderList_uca.class);
                                                 intent.putExtra("USERNAME", username);
                                                 intent.putExtra("OPERATOR_ID", operator_id);
                                                 intent.putExtra("CURRENT_ACTIVITY", det_short_ID);
                                                 intent.putExtra("CURRENT_ORDER", order_id);
                                                 //intent.putExtra("ORDER_LIST", n_order);
                                                 //intent.putExtra("ORDER_STATUS_LIST", s_order);
                                                 startActivity(intent);*/
                                            }

                                            //j++;


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        j++;

                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } else if (order_status_id == 4) {
                pr_order_completed = true;
                i++;
            }
        }
        if (i>n_order.size()){
            Toast.makeText(getApplicationContext(),
                    "Non ci sono altri ordini.", Toast.LENGTH_SHORT).show();
            //qui si apre la schermata con la lista degli ordini
            Intent intent_noOrdini = new Intent (MainActivity.this, OrderList.class);
            finish();
        }
    }

    /**public void getOrders_UCA(String username){
     String user = username;
     OkHttpClient client_UCA = new OkHttpClient.Builder()
     .connectTimeout(50, TimeUnit.SECONDS)
     .writeTimeout(50, TimeUnit.SECONDS)
     .readTimeout(50, TimeUnit.SECONDS)
     .retryOnConnectionFailure(true)
     .build();

     String url = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/getOrderbyOper")
     .newBuilder()
     //.addQueryParameter("login", "mrossi")
     .addQueryParameter("login", String.valueOf(user))
     //.addEncodedQueryParameter("login", UserName)
     .build().toString();
     Request request_UCA = new Request.Builder()
     .url(url)
     .build();

     client_UCA.newCall(request_UCA).enqueue(new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {
    e.printStackTrace();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
    if (response.isSuccessful()) {
    try {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SSS");
    String timeRicezione = format.format(calendar.getTime());

    final String myResponse = response.body().string();
    JSONArray jsonArray = new JSONArray(myResponse);

    for (int k = 0; k < jsonArray.length(); k++) {
    JSONObject jsonObject = jsonArray.getJSONObject(k);
    final int orderID = jsonObject.getInt("order_id");
    final int orderstatusID = jsonObject.getInt("order_status_id");
    final String order_description = jsonObject.getString("order_description");
    //n_order.add(orderID);
    //s_order.add(orderstatusID);
    }
    //dopo il ciclo for ho due colonne, una con gli id degli ordini da fare e un'altra
    //con i rispettivi order_status_id degli ordini

    int i = 0;
    //while (i<jsonArray.length()) {
    if (i < jsonArray.length()) {
    //for (int i=0; i<jsonArray.length(); i++) {
    System.out.println(i);
    JSONObject jsonObject = jsonArray.getJSONObject(i);
    final String operatorName = jsonObject.getString("operator_name");
    final int operatorID = jsonObject.getInt("operator_id");
    final int orderID = jsonObject.getInt("order_id");

    Intent intent = new Intent(MainActivity.this, OrderDetails_UCA.class);
    intent.putExtra("USERNAME", user);
    intent.putExtra("OPERATOR_ID", operatorID);
    //intent.putExtra("ORDER_LIST", n_order);
    //intent.putExtra("ORDER_STATUS_LIST", s_order);
    startActivity(intent);

    i++;
    }

    } catch (final JSONException e) {
    Log.e(TAG, "Json parsing error: " + e.getMessage());
    runOnUiThread(new Runnable() {
    @Override
    public void run() {
    Looper.prepare();
    Toast.makeText(getApplicationContext(),
    "Json parsing error: " + e.getMessage(),
    Toast.LENGTH_LONG)
    .show();
    }
    });
    }
    }
    }
    });
    }*/
}
