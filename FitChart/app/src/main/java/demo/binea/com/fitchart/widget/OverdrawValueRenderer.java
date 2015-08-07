package demo.binea.com.fitchart.widget;

import android.graphics.Path;
import android.graphics.RectF;

/**
 * Created by xubinggui on 7/24/15.
 */
public class OverdrawValueRenderer extends BaseRenderer implements Render {
    public OverdrawValueRenderer(RectF drawingArea, FitChartValue value) {
        super(drawingArea, value);
    }

    @Override public Path buildPath(float animationProgress, float animationSeek) {
        float startAngle = FitChart.START_ANGLE;
        float valueSweepAngle = getValue().getStartAngle() + getValue().getSweepAngle();
        valueSweepAngle -= startAngle;
        float sweepAngle = valueSweepAngle * animationProgress;
        Path path = new Path();
        path.addArc(getDrawingArea(), startAngle, sweepAngle);
        return path;
    }
}
