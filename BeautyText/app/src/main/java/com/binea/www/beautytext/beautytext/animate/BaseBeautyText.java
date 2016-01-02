package com.binea.www.beautytext.beautytext.animate;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import com.binea.www.beautytext.beautytext.BeautyText;
import com.binea.www.beautytext.beautytext.utils.CharacterUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xubinggui on 1/1/16.
 */
public abstract class BaseBeautyText implements IBeautyText {
    public final String TAG = this.getClass().getCanonicalName();
    Paint mNewPaint, mOldPaint;

    protected float[] newGaps = new float[100];
    protected float[] oldGaps = new float[100];

    protected float mTextSize;

    protected CharSequence mOldText;
    protected CharSequence mNewText;

    List<CharacterDiffResult> mDifferentCharList = new ArrayList<>();

    protected float mOldStartX = 0;
    protected float mNewStartX = 0;
    protected float mNewStartY = 0;

    protected BeautyText mBeautyText;

    @Override public void init(BeautyText textView, AttributeSet attrs, int defStyle) {
        mBeautyText = textView;

        mNewPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNewPaint.setStyle(Paint.Style.FILL);
        mNewPaint.setColor(mBeautyText.getCurrentTextColor());

        mOldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOldPaint.setStyle(Paint.Style.FILL);
        mOldPaint.setColor(mBeautyText.getCurrentTextColor());

        mNewText = mOldText = mBeautyText.getText();

        mTextSize = mBeautyText.getTextSize();

        initValue();

        mBeautyText.postDelayed(new Runnable() {
            @Override public void run() {
                prepareAnimate();
            }
        }, 50);
    }

    protected void prepareAnimate() {
        mTextSize = mBeautyText.getTextSize();
        mNewPaint.setTextSize(mTextSize);
        for (int i = 0; i < mNewText.length(); i++) {
            newGaps[i] = mNewPaint.measureText(String.valueOf(mNewText.charAt(i)));
        }

        mOldPaint.setTextSize(mTextSize);
        for (int i = 0; i < mOldText.length(); i++) {
            oldGaps[i] = mOldPaint.measureText(String.valueOf(mOldText.charAt(i)));
        }

        mOldStartX = (mBeautyText.getMeasuredWidth()
                - mBeautyText.getTotalPaddingLeft()
                - mOldPaint.measureText(String.valueOf(mOldText))) / 2f;
        Log.d(TAG, "mOldStartX " + mOldStartX);
        mNewStartX = (mBeautyText.getMeasuredWidth()
                - mBeautyText.getTotalPaddingLeft()
                - mNewPaint.measureText(String.valueOf(mNewText))) / 2f;
        Log.d(TAG, "mNewStartX " + mNewStartX);

        mNewStartY = mBeautyText.getBaseline();

        mDifferentCharList.clear();
        mDifferentCharList.addAll(CharacterUtils.diff(mOldText, mNewText));

    }

    protected abstract void initValue();

    @Override public void init(BeautyText textView, AttributeSet attrs) {
        init(textView, attrs, 0);
    }

    @Override public void animateText(CharSequence str) {
        mBeautyText.setText(str);
        mOldText = mNewText;
        mNewText = str;
        prepareAnimate();
        animatePrepare(str);
        animateStart(str);
    }

    protected abstract void animateStart(CharSequence str);

    protected abstract void animatePrepare(CharSequence str);

    @Override public void onDraw(Canvas canvas) {
        drawFrame(canvas);
    }

    protected abstract void drawFrame(Canvas canvas);

    @Override public void reset(CharSequence str) {
        animatePrepare(str);
        mBeautyText.postInvalidate();
    }
}
