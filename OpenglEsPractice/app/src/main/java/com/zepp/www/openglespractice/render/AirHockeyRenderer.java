package com.zepp.www.openglespractice.render;

import android.content.Context;
import android.opengl.GLSurfaceView;
import com.zepp.www.openglespractice.R;
import com.zepp.www.openglespractice.util.LogHelper;
import com.zepp.www.openglespractice.util.ShaderHelper;
import com.zepp.www.openglespractice.util.TextureResourceReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

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

    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;
    private Context mContext;
    private static final String U_COLOR = "u_Color";
    private int uColorLocation;

    private static final String A_POSITOIN = "a_Position";

    public AirHockeyRenderer(Context context) {
        mContext = context;
        float[] tableVertices = {
                0f, 0f, 0f, 14f, 9f, 14f, 9f, 0f,
        };

        float[] tableVerticesWithTriangles = {
                //triangle 1
                -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f,

                //triangle 2
                -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,

                // Line1
                -0.5f, 0f, 0.5f, 0f,

                //Mallets
                0f, -0.25f, 0f, 0.25f,
        };

        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                               .order(ByteOrder.nativeOrder())
                               .asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);
    }

    @Override public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        String vertexShaderSource = TextureResourceReader.readTextFileFromResource(mContext,
                                                                                   R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextureResourceReader.readTextFileFromResource(mContext,
                                                                                     R.raw.simple_fragment_shader);
        int vertextShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        int program = ShaderHelper.linkProgram(vertextShader, fragmentShader);
        if (LogHelper.enabled) {
            ShaderHelper.validateProgram(program);
        }
        glUseProgram(program);
        uColorLocation = glGetUniformLocation(program, U_COLOR);
        int aPositionLocation = glGetAttribLocation(program, A_POSITOIN);
        vertexData.position(0);
        //****** very import method *******//
        glVertexAttribPointer(aPositionLocation, POSITION_COMPOENT_COUNT, GL_FLOAT, false, 0,
                              vertexData);
        glEnableVertexAttribArray(aPositionLocation);
    }

    @Override public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        //set triangle color
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        //draw two triangles
        glDrawArrays(GL_TRIANGLES, 0, 6);

        //set line color
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);

        //draw the first blue mallets
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);

        //draw the second red mallets
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);
    }
}
