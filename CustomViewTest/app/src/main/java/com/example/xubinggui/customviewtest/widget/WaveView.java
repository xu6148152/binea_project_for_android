package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xubinggui on 15/4/5.
 */
public class WaveView extends View {
	private Path mPath;
	private Paint mPaint;

	private int vWidth, vHeight;
	private float ctrX, ctrY;
	private float waveY;

	private boolean isInc;

	public WaveView(Context context) {
		this(context, null);
	}

	public WaveView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
		mPaint.setColor(0xFFA2D6AE);
		mPath = new Path();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		vWidth = w;
		vHeight = h;

		// 计算控制点Y坐标
		waveY = 1 / 8F * vHeight;

		// 计算端点Y坐标
		ctrY = -1 / 16F * vHeight;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mPath.moveTo(-1 / 4F * vWidth, waveY);
		mPath.quadTo(ctrX, ctrY, vWidth + 1 / 4F * vWidth, waveY);

		mPath.lineTo(vWidth + 1 / 4F * vWidth, vHeight);
		mPath.lineTo(-1 / 4F * vWidth, vHeight);
		mPath.close();

		canvas.drawPath(mPath, mPaint);

		if (ctrX >= vWidth + 1 / 4F * vWidth) {
			isInc = false;
		}

		else if (ctrX <= -1 / 4F * vWidth) {
			isInc = true;
		}

		ctrX = isInc ? ctrX + 20 : ctrX - 20;

		if (ctrY <= vHeight) {
			ctrY += 2;
			waveY += 2;
		}

		mPath.reset();

		invalidate();
	}
}
