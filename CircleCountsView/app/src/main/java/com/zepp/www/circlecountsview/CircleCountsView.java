package com.zepp.www.circlecountsview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Xubinggui on 7/02/16.
 */
public class CircleCountsView extends View {

    private int mValue;
    private Paint mPaint;
    private Paint mUnitPaint;
    private Paint mDescriptionPaint;
    private float mCenterX;
    private float mCenterY;

    private int mStrokeWidth;
    private float mRadius;
    private int mValueTextSize;
    private int mUnitTextSize;

    private int circel_bg_normal = Color.argb(12, 255, 255, 255);
    private int circel_bg_select = Color.argb(255, 209, 238, 0);
    private int mMax = 1;

    private String mDescriptionText = "";
    private String mUnitString = "";

    private int mSwingTextcolor = -1;
    private int mValueColor = Color.BLACK;
    private int mCircleFillColor = Color.TRANSPARENT;
    private boolean isShowUnit = true;

    private RectF mCircleArea;

    private float mDescriptionTextSize = 0;

    private Typeface mDescriptionTypeface;

    private int mDescriptionColor;

    public CircleCountsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mUnitPaint = new Paint();
        mUnitPaint.setAntiAlias(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mDescriptionPaint = new Paint();
        mDescriptionPaint.setAntiAlias(true);
        mCircleArea = new RectF();
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleCountsView);
        mMax = ta.getInteger(R.styleable.CircleCountsView_ccv_max, 100);
        mValue = ta.getInteger(R.styleable.CircleCountsView_ccv_progress, 0);
        mDescriptionText = ta.getString(R.styleable.CircleCountsView_ccv_textString);
        mUnitString = ta.getString(R.styleable.CircleCountsView_ccv_unitString);
        mSwingTextcolor = ta.getColor(R.styleable.CircleCountsView_ccv_textColor, Color.BLACK);
        mValueColor = ta.getColor(R.styleable.CircleCountsView_ccv_unitColor, Color.BLACK);
        circel_bg_select = ta.getColor(R.styleable.CircleCountsView_ccv_progressColor,
                                       getResources().getColor(R.color.common_green));
        circel_bg_normal = ta.getColor(R.styleable.CircleCountsView_ccv_emptyColor,
                                       getResources().getColor(R.color.common_dark_1_10alpha));
        mValueTextSize = ta.getDimensionPixelOffset(R.styleable.CircleCountsView_ccv_textSize, 32);
        mUnitTextSize = ta.getDimensionPixelOffset(R.styleable.CircleCountsView_ccv_unitSize, 16);

        mDescriptionTextSize =
                ta.getDimensionPixelOffset(R.styleable.CircleCountsView_ccv_description_text_size,
                                           12);
        mDescriptionColor = ta.getColor(R.styleable.CircleCountsView_ccv_description_text_color,
                                        getResources().getColor(R.color.common_dark_2));
        mDescriptionText = ta.getString(R.styleable.CircleCountsView_ccv_description_text);
        mStrokeWidth = ta.getDimensionPixelOffset(R.styleable.CircleCountsView_ccv_stroke_width, 5);
        ta.recycle();
    }

    public void setSwingText(String swingText) {
        this.mDescriptionText = swingText;
    }

    public void setSwingTextColor(int color) {
        this.mSwingTextcolor = color;
    }

    public void setSwingCounts(int counts, int total) {
        if (counts < 0) {
            counts = 0;
        }
        if (total <= 0) {
            total = 1;
        }
        if (counts > total) {
            counts = total;
        }
        this.mValue = counts;
        this.mMax = total;
        invalidate();
    }

    public void setUnitString(String unit) {
        this.mUnitString = unit;
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //底色
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleFillColor);
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(circel_bg_normal);
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
        mCircleArea.set(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius,
                        mCenterY + mRadius);
        mPaint.setColor(circel_bg_select);
        canvas.drawArc(mCircleArea, -90, mValue * 360 / mMax, false, mPaint);
        mPaint.setStyle(Paint.Style.FILL);

        float x = 0, y = 0, totalTextHeight = 0, unitTextWidth = 0, descriptionTextWidth = 0,
                unitTop = 0;
        //value
        mPaint.setTextSize(mValueTextSize);
        float valueTextWidth = mPaint.measureText(String.valueOf(mValue));
        Paint.FontMetricsInt valueFontMetricsInt = mPaint.getFontMetricsInt();
        mPaint.setColor(mValueColor);

        totalTextHeight = valueFontMetricsInt.bottom - valueFontMetricsInt.top;
        //unit
        if (!TextUtils.isEmpty(mUnitString)) {
            mUnitPaint.setTextSize(mUnitTextSize);
            Paint.FontMetricsInt unitFontMetricsInt = mUnitPaint.getFontMetricsInt();
            totalTextHeight =
                    Math.max(unitFontMetricsInt.bottom - unitFontMetricsInt.top, totalTextHeight);
            unitTextWidth = mUnitPaint.measureText(mUnitString);
        }

        if (!TextUtils.isEmpty(mDescriptionText)) {
            //description
            mDescriptionPaint.setTextSize(mDescriptionTextSize);
            Paint.FontMetricsInt descriptionMetricsInt = mDescriptionPaint.getFontMetricsInt();
            descriptionTextWidth = mDescriptionPaint.measureText(mDescriptionText);
            mDescriptionPaint.setColor(mDescriptionColor);
            totalTextHeight += descriptionMetricsInt.bottom - descriptionMetricsInt.top;
            unitTop = descriptionMetricsInt.top;
        }

        x = mCenterX - (valueTextWidth + unitTextWidth) / 2;

        y = mCenterY - totalTextHeight / 2 - valueFontMetricsInt.top;

        canvas.drawText(String.valueOf(mValue), x, y, mPaint);

        if (!TextUtils.isEmpty(mUnitString)) {
            canvas.drawText(mUnitString, x + valueTextWidth, y, mUnitPaint);
        }

        if (!TextUtils.isEmpty(mDescriptionText)) {
            x = mCenterX - descriptionTextWidth / 2;
            y = y - unitTop;
            canvas.drawText(mDescriptionText, x, y, mDescriptionPaint);
        }
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStrokeWidth = WindowUtil.dip2px(getContext(), 5);
        mCenterX = getMeasuredWidth() * 0.5f;
        mCenterY = getMeasuredHeight() * 0.5f;
        mRadius = getMeasuredWidth() * 0.45f - mStrokeWidth * 0.5f;
    }
}
