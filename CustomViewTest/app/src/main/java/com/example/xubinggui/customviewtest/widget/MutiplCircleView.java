package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import com.example.xubinggui.customviewtest.R;
import com.example.xubinggui.customviewtest.util.Utils;

/**
 * Created by xubinggui on 15/4/4.
 */
public class MutiplCircleView extends View {
	private final Bitmap mSrcB;
	private final Bitmap mDstB;
	private Paint mPaint;
	private Paint mWhitePaint;
	private Paint mFirstPaint;

	private float centerX;
	private float centerY;
	private final int RADIUS = 20;

	private Context mContext;

	private PorterDuff.Mode mode = PorterDuff.Mode.DST_IN;
	private Xfermode mXfermode;

	private static final int W = 64;
	private static final int H = 64;

	private Bitmap mBitmap;

	// create a bitmap with a circle, used for the "dst" image
	static Bitmap makeDst(int w, int h) {
		Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bm);
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

		p.setColor(0xFFFFCC44);
		c.drawOval(new RectF(0, 0, w*3/4, h*3/4), p);
		return bm;
	}

	// create a bitmap with a rect, used for the "src" image
	static Bitmap makeSrc(int w, int h) {
		Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bm);
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

		p.setColor(0xFF66AAFF);
		c.drawRect(w / 3, h / 3, w * 19 / 20, h * 19 / 20, p);
		return bm;
	}

	private Bitmap makeCircleSrc(int w, int h) {
		Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bm);
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

		p.setColor(0xFF66AAFF);
//		c.drawRect(w/3, h/3, w*19/20, h*19/20, p);
		int radius = Math.min(w, h);
		c.drawOval(new RectF(0, 0, radius, radius), p);
		return bm;
	}

	public MutiplCircleView(Context context) {
		this(context, null);
	}

	public MutiplCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setLayerType(LAYER_TYPE_SOFTWARE, null);

		this.mContext = context;

		mSrcB = makeSrc(W, H);
		mDstB = makeDst(W, H);

		initPaint();
	}

	private void initPaint() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.argb(255, 255, 128, 103));

		mFirstPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mFirstPaint.setStyle(Paint.Style.FILL);
		mFirstPaint.setColor(Color.argb(255, 255, 128, 103));

		centerX = Utils.getScreenWidth(mContext) / 2;
		centerY = Utils.getScreenHeight(mContext) / 2;

		mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mWhitePaint.setStyle(Paint.Style.FILL);
		mWhitePaint.setColor(Color.WHITE);

		mXfermode = new PorterDuffXfermode(mode);

		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aa);

	}

	@Override
	protected void onDraw(Canvas canvas) {
//		final Bitmap dstBitmap = makeDst(W, H);
		canvas.drawBitmap(mBitmap, centerX - mBitmap.getWidth() / 2, centerY - mBitmap.getHeight() / 2, mPaint);
		mPaint.setXfermode(mXfermode);
		final Bitmap srcBitmap = makeCircleSrc(mBitmap.getWidth(), mBitmap.getHeight());
		canvas.drawBitmap(srcBitmap, centerX - mBitmap.getWidth()/2, centerY - mBitmap.getHeight()/2, mPaint);
//		canvas.drawColor(Color.WHITE);
//
//		canvas.translate(15, 35);
//		canvas.drawBitmap(mDstB, 0, 0, mPaint);
//		mPaint.setXfermode(mXfermode);
//		canvas.drawBitmap(mSrcB, 0, 0, mPaint);
//		mPaint.setXfermode(null);

//		mFirstPaint.setColor(Color.WHITE);
//		canvas.drawCircle(centerX, centerY, RADIUS + 5, mFirstPaint);
//		mFirstPaint.setColor(Color.argb(255, 255, 128, 103));
//		canvas.drawCircle(centerX, centerY, RADIUS, mFirstPaint);
//
//		mFirstPaint.setColor(Color.GREEN);
//		mFirstPaint.setXfermode(mXfermode);
//		canvas.drawCircle(centerX + 20, centerY + 10, RADIUS + 5, mFirstPaint);
//		mFirstPaint.setColor(Color.argb(255, 255, 128, 103));
//		canvas.drawCircle(centerX + 20, centerY + 10, RADIUS, mFirstPaint);

//		canvas.drawCircle(centerX - 20, centerY + 10, RADIUS + 5, mWhitePaint);
//		canvas.drawCircle(centerX - 20, centerY + 10, RADIUS, mPaint);
//
//		canvas.drawCircle(centerX - 10, centerY + 20, RADIUS + 5, mWhitePaint);
//		canvas.drawCircle(centerX - 10, centerY + 20, RADIUS, mPaint);
//
//		canvas.drawCircle(centerX + 10, centerY + 20, RADIUS + 5, mWhitePaint);
//		canvas.drawCircle(centerX + 10, centerY + 20, RADIUS, mPaint);


	}
}
