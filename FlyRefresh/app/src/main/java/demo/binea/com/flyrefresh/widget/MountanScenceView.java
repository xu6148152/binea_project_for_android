package demo.binea.com.flyrefresh.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import demo.binea.com.flyrefresh.widget.listener.PullHeaderListener;

/**
 * Created by xubinggui on 6/3/15.
 */
public class MountanScenceView extends View implements PullHeaderListener{
	private Paint mMountPaint;
	private Paint mTrunkPaint;
	private Paint mBranchPaint;
	private Paint mBoarderPaint;

	private float mMoveFactor = 0;

	public MountanScenceView(Context context) {
		this(context, null);
	}

	public MountanScenceView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MountanScenceView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public MountanScenceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	private void init() {
		mMountPaint.setAntiAlias(true);
		mMountPaint.setStyle(Paint.Style.FILL);

		mTrunkPaint.setAntiAlias(true);
		mBranchPaint.setAntiAlias(true);
		mBoarderPaint.setAntiAlias(true);
		mBoarderPaint.setStyle(Paint.Style.STROKE);
		mBoarderPaint.setStrokeWidth(2);
		mBoarderPaint.setStrokeJoin(Paint.Join.ROUND);

		updateMountainPath(mMoveFactor);
		updateTreePath(mMoveFactor, true);
	}

	private void updateTreePath(float moveFactor, boolean b) {

	}

	private void updateMountainPath(float moveFactor) {

	}


	@Override
	public void onPullProgress(PullHeadLayout parent, int state, float progress) {

	}
}
