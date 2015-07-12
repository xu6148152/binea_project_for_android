package demo.binea.com.mediacodecdemo.widget;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import demo.binea.com.mediacodecdemo.CaptureVideo.camera.CameraController;
import demo.binea.com.mediacodecdemo.CaptureVideo.camera.CameraHelper;
import demo.binea.com.mediacodecdemo.CaptureVideo.camera.CameraRecordRenderer;
import demo.binea.com.mediacodecdemo.CaptureVideo.camera.CommonHandlerListener;
import demo.binea.com.mediacodecdemo.CaptureVideo.encode.newencode.MediaVideoEncoder;
import demo.binea.com.mediacodecdemo.filter.FilterManager;

/**
 * Created by xubinggui on 7/12/15.
 */
public class CameraPreview extends AutoFitSurfaceView
        implements CommonHandlerListener, SurfaceTexture.OnFrameAvailableListener{
    private CameraHandler mBackgroundHandler;
    private HandlerThread mHandlerThread;
    private CameraRecordRenderer mCameraRenderer;

    private int viewWidth;
    private int viewHeight;

    public CameraPreview(Context context) {
        this(context, null);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        setEGLContextClientVersion(2);

        mHandlerThread = new HandlerThread("CameraHandlerThread");
        mHandlerThread.start();

        mBackgroundHandler = new CameraHandler(mHandlerThread.getLooper(), this);
        mCameraRenderer =
                new CameraRecordRenderer(context.getApplicationContext(), mBackgroundHandler);

        setRenderer(mCameraRenderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    public CameraRecordRenderer getRenderer() {
        return mCameraRenderer;
    }

    //public void setEncoderConfig(EncoderConfig encoderConfig) {
    //    if (mCameraRenderer != null) {
    //        mCameraRenderer.setEncoderConfig(encoderConfig);
    //    }
    //}

    @Override public void onPause() {
        mBackgroundHandler.removeCallbacksAndMessages(null);
        CameraController.getInstance().release();
        queueEvent(new Runnable() {
            @Override public void run() {
                // 跨进程 清空 Renderer数据
                mCameraRenderer.notifyPausing();
            }
        });

        super.onPause();
    }

    public void onDestroy() {
        mBackgroundHandler.removeCallbacksAndMessages(null);
        if (!mHandlerThread.isInterrupted()) {
            try {
                mHandlerThread.quit();
                mHandlerThread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void changeFilter(FilterManager.FilterType filterType) {
        mCameraRenderer.changeFilter(filterType);
    }

    @Override public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        requestRender();
    }

    public int getVideoWidth() {
        return viewHeight;
    }

    public int getVideoHeight() {
        return viewHeight;
    }

    public void setVideoEncoder(final MediaVideoEncoder encoder) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                synchronized (mCameraRenderer) {
                    mCameraRenderer.setVideoEncoder(encoder);
                    //mVideoEncoder = encoder;
                }
            }
        });
    }

    public static class CameraHandler extends Handler {
        public static final int SETUP_CAMERA = 1001;
        public static final int CONFIGURE_CAMERA = 1002;
        public static final int START_CAMERA_PREVIEW = 1003;
        //public static final int STOP_CAMERA_PREVIEW = 1004;
        private CommonHandlerListener listener;

        public CameraHandler(Looper looper, CommonHandlerListener listener) {
            super(looper);
            this.listener = listener;
        }

        @Override public void handleMessage(Message msg) {
            listener.handleMessage(msg);
        }
    }

    @Override public void handleMessage(final Message msg) {
        switch (msg.what) {
            case CameraHandler.SETUP_CAMERA: {
                final int width = msg.arg1;
                final int height = msg.arg2;
                final SurfaceTexture surfaceTexture = (SurfaceTexture) msg.obj;
                surfaceTexture.setOnFrameAvailableListener(this);

                mBackgroundHandler.post(new Runnable() {
                    @Override public void run() {
                        CameraController.getInstance()
                                .setupCamera(surfaceTexture, getContext().getApplicationContext(),
                                        width);
                        mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(
                                CameraPreview.CameraHandler.CONFIGURE_CAMERA, width, height));
                    }
                });
            }
            break;
            case CameraHandler.CONFIGURE_CAMERA: {
                final int width = msg.arg1;
                final int height = msg.arg2;
                Camera.Size previewSize = CameraHelper.getOptimalPreviewSize(
                        CameraController.getInstance().getCameraParameters(),
                        CameraController.getInstance().mCameraPictureSize, width);
                CameraController.getInstance().configureCameraParameters(previewSize);
                viewWidth = width;
                if (previewSize != null) {
                    mCameraRenderer.setCameraPreviewSize(previewSize.height, previewSize.width);
                }
                mBackgroundHandler.sendEmptyMessage(CameraHandler.START_CAMERA_PREVIEW);
            }
            break;

            case CameraHandler.START_CAMERA_PREVIEW:
                mBackgroundHandler.post(new Runnable() {
                    @Override public void run() {
                        CameraController.getInstance().startCameraPreview();
                    }
                });

                break;
            //case CameraHandler.STOP_CAMERA_PREVIEW:
            //    mBackgroundHandler.post(new Runnable() {
            //        @Override public void run() {
            //            CameraController.getInstance().stopCameraPreview();
            //        }
            //    });
            //    break;

            default:
                break;
        }
    }

    public void stopRecording(){
        mCameraRenderer.setRecordingEnabled(false);
    }

    public void startRecordind(){
        mCameraRenderer.setRecordingEnabled(true);
    }
}
