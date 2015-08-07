package demo.binea.com.fitchart.widget;

import android.graphics.RectF;

/**
 * Created by xubinggui on 8/7/15.
 */
abstract class BaseRenderer {
    private final RectF drawingArea;
    private final FitChartValue value;

    FitChartValue getValue() {
        return value;
    }

    RectF getDrawingArea() {
        return drawingArea;
    }

    public BaseRenderer(RectF drawingArea, FitChartValue value) {
        this.drawingArea = drawingArea;
        this.value = value;
    }
}
