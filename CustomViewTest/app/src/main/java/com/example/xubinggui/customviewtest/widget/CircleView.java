package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.xubinggui.customviewtest.util.Utils;

/**
 * Created by xubinggui on 15/3/29.
 */
public class CircleView extends View {

	private final String TAG = CircleView.class.getSimpleName();

	private Paint mPaint;
	private Context mContext;

	private AvoidXfermode mAvoidXfermode;

	public CircleView(Context context) {
		this(context, null);
	}

	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initPaint();
	}

	private void initPaint() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.argb(255, 255, 128, 103));
		ColorMatrix matrix = new ColorMatrix(new float[]{
				1, 0, 0, 0, 0,
				0, 1, 0, 0, 0,
				0, 0, 1, 0, 0,
				0, 0, 0, 1, 0,
		});

		mPaint.setColorFilter(new ColorMatrixColorFilter(matrix));

//		mPaint.setColor(Color.BLACK);
		mPaint.setStrokeWidth(10);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d(TAG, "screen width " + Utils.getScreenWidth(mContext) + " screen height " + Utils.getScreenHeight(mContext));
		float centerX = Utils.getScreenWidth(mContext)/2;
//		float centerY = Utils.getScreenHeight(mContext)/2;
		float centerY = getMeasuredHeight()/2;
		canvas.drawCircle(centerX, centerY, 100, mPaint);
	}

	public void setColorMatrix(float []src){
		ColorMatrix matrix = new ColorMatrix(src);

		mPaint.setColorFilter(new ColorMatrixColorFilter(matrix));
		invalidate();
	}

	public void setLightColorFilter(){
		mPaint.setColorFilter(new LightingColorFilter(0xFFFF00FF, 0x00000000));
	}

	public void setPorterDuffColorFilter(int color){
		mPaint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.DARKEN));
	}
}
