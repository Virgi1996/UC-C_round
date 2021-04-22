package com.example.loginsmartwatchsse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderList extends AppCompatActivity {

    ListView orders_list;
    int order_todo;
    ImageView back;
    String url_agv = "https://sseicosaf.cloud.reply.eu/solve_action";
    String url_events = "https://sseicosaf.cloud.reply.eu/events";
    //TextView order_to_execute;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                Integer order_Completed = data.getIntExtra("ORDER_COMPLETED", 0);
                Integer operator_Id = data.getIntExtra("OPERATOR_ID", 0);
                ArrayList<Integer> order_list = new ArrayList<Integer>();
                order_list = data.getIntegerArrayListExtra("ORDER_LIST");
                Toast.makeText(getApplicationContext(),
                        "Order ID " + order_Completed + " has been completed",Toast.LENGTH_SHORT).show();

                for (int i=0; i<order_list.size(); i++){
                    if (order_Completed.equals(order_list.get(i))){
                        order_todo = order_list.get(i+1);
                        //order_to_execute.setText("The order to execute is: " + order_todo);
                    }
                }
                Intent intent = new Intent(OrderList.this, ThirdActivity.class);
                intent.putExtra("ORDER_ID", order_todo);
                intent.putExtra("ORDER_LIST", order_list);
                intent.putExtra("OPERATOR_ID", operator_Id);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);

        ImageView icon = (ImageView) findViewById(R.id.icon);

        orders_list = findViewById(R.id.orders_list);
        back = (ImageView) findViewById(R.id.back);
        //icon = findViewById(R.id.icon);
        ArrayList<Integer> n_order = new ArrayList<>();
        ArrayList<String> d_order = new ArrayList<>();
        ArrayList<Integer> s_order = new ArrayList<>();

        Bundle userdata = getIntent().getExtras();
        String username = userdata.getString("USERNAME");
        Integer oper_id = userdata.getInt("OPERATOR_ID");
        String current_activity = userdata.getString("CURRENT_ACTIVITY");
        Integer current_order = userdata.getInt("CURRENT_ORDER");

        OkHttpClient client = new OkHttpClient();
        String url = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/getOrderbyOper")
                .newBuilder()
                //.addQueryParameter("login", "mrossi")
                .addQueryParameter("login", String.valueOf(username))
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

                        final String myResponse = response.body().string();
                        JSONArray jsonArray = new JSONArray(myResponse);

                        for (int k = 0; k < jsonArray.length(); k++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(k);
                            final int operatorID = jsonObject.getInt("operator_id");
                            final int orderID = jsonObject.getInt("order_id");
                            final int order_statusID = jsonObject.getInt("order_status_id");
                            final String order_description = jsonObject.getString("order_description");
                            //String N_order = "Ordine n:" + String.valueOf(orderID);

                            n_order.add(orderID);
                            d_order.add(order_description);
                            s_order.add(order_statusID);





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
                                        Integer solve_action_ID = jsonObject.getInt("solve_action_id");
                                        Integer oper_ID = jsonObject.getInt("oper_id");
                                        String cobot_ID = jsonObject.getString("cobot_name");
                                        Integer severity = jsonObject.getInt("severity");
                                        String act_Aut = jsonObject.getString("solve_act_aut");
                                        String act_Desc = jsonObject.getString("solve_action_desc");
                                        String kit_Name = jsonObject.getString("kit_name");
                                        Integer task_ID = jsonObject.getInt("task_id");

                                        if ((cobot_ID!=null)&&(oper_ID!=null)){
                                            if (oper_ID == oper_id) {
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

                                        if (oper_ID == oper_id) {
                                            if (current_activity == det_short_id) {
                                                if (status.equals("OK")) {
                                                    //qui bisogna tornare su OrderDetails.java
                                                    Intent intent2 = new Intent(OrderList.this, OrderDetails.class);
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






                            OrderList.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayAdapter adapter = new ArrayAdapter(OrderList.this, R.layout.order_row, R.id.order_id, d_order) {

                                        @NonNull
                                        @Override
                                        public View getView(int position, View convertView, ViewGroup parent) {

                                            View v = super.getView(position, convertView, parent);
                                            if (v == null) {
                                                LayoutInflater vi;
                                                vi = LayoutInflater.from(getContext());
                                                v = vi.inflate(R.layout.order_row, null);
                                            }
                                            ImageView icon = (ImageView) v.findViewById(R.id.icon);

                                            int order_status_id = s_order.get(position);
                                            //Resources resources = getResources();
                                            v.setBackgroundResource(R.drawable.background_grey);
                                            if (order_status_id == 4) {
                                                icon.setImageResource(R.drawable.green_circle);
                                            } else if ((order_status_id == 2) || (order_status_id == 3)) {
                                                //int current_order= n_order.get(position);
                                                icon.setImageResource(R.drawable.yellow_circle);
                                                //view.setBackgroundResource(R.drawable.ic_done_yellow);
                                                //((ImageView) icon).setColorFilter(0x44E79B0E, PorterDuff.Mode.SRC_ATOP);
                                                //icon.setBackgroundResource(R.drawable.bg_yellow);
                                                orders_list.deferNotifyDataSetChanged();
                                                /**v.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                int orderid = n_order.get(position);
                                                String order_desc = d_order.get(position);
                                                int order_status_id = s_order.get(position);
                                                Intent intent = new Intent(OrderList.this, ThirdActivity.class);
                                                intent.putExtra("ORDER_ID", orderid);
                                                intent.putExtra("ORDER_DESCR", order_desc);
                                                intent.putExtra("ORDER_STATUS", order_status_id);
                                                intent.putExtra("CURRENT_ACTIVITY", current_activity);
                                                //intent.putExtra("CURRENT_ORDER", current_order);
                                                intent.putExtra("OPERATOR_ID", operatorID);
                                                //intent.putExtra("ORDER_LIST", n_order);
                                                startActivityForResult(intent, 1);
                                                }
                                                });*/
                                            } else if (order_status_id == 1) {
                                                icon.setImageResource(R.drawable.grey_circle);
                                            } else if (order_status_id == 5) {
                                                icon.setImageResource(R.drawable.ic_circle_orange);
                                            } else if (order_status_id == 6) {
                                                icon.setImageResource(R.drawable.red_circle);
                                            }

                                            v.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    int orderid = n_order.get(position);
                                                    String order_desc = d_order.get(position);
                                                    int order_status_id = s_order.get(position);
                                                    Intent intent = new Intent(OrderList.this, ThirdActivity.class);
                                                    intent.putExtra("ORDER_ID", orderid);
                                                    intent.putExtra("ORDER_DESCR", order_desc);
                                                    intent.putExtra("ORDER_STATUS", order_status_id);
                                                    intent.putExtra("CURRENT_ACTIVITY", current_activity);
                                                    //intent.putExtra("CURRENT_ORDER", current_order);
                                                    intent.putExtra("OPERATOR_ID", operatorID);
                                                    //intent.putExtra("ORDER_LIST", n_order);
                                                    startActivityForResult(intent, 1);
                                                }
                                            });
                                            return v;
                                        }
                                    };

                                    adapter.notifyDataSetChanged();
                                    orders_list.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();


                                    back.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent1 = new Intent(OrderList.this, OrderDetails.class);
                                            finish();
                                        }
                                    });
                                }
                            });

                            /**back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            Intent intent1 = new Intent(OrderList.this, SecondActivity.class);
                            finish();
                            }
                            });*/
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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


