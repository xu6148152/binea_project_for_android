package com.example.android.bluetoothchat;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by xubinggui on 8/14/15.
 */
public class DataProcessHandler extends Handler {
    private final String TAG = DataProcessHandler.class.getCanonicalName();

    public static final int NEW_RAW_DATA = 1000;
    public DataProcessHandler(Looper looper){
        super(looper);
    }

    @Override public void handleMessage(Message msg) {
        RawDataMessage rawDataMessage = null;
        if(!(msg.obj instanceof RawDataMessage)){
            return;
        }
        rawDataMessage = (RawDataMessage) msg.obj;
        switch (msg.what){
            case NEW_RAW_DATA:
                Log.d(TAG, "handle data");
                String readMessage = Byte2Hex.convert2byte(rawDataMessage.data);
                FileUtil.saveData(readMessage);
                break;
        }
    }
}
