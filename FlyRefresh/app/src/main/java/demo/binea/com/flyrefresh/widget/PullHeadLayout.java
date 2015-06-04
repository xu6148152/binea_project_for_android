package demo.binea.com.flyrefresh.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Scroller;

import demo.binea.com.flyrefresh.R;
import demo.binea.com.flyrefresh.utils.UIUtils;
import demo.binea.com.flyrefresh.widget.Interpolator.ElasticOutInterpolator;
import demo.binea.com.flyrefresh.widget.listener.PullHeaderListener;
import demo.binea.com.flyrefresh.widget.listener.ScrollHandler;
import demo.binea.com.flyrefresh.widget.listener.SimpleAnimatorListener;

/**
 * Created by xubinggui on 6/2/15.
 */
public class PullHeadLayout extends FrameLayout {

	private final String TAG = this.getClass().getCanonicalName();

	public static final int STATE_IDLE = 0;
	public static final int STATE_DRAGE = 1;
	public static final int STATE_FLING = 2;
	public static final int STATE_BOUNCE = 3;

	private int mPullState = STATE_IDLE;

	private final static int DEFAULT_EXPAND = UIUtils.dpToPx(300);
	private final static int DEFAULT_HEIGHT = UIUtils.dpToPx(240);
	private final static int DEFAULT_SHRINK = UIUtils.dpToPx(48);
	private final static int ACTION_ICON_SIZE = UIUtils.dpToPx(32);
	private final static int ACTION_BUTTON_CENTER = UIUtils.dpToPx(40);

	private int mHeaderId = 0;
	private int mContentId = 0;

	private View mHeadView;
	private View mContentView;
	protected ImageView mFlyView;

	private Drawable mActionDrawable;

	private int mPagingTouchSlop;
	private int mMaxVelocity;

	protected FloatingActionButton mActionView;

	protected HeaderController mHeaderController;

	private VelocityTracker mVelocityTracker;
	private MotionEvent mLastMoveEvent;
	private boolean mHasSendCancelEvent = false;
	private MotionEvent mDownEvent;

	private ValueAnimator mBounceAnim;

	private ScrollChecker mScrollChecker;
	private boolean mPreventForHorizontal = false;
	private boolean mDisableWhenHorizontalMove = false;

	private ScrollHandler mScrollHandler;

	private PullHeaderListener mPullHeaderListenerView;

	public PullHeadLayout(Context context) {
		this(context, null);
	}

	public PullHeadLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PullHeadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		int headerHeight = DEFAULT_HEIGHT;
		int headerExpandHeight = DEFAULT_EXPAND;
		int headerShrinkHeight = DEFAULT_SHRINK;

		if (null != attrs) {
			final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PullHeaderLayout);

			headerHeight = ta.getDimensionPixelOffset(R.styleable.PullHeaderLayout_phl_header_height,
					DEFAULT_HEIGHT);
			headerExpandHeight = ta.getDimensionPixelOffset(R.styleable.PullHeaderLayout_phl_header_expand_height,
					DEFAULT_EXPAND);
			headerShrinkHeight = ta.getDimensionPixelOffset(R.styleable.PullHeaderLayout_phl_header_shrink_height,
					DEFAULT_SHRINK);

