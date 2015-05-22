package demo.binea.com.bezierindicator.widget.util;

import android.support.v4.view.ViewPager;

import java.lang.reflect.Field;

import demo.binea.com.bezierindicator.widget.FixedSpeedScroller;

/**
 * Created by xubinggui on 5/21/15.
 */
public class ScrollerUtil {
	public static void setViewPager(ViewPager viewPager) {
		Class viewPagerClazz = viewPager.getClass();
		final Field mScroller;
		try {
			mScroller = viewPagerClazz.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			FixedSpeedScroller fixedSpeedScroller = new FixedSpeedScroller(viewPager.getContext());
			fixedSpeedScroller.setScrollSpeed(5000);
			mScroller.set(viewPager, fixedSpeedScroller);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception ex){
			ex.printStackTrace();
		}

	}
}
