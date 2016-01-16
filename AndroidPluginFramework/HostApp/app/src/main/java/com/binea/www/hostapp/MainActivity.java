package com.binea.www.hostapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.binea.www.hostapp.core.AssetsMultiDexLoader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv_hello = (TextView) findViewById(R.id.tv_hello);
        tv_hello.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                //loadApk();
            }
        });
        AssetsMultiDexLoader.install(getApplicationContext());
        loadClass();
    }

    @Override protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    private void loadClass() {
        try {
            Class<?> clazz = Class.forName("com.binea.www.bundleapp.FileUtil");
            Constructor<?> constructor = clazz.getConstructor();
            Object bundleUtil = constructor.newInstance();

            Method method = clazz.getMethod("showToast", Context.class, String.class);
            method.setAccessible(true);
            method.invoke(null, getApplicationContext(), "Hello");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void loadApk() {
        try {
            Class<?> clazz = getClassLoader()
                    .loadClass("com.binea.www.bundleapp.MathUtil");
            Constructor<?> constructor = clazz.getConstructor();
            Object bundleUtils = constructor.newInstance();

            Method printSumMethod = clazz.getMethod("sum", Context.class,
                    int.class, int.class);
            printSumMethod.setAccessible(true);
            Integer sum = (Integer)printSumMethod.invoke(bundleUtils,
                    getApplicationContext(), 10, 20);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
