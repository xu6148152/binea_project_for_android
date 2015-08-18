package com.example.android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xubinggui on 8/18/15.
 */
public class DataBufferForList {
    List<Byte> mDataByteLists;

    public DataBufferForList(){
        mDataByteLists = new ArrayList<>();
    }

    public void append(byte[] bytes){
        for(int i = 0;i<bytes.length;i++){
            append(bytes[i]);
        }
    }

    public void append(byte aByte){
        mDataByteLists.add(aByte);
    }

    public int size(){
        return mDataByteLists.size();
    }

    public byte[] getBytes(int length){
        byte[] bytes = new byte[length];
        for(int i = 0;i<length;i++){
            bytes[i] = mDataByteLists.get(i);
        }
        return bytes;
    }


}
