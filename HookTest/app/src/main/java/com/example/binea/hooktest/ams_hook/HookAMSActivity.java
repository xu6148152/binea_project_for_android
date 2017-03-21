package com.example.binea.hooktest.ams_hook;

import com.example.binea.hooktest.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class HookAMSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hook_ams);
    }

    @Override protected void attachBaseContext(Context newBase) {
        HookHelper.hookActivityManager();
        HookHelper.hookPackageManager(newBase);
        super.attachBaseContext(newBase);
    }

    public void onTestAMS(View view) {
        Uri uri = Uri.parse("http://wwww.baidu.com");
        Intent t = new Intent(Intent.ACTION_VIEW);
        t.setData(uri);
        startActivity(t);
    }

    public void onTestPMS(View view) {
        getPackageManager().getInstalledApplications(0);
    }
}
