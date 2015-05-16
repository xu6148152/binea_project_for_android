package com.example.xubinggui.xfermodetest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by xubinggui on 15/1/1.
 */
public class RoundImageViewByXfermode extends ImageView {

	private Paint mPaint;
	private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
	private Bitmap mMaskBitmap;

	private WeakReference<Bitmap> mWeakBitmap;

	/**
	 * 图片的类型，圆形or圆角
	 */
	private int type;
	public static final int TYPE_CIRCLE = 0;
	public static final int TYPE_ROUND = 1;
	/**
	 * 圆角大小的默认值
	 */
	private static final int BODER_RADIUS_DEFAULT = 10;
	/**
	 * 圆角的大小
	 */
	private int mBorderRadius;

	public RoundImageViewByXfermode(Context context) {
		this(context, null);
	}

	public RoundImageViewByXfermode(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundImageViewByXfermode(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);

		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageViewByXfermode);

		mBorderRadius = typedArray.getDimensionPixelSize(R.styleable.RoundImageViewByXfermode_borderRadius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BODER_RADIUS_DEFAULT, getResources().getDisplayMetrics()));
		type = typedArray.getInt(R.styleable.RoundImageViewByXfermode_type, TYPE_CIRCLE);

		typedArray.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (type == TYPE_CIRCLE) {
			int width = Math.min(getMeasuredHeight(), getMeasuredWidth());
			setMeasuredDimension(width, width);
		}
	}

	public void invalidate()
	{
		mWeakBitmap = null;
		if (mMaskBitmap != null)
		{
			mMaskBitmap.recycle();
			mMaskBitmap = null;
		}
		super.invalidate();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		Bitmap bitmap = mWeakBitmap == null ? null : mWeakBitmap.get();

		if (bitmap == null || bitmap.isRecycled()) {
			final Drawable drawable = getDrawable();
			if (drawable != null) {
				final int dWidth = drawable.getIntrinsicWidth();
				final int dHeight = drawable.getIntrinsicHeight();
				bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

				float scale = 1.0f;

				Canvas drawCanvas = new Canvas(bitmap);

				if (type == TYPE_ROUND) {
					scale = Math.max(getWidth() * 1.0f / dWidth, getHeight() * 1.0f / dHeight);
				} else {
					scale = getWidth() * 1.0f / Math.min(dWidth, dHeight);
				}

				drawable.setBounds(0, 0, (int) scale * dWidth, (int) scale * dHeight);

				drawable.draw(drawCanvas);

				if (mMaskBitmap == null || mMaskBitmap.isRecycled()) {
					mMaskBitmap = getBitmap();
				}

				mPaint.reset();
				mPaint.setFilterBitmap(false);
				mPaint.setXfermode(mXfermode);
				drawCanvas.drawBitmap(mMaskBitmap, 0, 0, mPaint);
				mPaint.setXfermode(null);
				canvas.drawBitmap(bitmap, 0, 0, null);

				mWeakBitmap = new WeakReference<>(bitmap);
			}

		}

		if (bitmap != null) {
			mPaint.setXfermode(null);
			canvas.drawBitmap(bitmap, 0.0f, 0.0f, mPaint);
		}
	}


	public Bitmap getBitmap() {
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.BLACK);

		if (type == TYPE_ROUND) {
			canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()),
					mBorderRadius, mBorderRadius, paint);
		} else {
			canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2,
					paint);
		}
		return bitmap;
	}
}