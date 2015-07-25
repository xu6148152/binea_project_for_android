package demo.binea.com.fitchart.widget;

import android.graphics.RectF;

/**
 * Created by xubinggui on 7/24/15.
 */
abstract class BaseRenderer {
    private final RectF mDrawingArea;
    private final FitChartValue mValue;

    FitChartValue getValue() {
        return mValue;
    }

    RectF getDrawingArea() {
        return mDrawingArea;
    }

    public BaseRenderer(RectF drawingArea, FitChartValue value) {
        this.mDrawingArea = drawingArea;
        this.mValue = value;
    }
}
