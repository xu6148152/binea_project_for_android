package demo.binea.com.jumpbeans.widget;

import android.animation.ValueAnimator;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by xubinggui on 6/6/15.
 */
public final class JumpingBeansSpan extends SuperscriptSpan implements ValueAnimator.AnimatorUpdateListener{

	private WeakReference<TextView> textView;
	private int delay;
	private int loopDuration;
	private float animatedRange;
	private int shift;

	private ValueAnimator jumpAnimator;

	public JumpingBeansSpan(@NonNull TextView textView, int loopDuration, int position, int waveCharOffset,
	                        float animatedRange) {
		this.textView = new WeakReference<>(textView);
		this.delay = waveCharOffset * position;
		this.loopDuration = loopDuration;
		this.animatedRange = animatedRange;
	}

	@Override
	public void updateMeasureState(TextPaint tp) {
		initIfNecessary(tp.ascent());
		tp.baselineShift = shift;
	}

	private void initIfNecessary(float ascent) {
		if (jumpAnimator != null) {
			return;
		}

		this.shift = 0;
		int maxShift = (int) ascent / 2;
		jumpAnimator = ValueAnimator.ofInt(0, maxShift);
		jumpAnimator
				.setDuration(loopDuration)
				.setStartDelay(delay);
		jumpAnimator.setInterpolator(new JumpInterpolator(animatedRange));
		jumpAnimator.setRepeatCount(ValueAnimator.INFINITE);
		jumpAnimator.setRepeatMode(ValueAnimator.RESTART);
		jumpAnimator.addUpdateListener(this);
		jumpAnimator.start();
	}

	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
		TextView v = textView.get();
		if (v != null) {
			updateAnimationFor(animation, v);
		} else {
			cleanupAndComplainAboutUserBeingAFool();
		}
	}

	private void cleanupAndComplainAboutUserBeingAFool() {
		teardown();
	}

	public void teardown() {
		if (jumpAnimator != null) {
			jumpAnimator.cancel();
			jumpAnimator.removeAllListeners();
		}
		if (textView.get() != null) {
			textView.clear();
		}
	}

	private void updateAnimationFor(ValueAnimator animation, TextView v) {
		if (isAttachedToHierarchy(v)) {
			shift = (int) animation.getAnimatedValue();
			v.invalidate();
		}
	}

	private static boolean isAttachedToHierarchy(View v) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			return v.isAttachedToWindow();
		}
		return v.getParent() != null;   // Best-effort fallback
	}
}
