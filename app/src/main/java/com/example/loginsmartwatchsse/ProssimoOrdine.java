package com.example.loginsmartwatchsse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProssimoOrdine extends Activity {
    Button ok_nxt_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prossimo_ordine);

        ok_nxt_order = findViewById(R.id.ok_nxt_order);
        System.out.println("sono entrata in ProssimoOrdine.java");
        Bundle data = getIntent().getExtras();
        final Integer operator_id = data.getInt("OPERATOR_ID");
        final Integer order_id = data.getInt("ORDER_ID");
        final Integer task_ID = data.getInt("TASK_ID");
        //final Double Delay = data.getDouble("DELAY");
        final ArrayList<Integer> n_order = (ArrayList<Integer>) getIntent().getSerializableExtra("ORDER_LIST");
        final ArrayList<Integer> s_order = (ArrayList<Integer>) getIntent().getSerializableExtra("ORDER_STATUS_LIST");

        ProssimoOrdine.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ok_nxt_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finishActivity(1);

                        System.out.println("questo deve farlo solo se clicco ok");
                        //updateTaskStatus(task_ID, Delay);
                        /**Intent intent = new Intent(ProssimoOrdine.this, OrderDetails_UCA.class);
                         intent.putExtra("ORDER_LIST", n_order);
                         intent.putExtra("ORDER_STATUS_LIST", s_order);
                         intent.putExtra("OPERATOR_ID", operator_id);
                         startActivity(intent);
                         setResult(RESULT_OK, intent);
                         finish();*/


                        //updateOrderStatus(order_id);
                        //ProssimoOrdine.this.finish();
                    }
                });
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
                    System.out.println("888888888888");



                    System.out.println("9999999999");
                }
            }
        });
    }
}
