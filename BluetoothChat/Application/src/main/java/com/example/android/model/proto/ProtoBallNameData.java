package com.example.android.model.proto;

import com.example.android.model.DataBufferForList;
import com.example.android.utils.Byte2Hex;
import com.example.zprotocol.BasketballDataPackage;

/**
 * Created by xubinggui on 8/17/15.
 */
public class ProtoBallNameData {

    private DataBufferForList mDataBuffer;

    private BasketballDataPackage.BallEventBallState mBallState;

    public ProtoBallNameData(String ballName){
        mBallState = new BasketballDataPackage.BallEventBallState();
        mBallState.ballNameLength = Byte2Hex.int2ByteArray(ballName.length(), 2);
        mBallState.ballNameString = ballName.getBytes();
    }

    public byte[] getData(){
        return mDataBuffer.getBytes(mDataBuffer.size());
    }

    public BasketballDataPackage.BallEventBallState getBallState(){
        return mBallState;
    }
}
