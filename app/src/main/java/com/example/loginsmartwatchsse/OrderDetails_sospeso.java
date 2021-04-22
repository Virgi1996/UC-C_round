package com.example.loginsmartwatchsse;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class OrderDetails_sospeso extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.orderdetails_sospeso, container, false);

        return rootView;
    }

    /**float x1, x2, y1, y2;

     @Override
     public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.orderdetails_sospeso);
     }

     @Override
     public boolean onTouchEvent(MotionEvent touchEvent) {
     switch (touchEvent.getAction()){
     case MotionEvent.ACTION_DOWN:
     x1 = touchEvent.getX();
     y1 = touchEvent.getY();
     break;
     case MotionEvent.ACTION_UP:
     x2 = touchEvent.getX();
     y2 = touchEvent.getY();
     if (x1<x2){
     Intent i = new Intent (OrderDetails_sospeso.this, RequestDetails.class);
     startActivity(i);
     }
     }
     return false;
     }*/
}
