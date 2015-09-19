package com.example.android.model;

import com.example.android.utils.Byte2Hex;

/**
 * Created by xubinggui on 8/17/15.
 */
public class ShootingResultEventData {

    private DataBufferForList mDataBuffer;
    /**
     * only valid for shooting_result
     */
    public ShootingResultEventData(ShootingRecordWrapper shootingRecord){
        mDataBuffer = new DataBufferForList();
        //ball speed
        byte[] ballSpeed = new byte[4];
        mDataBuffer.append(ballSpeed);
        //currentShotArc
        float currentShotArc = shootingRecord.getCurrentShotArc();
        mDataBuffer.append(Byte2Hex.float2ByteArray(currentShotArc));
        //currentSpin
        float currentSpin = shootingRecord.getCurrentShotSpin();
        mDataBuffer.append(Byte2Hex.float2ByteArray(currentSpin));
        //currentSpinRate
        byte[] spinRate = new byte[4];
        mDataBuffer.append(spinRate);
        //Made
        final int currentShotMade = GlobalVar.shootMade;
        //final short currentShotMade = shootingRecord.getCurrentShotMade();
        mDataBuffer.append(((byte)(currentShotMade & 0xff)));
        //reserve
        byte[] reserve = new byte[3];
        mDataBuffer.append(reserve);
    }

    public byte[] getData(){
        return mDataBuffer.getBytes(mDataBuffer.size());
    }
}
