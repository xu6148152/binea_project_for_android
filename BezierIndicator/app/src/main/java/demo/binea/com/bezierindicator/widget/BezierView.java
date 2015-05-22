package demo.binea.com.bezierindicator.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import demo.binea.com.bezierindicator.widget.model.Point;

/**
 * Created by xubinggui on 5/20/15.
 */
public class BezierView extends View {

	private Paint mPaint;
	private Path mPath;

	private Point mHeadPoint;
	private Point mFootPoint;

	public BezierView(Context context) {
		this(context, null);
	}

	public BezierView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setStrokeWidth(1);
		mPath = new Path();

		mHeadPoint = new Point();
		mFootPoint = new Point();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		makePath();
		canvas.drawPath(mPath, mPaint);
		canvas.drawCircle(mHeadPoint.getX(), mHeadPoint.getY(), mHeadPoint.getRadius(), mPaint);
		canvas.drawCircle(mFootPoint.getX(), mFootPoint.getY(), mFootPoint.getRadius(), mPaint);
	}

	private void makePath() {
		float headOffsetX = (float) (mHeadPoint.getRadius()*Math.sin(Math.atan((mFootPoint.getY()-mHeadPoint.getY()) / (mFootPoint.getX()-mHeadPoint.getX()))));
		float headOffsetY = (float) (mHeadPoint.getRadius()*Math.cos(Math.atan((mFootPoint.getY()-mHeadPoint.getY()) / (mFootPoint.getX()-mHeadPoint.getX()))));

		float footOffsetX = (float) (mFootPoint.getRadius()*Math.sin(Math.atan((mFootPoint.getY()-mHeadPoint.getY()) / (mFootPoint.getX()-mHeadPoint.getX()))));
		float footOffsetY = (float) (mFootPoint.getRadius()*Math.cos(Math.atan((mFootPoint.getY()-mHeadPoint.getY()) / (mFootPoint.getX()-mHeadPoint.getX()))));

		float x1 = mHeadPoint.getX() - headOffsetX;
		float y1 = mHeadPoint.getY() + headOffsetY;

		float x2 = mHeadPoint.getX() + headOffsetX;
		float y2 = mHeadPoint.getY() - headOffsetY;

		float x3 = mFootPoint.getX() - footOffsetX;
		float y3 = mFootPoint.getY() + footOffsetY;

		float x4 = mFootPoint.getX() + footOffsetX;
		float y4 = mFootPoint.getY() - footOffsetY;

		float anchorX = (mFootPoint.getX() + mHeadPoint.getX()) / 2;
		float anchorY = (mFootPoint.getY() + mHeadPoint.getY()) / 2;

		mPath.reset();
		mPath.moveTo(x1, y1);
		mPath.quadTo(anchorX, anchorY, x3, y3);
		mPath.lineTo(x4, y4);
		mPath.quadTo(anchorX, anchorY, x2, y2);
		mPath.lineTo(x1, y1);
	}

	public Point getHeadPoint() {
		return mHeadPoint;
	}

	public Point getFootPoint() {
		return mFootPoint;
	}

	public void setIndicatorColor(int color){
		mPaint.setColor(color);
	}

	public int getIndicatorColor(){
		return mPaint.getColor();
	}

	public void animCreate(){
		setPivotX(getHeadPoint().getX());
		setPivotY(getFootPoint().getY());
		AnimatorSet animatorSet = new AnimatorSet();
		ObjectAnimator oaX = ObjectAnimator.ofFloat(this, "scaleX", 0.3f, 1f);
		ObjectAnimator oaY = ObjectAnimator.ofFloat(this, "scaleY", 0.3f, 1f);
		ObjectAnimator oaA = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1f);
		animatorSet.play(oaX).with(oaY).with(oaA);
		animatorSet.setDuration(500);
		animatorSet.setInterpolator(new OvershootInterpolator());
		animatorSet.setStartDelay(300);
		animatorSet.start();
	}
}
