package com.zepp.www.openglespractice.util;

import android.util.Log;

/**
 * Created by xubinggui on 4/26/16.
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
public class LogHelper {
    public static final boolean enabled = true;

    public static void debug(Object object, String msg) {
        d(object.getClass().getCanonicalName(), msg);
    }

    public static void verbose(Object object, String msg) {
        v(object.getClass().getCanonicalName(), msg);
    }

    public static void warn(Object object, String msg) {
        w(object.getClass().getCanonicalName(), msg);
    }

    public static void info(Object object, String msg) {
        i(object.getClass().getCanonicalName(), msg);
    }

    public static void error(Object object, String msg) {
        e(object.getClass().getCanonicalName(), msg);
    }

    public static void d(String tag, String msg) {
        if (enabled) {
            Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (enabled) {
            Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (enabled) {
            Log.w(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (enabled) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (enabled) {
            Log.e(tag, msg);
        }
    }
}
