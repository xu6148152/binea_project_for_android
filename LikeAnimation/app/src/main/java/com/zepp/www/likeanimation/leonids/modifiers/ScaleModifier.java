package com.zepp.www.likeanimation.leonids.modifiers;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.zepp.www.likeanimation.leonids.Particle;

/**
 * Created by xubinggui on 12/27/15.
 */
public class ScaleModifier implements ParticleModifier {

    private float mInitialValue;
    private float mFinalValue;
    private long mEndTime;
    private long mStartTime;
    private long mDuration;
    private float mValueIncrement;
    private Interpolator mInterpolator;

    public ScaleModifier(float initialValue, float finalValue, long startMilis, long endMilis,
            Interpolator interpolator) {
        mInitialValue = initialValue;
        mFinalValue = finalValue;
        mStartTime = startMilis;
        mEndTime = endMilis;
        mDuration = mEndTime - mStartTime;
        mValueIncrement = mFinalValue - mInitialValue;
        mInterpolator = interpolator;
    }

    public ScaleModifier(float initialValue, float finalValue, long startMilis, long endMilis) {
        this(initialValue, finalValue, startMilis, endMilis, new LinearInterpolator());
    }

    @Override public void apply(Particle particle, long miliseconds) {
        if (miliseconds < mStartTime) {
            particle.mScale = mInitialValue;
        } else if (miliseconds > mEndTime) {
            particle.mScale = mFinalValue;
        } else {
            particle.mScale = mInitialValue + mValueIncrement * mInterpolator.getInterpolation(
                    miliseconds - mStartTime / mDuration);
        }
    }
}
