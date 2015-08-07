package demo.binea.com.fitchart.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import demo.binea.com.fitchart.R;
import java.util.ArrayList;
import java.util.Collection;
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

    public static final int START_ANGLE = -90;
    private final int ANIMATION_DURATION = 1000;
    private final float INITIAL_ANIMATION_PROGRESS = 0.0f;
    private final float MAXIMUM_SWEEP_ANGLE = 360f;
    private final int DESIGN_MODE_SWEEP_ANGLE = 216;

    private float mAnimationProgress = INITIAL_ANIMATION_PROGRESS;
    private float mMaxSweepAngle = MAXIMUM_SWEEP_ANGLE;

    private float mMinValue = DEFAULT_MIN_VALUES;
    private float mMaxValue = DEFAULT_MAX_VALUES;

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

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDrawableArea();
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
            final Render renderer =
                    RendererFactory.getRenderer(mAnimationMode, fitChartValue, mDrawingArea);
            final Path path = renderer.buildPath(mAnimationProgress, animationSeek);
            if(null != path){
                canvas.drawPath(path, fitChartValue.getPaint());
            }
        }else{
            Path path = new Path();
            path.addArc(mDrawingArea, START_ANGLE, DESIGN_MODE_SWEEP_ANGLE);
            canvas.drawPath(path, mValueDesignPaint);
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

    public void setMinValue(float value){
        mMinValue = value;
    }

    public void setMaxValue(float value){
        mMaxValue = value;
    }

    public float getMinValue(){
        return mMinValue;
    }

    public float getMaxValue(){
        return mMaxValue;
    }

    public void setValue(float value){
        mChartValues.clear();
        FitChartValue chartValue = new FitChartValue(value, mValueStrokeColor);
        chartValue.setPaint(buildPaintForValue());
        chartValue.setStartAngle(START_ANGLE);
        chartValue.setSweepAngle(calculateSweepAngle(value));
        mChartValues.add(chartValue);
        mMaxSweepAngle = chartValue.getSweepAngle();
        playAnimation();
    }

    public void setValues(Collection<FitChartValue> values) {
        mChartValues.clear();
        mMaxSweepAngle = 0;
        float offsetSweepAngle = START_ANGLE;
        for (FitChartValue chartValue : values) {
            float sweepAngle = calculateSweepAngle(chartValue.getValue());
            chartValue.setPaint(buildPaintForValue());
            chartValue.setStartAngle(offsetSweepAngle);
            chartValue.setSweepAngle(sweepAngle);
            mChartValues.add(chartValue);
            offsetSweepAngle += sweepAngle;
            mMaxSweepAngle += sweepAngle;
        }
        playAnimation();
    }

    private void playAnimation() {
        final ObjectAnimator animator =
                ObjectAnimator.ofFloat(this, "animationSeek", 0.0f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setTarget(this);
        animatorSet.play(animator);
        animatorSet.start();
    }

    private float calculateSweepAngle(float value) {
        float chartValueWindow = Math.max(mMinValue, mMaxValue) - Math.min(mMinValue, mMaxValue);
        float chartValueScale = (360f / chartValueWindow);
        return value * chartValueScale;
    }

    private Paint buildPaintForValue(){
        Paint paint = getPaint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mStrokeSize);
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }

    public void setAnimationMode(AnimationMode mode){
        mAnimationMode = mode;
    }

    public void setAnimationSeek(float value){
        mAnimationProgress = value;
        invalidate();
    }

    private void calculateDrawableArea(){
        float drawPadding = (mStrokeSize / 2);
        float width = getWidth();
        float height = getHeight();
        float left = drawPadding;
        float top = drawPadding;
        float right = width - drawPadding;
        float bottom = height - drawPadding;
        mDrawingArea = new RectF(left, top, right, bottom);
    }


}
