package demo.binea.com.textpatheffect.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import demo.binea.com.textpatheffect.utils.MatchPath;

/**
 * Created by xubinggui on 5/29/15.
 */
public class TextPathEffect extends View {
	final String TAG = TextPathEffect.class.getSimpleName();

	private static final float BASE_SQUARE_UNIT = 72f;
	private float mScaleFactor = 1.0f;

	private Paint mPaint;

	private String mText = "hello";

	private List<float[]> mDatas;

	private ObjectAnimator mSvgAnimator;

	private float mPhase;

	private ArrayList<Path> mPaths = new ArrayList<>();

	private final Object mSvgLock = new Object();

	private PATH_TYPE mTYPE;

	public TextPathEffect(Context context) {
		this(context, null);
	}

	public TextPathEffect(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}

	public void setText(String str){
		if(str == null || str.length() == 0){
//			throw new IllegalArgumentException("must set words");
			Toast.makeText(getContext(), "please set text", Toast.LENGTH_SHORT).show();
			return;
		}

		mText = str;
		requestLayout();
//		invalidate();

		mDatas = MatchPath.getPath(mText);

		mSvgAnimator = ObjectAnimator.ofFloat(this, "phase", 0.0f, 1.0f).setDuration(3000);
		mSvgAnimator.start();
	}

	public void initPaint(){
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.BLACK);
		mPaint.setStrokeWidth(2);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	private int measureHeight(int heightMeasureSpec) {
		int result = 0;
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		final int size = MeasureSpec.getSize(heightMeasureSpec);

		if(heightMode == MeasureSpec.EXACTLY){
			return size;
		}else{
			result = (int) (BASE_SQUARE_UNIT * mScaleFactor + getPaddingTop() + getPaddingBottom());
			if(heightMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, size);
			}
		}
		return result;
	}

	private int measureWidth(int widthMeasureSpec) {
		int result = 0;
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int size = MeasureSpec.getSize(widthMeasureSpec);

		if(widthMode == MeasureSpec.EXACTLY){
			return size;
		}else{
			result = (int) (getPaddingLeft() + getPaddingRight() + mText.length() * BASE_SQUARE_UNIT * mScaleFactor);
			if(widthMode == MeasureSpec.AT_MOST){
				result = Math.min(result, size);
			}
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(mPaths == null){
			return;
		}

		synchronized ((mSvgLock)){
			for(int i = 0;i < mPaths.size(); i++){
				canvas.drawPath(mPaths.get(i), mPaint);
			}
		}
	}

	public void setPhase(float phase){
		this.mPhase = phase;

		synchronized (mSvgLock){
			updatePathPhase();
		}
	}

	private void updatePathPhase() {
		mPaths.clear();
		float singleFactor = mPhase * mDatas.size();
		for(int i = 0;i<mDatas.size();i++){
			Path path = new Path();
			path.moveTo(mDatas.get(i)[0], mDatas.get(i)[1]);
			path.lineTo(mDatas.get(i)[2], mDatas.get(i)[3]);

			if(mTYPE == PATH_TYPE.MULTTIPLY){
				PathMeasure pathMeasure = new PathMeasure(path, false);
				Path dst = new Path();
				pathMeasure.getSegment(0.0f, mPhase * pathMeasure.getLength(), dst, true);
				mPaths.add(dst);
			}else{
				if(singleFactor - (i+1) >= -0.01){
					mPaths.add(path);
				}else if(i - Math.floor(singleFactor) < 0.0001){
					Path dst = new Path();
					PathMeasure pathMeasure = new PathMeasure(path, false);
					pathMeasure.getSegment(0.0f, (singleFactor % 1) * pathMeasure.getLength(), dst, true);
					mPaths.add(dst);
				}
			}
		}
		invalidate();
	}

	public float getPhase() {
		return mPhase;
	}

	enum PATH_TYPE{
		SINGLE, MULTTIPLY
	}
}
