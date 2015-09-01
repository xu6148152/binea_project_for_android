package com.example.android.model.proto;

import android.text.TextUtils;
import com.example.android.model.DataBufferForList;
import com.example.android.model.GlobalVar;
import com.example.zprotocol.BasketballDataPackage;

public class ProtoBallEvent {
    /**
     * MessageType
     * 0x01 ball_connected
     * 0x02 ball_disconnected
     * 0x03 shooting_session_started
     * 0x04 shooting_session_ended
     * 0x05 shooting_result
     */

    DataBufferForList dataBuffer;

    BasketballDataPackage.BallEventCommon ballEventCommon;

    public ProtoBallEvent(byte eventType) {
        ballEventCommon = new BasketballDataPackage.BallEventCommon();
        //eventType
        ballEventCommon.eventType = new byte[] { eventType };
        //reserve
        ballEventCommon.reserve = new byte[3];
        //ball mac
        if (TextUtils.isEmpty(GlobalVar.currentBallMacAddress)) {
            throw new RuntimeException("currentBallMacAddress is null");
        }
        String[] macAddressParts = GlobalVar.currentBallMacAddress.split(":");

        byte[] ballMac = new byte[6];
        for (int i = 0; i < macAddressParts.length; i++) {
            Integer hex = Integer.parseInt(macAddressParts[i], 16);
            ballMac[i] = hex.byteValue();
        }
        ballEventCommon.ballMac = ballMac;
        //controllmac
        //if(!TextUtils.isEmpty(GlobalVar.currentDeviceMacAddress)) {
        String[] ipAddressParts = GlobalVar.currentDeviceMacAddress.split(":");
        byte[] controllerMac = new byte[6];
        for (int i = 0; i < ipAddressParts.length; i++) {
            Integer integer = Integer.parseInt(ipAddressParts[i], 16);
            controllerMac[i] = integer.byteValue();
        }
        ballEventCommon.controllerMac = controllerMac;
        //}

    }

    /**
     * @return return the ballevent data
     */
    public byte[] getData() {
        return dataBuffer.getBytes(dataBuffer.size());
    }

    public BasketballDataPackage.BallEventCommon getBallCommonEvent(){
        return ballEventCommon;
    }
}
