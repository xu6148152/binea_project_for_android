package com.zepp.www.openglespractice.util;

import static android.opengl.GLES20.*;

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
public class ShaderHelper {
    public static final String TAG = ShaderHelper.class.getCanonicalName();

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = glCreateShader(type);

        if (shaderObjectId == 0) {
            LogHelper.w(TAG, "Could not create new shader.");
            return 0;
        }

        glShaderSource(shaderObjectId, shaderCode);
        glCompileShader(shaderObjectId);

        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

        LogHelper.v(TAG,
                "Results of compiling source:" + "\n" + shaderCode + "\n" + glGetShaderInfoLog(
                        shaderObjectId));

        if (compileStatus[0] == 0) {
            glDeleteShader(shaderObjectId);

            LogHelper.w(TAG, "Compilation of shader failed");
            return 0;
        }

        return shaderObjectId;
    }

    public static int linkProgram(int vertextShaderId, int fragmentShaderId) {
        final int programObjectId = glCreateProgram();

        if (programObjectId == 0) {
            LogHelper.w(TAG, "Could not create new program");
            return 0;
        }

        glAttachShader(programObjectId, vertextShaderId);
        glAttachShader(programObjectId, fragmentShaderId);

        glLinkProgram(programObjectId);

        final int[] status = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, status, 0);

        LogHelper.v(TAG, "Result of linking program: \n" + glGetProgramInfoLog(programObjectId));

        if (status[0] == 0) {
            glDeleteProgram(programObjectId);
            LogHelper.w(TAG, "Linking of program failed.");
            return 0;
        }

        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);

        LogHelper.v(TAG, "Result of validating program: "
                + validateStatus[0]
                + "\nLog:"
                + glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }
}
