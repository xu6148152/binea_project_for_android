package com.example.android.bluetoothchat;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by xubinggui on 7/22/15.
 */
public class FileUtil {

    public static final String TAG = FileUtil.class.getCanonicalName();

    public static final String filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "basket_basket";
    public static final String fileName = "b_temp_baseket.txt";

    public static File file = null;

    public static void createFile(){
        if(file != null && file.exists()){
            Log.d(TAG, "file delete " + file.delete());
        }
        file = new File(filePath, fileName);

        if(!file.exists()){
            try {
                Log.d(TAG, "create directory " + file.getParentFile().mkdirs());
                Log.d(TAG, " file create " + file.createNewFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void saveData(String str) {
        if(file == null) {
            return;
        }
        //try {
        //    PrintWriter pw = new PrintWriter(file);
        //    pw.println(str);
        //    pw.close();
        //} catch (Exception e) {
        //
        //}
        try {
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(str);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean renameFile(String newName) {
        File file = new File(filePath, fileName);
        return file.renameTo(new File(filePath, newName));
    }
}
