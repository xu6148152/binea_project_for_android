package com.example.binea.hooktest;

import com.example.binea.hooktest.binder_hook_service.HookServiceActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onStartActivity(View view) {
        Intent intent = new Intent(this, HookedActivity.class);
        startActivity(intent);
    }

    public void onStartHookServiceActivity(View view) {
        Intent intent = new Intent(this, HookServiceActivity.class);
        startActivity(intent);
    }
}
