package com.example.android.bluetoothchat;

import com._94fifty.device.DeviceBridge;
import com._94fifty.model.request.AbstractRequest;
import com._94fifty.model.request.StartDribblingActivityRequest;
import com._94fifty.model.request.StartRawStreamRequest;
import com._94fifty.model.request.StartShootingActivityRequest;
import com._94fifty.model.response.StartDribblingActivityResponse;
import com._94fifty.model.response.StartRawStreamResponse;
import com._94fifty.model.response.StartShootingActivityResponse;
import com._94fifty.model.type.ActivityLimitBasis;
import com._94fifty.model.type.NotificationTrigger;
import com._94fifty.model.type.RequestStatus;
import com._94fifty.model.type.ShootingAlgorithmType;
import java.util.Date;

/**
 * Created by xubinggui on 7/27/15.
 */
public class DeviceFacade {
    public static RequestStatus startDribblingActivity(DeviceBridge deviceBridge, final ActivityLimitBasis activityLimitBasis,
            final NotificationTrigger notificationTrigger,
            final int activityLimit,
            final int notificationInterval,
            final int dribbleWindowSize,
            final long userId,
            final DeviceResponseCallback<StartDribblingActivityResponse> callback){
        final StartDribblingActivityRequest request = new StartDribblingActivityRequest();

        request.setStartTimestamp(new Date());
        request.setActivityLimitBasis(activityLimitBasis);
        request.setNotificationTrigger(notificationTrigger);
        request.setActivityLimit(activityLimit);
        request.setNotificationInterval(notificationInterval);
        request.setDribbleWindowSize(dribbleWindowSize);
        request.setUserId(userId);
        short i = (short)(int)(System.currentTimeMillis() & 0x7FFF);
        request.setToken(i);
        return executeRequest(deviceBridge, request);

    }

    private static RequestStatus executeRequest(DeviceBridge deviceBridge, AbstractRequest request){
        if(deviceBridge != null){
            return deviceBridge.executeRequest(request);
        }
        return RequestStatus.Error;
    }

    public static RequestStatus startShootingActivity(DeviceBridge deviceBridge, final ActivityLimitBasis activityLimitBasis,
            final NotificationTrigger notificationTrigger,
            final int activityLimit,
            final int notificationInterval,
            final long userId,
            final DeviceResponseCallback<StartShootingActivityResponse> callback){
        final StartShootingActivityRequest request = new StartShootingActivityRequest();

        request.setStartTimestamp(new Date());
        request.setActivityLimitBasis(activityLimitBasis);
        request.setNotificationTrigger(notificationTrigger);
        request.setActivityLimit(activityLimit);
        request.setNotificationInterval(notificationInterval);
        request.setPlayerHeight(70);
        request.setShootingAlgorithmType(ShootingAlgorithmType.WithMagnometer);
        request.setShotDistance(180);
        request.setUserId(userId);
        short i = (short)(int)(System.currentTimeMillis() & 0x7FFF);
        request.setToken(i);
        return executeRequest(deviceBridge, request);

    }

    public static RequestStatus startRawStream(DeviceBridge deviceBridge,
            final DeviceResponseCallback<StartRawStreamResponse> callback){
        final StartRawStreamRequest request = new StartRawStreamRequest();
        request.setActivityDuration(Integer.MAX_VALUE);
        request.setToken((short)(int)(System.currentTimeMillis() & 0x7FFF));
        return executeRequest(deviceBridge, request);

    }
}
