package com.example.loginsmartwatchsse;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderDetails extends AppCompatActivity {
    ProgressBar pb, pb_trasbordo;
    int counter = 0;
    TextView kit_name;
    //TextView attivita;
    // url_agv è l'url per ascoltare eventi riguardo richiesta assistenza AGV
    String url_agv = "https://sseicosaf.cloud.reply.eu/solve_action";
    // url_events è l'url per ascoltare eventi riguardo completamento task
    String url_events = "https://sseicosaf.cloud.reply.eu/events";
    Button notifiche;
    Button kit;
    int completed = 0;
    int to_do = 0;
    boolean breakLoop = false;
    Button trasbordo;
    Handler mHandler;
    boolean pr_order_completed= true;
    private String TAG = MainActivity.class.getSimpleName();
    ArrayList<Integer> n_order = new ArrayList<>();
    ArrayList<Integer> s_order = new ArrayList<>();
    final ArrayList<String> order_desc = new ArrayList<>();



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                final ArrayList<Integer> n_order = (ArrayList<Integer>) getIntent().getSerializableExtra("ORDER_LIST");
                final ArrayList<Integer> s_order = (ArrayList<Integer>) getIntent().getSerializableExtra("ORDER_STATUS_LIST");
                final Integer operator_id = data.getIntExtra("OPERATOR_ID", 0);
                System.out.println("PRIMA DEL RECREATE");
                recreate();
                System.out.println("DOPO IL RECREATE");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);

        this.mHandler = new Handler();

        kit_name = (TextView) findViewById(R.id.kit_name);
        //attivita = (TextView) findViewById(R.id.attivita);
        notifiche = (Button) findViewById(R.id.notifiche);
        kit = (Button) findViewById(R.id.kit);
        trasbordo = (Button) findViewById(R.id.trasbordo);

        pb = (ProgressBar) findViewById(R.id.pb);
        pb_trasbordo = (ProgressBar) findViewById(R.id.pb_trasbordo);



        final ArrayList<String> tasks = new ArrayList<>();



        Bundle data = getIntent().getExtras();
        final String username = data.getString("USERNAME");
        final Integer operator_id = data.getInt("OPERATOR_ID");

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        String url = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/getOrderbyOper")
                .newBuilder()
                .addQueryParameter("login", String.valueOf(username))
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

                        final String myResponse = response.body().string();
                        JSONArray jsonArray = new JSONArray(myResponse);

                        for (int k = 0; k < jsonArray.length(); k++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(k);
                            final int orderID = jsonObject.getInt("order_id");
                            final int orderstatusID = jsonObject.getInt("order_status_id");
                            final String order_description = jsonObject.getString("order_description");
                            n_order.add(orderID);
                            s_order.add(orderstatusID);
                            order_desc.add(order_description);
                        }
                        getOrder(operator_id, username);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void getOrder(int operator_ID, String Username){
        int operator_id = operator_ID;
        String username = Username;
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
            String order_des = order_desc.get(i);

            //controllare l'order status id, se è = 6 niente, se è 1 oppure 2...allora mostrarlo a schermo
            if ((order_status_id == 1) ||(order_status_id == 2) || (order_status_id == 3)) {
                if (order_status_id == 1){
                    Intent intent = new Intent (OrderDetails.this, NuovoKit.class);
                    intent.putExtra("TASK_DESCR", order_des);
                    startActivity(intent);
                }
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

                                            //m_Runnable.run();

                                            if (task_status_ID == 2) {
                                                completed++;
                                                System.out.println("Questo det_short_id ha status id 2 : " + det_short_ID);
                                            }
                                            else if (task_status_ID == 1) {
                                                to_do++;
                                                breakLoop = true;

                                                System.out.println("Questo det_short_id ha status id 1 : " + det_short_ID);
                                                OrderDetails.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        int totalTasks =0;
                                                        for (int k=0; k<jsonArray.length(); k++){
                                                            totalTasks++;
                                                            //tasks.add(det_short_ID);
                                                        }
                                                        System.out.println("I task totali sono: " + totalTasks);

                                                        if (task_status_ID == 1) {
                                                            if (det_short_ID.equals("Trasbordo")){
                                                                //popup per inizio trasbordo
                                                                Intent intent = new Intent (OrderDetails.this, Trasbordo.class);
                                                                intent.putExtra("TASK_DESCR", task_Descr);
                                                                startActivity(intent);


                                                                //openDialogTrasbordo(task_Descr);

                                                                //quando c'è l'attività di trasbordo il bottone diventa visibile
                                                                trasbordo.setVisibility(View.VISIBLE);
                                                                //attivita.setVisibility(View.INVISIBLE);
                                                                prog_trasbordo();

                                                                System.out.println("qui ci arriva");

                                                                trasbordo.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        System.out.println("444444444");
                                                                        Intent intent2 = new Intent (OrderDetails.this, FineTrasbordo.class);
                                                                        intent2.putExtra("TASK_ID", task_ID);
                                                                        intent2.putExtra("ORDER_ID", order_id);
                                                                        intent2.putExtra("ORDER_LIST", n_order);
                                                                        intent2.putExtra("ORDER_STATUS_LIST", s_order);
                                                                        intent2.putExtra("OPERATOR_ID", operator_id);
                                                                        //intent2.putExtra("TASK_DESCR", task_Descr);
                                                                        //startActivity(intent2);
                                                                        startActivityForResult(intent2, 1);
                                                                        pr_order_completed = true;
                                                                        System.out.println("Il codice è tornato qui");


                                                                    }
                                                                });
                                                            }
                                                            kit_name.setText(task_Descr);
                                                            //attivita.setText("ATTIVITÀ IN CORSO: " + det_short_ID);

                                                        }

                                                        int tasks = totalTasks - 1;
                                                        prog(completed, tasks);

                                                        //la riga seguente è per un'unica progress bar
                                                        //prog(completed, totalTasks);
                                                        notifiche.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent intent = new Intent(OrderDetails.this, Notifiche.class);
                                                                intent.putExtra("OPERATOR_ID", operator_id);
                                                                startActivity(intent);
                                                            }
                                                        });

                                                        kit.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                Intent intent1 = new Intent(OrderDetails.this, OrderList.class);
                                                                intent1.putExtra("USERNAME", username);
                                                                intent1.putExtra("OPERATOR_ID", operator_id);
                                                                intent1.putExtra("CURRENT_ACTIVITY", det_short_ID);
                                                                intent1.putExtra("CURRENT_ORDER", order_id);
                                                                startActivity(intent1);
                                                            }
                                                        });

                                                        //questa parte ascolta gli eventi riguardanti il completamento dei tasks
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
                                                                    if(jsonObject.has("oper_id")) {
                                                                        Integer task_id = jsonObject.getInt("task_id");
                                                                        Integer oper_ID = jsonObject.getInt("oper_id");
                                                                        //String oper_ID = jsonObject.getString("oper_id");
                                                                        String kit_Name = jsonObject.getString("kit_name");
                                                                        String det_short_id = jsonObject.getString("det_short_id");
                                                                        String status = jsonObject.getString("status");

                                                                        //inserendo oper_id!=null, legge solo gli eventi riguardanti task dell'operatore
                                                                        //non task degli AGV
                                                                        if ((oper_ID != null) && (oper_ID == operator_id)) {
                                                                            if (task_ID == task_id) {
                                                                                if (status.equals("OK")) {
                                                                                    //qui fare autorefresh
                                                                                    Intent intent = getIntent();
                                                                                    startActivity(intent);
                                                                                    overridePendingTransition(0, 0);
                                                                                    breakLoop = false;
                                                                                    System.out.println("Il breakloop è falso e l'evento è stato ricevuto");
                                                                                    //mHandler.postDelayed(m_Runnable,5000);
                                                                                }
                                                                            }
                                                                        }
                                                                        /**else if (oper_ID == null) {
                                                                         if (status.equals("OK")) {
                                                                         //qui fare autorefresh
                                                                         Intent intent = getIntent();
                                                                         startActivity(intent);
                                                                         overridePendingTransition(0, 0);
                                                                         breakLoop = false;
                                                                         System.out.println("Il breakloop è falso e l'evento è stato ricevuto");
                                                                         //mHandler.postDelayed(m_Runnable,5000);
                                                                         }

                                                                         }*/
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



                                                        //questa parte ascolta gli eventi riguardanti il completamento dei tasks di AGV
                                                        EventSource eventSource2 = new EventSource(url_events, new EventHandler() {
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
                                                                    if(jsonObject.has("task_id")) {
                                                                    Integer task_ID = jsonObject.getInt("task_id");
                                                                    String kit_Name = jsonObject.getString("kit_name");
                                                                    String det_short_id = jsonObject.getString("det_short_id");
                                                                    String status = jsonObject.getString("status");

                                                                    //inserendo oper_id!=null, legge solo gli eventi riguardanti task dell'operatore
                                                                    //non task degli AGV


                                                                    if (status.equals("OK")) {
                                                                        //qui fare autorefresh
                                                                        Intent intent = getIntent();
                                                                        startActivity(intent);
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
                                                        eventSource2.connect();
                                                        eventSource2.close();





                                                    }
                                                });
                                            }

                                            //j++;

                                            //QUI SONO IN ASCOLTO DEGLI EVENTI PER RICHIESTA ASSISTENZA AGV
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
                                                        if (jsonObject.has("solve_action_id")) {
                                                            Integer solve_action_ID = jsonObject.getInt("solve_action_id");
                                                            Integer oper_ID = jsonObject.getInt("oper_id");
                                                            String cobot_ID = jsonObject.getString("cobot_name");
                                                            Integer severity = jsonObject.getInt("severity");
                                                            String act_Aut = jsonObject.getString("solve_act_aut");
                                                            String act_Desc = jsonObject.getString("solve_action_desc");
                                                            String kit_Name = jsonObject.getString("kit_name");
                                                            Integer task_ID = jsonObject.getInt("task_id");


                                                            if ((cobot_ID != null) && (oper_ID != null)) {
                                                                if (oper_ID == operator_id) {
                                                                    //System.out.println("SONO UGUALI");
                                                                    //openDialog(solve_action_ID, cobot_ID, severity, act_Aut, act_Desc, kit_Name, task_ID, timeRicezione);
                                                                    //finish();

                                                                    final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                                                    vibrator.vibrate(500);

                                                                    Intent intent = new Intent(OrderDetails.this, RequestDialog1.class);
                                                                    intent.putExtra("SOLVE_ACTION_ID", solve_action_ID);
                                                                    intent.putExtra("COBOT_ID", cobot_ID);
                                                                    intent.putExtra("SEVERITY", severity);
                                                                    intent.putExtra("ACT_AUT", act_Aut);
                                                                    intent.putExtra("ACT_DESC", act_Desc);
                                                                    intent.putExtra("KIT_NAME", kit_Name);
                                                                    intent.putExtra("TASK_ID", task_ID);
                                                                    intent.putExtra("TIME_RICEZIONE", timeRicezione);
                                                                    startActivity(intent);
                                                                }
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
            Intent intent_noOrdini = new Intent (OrderDetails.this, OrderList.class);
            finish();
        }
    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run() {
            System.out.println("Il codice è entrato nel run, dove dovrebbe fare refresh");
            if (Looper.myLooper()==null) {
                Looper.prepare();
            }
            Toast.makeText(OrderDetails.this, "in runnable",Toast.LENGTH_SHORT).show();
            OrderDetails.this.mHandler.postDelayed(m_Runnable,20000);
            System.out.println("Fatto refresh");
        }

    };
    public void prog(int Completed, int TotalTasks) {
        pb = (ProgressBar) findViewById(R.id.pb);
        int completed = Completed;
        int totalTasks = TotalTasks;
        //counter++;
        pb.setMax(totalTasks);
        pb.setProgress(completed);
    }
    public void prog_trasbordo(){
        pb_trasbordo = (ProgressBar) findViewById(R.id.pb_trasbordo);
        pb_trasbordo.setMax(1);
        pb_trasbordo.setProgress(1);
    }
    public void openDialogTrasbordo(String task_descr) {
        String taskDescr = task_descr;

        TrasbordoDialog dialog = new TrasbordoDialog(taskDescr);
        //da verificare la vibrazione
        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(500);

        dialog.show(getSupportFragmentManager(), "open dialog");
    }


    /**public void openDialog(Integer solve_action_id, String cobot_id, int severity, String act_aut, String act_desc,
     String kit_name, int task_id, String time_ricezione) {
     Integer solveActionID = solve_action_id;
     String cobotID = cobot_id;
     Integer Severity = severity;
     String actAut = act_aut;
     String actDesc = act_desc;
     String kitName = kit_name;
     Integer taskID = task_id;
     //Date timeRicezione = time_ricezione;
     String timeRicezione = time_ricezione;



     RequestDialog dialog = new RequestDialog(solveActionID, cobotID, Severity, actAut, actDesc, kitName, taskID, timeRicezione);
     dialog.show(getSupportFragmentManager(), "example dialog");
     final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
     vibrator.vibrate(500);

     Intent intent = new Intent (OrderDetails.this, RequestDialog1.class);
     intent.putExtra("SOLVE_ACTION_ID", solve_action_id);
     intent.putExtra("COBOT_ID", cobot_id);
     intent.putExtra("SEVERITY", severity);
     intent.putExtra("ACT_AUT", act_aut);

     }*/
}