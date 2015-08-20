package com.example.android.bluetoothchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.android.ble.DeviceScanActivity;
import com.example.android.classical.MainActivity;

/**
 * Created by xubinggui on 8/20/15.
 */
public class ChooseBluetoothActivity extends Activity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bluetooth);

        findViewById(R.id.btn_classical_bluetooth).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(ChooseBluetoothActivity.this, MainActivity.class));
            }
        });

        findViewById(R.id.btn_ble).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(ChooseBluetoothActivity.this, DeviceScanActivity.class));
            }
        });
    }
}
