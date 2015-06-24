package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.xubinggui.customviewtest.R;
import com.example.xubinggui.customviewtest.util.Utils;

/**
 * Created by xubinggui on 15/4/5.
 */
public class ShaderView extends View {
	private static final int RECT_SIZE = 400;
	private static final int RADIUS = 50;
	private Paint mPaint;

	private int left, top, right, bottom;

	private Context mContext;
	private int mScreenX;
	private int mScreenY;

	private float mCenterX;
	private float mCenterY;

	public ShaderView(Context context) {
		this(context, null);
	}

	public ShaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initPaint();
	}

	private void initPaint() {

		mScreenX = (int) (Utils.getScreenWidth(mContext) / 2);
		mScreenY = (int) (Utils.getScreenHeight(mContext) / 2);

		mCenterX = mScreenX;
		mCenterY = mScreenY;

		left = mScreenX - RECT_SIZE;
		top = mScreenY - RECT_SIZE;
		right = mScreenX + RECT_SIZE;
		bottom = mScreenY + RECT_SIZE;

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aa);
//
		mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawCircle(mCenterX, mCenterY, RADIUS, mPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		final int action = event.getAction();
		switch (action){
			case MotionEvent.ACTION_MOVE:
				mCenterX = event.getX();
				mCenterY = event.getY();
				invalidate();
				break;
		}

		return true;
	}
}
