package demo.binea.com.mediacodecdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import demo.binea.com.mediacodecdemo.CaptureVideo.camera.CameraRecordRenderer;
import demo.binea.com.mediacodecdemo.CaptureVideo.coder.EncoderConfig;
import demo.binea.com.mediacodecdemo.CaptureVideo.coder.TextureMovieEncoder;
import demo.binea.com.mediacodecdemo.R;
import demo.binea.com.mediacodecdemo.filter.FilterManager;
import demo.binea.com.mediacodecdemo.filter.FilterManager.FilterType;
import demo.binea.com.mediacodecdemo.utils.FileUtil;
import demo.binea.com.mediacodecdemo.widget.CameraPreview;
import java.io.File;

public class VideoRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private CameraPreview mCameraSurfaceView;
    private Button mRecordButton;
    private boolean mIsRecordEnabled;

    private File file = null;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_record);
        mCameraSurfaceView = (CameraPreview) findViewById(R.id.camera);
        mCameraSurfaceView.setAspectRatio(1, 1);

        findViewById(R.id.filter_normal).setOnClickListener(this);
        findViewById(R.id.filter_tone_curve).setOnClickListener(this);
        findViewById(R.id.filter_soft_light).setOnClickListener(this);

        mRecordButton = (Button) findViewById(R.id.record);
        mRecordButton.setOnClickListener(this);

        mIsRecordEnabled = TextureMovieEncoder.getInstance().isRecording();
        updateRecordButton();
    }

    @Override protected void onResume() {
        super.onResume();
        mCameraSurfaceView.onResume();
        updateRecordButton();
    }

    @Override protected void onPause() {
        mCameraSurfaceView.onPause();
        super.onPause();
    }

    @Override protected void onDestroy() {
        mCameraSurfaceView.onDestroy();
        super.onDestroy();
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filter_normal:
                mCameraSurfaceView.changeFilter(FilterManager.FilterType.Normal);
                break;
            case R.id.filter_tone_curve:
                mCameraSurfaceView.changeFilter(FilterType.ToneCurve);
                break;
            case R.id.filter_soft_light:
                mCameraSurfaceView.changeFilter(FilterType.SoftLight);
                break;
            case R.id.record:
                if (!mIsRecordEnabled) {
                    file = new File(
                            FileUtil.getCacheDirectory(VideoRecordActivity.this, true),
                            "video-" + System.currentTimeMillis() + ".mp4");
                    mCameraSurfaceView.queueEvent(new Runnable() {
                        @Override public void run() {
                            CameraRecordRenderer renderer = mCameraSurfaceView.getRenderer();
                            renderer.setEncoderConfig(new EncoderConfig(file, 480, 480,
                                    1024 * 1024 /* 1 Mb/s */));
                        }
                    });
                }
                mIsRecordEnabled = !mIsRecordEnabled;
                mCameraSurfaceView.queueEvent(new Runnable() {
                    @Override public void run() {
                        mCameraSurfaceView.getRenderer().setRecordingEnabled(mIsRecordEnabled);
                    }
                });
                updateRecordButton();
                break;
        }
    }

    public void updateRecordButton() {
        mRecordButton.setText(
                getString(mIsRecordEnabled ? R.string.record_stop : R.string.record_start));
    }

    public void playVideo(View view) {
        Intent intent = new Intent(this, VideoPlayActivity.class);
        intent.putExtra(MainActivity.VIDEO_PATH, file.getAbsolutePath());
        startActivity(intent);
    }
}
