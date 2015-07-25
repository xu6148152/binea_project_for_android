package demo.binea.com.fitchart.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import demo.binea.com.fitchart.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xubinggui on 7/7/15.
 */
public class FitChart extends View {

    private List<FitChartValue> mChartValues;

    private int mValueStrokeColor = getResources().getColor(R.color.default_chart_value_color);
    private int mBackStrokeColor = getResources().getColor(R.color.default_back_color);
    private int mStrokeSize = getResources().getDimensionPixelSize(R.dimen.default_stroke_size);

    private AnimationMode mAnimationMode = AnimationMode.LINEAR;

    private final int ANIMATION_MODE_LINEAR = 0;
    private final int ANIMATION_MODE_OVERDRAW = 1;

    private Paint mBackStrokePaint;
    private Paint mValueDesignPaint;

    private Path mBackPath = new Path();

    private RectF mDrawingArea;

    private final int DEFAULT_VALUE_RADIUS = 0;
    private final int DEFAULT_MIN_VALUES = 0;
    private final int DEFAULT_MAX_VALUES = 100;

    private final int START_ANGLE = -90;
    private final int ANIMATION_DURATION = 1000;
    private final float INITIAL_ANIMATION_PROGRESS = 0.0f;
    private final float MAXIMUM_SWEEP_ANGLE = 360f;
    private final int DESIGN_MODE_SWEEP_ANGLE = 216;

    private float mAnimationProgress = INITIAL_ANIMATION_PROGRESS;
    private float mMaxSweepAngle = MAXIMUM_SWEEP_ANGLE;

    public FitChart(Context context) {
        this(context, null);
    }

    public FitChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FitChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(attrs);
    }

    private void initializeView(AttributeSet attrs) {
        mChartValues = new ArrayList<>();
        initializeBackground();
        readAttributes(attrs);
        initPaints();
    }

    private void initPaints() {
        mBackStrokePaint = getPaint();
        mBackStrokePaint.setColor(mBackStrokeColor);
        mBackStrokePaint.setStyle(Paint.Style.STROKE);
        mBackStrokePaint.setStrokeWidth(mStrokeSize);
        mValueDesignPaint = getPaint();
        mValueDesignPaint.setColor(mValueStrokeColor);
        mValueDesignPaint.setStyle(Paint.Style.STROKE);
        mValueDesignPaint.setStrokeCap(Paint.Cap.ROUND);
        mValueDesignPaint.setStrokeJoin(Paint.Join.ROUND);
        mValueDesignPaint.setStrokeWidth(mStrokeSize);
    }

    private Paint getPaint() {
        if(!isInEditMode()){
            return new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        return new Paint();
    }

    private void readAttributes(AttributeSet attrs) {
        if(null != attrs){
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.FitChart, 0, 0);
            mStrokeSize = ta.getDimensionPixelSize(R.styleable.FitChart_strokeSize, mStrokeSize);
            mValueStrokeColor = ta.getColor(R.styleable.FitChart_valueStrokeColor,
                    mValueStrokeColor);
            mBackStrokeColor = ta.getColor(R.styleable.FitChart_backStrokeColor, mBackStrokeColor);
            int animationMode = ta.getInteger(R.styleable.FitChart_animationMode, ANIMATION_MODE_LINEAR);
            if(animationMode == ANIMATION_MODE_LINEAR){
                mAnimationMode = AnimationMode.LINEAR;
            }else{
                mAnimationMode = AnimationMode.OVERDRAW;
            }
            ta.recycle();
        }
    }

    private void initializeBackground() {
        if(!isInEditMode()){
            if(getBackground() == null){
                setBackgroundColor(getContext().getResources().getColor(R.color.default_back_color));
            }
        }
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.max(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        renderBack(canvas);
        renderValue(canvas);
    }

    private void renderValue(Canvas canvas) {
        if(!isInEditMode()){
            int valueCount = mChartValues.size() - 1;
            for(int index = valueCount; index >= 0; index--){
                renderValue(canvas, mChartValues.get(index));
            }
        }else{
            renderValue(canvas, null);
        }
    }

    private void renderValue(Canvas canvas, FitChartValue fitChartValue) {
        if(!isInEditMode()){
            float animationSeek = calculateAnimationSeek();

        }
    }

    private float calculateAnimationSeek() {
        return mMaxSweepAngle * mAnimationProgress + START_ANGLE;
    }

    private void renderBack(Canvas canvas) {
        final float viewRadius = getViewRadius();
        mBackPath.addCircle(mDrawingArea.centerX(), mDrawingArea.centerY(), viewRadius, Path.Direction.CW);
        canvas.drawPath(mBackPath, mBackStrokePaint);
    }

    private float getViewRadius(){
        if(mDrawingArea != null){
            return mDrawingArea.width() / 2;
        }
        return DEFAULT_VALUE_RADIUS;
    }
}
