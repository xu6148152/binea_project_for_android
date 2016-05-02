package com.zepp.www.openglespractice.activity;

import com.zepp.www.openglespractice.render.AirHockeyOrthoRender;

public class AirHockeyOrthoActivity extends BaseOpenglActivity {

    @Override protected void setGlSurfaceViewRenderer() {
        mGlSurfaceView.setRenderer(new AirHockeyOrthoRender(this));
    }
}
