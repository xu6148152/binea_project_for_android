package com.example.binea.hooktest.binder_hook_service;

import com.example.binea.hooktest.R;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class HookServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hook_service);

        try {
            HookServiceHelper.hookClipboardService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
