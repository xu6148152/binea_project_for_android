package demo.binea.com.recyclerviewandtoolbar.util;

import android.content.Context;
import android.content.res.TypedArray;

import demo.binea.com.recyclerviewandtoolbar.R;

/**
 * Created by xubinggui on 15/3/2.
 */
public class Utils {
	public static int getToolbarHeight(Context context) {
		final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
				new int[]{R.attr.actionBarSize});
		int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
		styledAttributes.recycle();

		return toolbarHeight;
	}

	public static int getTabsHeight(Context context) {
		return (int) context.getResources().getDimension(R.dimen.tabsHeight);
	}
}
