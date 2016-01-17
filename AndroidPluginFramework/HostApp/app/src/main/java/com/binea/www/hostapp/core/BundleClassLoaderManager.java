package com.binea.www.hostapp.core;

import android.content.Context;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用DexClassLoader来加载bundleapk中的类文件
 * <p>
 * A class loader that loads classes from .jar and .apk files containing a classes.dex entry. This
 * can be used to execute code not installed as part of an application.
 * </p>
 * Created by xubinggui on 1/17/16.
 */
public class BundleClassLoaderManager {
    public static List<DexClassLoader> bundleDexClassLoaderList = new ArrayList<>();

    public static void install(Context context) {
        AssetsManager.copyAllAssetApk(context);
        File file = context.getDir(AssetsManager.APK_DIR, Context.MODE_PRIVATE);
        File[] files = file.listFiles(new FilenameFilter() {
            @Override public boolean accept(File dir, String filename) {
                return filename.endsWith(AssetsManager.APK_FILTER);
            }
        });
        for (File f : files) {
            BundleDexClassLoader loader =
                    new BundleDexClassLoader(f.getAbsolutePath(), file.getAbsolutePath(), null,
                            context.getClassLoader());
            bundleDexClassLoaderList.add(loader);
        }
    }

    public static Class<?> loadClass(Context context, String className)
            throws ClassNotFoundException {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(className);
            if (clazz != null) {
                return clazz;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (DexClassLoader loader : bundleDexClassLoaderList) {
                Class<?> clazz = loader.loadClass(className);
                if (clazz != null) {
                    return clazz;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new ClassNotFoundException("class " + className + " not found");
    }
}
