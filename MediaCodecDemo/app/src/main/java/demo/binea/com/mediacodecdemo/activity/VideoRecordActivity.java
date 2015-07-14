package demo.binea.com.mediacodecdemo.activity;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import demo.binea.com.mediacodecdemo.CaptureVideo.camera.CameraRecordRenderer;
import demo.binea.com.mediacodecdemo.CaptureVideo.coder.EncoderConfig;
import demo.binea.com.mediacodecdemo.CaptureVideo.coder.TextureMovieEncoder;
import demo.binea.com.mediacodecdemo.R;
import demo.binea.com.mediacodecdemo.filter.FilterManager;
import demo.binea.com.mediacodecdemo.filter.FilterManager.FilterType;
import demo.binea.com.mediacodecdemo.utils.FileUtil;
import demo.binea.com.mediacodecdemo.widget.CameraPreview;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class VideoRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private CameraPreview mCameraSurfaceView;
    private Button mRecordButton;
    private boolean mIsRecordEnabled;

    private MediaRecorder mRecorder;

    private File videoFile = null;
    private File audioFile = null;
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
                    videoFile = new File(
                            FileUtil.getCacheDirectory(VideoRecordActivity.this, true),
                            "video-test" + ".mp4");
                    mCameraSurfaceView.queueEvent(new Runnable() {
                        @Override public void run() {
                            CameraRecordRenderer renderer = mCameraSurfaceView.getRenderer();
                            renderer.setEncoderConfig(new EncoderConfig(videoFile, 480, 480,
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
                onRecordAudio(mIsRecordEnabled);
                updateRecordButton();
                break;
        }
    }

    private void onRecordAudio(boolean mIsRecordEnabled) {
        if(mIsRecordEnabled){
            startRecordAudio();
        }else{
            stopRecordAudio();
        }
    }

    private void stopRecordAudio() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        new AsyncTask<Void, Void, Void>() {
            @Override protected Void doInBackground(Void... params) {
                muxMp4WithVideoAndAudio();
                return null;
            }

            @Override protected void onPostExecute(Void aVoid) {
                Toast.makeText(VideoRecordActivity.this, "muxMp4 complete", Toast.LENGTH_SHORT)
                        .show();
            }
        }.execute();
    }

    private void muxMp4WithVideoAndAudio() {
        String audio = audioFile.getAbsolutePath();
        String video = videoFile.getAbsolutePath();

        try {
            Movie countVideo = MovieCreator.build(video);
            Movie countAudio = MovieCreator.build(audio);

            Track audioTrack = countAudio.getTracks().get(0);
            countVideo.addTrack(audioTrack);

            {
                Container out = new DefaultMp4Builder().build(countVideo);
                FileOutputStream fos = new FileOutputStream(new File(
                        FileUtil.getCacheDirectory(VideoRecordActivity.this, true),
                        "output.mp4"));
                out.writeContainer(fos.getChannel());
                fos.close();
            }
            //{
            //    FragmentedMp4Builder fragmentedMp4Builder = new FragmentedMp4Builder();
            //    fragmentedMp4Builder.setIntersectionFinder(new SyncSampleIntersectFinderImpl(countVideo, null, -1));
            //    Container out = fragmentedMp4Builder.build(countVideo);
            //    FileOutputStream fos = new FileOutputStream(new File("output-frag.mp4"));
            //    out.writeContainer(fos.getChannel());
            //    fos.close();
            //}

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private void startRecordAudio() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        audioFile = new File(FileUtil.getCacheDirectory(VideoRecordActivity.this, true),
                "audio-test" + ".mp4");
        mRecorder.setOutputFile(audioFile.toString());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try{
            mRecorder.prepare();
        }catch (IOException e){
            throw new RuntimeException("prepare erro");
        }

        mRecorder.start();
    }

    public void updateRecordButton() {
        mRecordButton.setText(
                getString(mIsRecordEnabled ? R.string.record_stop : R.string.record_start));
    }

    public void playVideo(View view) {
        Intent intent = new Intent(this, VideoPlayActivity.class);
        intent.putExtra(MainActivity.VIDEO_PATH, videoFile.getAbsolutePath());
        startActivity(intent);
    }
}
