package com.zepp.www.openglespractice.render;

import android.content.Context;
import android.opengl.GLSurfaceView;
import com.zepp.www.openglespractice.R;
import com.zepp.www.openglespractice.objects.Mallet;
import com.zepp.www.openglespractice.objects.Table;
import com.zepp.www.openglespractice.programs.ColorShaderProgram;
import com.zepp.www.openglespractice.programs.TextureShaderProgram;
import com.zepp.www.openglespractice.util.MatrixHelper;
import com.zepp.www.openglespractice.util.TextureHelper;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

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
public class AirHockeyTexturedRender implements GLSurfaceView.Renderer {
    private Context mContext;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private Table table;
    private Mallet mallet;

    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;

    private int texture;

    private int secondTexture;

    public AirHockeyTexturedRender(Context context) {
        this.mContext = context;
    }

    @Override public void onSurfaceCreated(
            GL10 gl, EGLConfig config) {
        glClearColor(0f, 0f, 0f, 0f);

        table = new Table();
        mallet = new Mallet();

        textureProgram = new TextureShaderProgram(mContext, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);
        colorProgram = new ColorShaderProgram(mContext, R.raw.simple_vertex_shader_texture, R.raw.simple_fragment_shader_texture);

        texture = TextureHelper.loadTexture(mContext, R.drawable.air_hockey_surface);

        secondTexture = TextureHelper.loadTexture(mContext, R.drawable.air_hockey_surface_low_res);
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

    @Override public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        //Draw the table
        textureProgram.useProgram();
        textureProgram.setUniforms(projectionMatrix, texture, GL_TEXTURE0);
        table.bindData(textureProgram);
        table.draw();

        //Draw the mallet
        colorProgram.useProgram();
        colorProgram.setUniforms(projectionMatrix);
        mallet.bindData(colorProgram);
        mallet.draw();
    }
}
