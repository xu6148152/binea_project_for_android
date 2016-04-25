package com.zepp.www.openglespractice.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import com.zepp.www.openglespractice.R;
import com.zepp.www.openglespractice.util.TextureResourceReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

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
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final int POSITION_COMPOENT_COUNT = 2;

    float[] tableVerticesWithTriangles = {
            //triangle 1
            0f, 0f, 9f, 14f, 0f, 14f,

            //triangle 2
            0f, 0f, 9f, 0f, 9f, 14f,

            // Line1
            0f, 7f, 9f, 7f,

            //Mallets
            4.5f, 2f, 4.5f, 12f,
    };

    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;
    private Context mContext;

    public AirHockeyRenderer(Context context) {
        mContext = context;
        float[] tableVertices = {
                0f, 0f, 0f, 14f, 9f, 14f, 9f, 0f,
        };

        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
    }

    @Override public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        String vertexShaderSource = TextureResourceReader.readTextFileFromResource(mContext,
                R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextureResourceReader.readTextFileFromResource(mContext,
                R.raw.simple_fragment_shader);
    }

    @Override public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override public void onDrawFrame(GL10 gl) {

    }
}
