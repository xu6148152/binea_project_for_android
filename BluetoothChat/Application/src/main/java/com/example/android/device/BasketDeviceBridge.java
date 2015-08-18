package com.example.android.device;

import com._94fifty.device.DeviceBridge;
import com._94fifty.model.DeviceInfo;
import com._94fifty.model.Version;
import com._94fifty.model.request.AbstractRequest;
import com._94fifty.model.type.RequestStatus;

/**
 * Created by xubinggui on 7/20/15.
 */
public class BasketDeviceBridge implements DeviceBridge {
    @Override public void addListener(Delegate delegate) {

    }

    @Override public void removeListener(Delegate delegate) {

    }

    @Override public DeviceInfo getDeviceInfo() {
        return null;
    }

    @Override public Version getBridgeVersion() {
        return null;
    }

    @Override public RequestStatus executeRequest(AbstractRequest abstractRequest) {
        return null;
    }
}
