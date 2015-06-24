package demo.binea.com.customviewdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xubinggui on 15/2/12.
 */
public class SquareLayout extends ViewGroup {

	private static final int ORIENTATION_HORIZONTAL = 0, ORIENTATION_VERTICAL = 1;// 排列方向的常量标识值
	private static final int DEFAULT_MAX_ROW = Integer.MAX_VALUE, DEFAULT_MAX_COLUMN = Integer.MAX_VALUE;// 最大行列默认值

	private int mMaxRow = DEFAULT_MAX_ROW;
	private int mMaxColumn = DEFAULT_MAX_COLUMN;

	private int mOrientation = ORIENTATION_VERTICAL;

	public SquareLayout(Context context) {
		this(context, null);

		mMaxRow = mMaxColumn = 2;
	}

	public SquareLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	}

	@Override
	protected LayoutParams generateLayoutParams(LayoutParams p) {
		return new MarginLayoutParams(p);
	}

	@Override
	public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}

	@Override
	protected boolean checkLayoutParams(LayoutParams p) {
		return p instanceof MarginLayoutParams;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int parentDesireWidth = 0;
		int parentDesireHeight = 0;

		int childMeasureState = 0;

		if(getChildCount() > 0){

			int [] childWidths = new int[getChildCount()];
			int [] childHeights = new int[getChildCount()];


			for (int i = 0; i< getChildCount(); i++){
				final View child = getChildAt(i);

				if(child.getVisibility() != View.GONE){
					measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

					int childMeasureSize = Math.max(child.getMeasuredWidth(), child.getMeasuredHeight());

					int childMeasureSpec = MeasureSpec.makeMeasureSpec(childMeasureSize, MeasureSpec.EXACTLY);

					child.measure(childMeasureSpec, childMeasureSpec);

					MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();

					childWidths[i] = child.getMeasuredWidth() + mlp.leftMargin + mlp.rightMargin;
					childHeights[i] = child.getMeasuredHeight() + mlp.topMargin + mlp.bottomMargin;

					childMeasureState = combineMeasuredStates(childMeasureState, child.getMeasuredState());
				}
			}

			int indexMultiWidth = 0, indexMultiHeight = 0;

			if (mOrientation == ORIENTATION_HORIZONTAL) {

				if(getChildCount() > mMaxColumn){
					int row = getChildCount() / mMaxColumn;

					int remainer = getChildCount() % mMaxColumn;

					int index = 0;

					for(int x = 0; x < row; x++){
						for(int y = 0; y < mMaxColumn; y++){
							indexMultiWidth +=childWidths[index];

							indexMultiHeight = Math.max(indexMultiHeight, childHeights[index++]);
						}

						parentDesireWidth = Math.max(parentDesireWidth, indexMultiWidth);
						parentDesireHeight += indexMultiHeight;

						indexMultiWidth = indexMultiHeight = 0;
					}

					if(remainer != 0){
						for(int i = getChildCount() - remainer; i<getChildCount(); i++){
							indexMultiWidth += childWidths[i];
							indexMultiHeight = Math.max(indexMultiHeight, childHeights[i]);
						}

						parentDesireWidth = Math.max(parentDesireWidth, indexMultiWidth);
						parentDesireHeight += indexMultiHeight;
						indexMultiWidth = indexMultiHeight = 0;
					}
				}else{
					for (int i = 0; i < getChildCount(); i++) {
						parentDesireHeight += childHeights[i];

						parentDesireWidth = Math.max(parentDesireWidth, childWidths[i]);
					}
				}
			} else if (mOrientation == ORIENTATION_VERTICAL) {
				if (getChildCount() > mMaxRow) {
					int column = getChildCount() / mMaxRow;
					int remainder = getChildCount() % mMaxRow;
					int index = 0;

					for (int x = 0; x < column; x++) {
						for (int y = 0; y < mMaxRow; y++) {
							indexMultiHeight += childHeights[index];
							indexMultiWidth = Math.max(indexMultiWidth, childWidths[index++]);
						}
						parentDesireHeight = Math.max(parentDesireHeight, indexMultiHeight);
						parentDesireWidth += indexMultiWidth;
						indexMultiWidth = indexMultiHeight = 0;
					}

					if (remainder != 0) {
						for (int i = getChildCount() - remainder; i < getChildCount(); i++) {
							indexMultiHeight += childHeights[i];
							indexMultiWidth = Math.max(indexMultiHeight, childWidths[i]);
						}
						parentDesireHeight = Math.max(parentDesireHeight, indexMultiHeight);
						parentDesireWidth += indexMultiWidth;
						indexMultiWidth = indexMultiHeight = 0;
					}
				} else {
					for (int i = 0; i < getChildCount(); i++) {
						parentDesireWidth += childWidths[i];

						parentDesireHeight = Math.max(parentDesireHeight, childHeights[i]);
					}
				}
			}
			parentDesireWidth +=getPaddingLeft() + getPaddingRight();
			parentDesireHeight +=getPaddingTop() + getPaddingBottom();

			parentDesireWidth = Math.max(parentDesireWidth, getSuggestedMinimumWidth());
			parentDesireHeight = Math.max(parentDesireHeight, getSuggestedMinimumHeight());

			setMeasuredDimension(resolveSizeAndState(parentDesireWidth, widthMeasureSpec, childMeasureState), resolveSizeAndState(parentDesireHeight, heightMeasureSpec, childMeasureState << MEASURED_HEIGHT_STATE_SHIFT));
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (getChildCount() > 0) {
			int multi = 0;

			int indexMulti = 1;

			int indexMultiWidth = 0, indexMultiHeight = 0;

			int tempHeight = 0, tempWidth = 0;

			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);

				if (child.getVisibility() != View.GONE) {
					MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();

					int childActualSize = child.getMeasuredWidth();// child.getMeasuredHeight()

					if (mOrientation == ORIENTATION_HORIZONTAL) {

						if (getChildCount() > mMaxColumn) {

							if (i < mMaxColumn * indexMulti) {
								child.layout(getPaddingLeft() + mlp.leftMargin + indexMultiWidth, getPaddingTop() + mlp.topMargin + indexMultiHeight,
										childActualSize + getPaddingLeft() + mlp.leftMargin + indexMultiWidth, childActualSize + getPaddingTop()
												+ mlp.topMargin + indexMultiHeight);
								indexMultiWidth += childActualSize + mlp.leftMargin + mlp.rightMargin;
								tempHeight = Math.max(tempHeight, childActualSize) + mlp.topMargin + mlp.bottomMargin;


								if (i + 1 >= mMaxColumn * indexMulti) {
									indexMultiHeight += tempHeight;

									indexMultiWidth = 0;

									indexMulti++;
								}
							}
						} else {
							child.layout(getPaddingLeft() + mlp.leftMargin + multi, getPaddingTop() + mlp.topMargin, childActualSize
									+ getPaddingLeft() + mlp.leftMargin + multi, childActualSize + getPaddingTop() + mlp.topMargin);

							multi += childActualSize + mlp.leftMargin + mlp.rightMargin;
						}
					}
					else if (mOrientation == ORIENTATION_VERTICAL) {
						if (getChildCount() > mMaxRow) {
							if (i < mMaxRow * indexMulti) {
								child.layout(getPaddingLeft() + mlp.leftMargin + indexMultiWidth, getPaddingTop() + mlp.topMargin + indexMultiHeight,
										childActualSize + getPaddingLeft() + mlp.leftMargin + indexMultiWidth, childActualSize + getPaddingTop()
												+ mlp.topMargin + indexMultiHeight);
								indexMultiHeight += childActualSize + mlp.topMargin + mlp.bottomMargin;
								tempWidth = Math.max(tempWidth, childActualSize) + mlp.leftMargin + mlp.rightMargin;
								if (i + 1 >= mMaxRow * indexMulti) {
									indexMultiWidth += tempWidth;
									indexMultiHeight = 0;
									indexMulti++;
								}
							}
						} else {
							child.layout(getPaddingLeft() + mlp.leftMargin, getPaddingTop() + mlp.topMargin + multi, childActualSize
									+ getPaddingLeft() + mlp.leftMargin, childActualSize + getPaddingTop() + mlp.topMargin + multi);

							multi += childActualSize + mlp.topMargin + mlp.bottomMargin;
						}
					}
				}
			}
		}
	}

	@Override
	public boolean shouldDelayChildPressedState() {
		return false;
	}
}
