package com.example.android.utils;

import java.util.regex.Pattern;

/**
 * Created by xubinggui on 8/19/15.
 */
public class RegexUtil {

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public static boolean isValidIp(String ip){
        return Pattern.compile(IPADDRESS_PATTERN).matcher(ip).matches();
    }
}
