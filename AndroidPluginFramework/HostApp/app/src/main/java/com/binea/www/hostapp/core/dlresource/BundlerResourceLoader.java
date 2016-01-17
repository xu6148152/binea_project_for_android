package com.binea.www.hostapp.core.dlresource;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import com.binea.www.hostapp.core.AssetsManager;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xubinggui on 1/17/16.
 */
public class BundlerResourceLoader {
    public static AssetManager createAssetManager(String path) {
        try {
            AssetManager am = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.invoke(am, path);
            return am;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Resources getBundleResource(Context context) {
        AssetsManager.copyAllAssetApk(context);
        File file = context.getDir(AssetsManager.APK_DIR, Context.MODE_PRIVATE);

        File[] files = file.listFiles(new FilenameFilter() {
            @Override public boolean accept(File dir, String filename) {
                return filename.endsWith(AssetsManager.APK_FILTER);
            }
        });

        String path = file.getAbsolutePath() + File.separator + "bundle.apk";
        AssetManager am = createAssetManager(path);
        return new Resources(am, context.getResources().getDisplayMetrics(),
                context.getResources().getConfiguration());
    }
}
