package com.zepp.www.openglespractice.render;

import android.content.Context;
import com.zepp.www.openglespractice.util.MatrixHelper;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.*;

/**
 * Created by xubinggui on 5/2/16.
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
public class AirHockey3DRender extends AirHockeyOrthoRender {

    static {
        POSITION_COMPONENT_COUNT = 4;
        STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    }

    private final float[] modelMatrix = new float[16];

    public AirHockey3DRender(Context context) {
        super(context);
    }

    @Override public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(
                projectionMatrix, 45, (float)width / (float) height, 1f, 10f);

        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        rotateM(modelMatrix, 0, 60f, 1f, 0f, 0f);

        final float[] temp = new float[16];
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);

    }

    @Override protected void doSomeAfterClear() {
        super.doSomeAfterClear();
    }

    @Override protected String readVertexShader() {
        return super.readVertexShader();
    }

    @Override protected String readFragmentShader() {
        return super.readFragmentShader();
    }

    @Override protected void getMatrixPosition() {
        super.getMatrixPosition();
    }

    @Override protected float[] getTableVertex() {
        return new float[] {
                ////triangle 1
                //-0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f,
                //
                ////triangle 2
                //-0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,

                // Order of coordinate X, Y, Z, W, R, G, B
                // triangle fan
                // triangle1
                0f, 0f, 0f, 1.5f, 1f, 1f, 1f,
                //x2, y2
                -0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,
                //x3, y3
                0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,
                //triangle2
                0.5f, 0.8f, 0f, 2f, 0.7f, 0.7f, 0.7f,
                //x5, y5
                -0.5f, 0.8f, 0f, 2f, 0.7f, 0.7f, 0.7f,
                //x6, y6
                -0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,

                // Line1
                //x0, y0
                -0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,
                //x1, y1
                0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,

                //Mallets
                //x0, y0
                0f, -0.4f, 0f, 1.25f, 0f, 0f, 1f,
                //x1, y1
                0f, 0.4f, 0f, 1.75f, 1f, 0f, 0f,
        };
    }
}
