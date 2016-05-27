package com.zepp.www.daemonservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.zepp.www.daemonservice.daemon.BackgroundService;
import com.zepp.www.daemonservice.daemon.WhiteService;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startBackgroundService(View view) {
        Intent bgIntent = new Intent(this, BackgroundService.class);
        startService(bgIntent);
    }

    public void startForegroundService(View view) {
        Intent whIntent = new Intent(this, WhiteService.class);
        startService(whIntent);
    }
}
