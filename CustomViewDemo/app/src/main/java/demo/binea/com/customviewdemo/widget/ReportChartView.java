package com.zepp.newsports.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import com.zepp.newsports.R;
import com.zepp.z3a.common.util.FontUtil;
import com.zepp.z3a.common.util.LogUtil;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xubinggui on 15/6/29.
 */
public class ReportChartView extends View {

    private static final String TAG = ReportChartView.class.getCanonicalName();
    private int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14,
            getResources().getDisplayMetrics());
    private int monthTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13,
            getResources().getDisplayMetrics());
    private final int LEFTMARGIN = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 23,
            getResources().getDisplayMetrics());
    private int leftMargin;

    private String[] months;

    private Paint linePaint;
    private Paint rectPaint;
    private Paint textPaint;
    private Paint monthPaint;
    private Paint blockPaint;
    private ValueAnimator mAnimatorY;
    protected float mRevealValue = 1.0f;

    private List<Float> columnDataPosition = new ArrayList<>();

    public ReportChartView(Context context) {
        super(context);
        init();
    }

    public ReportChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReportChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.parseColor("#A3A3A3"));

        blockPaint = new Paint(linePaint);
        blockPaint.setColor(Color.parseColor("#FF353535"));

        rectPaint = new Paint(linePaint);
        rectPaint.setColor(Color.parseColor("#1967CA"));

        textPaint = new Paint(linePaint);
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.parseColor("#A3A3A3"));

        monthPaint = new Paint(textPaint);
        monthPaint.setTypeface(
                FontUtil.getInstance().getFontTypeface(getContext(), FontUtil.FONT_TUNGSTEN_BOOK));
        monthPaint.setTextAlign(Paint.Align.CENTER);
        monthPaint.setTextSize(monthTextSize);
        monthPaint.setColor(Color.parseColor("#A3A3A3"));
        months = new String[] {
                getString(R.string.str0_103), getString(R.string.str0_104),
                getString(R.string.str0_105), getString(R.string.str0_106),
                getString(R.string.str0_107), getString(R.string.str0_108),
                getString(R.string.str0_109), getString(R.string.str0_110),
                getString(R.string.str0_111), getString(R.string.str0_112),
                getString(R.string.str0_113), getString(R.string.str0_114)
        };
        data = new ArrayList<Float>();
    }

    private String getString(int id) {
        return getContext().getString(id);
    }

    @Override protected void onDraw(Canvas canvas) {
        drawLines(canvas);
        drawMonths(canvas);
    }

    private float startLineX = 0;
    private float endLineX = 0;
    private float unitY = 0;

    private void drawLines(Canvas canvas) {
        linePaint.setStrokeWidth(1);
        for (int i = 1; i < 6; i++) {
            canvas.drawLine(startLineX, i * unitY, endLineX, i * unitY, linePaint);
        }
        linePaint.setStrokeWidth(3);
        canvas.drawLine(startLineX, unitY, startLineX, 5 * unitY, linePaint);
    }

    // 计算最大文本宽度,高度,左右Pad  来确定 startLineX
    private float textW;
    private float textH;
    //默认为Text Pading 5dp
    private int textPadDip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6,
            getResources().getDisplayMetrics());
    private float max = 1600f;
    private int txtPad;

    private int chartColumnWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27,
            getResources().getDisplayMetrics());
    private int columnIntervalWidth =
            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17,
                    getResources().getDisplayMetrics());

    private void initValue() {
        float tmp = 0.00f;
        for (float item : data) {
            tmp = Math.max(tmp, item);
        }
        if (tmp <= 0.04f) {
            tmp = 0.04f;
        }
        while (tmp * 100 % 4 != 0) {
            tmp += 0.01;
        }
        max = tmp;

        textW = (int) textPaint.measureText(String.valueOf(max));
        txtPad = textPadDip;
        leftMargin = LEFTMARGIN;
        startLineX = leftMargin + textW + txtPad;
        Rect bounds = new Rect();
        textPaint.getTextBounds(String.valueOf(max), 0, String.valueOf(max).length(), bounds);
        textH = bounds.height();
    }

    private void drawMonths(Canvas canvas) {
        int columnStartX = (int) (startLineX + offsetX);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(FontUtil.getInstance()
                .getFontTypeface(getContext(), FontUtil.FONT_TUNGSTEN_SEMIBOLD));
        textPaint.setColor(Color.BLACK);
        rectPaint.setColor(getResources().getColor(R.color.yellow));
        for (int i = 0; i < data.size(); i++) {
            float startx = columnStartX + i * chartColumnWidth + columnIntervalWidth * (i + 1);
            columnDataPosition.add(startx + chartColumnWidth);
            float textX = startx + chartColumnWidth * 0.5f;
            canvas.drawText(months[i], textX, getHeight() * 0.9f + unitY * 0.2f, monthPaint);
            float tmp = data.get(i) * unitY * 4f / max * mRevealValue;// 柱状图高度
            float h = unitY * 5 - tmp;
            canvas.drawRect(startx, h, startx + chartColumnWidth, unitY * 5f, rectPaint);

            //if (data.get(i) > 0) {
            if (tmp > textH * 1.5) {// 绘制在柱状图里面
                textPaint.setColor(Color.BLACK);
                canvas.drawText(String.valueOf(data.get(i)), textX, h + 1.5f * textH, textPaint);
            } else {// 柱状图上面
                textPaint.setColor(Color.WHITE);
                canvas.drawText(String.valueOf(data.get(i)), textX, h - 0.2f * textH, textPaint);
            }
            //}
        }
        rectPaint.setColor(getResources().getColor(R.color.common_bg));
        canvas.drawRect(0, 0, startLineX, getHeight(), rectPaint);
        //canvas.drawRect(endLineX, 0, getWidth(), getHeight(), blockPaint);
        textPaint.setTypeface(
                FontUtil.getInstance().getFontTypeface(getContext(), FontUtil.FONT_TUNGSTEN_BOOK));
        textPaint.setTextAlign(Paint.Align.RIGHT);
        textPaint.setColor(Color.parseColor("#A3A3A3"));
        float m = max / 4;
        DecimalFormat df = new DecimalFormat("0.00");
        String txt = null;
        for (int i = 1; i < 6; i++) {
            if (i == 5) {
                txt = "0";
            } else {
                txt = df.format((5 - i) * m);
            }

            canvas.drawText(txt, startLineX - textPadDip, i * unitY + 0.5f * textH, textPaint);
        }
    }

    private List<Float> data = null;

    public List<Float> getData() {
        return data;
    }

    public void setData(List<Float> data) {
        this.data = data;
        offsetX = 0;
        max = 1600;
        invalidate();
    }

    private float offsetX;
    private float oldX;

    //最大长度
    float maxL = 13.5f;

    float startY;
    float startX;

    @Override public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldX = event.getRawX();
                startX = event.getRawX();
                startY = event.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getRawX();
                offsetX += x - oldX;
                LogUtil.LOGD(TAG, "offsetX");
                oldX = x;
                if (Math.abs(event.getRawY() - startY) > Math.abs(event.getRawX() - startX)) {
                    if (getParent() instanceof View) {
                        return ((View) getParent()).onTouchEvent(event);
                    }
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                startX = x;
                startY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                oldX = 0;
                break;
        }

        if (offsetX > getWidth() - maxL * (chartColumnWidth + columnIntervalWidth)
                && offsetX < chartColumnWidth * 0.5) {
            invalidate();
        } else {
            offsetX = offsetX > 0 ? chartColumnWidth * 0.5f : getWidth() - maxL * chartColumnWidth;
        }

        return true;
    }

    /** the phase that is animated and influences the drawn values on the y-axis */
    //    protected float mPhaseY = 1f;
    public void animateY(int durationMillis) {

        if (android.os.Build.VERSION.SDK_INT < 11) return;

        mAnimatorY = ValueAnimator.ofFloat(0, 1);
        mAnimatorY.setDuration(durationMillis);
        mAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                mRevealValue = animation.getAnimatedFraction();
                invalidate();
            }
        });
        mAnimatorY.start();
    }

    public void initData(List<Float> datas, List<String> dataUnits) {
        this.data = datas;
        this.months = dataUnits.toArray(this.months);
        maxL = data.size() + 1.5f;
        initValue();

        //animateY(3000);
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        endLineX = getWidth();
        unitY = getHeight() * 0.85f * 0.2f;
    }
}

