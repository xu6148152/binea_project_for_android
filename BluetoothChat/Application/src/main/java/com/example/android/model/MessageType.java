package com.example.android.model;

/**
 * Created by xubinggui on 8/17/15.
 */
public enum MessageType {
    BALL_EVENT,
    BALL_MANAGE,
    OPERATION_ACK;

    public static int toInt(MessageType messageType){
        switch (messageType){
            case BALL_EVENT:
                return 0;
            case BALL_MANAGE:
                return 1;
            case OPERATION_ACK:
                return 2;
            default:
                return -1;
        }
    }

    public static MessageType toEnum(int value){
        switch (value){
            case 0:
                return BALL_EVENT;
            case 1:
                return BALL_MANAGE;
            case 2:
                return OPERATION_ACK;
            default:
                return BALL_EVENT;
        }
    }
}
