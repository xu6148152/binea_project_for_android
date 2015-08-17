package com.example.android.model;

import android.util.Log;
import com.example.android.utils.Byte2Hex;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by xubinggui on 8/17/15.
 */
public class DataBuffer {
    private static final String TAG = DataBuffer.class.getSimpleName();

    private static int MAXSIZE = 1 * 1024;

    public ByteBuffer buffer;
    boolean isReading;

    private int currentLength = 0;

    public DataBuffer() {
        buffer = ByteBuffer.allocate(MAXSIZE).order(ByteOrder.LITTLE_ENDIAN);
        isReading = false;
    }

    public DataBuffer(int maxsize) {
        buffer = ByteBuffer.allocate(maxsize).order(ByteOrder.LITTLE_ENDIAN);
        isReading = false;
    }

    private void toReading() {
        if (!isReading) {
            buffer.flip();
            isReading = true;
        }
    }

    private void toWriting() {
        if (isReading) {
            buffer.compact();
            isReading = false;
        }
    }

    public int size() {
        if (isReading) {
            Log.d(TAG, "[size]reading  = " + buffer.remaining());
            return buffer.remaining();
        } else {
            Log.d(TAG, "[size]not reading  = " + buffer.position());
            return buffer.position();
        }
    }

    public short getShort(int index) {
        return buffer.getShort(index);
    }

    public byte getByte(int index) {
        return buffer.get(index);
    }

    public byte[] consumeBytes(int length) {
        toReading();

        if (buffer.remaining() == 0) {
            return null;
        }

        byte[] bytes;
        if (length > buffer.remaining()) {
            bytes = new byte[buffer.remaining()];
        } else {
            bytes = new byte[length];
        }

        buffer.get(bytes);
        return bytes;
    }

    public String toHexString(String delimiter) {
        toReading();
        StringBuilder r = new StringBuilder(buffer.limit() * 2);

        for (int i = 0; i < buffer.limit(); i++) {
            byte b = buffer.get(i);
            r.append(Byte2Hex.HEXCODE[(b >> 4) & 0xF]);
            r.append(Byte2Hex.HEXCODE[(b & 0xF)]);
            r.append(delimiter);
        }

        return r.toString();
    }

    public boolean append(byte[] bytes, int srcOffset, int length) {
        toWriting();
        if (buffer.remaining() < length) {
            // LogUtil.printStackTrace(this, TAG, TAG);
            Log.d(TAG, buffer.remaining() + " === remaining,,,,append(),,,, len ==== " + length);
            buffer.clear();
            // exception
            return false;
        }
        buffer.put(bytes, srcOffset, length);
        return true;
    }

    public void append(byte[] bytes) {
        append(bytes, 0, bytes.length);
        currentLength += bytes.length;
    }

    public boolean append(byte aByte){
        if(buffer.remaining() > 0){
            buffer.put(aByte);
            currentLength++;
            return true;
        }
        return false;
    }

    public void clear() {
        buffer.clear();
        isReading = false;
    }
}
