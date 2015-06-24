package demo.binea.com.drawabledemo.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by xubinggui on 15/3/7.
 */
public class RoundImageDrawable extends Drawable {

	private Bitmap mBitmap;
	private Paint mPaint;
	private RectF mRectF;

	public RoundImageDrawable(Bitmap bitmap){
		mBitmap = bitmap;
		BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setShader(bitmapShader);
	}

	@Override
	public void setBounds(int left, int top, int right, int bottom) {
		super.setBounds(left, top, right, bottom);
		mRectF = new RectF(left, top, right, bottom);
	}

	@Override
	public void setBounds(Rect bounds) {
		super.setBounds(bounds);

	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawRoundRect(mRectF, 30, 30, mPaint);
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
	public int getIntrinsicWidth() {
		return mBitmap.getWidth();
	}

	@Override
	public int getIntrinsicHeight() {
		return mBitmap.getHeight();
	}
}
