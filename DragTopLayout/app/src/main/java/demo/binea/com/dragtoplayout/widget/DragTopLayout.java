package demo.binea.com.dragtoplayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import demo.binea.com.dragtoplayout.R;

/**
 * Created by xubinggui on 5/23/15.
 */
public class DragTopLayout extends FrameLayout {

	private static final String TAG = DragTopLayout.class.getSimpleName();
	private ViewDragHelper mViewDragHelper;
	private int collapseOffset;
	private boolean overDrag = true;
	private int dragContentViewId;
	private int topViewId;
	private boolean captureTop;
	private PanelState panelState;

	private View topView;
	private View contentView;
	private int dragRange;
	private int contentTop;
	private int topViewHeight;
	private int top;
	private boolean shouldIntercept = false;
	private float ratio;
	private boolean dispatchingChildrenDownFaked;
	private float dispatchingChildrenStartedAtY;
	private boolean dispatchingChildrenContentView;
	private boolean isRefreshing = false;
	private float refreshRatio = 1.0f;
	private PanelListener panelListener;

	private boolean isAutoScrollBack = true;

	public static enum PanelState{
		COLLAPSED(0),
		EXPANDED(1),
		SLIDING(2);

		private int asInt;

		PanelState(int i){
			asInt = i;
		}

		public static PanelState fromeInt(int i){
			switch (i){
				case 0:
					return COLLAPSED;
				case 2:
					return SLIDING;

				default:
				case 1:
					return EXPANDED;
			}
		}

		public int toInt() {
			return asInt;
		}
	}

	public DragTopLayout(Context context) {
		this(context, null);
	}

	public DragTopLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DragTopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		initAttribute(attrs);
	}

	private void initAttribute(AttributeSet attrs) {
		mViewDragHelper = ViewDragHelper.create(this, 1.0f, mCallback);
		final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DragTopLayout);
		setCollapseOffset(a.getDimensionPixelSize(R.styleable.DragTopLayout_dtlCollapseOffset, collapseOffset));
		overDrag = a.getBoolean(R.styleable.DragTopLayout_dtlOverDrag, overDrag);
		dragContentViewId = a.getResourceId(R.styleable.DragTopLayout_dtlDragContentView, -1);
		topViewId = a.getResourceId(R.styleable.DragTopLayout_dtlTopView, -1);
		initOpen(a.getBoolean(R.styleable.DragTopLayout_dtlOpen, true));
		captureTop = a.getBoolean(R.styleable.DragTopLayout_dtlCaptureTop, true);
		a.recycle();
	}

	private void initOpen(boolean isOpen) {
		if (isOpen) {
			panelState = PanelState.EXPANDED;
		} else {
			panelState = PanelState.COLLAPSED;
		}
	}

	private void calculateRatio(float top) {
		ratio = (top-collapseOffset) / (topViewHeight - collapseOffset);
		if (dispatchingChildrenContentView) {
			resetDispatchingContentView();
		}

		if (panelListener != null) {
			// Calculate the ratio while dragging.
			panelListener.onSliding(ratio);
			if (ratio > refreshRatio && !isRefreshing) {
				isRefreshing = true;
				panelListener.onRefresh();
			}
		}
	}

	private void updatePanelState(){
		if (contentTop <= getPaddingTop() + collapseOffset) {
			panelState = PanelState.COLLAPSED;
		} else if(contentTop >= topView.getHeight()){
			panelState = PanelState.EXPANDED;
		} else {
			panelState = PanelState.SLIDING;
		}

		if (panelListener != null) {
			panelListener.onPanelStateChanged(panelState);
		}
	}

