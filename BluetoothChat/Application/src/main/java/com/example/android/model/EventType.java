package com.example.android.model;

/**
 * Created by xubinggui on 8/17/15.
 */
public interface EventType{
    public static final byte BALL_CONNECTED = 0x01;
    public static final byte BALL_DISCONNECTED = 0x02;
    public static final byte BALL_DISCOVERED = 0x03;
    public static final byte SHOOTING_SESSION_STARTED = 0x23;
    public static final byte SHOOTING_SESSION_ENDED = 0x24;
    public static final byte SHOOTING_RESULT = 0x25;
    public static final byte DRIBBLING_RESULT = 0x26;
}
