package demo.binea.com.bezierviewdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by xubinggui on 15/4/19.
 */
public class BezierView extends View {

	private final float DEFAULT_RADIUS = 100;

	private int mScreenW;
	private int mScreenH;

	private Paint mPaint;
	private Path path;

	private Rect mRect;

	private boolean isClickRect = false;

	private float radius = DEFAULT_RADIUS;

	float x = 300;
	float y = 300;

	float anchorX = 200;
	float anchorY = 300;

	float startX = 100;
	float startY = 100;

	boolean isAnimStart = false;

	public BezierView(Context context) {
		this(context, null);
	}

	public BezierView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
		mPaint.setColor(Color.RED);
		mRect = new Rect();
		path = new Path();
	}

	private void calculate(){
		float distance = (float) Math.sqrt(Math.pow(y-startY, 2) + Math.pow(x-startX, 2));
		radius = -distance/15+DEFAULT_RADIUS;

		if(radius < 9){
			isAnimStart = true;
		}

		float offsetX = (float) (radius*Math.sin(Math.atan((y - startY) / (x - startX))));
		float offsetY = (float) (radius*Math.cos(Math.atan((y - startY) / (x - startX))));

		float x1 = startX - offsetX;
		float y1 = startY + offsetY;

		float x2 = x - offsetX;
		float y2 = y + offsetY;

		float x3 = x + offsetX;
		float y3 = y - offsetY;

		float x4 = startX + offsetX;
		float y4 = startY - offsetY;

		path.reset();
		path.moveTo(x1, y1);
		path.quadTo(anchorX, anchorY, x2, y2);
		path.lineTo(x3, y3);
		path.quadTo(anchorX, anchorY, x4, y4);
		path.lineTo(x1, y1);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		startX = getMeasuredWidth()/2;
		startY = getMeasuredHeight()/2;
		if(isClickRect) {
			calculate();
			canvas.drawPath(path, mPaint);
			canvas.drawCircle(startX, startY, radius, mPaint);
			canvas.drawCircle(x, y, radius, mPaint);
		}else{
			radius = DEFAULT_RADIUS;
			canvas.drawCircle(startX, startY, radius, mPaint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			anchorX = startX;
			anchorY = startY;
			mRect.left = (int) (getMeasuredWidth()/2 - DEFAULT_RADIUS);
			mRect.top = (int) (getMeasuredHeight()/2 - DEFAULT_RADIUS);
			mRect.right = (int) (getMeasuredWidth()/2 + DEFAULT_RADIUS);
			mRect.bottom = (int) (getMeasuredHeight()/2 + DEFAULT_RADIUS);

			if (mRect.contains((int) event.getX(), (int)event.getY())){
				isClickRect = true;

			}
		}else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
			isClickRect = false;
		}
		invalidate();
		if(isAnimStart){
			return super.onTouchEvent(event);
		}

		anchorX =  (event.getX() + startX)/2;
		anchorY =  (event.getY() + startY)/2;
		x =  event.getX();
		y =  event.getY();

		return true;

	}

	public Point getScreenW(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		final Display defaultDisplay = wm.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		defaultDisplay.getMetrics(dm);
		Point point = new Point();
//		defaultDisplay.getSize(point);
		point.x = dm.widthPixels;
		point.y = dm.heightPixels;
		return point;
	}
}
