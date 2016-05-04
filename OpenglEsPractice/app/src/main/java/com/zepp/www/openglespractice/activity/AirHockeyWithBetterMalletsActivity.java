package com.zepp.www.openglespractice.activity;

import com.zepp.www.openglespractice.render.AirHockeyWithBetterMalletsRender;

public class AirHockeyWithBetterMalletsActivity extends BaseOpenglActivity {

    @Override protected void setGlSurfaceViewRenderer() {
        mGlSurfaceView.setRenderer(new AirHockeyWithBetterMalletsRender(this));
    }
}
