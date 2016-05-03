package com.zepp.www.openglespractice.activity;

import com.zepp.www.openglespractice.render.AirHockeyTexturedRender;

public class AirHockeyTextureActivity extends BaseOpenglActivity {

    @Override protected void setGlSurfaceViewRenderer() {
        mGlSurfaceView.setRenderer(new AirHockeyTexturedRender(this));
    }
}
