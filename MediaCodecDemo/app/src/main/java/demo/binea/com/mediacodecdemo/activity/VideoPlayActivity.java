package demo.binea.com.mediacodecdemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.VideoView;
import demo.binea.com.mediacodecdemo.R;

/**
 * Created by xubinggui on 7/9/15.
 */
public class VideoPlayActivity extends Activity {

    private VideoView video_view;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_layout);

        video_view = (VideoView) findViewById(R.id.video_view);
        video_view.setVideoPath(getIntent().getStringExtra(MainActivity.VIDEO_PATH));
        video_view.start();
    }

    @Override protected void onPause() {
        super.onPause();
        video_view.stopPlayback();
    }
}
