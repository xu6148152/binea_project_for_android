package com.zepp.www.openglespractice.render;

import android.content.Context;
import android.opengl.GLES20;
import com.zepp.www.openglespractice.R;
import com.zepp.www.openglespractice.util.TextureResourceReader;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.Matrix.orthoM;

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
public class AirHockeyOrthoRender extends BaseHockeyRender {

    private static final String U_MATRIX = "u_Matrix";

    private final float[] projectionMatrix = new float[16];

    private int uMatrixLocation;

    public AirHockeyOrthoRender(Context context) {
        super(context);
    }

    @Override public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        final float aspectRatio =
                width > height ? (float) width / (float) height : (float) height / (float) width;
        if (width > height) {
            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
    }

    protected void doSomeAfterClear() {
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
    }

    @Override protected String readVertexShader() {
        return TextureResourceReader.readTextFileFromResource(mContext,
                                                              R.raw.simple_vertex_shader_ortho);
    }

    @Override protected String readFragmentShader() {
        return TextureResourceReader.readTextFileFromResource(mContext,
                                                              R.raw.simple_fragment_shader);
    }

    @Override protected void getMatrixPosition() {
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
    }
}
