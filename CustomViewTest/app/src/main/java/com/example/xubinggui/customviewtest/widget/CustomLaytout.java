package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xubinggui on 15/4/6.
 */
public class CustomLaytout extends ViewGroup {

	public CustomLaytout(Context context) {
		this(context, null);
	}

	public CustomLaytout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int desiredWidth = 0;
		int desiredHeight = 0;

		if(getChildCount() > 0){
			for(int i = 0; i<getChildCount();i++){
				final View child = getChildAt(i);
				measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

				CustomLayoutParams clp = (CustomLayoutParams) child.getLayoutParams();

				desiredWidth = clp.leftMargin + child.getMeasuredWidth() + clp.rightMargin;
				desiredHeight = clp.topMargin + child.getMeasuredHeight() + clp.bottomMargin;

			}

			desiredWidth += getPaddingLeft() + getPaddingRight();
			desiredHeight += getPaddingTop() + getPaddingBottom();

			desiredWidth = Math.max(desiredWidth, getSuggestedMinimumWidth());
			desiredHeight = Math.max(desiredHeight, getSuggestedMinimumHeight());

			setMeasuredDimension(resolveSize(desiredWidth, widthMeasureSpec), resolveSize(desiredHeight, heightMeasureSpec));
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int paddingLeft = getPaddingLeft();
		final int paddingTop = getPaddingTop();

		if(getChildCount()>0){
			int delheight = 0;
			for(int i = 0; i<getChildCount();i++){
				final View child = getChildAt(i);
				final CustomLayoutParams clp = (CustomLayoutParams) child.getLayoutParams();

				child.layout(paddingLeft + clp.leftMargin, delheight + paddingTop + clp.topMargin, child.getMeasuredWidth() + paddingLeft + clp.leftMargin, child.getMeasuredHeight() + delheight + paddingTop + clp.topMargin);
				delheight += child.getMeasuredHeight() + clp.topMargin + clp.bottomMargin;
			}
		}
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new CustomLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	@Override
	protected LayoutParams generateLayoutParams(LayoutParams p) {
		return new CustomLayoutParams(p);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new CustomLayoutParams(getContext(), attrs);
	}

	@Override
	protected boolean checkLayoutParams(LayoutParams p) {
		return p instanceof CustomLayoutParams;
	}

	static class CustomLayoutParams extends MarginLayoutParams{

		public CustomLayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
		}

		public CustomLayoutParams(int width, int height) {
			super(width, height);
		}

		public CustomLayoutParams(MarginLayoutParams source) {
			super(source);
		}

		public CustomLayoutParams(LayoutParams source) {
			super(source);
		}
	}
}
