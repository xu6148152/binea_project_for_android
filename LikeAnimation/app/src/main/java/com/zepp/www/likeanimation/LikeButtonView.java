package com.zepp.www.likeanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by xubinggui on 12/25/15.
 */
public class LikeButtonView extends FrameLayout implements View.OnClickListener {

    public final String TAG = getClass().getCanonicalName();

    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR =
            new DecelerateInterpolator();
    private static final AccelerateDecelerateInterpolator ACCELERATE_DECELERATE_INTERPOLATOR =
            new AccelerateDecelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR =
            new OvershootInterpolator(4);

    ImageView ivStar;
    DotsView dotsView;
    CircleView circleView;

    boolean isChecked = false;

    AnimatorSet animationSet;

    public LikeButtonView(Context context) {
        this(context, null);
    }

    public LikeButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LikeButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LikeButtonView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_like_button, this, true);
        ivStar = (ImageView) findViewById(R.id.iv_star);
        dotsView = (DotsView) findViewById(R.id.dots_view);
        circleView = (CircleView) findViewById(R.id.circle_view);
        setOnClickListener(this);
    }

    @Override public void onClick(View view) {
        isChecked = !isChecked;
        ivStar.setImageResource(isChecked ? R.mipmap.ic_star_rate_on : R.mipmap.ic_star_rate_off);

        if (animationSet != null) {
            animationSet.cancel();
        }

        if (isChecked) {
            ivStar.animate().cancel();
            ivStar.setScaleX(0);
            ivStar.setScaleY(0);
            circleView.setmInnerCircleRadiusProgress(0);
            circleView.setmOuterCircleRadiusProgress(0);
            dotsView.setCurrentProgress(0);

            animationSet = new AnimatorSet();

            ObjectAnimator outerCircleAnimator =
                    ObjectAnimator.ofFloat(circleView, CircleView.OUTTER_CIRCLE_RADIUS_PROGRESS,
                            0.1f, 1f);
            outerCircleAnimator.setDuration(250);
            outerCircleAnimator.setInterpolator(DECCELERATE_INTERPOLATOR);

            ObjectAnimator innerCircleAnimator =
                    ObjectAnimator.ofFloat(circleView, CircleView.INNER_CIRCLE_RADIUS_PROGRESS,
                            0.1f, 1f);
            innerCircleAnimator.setDuration(200);
            innerCircleAnimator.setInterpolator(DECCELERATE_INTERPOLATOR);

            ObjectAnimator starScaleYAnimator =
                    ObjectAnimator.ofFloat(ivStar, ImageView.SCALE_Y, 0.2f, 1f);
            starScaleYAnimator.setDuration(350);
            starScaleYAnimator.setStartDelay(250);
            starScaleYAnimator.setInterpolator(OVERSHOOT_INTERPOLATOR);

            ObjectAnimator starScaleXAnimator =
                    ObjectAnimator.ofFloat(ivStar, ImageView.SCALE_X, 0.2f, 1f);
            starScaleXAnimator.setDuration(350);
            starScaleXAnimator.setStartDelay(250);
            starScaleYAnimator.setInterpolator(OVERSHOOT_INTERPOLATOR);

            ObjectAnimator dotsAnimator =
                    ObjectAnimator.ofFloat(dotsView, DotsView.DOTS_PROGRESS, 0, 1f);
            dotsAnimator.setDuration(900);
            dotsAnimator.setStartDelay(50);
            dotsAnimator.setInterpolator(ACCELERATE_DECELERATE_INTERPOLATOR);

            animationSet.playTogether(outerCircleAnimator, innerCircleAnimator, starScaleYAnimator,
                    starScaleXAnimator, dotsAnimator);

            animationSet.addListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationCancel(Animator animation) {
                    circleView.setmInnerCircleRadiusProgress(0);
                    circleView.setmOuterCircleRadiusProgress(0);
                    dotsView.setCurrentProgress(0);
                    ivStar.setScaleX(1);
                    ivStar.setScaleY(1);
                }
            });
            Log.d(TAG, "onclick");
            animationSet.start();
        }
    }

    //@Override public boolean onTouchEvent(MotionEvent event) {
    //    switch (event.getAction()) {
    //        case MotionEvent.ACTION_DOWN:
    //            ivStar.animate()
    //                    .scaleX(0.7f)
    //                    .scaleY(0.7f)
    //                    .setDuration(150)
    //                    .setInterpolator(DECCELERATE_INTERPOLATOR);
    //            setPressed(true);
    //            break;
    //
    //        case MotionEvent.ACTION_MOVE:
    //            float x = event.getX();
    //            float y = event.getY();
    //            boolean isInside = (x > 0 && x < getWidth() && y > 0 && y < getHeight());
    //            if (isPressed() != isInside) {
    //                setPressed(isInside);
    //            }
    //            break;
    //
    //        case MotionEvent.ACTION_UP:
    //            ivStar.animate().scaleY(1).setInterpolator(DECCELERATE_INTERPOLATOR);
    //            if (isPressed()) {
    //                performClick();
    //                setPressed(false);
    //            }
    //            break;
    //    }
    //    return true;
    //}
}
