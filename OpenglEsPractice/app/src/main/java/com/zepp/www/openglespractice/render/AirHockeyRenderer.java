package com.zepp.www.openglespractice.render;

import android.content.Context;
import com.zepp.www.openglespractice.R;
import com.zepp.www.openglespractice.util.TextureResourceReader;

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

/**
 * blended_value = (vertex_0_value * (100% - distance_ratio) + (vertext_1_value * distance_ratio))
 */
public class AirHockeyRenderer extends BaseHockeyRender {

    public AirHockeyRenderer(Context context) {
        super(context);
    }

    @Override protected void doSomeAfterClear() {

    }

    @Override protected String readVertexShader() {
        return TextureResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
    }

    @Override protected String readFragmentShader() {
        return TextureResourceReader.readTextFileFromResource(mContext,
                                                              R.raw.simple_fragment_shader);
    }

    @Override protected void getMatrixPosition() {

    }
}
