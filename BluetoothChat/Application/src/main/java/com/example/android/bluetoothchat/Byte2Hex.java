package com.example.android.bluetoothchat;

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

}
