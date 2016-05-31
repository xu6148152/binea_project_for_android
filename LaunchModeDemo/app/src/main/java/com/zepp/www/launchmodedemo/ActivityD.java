package com.zepp.www.launchmodedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class ActivityD extends AppCompatActivity {

    private final String TAG = ActivityD.class.getCanonicalName();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_d);
        Log.i(Constant.TAG, "onCreate ActivityD");
    }

    @Override protected void onResume() {
        super.onResume();
        Log.i(Constant.TAG, "onResume ActivityD");
    }

    public void startMainActivity(View view) {
        startService(new Intent(this, NotificationService.class));
        //showNotification();
        //Intent intent = new Intent(this, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //startActivity(intent);
    }

    private void showNotification() {
        //sendBroadcast(new Intent(this, NotificationReceiver.class));
    }
}
