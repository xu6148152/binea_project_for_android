package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by xubinggui on 15/4/5.
 */
public class AnimListView extends ListView {
	private Context mContext;
	private Paint mPaint;
	private Camera mCamera;
	private Matrix mMatrix;

	public AnimListView(Context context) {
		this(context, null);
	}

	public AnimListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);

		mCamera = new Camera();
		mMatrix = new Matrix();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mCamera.save();
		mCamera.rotate(30, 0, 0);
		mCamera.getMatrix(mMatrix);
		mMatrix.preTranslate(-getWidth() / 2, -getHeight() / 2);
		mMatrix.postTranslate(getWidth() / 2, getHeight() / 2);
		canvas.concat(mMatrix);
		super.onDraw(canvas);
		mCamera.restore();
	}
}
