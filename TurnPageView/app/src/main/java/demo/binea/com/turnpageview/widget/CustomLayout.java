package demo.binea.com.turnpageview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xubinggui on 15/1/25.
 */
public class CustomLayout extends ViewGroup {

	private final static String TAG = CustomLayout.class.getSimpleName();

	public CustomLayout(Context context) {
		this(context, null);
	}

	public CustomLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int parentPaddingLeft = getPaddingLeft();
		int parentPaddingTop = getPaddingTop();

		if (getChildCount() > 0) {
			int mutilHeight = 0;

			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);

				CustomLayoutParams clp = (CustomLayoutParams) child.getLayoutParams();

				Log.d(TAG, "left " + clp.leftMargin + " top " + clp.topMargin + " right " + clp.rightMargin + " bottom " + clp.bottomMargin);
				child.layout(parentPaddingLeft + clp.leftMargin, mutilHeight + parentPaddingTop + clp.topMargin, child.getMeasuredWidth() + parentPaddingLeft + clp.leftMargin, child.getMeasuredHeight() + mutilHeight + parentPaddingTop + clp.topMargin);
				Log.d(TAG,"child measuredHeight " + child.getMeasuredHeight());
				mutilHeight += child.getMeasuredHeight() + clp.topMargin + clp.bottomMargin;
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if(getChildCount() > 0){
			measureChildren(widthMeasureSpec, heightMeasureSpec);
		}
//		int parentDesireWidth = 0;
//		int parentDesireHeight = 0;
//
//		if (getChildCount() > 0) {
//			for (int i = 0; i < getChildCount(); i++) {
//
//				View child = getChildAt(i);
//
//				CustomLayoutParams clp = (CustomLayoutParams) child.getLayoutParams();
//
//				measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
//
//				parentDesireWidth += child.getMeasuredWidth() + clp.leftMargin + clp.rightMargin;
//				parentDesireHeight += child.getMeasuredHeight() + clp.topMargin + clp.bottomMargin;
//			}
//
//			parentDesireWidth += getPaddingLeft() + getPaddingRight();
//			parentDesireHeight += getPaddingTop() + getPaddingBottom();
//
//			parentDesireWidth = Math.max(parentDesireWidth, getSuggestedMinimumWidth());
//			parentDesireHeight = Math.max(parentDesireHeight, getSuggestedMinimumHeight());
//		}
//
//		setMeasuredDimension(resolveSize(parentDesireWidth, widthMeasureSpec), resolveSize(parentDesireHeight, heightMeasureSpec));
	}

	public static class CustomLayoutParams extends MarginLayoutParams {

		public CustomLayoutParams(MarginLayoutParams source) {
			super(source);
		}

		public CustomLayoutParams(android.view.ViewGroup.LayoutParams source) {
			super(source);
		}

		public CustomLayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
		}

		public CustomLayoutParams(int width, int height) {
			super(width, height);
		}
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new CustomLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new CustomLayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateLayoutParams(LayoutParams p) {
		return new CustomLayoutParams(p);
	}

	@Override
	protected boolean checkLayoutParams(LayoutParams p) {
		return p instanceof CustomLayoutParams;
	}
}
