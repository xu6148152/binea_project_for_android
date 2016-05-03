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
        startActivityForClass(FirstOpenglActivity.class);
    }

    public void AirHockey1(View view) {
        startActivityForClass(AirHockeyActivity.class);
    }

    public void AirHockeyOrtho(View view) {
        startActivityForClass(AirHockeyOrthoActivity.class);
    }

    public void AirHockey3D(View view) {
        startActivityForClass(AirHockey3DActivity.class);
    }

    public void AirHockeyTextured(View view) {
        startActivityForClass(AirHockeyTextureActivity.class);
    }

    private void startActivityForClass(Class clazz) {
        startActivity(new Intent(this, clazz));
    }
}
