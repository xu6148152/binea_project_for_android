package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.xubinggui.customviewtest.R;

/**
 * Created by xubinggui on 15/1/2.
 */

public class PortDuffView extends View {
	/*
     * PorterDuff模式常量
     * 可以在此更改不同的模式测试
     */
	private static final PorterDuff.Mode MODE = PorterDuff.Mode.DST_IN;

	private static final int RECT_SIZE_SMALL = 400;// 左右上方示例渐变正方形的尺寸大小
	private static final int RECT_SIZE_BIG = 800;// 中间测试渐变正方形的尺寸大小

	private Paint mPaint;// 画笔

	private PorterDuffXfermode porterDuffXfermode;// 图形混合模式

	private Point mPoint;

	private int screenW, screenH;// 屏幕尺寸
	private int s_l, s_t;// 左上方正方形的原点坐标
	private int d_l, d_t;// 右上方正方形的原点坐标
	private int rectX, rectY;// 中间正方形的原点坐标

	private Bitmap bgBitmap;
	private Bitmap fgBitmap;

	private Path path;

	private float preX;
	private float preY;

	private static final int MIN_MOVE_DIS = 5;// 最小的移动距离：如果我们手指在屏幕上的移动距离小于此值则不会绘制

	private Canvas mCanvas;
	public PortDuffView(Context context) {
		this(context,null);
	}

	public PortDuffView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public PortDuffView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		calu(context);

		init(context);


	}

	private void init(Context context) {
		path = new Path();

		// 实例化画笔并设置抗锯齿
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

		mPaint.setARGB(128, 255, 0, 0);

		// 实例化混合模式
		porterDuffXfermode = new PorterDuffXfermode(MODE);

		mPaint.setXfermode(porterDuffXfermode);

		// 设置画笔风格为描边
		mPaint.setStyle(Paint.Style.STROKE);

		// 设置路径结合处样式
		mPaint.setStrokeJoin(Paint.Join.ROUND);

		// 设置笔触类型
		mPaint.setStrokeCap(Paint.Cap.ROUND);

		// 设置描边宽度
		mPaint.setStrokeWidth(50);

		fgBitmap = Bitmap.createBitmap(screenW,screenH, Bitmap.Config.ARGB_8888);

		mCanvas = new Canvas(fgBitmap);

		mCanvas.drawColor(0xFF808080);

		bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aa);

		bgBitmap = Bitmap.createScaledBitmap(bgBitmap,screenW,screenH,true);
	}

	private void calu(Context context) {
		mPoint = new Point();
		// 获取包含屏幕尺寸的数组
		WindowManager win = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		win.getDefaultDisplay().getSize(mPoint);
		// 获取屏幕尺寸
		screenW = mPoint.x;
		screenH = mPoint.y;

		// 计算左上方正方形原点坐标
		s_l = 0;
		s_t = 0;

		// 计算右上方正方形原点坐标
		d_l = screenW - RECT_SIZE_SMALL;
		d_t = 0;

		// 计算中间方正方形原点坐标
		rectX = screenW / 2 - RECT_SIZE_BIG / 2;
		rectY = RECT_SIZE_SMALL + (screenH - RECT_SIZE_SMALL) / 2 - RECT_SIZE_BIG / 2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(bgBitmap,0,0,null);

		canvas.drawBitmap(fgBitmap,0,0,null);

		mCanvas.drawPath(path,mPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
		switch (action){
			case MotionEvent.ACTION_DOWN:
				path.reset();
				path.moveTo(x,y);
				preX = x;
				preY = y;
				break;

			case MotionEvent.ACTION_MOVE:
				float dx = Math.abs(x - preX);
				float dy = Math.abs(y - preY);
				if (dx >= MIN_MOVE_DIS || dy >= MIN_MOVE_DIS) {
//					path.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);
					path.lineTo(x,y);
					preX = x;
					preY = y;
				}
				break;
		}

		invalidate();
		return true;
	}
}
