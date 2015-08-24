package com.example.android.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Byte2Hex {

    public static final String TAG = Byte2Hex.class.getCanonicalName();
    public static final char[] HEXCODE = "0123456789ABCDEF".toCharArray();

    public static final String RAWDATACOMMAND = "7E5CA1FF380100000000000000020000000000000000000000E72401017E";

    public static final byte Z = 0x5A;
    public static final byte E = 0x45;
    public static final byte P = 0x50;

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
            bytes[i] = (byte) (value >> j & 0xff);
            j += 8;
        }
        return bytes;
    }

    public static byte[] float2ByteArray (float value)
    {
        return ByteBuffer.allocate(4).putFloat(value).array();
    }

    public static byte[] int2ByteArray(int value, int capacity){
        //return ByteBuffer.allocate(4).putInt(value).order(ByteOrder.LITTLE_ENDIAN).array();
        byte[] bytes = new byte[capacity];
        int j = 0;
        for(int i = 0;i<capacity;i++){
            bytes[i] = (byte) (value >> j & 0xff);
            j += 8;
        }
        return bytes;
    }

    public static byte[] short2ByteArray(short value){
        return ByteBuffer.allocate(2).putShort(value).order(ByteOrder.LITTLE_ENDIAN).array();
    }

    public static int bytesArray2int(byte[] bytes){
        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        return byteBuffer.getInt();
    }

    public static short bytesArray2Short(byte[] bytes){
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public static long bytesArray2long(byte[] bytes){
        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        return byteBuffer.getLong();
    }

    public static float bytes2float(byte[] bytes){
        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        return byteBuffer.getFloat();
    }

    public static byte[] float2bytes(float value){
        int bits = Float.floatToIntBits(value);
        byte[] bytes = new byte[4];
        bytes[0] = (byte)(bits & 0xff);
        bytes[1] = (byte)((bits >> 8) & 0xff);
        bytes[2] = (byte)((bits >> 16) & 0xff);
        bytes[3] = (byte)((bits >> 24) & 0xff);
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

    public static boolean isBasketball(byte[] scanRecord) {
        if (scanRecord == null || scanRecord.length < 31) return false;
        if (scanRecord[0] == 0x02 && scanRecord[1] == 0x01 && scanRecord[2] == 0x06) {
            int len = scanRecord[3];
            int indexZ = 3 + 1 + (int)scanRecord[3] + 1 + 1 + 6 + 1 + 1;
            if (scanRecord[indexZ] == Z && scanRecord[indexZ + 1] == E
                    && scanRecord[indexZ + 2] == P && scanRecord[indexZ + 3] == P) {
                return true;
            }
        }
        return false;
    }

    //public static List<UUID> parseUUIDs(final byte[] advertisedData) {
    //    List<UUID> uuids = new ArrayList<UUID>();
    //
    //    int offset = 0;
    //    while (offset < (advertisedData.length - 2)) {
    //        int len = advertisedData[offset++];
    //        if (len == 0)
    //            break;
    //
    //        int type = advertisedData[offset++];
    //        switch (type) {
    //            case 0x02: // Partial list of 16-bit UUIDs
    //            case 0x03: // Complete list of 16-bit UUIDs
    //                while (len > 1) {
    //                    int uuid16 = advertisedData[offset++];
    //                    uuid16 += (advertisedData[offset++] << 8);
    //                    len -= 2;
    //                    uuids.add(UUID.fromString(String.format(
    //                            "%08x-0000-1000-8000-00805f9b34fb", uuid16)));
    //                }
    //                break;
    //            case 0x06:// Partial list of 128-bit UUIDs
    //            case 0x07:// Complete list of 128-bit UUIDs
    //                // Loop through the advertised 128-bit UUID's.
    //                while (len >= 16) {
    //                    try {
    //                        // Wrap the advertised bits and order them.
    //                        ByteBuffer buffer = ByteBuffer.wrap(advertisedData,
    //                                offset++, 16).order(ByteOrder.LITTLE_ENDIAN);
    //                        long mostSignificantBit = buffer.getLong();
    //                        long leastSignificantBit = buffer.getLong();
    //                        uuids.add(new UUID(leastSignificantBit,
    //                                mostSignificantBit));
    //                    } catch (IndexOutOfBoundsException e) {
    //                        // Defensive programming.
    //                        Log.e(TAG, e.toString());
    //                        continue;
    //                    } finally {
    //                        // Move the offset to read the next uuid.
    //                        offset += 15;
    //                        len -= 16;
    //                    }
    //                }
    //                break;
    //            default:
    //                offset += (len - 1);
    //                break;
    //        }
    //    }
    //
    //    return uuids;
    //}

    public static List<UUID> parseUUIDs(byte[] advertisedData) {
        List<UUID> uuids = new ArrayList<UUID>();

        ByteBuffer buffer = ByteBuffer.wrap(advertisedData).order(ByteOrder.LITTLE_ENDIAN);
        while (buffer.remaining() > 2) {
            byte length = buffer.get();
            if (length == 0) break;

            byte type = buffer.get();
            switch (type) {
                case 0x02: // Partial list of 16-bit UUIDs
                case 0x03: // Complete list of 16-bit UUIDs
                    while (length >= 2) {
                        uuids.add(UUID.fromString(String.format("%08x-0000-1000-8000-00805f9b34fb",
                                buffer.getShort())));
                        length -= 2;
                    }
                    break;

                case 0x06: // Partial list of 128-bit UUIDs
                case 0x07: // Complete list of 128-bit UUIDs
                    while (length >= 16) {
                        long lsb = buffer.getLong();
                        long msb = buffer.getLong();
                        uuids.add(new UUID(msb, lsb));
                        length -= 16;
                    }
                    break;

                default:
                    buffer.position(buffer.position() + length - 1);
                    break;
            }
        }

        return uuids;
    }


}