//	@Override
//	protected Parcelable onSaveInstanceState() {
//
//		Parcelable superState = super.onSaveInstanceState();
//		SavedState state = new SavedState(superState);
//		state.panelState = panelState.toInt();
//
//		return state;
//	}

	ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			if(child == topView && captureTop){
				mViewDragHelper.captureChildView(contentView, pointerId);
				return false;
			}
			return child == contentView;
		}

		@Override
		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
			super.onViewPositionChanged(changedView, left, top, dx, dy);
			contentTop = top;
			requestLayout();
			calculateRatio(contentTop);
			updatePanelState();
		}

		@Override
		public int getViewVerticalDragRange(View child) {
			return dragRange;
		}

		@Override
		public int clampViewPositionVertical(View child, int top, int dy) {
			if (overDrag) {
				// Drag over the top view height.
				return Math.max(top, getPaddingTop() + collapseOffset);
			} else {
				return Math.min(topViewHeight, Math.max(top, getPaddingTop() + collapseOffset));
			}
		}

		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			super.onViewReleased(releasedChild, xvel, yvel);

			if(isAutoScrollBack) {
				int top;
				if (yvel > 0 || contentTop > topViewHeight) {
					top = topViewHeight + getPaddingTop();
				} else {
					top = getPaddingTop() + collapseOffset;
				}
				mViewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
				postInvalidate();
			}
		}
	};

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		if(getChildCount() < 2){
			throw new RuntimeException("Content view must contains two child views at least.");
		}

//		if(topViewId == -1 || dragContentViewId == -1){
//			throw new IllegalArgumentException("make sure you have set topview and contentview both");
//		}

