package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.xubinggui.customviewtest.R;
import com.example.xubinggui.customviewtest.util.Utils;

/**
 * Created by xubinggui on 15/4/5.
 */
public class DreamEffectView extends View {

	private Context mContext;
	private Paint mPaint;
	private Paint shaderPaint;

	private Bitmap mSrcBitmap, mRefBitmap;
	private PorterDuffXfermode mXfermode;
	private float x;
	private float y;

	public DreamEffectView(Context context) {
		this(context, null);
	}

	public DreamEffectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initPaint();
	}

	private void initPaint() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);

		x = Utils.getScreenWidth(mContext) / 2;
		y = Utils.getScreenHeight(mContext) / 2;

		mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aa);

		mPaint.setColorFilter(new ColorMatrixColorFilter(new float[]{0.8587F, 0.2940F, -0.0927F, 0, 6.79F, 0.0821F, 0.9145F, 0.0634F, 0, 6.79F, 0.2019F, 0.1097F, 0.7483F, 0, 6.79F, 0, 0, 0, 1, 0}));
//		mPaint.setShader(new LinearGradient(x, y + mSrcBitmap.getHeight(), x, y + mSrcBitmap.getHeight() + mSrcBitmap.getHeight() / 4, 0xAA000000, Color.TRANSPARENT, Shader.TileMode.CLAMP));

		mRefBitmap = Bitmap.createBitmap(mSrcBitmap.getWidth(), mSrcBitmap.getHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(mRefBitmap);

		float radiu = canvas.getHeight() * (2F / 3F);
		RadialGradient radialGradient = new RadialGradient(canvas.getWidth() / 2F, canvas.getHeight() / 2F, radiu, new int[] { 0, 0, 0xAA000000 }, new float[] { 0F, 0.7F, 1.0F }, Shader.TileMode.CLAMP);

		Matrix matrix = new Matrix();

		matrix.setScale(canvas.getWidth() / (radiu * 2F), 1.0F);
		matrix.preTranslate(((radiu * 2F) - canvas.getWidth()) / 2F, 0);

		radialGradient.setLocalMatrix(matrix);

		mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SCREEN);

		shaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		shaderPaint.setShader(radialGradient);

		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), shaderPaint);
	}

	@Override
	protected void onDraw(Canvas canvas) {


		int sc = canvas.saveLayer(x, y, x + mSrcBitmap.getWidth(), y + mSrcBitmap.getHeight(), null, Canvas.ALL_SAVE_FLAG);

		canvas.drawColor(0xcc1c093e);
		mPaint.setXfermode(mXfermode);
		canvas.drawBitmap(mSrcBitmap, x, y, mPaint);
		mPaint.setXfermode(null);
		canvas.restoreToCount(sc);
		canvas.drawBitmap(mRefBitmap, x, y, null);
//		canvas.drawBitmap(mRefBitmap, x, y + mSrcBitmap.getHeight(), null);
//
//		mPaint.setXfermode(mXfermode);
//
//		canvas.drawRect(x, y + mSrcBitmap.getHeight(), x + mRefBitmap.getWidth(), y + mSrcBitmap.getHeight() * 2, mPaint);
//		mPaint.setXfermode(null);
//		canvas.restoreToCount(sc);
	}
}
