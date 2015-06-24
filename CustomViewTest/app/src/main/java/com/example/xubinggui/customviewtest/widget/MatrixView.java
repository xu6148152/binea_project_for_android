package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.xubinggui.customviewtest.R;
import com.example.xubinggui.customviewtest.util.Utils;

/**
 * Created by xubinggui on 15/4/5.
 */
public class MatrixView extends View {
	private static final int RECT_SIZE = 400;
	private Paint mPaint;
	private int left, top, right, bottom;
	private int screenX, screenY;
	private Context mContext;

	public MatrixView(Context context) {
		this(context, null);
	}

	public MatrixView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	private void init() {
		screenX = (int) (Utils.getScreenWidth(mContext) / 2);
		screenY = (int) (Utils.getScreenHeight(mContext) / 2);

		left = screenX - RECT_SIZE;
		top = screenY - RECT_SIZE;
		right = screenX + RECT_SIZE;
		bottom = screenY + RECT_SIZE;

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aa);
		BitmapShader mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		Matrix matrix = new Matrix();
		matrix.setTranslate(500, 500);
		mBitmapShader.setLocalMatrix(matrix);
		mPaint.setShader(mBitmapShader);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawRect(0, 0, screenX * 2, screenY * 2, mPaint);
	}
}
