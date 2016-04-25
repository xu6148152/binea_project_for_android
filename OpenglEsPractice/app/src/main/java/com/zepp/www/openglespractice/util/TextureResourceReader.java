package com.zepp.www.openglespractice.util;

import android.content.Context;
import android.content.res.Resources;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by xubinggui on 4/25/16.
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
public class TextureResourceReader {
    public static String readTextFileFromResource(Context context, int resId) {
        StringBuilder sb = new StringBuilder();

        try {
            InputStream is = context.getResources().openRawResource(resId);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String nextLine;

            while ((nextLine = br.readLine()) != null) {
                sb.append(nextLine);
                sb.append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not open resource: " + resId, e);
        } catch (Resources.NotFoundException e) {
            throw new RuntimeException("Resource not found: " + resId, e);
        }
        return sb.toString();
    }
}
