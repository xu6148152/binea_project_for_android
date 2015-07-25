package demo.binea.com.fitchart.widget;

import android.graphics.RectF;

/**
 * Created by xubinggui on 7/24/15.
 */
public class RendererFactory {
    public static Render getRenderer(AnimationMode mode, FitChartValue value, RectF drawingArea) {
        if (mode == AnimationMode.LINEAR) {
            return new LinearValueRenderer(drawingArea, value);
        } else {
            return new OverdrawValueRenderer(drawingArea, value);
        }
    }
}
