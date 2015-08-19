package com.example.android.bluetoothchat;

import android.content.Intent;
import android.util.Log;
import com.example.android.model.BaseData;
import com.example.android.model.DribblingRecordWrapper;
import com.example.android.model.GlobalVar;
import com.example.android.model.MessageType;
import com.example.android.model.PackageData;
import com.example.android.model.ShootingRecordWrapper;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by xubinggui on 7/31/15.
 */
public class TaskIntentService extends WrapperIntentService {

    public static final String DATA = "DATA";
    public static final String MESSAGE_TYPE = "MESSAGE_TYPE";
    public static final String EVENT_TYPE = "EVENT_TYPE";

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
        Log.d(TAG, " onHandleIntent data ");
        final Serializable data = intent.getSerializableExtra(DATA);
        final byte eventType = intent.getByteExtra(EVENT_TYPE, (byte) -1);
        final int messageType = intent.getIntExtra(MESSAGE_TYPE, -1);
        if(data instanceof ShootingRecordWrapper){
            ShootingRecordWrapper recordWrapper = (ShootingRecordWrapper) data;
            sendUDPMessage(MessageType.toEnum(messageType), eventType, recordWrapper);
            Log.d(TAG, " ShootingRecordWrapper " + recordWrapper.getCurrentShotMade());
        }else if(data instanceof DribblingRecordWrapper){
            DribblingRecordWrapper recordWrapper = (DribblingRecordWrapper) data;
            Log.d(TAG, " DribblingRecordWrapper " + recordWrapper.getTotalDribbles());
            sendUDPMessage(MessageType.toEnum(messageType), eventType, recordWrapper);
        }else{
            sendUDPMessage(MessageType.toEnum(messageType), eventType, null);
        }
    }

    private void sendUDPMessage(MessageType messageType, byte eventType, BaseData data) {
        PackageData packageData = new PackageData(messageType, eventType, data);
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress local = InetAddress.getByName(GlobalVar.SERVER_ADDRESS);
            DatagramPacket packet = new DatagramPacket(
                    packageData.getPackageData(),
                    packageData.getPackageData().length,
                    local,
                    Constants.UDP_PORT);
            socket.send(packet);
            Log.d(TAG, "sendUDPMessage " + packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
