package com.zepp.www.openglespractice.render;

import android.content.Context;
import android.opengl.GLSurfaceView;
import com.zepp.www.openglespractice.util.LogHelper;
import com.zepp.www.openglespractice.util.ShaderHelper;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

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
public abstract class BaseHockeyRender implements GLSurfaceView.Renderer {
    private static final int POSITION_COMPONENT_COUNT = 2;

    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;
    protected Context mContext;
    private static final String U_COLOR = "u_Color";
    private static final String A_COLOR = "a_Color";
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private static final String A_POSITOIN = "a_Position";

    protected int program;

    public BaseHockeyRender(Context context) {
        mContext = context;
        float[] tableVertices = {
                0f, 0f, 0f, 14f, 9f, 14f, 9f, 0f,
        };

        float[] tableVerticesWithTriangles = {
                ////triangle 1
                //-0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f,
                //
                ////triangle 2
                //-0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,

                // Order of coordinate X, Y, R, G, B
                // triangle fan
                // triangle1
                0, 0, 1f, 1f, 1f,
                //x2, y2
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                //x3, y3
                0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                //triangle2
                0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                //x5, y5
                -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                //x6, y6
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

                // Line1
                //x0, y0
                -0.5f, 0f, 1f, 0f, 0f,
                //x1, y1
                0.5f, 0f, 0f, 0f, 0f,

                //Mallets
                //x0, y0
                0f, -0.4f, 0f, 0f, 1f,
                //x1, y1
                0f, 0.4f, 1f, 0f, 0f,
        };

        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                               .order(ByteOrder.nativeOrder())
                               .asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);
    }

    @Override public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        String vertexShaderSource = readVertexShader();
        String fragmentShaderSource = readFragmentShader();

        int vertextShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        program = ShaderHelper.linkProgram(vertextShader, fragmentShader);
        if (LogHelper.enabled) {
            ShaderHelper.validateProgram(program);
        }
        glUseProgram(program);
        //uColorLocation = glGetUniformLocation(program, U_COLOR);
        int aColorLocation = glGetAttribLocation(program, A_COLOR);
        int aPositionLocation = glGetAttribLocation(program, A_POSITOIN);
        vertexData.position(0);
        //****** very import method *******//
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE,
                              vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        //****** very import method *******//
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE,
                              vertexData);
        glEnableVertexAttribArray(aColorLocation);
    }

    @Override public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        doSomeAfterClear();
        //set triangle color
        //glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        //draw two triangles
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        //set line color
        //glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);

        //draw the first blue mallets
        //glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);

        //draw the second red mallets
        //glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);
    }

    protected abstract void doSomeAfterClear();

    protected abstract String readVertexShader();

    protected abstract String readFragmentShader();

    protected abstract void getMatrixPosition();
}
