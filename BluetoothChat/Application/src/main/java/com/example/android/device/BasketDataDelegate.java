package com.example.android.device;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.HandlerThread;
import android.util.Log;
import com._94fifty.device.BluetoothDeviceBridgeFactory;
import com._94fifty.device.DeviceBridge;
import com._94fifty.model.request.AbstractRequest;
import com._94fifty.model.response.AbstractResponse;
import com._94fifty.model.response.EndDribblingActivityResponse;
import com._94fifty.model.response.EndRawStreamResponse;
import com._94fifty.model.response.EndShootingActivityResponse;
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
import com._94fifty.model.type.ResponseStatus;
import com.example.android.listener.BasketballDataNotificationListener;
import com.example.android.process.DataProcessHandler;
import com.example.android.bluetoothchat.DeviceResponseCallback;
import com.example.android.listener.EndDribblingListener;
import com.example.android.listener.EndRawStreamListener;
import com.example.android.listener.EndShootingListener;

/**
 * Created by xubinggui on 7/20/15.
 */
public class BasketDataDelegate implements DeviceBridge.Delegate {
    private final static String TAG = BasketDataDelegate.class.getCanonicalName();

    private DeviceBridge mDeviceBridge;

    private ConnectionState mCurrentConnectionState;

    private BasketballDataNotificationListener mListener;

    private EndRawStreamListener endRawStreamListener;
    private EndShootingListener endShootingListener;
    private EndDribblingListener endDribblingListener;

    private HandlerThread mHandlerThread;
    private DataProcessHandler handler;

    private StringBuilder sb;

    private Context mContext;

    public BasketDataDelegate(BluetoothSocket socket, Context context) {
        BluetoothDeviceBridgeFactory factory = new BluetoothDeviceBridgeFactory();
        mDeviceBridge = factory.create(socket, this);
        mDeviceBridge.addListener(this);
        mHandlerThread = new HandlerThread("DataDeletate Thread " + System.currentTimeMillis());
        mHandlerThread.start();
        handler = new DataProcessHandler(mHandlerThread.getLooper());
        mContext = context;
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
        Log.d(TAG, "onNotification" + abstractNotification);
        //for(int i =0;i<notification.getRawData().length;i++){
        //    if(notification.getRawData()[i] != 0)
        //    Log.d(TAG,"onNotification " + notification.getRawData()[i]);
        //}
        if(abstractNotification.getType() == InvocationType.DribblingActivityRecord) {
            DribblingActivityRecordNotification notification =
                    (DribblingActivityRecordNotification) abstractNotification;
            Log.d(TAG, "DribblingActivityRecordNotification onNotification " + notification.getRecord().getTotalDribbles());
            mListener.dribblingActivityRecord(notification);
        }else if(abstractNotification.getType() == InvocationType.ShootingActivityRecord){
            ShootingActivityRecordNotification notification =
                    (ShootingActivityRecordNotification) abstractNotification;
            Log.d(TAG, "ShootingActivityRecordNotification onNotification " + notification);
        }else if(abstractNotification.getType() == InvocationType.RawData){
            RawDataNotification notification =
                    (RawDataNotification) abstractNotification;
            Log.d(TAG, "notificationId " + notification.getNotificationId());
            //Intent intent = new Intent(mContext, TaskIntentService.class);
            //intent.putExtra(TaskIntentService.DATA, notification.getRawData());
            //mContext.startService(intent);
            //String readMessage = Byte2Hex.convert2byte(notification.getRawData());
            //sb.append(readMessage);
            //final Message msg = handler.obtainMessage();
            //msg.what = DataProcessHandler.NEW_RAW_DATA;
            //msg.obj = new RawDataMessage(notification.getRawData());
            //handler.sendMessage(msg);

        }

    }

    @Override public void onResponse(AbstractResponse abstractResponse) {
        if(abstractResponse.getType() == InvocationType.EndRawStream){
            if(endRawStreamListener != null) {
                if (abstractResponse.getStatus() == ResponseStatus.OK) {
                    Log.d(TAG, "rawData " + sb.toString());
                    sb.delete(0, sb.length());
                    endRawStreamListener.onResponse(true);
                } else {
                    endRawStreamListener.onResponse(false);
                }
            }
        }else if(abstractResponse.getType() == InvocationType.EndShootingActivity){
            if(endShootingListener != null) {
                if (abstractResponse.getStatus() == ResponseStatus.OK) {
                    endShootingListener.onResponse(true);
                } else {
                    endShootingListener.onResponse(false);
                }
            }
        }else if(abstractResponse.getType() == InvocationType.EndDribblingActivity){
            if(endDribblingListener != null) {
                if (abstractResponse.getStatus() == ResponseStatus.OK) {
                    endDribblingListener.onResponse(true);
                } else {
                    endDribblingListener.onResponse(false);
                }
            }
        }
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
                NotificationTrigger.Time, 600000, 200, 5, 1,
                new DeviceResponseCallback<StartDribblingActivityResponse>() {
                    @Override protected void onResponse(StartDribblingActivityResponse response) {
                        Log.d(TAG,
                                "startDribblingActivity response " + response.getStatus().isOK());
                    }
                });
    }

    public void endDribblingActivity(){
        DeviceFacade.endDribblingActivity(mDeviceBridge,
                new DeviceResponseCallback<EndDribblingActivityResponse>() {
                    @Override protected void onResponse(EndDribblingActivityResponse response) {
                        Log.d(TAG,
                                "endDribblingActivity onResponse " + response.getStatus().isOK());
                    }
                });
    }

    public void startShootingActivity(){
        DeviceFacade.startShootingActivity(mDeviceBridge, ActivityLimitBasis.Time,
                NotificationTrigger.Event, 10, 1, 1,
                new DeviceResponseCallback<StartShootingActivityResponse>() {
                    @Override protected void onResponse(StartShootingActivityResponse response) {
                        Log.d(TAG, "startShootingActivity response " + response.getStatus().isOK());
                    }
                });
    }

    public void endShootingActivity(){
        DeviceFacade.endShootingActivity(mDeviceBridge,
                new DeviceResponseCallback<EndShootingActivityResponse>() {
                    @Override protected void onResponse(EndShootingActivityResponse response) {
                        Log.d(TAG, "endShootingActivity onResponse " + response.getStatus().isOK());
                    }
                });
    }

    public void startRawStream(){
        sb = new StringBuilder();
        DeviceFacade.startRawStream(mDeviceBridge,
                new DeviceResponseCallback<StartRawStreamResponse>() {
                    @Override protected void onResponse(StartRawStreamResponse response) {
                        Log.d(TAG, "startRawStream response " + response.getStatus().isOK());
                    }
                });
    }

    public void endRawStream(){
        DeviceFacade.endRawStream(mDeviceBridge,
                new DeviceResponseCallback<EndRawStreamResponse>() {
                    @Override protected void onResponse(EndRawStreamResponse response) {
                        Log.d(TAG, "endRawStream onResponse " + response.getStatus().isOK());
                    }
                });
    }

    public void setEndRawStreamListener(EndRawStreamListener listener){
        endRawStreamListener = listener;
    }

    public void setEndShootingListener(EndShootingListener listener){
        endShootingListener = listener;
    }

    public void setEndDribblingListener(EndDribblingListener listener){
        endDribblingListener = listener;
    }
}
