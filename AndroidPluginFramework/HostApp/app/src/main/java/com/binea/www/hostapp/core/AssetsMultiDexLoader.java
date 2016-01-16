package com.binea.www.hostapp.core;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xubinggui on 1/16/16.
 */
public class AssetsMultiDexLoader {
    private static final String TAG = "AssetsApkLoader";

    private static boolean installed = false;

    private static final int MAX_SUPPORTED_SDK_VERSION = 23;

    private static final int MIN_SDK_VERSION = 4;

    private static final Set<String> installedApk = new HashSet<String>();

    private AssetsMultiDexLoader() {

    }

    public static void install(Context context) {
        if (installed) {
            return;
        }

        try {
            clearOldDex(context);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        AssetsManager.copyAllAssetApk(context);

        try {
            final ApplicationInfo applicationInfo = getApplicationInfo(context);
            if (applicationInfo == null) {
                return;
            }

            synchronized (installedApk) {
                String path = applicationInfo.sourceDir;
                if (installedApk.contains(path)) {
                    return;
                }

                installedApk.add(path);

                ClassLoader classLoader;
                try {
                    classLoader = context.getClassLoader();
                } catch (RuntimeException e) {
                    return;
                }
                File dexDir = context.getDir(AssetsManager.APK_DIR, Context.MODE_PRIVATE);
                File[] files = dexDir.listFiles(new FilenameFilter() {
                    @Override public boolean accept(File dir, String filename) {
                        return filename.endsWith(AssetsManager.APK_FILTER);
                    }
                });
                ArrayList<File> fileList = new ArrayList<>();
                for(File file : files) {
                    fileList.add(file);
                }
                installBundleDex(classLoader, fileList, dexDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        installed = true;
    }

    private static ApplicationInfo getApplicationInfo(Context context)
            throws PackageManager.NameNotFoundException {
        PackageManager pm = null;
        String packageName = null;
        try {
            pm = context.getPackageManager();
            packageName = context.getPackageName();
        } catch (Exception e) {
            Log.d(TAG, "getApplicationInfo");
            e.printStackTrace();
        }

        if (pm == null || packageName == null) {
            return null;
        }
        ApplicationInfo info = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        return info;
    }

    private static void installBundleDex(ClassLoader classLoader, ArrayList<File> files, File dir)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException,
            NoSuchFieldException {
        if (files != null) {
            if (Build.VERSION.SDK_INT >= 19) {
                V19.install(classLoader, files, dir);
            } else if (Build.VERSION.SDK_INT >= 14) {

            }
        }
    }

    private static void clearOldDex(Context context) {
        File dexDir = context.getDir(AssetsManager.APK_DIR, Context.MODE_PRIVATE);
        if (dexDir.isDirectory()) {
            Log.i(TAG, "Clearing old secondary dex dir (" + dexDir.getPath()
                    + ").");
            File[] files = dexDir.listFiles();
            if (files == null) {
                Log.w(TAG, "Failed to list secondary dex dir content ("
                        + dexDir.getPath() + ").");
                return;
            }
            for (File oldFile : files) {
                Log.i(TAG, "Trying to delete old file " + oldFile.getPath()
                        + " of size " + oldFile.length());
                if (!oldFile.delete()) {
                    Log.w(TAG, "Failed to delete old file " + oldFile.getPath());
                } else {
                    Log.i(TAG, "Deleted old file " + oldFile.getPath());
                }
            }
            if (!dexDir.delete()) {
                Log.w(TAG,
                        "Failed to delete secondary dex dir "
                                + dexDir.getPath());
            } else {
                Log.i(TAG, "Deleted old secondary dex dir " + dexDir.getPath());
            }
        }
    }

    private static Field findField(Object object, String name) throws NoSuchFieldException {
        for (Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(name);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        throw new NoSuchFieldException("Field " + name + " not found in " + object.getClass());
    }

    private static void expandFieldArray(Object object, String fieldName, Object[] extraElement)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = findField(object, fieldName);
        Object[] original = (Object[]) field.get(object);
        Object[] combined = (Object[]) Array.newInstance(original.getClass().getComponentType(),
                original.length + extraElement.length);
        System.arraycopy(original, 0, combined, 0, original.length);
        System.arraycopy(extraElement, 0, combined, 0, extraElement.length);
        field.set(object, combined);
    }

    private static Method findMethod(Object object, String name, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        for(Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Method method = clazz.getDeclaredMethod(name, parameterTypes);
                if(!method.isAccessible()) {
                    method.setAccessible(true);
                }
                return method;
            }catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        throw new NoSuchMethodException("Method " + name + " not found in " + object.getClass());
    }


    private static final class V19 {
        private static void install(ClassLoader loader, ArrayList<File> files, File dir)
                throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException,
                InvocationTargetException {
            Field pathListField = findField(loader, "pathList");
            Object dexPathList = pathListField.get(loader);
            ArrayList<IOException> suppressedExceptions = new ArrayList<>();
            expandFieldArray(dexPathList, "dexElements", makeDexElements(dexPathList, files, dir, suppressedExceptions));
            if(suppressedExceptions.size() > 0) {
                for(IOException e : suppressedExceptions) {
                    Log.w(TAG, "Exception in makeDexElement", e);
                }
            }

            //Field field = findField(loader, "dexElementsSuppressedExceptions");
            //IOException[] dexElementsSuppressedExceptions = (IOException[]) field
            //        .get(loader);

            //if (dexElementsSuppressedExceptions == null) {
            //    dexElementsSuppressedExceptions = suppressedExceptions
            //            .toArray(new IOException[suppressedExceptions
            //                    .size()]);
            //} else {
            //    IOException[] combined = new IOException[suppressedExceptions
            //            .size() + dexElementsSuppressedExceptions.length];
            //    suppressedExceptions.toArray(combined);
            //    System.arraycopy(dexElementsSuppressedExceptions, 0,
            //            combined, suppressedExceptions.size(),
            //            dexElementsSuppressedExceptions.length);
            //    dexElementsSuppressedExceptions = combined;
            //}
            //
            //field.set(loader,
            //        dexElementsSuppressedExceptions);
        }

        private static Object[] makeDexElements(Object dexPathList, ArrayList<File> files, File dir, ArrayList<IOException> suppressExceptions)
                throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method method = findMethod(dexPathList, "makeDexElements", ArrayList.class, File.class, ArrayList.class);
            return (Object[]) method.invoke(null, files, dir, suppressExceptions);
        }
    }

    private static final class V14 {
        private static void install(ClassLoader loader, File[] files, File dir)
                throws NoSuchFieldException, IllegalAccessException {

        }
    }
}
