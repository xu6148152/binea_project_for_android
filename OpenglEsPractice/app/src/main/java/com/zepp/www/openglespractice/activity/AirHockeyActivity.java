package com.zepp.www.openglespractice.activity;

import com.zepp.www.openglespractice.render.AirHockeyRenderer;

public class AirHockeyActivity extends BaseOpenglActivity {

    @Override protected void setGlSurfaceViewRenderer() {
        mGlSurfaceView.setRenderer(new AirHockeyRenderer(this));
    }

}
