package demo.binea.com.flyrefresh.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by xubinggui on 6/3/15.
 */
public class FlyRefreshLayout extends PullHeadLayout {
	public FlyRefreshLayout(Context context) {
		this(context, null);
	}

	public FlyRefreshLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FlyRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		init(context);
	}

	private void init(Context context) {
		MountainScenceView headerView = new MountainScenceView(getContext());
		LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeaderController.getMaxHeight());
		setHeaderView(headerView, lp);
	}


}
