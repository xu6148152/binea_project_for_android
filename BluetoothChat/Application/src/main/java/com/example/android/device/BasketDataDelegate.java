package com.example.android.device;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
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
import com._94fifty.model.type.NotificationTrigger;
import com._94fifty.model.type.RequestStatus;
import com._94fifty.model.type.ResponseStatus;
import com.example.android.listener.DeviceResponseCallback;
import com.example.android.bluetoothchat.TaskIntentService;
import com.example.android.listener.BasketballDataNotificationListener;
import com.example.android.listener.EndDribblingListener;
import com.example.android.listener.EndRawStreamListener;
import com.example.android.listener.EndShootingListener;
import com.example.android.model.DribblingRecordWrapper;
import com.example.android.model.EventType;
import com.example.android.model.MessageType;
import com.example.android.model.ShootingRecordWrapper;
import com.example.android.process.DataProcessHandler;

import static com._94fifty.model.type.InvocationType.DribblingActivityRecord;
import static com._94fifty.model.type.InvocationType.RawData;
import static com._94fifty.model.type.InvocationType.ShootingActivityRecord;

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
        Log.d(TAG, "onConnectionStateChanged ");
        mCurrentConnectionState = connectionState;
        //if(connectionState == ConnectionState.Open){
        //    Log.d(TAG, "onConnectionStateChanged open ");
        //    mDeviceBridge.executeRequest(new StartRawStreamRequest());
        //}
    }

    @Override public void onNotification(AbstractNotification abstractNotification) {
        Log.d(TAG, "onNotification" + abstractNotification);
        Intent intent = new Intent(mContext, TaskIntentService.class);
        //for(int i =0;i<notification.getRawData().length;i++){
        //    if(notification.getRawData()[i] != 0)
        //    Log.d(TAG,"onNotification " + notification.getRawData()[i]);
        //}
        if(abstractNotification.getType() == DribblingActivityRecord) {
            DribblingActivityRecordNotification notification =
                    (DribblingActivityRecordNotification) abstractNotification;
            Log.d(TAG, " DribblingActivityRecordNotification isLastNotification "
                    + notification.isLastNotification());
            DribblingRecordWrapper recordWrapper = new DribblingRecordWrapper(notification.getRecord());
            intent.putExtra(TaskIntentService.DATA, recordWrapper);
            intent.putExtra(TaskIntentService.MESSAGE_TYPE, MessageType.toInt(MessageType.BALL_EVENT));
            mListener.dribblingActivityRecord(notification);
        }else if(abstractNotification.getType() == ShootingActivityRecord){
            ShootingActivityRecordNotification notification =
                    (ShootingActivityRecordNotification) abstractNotification;
            ShootingRecordWrapper recordWrapper = new ShootingRecordWrapper(notification.getRecord());
            intent.putExtra(TaskIntentService.DATA, recordWrapper);
            intent.putExtra(TaskIntentService.MESSAGE_TYPE, MessageType.toInt(MessageType.BALL_EVENT));
            intent.putExtra(TaskIntentService.EVENT_TYPE, EventType.SHOOTING_RESULT);
            Log.d(TAG, "ShootingActivityRecordNotification " + recordWrapper);
        }else if(abstractNotification.getType() == RawData){
            RawDataNotification notification =
                    (RawDataNotification) abstractNotification;
            Log.d(TAG, "notificationId " + notification.getNotificationId());
        }

        mContext.startService(intent);
    }

    @Override public void onResponse(AbstractResponse abstractResponse) {
        Log.d(TAG, "onResponse " + abstractResponse.toString());
        if(abstractResponse.getStatus() != ResponseStatus.OK){
            if(abstractResponse.getStatus() == ResponseStatus.InProgress){
                   if(abstractResponse instanceof EndShootingActivityResponse){
                       endShootingActivity();
                   }else if(abstractResponse instanceof EndDribblingActivityResponse){
                       endDribblingActivity();
                   }
            }
            return;
        }
        Intent intent = null;
        switch (abstractResponse.getType()){
            case StartRawStream:

                break;
            case EndRawStream:

                break;
            case StartDribblingActivity:

                break;
            case EndDribblingActivity:

                break;
            case StartShootingActivity:
                intent = new Intent(mContext, TaskIntentService.class);
                intent.putExtra(TaskIntentService.MESSAGE_TYPE, MessageType.toInt(
                        MessageType.BALL_EVENT));
                intent.putExtra(TaskIntentService.EVENT_TYPE, EventType.SHOOTING_SESSION_STARTED);
                break;
            case EndShootingActivity:
                intent = new Intent(mContext, TaskIntentService.class);
                intent.putExtra(TaskIntentService.MESSAGE_TYPE, MessageType.toInt(
                        MessageType.BALL_EVENT));
                intent.putExtra(TaskIntentService.EVENT_TYPE, EventType.SHOOTING_SESSION_ENDED);
                break;
        }
        if(intent != null) {
            mContext.startService(intent);
        }
        //if(abstractResponse.getType() == EndRawStream){
        //    if(endRawStreamListener != null) {
        //        if (abstractResponse.getStatus() == ResponseStatus.OK) {
        //            endRawStreamListener.onResponse(true);
        //        } else {
        //            endRawStreamListener.onResponse(false);
        //        }
        //    }
        //}else if(abstractResponse.getType() == EndShootingActivity){
        //    if(endShootingListener != null) {
        //        if (abstractResponse.getStatus() == ResponseStatus.OK) {
        //            endShootingListener.onResponse(true);
        //        } else {
        //            endShootingListener.onResponse(false);
        //        }
        //    }
        //}else if(abstractResponse.getType() == EndDribblingActivity){
        //    if(endDribblingListener != null) {
        //        if (abstractResponse.getStatus() == ResponseStatus.OK) {
        //            endDribblingListener.onResponse(true);
        //        } else {
        //            endDribblingListener.onResponse(false);
        //        }
        //    }
        //}
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
        DeviceFacade.startShootingActivity(mDeviceBridge, ActivityLimitBasis.Event,
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
