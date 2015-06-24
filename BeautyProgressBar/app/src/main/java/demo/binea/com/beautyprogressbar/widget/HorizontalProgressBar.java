package demo.binea.com.beautyprogressbar.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import demo.binea.com.beautyprogressbar.R;
import demo.binea.com.beautyprogressbar.utils.DimenUtil;

/**
 * Created by xubinggui on 15/3/8.
 */
public class HorizontalProgressBar extends ProgressBar {

	private static final int DEFAULT_TEXT_SIZE = 10;
	private static final int DEFAULT_TEXT_COLOR = 0XFFFC00D1;
	private static final int DEFAULT_COLOR_UNREACHED_COLOR = 0xFFd3d6da;
	private static final int DEFAULT_HEIGHT_REACHED_PROGRESS_BAR = 2;
	private static final int DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR = 2;
	private static final int DEFAULT_SIZE_TEXT_OFFSET = 10;

	/**
	 * painter of all drawing things
	 */
	protected Paint mPaint = new Paint();
	/**
	 * color of progress number
	 */
	protected int mTextColor = DEFAULT_TEXT_COLOR;
	/**
	 * size of text (sp)
	 */
	protected int mTextSize = DimenUtil.dp2px(DEFAULT_TEXT_SIZE, getContext());

	/**
	 * offset of draw progress
	 */
	protected int mTextOffset = DimenUtil.dp2px(DEFAULT_SIZE_TEXT_OFFSET, getContext());

	/**
	 * height of reached progress bar
	 */
	protected int mReachedProgressBarHeight = DimenUtil.dp2px(DEFAULT_HEIGHT_REACHED_PROGRESS_BAR, getContext());

	/**
	 * color of reached bar
	 */
	protected int mReachedBarColor = DEFAULT_TEXT_COLOR;
	/**
	 * color of unreached bar
	 */
	protected int mUnReachedBarColor = DEFAULT_COLOR_UNREACHED_COLOR;
	/**
	 * height of unreached progress bar
	 */
	protected int mUnReachedProgressBarHeight = DimenUtil.dp2px(DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR, getContext());
	/**
	 * view width except padding
	 */
	protected int mRealWidth;

	protected boolean mIfDrawText = true;

	protected static final int VISIBLE = 0;

	public HorizontalProgressBar(Context context) {
		this(context, null);
	}

	public HorizontalProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		setHorizontalScrollBarEnabled(true);

		obtainStyledAttributes(attrs);

		mPaint.setTextSize(mTextSize);
		mPaint.setColor(mTextColor);
	}

	private void obtainStyledAttributes(AttributeSet attrs) {
		final TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBar);

		mTextColor = attributes
				.getColor(
						R.styleable.HorizontalProgressBar_progress_text_color,
						DEFAULT_TEXT_COLOR);
		mTextSize = (int) attributes.getDimension(
				R.styleable.HorizontalProgressBar_progress_text_size,
				mTextSize);

		mReachedBarColor = attributes
				.getColor(
						R.styleable.HorizontalProgressBar_progress_reached_color,
						mTextColor);
		mUnReachedBarColor = attributes
				.getColor(
						R.styleable.HorizontalProgressBar_progress_unreached_color,
						DEFAULT_COLOR_UNREACHED_COLOR);
		mReachedProgressBarHeight = (int) attributes
				.getDimension(
						R.styleable.HorizontalProgressBar_progress_reached_bar_height,
						mReachedProgressBarHeight);
		mUnReachedProgressBarHeight = (int) attributes
				.getDimension(
						R.styleable.HorizontalProgressBar_progress_unreached_bar_height,
						mUnReachedProgressBarHeight);
		mTextOffset = (int) attributes
				.getDimension(
						R.styleable.HorizontalProgressBar_progress_text_offset,
						mTextOffset);

		int textVisible = attributes
				.getInt(R.styleable.HorizontalProgressBar_progress_text_visibility,
						VISIBLE);
		if (textVisible != VISIBLE)
		{
			mIfDrawText = false;
		}
		attributes.recycle();
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		if (heightMode != MeasureSpec.EXACTLY){

			float textHeight = (mPaint.descent() + mPaint.ascent());
			int exceptHeight = (int) (getPaddingTop() + getPaddingBottom() + Math
					.max(Math.max(mReachedProgressBarHeight,
							mUnReachedProgressBarHeight), Math.abs(textHeight)));

			heightMeasureSpec = MeasureSpec.makeMeasureSpec(exceptHeight,
					MeasureSpec.EXACTLY);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		canvas.save();
		canvas.translate(getPaddingLeft(), getHeight()/2);

		boolean isNeedBg = false;
		float ratio = getProgress() * 1.0f / getMax();

		float currentProgress = (int)(mRealWidth * ratio);

		String text = currentProgress + "%";

		float textWidth = mPaint.measureText(text);

		float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;

		if(currentProgress + textWidth > mRealWidth){
			currentProgress = mRealWidth - textWidth;
			isNeedBg = true;
		}

		float endX = currentProgress - mTextOffset / 2;

		//draw progress
		if(endX > 0){
			mPaint.setColor(mReachedBarColor);
			mPaint.setStrokeWidth(mReachedProgressBarHeight);
			canvas.drawLine(0, 0, endX, 0, mPaint);
		}

		//draw text
		if (mIfDrawText)
		{
			mPaint.setColor(mTextColor);
			canvas.drawText(text, currentProgress, -textHeight, mPaint);
		}

		if (!isNeedBg)
		{
			float start = currentProgress + mTextOffset / 2 + textWidth;
			mPaint.setColor(mUnReachedBarColor);
			mPaint.setStrokeWidth(mUnReachedProgressBarHeight);
			canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
		}

		canvas.restore();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mRealWidth = w - getPaddingLeft() - getPaddingRight();
	}

}
