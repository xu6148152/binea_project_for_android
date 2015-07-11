package org.camera.camera;

import android.annotation.SuppressLint;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import java.io.IOException;
import java.util.List;
import org.camera.encode.VideoEncoderFromBuffer;

@SuppressLint("NewApi")
public class CameraWrapper {
	private static final String TAG = "CameraWrapper";
	private Camera mCamera;
	private Camera.Parameters mCameraParamters;
	private static CameraWrapper mCameraWrapper;
	private boolean mIsPreviewing = false;
	private float mPreviewRate = -1.0f;
	public static final int IMAGE_HEIGHT = 480;
	public static final int IMAGE_WIDTH = 640;
	private CameraPreviewCallback mCameraPreviewCallback;
	private byte[] mImageCallbackBuffer = new byte[CameraWrapper.IMAGE_WIDTH
	                     				* CameraWrapper.IMAGE_HEIGHT * 3 / 2];
	private int previewWidth;
	private int previewHeight;
	private int cameraSelection = -1;

	public interface CamOpenOverCallback {
		public void cameraHasOpened();
	}

	private CameraWrapper() {
	}

	public static synchronized CameraWrapper getInstance() {
		if (mCameraWrapper == null) {
			mCameraWrapper = new CameraWrapper();
		}
		return mCameraWrapper;
	}

