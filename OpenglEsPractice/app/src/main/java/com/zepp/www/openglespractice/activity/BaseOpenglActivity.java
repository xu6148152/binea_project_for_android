package com.zepp.www.openglespractice.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by xubinggui on 4/24/16.
 * //                            _ooOoo_
 * //                           o8888888o
 * //                           88" . "88
 * //                           (| -_- |)
 * //                            O\ = /O
 * //                        ____/`---'\____
 * //                      .   ' \\| |// `.
 * //                       / \\||| : |||// \
 * //                     / _||||| -:- |||||- \
 * //                       | | \\\ - /// | |
 * //                     | \_| ''\---/'' | |
 * //                      \ .-\__ `-` ___/-. /
 * //                   ___`. .' /--.--\ `. . __
 * //                ."" '< `.___\_<|>_/___.' >'"".
 * //               | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * //                 \ \ `-. \_ __\ /__ _/ .-` / /
 * //         ======`-.____`-.___\_____/___.-`____.-'======
 * //                            `=---='
 * //
 * //         .............................................
 * //                  佛祖镇楼                  BUG辟易
 */
public abstract class BaseOpenglActivity extends AppCompatActivity {

    protected GLSurfaceView mGlSurfaceView;
    protected boolean mRenderSet = false;
    protected boolean mIsSupportedEs2 = false;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlSurfaceView = new GLSurfaceView(this);

        final ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo deviceConfigurationInfo = am.getDeviceConfigurationInfo();
        mIsSupportedEs2 =
                deviceConfigurationInfo.reqGlEsVersion >= 0x20000 || (Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                        && (Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith(
                        "unknow") || Build.MODEL.contains("google_sdk") || Build.MODEL.contains(
                        "Emulator") || Build.MODEL.contains("Android SDK built for x86")));
        mGlSurfaceView.setEGLContextClientVersion(2);
        if (mIsSupportedEs2) {
            setGlSurfaceViewRenderer();
            mRenderSet = true;
        } else {
            Toast.makeText(this, "This device does not support OpenGl ES 2.0", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        setContentView(mGlSurfaceView);
    }

    @Override protected void onResume() {
        super.onResume();
        if (mRenderSet) {
            mGlSurfaceView.onResume();
        }
    }

    @Override protected void onPause() {
        super.onPause();
        if (mRenderSet) {
            mGlSurfaceView.onPause();
        }
    }

    protected abstract void setGlSurfaceViewRenderer();
}
