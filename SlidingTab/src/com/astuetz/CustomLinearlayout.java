package com.astuetz;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by xubinggui on 6/11/15.
 */
public class CustomLinearlayout extends LinearLayout {

	private int offset;

	private int shouldOffset;

	private View child1;
	private View child2;

	private final String TAG = CustomLinearlayout.class.getCanonicalName();

	public int OFFSET = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

	public CustomLinearlayout(Context context) {
		this(context, null);
	}

	public CustomLinearlayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = 0;
		for(int i = 0; i< getChildCount(); i++) {
			if(i == 0) {
				width += getChildAt(i).getMeasuredWidth() / 2;
			}else if(i == getChildCount() - 1) {
				width += getChildAt(i).getMeasuredWidth() / 2;
			}else {
				width += getChildAt(i).getMeasuredWidth();
			}
		}
		setMeasuredDimension(getScreenWidth() + OFFSET * (getChildCount() - 1) + width, getMeasuredHeight());
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Log.d(TAG, "offset " + offset);

		for(int i = 0; i < getChildCount(); i++) {
			View child1 = getChildAt(i);
			if(i == 0){
				child1.layout((getScreenWidth() - child1.getMeasuredWidth()) / 2 + offset, t,
						(getScreenWidth() + child1.getMeasuredWidth()) / 2 + offset, b);
			}else {
				View previousChild = getChildAt(i - 1);
				child1.layout(previousChild.getRight() + OFFSET, t, previousChild.getRight() + OFFSET + child1.getMeasuredWidth(), b);
				if(shouldOffset == 0){
					shouldOffset = child1.getLeft() + child1.getMeasuredWidth() / 2 - (previousChild.getLeft()
							+ previousChild.getMeasuredWidth() / 2);
				}
			}
		}
	}

	public int getScreenWidth(){
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}

	public void setOffset(float offset){
		this.offset = (int) offset;
	}

	public TextView getFirstChild(){
		child1 = getChildAt(0);
		if(child1 instanceof TextView){
			return (TextView) child1;
		}
		return null;
	}

	public TextView getSecondChild(){
		child2 = getChildAt(1);
		if(child2 instanceof TextView){
			return (TextView) child2;
		}
		return null;
	}

	public int shouldOffset(){
		return shouldOffset;
	}

}
