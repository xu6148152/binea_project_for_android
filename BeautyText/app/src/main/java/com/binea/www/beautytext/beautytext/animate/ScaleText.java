package com.binea.www.beautytext.beautytext.animate;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import com.binea.www.beautytext.beautytext.utils.CharacterUtils;

/**
 * Created by xubinggui on 1/1/16.
 */
public class ScaleText extends BaseBeautyText {

    private int mMostCount = 20;
    private int mCharTime = 400;
    private long mDuration = 2000;
    private float mProgress;

    @Override protected void initValue() {

    }

    @Override protected void animateStart(CharSequence str) {
        int n = str.length();
        n = n < 0 ? 1 : n;
        mDuration = (long) (mCharTime + mCharTime / mMostCount * (n - 1));
        ValueAnimator animator = ValueAnimator.ofFloat(0, mDuration).setDuration(mDuration);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                mProgress = (float) animation.getAnimatedValue();
                mBeautyText.postInvalidate();
            }
        });
        animator.start();
    }

    @Override protected void animatePrepare(CharSequence str) {

    }

    @Override protected void drawFrame(Canvas canvas) {
        float newOffset = mNewStartX;
        float oldOffset = mOldStartX;

        int maxLength = Math.max(mOldText.length(), mNewText.length());

        for (int i = 0; i < maxLength; i++) {

            //draw old text
            if (i < mOldText.length()) {
                float percent = mProgress / mDuration;
                int move = CharacterUtils.needMove(i, mDifferentCharList);
                Log.d(TAG, "move " + move);
                if (move != -1) {
                    mOldPaint.setTextSize(mTextSize);
                    mOldPaint.setAlpha(255);

                    float p = percent * 2f;
                    p = p > 1 ? 1 : p;

                    float distX =
                            CharacterUtils.getOffset(i, move, p, mNewStartX, mOldStartX, newGaps,
                                    oldGaps);
                    canvas.drawText(String.valueOf(mOldText.charAt(i)), 0, 1, distX, mNewStartY,
                            mOldPaint);
                }
            }
            oldOffset += oldGaps.get(i);

            // draw new text
            if (i < mNewText.length()) {

                if (!CharacterUtils.stayHere(i, mDifferentCharList)) {

                    int alpha = (int) (255f / mCharTime * (mProgress - mCharTime * i / mMostCount));
                    if (alpha > 255) alpha = 255;
                    if (alpha < 0) alpha = 0;

                    float size =
                            mTextSize * 1f / mCharTime * (mProgress - mCharTime * i / mMostCount);
                    if (size > mTextSize) size = mTextSize;
                    if (size < 0) size = 0;

                    mNewPaint.setAlpha(alpha);
                    mNewPaint.setTextSize(size);

                    float width = mNewPaint.measureText(mNewText.charAt(i) + "");
                    canvas.drawText(mNewText.charAt(i) + "", 0, 1,
                            newOffset + (newGaps.get(i) - width) / 2, mNewStartY, mNewPaint);
                }

                newOffset += newGaps.get(i);
            }
        }
    }
}
