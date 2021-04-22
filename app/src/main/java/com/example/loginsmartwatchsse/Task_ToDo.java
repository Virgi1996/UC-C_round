package com.example.loginsmartwatchsse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Task_ToDo extends Activity {
    Button ok, nok;
    TextView task_descr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_uca);

        ok = findViewById(R.id.ok);
        nok = findViewById(R.id.nok);
        task_descr = findViewById(R.id.tasc_descr);

        Bundle data = getIntent().getExtras();
        final String taskDescr = data.getString("TASK_DESCR");
        final String timeRicezione = data.getString("TIME_RICEZ");
        final Integer task_ID = data.getInt("TASK_ID");
        final Integer order_id = data.getInt("ORDER_ID");
        //final Integer counter = data.getInt("COUNTER");
        //final Integer operator_id = data.getInt("OPERATOR_ID");
        //final ArrayList<Integer> n_order = (ArrayList<Integer>) getIntent().getSerializableExtra("ORDER_LIST");
        //final ArrayList<Integer> s_order = (ArrayList<Integer>) getIntent().getSerializableExtra("ORDER_STATUS_LIST");

        if (taskDescr.equals("UC-A oper Alberi check OK")){
            task_descr.setText("Procedere con controllo alberi");
        } else if (taskDescr.equals("UC-A oper Motor check OK")){
            task_descr.setText("Procedere con controllo motore");
        }





        //se si clicca ok viene fatta una chiamata a updateStatusOk e si ritorna a OrderDetails_UCA.java
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

                    time1 = dates.parse(timeRicezione);
                    time2 = dates.parse(time_finito);


                    long difference = Math.abs(time1.getTime() - time2.getTime());
                    double Delay = difference / (1000);

                    System.out.println("tempo ricezione: " + timeRicezione);
                    System.out.println("tempo finito: " + time_finito);
                    System.out.println("tempo differenza: " + Delay + "secondi");

                    if (taskDescr.equals("UC-A oper Motor check OK")){
                        System.out.println("sto per fare update task status");
                        updateTaskStatus(task_ID, Delay);
                        System.out.println("adesso sto per fare update order status");
                        //updateOrderStatus(order_id);


                        OkHttpClient client = new OkHttpClient();
                        String url = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/updateOrderStatus")
                                .newBuilder()
                                .addQueryParameter("order_id", String.valueOf(order_id))
                                .build().toString();

                        Request request = new Request.Builder()
                                .url(url)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    System.out.println("Response di UPDATEORDERSTATUS è successful");
                                    //qui mettere intent e mettere updateTaskStatus nell'ok.setonclicklistener in prossimoordine.java
                                    //updateTaskStatus(task_ID, Delay);
                                    /**Intent intent = new Intent(Task_ToDo.this, ProssimoOrdine.class);
                                     intent.putExtra("ORDER_LIST", n_order);
                                     intent.putExtra("ORDER_STATUS_LIST", s_order);
                                     intent.putExtra("OPERATOR_ID", operator_id);
                                     intent.putExtra("ORDER_ID", order_id);
                                     intent.putExtra("TASK_ID", task_ID);
                                     intent.putExtra("DELAY", Delay);
                                     startActivity(intent);*/

                                    System.out.println("9999999999");
                                }
                            }
                        });


                        /**Intent intent = new Intent(Task_ToDo.this, ProssimoOrdine.class);
                         intent.putExtra("ORDER_LIST", n_order);
                         intent.putExtra("ORDER_STATUS_LIST", s_order);
                         intent.putExtra("OPERATOR_ID", operator_id);
                         intent.putExtra("ORDER_ID", order_id);
                         startActivity(intent);*/
                    } else {
                        updateTaskStatus(task_ID, Delay);
                        System.out.println("counter non è uguale a due e ritorno nella schermata principale");
                        //Intent intent1 = new Intent (Task_ToDo.this, TaskList_uca.class);
                        finish();
                    }


                    //qui viene fatta una chiamata ad updateStatusOk
                    /**OkHttpClient client = new OkHttpClient();
                     String url = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/updateStatusOk")
                     .newBuilder()
                     .addQueryParameter("task_id", String.valueOf(task_ID))
                     .addQueryParameter("delay", String.valueOf(delay))
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
                    System.out.println("22222222222");
                    if (taskDescr.equals("UC-A oper Motor check OK")){
                    System.out.println("counter è  uguale a 2 e deve fare updateorderstatus");
                    //updateOrderStatus(order_id);
                    Intent intent = new Intent(Task_ToDo.this, ProssimoOrdine.class);
                    intent.putExtra("ORDER_LIST", n_order);
                    intent.putExtra("ORDER_STATUS_LIST", s_order);
                    intent.putExtra("OPERATOR_ID", operator_id);
                    intent.putExtra("ORDER_ID", order_id);
                    startActivity(intent);
                    } else {
                    System.out.println("counter non è uguale a due e ritorno nella schermata principale");
                    Intent intent1 = new Intent (Task_ToDo.this, OrderDetails_UCA.class);
                    finish();
                    }
                    }
                    }
                    });*/


                    //qui devo fare il controllo se la task_descr corrisponde al controllo motore
                    //in questo caso si deve aprire una schermata ProssimoOrdine.java che mi chiede se voglio andare al prossimo ordine
                    //if (counter == 2){
                    /**if (taskDescr.equals("UC-A oper Motor check OK")){
                     System.out.println("counter è  uguale a 2 e deve fare updateorderstatus");
                     updateOrderStatus(order_id);
                     Intent intent = new Intent(Task_ToDo.this, ProssimoOrdine.class);
                     intent.putExtra("ORDER_LIST", n_order);
                     intent.putExtra("ORDER_STATUS_LIST", s_order);
                     intent.putExtra("OPERATOR_ID", operator_id);
                     startActivity(intent);
                     } else {
                     System.out.println("counter non è uguale a due e ritorno nella schermata principale");
                     Intent intent1 = new Intent (Task_ToDo.this, OrderDetails_UCA.class);
                     finish();
                     }*/




                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        nok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (taskDescr.equals("UC-A oper Alberi check OK")) {
                    Intent intent = getIntent();
                    startActivity(intent);
                } else if (taskDescr.equals("UC-A oper Motor check OK")) {
                    updateStatusEr(task_ID);


                }
            }
        });

    }

    public void updateOrderStatus(Integer order_ID) {
        Integer order_id = order_ID;

        OkHttpClient client1 = new OkHttpClient();
        String url1 = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/updateOrderStatus")
                .newBuilder()
                .addQueryParameter("order_id", String.valueOf(order_id))
                .build().toString();

        Request request1 = new Request.Builder()
                .url(url1)
                .build();

        client1.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("888888888888");



                    System.out.println("9999999999");
                }
            }
        });
    }

    public void updateStatusEr(Integer task_ID){
        Integer task_id = task_ID;
        Integer error_type_id = 3;

        OkHttpClient client2 = new OkHttpClient();
        String url2 = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/updateStatusEr")
                .newBuilder()
                .addQueryParameter("task_id", String.valueOf(task_ID))
                .addQueryParameter("error_type_id", String.valueOf(error_type_id))
                .build().toString();

        Request request2 = new Request.Builder()
                .url(url2)
                .build();

        client2.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("update status er è stata fatta");

                    //Intent intent = new Intent (Task_ToDo.this, OrderList_uca.class);
                    finish();

                }
            }
        });
    }

    public void updateTaskStatus(Integer task_ID, Double Delay) {
        Integer task_id = task_ID;
        Double delay = Delay;

        OkHttpClient client1 = new OkHttpClient();
        String url1 = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/updateStatusOk")
                .newBuilder()
                .addQueryParameter("task_id", String.valueOf(task_ID))
                .addQueryParameter("delay", String.valueOf(delay))
                .build().toString();

        Request request1 = new Request.Builder()
                .url(url1)
                .build();

        client1.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("update task status è stata fatta");

                    //Intent intent = new Intent (Task_ToDo.this, OrderList_uca.class);
                    finish();

                }
            }
        });
    }
}
