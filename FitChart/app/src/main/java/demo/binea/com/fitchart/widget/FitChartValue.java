package demo.binea.com.fitchart.widget;

import android.graphics.Paint;

public class FitChartValue {
    private final float value;
    private final int color;
    private Paint paint;
    private float startAngle;
    private float sweepAngle;

    float getValue() {
        return this.value;
    }

    int getColor() {
        return this.color;
    }

    void setPaint(Paint paint) {
        this.paint = paint;
        this.paint.setColor(color);
    }

    void setStartAngle(float angle) {
        this.startAngle = angle;
    }

    void setSweepAngle(float sweep) {
        this.sweepAngle = sweep;
    }

    float getStartAngle() {
        return this.startAngle;
    }

    float getSweepAngle() {
        return this.sweepAngle;
    }

    Paint getPaint() {
        return this.paint;
    }

    public FitChartValue(float value, int color) {
        this.value = value;
        this.color = color;
    }
}