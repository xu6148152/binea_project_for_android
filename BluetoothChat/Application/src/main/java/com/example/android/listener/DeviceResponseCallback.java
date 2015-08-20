package com.example.android.listener;

import com._94fifty.model.response.AbstractResponse;

/**
 * Created by xubinggui on 7/27/15.
 */
public abstract class DeviceResponseCallback<T extends AbstractResponse> {

    public void onDeviceResponse(AbstractResponse response){
        if(response != null) {
            onResponse((T)response);
        }
    }

    public void onDeviceTimeOut(){
        onResponse(null);
    }

    protected abstract void onResponse(T response);
}
