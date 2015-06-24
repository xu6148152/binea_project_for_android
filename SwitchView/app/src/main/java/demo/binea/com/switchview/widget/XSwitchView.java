package demo.binea.com.switchview.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Property;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import demo.binea.com.switchview.R;

/**
 * Created by xubinggui on 15/4/26.
 */
public class XSwitchView extends View {

	private final RectF ovalForPath;
	private final RectF tempForRoundRect;
	private boolean isShowSwitchTag = false;

	private int width;
	private int height;

	private int centerX;
	private int centerY;

	private Paint paint;

	private int outerStrokeWidth;
	private int shadowSpace;
	private int cornerRadius;

	private RectF innerContentBound;
	private RectF knobBound;

	private float intrinsicInnerWidth;
	private float intrinsicInnerHeight;
	private float intrinsicKnobWidth;
	private float knobMaxExpandWidth;
	private float innerContentRate = 0.0F;
	private float knobExpandRate = 1F;
	private float knobMoveRate = 1F;
	private int tintColor;
	private int tempTintColor;

	private static final int backgroundColor = 0xFFCCCCCC;
	private int colorStep = backgroundColor;
	private static final int foregroundColor = 0xFFEFEFEF;

	private static final long commonDuration = 300L;

	private boolean isOn;
	private boolean isPreOn;
	private boolean knobState;

	private OnSwitchStateChangeListener onSwitchStateChangeListener;

	private ObjectAnimator innerContentAnimator;
	private Property<XSwitchView, Float> innerContentProperty = new Property<XSwitchView, Float>(Float.class, "innerBound"){

		@Override
		public Float get(XSwitchView object) {
			return object.getInnerContentRate();
		}

		@Override
		public void set(XSwitchView object, Float value) {
			object.setInnerContentRate(value);
		}
	};

	private ObjectAnimator knobExpandAnimator;
	private Property<XSwitchView, Float> knobExpandProperty = new Property<XSwitchView, Float>(Float.class, "knobExpand"){
		@Override
		public void set(XSwitchView sv, Float knobExpandRate){
			sv.setKnobExpandRate(knobExpandRate);
		}

		@Override
		public Float get(XSwitchView sv){
			return sv.getKnobExpandRate();
		}
	};

	private ObjectAnimator knobMoveAnimator;
	private Property<XSwitchView, Float> knobMoveProperty = new Property<XSwitchView, Float>(Float.class, "knobMove"){
		@Override
		public void set(XSwitchView sv, Float knobMoveRate){
			sv.setKnobMoveRate(knobMoveRate);
		}

		@Override
		public Float get(XSwitchView sv){
			return sv.getKnobMoveRate();
		}
	};

	private GestureDetector mGestureDetector;
	private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener(){
		@Override
		public boolean onDown(MotionEvent e) {
			if(!isEnabled()) return false;
			isPreOn = isOn;
			innerContentAnimator.setFloatValues(innerContentRate, 0.0f);
			innerContentAnimator.start();

			knobExpandAnimator.setFloatValues(knobExpandRate, 1.0F);
			knobExpandAnimator.start();
			return true;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent event){
			isOn = knobState;

			if(isPreOn == isOn){
				isOn = !isOn;
				knobState = !knobState;
			}

			if(knobState){

				knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
				knobMoveAnimator.start();

				innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
				innerContentAnimator.start();
			}else{

				knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
				knobMoveAnimator.start();

				innerContentAnimator.setFloatValues(innerContentRate, 1.0F);
				innerContentAnimator.start();
			}

			knobExpandAnimator.setFloatValues(knobExpandRate, 0.0F);
			knobExpandAnimator.start();

			if(XSwitchView.this.onSwitchStateChangeListener != null && isOn != isPreOn){
				XSwitchView.this.onSwitchStateChangeListener.onSwitchStateChange(isOn);
			}

			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

			if(e2.getX() > centerX){
				if(!knobState){
					knobState = !knobState;

					knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
					knobMoveAnimator.start();

					innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
					innerContentAnimator.start();
				}
			}else{
				if(knobState){
					knobState = !knobState;

					knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
					knobMoveAnimator.start();
				}
			}

			return true;
		}
	};
	private boolean isAttachedToWindow = false;
	private boolean dirtyAnimation = false;

	public XSwitchView(Context context) {
		this(context, null);
	}

	public XSwitchView(Context context, AttributeSet attrs) {
		super(context, attrs);

		final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.XSwitchView);
		isShowSwitchTag = ta.getBoolean(R.styleable.XSwitchView_isShowSwitchTag, false);

		outerStrokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5F, context.getResources().getDisplayMetrics());
		shadowSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7F, context.getResources().getDisplayMetrics());
		tintColor = ta.getColor(R.styleable.XSwitchView_tintColor, 0xFF9CE949);
		ta.recycle();

		paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		innerContentBound = new RectF();
		knobBound = new RectF();
		ovalForPath = new RectF();

		tempForRoundRect = new RectF();

		mGestureDetector = new GestureDetector(context, mGestureListener);
		mGestureDetector.setIsLongpressEnabled(false);

		if(Build.VERSION.SDK_INT >= 11){
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		innerContentAnimator = ObjectAnimator.ofFloat(XSwitchView.this, innerContentProperty, innerContentRate, 1.0F);
		innerContentAnimator.setDuration(commonDuration);
		innerContentAnimator.setInterpolator(new DecelerateInterpolator());

		knobExpandAnimator = ObjectAnimator.ofFloat(XSwitchView.this, knobExpandProperty, knobExpandRate, 1.0F);
		knobExpandAnimator.setDuration(commonDuration);
		knobExpandAnimator.setInterpolator(new DecelerateInterpolator());

		knobMoveAnimator = ObjectAnimator.ofFloat(XSwitchView.this, knobMoveProperty, knobMoveRate, 1.0F);
		knobMoveAnimator.setDuration(commonDuration);
		knobMoveAnimator.setInterpolator(new DecelerateInterpolator());

		setOn(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);

		//make sure widget remain in a good appearance
		if((float) height / (float) width < 0.33333F){
			height = (int) ((float) width * 0.33333F);

			widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec));
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec));

			super.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		}

		centerX = width / 2;
		centerY = height / 2;

		cornerRadius = centerY - shadowSpace;

		innerContentBound.left = outerStrokeWidth + shadowSpace;
		innerContentBound.top = outerStrokeWidth + shadowSpace;
		innerContentBound.right = width - outerStrokeWidth - shadowSpace;
		innerContentBound.bottom = height - outerStrokeWidth - shadowSpace;

		intrinsicInnerWidth = innerContentBound.width();
		intrinsicInnerHeight = innerContentBound.height();

		knobBound.left = outerStrokeWidth + shadowSpace;
		knobBound.top = outerStrokeWidth + shadowSpace;
		knobBound.right = height - outerStrokeWidth - shadowSpace;
		knobBound.bottom = height - outerStrokeWidth - shadowSpace;

		intrinsicKnobWidth = knobBound.height();
		knobMaxExpandWidth = (float) width * 0.7F;
		if(knobMaxExpandWidth > knobBound.width() * 1.25F){
			knobMaxExpandWidth = knobBound.width() * 1.25F;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		//innerContentCalculation begin
		float w = (float) intrinsicInnerWidth / 2.0F * innerContentRate;
		float h = (float) intrinsicInnerHeight / 2.0F * innerContentRate;

		this.innerContentBound.left = centerX - w;
		this.innerContentBound.top = centerY - h;
		this.innerContentBound.right = centerX + w;
		this.innerContentBound.bottom = centerY + h;
		//innerContentCalculation end

		//knobExpandCalculation begin
		w = intrinsicKnobWidth + (float) (knobMaxExpandWidth - intrinsicKnobWidth) * knobExpandRate;

		boolean left = knobBound.left + knobBound.width() / 2 > centerX;

		if(left){
			knobBound.left = knobBound.right - w;
		}else{
			knobBound.right = knobBound.left + w;
		}
		//knobExpandCalculation end

		//knobMoveCalculation begin
		float kw = knobBound.width();
		w = (float) (width - kw - ((shadowSpace + outerStrokeWidth) * 2)) * knobMoveRate;

		this.colorStep = RGBColorTransform(knobMoveRate, backgroundColor, tintColor);


		knobBound.left = shadowSpace + outerStrokeWidth + w;
		knobBound.right = knobBound.left + kw;
		//knobMoveCalculation end

		//background
		paint.setColor(colorStep);
		paint.setStyle(Paint.Style.FILL);

		drawRoundRect(shadowSpace, shadowSpace, width - shadowSpace, height - shadowSpace, cornerRadius, canvas, paint);

		//innerContent
		paint.setColor(foregroundColor);
		canvas.drawRoundRect(innerContentBound, innerContentBound.height() / 2, innerContentBound.height() / 2, paint);

		//knob
//        shadowDrawable.setBounds((int) (knobBound.left - shadowSpace), (int) (knobBound.top - shadowSpace), (int) (knobBound.right + shadowSpace), (int) (knobBound.bottom + shadowSpace));
//        shadowDrawable.draw(canvas);
		paint.setShadowLayer(2, 0, shadowSpace / 2, isEnabled() ? 0x20000000 : 0x10000000);

//        paint.setColor(isEnabled() ? 0x20000000 : 0x10000000);
//        drawRoundRect(knobBound.left, knobBound.top + shadowSpace / 2, knobBound.right, knobBound.bottom + shadowSpace / 2, cornerRadius - outerStrokeWidth, canvas, paint);
//
//        paint.setColor(foregroundColor);
		canvas.drawRoundRect(knobBound, cornerRadius - outerStrokeWidth, cornerRadius - outerStrokeWidth, paint);
		paint.setShadowLayer(0, 0, 0, 0);

		paint.setColor(backgroundColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);

		canvas.drawRoundRect(knobBound, cornerRadius - outerStrokeWidth, cornerRadius - outerStrokeWidth, paint);
	}

	private void drawRoundRect(int left, int top, int right, int bottom, int cornerRadius, Canvas canvas, Paint paint) {
		tempForRoundRect.left = left;
		tempForRoundRect.top = top;
		tempForRoundRect.right = right;
		tempForRoundRect.bottom = bottom;
		canvas.drawRoundRect(tempForRoundRect, cornerRadius,cornerRadius, paint);

	}

	private int RGBColorTransform(float progress, int fromColor, int toColor) {
		int or = (fromColor >> 16) & 0xFF;
		int og = (fromColor >> 8) & 0xFF;
		int ob = fromColor & 0xFF;

		int nr = (toColor >> 16) & 0xFF;
		int ng = (toColor >> 8) & 0xFF;
		int nb = toColor & 0xFF;

		int rGap = (int) ((float) (nr - or) * progress);
		int gGap = (int) ((float) (ng - og) * progress);
		int bGap = (int) ((float) (nb - ob) * progress);

		return 0xFF000000 | ((or + rGap) << 16) | ((og + gGap) << 8) | (ob + bGap);

	}


	void setInnerContentRate(float rate){
		this.innerContentRate = rate;

		invalidate();
	}

	float getInnerContentRate(){
		return this.innerContentRate;
	}

	void setKnobExpandRate(float rate){
		this.knobExpandRate = rate;

		invalidate();
	}

	float getKnobExpandRate(){
		return this.knobExpandRate;
	}

	void setKnobMoveRate(float rate){
		this.knobMoveRate = rate;

		invalidate();
	}

	float getKnobMoveRate(){
		return knobMoveRate;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if(!isEnabled()) return false;

		switch(event.getAction()){
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				if(!knobState){
					innerContentAnimator = ObjectAnimator.ofFloat(XSwitchView.this, innerContentProperty, innerContentRate, 1.0F);
					innerContentAnimator.setDuration(300L);
					innerContentAnimator.setInterpolator(new DecelerateInterpolator());

					innerContentAnimator.start();
				}

				knobExpandAnimator = ObjectAnimator.ofFloat(XSwitchView.this, knobExpandProperty, knobExpandRate, 0.0F);
				knobExpandAnimator.setDuration(300L);
				knobExpandAnimator.setInterpolator(new DecelerateInterpolator());

				knobExpandAnimator.start();

				isOn = knobState;

				if(XSwitchView.this.onSwitchStateChangeListener != null && isOn != isPreOn){
					XSwitchView.this.onSwitchStateChangeListener.onSwitchStateChange(isOn);
				}

				break;
		}

		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		isAttachedToWindow = true;

		if(dirtyAnimation){
			knobState = this.isOn;
			if(knobState){

				knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
				knobMoveAnimator.start();

				innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
				innerContentAnimator.start();
			}else{

				knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
				knobMoveAnimator.start();

				innerContentAnimator.setFloatValues(innerContentRate, 1.0F);
				innerContentAnimator.start();
			}

			knobExpandAnimator.setFloatValues(knobExpandRate, 0.0F);
			knobExpandAnimator.start();

			if(XSwitchView.this.onSwitchStateChangeListener != null && isOn != isPreOn){
				XSwitchView.this.onSwitchStateChangeListener.onSwitchStateChange(isOn);
			}

			dirtyAnimation = false;
		}
	}

	@Override
	protected void onDetachedFromWindow(){
		super.onDetachedFromWindow();
		isAttachedToWindow = false;
	}

	public boolean isOn(){
		return this.isOn;
	}

	public void setOn(boolean on){
		setOn(on, false);
	}

	public void setOn(boolean on, boolean animated){

		if(this.isOn == on) return;

		if(!isAttachedToWindow && animated){
			dirtyAnimation = true;
			this.isOn = on;

			return;
		}

		this.isOn = on;
		knobState = this.isOn;

		if(!animated){

			if(on){
				setKnobMoveRate(1.0F);
				setInnerContentRate(0.0F);
			}else{
				setKnobMoveRate(0.0F);
				setInnerContentRate(1.0F);
			}

			setKnobExpandRate(0.0F);

		}else{
			if(knobState){

				knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
				knobMoveAnimator.start();

				innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
				innerContentAnimator.start();
			}else{

				knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
				knobMoveAnimator.start();

				innerContentAnimator.setFloatValues(innerContentRate, 1.0F);
				innerContentAnimator.start();
			}

			knobExpandAnimator.setFloatValues(knobExpandRate, 0.0F);
			knobExpandAnimator.start();
		}

		if(XSwitchView.this.onSwitchStateChangeListener != null && isOn != isPreOn){
			XSwitchView.this.onSwitchStateChangeListener.onSwitchStateChange(isOn);
		}
	}

	public void setTintColor(int tintColor){
		this.tintColor = tintColor;
		tempTintColor = this.tintColor;
	}

	public int getTintColor(){
		return this.tintColor;
	}
}
