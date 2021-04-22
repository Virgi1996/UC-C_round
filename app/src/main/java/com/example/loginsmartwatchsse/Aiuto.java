package com.example.loginsmartwatchsse;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.net.ConnectivityManagerCompat;


import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordClickListener;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import android.widget.Button;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.icu.lang.UCharacter.LineBreak.LINE_FEED;


public class Aiuto extends AppCompatActivity {
    //private Button record, stop;
    CheckBox cont_mancanti;
    CheckBox pn_mancanti;
    TextView reg_incorso, audio_reg;
    ImageView record, stop, back;
    Button cancella, invia;
    //TextView cobot_id;
    //TextView kit_name;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private String attachmentName = "recording";

    int error_id = 0;

    String crlf = "\r\n";
    String twoHyphens = "----";
    String boundary =  "WebKitFormBoundaryJByteO13Rd6xDLw4";

    //MediaRecorder mediaRecorder;
    public static String fileName = "recorded.3gp";
    String file = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + fileName;



    ImageView record_Button;
    ArrayAdapter<String> adapter;
    ArrayList<String> selectedItems = new ArrayList<>();
    String[] array_aiuto = {"Contenitori mancanti", "PN mancanti", "altro"};

    private static final String LOG_TAG = "Record_log";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    //private String fileName = null;
    //private RecordButton recordButton = null;
    //private RecordButton recordButton;
    //private MediaRecorder recorder = null;
    private MediaRecorder recorder;
    private MediaPlayer player = null;
    //private PlayButton playButton = null;

    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aiuto);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //questa Ã¨ l'ultima prova con audiorecorder class
        /**AudioRecorder recorder=new AudioRecorder();
         final ImageButton recordButton=(ImageButton) findViewById(R.id.recordButton);
         recordButton.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction()==motionEvent.ACTION_DOWN)
        recorder.startRecording();
        if(motionEvent.getAction()==motionEvent.ACTION_UP) {
        recorder.stopRecording();
        }
        return false;
        }
        });*/


        //parte vecchia con Button, ora c'Ã¨ ImageView
        /**stop = (Button) findViewById(R.id.stop);
         record = (Button) findViewById(R.id.record);*/
        //questa Ã¨ la parte da tenere
        cont_mancanti = (CheckBox) findViewById(R.id.cont_mancanti);
        pn_mancanti = (CheckBox) findViewById(R.id.pn_mancanti);
        reg_incorso = (TextView) findViewById(R.id.reg_incorso);
        audio_reg = (TextView) findViewById(R.id.audio_reg);
        stop = (ImageView) findViewById(R.id.stop);
        record = (ImageView) findViewById(R.id.record);
        back = (ImageView) findViewById(R.id.back);
        invia = (Button) findViewById(R.id.invia);
        cancella = (Button) findViewById(R.id.cancella);

        stop.setEnabled(false);
        //String outputFile = getExternalCacheDir().getAbsolutePath()+ "/recording.3gp";
        //System.out.println("OUTPUT FILE Ã©: " + outputFile);


        Bundle data = getIntent().getExtras();
        final String cobot_ID = data.getString("COBOT_ID");
        final String kit_Name = data.getString("KIT_NAME");
        final Integer task_id = data.getInt("TASK_ID");

        //String outputFile = getExternalCacheDir().getAbsolutePath()+ "/recording.mp3";
        //String outputFile = getExternalCacheDir().getAbsolutePath()+ "/recording.3gp";
        String outputFile = getExternalCacheDir().getAbsolutePath()+ "/" + task_id + ".mp3";
        System.out.println("OUTPUT FILE Ã©: " + outputFile);

        //cobot_id.setText(cobot_ID);
        //kit_name.setText(kit_Name);

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
        myAudioRecorder.setAudioSamplingRate(16000);

        cont_mancanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cont_mancanti.setChecked(true);
            }
        });

        pn_mancanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pn_mancanti.setChecked(true);
            }
        });



        invia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cont_mancanti.isChecked()){
                    error_id = 10;
                } else if (pn_mancanti.isChecked()){
                    error_id = 11;
                }

                updateStatusEr_call(error_id, task_id);

                //da testare se funziona
                Intent intent = new Intent (Aiuto.this, FineOper.class);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Aiuto.this, RequestDetails.class);
                finish();
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException ise) {
                    // make something ...
                } catch (IOException ioe) {
                    // make something
                }
                record.setEnabled(false);
                stop.setEnabled(true);

                record.setVisibility(View.INVISIBLE);
                reg_incorso.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);
                cont_mancanti.setVisibility(View.INVISIBLE);
                pn_mancanti.setVisibility(View.INVISIBLE);
                //Toast.makeText(getApplicationContext(), "Recording started...", Toast.LENGTH_LONG).show();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
                myAudioRecorder.reset();
                myAudioRecorder.release();
                myAudioRecorder = null;
                record.setEnabled(true);
                stop.setEnabled(false);

                reg_incorso.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.INVISIBLE);
                audio_reg.setVisibility(View.VISIBLE);
                cancella.setVisibility(View.VISIBLE);

                cancella.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //faccio refresh
                        Intent intent= getIntent();
                        startActivity(intent);
                    }
                });

                //Toast.makeText(getApplicationContext(), "Recording finished", Toast.LENGTH_LONG).show();
                System.out.println("OUTPUT FILE ora Ã©: " + outputFile);


                //quando viene premuto 'invia' il file viene inviato al backend
                invia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // invio del file al backend
                        String charset = "UTF-8";
                        String requestURL = "http://icowms.cloud.reply.eu/Details/uploadfile";
                        try {
                            MultipartUtility multipart = new MultipartUtility(requestURL, charset);

                            multipart.addHeaderField("fieldname", "myFile");

                            multipart.addFormField("fieldname", "myFile");

                            multipart.addFilePart("myFile", new File(outputFile));

                            List<String> response = multipart.finish();

                            System.out.println("SERVER REPLIED:");

                            //qui faccio chiamata ad updateStatusEr
                            error_id = 12;
                            updateStatusEr_call(error_id, task_id);

                            for (String line : response) {
                                System.out.println(line);
                            }
                        } catch (IOException ex) {
                            System.err.println(ex);
                        }

                        //se viene inviato l'audio al backend, fare una chiamata con error_id=12
                        error_id = 12;
                        updateStatusEr_call(error_id, task_id);
                    }
                });


            }
        });

        //SendFiles sendFiles = new SendFiles(outputFile);


        // recordView per adesso non la considero, ma considero solo myAudioRecorder

        /**RecordView recordView = (RecordView) findViewById(R.id.record_view);
         final RecordButton recordButton = (RecordButton) findViewById(R.id.record_button);
         recordButton.setRecordView(recordView);
         cobot_id = findViewById(R.id.cobot_id);
         kit_name = findViewById(R.id.kit_name);
         invia = findViewById(R.id.invia);
         //RecordView recordView = (RecordView) findViewById(R.id.record_view);
         //recordButton = (RecordButton) findViewById(R.id.recordButton);
         recordButton.setOnRecordClickListener(new OnRecordClickListener() {
        @Override
        public void onClick(View v) {
        Toast.makeText(Aiuto.this, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT).show();
        Log.d("RecordButton", "RECORD BUTTON CLICKED");
        }
        });
         recordView.setCancelBounds(8);
         recordView.setSlideToCancelText("Slide To Cancel");

         recordView.setOnRecordListener(new OnRecordListener() {
        @Override
        public void onStart() {
        Log.d("RecordView", "onStart");
        Toast.makeText(Aiuto.this, "OnStartRecord", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
        Toast.makeText(Aiuto.this, "onCancel", Toast.LENGTH_SHORT).show();

        Log.d("RecordView", "onCancel");

        }

        @Override
        public void onFinish(long recordTime) {

        String time = getHumanTimeText(recordTime);
        Toast.makeText(Aiuto.this, "onFinishRecord - Recorded Time is: " + time, Toast.LENGTH_SHORT).show();
        Log.d("RecordView", "onFinish");

        Log.d("RecordTime", time);
        }

        @Override
        public void onLessThanSecond() {
        Toast.makeText(Aiuto.this, "OnLessThanSecond", Toast.LENGTH_SHORT).show();
        Log.d("RecordView", "onLessThanSecond");
        }
        });
         recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
        @Override
        public void onAnimationEnd() {
        Log.d("RecordView", "Basket Animation Finished");
        }
        });

         fileName = getExternalCacheDir().getAbsolutePath();
         fileName += "/audiorecordtest.3gp";

         ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);*/

        /**fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
         fileName += "/recorded_audio.3gp";
         recordButton = new RecordButton(this);
         recordButton.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
        startRecording();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
        stopRecording();
        }
        return false;
        }
        });*/
