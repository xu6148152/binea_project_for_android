package demo.binea.com.mediacodecdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import demo.binea.com.mediacodecdemo.CaptureVideo.encode.TextureMovieEncoder;
import demo.binea.com.mediacodecdemo.CaptureVideo.encode.newencode.MediaAudioEncoder;
import demo.binea.com.mediacodecdemo.CaptureVideo.encode.newencode.MediaEncoder;
import demo.binea.com.mediacodecdemo.CaptureVideo.encode.newencode.MediaMuxerWrapper;
import demo.binea.com.mediacodecdemo.CaptureVideo.encode.newencode.MediaVideoEncoder;
import demo.binea.com.mediacodecdemo.R;
import demo.binea.com.mediacodecdemo.filter.FilterManager;
import demo.binea.com.mediacodecdemo.filter.FilterManager.FilterType;
import demo.binea.com.mediacodecdemo.widget.CameraPreview;
import java.io.File;
import java.io.IOException;

public class VideoRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private CameraPreview mCameraSurfaceView;
    private Button mRecordButton;
    private boolean mIsRecordEnabled;

    private MediaMuxerWrapper mMuxer;

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
                //if (!mIsRecordEnabled) {
                //    file = new File(
                //            FileUtil.getCacheDirectory(VideoRecordActivity.this, true),
                //            "video-" + System.currentTimeMillis() + ".mp4");
                //    mCameraSurfaceView.queueEvent(new Runnable() {
                //        @Override public void run() {
                //            CameraRecordRenderer renderer = mCameraSurfaceView.getRenderer();
                //            renderer.setEncoderConfig(new EncoderConfig(file, 480, 480,
                //                    1024 * 1024 /* 1 Mb/s */));
                //        }
                //    });
                //}
                //mIsRecordEnabled = !mIsRecordEnabled;
                //mCameraSurfaceView.queueEvent(new Runnable() {
                //    @Override public void run() {
                //        mCameraSurfaceView.getRenderer().setRecordingEnabled(mIsRecordEnabled);
                //    }
                //});
                if (mMuxer == null) {
                    startRecording();
                } else {
                    stopRecording();
                }
                mIsRecordEnabled = !mIsRecordEnabled;
                updateRecordButton();
                break;
        }
    }

    /**
     * start resorcing
     * This is a sample project and call this on UI thread to avoid being complicated
     * but basically this should be called on private thread because prepareing
     * of encoder is heavy work
     */
    private void startRecording() {
        try {
            mCameraSurfaceView.startRecordind();
            mMuxer = new MediaMuxerWrapper(
                    ".mp4");    // if you record audio only, ".m4a" is also OK.
            // for video capturing
            new MediaVideoEncoder(mMuxer, mMediaEncoderListener, 480,
                    480);
            // for audio capturing
            new MediaAudioEncoder(mMuxer, mMediaEncoderListener);
            mMuxer.prepare();
            mMuxer.startRecording();
        } catch (final IOException e) {
        }
    }

    /**
     * request stop recording
     */
    private void stopRecording() {
        if (mMuxer != null) {
            mCameraSurfaceView.stopRecording();
            mMuxer.stopRecording();
            mMuxer = null;
            // you should not wait here
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

    private final MediaEncoder.MediaEncoderListener mMediaEncoderListener =
            new MediaEncoder.MediaEncoderListener() {
                @Override public void onPrepared(final MediaEncoder encoder) {
                    if (encoder instanceof MediaVideoEncoder) {
                        mCameraSurfaceView.setVideoEncoder((MediaVideoEncoder) encoder);
                    }
                }

                @Override public void onStopped(final MediaEncoder encoder) {
                    if (encoder instanceof MediaVideoEncoder) {
                        mCameraSurfaceView.setVideoEncoder(null);
                    }
                }
            };
}
