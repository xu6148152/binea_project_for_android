package demo.binea.com.beautyprogressbar.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by xubinggui on 15/3/8.
 */
public class DimenUtil {

	public static int px2dp(float px, Context context){
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.getResources().getDisplayMetrics());
	}

	public static int dp2px(float dp, Context context){
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}

	public static int sp2px(float sp, Context context){
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
	}
}
