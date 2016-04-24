package com.zepp.www.openglespractice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.zepp.www.openglespractice.R;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void firstOpengl(View view) {
        Intent intent = new Intent(this, FirstOpenglActivity.class);
        startActivity(intent);
    }
}
