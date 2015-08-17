package com.example.android.bluetoothchat;

import android.content.Intent;
import android.util.Log;
import com.example.android.utils.Byte2Hex;
import com.example.android.utils.FileUtil;

/**
 * Created by xubinggui on 7/31/15.
 */
public class TaskIntentService extends WrapperIntentService {

    public static final String DATA = "DATA";

    private static final String TAG = TaskIntentService.class.getCanonicalName();

    public static final String KEY_SERVICE_ACTION = "key_service_action";
    public static final String KEY_SERVICE_ID = "key_service_id";
    public static final int ACTION_SYNC = 0;
    public static final int ACTION_STOP_SYNC = 3;
    public static final int ACTION_UPLOAD_TRACK = 1;
    public static final int ACTION_UPLOAD_PRACTICE = 2;

    public TaskIntentService(){
        super(TAG);
    }

    @Override public void onCreate() {
        super.onCreate();
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        int action = intent.getIntExtra(KEY_SERVICE_ACTION, -1);
        if(action == ACTION_STOP_SYNC){
            clearQueue();
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override protected void onHandleIntent(Intent intent) {
        byte[] data = intent.getByteArrayExtra(DATA);
        Log.d(TAG, " onHandleIntent data " + data);
        String readMessage = Byte2Hex.convert2byte(data);
        FileUtil.saveData(readMessage);
        //int action = intent.getIntExtra(KEY_SERVICE_ACTION, -1);
        //long serviceId = intent.getLongExtra(KEY_SERVICE_ID, -1);
        //
        //switch (action){
        //    case ACTION_SYNC:
        //        Log.d(TAG, "onHandleIntent");
        //        sync();
        //        break;
        //}
    }

    private void sync() {

    }
}
