package com.example.android.model;

import com.example.android.utils.Byte2Hex;

/**
 * Created by xubinggui on 8/17/15.
 */
public class BallNameData {

    private DataBufferForList mDataBuffer;

    public BallNameData(String ballName){
        mDataBuffer = new DataBufferForList();
        final byte[] lengthBytes = Byte2Hex.int2ByteArray(ballName.length(), 2);
        mDataBuffer.append(lengthBytes);
        final byte[] ballNameRealBytes = ballName.getBytes();
        mDataBuffer.append(ballNameRealBytes);
    }

    public byte[] getData(){
        return mDataBuffer.getBytes(mDataBuffer.size());
    }
}
