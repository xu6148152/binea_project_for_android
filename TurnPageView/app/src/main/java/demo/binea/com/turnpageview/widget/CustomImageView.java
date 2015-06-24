package demo.binea.com.turnpageview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xubinggui on 15/1/25.
 */
public class CustomImageView extends View {

	private Bitmap mBitmap;

	public CustomImageView(Context context) {
		this(context, null);
	}

	public CustomImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);


	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int resultWidth,resultHeight;

		//width measure
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		if(widthMode == MeasureSpec.EXACTLY){
			resultWidth = widthSize;
		}else{
			resultWidth = mBitmap.getWidth();

			if(widthMode == MeasureSpec.AT_MOST){
				resultWidth = Math.min(resultWidth, widthSize);
			}
		}

		//height

		final int heightMode = MeasureSpec.getMode(widthMeasureSpec);
		final int heightSize = MeasureSpec.getSize(widthMeasureSpec);

		if(heightMode == MeasureSpec.EXACTLY){
			resultHeight = heightSize;
		}else{
			resultHeight = mBitmap.getWidth();

			if(heightMode == MeasureSpec.AT_MOST){
				resultHeight = Math.min(resultHeight, heightSize);
			}
		}

		resultWidth = resultWidth + getPaddingLeft() + getPaddingRight();
		resultHeight = resultHeight + getPaddingTop() + getPaddingBottom();

		setMeasuredDimension(resultWidth, resultHeight);
	}

	public void setmBitmap(Bitmap bitmap){
		this.mBitmap = bitmap;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, getPaddingLeft(), getPaddingTop(), null);
	}
}
