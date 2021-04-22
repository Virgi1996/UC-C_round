package com.example.loginsmartwatchsse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Notifiche extends AppCompatActivity {
    Button completato;
    Button problema_persiste;
    ImageView chiudi;
    ListView lista_notifiche;
    //String url = "https://sseicosaf.cloud.reply.eu/solve_action";
    String url_popup = "http://sseicosaf.cloud.reply.eu/solve_action";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifiche);
        chiudi = (ImageView) findViewById(R.id.chiudi);
        lista_notifiche = (ListView) findViewById(R.id.lista_notifiche);

        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> arrayList1 = new ArrayList<>();
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        ArrayList<String> arrayList3 = new ArrayList<>();

        Bundle data = getIntent().getExtras();
        Integer operatorID = data.getInt("OPERATOR_ID");

        OkHttpClient client = new OkHttpClient();
        //String url = "http://icowms.cloud.reply.eu/Details/getOrderbyOper?login=mrossi";

        String url = HttpUrl.parse("http://icowms.cloud.reply.eu/Details/getNotificationList")
                .newBuilder()
                .addQueryParameter("exec_oper_id", String.valueOf(operatorID))
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
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            final String solve_action = jsonObject1.getString("solve_action");
                            final String solve_action_time = jsonObject1.getString("solve_action_time");
                            final Integer task_status_ID = jsonObject1.getInt("task_status_id");
                            final Integer cobot_ID = jsonObject1.getInt("cobot_id");
                            final Integer severity = jsonObject1.getInt("severity");
                            String time = solve_action_time.substring(11, 16);
                            System.out.println(solve_action);
                            System.out.println(time);

                            //arrayList.add(solve_action);
                            //arrayList1.add(time);
                            //arrayList2.add(task_status_ID);


                            Notifiche.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    arrayList.add(solve_action);
                                    if (severity == 1) {
                                        arrayList3.add("URGENZA: ALTA");
                                    } else if (severity == 2) {
                                        arrayList3.add("URGENZA: BASSA");
                                    }
                                    arrayList1.add(time);
                                    arrayList2.add(task_status_ID);

                                    ArrayAdapter adapter = new ArrayAdapter(Notifiche.this, R.layout.notifica, R.id.solve_action, arrayList) {
                                        @NonNull
                                        @Override
                                        public View getView(int position, View convertView, ViewGroup parent) {
                                            View v = super.getView(position, convertView, parent);
                                            if (v == null) {
                                                LayoutInflater vi;
                                                vi = LayoutInflater.from(getContext());
                                                v = vi.inflate(R.layout.order_row, null);
                                            }
                                            System.out.println("11111111");
                                            TextView time = (TextView) v.findViewById(R.id.time);
                                            ImageView icon = (ImageView) v.findViewById(R.id.icon);
                                            //String action_time = String.valueOf(time);


                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                String action_time = arrayList1.get(position);
                                                System.out.println("22222222");
                                                int status_id = arrayList2.get(position);
                                                if (status_id == 1) {
                                                    icon.setImageResource(R.drawable.grey_circle);
                                                } else if (status_id == 2) {
                                                    icon.setImageResource(R.drawable.green_circle);
                                                }
                                                time.setText(action_time);
                                                v.setBackgroundResource(R.drawable.background_grey);
                                            }
                                            System.out.println("3333333");
                                            return v;
                                        }
                                    };
                                    adapter.notifyDataSetChanged();
                                    //lista_notifiche.notify();
                                    lista_notifiche.setAdapter(adapter);
                                    System.out.println("4444444");
                                    adapter.notifyDataSetChanged();

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
                                    adapter.notifyDataSetChanged();
                                }
                            });

                        }
                    } catch (final JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        chiudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Notifiche.this, SecondActivity.class);
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

        RequestDialog dialog = new RequestDialog(solveActionID, cobotID, Severity, actAut, actDesc, kitName, taskID, timeRicezione);
        dialog.show(getSupportFragmentManager(), "example dialog");
    }
}