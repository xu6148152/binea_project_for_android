package com.zepp.www.daemonservice.daemon;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class GrayService extends Service {
    private final static String TAG = GrayService.class.getSimpleName();
    /**
     * 定时唤醒的时间间隔，5分钟
     */
    private final static int ALARM_INTERVAL = 5 * 60 * 1000;
    private final static int WAKE_REQUEST_CODE = 6666;

    private final static int GRAY_SERVICE_ID = -1001;

    @Override
    public void onCreate() {
        Log.i(TAG, "GrayService->onCreate");
        super.onCreate();
    }

    @Override public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "GrayService->onStartCommand");
        if(Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());
        }else {
            Intent innerIntent = new Intent(this, GrayService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent();
        //alarmIntent.setAction()

        return super.onStartCommand(intent, flags, startId);
    }
}
