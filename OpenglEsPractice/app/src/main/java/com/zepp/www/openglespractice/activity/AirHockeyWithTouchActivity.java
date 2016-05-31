package com.zepp.www.openglespractice.activity;

import android.view.MotionEvent;
import android.view.View;
import com.zepp.www.openglespractice.render.AirHockeyWithTouchRender;

/**
 * Created by xubinggui on 5/31/16.
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
public class AirHockeyWithTouchActivity extends BaseOpenglActivity {
    private AirHockeyWithTouchRender mAirHockeyWithTouchRender = new AirHockeyWithTouchRender(this);

    @Override protected void setGlSurfaceViewRenderer() {
        mGlSurfaceView.setRenderer(mAirHockeyWithTouchRender);
        mGlSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                if (event != null) {
                    final float normalizedX = (event.getX() / v.getWidth()) * 2 - 1;
                    final float normalizedY = -((event.getY() / v.getHeight()) * 2 - 1);

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        mGlSurfaceView.queueEvent(new Runnable() {
                            @Override public void run() {
                                mAirHockeyWithTouchRender.handleTouchPress(normalizedX,
                                                                           normalizedY);
                            }
                        });
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        mGlSurfaceView.queueEvent(new Runnable() {
                            @Override public void run() {
                                mAirHockeyWithTouchRender.handleTouchDrag(normalizedX, normalizedY);
                            }
                        });
                    }
                    return true;
                }

                return false;
            }
        });
    }
}
