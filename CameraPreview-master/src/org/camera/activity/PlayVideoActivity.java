package org.camera.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.VideoView;
import com.example.camerapreview.R;

/**
 * Created by xubinggui on 7/11/15.
 */
public class PlayVideoActivity extends Activity {

    public static final String PATH = "PATH";
    private VideoView video_view;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_video);
        video_view = (VideoView) findViewById(R.id.video_view);

        video_view.setVideoPath(getIntent().getStringExtra(PATH));
        video_view.start();
    }

    @Override protected void onPause() {
        super.onPause();
        video_view.stopPlayback();
    }
}
