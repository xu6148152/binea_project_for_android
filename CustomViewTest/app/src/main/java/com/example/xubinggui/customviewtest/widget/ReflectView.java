package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.xubinggui.customviewtest.R;
import com.example.xubinggui.customviewtest.util.Utils;

/**
 * Created by xubinggui on 15/4/5.
 */
public class ReflectView extends View {

	private Context mContext;
	private Paint mPaint;

	private Bitmap mSrcBitmap, mRefBitmap;
	private PorterDuffXfermode mXfermode;
	private float x;
	private float y;

	public ReflectView(Context context) {
		this(context, null);
	}

	public ReflectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initPaint();
	}

	private void initPaint() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);

		x = Utils.getScreenWidth(mContext) / 2;
		y = Utils.getScreenHeight(mContext) / 2;

		mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aa);

		mPaint.setShader(new LinearGradient(x, y + mSrcBitmap.getHeight(), x, y + mSrcBitmap.getHeight() + mSrcBitmap.getHeight() / 4, 0xAA000000, Color.TRANSPARENT, Shader.TileMode.CLAMP));

		Matrix matrix = new Matrix();
		matrix.setScale(1F, -1F);

		mRefBitmap = Bitmap.createBitmap(mSrcBitmap, 0, 0, mSrcBitmap.getWidth(), mSrcBitmap.getHeight(), matrix, true);

		mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mSrcBitmap, x, y, null);

		int sc = canvas.saveLayer(x, y + mSrcBitmap.getHeight(), x + mRefBitmap.getWidth(), y + mSrcBitmap.getHeight() * 2, null, Canvas.ALL_SAVE_FLAG);

		canvas.drawBitmap(mRefBitmap, x, y + mSrcBitmap.getHeight(), null);

		mPaint.setXfermode(mXfermode);

		canvas.drawRect(x, y + mSrcBitmap.getHeight(), x + mRefBitmap.getWidth(), y + mSrcBitmap.getHeight() * 2, mPaint);
		mPaint.setXfermode(null);
		canvas.restoreToCount(sc);
	}
}
