package com.zepp.www.likeanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by xubinggui on 12/26/15.
 */
public class SmallBang extends View {
    public final String TAG = getClass().getCanonicalName();

    int[] colors = {0xFFDF4288, 0xFFCD8BF8, 0XFF2B9DF2, 0XFFA4EEB4, 0XFFE097CA, 0XFFCAACC6, 0XFFC5A5FC, 0XFFF5BC16, 0XFFF2DFC8, 0XFFE1BE8E, 0XFFC8C79D};
    List<Dot> dotList = new ArrayList<>();
    private final long ANIMATE_DURATION = 1000;
    private final float MAX_RADIUS = 150;
    private final float MAX_CIRCLE_RADIUS = 100;
    private float mProgress;
    private Paint mCirclePaint;
    private final float RING_WIDTH = 10;
    private float P1 = 0.15f;
    private float P2 = 0.28f;
    private float P3 = 0.30f;
    private final int DOT_NUMBER = 16;
    private int mDotNumbers = DOT_NUMBER;
    private final float DOT_BIG_RADIUS = 8;
    private float mDotBigRadius = DOT_BIG_RADIUS;
    private final float DOT_SMALL_RADIUS = 5;
    private float mDotSmallRadius = DOT_SMALL_RADIUS;
    private int[] mExpandInset = new int[2];
    private SmallBangListener mListener;
    private int mCenterY;
    private int mCenterX;
    private float mMaxRadius = MAX_RADIUS;
    private float mMaxCircleRadius = MAX_CIRCLE_RADIUS;
    private float mRingWidth = RING_WIDTH;
    private ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();
    private AnimatorSet mAnimatorSet = new AnimatorSet();

    public SmallBang(Context context) {
        this(context, null);
    }

    public SmallBang(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmallBang(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SmallBang(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public static SmallBang attach2Window(Activity activity) {
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        SmallBang smallBang = new SmallBang(activity);
        viewGroup.addView(smallBang, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return smallBang;
    }

    private void init() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.BLACK);
    }

    public void setSmallBangListener(SmallBangListener listener) {
        this.mListener = listener;
    }

    public void setDotColors(int[] colors) {
        this.colors = Arrays.copyOf(colors, colors.length);
    }

    public void setDotNumbers(int numbers) {
        this.mDotNumbers = numbers;
    }

    public void bang(View view, SmallBangListener listener) {
        bang(view, -1, listener);
    }

    public void bang(final View view, int radius, SmallBangListener listener) {
        if(mAnimatorSet == null) {
            mAnimatorSet = new AnimatorSet();
        }
        mAnimatorSet.cancel();

        if(listener != null) {
            setSmallBangListener(listener);
            mListener.onAnimationStart();
        }

        Rect r = new Rect();
        view.getGlobalVisibleRect(r);
        int[] location = new int[2];
        getLocationOnScreen(location);
        r.offset(-location[0], -location[1]);
        r.inset(-mExpandInset[0], -mExpandInset[1]);

        mCenterX = r.left + r.width() / 2;
        mCenterY = r.top + r.height() / 2;

        if (radius != -1) {
            initRadius(radius);
        }else {
            initRadius(Math.max(r.width(), r.height()));
        }

        view.setScaleY(0.0f);
        view.setScaleY(0.0f);

        ObjectAnimator objectXAnimator = ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1f);
        ObjectAnimator objectYAnimator = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1f);
        //ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(
        //        (long) (ANIMATE_DURATION * 0.5));
        //valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        //    @Override public void onAnimationUpdate(ValueAnimator animation) {
        //        final float fraction = animation.getAnimatedFraction();
        //        view.setScaleX(0.1f + fraction * 0.9f);
        //        view.setScaleY(0.1f + fraction * 0.9f);
        //    }
        //});
        //valueAnimator.setInterpolator(new OvershootInterpolator(2));
        //valueAnimator.setStartDelay((long) (ANIMATE_DURATION * P3));
        //
        //valueAnimator.start();
        mAnimatorSet.setDuration((long) (ANIMATE_DURATION * 0.5));
        mAnimatorSet.setInterpolator(new OvershootInterpolator(2));
        mAnimatorSet.setStartDelay((long) (ANIMATE_DURATION * P3));
        mAnimatorSet.playTogether(objectXAnimator, objectYAnimator);
        mAnimatorSet.start();
        bang();

    }