	public void doOpenCamera(CamOpenOverCallback callback) {
		Log.i(TAG, "Camera open....");
        int numCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < numCameras; i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCamera = Camera.open(i);
                break;
            }
        }
        if (mCamera == null) {
            Log.d(TAG, "No front-facing camera found; opening default");
            mCamera = Camera.open();    // opens first back-facing camera
        }
        if (mCamera == null) {
            throw new RuntimeException("Unable to open camera");
        }
		Log.i(TAG, "Camera open over....");
		callback.cameraHasOpened();
	}

	public void doStartPreview(SurfaceHolder holder, float previewRate) {
		Log.i(TAG, "doStartPreview...");
		if (mIsPreviewing) {
			this.mCamera.stopPreview();
			return;
		}

		try {
			this.mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		initCamera();
	}
	
	public void doStartPreview(SurfaceTexture surface, float previewRate) {
		Log.i(TAG, "doStartPreview()");
		if (mIsPreviewing) {
			this.mCamera.stopPreview();
			return;
		}

		try {
			this.mCamera.setPreviewTexture(surface);
		} catch (IOException e) {
			e.printStackTrace();
		}
		initCamera();
	}

	public void doStopCamera() {
		Log.i(TAG, "doStopCamera");
		if (this.mCamera != null) {
			mCameraPreviewCallback.close();
			this.mCamera.setPreviewCallback(null);
			this.mCamera.stopPreview();
			this.mIsPreviewing = false;
			this.mPreviewRate = -1f;
			this.mCamera.release();
			this.mCamera = null;
		}
	}
	
	private void initCamera() {
		if (this.mCamera != null) {
			this.mCameraParamters = this.mCamera.getParameters();
			//this.mCameraParamters.setPreviewFormat(ImageFormat.NV21);
			//this.mCameraParamters.setFlashMode("off");
			//this.mCameraParamters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
			//this.mCameraParamters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
			this.mCameraParamters.setPreviewSize(IMAGE_WIDTH, IMAGE_HEIGHT);
			previewHeight = IMAGE_HEIGHT;
			previewWidth = IMAGE_WIDTH;
			this.mCamera.setDisplayOrientation(90);
			//mCameraPreviewCallback = new CameraPreviewCallback();
			//mCamera.addCallbackBuffer(mImageCallbackBuffer);
			//mCamera.setPreviewCallbackWithBuffer(mCameraPreviewCallback);
			List<String> focusModes = this.mCameraParamters.getSupportedFocusModes();
			if (focusModes.contains("continuous-video")) {
				this.mCameraParamters
						.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			}
			this.mCamera.setParameters(this.mCameraParamters);
			this.mCamera.startPreview();
			
			this.mIsPreviewing = true;
		}
	}
	
	class CameraPreviewCallback implements Camera.PreviewCallback {
		private static final String TAG = "CameraPreviewCallback";
		private VideoEncoderFromBuffer videoEncoder = null;

		private CameraPreviewCallback() {
			videoEncoder = new VideoEncoderFromBuffer(CameraWrapper.IMAGE_WIDTH,
					CameraWrapper.IMAGE_HEIGHT);
		}

		void close() {
			videoEncoder.close();
		}

		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			Log.i(TAG, "onPreviewFrame");
			long startTime = System.currentTimeMillis();
			byte[] tempData = rotateYUV420Degree90(data, previewWidth, previewHeight);
			if(cameraSelection == 1)
				tempData = rotateYUV420Degree270(data, previewWidth, previewHeight);
			Log.d(TAG, "onPreviewFrame data size " + previewHeight * previewWidth * ImageFormat.getBitsPerPixel(camera.getParameters().getPreviewFormat() / 8));
			byte[] newData = new byte[previewHeight * previewWidth];
			int count = 0;
			for(int i = 0, j = 0;i<data.length && j < newData.length; i++,j++){
				if((i + 1) % 480 == 0){
					i = i + (previewWidth - previewHeight);
				}
				count++;
				newData[j] = data[i];
			}
			Log.d(TAG, "onPreview Frame count " + count);
			videoEncoder.encodeFrame(tempData/*, encodeData*/);
			long endTime = System.currentTimeMillis();
			Log.i(TAG, Integer.toString((int)(endTime-startTime)) + "ms");
			camera.addCallbackBuffer(data);
		}
	}

	public void setPreviewCallbackWidthBuffer(){
		mCameraPreviewCallback = new CameraPreviewCallback();
		mCamera.addCallbackBuffer(mImageCallbackBuffer);
		mCamera.setPreviewCallbackWithBuffer(mCameraPreviewCallback);
	}

	public void removePreviewCallbackWidthButter(){
		mCamera.setPreviewCallbackWithBuffer(null);
	}

	private byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight)
	{

		final byte [] yuv = new byte[previewWidth*previewHeight*3/2];
		// Rotate the Y luma
		int i = 0;
		for(int x = 0;x < imageWidth;x++)
		{
			for(int y = imageHeight-1;y >= 0;y--)
			{
				yuv[i] = data[y*imageWidth+x];
				i++;
			}

		}
		// Rotate the U and V color components
		i = imageWidth*imageHeight*3/2-1;
		for(int x = imageWidth-1;x > 0;x=x-2)
		{
			for(int y = 0;y < imageHeight/2;y++)
			{
				yuv[i] = data[(imageWidth*imageHeight)+(y*imageWidth)+x];
				i--;
				yuv[i] = data[(imageWidth*imageHeight)+(y*imageWidth)+(x-1)];
				i--;
			}
		}
		return yuv;
	}

	private byte[] rotateYUV420Degree180(byte[] data, int imageWidth, int imageHeight)
	{
		byte [] yuv = new byte[imageWidth*imageHeight*3/2];
		int i = 0;
		int count = 0;

		for (i = imageWidth * imageHeight - 1; i >= 0; i--) {
			yuv[count] = data[i];
			count++;
		}

		i = imageWidth * imageHeight * 3 / 2 - 1;
		for (i = imageWidth * imageHeight * 3 / 2 - 1; i >= imageWidth
				* imageHeight; i -= 2) {
			yuv[count++] = data[i - 1];
			yuv[count++] = data[i];
		}
		return yuv;
	}

	private byte[] rotateYUV420Degree270(byte[] data, int imageWidth, int imageHeight)
	{
		final byte [] yuv = new byte[previewWidth*previewHeight*3/2];
		int wh = 0;
		int uvHeight = 0;
		if(imageWidth != 0 || imageHeight != 0)
		{
			wh = imageWidth * imageHeight;
			uvHeight = imageHeight >> 1;//uvHeight = height / 2
		}

		//旋转Y
		int k = 0;
		for(int i = 0; i < imageWidth; i++) {
			int nPos = 0;
			for(int j = 0; j < imageHeight; j++) {
				yuv[k] = data[nPos + i];
				k++;
				nPos += imageWidth;
			}
		}

		for(int i = 0; i < imageWidth; i+=2){
			int nPos = wh;
			for(int j = 0; j < uvHeight; j++) {
				yuv[k] = data[nPos + i];
				yuv[k + 1] = data[nPos + i + 1];
				k += 2;
				nPos += imageWidth;
			}
		}
		//这一部分可以直接旋转270度，但是图像颜色不对
		//	    // Rotate the Y luma
		//	    int i = 0;
		//	    for(int x = imageWidth-1;x >= 0;x--)
		//	    {
		//	        for(int y = 0;y < imageHeight;y++)
		//	        {
		//	            yuv[i] = data[y*imageWidth+x];
		//	            i++;
		//	        }
		//
		//	    }
		//	    // Rotate the U and V color components
		//		i = imageWidth*imageHeight;
		//	    for(int x = imageWidth-1;x > 0;x=x-2)
		//	    {
		//	        for(int y = 0;y < imageHeight/2;y++)
		//	        {
		//	            yuv[i] = data[(imageWidth*imageHeight)+(y*imageWidth)+x];
		//	            i++;
		//	            yuv[i] = data[(imageWidth*imageHeight)+(y*imageWidth)+(x-1)];
		//	            i++;
		//	        }
		//	    }
		return rotateYUV420Degree180(yuv,imageWidth,imageHeight);
	}

	public byte[] cropYUV420(byte[] data,int imageW,int imageH,int newImageH){
		int cropH;
		int i,j,count,tmp;
		byte[] yuv = new byte[imageW*newImageH*3/2];

		cropH = (imageH - newImageH)/2;

		count = 0;
		for(j=cropH;j<cropH+newImageH;j++){
			for(i=0;i<imageW;i++){
				yuv[count++] = data[j*imageW+i];
			}
		}

		//Cr Cb
		tmp = imageH+cropH/2;
		for(j=tmp;j<tmp + newImageH/2;j++){
			for(i=0;i<imageW;i++){
				yuv[count++] = data[j*imageW+i];
			}
		}

		return yuv;
	}
}
