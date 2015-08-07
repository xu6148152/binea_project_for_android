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
        Path path = null;
        if(getValue().getStartAngle() <= animationSeek){
            path = new Path();
            float sweepAngle = calculateSweepAngle(animationSeek, getValue());
            path.addArc(getDrawingArea(), getValue().getStartAngle(), sweepAngle);
        }
        return path;
    }

    private float calculateSweepAngle(float animationSeek, FitChartValue value) {
        float result;
        float totalSizeOfValue = value.getStartAngle() + value.getSweepAngle();
        if(totalSizeOfValue > animationSeek){
            result = animationSeek - value.getStartAngle();
        }else{
            result = value.getSweepAngle();
        }
        return result;
    }
}