    private void bang() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, PROGRESS, 0f, 1f).setDuration(ANIMATE_DURATION);
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                if(null != mListener) {
                    mListener.onAnimationEnd();
                }
            }
        });
        //ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(ANIMATE_DURATION);
        //valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        //    @Override public void onAnimationUpdate(ValueAnimator animation) {
        //        mProgress = (float) animation.getAnimatedValue();
        //        invalidate();
        //    }
        //});
        //
        //valueAnimator.start();
        //valueAnimator.addListener(new AnimatorListenerAdapter() {
        //
        //    @Override public void onAnimationEnd(Animator animation) {
        //        if(null != mListener) {
        //            mListener.onAnimationEnd();
        //        }
        //    }
        //});

        initDots();
    }

    private void initDots() {
        Random random = new Random(System.currentTimeMillis());
        for(int i = 0; i < mDotNumbers * 2; i++) {
            Dot dot = new Dot();
            dot.startColor = colors[random.nextInt(9999) % colors.length];
            dot.endColor = colors[random.nextInt(9999) % colors.length];
            dotList.add(dot);
        }
    }

    public void bang(View view) {
        bang(view, null);
    }

    private void initRadius(int radius) {
        this.mMaxCircleRadius = radius;
        this.mMaxRadius = this.mMaxCircleRadius * 1.1f;
        this.mDotBigRadius = this.mMaxRadius * 0.07f;
        this.mDotSmallRadius = this.mDotBigRadius * 0.5f;
    }

    @Override protected void onDraw(Canvas canvas) {
        Log.d(TAG, "ondraw");
        if (mProgress > 0 && mProgress <= P1) {
            float progress1 = 1f / P1 * mProgress;
            if(progress1 > 1) {
                progress1 = 1;
            }
            int startColor = colors[0];
            int endColor = colors[1];
            mCirclePaint.setStyle(Paint.Style.FILL);
            mCirclePaint.setColor(
                    (Integer) mArgbEvaluator.evaluate(progress1, startColor, endColor));
            canvas.drawCircle(mCenterX, mCenterY, mMaxCircleRadius * progress1, mCirclePaint);
        }else if(mProgress > P1) {
            if(mProgress > P1 && mProgress <= P3) {
                float progress2 = (mProgress - P1) / (P3 - P1);
                if(progress2 < 0) {
                    progress2 = 0;
                }
                if(progress2 > 1) {
                    progress2 = 1;
                }

                mCirclePaint.setStyle(Paint.Style.STROKE);
                float strokeWidth = mMaxCircleRadius * (1-progress2);
                mCirclePaint.setStrokeWidth(strokeWidth);
                canvas.drawCircle(mCenterX, mCenterY, mMaxCircleRadius * progress2 + strokeWidth / 2, mCirclePaint);
            }

            if(mProgress >= P2) {
                mCirclePaint.setStyle(Paint.Style.FILL);
                float progress3 = (mProgress - P2) / (1 - P2);
                float r = mMaxCircleRadius + progress3 * (mMaxRadius - mMaxCircleRadius);

                for(int i = 0; i< dotList.size(); i+=2) {
                    Dot dot = dotList.get(i);
                    mCirclePaint.setColor(
                            (Integer) mArgbEvaluator.evaluate(progress3, dot.startColor, dot.endColor));

                    float x = (float) (r * Math.cos(i * 2 * Math.PI / DOT_NUMBER)) + mCenterX;
                    float y = (float) (r * Math.sin(i * 2 * Math.PI / DOT_NUMBER)) + mCenterY;
                    canvas.drawCircle(x, y, DOT_BIG_RADIUS * (1 - progress3), mCirclePaint);

                    Dot dot2 = dotList.get(i + 1);

                    mCirclePaint.setColor(
                            (Integer) mArgbEvaluator.evaluate(progress3, dot2.startColor, dot2.endColor));
                    float x2 = (float) (r * Math.cos(i * 2 * Math.PI / DOT_NUMBER + 0.2)) + mCenterX;
                    float y2 = (float) (r * Math.sin(i * 2 * Math.PI / DOT_NUMBER + 0.2)) + mCenterY;
                    canvas.drawCircle(x2, y2, DOT_SMALL_RADIUS * (1 - progress3), mCirclePaint);
                }
            }
        }
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float mProgress) {
        this.mProgress = mProgress;
        postInvalidate();
    }

    private Property<SmallBang, Float> PROGRESS = new Property<SmallBang, Float>(Float.class, " mProgress") {
        @Override public Float get(SmallBang object) {
            Log.d(TAG, "mprogress " + object.getProgress());
            return getProgress();
        }

        @Override public void set(SmallBang object, Float value) {
            Log.d(TAG, "mprogress " + value);
            object.setProgress(value);
        }
    };
}
