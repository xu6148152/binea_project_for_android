package demo.binea.com.horizontalscrollviewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by xubinggui on 6/11/15.
 */
public class IndicatorView extends View {

	private final String TAG = IndicatorView.class.getSimpleName();
	private int currentIndex = 0;

	private final String practise = "practise";

	private final String track = "track";

	private Paint mPaint;
	private TextPaint mTextPaint;

	private Rect mBounds;

	private float startX;
	private float oldX;
	private float startY;

	private float offset;

	private float textX1;
	private float textY1;

	private float textX2;
	private float textY2;

	private float defaultX1;
	private float defaultX2;

	private float actualOffset = 0;

	public IndicatorView(Context context) {
		this(context, null);
	}

	public IndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.BLACK);
		mTextPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
		mBounds = new Rect();

		mPaint.setTextSize(60);

		mPaint.getTextBounds(practise, 0, practise.length(), mBounds);

		mBounds.offset(0, -mBounds.top);

		defaultX1 = textX1 = (getScreenWidth() - mBounds.width()) / 2;
		textY1 = mBounds.bottom;

		mPaint.getTextBounds(practise, 0, practise.length(), mBounds);

		mBounds.offset(0, -mBounds.top);

		defaultX2 = textX2 = getScreenWidth() - mBounds.width() / 2;
		textY2 = mBounds.bottom;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Log.d(TAG, "offset " + offset);

		canvas.drawText(practise, textX1, mBounds.bottom, mPaint);

		mPaint.getTextBounds(track, 0, track.length(), mBounds);

		mBounds.offset(0, -mBounds.top);
		canvas.drawText(track, textX2, mBounds.bottom, mPaint);
	}

	private int getScreenWidth(){
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();

		switch (action){
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:


				if(Math.abs(actualOffset) > getScreenWidth() / 3){
					Log.d(TAG, "actualOffset " + (getScreenWidth() / 2 - actualOffset));
					if(actualOffset > 0){
						//scroll right
						textX1 = defaultX1;
						textX2 = defaultX2;
					}else{
						textX1 = defaultX2;
						textX2 = defaultX1;
					}
				}else{
//					setTranslationX(-actualOffset);
					textX1 = textX1 - actualOffset;
					textX2 = textX2 - actualOffset;
				}
				oldX = 0;
				offset = 0;
				actualOffset = 0;
				break;
			case MotionEvent.ACTION_DOWN:
				oldX = startX = event.getX();
				startY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				float x = event.getX();
				offset = x - oldX;

				textX1 = textX1 + offset;
				textX2 = textX2 + offset;
				oldX = x;
				actualOffset += offset;
//				if(Math.abs(event.getRawY()-startY)>Math.abs(event.getRawX()-startX)){
//					if(getParent() instanceof View){
//						return     ( (View)getParent()).onTouchEvent(event);
//					}
//				}else{
//					getParent().requestDisallowInterceptTouchEvent(true);
//				}
//				startX = x;
				startY = event.getY();
				invalidate();

				break;
		}

		return true;
	}
}