//questo Ã¨ il layout creato da queste righe di codice con i due bottoni
        /**LinearLayout ll = new LinearLayout(this);

         ll.addView(recordButton,
         new LinearLayout.LayoutParams(
         ViewGroup.LayoutParams.WRAP_CONTENT,
         ViewGroup.LayoutParams.WRAP_CONTENT,
         0));*/

        /**playButton = new PlayButton(this);
         ll.addView(playButton,
         new LinearLayout.LayoutParams(
         ViewGroup.LayoutParams.WRAP_CONTENT,
         ViewGroup.LayoutParams.WRAP_CONTENT,
         0));
         setContentView(ll);*/



        //String[] items = {"Contenitori mancanti", "PN mancanti", "altro"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_aiuto, R.id.rowaiuto, items);
        //list_aiuto.setAdapter(adapter);

        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, array_aiuto);






    }


    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }



    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    public void updateStatusEr_call(Integer error_id, Integer task_id){
        Integer error_ID = error_id;
        Integer task_ID = task_id;

        OkHttpClient client1 = new OkHttpClient();
        String url1 = HttpUrl.parse("https://icowms.cloud.reply.eu/Details/updateStatusEr")
                .newBuilder()
                .addQueryParameter("task_id", String.valueOf(task_ID))
                .addQueryParameter("error_type_id", String.valueOf(error_ID))
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
                    System.out.println("Response of updateStatusEr call is SUCCESSFUL.");

                }
            }
        });
    }



}