//		topView = findViewById(topViewId);
//		contentView = findViewById(dragContentViewId);
		topView = getChildAt(0);
		contentView = getChildAt(1);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		dragRange = getHeight();

		int contentTopTemp = contentTop;
		resetTopViewHeight();
		resetContentHeight();

		topView.layout(left, Math.min(topView.getPaddingTop(), contentTop - topView.getHeight()), right, contentTop);
		contentView.layout(left, contentTopTemp, right, contentTopTemp + contentView.getHeight());
	}

	private void resetContentHeight() {
		if (contentView != null && contentView.getHeight() != 0) {
			ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
			layoutParams.height = getHeight() - collapseOffset;
			contentView.setLayoutParams(layoutParams);
		}
	}

	private void resetTopViewHeight() {
		final int newTopViewHeight = topView.getHeight();

		if(newTopViewHeight != topViewHeight){
			if(panelState == PanelState.EXPANDED){
				contentTop = newTopViewHeight;
				handleSlide(newTopViewHeight);
			}else if(panelState == PanelState.COLLAPSED){
				contentTop = collapseOffset;
			}
			topViewHeight = newTopViewHeight;
		}
	}

	private void handleSlide(final int top) {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				mViewDragHelper.smoothSlideViewTo(contentView, getPaddingLeft(), top);
				postInvalidate();
			}
		});
	}

	private void resetDragContent(boolean isAnim, int top){
		contentTop = top;

		if (isAnim) {
			mViewDragHelper.smoothSlideViewTo(contentView, getPaddingLeft(), contentTop);
			postInvalidate();
		} else {
			requestLayout();
		}
	}

	@Override
	public void computeScroll() {
		if(mViewDragHelper.continueSettling(true)){
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.d(TAG, "viewTop " + topView.getTop());
		if(topView.getTop() >= 0){
			return true;
		}
		final boolean b = mViewDragHelper.shouldInterceptTouchEvent(ev);
		Log.d(TAG, "[onInterceptTouchEvent] shouldIntercept " + shouldIntercept + " b " + b);
		return shouldIntercept && b;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d(TAG,"[onTouchEvent]");
		final int action = event.getActionMasked();

		if(!dispatchingChildrenContentView){
			mViewDragHelper.processTouchEvent(event);
		}

		if(action == MotionEvent.ACTION_MOVE && ratio == 0.0f){
			dispatchingChildrenContentView = true;
			if(!dispatchingChildrenDownFaked){
				dispatchingChildrenStartedAtY = event.getY();
				event.setAction(MotionEvent.ACTION_DOWN);
				dispatchingChildrenDownFaked = true;
			}
			Log.d(TAG, "contentView dispatchTouchEvent");
			contentView.dispatchTouchEvent(event);
		}

		if (dispatchingChildrenContentView && dispatchingChildrenStartedAtY < event.getY()) {
			resetDispatchingContentView();
		}

		if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
			resetDispatchingContentView();
			contentView.dispatchTouchEvent(event);
		}

		return true;
	}

	private void resetDispatchingContentView() {
		dispatchingChildrenDownFaked = false;
		dispatchingChildrenContentView = false;
		dispatchingChildrenStartedAtY = Float.MAX_VALUE;
	}

	public PanelState getState() {
		return panelState;
	}

	public void openTopView(boolean anim) {
		// Before created
		if (contentView.getHeight() == 0) {
			panelState = PanelState.EXPANDED;
			if (panelListener != null) {
				panelListener.onSliding(1.0f);
			}
		} else {
			resetDragContent(anim, topViewHeight);
		}
	}

	public void closeTopView(boolean anim) {
		if (contentView.getHeight() == 0) {
			panelState = PanelState.COLLAPSED;
			if (panelListener != null) {
				panelListener.onSliding(0.0f);
			}
		}else{
			resetDragContent(anim, getPaddingTop() + collapseOffset);
		}
	}

	public void updateTopViewHeight(int height){
		ViewGroup.LayoutParams layoutParams = topView.getLayoutParams();
		layoutParams.height = height;
		topView.setLayoutParams(layoutParams);
	}

	public void toggleTopView() {
		toggleTopView(false);
	}

	public void toggleTopView(boolean touchMode) {
		switch (panelState) {
			case COLLAPSED:
				openTopView(true);
				if (touchMode) {
					setTouchMode(true);
				}
				break;
			case EXPANDED:
				closeTopView(true);
				if (touchMode) {
					setTouchMode(false);
				}
				break;
		}
	}

	public DragTopLayout setTouchMode(boolean shouldIntercept) {
		this.shouldIntercept = shouldIntercept;
		return this;
	}

	/**
	 * Setup the drag listener.
	 *
	 * @return SetupWizard
	 */
	public DragTopLayout listener(PanelListener panelListener) {
		this.panelListener = panelListener;
		return this;
	}

	/**
	 * Set the refresh position while dragging you want.
	 * The default value is 1.5f.
	 *
	 * @return SetupWizard
	 */
	public DragTopLayout setRefreshRatio(float ratio) {
		this.refreshRatio = ratio;
		return this;
	}

	/**
	 * Set enable drag over.
	 * The default value is true.
	 *
	 * @return SetupWizard
	 */
	public DragTopLayout setOverDrag(boolean overDrag) {
		this.overDrag = overDrag;
		return this;
	}

	/**
	 * Set the content view. Pass the id of the view (R.id.xxxxx).
	 * This one will be set as the content view and will be dragged together with the topView
	 *
	 * @param id The id (R.id.xxxxx) of the content view.
	 * @return
	 */
	public DragTopLayout setDragContentViewId(int id) {
		this.dragContentViewId = id;
		return this;
	}

	/**
	 * Set the top view. The top view is the header view that will be dragged out.
	 * Pass the id of the view (R.id.xxxxx)
	 *
	 * @param id The id (R.id.xxxxx) of the top view
	 * @return
	 */
	public DragTopLayout setTopViewId(int id) {
		this.topViewId = id;
		return this;
	}

	public boolean isOverDrag() {
		return overDrag;
	}

	/**
	 * Get refresh state
	 */
	public boolean isRefreshing() {
		return isRefreshing;
	}

	public void setRefreshing(boolean isRefreshing) {
		this.isRefreshing = isRefreshing;
	}

	/**
	 * Complete refresh and reset the refresh state.
	 */
	public void onRefreshComplete() {
		isRefreshing = false;
	}

	public void setIsAutoScrollBack(){
		isAutoScrollBack = !isAutoScrollBack;
		overDrag = !overDrag;
	}

	/**
	 * Set the collapse offset
	 *
	 * @return SetupWizard
	 */
	public DragTopLayout setCollapseOffset(int px) {
		collapseOffset = px;
		resetContentHeight();
		return this;
	}

	public int getCollapseOffset() {
		return collapseOffset;
	}


	// ---------------------

	public interface PanelListener {
		/**
		 * Called while the panel state is changed.
		 */
		public void onPanelStateChanged(PanelState panelState);

		/**
		 * Called while dragging.
		 * ratio >= 0.
		 */
		public void onSliding(float ratio);

		/**
		 * Called while the ratio over refreshRatio.
		 */
		public void onRefresh();
	}

	public static class SimplePanelListener implements PanelListener {

		@Override
		public void onPanelStateChanged(PanelState panelState) {

		}

		@Override
		public void onSliding(float ratio) {

		}

		@Deprecated
		@Override
		public void onRefresh() {

		}
	}

//	/**
//	 * Save the instance state
//	 */
//	private static class SavedState extends BaseSavedState {
//
//		int panelState;
//
//		SavedState(Parcelable superState) {
//			super(superState);
//		}
//
//	}

}
