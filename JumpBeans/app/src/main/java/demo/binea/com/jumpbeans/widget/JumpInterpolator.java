package demo.binea.com.jumpbeans.widget;

import android.animation.TimeInterpolator;

/**
 * Created by xubinggui on 6/6/15.
 */
public class JumpInterpolator implements TimeInterpolator {

	private final float animRange;

	public JumpInterpolator(float animateRange) {
		animRange = animateRange;
	}

	@Override
	public float getInterpolation(float input) {
		// We want to map the [0, PI] sine range onto [0, animRange]
		double radians = (input / animRange) * Math.PI;
		double interpolatedValue = Math.max(0f, Math.sin(radians));
		return (float) interpolatedValue;
	}
}
