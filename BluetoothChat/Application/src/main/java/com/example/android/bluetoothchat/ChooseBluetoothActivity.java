package com.example.android.bluetoothchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.android.ble.DeviceScanActivity;
import com.example.android.classical.MainActivity;
import com.example.android.model.GlobalVar;
import com.example.android.utils.RegexUtil;

/**
 * Created by xubinggui on 8/20/15.
 */
public class ChooseBluetoothActivity extends Activity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bluetooth);

        final EditText et_ip = (EditText) findViewById(R.id.et_ip);

        findViewById(R.id.btn_classical_bluetooth).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                GlobalVar.SERVER_ADDRESS = et_ip.getText().toString();
                if (!RegexUtil.isValidIp(GlobalVar.SERVER_ADDRESS)) {
                    Toast.makeText(ChooseBluetoothActivity.this, "please input right ip format",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(ChooseBluetoothActivity.this, MainActivity.class));
            }
        });

        findViewById(R.id.btn_ble).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                GlobalVar.SERVER_ADDRESS = et_ip.getText().toString();
                if (!RegexUtil.isValidIp(GlobalVar.SERVER_ADDRESS)) {
                    Toast.makeText(ChooseBluetoothActivity.this, "please input right ip format",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(ChooseBluetoothActivity.this, DeviceScanActivity.class));
            }
        });
    }
}
