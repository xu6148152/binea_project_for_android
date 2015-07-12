package demo.binea.com.mediacodecdemo.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by xubinggui on 7/12/15.
 */
public class AutoFitSurfaceView extends GLSurfaceView {

    protected int mRatioWidth = 0;
    protected int mRatioHeight = 0;

    public AutoFitSurfaceView(Context context) {
        this(context, null);
    }

    public AutoFitSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        if(mRatioWidth == 0 || mRatioHeight == 0){
            setMeasuredDimension(width, height);
        }else{
            setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
        }
    }
}
