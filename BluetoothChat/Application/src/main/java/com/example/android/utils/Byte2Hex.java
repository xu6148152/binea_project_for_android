package com.example.android.utils;

import java.nio.ByteBuffer;

public class Byte2Hex {

    public static final char[] HEXCODE = "0123456789ABCDEF".toCharArray();

    public static final String RAWDATACOMMAND = "7E5CA1FF380100000000000000020000000000000000000000E72401017E";

    public static String bytesToHex(byte[] bytes, String delimiter) {
        StringBuilder r = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            r.append(HEXCODE[(b >> 4) & 0xF]);
            r.append(HEXCODE[(b & 0xF)]);
            r.append(delimiter);
        }
        return r.toString();
    }

    public static String bytesToHex(byte[] bytes, String delimiter, boolean appendDelimiterAtEnd) {
        if (bytes == null) return null;
        StringBuilder r = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            r.append(HEXCODE[(b >> 4) & 0xF]);
            r.append(HEXCODE[(b & 0xF)]);
            if (i != bytes.length - 1 || appendDelimiterAtEnd) r.append(delimiter);
        }
        return r.toString();
    }

    public static String bytesToHex(byte[] bytes) {
        return bytesToHex(bytes, "");
    }

    public static byte[] hexToBytes(String hex) {
        hex = hex.replace(" ", "");
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }

        byte[] bytes = new byte[hex.length() / 2];

        for (int i = 0; i < hex.length(); i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(
                    hex.charAt(i + 1), 16));
        }

        return bytes;
    }

    public static String convert2byte(byte[] bytes){
        byte[] accemerometers = new byte[600];//x y z short * 100
        byte[] gyro = new byte[600];//x y z short * 100
        byte[] magnetometer = new byte[120];//x y z short * 20

        System.arraycopy(bytes, 0, accemerometers, 0, accemerometers.length);
        System.arraycopy(bytes, accemerometers.length, gyro, 0, gyro.length);
        System.arraycopy(bytes, accemerometers.length * 2, magnetometer, 0, magnetometer.length);
        StringBuilder sb = new StringBuilder();
        int k = 0;
        for(int i = 0;i<600;i = i + 6){
            //accemerometers x y z
            //byte xLow = accemerometers[i];
            //byte xHigh = accemerometers[i + 1];
            //final int accemerometersX = convert2short(xLow, xHigh);
            //sb.append(accemerometersX + " ");
            //byte ylow = accemerometers[i + 2];
            //byte yHigh = accemerometers[i + 3];
            //final int accemerometersY = convert2short(ylow, yHigh);
            //sb.append(accemerometersY + " ");
            //byte zLow = accemerometers[i + 4];
            //byte zHigh = accemerometers[i + 5];
            //final int accemerometersZ = convert2short(zLow, zHigh);
            //sb.append(accemerometersZ + " ");
            loopXYZ(sb, accemerometers, i, TYPE.ACCEMEROMETERS);
            loopXYZ(sb, gyro, i, TYPE.GYRO);
            if(i != 0 && i%30 == 0){
                k = k + 6;
            }
            loopXYZ(sb ,magnetometer, k, TYPE.COMPASS);
            //gyro x y z

            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    private static int convert2short(byte low, byte high){
        return high + low * 256;
    }

    private static void loopXYZ(StringBuilder sb, byte[] bytes, int i, TYPE type){
        double tmpData = 0;
        byte xLow = bytes[i];
        byte xHigh = bytes[i + 1];
        final int accemerometersX = convert2short(xLow, xHigh);
        tmpData = transferRawDataToNormal(accemerometersX, type);
        sb.append(tmpData + "\t");
        byte ylow = bytes[i + 2];
        byte yHigh = bytes[i + 3];
        final int accemerometersY = convert2short(ylow, yHigh);
        tmpData = transferRawDataToNormal(accemerometersY, type);
        sb.append(tmpData + "\t");
        byte zLow = bytes[i + 4];
        byte zHigh = bytes[i + 5];
        final int accemerometersZ = convert2short(zLow, zHigh);
        tmpData = transferRawDataToNormal(accemerometersZ, type);
        sb.append(tmpData + "\t");
    }

    private static double transferRawDataToNormal(int accemerometersX, TYPE type){
        double tmpData;
        if(type == TYPE.ACCEMEROMETERS){
            tmpData = accemerometersX / 32767.0f * 16 * 9.8;
        }else if(type == TYPE.GYRO){
            tmpData = accemerometersX / 32767.0f * 2000;
        }else{
            tmpData = accemerometersX / 4096.0f * 0.88;
        }
        return tmpData;
    }

    enum TYPE{
        ACCEMEROMETERS,
        GYRO,
        COMPASS
    }

    public static byte[] long2ByteArray (long value, int capacity)
    {
        //return ByteBuffer.allocate(capacity).putLong(value).array();
        byte[] bytes = new byte[capacity];
        int j = 0;
        for(int i = 0;i<capacity;i++){
            bytes[capacity - i - 1] = (byte) (value >> j & 0xff);
            j += 8;
        }
        return bytes;
    }

    public static byte[] float2ByteArray (float value)
    {
        return ByteBuffer.allocate(4).putFloat(value).array();
    }

    public static byte[] int2ByteArray(int value, int capacity){
        byte[] bytes = new byte[capacity];
        int j = 0;
        for(int i = 0;i<capacity;i++){
            bytes[capacity - i - 1] = (byte) (value >> j & 0xff);
            j += 8;
        }
        return bytes;
    }

    public static int crc16(final byte[] buffer) {
        int crc = 0xFFFF;

        for (int j = 0; j < buffer.length ; j++) {
            crc = ((crc  >>> 8) | (crc  << 8) )& 0xffff;
            crc ^= (buffer[j] & 0xff);//byte to int, trunc sign
            crc ^= ((crc & 0xff) >> 4);
            crc ^= (crc << 12) & 0xffff;
            crc ^= ((crc & 0xFF) << 5) & 0xffff;
        }
        crc &= 0xffff;
        return crc;

    }

}
