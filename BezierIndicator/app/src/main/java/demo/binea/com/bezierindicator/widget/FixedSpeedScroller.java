package demo.binea.com.bezierindicator.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

/**
 * Created by xubinggui on 5/21/15.
 */
public class FixedSpeedScroller extends OverScroller {

	private int mDuration;

	public FixedSpeedScroller(Context context) {
		super(context);
	}

	public FixedSpeedScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
	}

	public FixedSpeedScroller(Context context, Interpolator interpolator, float bounceCoefficientX, float bounceCoefficientY) {
		super(context, interpolator, bounceCoefficientX, bounceCoefficientY);
	}

	public void setScrollSpeed(int duration) {
		this.mDuration = duration;
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		super.startScroll(startX, startY, dx, dy, mDuration);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy) {
		super.startScroll(startX, startY, dx, dy, mDuration);
	}
}
