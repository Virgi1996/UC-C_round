package com.example.loginsmartwatchsse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.loginsmartwatchsse.R.id.kit_id;
import static com.example.loginsmartwatchsse.R.id.start;

public class ThirdActivity extends AppCompatActivity {
    TextView det_short_id;
    String[] windowArray = {};
    ListView listView;
    ImageView chiudi;
    TextView kit_name;
    TextView kit_id;
    String kit_ID;
    Button next_order;
    Button indietro;
    boolean kit_completed;
    private String TAG = ThirdActivity.class.getSimpleName();
    String url_agv = "https://sseicosaf.cloud.reply.eu/solve_action";
    String url_events = "https://sseicosaf.cloud.reply.eu/events";
    String det_short_ID;
    boolean found = false;
    int focus =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        //order_number = (TextView) findViewById(R.id.order_number);
        //next_order = findViewById(R.id.next_order);
        //indietro = (Button) findViewById(R.id.indietro);
        listView = (ListView) findViewById(R.id.list);
        kit_id = (TextView) findViewById(R.id.kit_id);
        kit_name = (TextView) findViewById(R.id.kit_name);
        chiudi = (ImageView) findViewById(R.id.chiudi);
        final String kit_ID = String.valueOf(kit_id);

        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<Integer> arrayList1 = new ArrayList<>();
        ArrayList<Integer> arrayListColore = new ArrayList<>();


        Bundle data = getIntent().getExtras();
        final Integer operator_id = data.getInt("OPERATOR_ID");
        final String current_activity = data.getString("CURRENT_ACTIVITY");
        //final ArrayList<String> tasks = (ArrayList<String>)getIntent().getSerializableExtra("TASK_LIST");
        //final ArrayList<Integer> n_order = (ArrayList<Integer>) getIntent().getSerializableExtra("ORDER_LIST");
        final Integer order_id = data.getInt("ORDER_ID");
        final Integer order_status_id = data.getInt("ORDER_STATUS");
        final String order_descr = data.getString("ORDER_DESCR");

        OkHttpClient client = new OkHttpClient();

        System.out.println("11111111111");

        String url = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/getTaskListOper")
                .newBuilder()
                .addQueryParameter("oper_id", String.valueOf(operator_id))
                .addQueryParameter("order_id", String.valueOf(order_id))
                .build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        System.out.println("2222222222222222222");

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
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            final String det_short_ID = jsonObject1.getString("det_short_id");
                            final Integer task_status_ID = jsonObject1.getInt("task_status_id");

                            int r = det_short_ID.compareTo(current_activity);
                            arrayListColore.add(r);

                            if (r==0){
                                if (j>3) {
                                    focus = j-2;
                                }
                            }

                            System.out.println(det_short_ID);
                            /**arrayList.add(det_short_ID);
                             arrayList1.add(task_status_ID);*/


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
                                        Integer task_id = jsonObject.getInt("task_id");
                                        Integer oper_ID = jsonObject.getInt("oper_id");
                                        String kit_Name = jsonObject.getString("kit_name");
                                        String det_short_id = jsonObject.getString("det_short_id");
                                        String status = jsonObject.getString("status");

                                        if (oper_ID == operator_id) {
                                            if (current_activity == det_short_id) {
                                                if (status.equals("OK")) {
                                                    //qui bisogna tornare su OrderDetails.java
                                                    Intent intent2 = new Intent(ThirdActivity.this, OrderDetails.class);
                                                    finish();
                                                    overridePendingTransition(0, 0);
                                                    //breakLoop = false;
                                                    System.out.println("Il breakloop è falso e l'evento è stato ricevuto");
                                                    //mHandler.postDelayed(m_Runnable,5000);
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
                            eventSource1.connect();
                            eventSource1.close();




                            ThirdActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    arrayList.add(det_short_ID);
                                    arrayList1.add(task_status_ID);

                                    System.out.println("ThirdActivity.this");
                                    //order_number.setText("Nome Kit: " + order_id);
                                    kit_name.setText(order_descr);


                                    ArrayAdapter adapter = new ArrayAdapter(ThirdActivity.this, R.layout.row, R.id.kit_id, arrayList) {
                                        @NonNull
                                        @Override
                                        public View getView(int position, View convertView, ViewGroup parent) {
                                            System.out.println("Position is: " + position);
                                            View view = super.getView(position, convertView, parent);
                                            if (convertView == null) {
                                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                convertView = inflater.inflate(R.layout.row, parent, false);
                                            }

                                            /**if (view == null) {
                                             LayoutInflater vi;
                                             vi = LayoutInflater.from(getContext());
                                             view = vi.inflate(R.layout.order_row, null);
                                             }*/
                                            ImageView icon = (ImageView) view.findViewById(R.id.icon);
                                            view.setBackgroundResource(R.drawable.background_grey);
                                            listView.setSelection(focus);
                                            //questa riga di codice dovrebbe mettere il focus sul giallo

                                            int status_id = arrayList1.get(position);
                                            int c = arrayListColore.get(position);
                                            //status_id è nel json task_status_id e può essere:
                                            // giallo (1) se è in corso
                                            // verde (2) se è completato
                                            // grigio (3) se è da fare
                                            if (status_id == 2) {
                                                kit_completed = true;
                                                icon.setImageResource(R.drawable.green_circle);
                                                //view.setBackgroundResource(R.drawable.bg_green);
                                                //view.setBackgroundColor(Color.GREEN);
                                                System.out.println("Kit completed: " + kit_completed);
                                            } else if (status_id == 1){
                                                //if (det_short_ID.equals(current_activity)){
                                                if (c == 0) {
                                                    //listView.setItemsCanFocus(true);
                                                    System.out.println("questa" + det_short_ID);
                                                    System.out.println("è uguale a questa" +current_activity);
                                                    kit_completed = false;
                                                    icon.setImageResource(R.drawable.yellow_circle);
                                                } else {
                                                    icon.setImageResource(R.drawable.grey_circle);
                                                }
                                            }
                                            return view;
                                        }
                                    };

                                    adapter.notifyDataSetChanged();
                                    listView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();

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
                                                    if (oper_ID == operator_id) {
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
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //m++;
        chiudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ThirdActivity.this, OrderList.class);
                finish();
            }
        });
    }

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