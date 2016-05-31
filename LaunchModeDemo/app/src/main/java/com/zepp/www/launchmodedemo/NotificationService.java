package com.zepp.www.launchmodedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationService extends Service {
    public NotificationService() {
    }

    @Override public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        Intent tmpIntent = new Intent(this, NotificationReceiver.class);
        sendBroadcast(tmpIntent);
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
}
