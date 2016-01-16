package com.binea.www.bundleapp;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by xubinggui on 1/16/16.
 */
public class FileUtil {
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
