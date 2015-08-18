package com.example.android.model;

import com.example.android.utils.Byte2Hex;

/**
 * Created by xubinggui on 8/17/15.
 */
public class PackageData {
    private final String TAG = PackageData.class.getCanonicalName();
    //messageType 0***0000 from controller  1***0000 to controller

    private final String mPrefix = "ZP";

    private DataBufferForList mHeaderDataBuffer;
    private DataBufferForList mBodyDataBuffer;

    private DataBufferForList mPackageData;

    public PackageData(MessageType messageType, byte eventType, BaseData shootingRecord){
        mHeaderDataBuffer = new DataBufferForList();
        mBodyDataBuffer = new DataBufferForList();
        byte[] prefix = mPrefix.getBytes();
        mHeaderDataBuffer.append(prefix);
        byte[] version = new byte[2];
        mHeaderDataBuffer.append(version);
        byte[] messageTypeBytes = new byte[2];
        //MessageType
        if(messageType == MessageType.BALL_EVENT){
            messageTypeBytes[0] = 0x00;
            messageTypeBytes[1] = 0x01;
            mHeaderDataBuffer.append(messageTypeBytes);
            BallEvent event = new BallEvent(eventType);
            byte[] data = event.getData();
            mBodyDataBuffer.append(data);
            switch (eventType){
                case EventType.BALL_CONNECTED:
                case EventType.BALL_DISCOVERED:
                    BallNameData ballNameData = new BallNameData(GlobalVar.currentDeviceName);
                    mBodyDataBuffer.append(ballNameData.getData());
                    final int ballNameCRC16 =
                            Byte2Hex.crc16(mBodyDataBuffer.getBytes(mBodyDataBuffer.size()));
                    mHeaderDataBuffer.append(Byte2Hex.int2byte(ballNameCRC16, 2));
                    break;
                case EventType.BALL_DISCONNECTED:
                case EventType.SHOOTING_SESSION_STARTED:
                case EventType.SHOOTING_SESSION_ENDED:
                    int crc16Result = Byte2Hex.crc16(mBodyDataBuffer.getBytes(16));
                    final byte[] crc16bytes = Byte2Hex.int2byte(crc16Result, 2);
                    mHeaderDataBuffer.append(crc16bytes);
                    break;
                case EventType.SHOOTING_RESULT:
                    if(shootingRecord != null) {
                        ShootingResultEventData shootingResultEventData = new ShootingResultEventData((ShootingRecordWrapper) shootingRecord);
                        mBodyDataBuffer.append(shootingResultEventData.getData());
                        final int shootingResultCRC16 = Byte2Hex.crc16(mBodyDataBuffer.getBytes(
                                mBodyDataBuffer.size()));
                        mHeaderDataBuffer.append(Byte2Hex.int2byte(shootingResultCRC16, 2));
                    }
                    break;
                case EventType.DRIBBLING_RESULT:

                    break;
            }
        }else if(messageType == MessageType.OPERATION_ACK){
            messageTypeBytes[0] = 0x00;
            messageTypeBytes[1] = 0x02;
        }else{
            messageTypeBytes[0] = 0x00;
            messageTypeBytes[1] = 0x10;
        }
        long secondes = System.currentTimeMillis() / 1000;
        mHeaderDataBuffer.append(Byte2Hex.long2ByteArray(secondes, 4));

        mPackageData = new DataBufferForList();
        mPackageData.append(mHeaderDataBuffer.getBytes(mHeaderDataBuffer.size()));
        mPackageData.append(mBodyDataBuffer.getBytes(mBodyDataBuffer.size()));

    }

    public byte[] getHeaderBytes(){
        return mHeaderDataBuffer.getBytes(mHeaderDataBuffer.size());
    }

    public byte[] getBodyBytes(){
        return mBodyDataBuffer.getBytes(mBodyDataBuffer.size());
    }

    public byte[] getPackageData(){
        return mPackageData.getBytes(mPackageData.size());
    }
}
