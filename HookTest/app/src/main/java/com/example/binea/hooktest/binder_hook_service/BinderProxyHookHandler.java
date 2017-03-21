package com.example.binea.hooktest.binder_hook_service;

import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// Created by binea on 3/17/17.

public class BinderProxyHookHandler implements InvocationHandler {

    private static final String TAG = BinderProxyHookHandler.class.getCanonicalName();

    IBinder mBase;
    Class<?> mStub;
    Class<?> mInterface;

    public BinderProxyHookHandler(IBinder base) {
        mBase = base;
        try {
            this.mStub = Class.forName("android.content.IClipboard$Stub");
            this.mInterface = Class.forName("android.content.IClipboard");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("queryLocalInterface".equals(method.getName())) {
            Log.d(TAG, "hook queryLocalInterface");

            return Proxy.newProxyInstance(proxy.getClass().getClassLoader(),
                                          new Class[]{IBinder.class, IInterface.class, this.mInterface},
                                          new BinderHookHandler(mBase, mStub));
        }

        Log.d(TAG, "method: " + method.getName());
        return method.invoke(mBase, args);
    }
}
