package com.example.xubinggui.customviewtest.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by xubinggui on 15/3/7.
 */
public class CircleImageDrawable extends Drawable {

	private Bitmap mBitmap;
	private Paint mPaint;
	private RectF mRectF;
	private int mWidth;

	public CircleImageDrawable(Bitmap bitmap){
		mBitmap = bitmap;

		BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setShader(bitmapShader);
		mWidth = Math.min(mBitmap.getWidth(), mBitmap.getHeight());
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawCircle(mWidth/2, mWidth/2, mWidth/2, mPaint);
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		mPaint.setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public int getIntrinsicWidth()
	{
		return mWidth;
	}

	@Override
	public int getIntrinsicHeight()
	{
		return mWidth;
	}
}
