package demo.binea.com.flyrefresh.widget.listener;

import demo.binea.com.flyrefresh.widget.FlyRefreshLayout;

/**
 * Created by xubinggui on 6/4/15.
 */
public interface PullRefreshListener {
	void onRefresh(FlyRefreshLayout view);
	void onRefreshAnimationEnd(FlyRefreshLayout view);
}
