package com.example.binea.hooktest.proxy;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// Created by binea on 3/17/17.

public class InstrumentationProxy extends Instrumentation {

    private final String TAG = InstrumentationProxy.class.getCanonicalName();

    Instrumentation mBase;

    public InstrumentationProxy(Instrumentation base) {
        mBase = base;
    }

    public ActivityResult execStartActivity(Context who,
                                            IBinder contextThread,
                                            IBinder token,
                                            Activity target,
                                            Intent intent,
                                            int requestCode,
                                            Bundle options) {
        Log.d(TAG,
              "params are: context = [" + who + "],\ncontextThread = [" + contextThread + "], \ntoken = [" + token + "\ntarget = [" + target + "], \nintent = [" + intent + "], \nrequestCode = [" + requestCode + "], \noptions = [" + options + "]");
        //call real start activity
        try {
            Method execStartActivity = Instrumentation.class.getDeclaredMethod("execStartActivity",
                                                                               Context.class,
                                                                               IBinder.class,
                                                                               IBinder.class,
                                                                               Activity.class,
                                                                               Intent.class,
                                                                               int.class,
                                                                               Bundle.class);
            execStartActivity.setAccessible(true);
            return (ActivityResult) execStartActivity.invoke(mBase,
                                                             who,
                                                             contextThread,
                                                             token,
                                                             target,
                                                             intent,
                                                             requestCode,
                                                             options);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("fuck god, someone modify incorrect");
    }
}
