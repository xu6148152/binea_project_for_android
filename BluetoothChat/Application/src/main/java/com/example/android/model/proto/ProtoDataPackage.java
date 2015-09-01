package com.example.android.model.proto;

import com.example.android.model.BaseData;
import com.example.android.model.EventType;
import com.example.android.model.GlobalVar;
import com.example.android.model.MessageType;
import com.example.android.model.ShootingRecordWrapper;
import com.example.android.utils.Byte2Hex;
import com.example.android.utils.DateUtil;
import com.example.zprotocol.BasketballDataPackage;

/**
 * Created by xubinggui on 8/28/15.
 */
public class ProtoDataPackage {

    private int mCrc16;

    BasketballDataPackage.BasketballProtocol basketballProtocol;
    public ProtoDataPackage(MessageType messageType, byte eventType, BaseData shootingRecord){
        basketballProtocol = new BasketballDataPackage.BasketballProtocol();

        basketballProtocol.header = new BasketballDataPackage.Header();
        basketballProtocol.header.prefix = "ZP";

        basketballProtocol.header.version = new byte[2];
        basketballProtocol.body = new BasketballDataPackage.Body();
        byte[] messageTypeBytes = new byte[2];
        //MessageType
        if(messageType == MessageType.BALL_EVENT){
            messageTypeBytes[0] = 0x00;
            messageTypeBytes[1] = 0x01;
            basketballProtocol.header.messageType = messageTypeBytes;
            long mTimeStamps = DateUtil.getCurrentTimeStamps();
            basketballProtocol.header.timeStamps = Byte2Hex.long2ByteArray(mTimeStamps, 4);
            ProtoBallEvent event = new ProtoBallEvent(eventType);
            basketballProtocol.body.commonBody = event.getBallCommonEvent();
            switch (eventType){
                case EventType.BALL_CONNECTED:
                case EventType.BALL_DISCOVERED:
                    ProtoBallNameData ballNameData = new ProtoBallNameData(GlobalVar.currentBallName);
                    basketballProtocol.body.ballState = ballNameData.getBallState();
                    mCrc16 = Byte2Hex.crc16(BasketballDataPackage.Body.toByteArray(
                            basketballProtocol.body));
                    basketballProtocol.header.crc16 = Byte2Hex.int2ByteArray(mCrc16, 2);
                    break;
                case EventType.BALL_DISCONNECTED:
                case EventType.SHOOTING_SESSION_STARTED:
                case EventType.SHOOTING_SESSION_ENDED:
                    mCrc16 = Byte2Hex.crc16(BasketballDataPackage.Body.toByteArray(basketballProtocol.body));
                    basketballProtocol.header.crc16 = Byte2Hex.int2ByteArray(mCrc16, 2);
                    break;
                case EventType.SHOOTING_RESULT:
                    if(shootingRecord != null) {
                        ProtoShootingResultEventData shootingResultEventData = new ProtoShootingResultEventData((ShootingRecordWrapper) shootingRecord);
                        basketballProtocol.body.ballResult = shootingResultEventData.getBallEventResult();
                        basketballProtocol.header.crc16 = Byte2Hex.int2ByteArray(Byte2Hex.crc16(BasketballDataPackage.Body.toByteArray(basketballProtocol.body)), 2);
                    }
                    break;
                case EventType.DRIBBLING_RESULT:

                    break;
            }
        }else if(messageType == MessageType.OPERATION_ACK){
            messageTypeBytes[0] = 0x00;
            messageTypeBytes[1] = (byte)0xA0;
        }else{
            messageTypeBytes[0] = 0x00;
            messageTypeBytes[1] = 0x20;
        }

        //body length
        basketballProtocol.header.bodyLength = Byte2Hex.int2ByteArray(
                basketballProtocol.body.getSerializedSize(), 4);
    }

    public byte[] getPackageData(){
        return BasketballDataPackage.BasketballProtocol.toByteArray(basketballProtocol);
    }
}
