package com.example.android.model;

import com._94fifty.model.ShootingRecord;
import com.example.android.utils.Byte2Hex;

/**
 * Created by xubinggui on 8/17/15.
 */
public class ShootingResultEventData {

    private DataBuffer mDataBuffer;
    /**
     * only valid for shooting_result
     */
    public ShootingResultEventData(ShootingRecord shootingRecord){
        mDataBuffer = new DataBuffer(20);
        //ball speed
        byte[] ballSpeed = new byte[4];
        mDataBuffer.append(ballSpeed);
        //currentShotArc
        int currentShotArc = shootingRecord.getCurrentShotArc();
        mDataBuffer.append(Byte2Hex.int2byte(currentShotArc, 4));
        //currentSpin
        int currentSpin = shootingRecord.getCurrentShotSpin();
        mDataBuffer.append(Byte2Hex.int2byte(currentSpin, 4));
        //currentSpinRate
        byte[] spinRate = new byte[4];
        mDataBuffer.append(spinRate);
        //Made
        final short currentShotMade = shootingRecord.getCurrentShotMade();
        mDataBuffer.append(((byte)(currentShotMade & 0xff)));
        //reserve
        byte[] reserve = new byte[3];
        mDataBuffer.append(reserve);
    }

    public byte[] getData(){
        return mDataBuffer.consumeBytes(mDataBuffer.size());
    }
}
