package com.zepp.www.launchmodedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class ActivityC extends AppCompatActivity {
    private final String TAG = ActivityC.class.getCanonicalName();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_c);
        Log.i(Constant.TAG, "onCreate ActivityC");
    }

    @Override protected void onResume() {
        super.onResume();
        Log.i(Constant.TAG, "onResume ActivityC");
    }

    public void startActivityD(View view) {
        Intent intent = new Intent(this, ActivityD.class);
        startActivity(intent);
    }
}
