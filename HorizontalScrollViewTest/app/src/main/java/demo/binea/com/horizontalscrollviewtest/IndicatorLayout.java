package demo.binea.com.horizontalscrollviewtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by xubinggui on 6/11/15.
 */
public class IndicatorLayout extends LinearLayout {
	public IndicatorLayout(Context context) {
		this(context, 0);
	}

	public IndicatorLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		final View firstView = getChildAt(0);
		if(firstView != null){

		}
	}
}
