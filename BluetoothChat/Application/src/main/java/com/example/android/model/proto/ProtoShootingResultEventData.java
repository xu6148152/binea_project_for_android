package com.example.android.model.proto;

import com.example.android.model.DataBufferForList;
import com.example.android.model.ShootingRecordWrapper;
import com.example.zprotocol.BasketballDataPackage;

/**
 * Created by xubinggui on 8/17/15.
 */
public class ProtoShootingResultEventData {

    private DataBufferForList mDataBuffer;

    private BasketballDataPackage.BallEventResult mBallResult;
    /**
     * only valid for shooting_result
     */
    public ProtoShootingResultEventData(ShootingRecordWrapper shootingRecord){
        mBallResult = new BasketballDataPackage.BallEventResult();
        //ball speed
        mBallResult.speed = 0f;
        //currentShotArc
        mBallResult.arc = shootingRecord.getCurrentShotArc();
        //currentSpin
        mBallResult.spin = shootingRecord.getCurrentShotSpin();
        //currentSpinRate
        mBallResult.spinRate = 0f;
        //Made
        mBallResult.made = new byte[]{((byte)(shootingRecord.getCurrentShotMade() & 0xff))};
        //reserve
        mBallResult.reserve = new byte[3];
    }

    public byte[] getData(){
        return mDataBuffer.getBytes(mDataBuffer.size());
    }

    public BasketballDataPackage.BallEventResult getBallEventResult(){
        return mBallResult;
    }
}
