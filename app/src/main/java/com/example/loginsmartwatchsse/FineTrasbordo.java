package com.example.loginsmartwatchsse;

        import android.app.Activity;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Looper;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.Toast;

        import org.jetbrains.annotations.NotNull;

        import java.io.IOException;
        import java.text.ParseException;
        import java.util.ArrayList;
        import java.util.concurrent.TimeUnit;

        import okhttp3.Call;
        import okhttp3.Callback;
        import okhttp3.HttpUrl;
        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.RequestBody;
        import okhttp3.Response;

public class FineTrasbordo extends Activity {
    Button ok;
    Button annulla;
    Double delay = 5.3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fine_trasbordo);

        ok = (Button) findViewById(R.id.ok);
        annulla = (Button) findViewById(R.id.annulla);

        Bundle data = getIntent().getExtras();
        final Integer task_id = data.getInt("TASK_ID");
        final Integer order_id = data.getInt("ORDER_ID");
        final Integer operator_id = data.getInt("OPERATOR_ID");
        final ArrayList<Integer> n_order = (ArrayList<Integer>) getIntent().getSerializableExtra("ORDER_LIST");
        final ArrayList<Integer> s_order = (ArrayList<Integer>) getIntent().getSerializableExtra("ORDER_STATUS_LIST");
        System.out.println("Il codice è entrato in FineTrasbordo.java");


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.check,
                        (ViewGroup)findViewById(R.id.relativeLayout1));
                //Looper.prepare();
                Toast toast = new Toast(FineTrasbordo.this);
                toast.setView(view);
                toast.setGravity(Gravity.FILL, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();

                finishActivity(1);

                for (int i=0; i<n_order.size(); i++){
                    int task = n_order.get(i);
                    if (task == task_id) {
                        s_order.set(i, 4);
                    }
                }

                Intent intent = new Intent(FineTrasbordo.this, OrderDetails.class);
                intent.putExtra("ORDER_LIST", n_order);
                intent.putExtra("ORDER_STATUS_LIST", s_order);
                intent.putExtra("OPERATOR_ID", operator_id);
                setResult(RESULT_OK, intent);
                finish();

                //startActivity(intent);
                FineTrasbordo.this.finish();

                OkHttpClient client = new OkHttpClient();
                client.dispatcher().setMaxRequestsPerHost(50);

                String url = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/updateStatusOk")
                        .newBuilder()
                        .addQueryParameter("task_id", String.valueOf(task_id))
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
                            System.out.println("22222222222");
                            updateOrderStatus(order_id);
                        }
                    }
                });
            }
        });
        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FineTrasbordo.this, OrderDetails.class);
                finish();
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
}


