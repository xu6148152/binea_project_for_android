package com.binea.www.hostapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.binea.www.hostapp.core.dexclassloader.BundleClassLoaderManager;
import com.binea.www.hostapp.core.dlresource.BundlerResourceLoader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    Resources customRs;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(customRs != null) {
            View layout = LayoutInflater.from(this).inflate(0x7f04001a, null);
            setContentView(layout);
            return;
        }
        setContentView(R.layout.activity_main);
        //AssetsMultiDexLoader.install(getApplicationContext());
        //BundleClassLoaderManager.install(getApplicationContext());

        TextView tv_hello = (TextView) findViewById(R.id.tv_hello);
        tv_hello.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                loadApk();
            }
        });
        ImageView iv_image = (ImageView) findViewById(R.id.iv_image);
        //int resId = customRs.getIdentifier("tagevent_soccer_shot", "mipmap", "com.binea.www.bundleapp.apk");
        //int resId = customRs.getIdentifier("dl", "string", "com.binea.www.bundleapp.R");
        if(customRs != null) {
            String str = customRs.getString(0x7f060018);
            tv_hello.setText(str);
            Drawable drawable = customRs.getDrawable(0x7f030001);
            iv_image.setImageDrawable(drawable);
        }
        //loadClass();
    }

    @Override protected void attachBaseContext(Context newBase) {
        replaceContextResources(newBase);
        super.attachBaseContext(newBase);
    }

    private void replaceContextResources(Context context) {
        try{
            Field field = context.getClass().getDeclaredField("mResources");
            field.setAccessible(true);
            if(customRs == null) {
                customRs = BundlerResourceLoader.getBundleResource(context);
            }
            field.set(context, customRs);
        }catch (Exception e) {
            e.printStackTrace();
        }
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
            Class<?> clazz = BundleClassLoaderManager.loadClass(getApplicationContext(), "com.binea.www.bundleapp.MathUtil");
            Constructor<?> constructor = clazz.getConstructor();
            Object object = constructor.newInstance();

            Method method = clazz.getMethod("sum", Context.class, int.class, int.class);
            method.setAccessible(true);
            method.invoke(object, getApplicationContext(), 10, 20);
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
