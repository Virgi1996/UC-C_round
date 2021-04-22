package com.example.loginsmartwatchsse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class SecondActivity extends AppCompatActivity {
    //EditText username;
    TextView welcomeuser;
    TextView order_to_execute;
    TextView order_completed;
    Button go_next;
    Button kit_btn;
    Button notifications;
    int order_todo;
    int operator_ID;
    private String TAG = SecondActivity.class.getSimpleName();
    //String url_popup = "https://sseicosaf.cloud.reply.eu/events";
    String url_popup = "https://sseicosaf.cloud.reply.eu/solve_action";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        welcomeuser = findViewById(R.id.welcome_user);
        //order_to_execute= findViewById(R.id.order_to_execute);
        kit_btn = findViewById(R.id.kit_btn);
        notifications = findViewById(R.id.notifications);
        //TextClock clock = (TextClock) findViewById(R.id.clock);
        ArrayList<Integer> n_order = new ArrayList<>();
        ArrayList<Integer> s_order = new ArrayList<>();

        Bundle userdata = getIntent().getExtras();
        String username = userdata.getString("USERNAME");

        //Bundle order = getIntent().getExtras();
        //Integer order_id = order.getInt("ORDER_COMPLETED");

        //OkHttpClient client = new OkHttpClient();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        //String url = "http://icowms.cloud.reply.eu/Details/getOrderbyOper?login=mrossi";

        String url = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/getOrderbyOper")
                .newBuilder()
                //.addQueryParameter("login", "mrossi")
                .addQueryParameter("login", String.valueOf(username))
                //.addEncodedQueryParameter("login", UserName)
                .build().toString();

        /**URL url = null;
        try {
            url = new URL("http://icowms.cloud.reply.eu/Details/getOrderbyOper?login=mrossi");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept-Encoding", "gzip");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


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

                        final String myResponse = response.body().string();
                        JSONArray jsonArray = new JSONArray(myResponse);

                        for (int k = 0; k < jsonArray.length(); k++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(k);
                            final int orderID = jsonObject.getInt("order_id");
                            final int orderstatusID = jsonObject.getInt("order_status_id");
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


                            SecondActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    welcomeuser.setText("Benvenuto, " + operatorName + "\n\n");
                                    //order_to_execute.setText("The order to execute is: " + orderID);
                                }
                            });
                            //final String operator_ID = String.valueOf(operatorID);
                            //final String order_ID = String.valueOf(order_todo);


                            // da non cancellare
                            /**kit_list.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SecondActivity.this, OrderList.class);
                                    intent.putExtra("USERNAME", username);
                                    //intent.putExtra("ORDER_LIST", n_order);
                                    startActivity(intent);
                                }
                            });*/

                            kit_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SecondActivity.this, OrderDetails.class);
                                    intent.putExtra("OPERATOR_ID", operatorID);
                                    intent.putExtra("ORDER_LIST", n_order);
                                    intent.putExtra("ORDER_STATUS_LIST", s_order);
                                    startActivity(intent);
                                }
                            });

                            notifications.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SecondActivity.this, Notifiche.class);
                                    intent.putExtra("OPERATOR_ID", operatorID);
                                    startActivity(intent);
                                }
                            });
                            i++;

                            EventSource eventSource = new EventSource(url_popup, new EventHandler() {
                                @Override
                                public void onOpen() {
                                    Log.d("OPEN", "Open");
                                }

                                @Override
                                public void onMessage(@NonNull com.example.loginsmartwatchsse.MessageEvent event) {
                                    // run on worker thread
                                    Log.d("MESSAGE", "Event: " + event.getData());
                                    try {
                                        //Date timeRicezione = Calendar.getInstance().getTime();
                                        //System.out.println("current time: " + timeRicezione);
                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SSS");
                                        //String timeRicezione = "Tempo ricezione: " + format.format(calendar.getTime());
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

                                        if ((cobot_ID!=null)&&(oper_ID!=null)){
                                            if (oper_ID == operatorID) {
                                                //System.out.println("SONO UGUALI");
                                                openDialog(solve_action_ID, cobot_ID, severity, act_Aut, act_Desc, kit_Name, task_ID, timeRicezione);
                                                //finish();
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
                            eventSource.connect();
                            eventSource.close();


                        }


                        //setAmbientEnabled();


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

        // Enables Always-on


    }

    /**public void openDialog(String cobot_id, int severity, String action_descr) {
        String cobotID = cobot_id;
        Integer Severity = severity;
        String actionDescr = action_descr;

        RequestDialog dialog = new RequestDialog(cobotID, Severity, actionDescr);
        dialog.show(getSupportFragmentManager(), "example dialog");
    }*/

    public void openDialog(Integer solve_action_id, String cobot_id, int severity, String act_aut, String act_desc, String kit_name, int task_id, String time_ricezione) {
        Integer solveActionID = solve_action_id;
        String cobotID = cobot_id;
        Integer Severity = severity;
        String actAut = act_aut;
        String actDesc = act_desc;
        String kitName = kit_name;
        Integer taskID = task_id;
        String timeRicezione = time_ricezione;

        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(500);

        RequestDialog dialog = new RequestDialog(solveActionID, cobotID, Severity, actAut, actDesc, kitName, taskID, timeRicezione);
        dialog.show(getSupportFragmentManager(), "example dialog");
    }
}