package com.zepp.www.flatbufferdemo.utils;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xubinggui on 7/9/16.
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
public class Utils {
    public static byte[] readRawResource(Context context, int resId) {
        InputStream stream = null;
        byte[] buffer = null;
        try {
            stream = context.getResources().openRawResource(resId);
            buffer = new byte[stream.available()];
            while (stream.read(buffer) != -1) ;
        } catch (IOException e) {
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
        return buffer;
    }
}
