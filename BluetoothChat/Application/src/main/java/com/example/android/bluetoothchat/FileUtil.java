package com.example.android.bluetoothchat;

import android.os.Environment;
import java.io.File;
import java.io.PrintWriter;

/**
 * Created by xubinggui on 7/22/15.
 */
public class FileUtil {
    public static final String filePath = Environment.getExternalStorageDirectory().toString();
    public static final String fileName = "baseket.txt";

    public static void saveData(String str) {
        try {
            File file = new File(filePath, fileName);
            PrintWriter pw = new PrintWriter(file);
            pw.println(str);
            pw.close();
        } catch (Exception e) {

        }
    }
}
