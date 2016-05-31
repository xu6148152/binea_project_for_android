package com.zepp.www.launchmodedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class ActivityB extends AppCompatActivity {

    private final String TAG = ActivityB.class.getCanonicalName();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_b);
        Log.i(Constant.TAG, "onCreate ActivityB");
    }

    @Override protected void onResume() {
        super.onResume();
        Log.i(Constant.TAG, "onResume ActivityB");
    }

    public void startActivityC(View view) {
        Intent intent = new Intent(this, ActivityC.class);
        startActivity(intent);
    }
}
