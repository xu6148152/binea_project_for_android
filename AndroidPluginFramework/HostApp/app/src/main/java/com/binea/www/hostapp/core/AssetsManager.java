package com.binea.www.hostapp.core;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by xubinggui on 1/16/16.
 */
public class AssetsManager {
    public static final String TAG = "AssetsApkLoader";

    public static final String APK_DIR = "bundle_apk";

    public static final String APK_FILTER = ".apk";

    public static void copyAllAssetApk(Context context) {
        AssetManager am = context.getAssets();
        InputStream is = null;
        OutputStream os = null;
        try {
            final File file = context.getDir(APK_DIR, Context.MODE_PRIVATE);
            file.mkdir();
            String[] filenames = am.list("");
            for(String name : filenames) {
                if(!name.endsWith(APK_FILTER)) {
                    continue;
                }

                is = am.open(name);
                File f = new File(file, name);
                if(f.exists() && f.length() == is.available()) {
                    return;
                }

                os = new FileOutputStream(f);
                byte[] buffer = new byte[1024];
                int read = 0;
                while((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }

                os.flush();
            }
        }catch (Exception e) {

        }finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
