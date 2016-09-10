package com.zepp.www.binderdemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

//  Created by xubinggui on 9/9/16.
//                            _ooOoo_  
//                           o8888888o  
//                           88" . "88  
//                           (| -_- |)  
//                            O\ = /O  
//                        ____/`---'\____  
//                      .   ' \\| |// `.  
//                       / \\||| : |||// \  
//                     / _||||| -:- |||||- \  
//                       | | \\\ - /// | |  
//                     | \_| ''\---/'' | |  
//                      \ .-\__ `-` ___/-. /  
//                   ___`. .' /--.--\ `. . __  
//                ."" '< `.___\_<|>_/___.' >'"".  
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
//                 \ \ `-. \_ __\ /__ _/ .-` / /  
//         ======`-.____`-.___\_____/___.-`____.-'======  
//                            `=---='  
//  
//         .............................................  
//                  佛祖镇楼                  BUG辟易 
public class BookProvider extends ContentProvider {

    private static final String TAG = BookProvider.class.getCanonicalName();

    @Override public boolean onCreate() {
        Log.i(TAG, "onCreate, current thread:" + Thread.currentThread().getName());
        return false;
    }

    @Nullable @Override public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.i(TAG, "query, current thread: " + Thread.currentThread().getName());
        return null;
    }

    @Nullable @Override public String getType(Uri uri) {
        Log.i(TAG, "getType");
        return null;
    }

    @Nullable @Override public Uri insert(Uri uri, ContentValues values) {
        Log.i(TAG, "insert, current thread: " + Thread.currentThread().getName());
        return null;
    }

    @Override public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.i(TAG, "delete, current thread: " + Thread.currentThread().getName());
        return 0;
    }

    @Override public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.i(TAG, "update, current thread: " + Thread.currentThread().getName());
        return 0;
    }
}
