package com.example.xubinggui.shadertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by xubinggui on 15/1/3.
 */
public class BrikView extends View {
	private final String TAG = BrikView.class.getSimpleName();
	private Paint mFillPaint, mStrokePaint;// 填充和描边的画笔
	private Shader mBitmapShader;// Bitmap着色器

	private static final int RECT_SIZE = 400;// 矩形尺寸的一半

	private int left, top, right, bottom;// 矩形坐上右下坐标

	private float posX, posY;// 触摸点的XY坐标

	public BrikView(Context context) {
		this(context, null);
	}

	public BrikView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context);
	}

	private void init(Context context) {

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);

		int screenX = dm.widthPixels / 2;
		int screenY = dm.heightPixels / 2;

		Log.d(TAG, "screenX = " + screenX);
		Log.d(TAG,"screenY = " + screenY);

		// 计算矩形左上右下坐标值
		left = screenX - RECT_SIZE;
		top = screenY - RECT_SIZE;
		right = screenX + RECT_SIZE;
		bottom = screenY + RECT_SIZE;

		/*
         * 实例化描边画笔并设置参数
         */
		mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mStrokePaint.setColor(0xFF000000);
		mStrokePaint.setStyle(Paint.Style.STROKE);
		mStrokePaint.setStrokeWidth(5);

		// 实例化填充画笔
		mFillPaint = new Paint();

        /*
         * 生成BitmapShader
         */
		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aa);
//		mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
		mBitmapShader = new LinearGradient(left,top,right,bottom,new int[] { Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE }, new float[] { 0, 0.1F, 0.5F, 0.7F, 0.8F }, Shader.TileMode.REPEAT);
		mFillPaint.setShader(mBitmapShader);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
        /*
         * 手指移动时获取触摸点坐标并刷新视图
         */
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			posX = event.getX();
			posY = event.getY();

			invalidate();
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		// 设置画笔背景色
//		canvas.drawColor(Color.DKGRAY);
//
//        /*
//         * 绘制圆和描边
//         */
//		canvas.drawCircle(posX, posY, 300, mFillPaint);
//		canvas.drawCircle(posX, posY, 300, mStrokePaint);

		canvas.drawRect(left,top,right,bottom,mFillPaint);
	}
}
