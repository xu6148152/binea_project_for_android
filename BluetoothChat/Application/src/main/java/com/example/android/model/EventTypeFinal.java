package com.example.android.model;

/**
 * Created by xubinggui on 8/17/15.
 */
public final class EventTypeFinal {
    public static final byte FROM_CONTROLLER_BALL_EVENT_MESSAGE_TYPE_LOW = Byte.parseByte("0000", 2);
    public static final byte FROM_CONTROLLER_BALL_EVENT_MESSAGE_TYPE_HIGH = Byte.parseByte("0001", 2);
    public static final byte FROM_CONTROLLER_OPERATION_ACK_LOW = Byte.parseByte("0000", 2);
    public static final byte FROM_CONTROLLER_OPERATION_ACK_HIGH = Byte.parseByte("0002", 2);
    public static final byte FROM_CONTROLLER_BALL_MANAGEMENT_LOW = Byte.parseByte("0000", 2);
    public static final byte FROM_CONTROLLER_BALL_MANAGEMENT_HIGH = Byte.parseByte("1002", 2);
}
