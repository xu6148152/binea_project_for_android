package com.zepp.www.launchmodedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //private final String TAG = MainActivity.class.getCanonicalName();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(Constant.TAG, "onCreate MainActivity");
    }

    @Override protected void onResume() {
        super.onResume();
        Log.i(Constant.TAG, "onResume MainActivity");
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(Constant.TAG, "onNewIntent MainActivity");
    }

    public void startActivityB(View view) {
        Intent intent = new Intent(this, ActivityB.class);
        startActivity(intent);
    }

    public void startMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
