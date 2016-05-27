package com.zepp.www.daemonservice.daemon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service {

    private final static String TAG = BackgroundService.class.getCanonicalName();

    @Override public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
    }

    @Override public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }
}
