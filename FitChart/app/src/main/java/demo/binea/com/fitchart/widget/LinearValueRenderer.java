package demo.binea.com.fitchart.widget;

import android.graphics.Path;
import android.graphics.RectF;

/**
 * Created by xubinggui on 7/24/15.
 */
public class LinearValueRenderer extends BaseRenderer implements Render {
    public LinearValueRenderer(RectF drawingArea, FitChartValue value) {
        super(drawingArea, value);
    }

    @Override public Path buildPath(float animationProgress, float animationSeek) {
        return null;
    }
}
