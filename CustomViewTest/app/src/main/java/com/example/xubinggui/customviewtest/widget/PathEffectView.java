package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xubinggui on 15/4/4.
 */
public class PathEffectView extends View {

	private PathEffect[] mEffects;

	private Paint mPaint;
	private Context mContext;

	private Path mPath;
	private float mPhase;

	public PathEffectView(Context context) {
		this(context, null);
	}

	public PathEffectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;

		initPaint();
	}

	private void initPaint() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(Color.RED);

		mPath = new Path();

		mPath.moveTo(0, 0);

		for(int i = 0;i<=30;i++){
			mPath.lineTo(i*35, (float)Math.random()*100);
		}

		mEffects = new PathEffect[7];
		mEffects[0] = null;
		mEffects[1] = new CornerPathEffect(10);
		mEffects[2] = new DiscretePathEffect(3.0F, 5.0F);
		mEffects[3] = new DashPathEffect(new float[] { 20, 10, 5, 10 }, mPhase);
		Path path = new Path();
		path.addRect(0, 0, 8, 8, Path.Direction.CCW);
		mEffects[4] = new PathDashPathEffect(path, 12, mPhase, PathDashPathEffect.Style.ROTATE);
		mEffects[5] = new ComposePathEffect(mEffects[2], mEffects[4]);
		mEffects[6] = new SumPathEffect(mEffects[4], mEffects[3]);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		for(int i = 0;i<mEffects.length;i++){
			mPaint.setPathEffect(mEffects[i]);
			canvas.drawPath(mPath, mPaint);
			canvas.translate(0,250);
		}

		mPhase++;
		invalidate();
	}
}
