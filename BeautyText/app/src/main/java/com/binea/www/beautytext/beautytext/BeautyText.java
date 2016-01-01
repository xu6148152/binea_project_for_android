package com.binea.www.beautytext.beautytext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;
import com.binea.www.beautytext.R;
import com.binea.www.beautytext.beautytext.animate.AnvilText;
import com.binea.www.beautytext.beautytext.animate.EvaporateText;
import com.binea.www.beautytext.beautytext.animate.FallText;
import com.binea.www.beautytext.beautytext.animate.IBeautyText;
import com.binea.www.beautytext.beautytext.animate.LineText;
import com.binea.www.beautytext.beautytext.animate.PixelateText;
import com.binea.www.beautytext.beautytext.animate.RainbowText;
import com.binea.www.beautytext.beautytext.animate.ScaleText;
import com.binea.www.beautytext.beautytext.animate.TyperText;

/**
 * Created by xubinggui on 1/1/16.
 */
public class BeautyText extends TextView {
    IBeautyText mBeautyText;
    AttributeSet mAttributeSet;

    public enum BeautyTextType {
        SCALE, EVAPORATE, FALL, PIXELATE, ANVIL, SPARKLE, LINE, TYPER, RAINBOW;

        public int enum2int() {
            switch (this) {
                case SCALE:
                    return 0;
                case EVAPORATE:
                    return 1;
                case FALL:
                    return 2;
                case PIXELATE:
                    return 3;
                case ANVIL:
                    return 4;
                case SPARKLE:
                    return 5;
                case LINE:
                    return 6;
                case TYPER:
                    return 7;
                case RAINBOW:
                    return 8;
            }
            return 0;
        }
    }

    public BeautyText(Context context) {
        this(context, null);
    }

    public BeautyText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BeautyText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mAttributeSet = attrs;
        final TypedArray ta =
                getContext().obtainStyledAttributes(attrs, R.styleable.BeautyText);
        int animateType = ta.getInt(R.styleable.BeautyText_animateType, 0);
        ta.recycle();
        initBeautyText(attrs, animateType);
    }

    private void initBeautyText(AttributeSet attrs, int animateType) {
        switch (animateType) {
            case 0 :
                mBeautyText = new ScaleText();
                break;
            case 1:
                mBeautyText = new EvaporateText();
                break;
            case 2:
                mBeautyText = new FallText();
                break;
            case 3:
                mBeautyText = new PixelateText();
                break;
            case 4:
                mBeautyText = new AnvilText();
                break;
            case 5:
                mBeautyText = new ScaleText();
                break;
            case 6:
                mBeautyText = new LineText();
                break;
            case 7:
                mBeautyText = new TyperText();
                break;
            case 8:
                mBeautyText = new RainbowText();
                break;

            default:
                mBeautyText = new ScaleText();
                break;
        }
        mBeautyText.init(this, attrs);
    }

    public void setAnimateType(BeautyTextType animateType) {
        final int type = animateType.enum2int();
        initBeautyText(mAttributeSet, type);
    }

    @Override protected void onDraw(Canvas canvas) {
        mBeautyText.onDraw(canvas);
    }

    public void reset(CharSequence str) {
        mBeautyText.reset(str);
    }

    public void animateText(CharSequence str) {
        mBeautyText.animateText(str);
    }
}
