package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.example.xubinggui.customviewtest.R;

/**
 * Created by xubinggui on 15/1/2.
 */
public class CustomViewTestCircle extends View {

	private final String TAG = CustomViewTestCircle.class.getSimpleName();
	private Paint mPaint;

	private Point mPoint;

	private float radius = 100;

	private Bitmap bitmap;

	private Handler hander = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			invalidate();
		}
	};
	public CustomViewTestCircle(Context context) {
		this(context,null);
	}

	public CustomViewTestCircle(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomViewTestCircle(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		final Display defaultDisplay = window.getDefaultDisplay();
		mPoint = new Point();
		defaultDisplay.getSize(mPoint);
		Log.d(TAG,"screen width " + mPoint.x + " screen height " + mPoint.y);
		initPaint();

		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aa);


//		new Thread(){
//			@Override
//			public void run() {
//				while(radius ++ < mPoint.x/2){
//					SystemClock.sleep(1000);
//					setRadius(radius);
//					hander.sendEmptyMessage(0);
//				}
//			}
//		}.start();
	}

	private void initPaint() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.DARKEN));
//		ColorMatrix matrix = new ColorMatrix(new float[]{
//				0.33F, 0.59F, 0.11F, 0, 0,
//				0.33F, 0.59F, 0.11F, 0, 0,
//				0.33F, 0.59F, 0.11F, 0, 0,
//				0, 0, 0, 1, 0,
//		});
//		mPaint.setColorFilter(new ColorMatrixColorFilter(matrix));
//		mPaint.setColor(Color.argb(255, 255, 128, 103));
//		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//		mPaint.setStrokeWidth(10);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

//		canvas.drawCircle(mPoint.x / 2f, mPoint.y / 2f, radius, mPaint);
		canvas.drawBitmap(bitmap,0,0,mPaint);
	}

	public void setRadius(float radius){
		if(radius > 0) {
			this.radius = radius;
		}else{
			throw new RuntimeException("radius can not less than 0");
		}
	}
}
