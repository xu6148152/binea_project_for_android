package com.binea.www.hostapp.core;

import dalvik.system.DexClassLoader;

/**
 * Created by xubinggui on 1/17/16.
 */
public class BundleDexClassLoader extends DexClassLoader {

    public BundleDexClassLoader(String dexPath, String optimizedDirectory, String libraryPath,
            ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
    }
}
