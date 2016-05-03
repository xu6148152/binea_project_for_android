package com.zepp.www.openglespractice.objects;

import com.zepp.www.openglespractice.data.VertexArray;
import com.zepp.www.openglespractice.programs.TextureShaderProgram;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;
import static com.zepp.www.openglespractice.support.Constants.BYTES_PER_FLOAT;
import static com.zepp.www.openglespractice.support.Constants.POSITION_COMPONENT_COUNT;
import static com.zepp.www.openglespractice.support.Constants.TEXTURE_COORDINATES_COMPONENT_COUNT;

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
public class Table {

    protected static int STRIDE =
            (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            //Order of coordinates: X, Y, S, T

            //Triangle Fan
               0f,    0f,  0.5f,  0.5f,
            -0.5f, -0.8f,    0f,  0.9f,
             0.5f, -0.8f,    1f,  0.9f,
             0.5f,  0.8f,    1f,  0.1f,
            -0.5f,  0.8f,    0f,  0.1f,
            -0.5f, -0.8f,    0f,  0.9f,
    };

    private final VertexArray vertexArray;

    public Table() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE
        );
    }

    public void draw() {
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }
}
