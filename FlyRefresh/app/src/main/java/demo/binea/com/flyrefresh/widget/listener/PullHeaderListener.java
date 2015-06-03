package demo.binea.com.flyrefresh.widget.listener;

import demo.binea.com.flyrefresh.widget.PullHeadLayout;

/**
 * Created by xubinggui on 6/3/15.
 */
public interface PullHeaderListener {
	void onPullProgress(PullHeadLayout parent, int state, float progress);
}
