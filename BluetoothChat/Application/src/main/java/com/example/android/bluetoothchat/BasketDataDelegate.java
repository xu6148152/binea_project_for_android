package com.example.android.bluetoothchat;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import com._94fifty.device.BluetoothDeviceBridgeFactory;
import com._94fifty.device.DeviceBridge;
import com._94fifty.model.request.StartRawStreamRequest;
import com._94fifty.model.response.AbstractResponse;
import com._94fifty.model.response.notification.AbstractNotification;
import com._94fifty.model.response.notification.RawDataNotification;
import com._94fifty.model.type.ConnectionState;

/**
 * Created by xubinggui on 7/20/15.
 */
public class BasketDataDelegate implements DeviceBridge.Delegate {
    private final static String TAG = BasketDataDelegate.class.getCanonicalName();

    private DeviceBridge mDeviceBridge;

    public BasketDataDelegate(BluetoothSocket socket) {
        BluetoothDeviceBridgeFactory factory = new BluetoothDeviceBridgeFactory();
        mDeviceBridge = factory.create(socket, this);
        mDeviceBridge.addListener(this);
    }

    @Override public void onConnectionStateChanged(ConnectionState connectionState) {
        Log.d(TAG, "onConnectionStateChanged " );
        if(connectionState == ConnectionState.Open){
            Log.d(TAG, "onConnectionStateChanged open ");
            mDeviceBridge.executeRequest(new StartRawStreamRequest());
        }
    }

    @Override public void onNotification(AbstractNotification abstractNotification) {
        RawDataNotification notification =
                (RawDataNotification) abstractNotification;
        for(int i =0;i<notification.getRawData().length;i++){
            if(notification.getRawData()[i] != 0)
            Log.d(TAG,"onNotification " + notification.getRawData()[i]);
        }
    }

    @Override public void onResponse(AbstractResponse abstractResponse) {
        Log.d(TAG, "onResponse " + abstractResponse.toString());
    }

    @Override public void onResponseError(byte[] bytes, String s) {
        Log.d(TAG, "onResponseError " + s);
    }
}
