package com.zepp.www.likeanimation;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.View;

/**
 * Created by xubinggui on 12/25/15.
 */
public class CircleView extends View {
    public static final String TAG = CircleView.class.getCanonicalName();

    Paint mCirclePaint = new Paint();
    Paint mMaskPaint = new Paint();
    int mMaxCircleSize;
    Bitmap mTempBitmap;
    Canvas mTempCanvas;

    private float mOuterCircleRadiusProgress = 0f;
    private float mInnerCircleRadiusProgress = 0f;

    private ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();

    private static final int START_COLOR = 0xFFFF5722;
    private static final int END_COLOR = 0xFFFFC107;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mCirclePaint.setStyle(Paint.Style.FILL);
        mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMaxCircleSize = w / 2;
        mTempBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mTempCanvas = new Canvas(mTempBitmap);
    }

    @Override protected void onDraw(Canvas canvas) {
        mTempCanvas.drawColor(0xffffff, PorterDuff.Mode.CLEAR);
        mTempCanvas.drawCircle(getWidth() / 2, getHeight() / 2, mOuterCircleRadiusProgress * mMaxCircleSize, mCirclePaint);
        mTempCanvas.drawCircle(getWidth() / 2, getHeight() / 2, mInnerCircleRadiusProgress * mMaxCircleSize, mMaskPaint);
        canvas.drawBitmap(mTempBitmap, 0, 0, null);
    }

    public float getmOuterCircleRadiusProgress() {
        return mOuterCircleRadiusProgress;
    }

    public void setmOuterCircleRadiusProgress(float mOuterCircleRadiusProgress) {
        this.mOuterCircleRadiusProgress = mOuterCircleRadiusProgress;
        updateCircleColor();
        postInvalidate();
    }

    private void updateCircleColor() {
        float colorProgress = (float) Utils.clamp(mOuterCircleRadiusProgress, 0.5, 1);
        colorProgress = (float) Utils.mapValueFromRangeToRange(colorProgress, 0.5f, 1f, 0f, 1f);
        mCirclePaint.setColor((Integer) mArgbEvaluator.evaluate(colorProgress, START_COLOR, END_COLOR));
    }

    public float getmInnerCircleRadiusProgress() {
        return mInnerCircleRadiusProgress;
    }

    public void setmInnerCircleRadiusProgress(float mInnerCircleRadiusProgress) {
        this.mInnerCircleRadiusProgress = mInnerCircleRadiusProgress;
        postInvalidate();
    }

    public static final Property<CircleView, Float> INNER_CIRCLE_RADIUS_PROGRESS = new Property<CircleView, Float>(Float.class, "mInnerCircleRadiusProgress") {
        @Override public Float get(CircleView object) {
            return object.getmInnerCircleRadiusProgress();
        }

        @Override public void set(CircleView object, Float value) {
            Log.d(TAG, "INNER_CIRCLE_RADIUS_PROGRESS " + value);
            object.setmInnerCircleRadiusProgress(value);
        }
    };

    public static final Property<CircleView, Float> OUTTER_CIRCLE_RADIUS_PROGRESS = new Property<CircleView, Float>(Float.class, "mOuterCircleRadiusProgress") {
        @Override public Float get(CircleView object) {
            return object.getmOuterCircleRadiusProgress();
        }

        @Override public void set(CircleView object, Float value) {
            Log.d(TAG, "OUTTER_CIRCLE_RADIUS_PROGRESS " + value);
            object.setmOuterCircleRadiusProgress(value);
        }
    };
}