			mHeaderId = ta.getResourceId(R.styleable.PullHeaderLayout_phl_header, mHeaderId);
			mContentId = ta.getResourceId(R.styleable.PullHeaderLayout_phl_content, mContentId);
			mActionDrawable = ta.getDrawable(R.styleable.PullHeaderLayout_phl_action);
			ta.recycle();
		}

		mHeaderController = new HeaderController(headerHeight, headerExpandHeight, headerShrinkHeight);
		final ViewConfiguration conf = ViewConfiguration.get(getContext());
		mPagingTouchSlop = conf.getScaledTouchSlop() * 2;

		mMaxVelocity = conf.getScaledMaximumFlingVelocity();
		mScrollHandler = new DefaultScrollHandler();

		mScrollChecker = new ScrollChecker();


	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		final int childCount = getChildCount();

		if (childCount > 2) {
			throw new IllegalArgumentException("FlyRefreshLayout only can host 2 elements");
		} else if (childCount == 2) {
			if (mHeaderId != 0 && mHeadView == null) {
				mHeadView = findViewById(mHeaderId);
			}

			if (mContentId != 0 && mContentView == null) {
				mContentView = findViewById(mContentId);
			}

			if (mHeadView == null || mContentView == null) {
				mHeadView = getChildAt(0);
				mContentView = getChildAt(1);
			}
		} else if (childCount == 1) {
			mContentView = getChildAt(0);
		}

		setActionDrawable(mActionDrawable);

	}

	public void setActionDrawable(Drawable actionDrawable) {
		if (actionDrawable != null) {
			if (mActionView == null) {
				final int bgColor = UIUtils.getThemeColorFromAttrOrRes(getContext(), R.attr.colorAccent, R.color.accent);
				final int pressedColor = UIUtils.darkerColor(bgColor, 0.8f);
				mActionView = new FloatingActionButton(getContext());
				mActionView.setBackgroundTintList(UIUtils.createColorStateList(bgColor, pressedColor));
				addView(mActionView, new LayoutParams(ACTION_ICON_SIZE, ACTION_ICON_SIZE));
			}
			if (mFlyView == null) {
				mFlyView = new ImageView(getContext());
				mFlyView.setScaleType(ImageView.ScaleType.FIT_XY);
				addView(mFlyView, new LayoutParams(ACTION_ICON_SIZE, ACTION_ICON_SIZE));
			}
			mFlyView.setImageDrawable(mActionDrawable);

		} else {
			if (mActionView != null) {
				removeView(mActionView);
				removeView(mFlyView);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (mHeadView != null) {
			measureChildWithMargins(mHeadView, widthMeasureSpec, 0, heightMeasureSpec, 0);
		}

		if (mContentView != null) {
			measureChildWithMargins(mContentView, widthMeasureSpec, 0, heightMeasureSpec, mHeaderController.getMinHeight());
		}

		if (mActionView != null) {
			measureChild(mActionView, widthMeasureSpec, heightMeasureSpec);
			measureChild(mFlyView, widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		layoutChildren();
	}

	private void layoutChildren() {
		int offsetY = mHeaderController.getCurrentPos();

		final int paddingLeft = getPaddingLeft();
		final int paddingTop = getPaddingTop();
		int left, top, right, bottom;
		if (mHeadView != null) {
			MarginLayoutParams lp = (MarginLayoutParams) mHeadView.getLayoutParams();
			left = lp.leftMargin + paddingLeft;
			top = lp.topMargin + paddingTop;
			right = lp.rightMargin + mHeadView.getMeasuredWidth();
			bottom = lp.bottomMargin + mHeadView.getMeasuredHeight();
			mHeadView.layout(left, top, right, bottom);
		}

		if (mContentView != null) {
			MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
			left = paddingLeft + lp.leftMargin;
			top = paddingTop + lp.topMargin;
			right = mContentView.getMeasuredWidth() + lp.rightMargin;
			bottom = mContentView.getMeasuredHeight() + lp.bottomMargin;
			mContentView.layout(left, top, right, bottom);
		}

		if (mActionView != null) {
			int halfWidth = (mActionView.getMeasuredWidth() + 1) / 2;
			int halfHeight = (mActionView.getMeasuredHeight() + 1) / 2;
			final int adjustCenter = UIUtils.dpToPx(2);
			mActionView.layout(ACTION_BUTTON_CENTER - halfWidth,
					offsetY - halfHeight + adjustCenter,
					ACTION_BUTTON_CENTER + halfWidth,
					offsetY + halfHeight + adjustCenter);

			halfWidth = (mFlyView.getMeasuredWidth() + 1) / 2;
			halfHeight = (mFlyView.getMeasuredHeight() + 1) / 2;
			mFlyView.layout(ACTION_BUTTON_CENTER - halfWidth,
					offsetY - halfHeight + adjustCenter,
					ACTION_BUTTON_CENTER + halfWidth,
					offsetY + halfHeight + adjustCenter);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (!isEnabled() || mHeadView == null || mContentView == null) {
			return super.dispatchTouchEvent(ev);
		}

		obtainVelocityTracker(ev);
		final int action = ev.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				mHasSendCancelEvent = false;
				mDownEvent = ev;
				mHeaderController.onTouchDown(ev.getX(), ev.getY());

				if (mBounceAnim != null && mBounceAnim.isRunning()) {
					mBounceAnim.cancel();
				}

				mScrollChecker.abortWorking();
				mPreventForHorizontal = false;
				return super.dispatchTouchEvent(ev);
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				final float initVelocity = getInitVelocity();
				releaseVelocity();
				if (mHeaderController.isInTouch()) {
					mHeaderController.onTouchRelease();
					onRelease((int) initVelocity);

					if (mHeaderController.hasMoved()) {
						sendCancelEvent();
						return true;
					}
					return super.dispatchTouchEvent(ev);
				} else {
					return super.dispatchTouchEvent(ev);
				}

			case MotionEvent.ACTION_MOVE:
				mLastMoveEvent = ev;
				mHeaderController.onTouchMove(ev.getX(), ev.getY());

				int offsetX = mHeaderController.getOffsetX();
				int offsetY = mHeaderController.getOffsetY();

				if (mDisableWhenHorizontalMove && !mPreventForHorizontal
						&& (Math.abs(offsetX) > mPagingTouchSlop
						&& Math.abs(offsetX) > Math.abs(offsetY))) {
					mPreventForHorizontal = true;
				}
				if (mPreventForHorizontal) {
					return super.dispatchTouchEvent(ev);
				}

				boolean moveDown = offsetY > 0;

				if (moveDown) {
					if (mContentView != null && mScrollHandler.canScrollDown(mContentView)) {
						if (mHeaderController.isInTouch()) {
							mHeaderController.onTouchRelease();
							sendDownEvent();
						}
						return super.dispatchTouchEvent(ev);
					} else {
						if (!mHeaderController.isInTouch()) {
							mHeaderController.onTouchDown(ev.getX(), ev.getY());
							offsetY = mHeaderController.getOffsetY();
						}
						willMovePos(offsetY);
						return true;
					}
				} else {
					if (mHeaderController.canMoveUp()) {
						willMovePos(offsetY);
						return true;
					} else {
						if (mHeaderController.isInTouch()) {
							mHeaderController.onTouchRelease();
							sendDownEvent();
						}
						return super.dispatchTouchEvent(ev);
					}
				}

		}
		return super.dispatchTouchEvent(ev);
	}

	private void willMovePos(int deltaY) {
		int delta = mHeaderController.willMove(deltaY);
		//Log.d(TAG, String.format("willMovePos deltaY = %s, delta = %d", deltaY, delta));

		if (delta == 0) {
			return;
		}

		if (!mHasSendCancelEvent && mHeaderController.isInTouch()) {
			sendCancelEvent();
		}

		if (mPullState != STATE_DRAGE) {
			mPullState = STATE_DRAGE;
		}

		movePos(delta);
	}

	private void movePos(int delta) {
		if (mContentView != null) {
			mContentView.offsetTopAndBottom(delta);
		}

		if (mActionView != null) {
			mActionView.offsetTopAndBottom((int) delta);

			mFlyView.offsetTopAndBottom((int) delta);

			float percentage = mHeaderController.getMovePercentage();
			onMoveHeader(mPullState, percentage);

			if (mPullHeaderListenerView != null) {
				mPullHeaderListenerView.onPullProgress(this, mPullState, percentage);
			}

//			if (mPullListener != null) {
//				mPullListener.onPullProgress(this, mPullState, percentage);
//			}
		}
	}

	private void onMoveHeader(int pullState, float percentage) {

	}

	private void sendDownEvent() {
		final MotionEvent last = mLastMoveEvent;
		MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime(),
				MotionEvent.ACTION_DOWN, last.getX(), last.getY(), last.getMetaState());
		super.dispatchTouchEvent(e);
	}

	private void sendCancelEvent() {
		MotionEvent last = mLastMoveEvent;
		MotionEvent e = MotionEvent.obtain(last.getDownTime(),
				last.getEventTime() + ViewConfiguration.getLongPressTimeout(),
				MotionEvent.ACTION_CANCEL, last.getX(), last.getY(), last.getMetaState());
		super.dispatchTouchEvent(e);
	}

	private void onRelease(int initVelocity) {
		mScrollChecker.tryToScrollTo(initVelocity);
	}

	private void releaseVelocity() {
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	private void obtainVelocityTracker(MotionEvent ev) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
	}

	private float getInitVelocity() {
		if (mVelocityTracker != null) {
			mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
			return mVelocityTracker.getYVelocity();
		}
		return 0;
	}

	class ScrollChecker implements Runnable {
		private Scroller mScroller;
		private boolean mIsRunning = false;
		private int mStart;

		public ScrollChecker() {
			mScroller = new Scroller(getContext());
		}

		@Override
		public void run() {
			boolean finish = !mScroller.computeScrollOffset();
			int curY = mScroller.getCurrY();
			int deltaY = mHeaderController.moveTo(curY);
			//Log.d(TAG, String.format("Scroller: currY = %d, deltaY = %d", curY, deltaY));

			if (!finish) {
				movePos(deltaY);
				postDelayed(this, 10);
			} else {
				finish();
			}
		}

		private void finish() {
			reset();
			onScrollFinish();
		}

		private void reset() {
			mIsRunning = false;
			removeCallbacks(this);
		}


		public void abortWorking() {
			if (mIsRunning) {
				if (!mScroller.isFinished()) {
					mScroller.forceFinished(true);
				}
				//onPtrScrollAbort();
				reset();
			}
		}

		public void tryToScrollTo(int initVelocity) {
			mPullState = STATE_FLING;

			mStart = mHeaderController.getCurrentPos();
			removeCallbacks(this);

			// fix #47: Scroller should be reused, https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh/issues/47
			if (!mScroller.isFinished()) {
				mScroller.forceFinished(true);
			}
			mHeaderController.startMove();

			mScroller.setFriction(ViewConfiguration.getScrollFriction() * 4);
			mScroller.fling(0, mStart, 0, initVelocity, 0, 0, mHeaderController.getMinHeight(),
					mHeaderController.getMaxHeight());

			//Log.d(TAG, String.format("Scroller: velocity = %d, duration = %d", velocity, mScroller.getDuration()));

			post(this);
			mIsRunning = true;
		}
	}

	private void onScrollFinish() {
		if (mHeaderController.isOverHeight()) {
			mBounceAnim = ObjectAnimator.ofFloat(mHeaderController.getCurrentPos(), mHeaderController.getHeight());
			mBounceAnim.setInterpolator(new ElasticOutInterpolator());
			mBounceAnim.setDuration(500);
			mBounceAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float value = (float) animation.getAnimatedValue();
					movePos(mHeaderController.moveTo(value));
				}
			});
			mBounceAnim.addListener(new SimpleAnimatorListener() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mPullState = STATE_IDLE;
				}
			});

			mBounceAnim.start();
			mPullState = STATE_BOUNCE;

			if (mHeaderController.needSendRefresh()) {
				onStartRefreshAnimation();
			}
		} else {
			mPullState = STATE_IDLE;
		}
	}

	protected void onStartRefreshAnimation() {

	}

	public void setHeaderView(View headerView, LayoutParams lp) {
		if (mHeadView != null) {
			removeView(mHeadView);
		}

		addView(headerView, 0, lp);

		mHeadView = headerView;

	}

}
