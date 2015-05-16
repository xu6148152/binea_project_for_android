package demo.binea.com.materialdesignprogressbar.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import demo.binea.com.materialdesignprogressbar.R;

/**
 * Created by xubinggui on 5/16/15.
 */
public class MaterialDesignProgressBar extends View {
	private static final float INDETERMINANT_MIN_SWEEP = 15F;
	private float currentProgress;
	private float maxProgress;
	private int thickness;
	private boolean isIndeterminate;
	private boolean autostartAnimation;
	private int color;
	private int animDuration;
	private int animSteps;
	private Paint paint;
	private RectF bounds;
	private int size;

	private float indeterminateRotateOffset;
	private float indeterminateSweep;


	// Animation related stuff
	private float actualProgress;
	private float startAngle;
	private ValueAnimator startAngleRotate;
	private ValueAnimator progressAnimator;
	private AnimatorSet indeterminateAnimator;


	public MaterialDesignProgressBar(Context context) {
		this(context, null, 0);
	}

	public MaterialDesignProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MaterialDesignProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs, defStyleAttr);
	}

	private void init(AttributeSet attrs, int defStyleAttr) {
		initAttribute(attrs, defStyleAttr);

		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		updatePaint();

		bounds = new RectF();

		if(autostartAnimation) {
			startAnimation();
		}
	}

	public void startAnimation() {
		resetAnimation();
	}

	private void updatePaint() {
		paint.setColor(color);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(thickness);
//		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
	}

	private void initAttribute(AttributeSet attrs, int defStyleAttr) {
		final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MaterialDesignProgressBar, defStyleAttr, 0);

		Resources resources = getResources();

		// Initialize attributes from styleable attributes
		currentProgress = a.getFloat(R.styleable.MaterialDesignProgressBar_mdp_progress,
				resources.getInteger(R.integer.cpv_default_progress));
		maxProgress = a.getFloat(R.styleable.MaterialDesignProgressBar_mdp_maxProgress,
				resources.getInteger(R.integer.cpv_default_max_progress));
		thickness = a.getDimensionPixelSize(R.styleable.MaterialDesignProgressBar_mdp_thickness,
				resources.getDimensionPixelSize(R.dimen.cpv_default_thickness));
		isIndeterminate = a.getBoolean(R.styleable.MaterialDesignProgressBar_mdp_indeterminate,
				resources.getBoolean(R.bool.cpv_default_is_indeterminate));
		autostartAnimation = a.getBoolean(R.styleable.MaterialDesignProgressBar_mdp_animAutostart,
				resources.getBoolean(R.bool.cpv_default_anim_autostart));

		int accentColor = getContext().getResources().getIdentifier("colorAccent", "attr", getContext().getPackageName());

		// If color explicitly provided
		if (a.hasValue(R.styleable.MaterialDesignProgressBar_mdp_color)) {
			color = a.getColor(R.styleable.MaterialDesignProgressBar_mdp_color, resources.getColor(R.color.cpv_default_color));
		}
		// If using support library v7 accentColor
		else if(accentColor != 0) {
			TypedValue t = new TypedValue();
			getContext().getTheme().resolveAttribute(accentColor, t, true);
			color = t.data;
		}

		// If using native accentColor (SDK >21)
		else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			TypedArray t = getContext().obtainStyledAttributes(new int[] { android.R.attr.colorAccent });
			color = t.getColor(0, resources.getColor(R.color.cpv_default_color));
			t.recycle();
		}
		else {
			//Use default color
			color = resources.getColor(R.color.cpv_default_color);
		}

		animDuration = a.getInteger(R.styleable.MaterialDesignProgressBar_mdp_animDuration,
				resources.getInteger(R.integer.cpv_default_anim_duration));
		animSteps = a.getInteger(R.styleable.MaterialDesignProgressBar_mdp_animSteps,
				resources.getInteger(R.integer.cpv_default_anim_steps));
		a.recycle();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int paddingX = getPaddingLeft() + getPaddingRight();
		int paddingY = getPaddingTop() + getPaddingBottom();
		int width = getMeasuredWidth() - paddingX;
		int height = getMeasuredHeight() - paddingY;

		size = width < height ? width : height;
		setMeasuredDimension(size + paddingX, size + paddingY);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		size = w < h ? w : h;
		updateBounds();
	}

	private void updateBounds() {
		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		bounds.set(paddingLeft + thickness, getPaddingTop() + thickness, size - getPaddingRight() - thickness, size - getPaddingTop() - thickness);
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		float sweepAngle = (isInEditMode()) ? currentProgress/maxProgress*360 : actualProgress/maxProgress*360;
		if(!isIndeterminate)
			canvas.drawArc(bounds, startAngle, sweepAngle, false, paint);
		else
			canvas.drawArc(bounds, startAngle + indeterminateRotateOffset, indeterminateSweep, false, paint);
	}

	/**
	 *
	 * @return true if this view is in indeterminate mode.
	 */
	public boolean isIndeterminate() {
		return isIndeterminate;
	}

	/**
	 * Sets whether this CircularProgressView is indeterminate or not.
	 * It will reset the animation if the mode has changed.
	 * @param isIndeterminate
	 */
	public void setIndeterminate(boolean isIndeterminate) {
		boolean reset = this.isIndeterminate == isIndeterminate;
		this.isIndeterminate = isIndeterminate;
		if(reset)
			resetAnimation();
	}

	/**
	 *
	 * @return the thickness of the progress bar arc
	 */
	public int getThickness() {
		return thickness;
	}

	/**
	 * Sets the thickness of the progress bar arc.
	 * @param thickness the thickness of the progress bar arc
	 */
	public void setThickness(int thickness) {
		this.thickness = thickness;
		updatePaint();
		updateBounds();
		invalidate();
	}

	/**
	 *
	 * @return the color of the progress bar
	 */
	public int getColor() {
		return color;
	}

	/**
	 * Sets the color of the progress bar.
	 * @param color the color of the progress bar
	 */
	public void setColor(int color) {
		this.color = color;
		updatePaint();
		invalidate();
	}

	/**
	 * Gets the progress value considered to be 100% of the progress bar.
	 * @return the maximum progress
	 */
	public float getMaxProgress() {
		return maxProgress;
	}

	/**
	 * Sets the progress value considered to be 100% of the progress bar.
	 * @param maxProgress the maximum progress
	 */
	public void setMaxProgress(float maxProgress) {
		this.maxProgress = maxProgress;
		invalidate();
	}

	/**
	 *
	 * @return current progress
	 */
	public float getProgress() {
		return currentProgress;
	}

	public void setProgress(float currentProgress){
		this.currentProgress = currentProgress;
		if(!isIndeterminate){
			if(progressAnimator != null){
				if(progressAnimator.isRunning()) {
					progressAnimator.cancel();
				}
				progressAnimator = ValueAnimator.ofFloat(actualProgress, currentProgress);
				progressAnimator.setDuration(500);
				progressAnimator.setInterpolator(new LinearInterpolator());
				progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						actualProgress = (float) animation.getAnimatedValue();
						invalidate();
					}
				});
				progressAnimator.start();
			}

		}
		invalidate();
	}
	private void resetAnimation() {
		if(startAngleRotate != null){
			if(startAngleRotate.isRunning()) {
				startAngleRotate.cancel();
			}
		}

		if(progressAnimator != null && progressAnimator.isRunning()){
			progressAnimator.cancel();
		}

		if(indeterminateAnimator != null && indeterminateAnimator.isRunning()){
			indeterminateAnimator.cancel();
		}

		if(isIndeterminate){
			// Indeterminate animation
			startAngle = -90F;
			indeterminateSweep = INDETERMINANT_MIN_SWEEP;
			indeterminateAnimator = new AnimatorSet();
			AnimatorSet prevSet = null, nextSet;
			for(int k=0;k<animSteps;k++)
			{
				nextSet = createIndeterminateAnimator(k);
				AnimatorSet.Builder builder = indeterminateAnimator.play(nextSet);
				if(prevSet != null)
					builder.after(prevSet);
				prevSet = nextSet;
			}

			indeterminateAnimator.addListener(new AnimatorListenerAdapter() {
				boolean wasCancelled = false;
				@Override
				public void onAnimationCancel(Animator animation) {
					wasCancelled = true;
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					if(!wasCancelled)
						resetAnimation();
				}
			});
			indeterminateAnimator.start();

		}else{
			startAngle = -90F;
			startAngleRotate = ValueAnimator.ofFloat(-90F, 270F);
			startAngleRotate.setDuration(500);
			startAngleRotate.setInterpolator(new DecelerateInterpolator());
			startAngleRotate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					startAngle = (float) animation.getAnimatedValue();
					invalidate();
				}
			});
			startAngleRotate.start();
			actualProgress = 0F;
			progressAnimator = ValueAnimator.ofFloat(actualProgress, currentProgress);
			progressAnimator.setDuration(500);
			progressAnimator.setInterpolator(new LinearInterpolator());
			progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					actualProgress = (Float) animation.getAnimatedValue();
					invalidate();
				}
			});
			progressAnimator.start();
		}
	}

	private AnimatorSet createIndeterminateAnimator(int step) {
		final float maxSweep = 360f*(animSteps-1)/animSteps + INDETERMINANT_MIN_SWEEP;
		final float start = -90f + step*(maxSweep-INDETERMINANT_MIN_SWEEP);

		// Extending the front of the arc
		ValueAnimator frontEndExtend = ValueAnimator.ofFloat(INDETERMINANT_MIN_SWEEP, maxSweep);
		frontEndExtend.setDuration(animDuration/animSteps/2);
		frontEndExtend.setInterpolator(new DecelerateInterpolator(1));
		frontEndExtend.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				indeterminateSweep = (Float) animation.getAnimatedValue();
				invalidate();
			}
		});

		// Overall rotation
		ValueAnimator rotateAnimator1 = ValueAnimator.ofFloat(step*720f/animSteps, (step+.5f)*720f/animSteps);
		rotateAnimator1.setDuration(animDuration/animSteps/2);
		rotateAnimator1.setInterpolator(new LinearInterpolator());
		rotateAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				indeterminateRotateOffset = (Float) animation.getAnimatedValue();
			}
		});

		// Followed by...

		// Retracting the back end of the arc
		ValueAnimator backEndRetract = ValueAnimator.ofFloat(start, start+maxSweep-INDETERMINANT_MIN_SWEEP);
		backEndRetract.setDuration(animDuration/animSteps/2);
		backEndRetract.setInterpolator(new DecelerateInterpolator(1));
		backEndRetract.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				startAngle = (Float) animation.getAnimatedValue();
				indeterminateSweep = maxSweep - startAngle + start;
				invalidate();
			}
		});

		// More overall rotation
		ValueAnimator rotateAnimator2 = ValueAnimator.ofFloat((step+.5f)*720f/animSteps, (step+1)*720f/animSteps);
		rotateAnimator2.setDuration(animDuration/animSteps/2);
		rotateAnimator2.setInterpolator(new LinearInterpolator());
		rotateAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				indeterminateRotateOffset = (Float) animation.getAnimatedValue();
			}
		});

		AnimatorSet set = new AnimatorSet();
		set.play(frontEndExtend).with(rotateAnimator1);
		set.play(backEndRetract).with(rotateAnimator2).after(rotateAnimator1);
		return set;
	}
}
