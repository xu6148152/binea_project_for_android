package com.example.xubinggui.customviewtest.util;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by xubinggui on 15/3/29.
 */
public class Utils {
	private static Point point;
	private static DisplayMetrics dm = new DisplayMetrics();

	private static Display init(Context context){
		if(point == null){
			point = new Point();
		}

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay();
	}

	public static float getScreenWidth(Context context) {
		final Display display = init(context);
		display.getSize(point);
		return point.x;
	}

	public static float getScreenHeight(Context context){
		final Display display = init(context);
		display.getSize(point);
		return point.y;
	}

	public static float dp2px(float dp){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
	}
}
