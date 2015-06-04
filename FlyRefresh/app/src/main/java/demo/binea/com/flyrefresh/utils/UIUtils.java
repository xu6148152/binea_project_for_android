package demo.binea.com.flyrefresh.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by xubinggui on 6/2/15.
 */
public class UIUtils {
	public static final int dpToPx(int dp) {
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
	}

	public static int lighterColor(int color, float factor) {
		int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
		int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
		int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
		return Color.argb(Color.alpha(color), red, green, blue);
	}

	public static int darkerColor(int color, float factor) {
		int a = Color.alpha( color );
		int r = Color.red( color );
		int g = Color.green( color );
		int b = Color.blue( color );

		return Color.argb( a,
				Math.max( (int)(r * factor), 0 ),
				Math.max( (int)(g * factor), 0 ),
				Math.max( (int)(b * factor), 0 ) );
	}

	public static int getThemeColor(Context ctx, int attr) {
		TypedValue tv = new TypedValue();
		if (ctx.getTheme().resolveAttribute(attr, tv, true)) {
			return tv.data;
		}
		return 0;
	}

	/**
	 * helper method to get the color by attr (which is defined in the style) or by resource.
	 *
	 * @param ctx
	 * @param attr
	 * @param res
	 * @return
	 */
	public static int getThemeColorFromAttrOrRes(Context ctx, int attr, int res) {
		int color = getThemeColor(ctx, attr);
		if (color == 0) {
			color = ctx.getResources().getColor(res);
		}
		return color;
	}

	public static void clearAnimator(View v) {
		ViewCompat.setAlpha(v, 1);
		ViewCompat.setScaleY(v, 1);
		ViewCompat.setScaleX(v, 1);
		ViewCompat.setTranslationY(v, 0);
		ViewCompat.setTranslationX(v, 0);
		ViewCompat.setRotation(v, 0);
		ViewCompat.setRotationY(v, 0);
		ViewCompat.setRotationX(v, 0);
		v.setPivotY(v.getMeasuredHeight() / 2);
		ViewCompat.setPivotX(v, v.getMeasuredWidth() / 2);
		ViewCompat.animate(v).setInterpolator(null);
	}

	public static ColorStateList createColorStateList(int normal, int pressed) {
		int[] colors = new int[] {pressed, pressed, pressed, normal};
		int[][] states = new int[4][];
		states[0] = new int[] { android.R.attr.state_pressed};
		states[1] = new int[] { android.R.attr.state_focused};
		states[2] = new int[] { android.R.attr.state_checked};
		states[3] = new int[] { };
		return new ColorStateList(states, colors);
	}
}
