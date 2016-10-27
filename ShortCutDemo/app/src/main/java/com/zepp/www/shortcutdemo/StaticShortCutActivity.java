package com.zepp.www.shortcutdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class StaticShortCutActivity extends AppCompatActivity {

    private final String TAG = StaticShortCutActivity.class.getCanonicalName();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_short_cut);
    }

    @Override protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    public void onBack(View view) {
        //onBackPressed();
        finish();
    }
}
