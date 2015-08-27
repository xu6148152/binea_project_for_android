package com.example.android.model;

import android.text.TextUtils;

/**
 * Created by xubinggui on 8/17/15.
 */
public class BallEvent {
    /**
     * MessageType
     * 0x01 ball_connected
     * 0x02 ball_disconnected
     * 0x03 shooting_session_started
     * 0x04 shooting_session_ended
     * 0x05 shooting_result
     */

    DataBufferForList dataBuffer;
    public BallEvent(byte eventType){
        dataBuffer = new DataBufferForList();
        //eventType
        dataBuffer.append(eventType);
        //reserve
        byte[] reserve = new byte[3];
        dataBuffer.append(reserve);
        //ball mac
        if(TextUtils.isEmpty(GlobalVar.currentBallMacAddress)){
            throw new RuntimeException("currentBallMacAddress is null");
        }
        String[] macAddressParts = GlobalVar.currentBallMacAddress.split(":");

        byte[] ballMac = new byte[6];
        for(int i = 0; i<macAddressParts.length; i++){
            Integer hex = Integer.parseInt(macAddressParts[i], 16);
            ballMac[i] = hex.byteValue();
        }
        dataBuffer.append(ballMac);
        //controllmac
        //if(!TextUtils.isEmpty(GlobalVar.currentDeviceMacAddress)) {
            String[] ipAddressParts = GlobalVar.currentDeviceMacAddress.split(":");
            byte[] controllerMac = new byte[6];
            for (int i = 0; i < ipAddressParts.length; i++) {
                Integer integer = Integer.parseInt(ipAddressParts[i], 16);
                controllerMac[i] = integer.byteValue();
            }
            dataBuffer.append(controllerMac);
        //}

    }

    /**
     *
     * @return return the ballevent data
     */
    public byte[] getData(){
        return dataBuffer.getBytes(dataBuffer.size());
    }
}
