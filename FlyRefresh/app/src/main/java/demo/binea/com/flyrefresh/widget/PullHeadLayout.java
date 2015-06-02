package demo.binea.com.flyrefresh.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;

import demo.binea.com.flyrefresh.R;
import demo.binea.com.flyrefresh.utils.UIUtils;

/**
 * Created by xubinggui on 6/2/15.
 */
public class PullHeadLayout extends FrameLayout {

	private final String TAG = this.getClass().getCanonicalName();

	private final static int DEFAULT_EXPAND = UIUtils.dpToPx(300);
	private final static int DEFAULT_HEIGHT = UIUtils.dpToPx(240);
	private final static int DEFAULT_SHRINK = UIUtils.dpToPx(48);
	private final static int ACTION_ICON_SIZE = UIUtils.dpToPx(32);

	private int mHeaderId = 0;
	private int mContentId = 0;

	private View mHeadView;
	private View mContentView;
	private ImageView mFlyView;

	private Drawable mActionDrawable;

	private int mPagingTouchSlop;
	private int mMaxVelocity;

	private FloatingActionButton mActionView;

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

		if(null != attrs){
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

//		mHeaderController = new HeaderController(headerHeight, headerExpandHeight, headerShrinkHeight);
		final ViewConfiguration conf = ViewConfiguration.get(getContext());
		mPagingTouchSlop = conf.getScaledTouchSlop() * 2;

		mMaxVelocity = conf.getScaledMaximumFlingVelocity();
//		mScrollHandler = new DefalutScrollHandler();

//		mScrollChecker = new ScrollChecker();


	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		final int childCount = getChildCount();

		if(childCount > 2){
			throw new IllegalArgumentException("FlyRefreshLayout only can host 2 elements");
		}else if(childCount == 2){
			if(mHeaderId != 0 && mHeadView == null){
				mHeadView = findViewById(mHeaderId);
			}

			if(mContentId != 0 && mContentView == null){
				mContentView = findViewById(mContentId);
			}

			if(mHeadView == null || mContentView == null){
				mHeadView = getChildAt(0);
				mContentView = getChildAt(1);
			}
		}else if(childCount == 1){
			mContentView = getChildAt(0);
		}

		setActionDrawable(mActionDrawable);

	}

	private void setActionDrawable(Drawable actionDrawable) {
		if(actionDrawable != null){
			if(mActionView == null){
				final int bgColor = UIUtils.getThemeColorFromAttrOrRes(getContext(), R.attr.colorAccent, R.color.accent);
				final int pressedColor = UIUtils.darkerColor(bgColor, 0.8f);
				mActionView = new FloatingActionButton(getContext());
				mActionView.setBackgroundTintList(UIUtils.createColorStateList(bgColor, pressedColor));
				addView(mActionView);
			}else {
				if (mFlyView == null) {
					mFlyView = new ImageView(getContext());
					mFlyView.setScaleType(ImageView.ScaleType.FIT_XY);
					addView(mFlyView, new LayoutParams(ACTION_ICON_SIZE, ACTION_ICON_SIZE));
				}
				mFlyView.setImageDrawable(mActionDrawable);
			}
		}else{
			if(mActionView != null){
				removeView(mActionView);
				removeView(mFlyView);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if(mHeadView != null){
			//TODO miniHeight
			measureChildWithMargins(mHeadView, widthMeasureSpec, 0, heightMeasureSpec, 0);
		}

		if(mContentView != null){
			//TODO miniHeight
			measureChildWithMargins(mContentView, widthMeasureSpec, 0, heightMeasureSpec, 0);
		}

		if(mActionView != null){
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

	}
}
