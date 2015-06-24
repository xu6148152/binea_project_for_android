package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import com.example.xubinggui.customviewtest.util.Utils;

/**
 * Created by xubinggui on 15/3/29.
 */
public class ClockView extends View {
	private Context mContext;
	private Paint mPaint;
	private Paint mRingPaint;
	private Paint mLinePaint;

	private float centerX;
	private float centerY;

	private float leftCenterX;
	private float leftCenterY;

	private float rightCenterX;
	private float rightCenterY;

	private PorterDuff.Mode mode = PorterDuff.Mode.DST_IN;

	private Xfermode mXfermode;

	private final int BIG_RADIUS = 100;

	public ClockView(Context context) {
		this(context, null);
	}

	public ClockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;

		initPaint();
	}

	private void initPaint() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.argb(255, 255, 128, 103));

		centerX = Utils.getScreenWidth(mContext)/2;
		centerY = Utils.getScreenHeight(mContext)/2;

		leftCenterX = centerX - BIG_RADIUS / 2;
		double tempValue = Math.pow(BIG_RADIUS, 2) - Math.pow(BIG_RADIUS/2, 2);
		leftCenterY = (float) Math.sqrt(tempValue);

		rightCenterX = centerX + BIG_RADIUS / 2;

		mRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mRingPaint.setColor(Color.WHITE);
		mRingPaint.setStyle(Paint.Style.STROKE);
		mRingPaint.setStrokeWidth(20);

		mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mLinePaint.setColor(Color.WHITE);
		mLinePaint.setStyle(Paint.Style.STROKE);
		mLinePaint.setStrokeWidth(10);

		mXfermode = new PorterDuffXfermode(mode);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		mPaint.setColor(Color.WHITE);
		//left top circle
		canvas.drawCircle(leftCenterX, centerY - leftCenterY, BIG_RADIUS / 3, mPaint);
		//right top circle
		canvas.drawCircle(rightCenterX, centerY - leftCenterY, BIG_RADIUS / 3, mPaint);

		mPaint.setColor(Color.argb(255, 255, 128, 103));
		canvas.drawCircle(centerX, centerY, BIG_RADIUS, mPaint);
		canvas.drawCircle(centerX, centerY, BIG_RADIUS - 20, mRingPaint);
		canvas.drawLine(centerX, centerY, centerX, centerY - BIG_RADIUS / 2, mLinePaint);
		canvas.drawLine(centerX - 5, centerY, centerX + BIG_RADIUS / 2 - 10, centerY, mLinePaint);

	}
}
