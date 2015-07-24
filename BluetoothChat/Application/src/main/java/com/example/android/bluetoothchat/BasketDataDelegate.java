package com.example.android.bluetoothchat;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import com._94fifty.device.BluetoothDeviceBridgeFactory;
import com._94fifty.device.DeviceBridge;
import com._94fifty.model.request.AbstractRequest;
import com._94fifty.model.response.AbstractResponse;
import com._94fifty.model.response.notification.AbstractNotification;
import com._94fifty.model.response.notification.DribblingActivityRecordNotification;
import com._94fifty.model.type.ConnectionState;
import com._94fifty.model.type.RequestStatus;

/**
 * Created by xubinggui on 7/20/15.
 */
public class BasketDataDelegate implements DeviceBridge.Delegate {
    private final static String TAG = BasketDataDelegate.class.getCanonicalName();

    private DeviceBridge mDeviceBridge;

    private ConnectionState mCurrentConnectionState;

    private BasketballDataNotificationListener mListener;

    public BasketDataDelegate(BluetoothSocket socket) {
        BluetoothDeviceBridgeFactory factory = new BluetoothDeviceBridgeFactory();
        mDeviceBridge = factory.create(socket, this);
        mDeviceBridge.addListener(this);
    }

    @Override public void onConnectionStateChanged(ConnectionState connectionState) {
        Log.d(TAG, "onConnectionStateChanged " );
        mCurrentConnectionState = connectionState;
        //if(connectionState == ConnectionState.Open){
        //    Log.d(TAG, "onConnectionStateChanged open ");
        //    mDeviceBridge.executeRequest(new StartRawStreamRequest());
        //}
    }

    @Override public void onNotification(AbstractNotification abstractNotification) {
        DribblingActivityRecordNotification notification =
                (DribblingActivityRecordNotification) abstractNotification;
        //for(int i =0;i<notification.getRawData().length;i++){
        //    if(notification.getRawData()[i] != 0)
        //    Log.d(TAG,"onNotification " + notification.getRawData()[i]);
        //}
        Log.d(TAG, "onNotification " + notification.getRecord().getTotalDribbles());
        mListener.dribblingActivityRecord(notification);

    }

    @Override public void onResponse(AbstractResponse abstractResponse) {
        Log.d(TAG, "onResponse " + abstractResponse.toString());
    }

    @Override public void onResponseError(byte[] bytes, String s) {
        Log.d(TAG, "onResponseError " + s);
    }

    public ConnectionState getConnectionState(){
        return mCurrentConnectionState;
    }

    public RequestStatus sendRequest(AbstractRequest request){
        if(mCurrentConnectionState == ConnectionState.Open) {
            return mDeviceBridge.executeRequest(request);
        }
        return RequestStatus.Error;
    }

    public void setBasketballDataMotificationListener(BasketballDataNotificationListener listener){
        mListener = listener;
    }
}
