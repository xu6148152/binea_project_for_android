package com.android.tools.fd.runtime;

public final class Paths {
    public static final String BUILD_ID_TXT = "build-id.txt";
    public static final String DEVICE_TEMP_DIR = "/data/local/tmp";
    public static final String DEX_DIRECTORY_NAME = "dex";
    public static final String DEX_SLICE_PREFIX = "slice-";
    public static final String RELOAD_DEX_FILE_NAME = "classes.dex.3";
    public static final String RESOURCE_FILE_NAME = "resources.ap_";

    public static String getDataDirectory(String paramString) {
        return "/data/data/" + paramString + "/files/instant-run";
    }

    public static String getDeviceIdFolder(String paramString) {
        return "/data/local/tmp/" + paramString + "-" + "build-id.txt";
    }

    public static String getDexFileDirectory(String paramString) {
        return getDataDirectory(paramString) + "/" + "dex";
    }

    public static String getInboxDirectory(String paramString) {
        return getDataDirectory(paramString) + "/inbox";
    }

    public static String getMainApkDataDirectory(String paramString) {
        return "/data/data/" + paramString;
    }
}
