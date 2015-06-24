package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.xubinggui.customviewtest.util.Utils;

/**
 * Created by xubinggui on 15/4/4.
 */
public class MaskFilterView extends View {
	private static final int RECT_SIZE = 400;

	private Paint mPaint;

	private float left;
	private float top;
	private float right;
	private float bottom;

	private Context mContext;

	public MaskFilterView(Context context) {
		this(context, null);
	}

	public MaskFilterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initPaint();
	}

	private void initPaint() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(0xFF603811);

		mPaint.setMaskFilter(new BlurMaskFilter(40, BlurMaskFilter.Blur.NORMAL));

		left = Utils.getScreenWidth(mContext) / 2 - RECT_SIZE / 2;
		top = Utils.getScreenHeight(mContext) / 2 - RECT_SIZE / 2;
		right = Utils.getScreenWidth(mContext) / 2 + RECT_SIZE / 2;
		bottom = Utils.getScreenHeight(mContext) / 2 + RECT_SIZE / 2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.GRAY);

		canvas.drawRect(left, top, right, bottom, mPaint);
	}
}
