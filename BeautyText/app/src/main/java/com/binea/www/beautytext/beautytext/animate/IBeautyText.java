package com.binea.www.beautytext.beautytext.animate;

import android.graphics.Canvas;
import android.util.AttributeSet;
import com.binea.www.beautytext.beautytext.BeautyText;

/**
 * Created by xubinggui on 1/1/16.
 */
public interface IBeautyText {
    void init(BeautyText textView, AttributeSet attrs, int defStyle);
    void init(BeautyText textView, AttributeSet attrs);
    void animateText(CharSequence str);
    void onDraw(Canvas canvas);
    void reset(CharSequence str);
}
