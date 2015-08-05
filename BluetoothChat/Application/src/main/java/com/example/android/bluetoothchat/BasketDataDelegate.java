package com.example.android.bluetoothchat;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import com._94fifty.device.BluetoothDeviceBridgeFactory;
import com._94fifty.device.DeviceBridge;
import com._94fifty.model.request.AbstractRequest;
import com._94fifty.model.response.AbstractResponse;
import com._94fifty.model.response.StartDribblingActivityResponse;
import com._94fifty.model.response.StartRawStreamResponse;
import com._94fifty.model.response.StartShootingActivityResponse;
import com._94fifty.model.response.notification.AbstractNotification;
import com._94fifty.model.response.notification.DribblingActivityRecordNotification;
import com._94fifty.model.response.notification.RawDataNotification;
import com._94fifty.model.response.notification.ShootingActivityRecordNotification;
import com._94fifty.model.type.ActivityLimitBasis;
import com._94fifty.model.type.ConnectionState;
import com._94fifty.model.type.InvocationType;
import com._94fifty.model.type.NotificationTrigger;
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

        //for(int i =0;i<notification.getRawData().length;i++){
        //    if(notification.getRawData()[i] != 0)
        //    Log.d(TAG,"onNotification " + notification.getRawData()[i]);
        //}
        if(abstractNotification.getType() == InvocationType.DribblingActivityRecord) {
            DribblingActivityRecordNotification notification =
                    (DribblingActivityRecordNotification) abstractNotification;
            Log.d(TAG, "DribblingActivityRecordNotification onNotification " + notification.getRecord().getTotalDribbles());
            mListener.dribblingActivityRecord(notification);
        }else if(abstractNotification.getType() == InvocationType.StartShootingActivity){
            ShootingActivityRecordNotification notification =
                    (ShootingActivityRecordNotification) abstractNotification;
            Log.d(TAG, "ShootingActivityRecordNotification onNotification " + notification.getRecord().getAverageShotSpin());
        }else if(abstractNotification.getType() == InvocationType.RawData){
            RawDataNotification notification =
                    (RawDataNotification) abstractNotification;
            String readMessage = Byte2Hex.bytesToHex(notification.getRawData());
            FileUtil.saveData(readMessage);
            //for(int i = 0; i < notification.getRawData().length; i++){
            //    if(notification.getRawData()[i] != 0){
            //        Log.d(TAG, "RawDataNotification onNotification " + notification.getRawData()[i]);
            //    }
            //}
        }

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

    public void startDribblingActivity(){
        DeviceFacade.startDribblingActivity(mDeviceBridge, ActivityLimitBasis.Time,
                NotificationTrigger.Time, 600000, 200, 5, 1, new DeviceResponseCallback<StartDribblingActivityResponse>() {
                    @Override protected void onResponse(StartDribblingActivityResponse response) {
                        Log.d(TAG, " response " + response.getStatus().isOK());
                    }
                });
    }

    public void startShootingActivity(){
        DeviceFacade.startShootingActivity(mDeviceBridge, ActivityLimitBasis.Event,
                NotificationTrigger.Event, 20, 1, 1,
                new DeviceResponseCallback<StartShootingActivityResponse>() {
                    @Override protected void onResponse(StartShootingActivityResponse response) {
                        Log.d(TAG, " response " + response.getStatus().isOK());
                    }
                });
    }

    public void startRawStream(){
        DeviceFacade.startRawStream(mDeviceBridge, new DeviceResponseCallback<StartRawStreamResponse>() {
            @Override protected void onResponse(StartRawStreamResponse response) {
                Log.d(TAG, " response " + response.getStatus().isOK());
            }
        });
    }
}
