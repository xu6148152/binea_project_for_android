package com.example.android.model;

import android.text.TextUtils;
import com.example.android.bluetoothchat.Constants;

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
        if(TextUtils.isEmpty(GlobalVar.currentMacAddress)){
            throw new RuntimeException("currentMacAddress is null");
        }
        String[] macAddressParts = GlobalVar.currentMacAddress.split(":");
        byte[] ballMac = new byte[6];
        for(int i = 0; i<macAddressParts.length; i++){
            Integer hex = Integer.parseInt(macAddressParts[i], 16);
            ballMac[i] = hex.byteValue();
        }
        dataBuffer.append(ballMac);
        //controllmac
        String[] ipAddressParts = Constants.SERVERADDRESS.split("\\.");
        byte[] controllerMac = new byte[6];
        for(int i = 0;i<ipAddressParts.length; i++){
            Integer integer = Integer.parseInt(ipAddressParts[i]);
            controllerMac[i] = integer.byteValue();
        }
        dataBuffer.append(controllerMac);

    }

    /**
     *
     * @return return the ballevent data
     */
    public byte[] getData(){
        return dataBuffer.getBytes(dataBuffer.size());
    }
}
