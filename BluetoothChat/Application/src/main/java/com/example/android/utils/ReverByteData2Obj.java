package com.example.android.utils;

import com.example.android.model.BaseData;
import com.example.android.model.EventType;
import com.example.android.model.MessageType;
import com.example.android.model.ShootingRecordWrapper;

/**
 * Created by xubinggui on 8/21/15.
 */
public class ReverByteData2Obj {

    public ReverByteData2Obj(
            byte[] data,
            MessageType messageType,
            long timestamps,
            byte[] crc16,
            int bodyLength,
            byte eventType,
            BaseData recordWrapper){
        /**
         * header
         */
        byte[] header = new byte[16];
        System.arraycopy(data, 0, header, 0, header.length);

        //prefix
        byte[] prefix = new byte[2];
        System.arraycopy(header, 0, prefix, 0, prefix.length);
        String strPrefix = new String(prefix);
        if(!"ZP".equals(strPrefix)){
            throw new AssertionError("prefix error");
        }

        //messageType
        byte[] messageTypeBytes = new byte[2];
        System.arraycopy(header, 4, messageTypeBytes, 0, messageTypeBytes.length);
        final short messageTypeValue = Byte2Hex.bytesArray2Short(messageTypeBytes);
        if(messageTypeValue != 0x0100){
            throw new AssertionError("messageType error");
        }

        //timestamps
        byte[] timestampsBytes = new byte[4];
        System.arraycopy(header, 6, timestampsBytes, 0, timestampsBytes.length);
        if(timestamps != Byte2Hex.bytesArray2int(timestampsBytes)){
            throw new AssertionError("timestamps error");
        }

        //crc-16
        byte[] crc16bytes = new byte[2];
        System.arraycopy(header, 10, crc16bytes, 0, crc16bytes.length);
        if(!(crc16[0] == crc16bytes[0] && crc16[1] == crc16bytes[1])){
            throw new AssertionError("crc16 error");
        }

        //body length
        byte[] bodyLengthBytes = new byte[4];
        System.arraycopy(header, header.length - 4, bodyLengthBytes, 0, bodyLengthBytes.length);
        if(bodyLength != Byte2Hex.bytesArray2int(bodyLengthBytes)){
            throw new AssertionError("bodyLength error");
        }

        /**
         * body
         */
        byte[] body = new byte[data.length - header.length];
        System.arraycopy(data, header.length, body, 0, body.length);

        //BALL TYPE EVENT_TYPE
        byte eventTypeByte = body[0];
        if(eventType != eventTypeByte){
            throw new AssertionError("eventType error");
        }

        //BALL MAC
        byte[] ballMacBytes = new byte[6];
        System.arraycopy(body, 4, ballMacBytes, 0, ballMacBytes.length);
        StringBuilder ballMacSb = new StringBuilder();
        for(int i = 0;i<ballMacBytes.length;i++){
            //TODO
        }

        //CONTROLLER MAC
        //TODO

        //shooting result
        if(eventType == EventType.SHOOTING_RESULT){

            ShootingRecordWrapper wrapper = (ShootingRecordWrapper) recordWrapper;
            //speed none

            //arc
            byte[] arcBytes = new byte[4];
            System.arraycopy(body, 20, arcBytes, 0, arcBytes.length);
            //if(Byte2Hex.bytes2float(arcBytes) != wrapper.getCurrentShotArc()){
            //    throw new AssertionError("arc error");
            //}
            //spin
            byte[] spinBytes = new byte[4];
            System.arraycopy(body, 24, spinBytes, 0, spinBytes.length);
            //if(Byte2Hex.bytes2float(spinBytes) != wrapper.getCurrentShotSpin()){
            //    throw new AssertionError("spin error");
            //}

            //spin rate

            //made
            byte madeByte = body[body.length - 3 - 1];
        }else if(eventType == EventType.BALL_CONNECTED ||
                eventType== EventType.BALL_DISCONNECTED ||
                eventType == EventType.BALL_DISCOVERED){
            //ball name length
            byte[] ballNameLengthBytes = new byte[2];
            System.arraycopy(body, 16, ballNameLengthBytes, 0, ballNameLengthBytes.length);

            //ball name string
            byte[] ballNameStringBytes = new byte[body.length - 18];
            System.arraycopy(body, 18, ballNameStringBytes, 0, ballNameStringBytes.length);
        }else if(eventType == EventType.DRIBBLING_RESULT){

        }
    }
}
