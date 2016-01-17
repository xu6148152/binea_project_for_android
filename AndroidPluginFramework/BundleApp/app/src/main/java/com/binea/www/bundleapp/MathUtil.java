package com.binea.www.bundleapp;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by xubinggui on 1/16/16.
 */
public class MathUtil {
    public void sum(Context context, int a, int b){
        Toast.makeText(context, String.valueOf(a + b), Toast.LENGTH_LONG).show();
    }
}
