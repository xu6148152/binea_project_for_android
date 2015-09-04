package com.example.android.bluetoothchat;

import android.app.Application;
import com.example.android.common.logger.Log;
import com.example.android.utils.ChannelUtil;

/**
 * Created by xubinggui on 9/4/15.
 */
public class BluetoothApplication extends Application {

    private final String TAG = BluetoothApplication.class.getCanonicalName();
    @Override public void onCreate() {
        super.onCreate();
        Log.d(TAG, ChannelUtil.getChannel(this));
    }
}
