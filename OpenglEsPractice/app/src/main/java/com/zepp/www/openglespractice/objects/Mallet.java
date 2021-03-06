package com.zepp.www.openglespractice.objects;

import com.zepp.www.openglespractice.data.VertexArray;
import com.zepp.www.openglespractice.programs.ColorShaderProgram;
import com.zepp.www.openglespractice.util.Geometry;
import java.util.List;

import static com.zepp.www.openglespractice.support.Constants.BYTES_PER_FLOAT;
import static com.zepp.www.openglespractice.support.Constants.COLOR_COMPONENT_COUNT;

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
public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius;
    public final float height;

    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            //Order of coordinates: X, Y, R, G, B
            0f, -0.4f, 0f, 0f, 1f, 0f, 0.4f, 1f, 0f, 0f,
    };

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Mallet(float radius, float height, int numPointsAroundMallet) {
        ObjectBuilder.GeneratedData generatedData =
                ObjectBuilder.createMallet(new Geometry.Point(0f, 0f, 0f), radius, height,
                                           numPointsAroundMallet);

        this.radius = radius;
        this.height = height;

        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    public void bindData(ColorShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(),
                                           POSITION_COMPONENT_COUNT, STRIDE);

        //vertexArray.setVertexAttribPointer(0, colorProgram.getColorAttributeLocation(),
        //                                   COLOR_COMPONENT_COUNT, STRIDE);
    }

    public void draw() {
        //glDrawArrays(GL_POINTS, 0, 2);
        for (ObjectBuilder.DrawCommand command : drawList) {
            command.draw();
        }
    }
}
