package com.example.xubinggui.shadertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by xubinggui on 15/1/3.
 */
public class ShaderView extends View {

	private final String TAG = ShaderView.class.getSimpleName();
	private static final int RECT_SIZE = 400;// 矩形尺寸的一半

	private Paint mPaint;// 画笔

	private int left, top, right, bottom;// 矩形坐上右下坐标

	public ShaderView(Context context) {
		this(context, null);
	}

	public ShaderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ShaderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		init(context);
	}

	private void init(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);

		int screenX = dm.widthPixels / 2;
		int screenY = dm.heightPixels / 2;

		Log.d(TAG,"screenX = " + screenX);
		Log.d(TAG,"screenY = " + screenY);

		// 计算矩形左上右下坐标值
		left = screenX - RECT_SIZE;
		top = screenY - RECT_SIZE;
		right = screenX + RECT_SIZE;
		bottom = screenY + RECT_SIZE;

		// 实例化画笔
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

		// 获取位图
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aa);

		// 设置着色器
		mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawRect(left,top,right,bottom,mPaint);
	}
}
