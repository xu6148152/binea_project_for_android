package com.zepp.www.openglespractice.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static android.opengl.GLES20.*;
import static android.opengl.GLUtils.*;

/**
 * Created by xubinggui on 5/3/16.
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
public class TextureHelper {
    private static final String TAG = TextureHelper.class.getCanonicalName();

    public static int loadTexture(Context context, int resourceId) {
        final int[] textureObjectIds = new int[1];
        //生成纹理
        glGenTextures(1, textureObjectIds, 0);

        if (textureObjectIds[0] == 0) {
            LogHelper.w(TAG, "Counld not generate a new Opengl texture object.");
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap =
                BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        if (bitmap == null) {
            LogHelper.w(TAG, "Resource ID " + resourceId + " could not be decoded.");

            glDeleteTextures(1, textureObjectIds, 0);

            return 0;
        }

        //纹理绑定
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

        //纹理过滤
        //最近邻纹理过滤， 双线性纹理过滤，MIP贴图，三线性过滤
        //缩小使用三线性过滤
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        //放大使用双线性过滤
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        //加载纹理到opengl
        //释放位图信息比较耗时
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();

        //生成MIP贴图
        glGenerateMipmap(GL_TEXTURE_2D);

        //接触纹理绑定
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }
}
