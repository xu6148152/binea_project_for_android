package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.xubinggui.customviewtest.R;
import com.example.xubinggui.customviewtest.util.Utils;

/**
 * Created by xubinggui on 15/4/6.
 */
public class CustomImageView extends View {
	private Bitmap mBitmap;
	private TextPaint mTextPaint;
	private String str;
	private float mTextSize;
	private Context mContext;

	private enum Ratio {
		WIDTH, HEIGHT
	}

	public CustomImageView(Context context) {
		this(context, null);
	}

	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mContext = context;

		calArgs(context);

		init();
	}

	private void calArgs(Context context) {
		int sreenW = (int) Utils.getScreenWidth(mContext);

		mTextSize = sreenW * 1 / 10F;
	}

	private void init() {
		if (null == mBitmap) {
			mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		}

		if (null == str || str.trim().length() == 0) {
			str = "binea";
		}

		mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
		mTextPaint.setColor(Color.LTGRAY);
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setTextAlign(Paint.Align.CENTER);
		mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
	}

	public void setBitmap(Bitmap bitmap) {
		this.mBitmap = bitmap;
	}

	public void setText(String str) {
		this.str = str;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getMeasureSize(widthMeasureSpec, Ratio.WIDTH), getMeasureSize(heightMeasureSpec, Ratio.HEIGHT));
	}

	private int getMeasureSize(int measureSpec, Ratio radio) {

		int result = 0;

		final int mode = MeasureSpec.getMode(measureSpec);
		final int size = MeasureSpec.getSize(measureSpec);
		switch (mode) {
			case MeasureSpec.EXACTLY:
				result = size;
				break;

			default:
				if (radio == Ratio.WIDTH) {
					final int textWidth = (int) mTextPaint.measureText(str);
					result = ((textWidth >= mBitmap.getWidth() ? textWidth : mBitmap.getWidth())) + getPaddingLeft() + getPaddingRight();
				} else {
					result = (int) ((((mTextPaint.descent() - mTextPaint.ascent()) * 2 + mBitmap.getHeight())) + getPaddingTop() + getPaddingBottom());
				}

				if (mode == MeasureSpec.AT_MOST) {
					result = Math.min(result, size);
				}
				break;
		}

		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, getWidth() / 2 - mBitmap.getWidth() / 2, getHeight() / 2 - mBitmap.getHeight() / 2, null);
		canvas.drawText(str, getWidth() / 2, mBitmap.getHeight() + getHeight() / 2 - mBitmap.getHeight() / 2 - mTextPaint.ascent(), mTextPaint);
	}
}
