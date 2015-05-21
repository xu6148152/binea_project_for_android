package demo.binea.com.bezierindicator.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import demo.binea.com.bezierindicator.R;
import demo.binea.com.bezierindicator.widget.listener.TabClickListener;

/**
 * Created by xubinggui on 5/20/15.
 */
public class BezierIndicator extends FrameLayout {

	private static final float INDICATOR_ANIM_DURATION = 3000;
	private int textColor;
	private int selectedTextColor;
	private float textSize;
	private int textBg;
	private int indicatorColor;
	private int indicatorColors;
	private float radiusMax;
	private float radiusMin;
	private int[] indicatorColorArray;
	private float radiusOffset;

	private ViewPager mViewPager;
	private BezierView mBezierView;
	private List<TextView> tabs;
	private LinearLayout mTabContainer;
	private float headMoveOffset = 0.6f;
	private float acceleration = 0.5f;
	private float footMoveOffset = 1 - headMoveOffset;

	public BezierIndicator(Context context) {
		this(context, null);
	}

	public BezierIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttribute(attrs);
	}

	private void initAttribute(AttributeSet attrs) {

		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SpringIndicator);
		textColor = a.getResourceId(R.styleable.SpringIndicator_siTextColor, R.color.si_default_text_color);
		selectedTextColor = a.getResourceId(R.styleable.SpringIndicator_siSelectedTextColor, R.color.si_default_text_color_selected);
		textSize = a.getDimension(R.styleable.SpringIndicator_siTextSize, getResources().getDimension(R.dimen.si_default_text_size));
		textBg = a.getResourceId(R.styleable.SpringIndicator_siTextBg, 0);
		indicatorColor = a.getResourceId(R.styleable.SpringIndicator_siIndicatorColor, R.color.si_default_indicator_bg);
		indicatorColors = a.getResourceId(R.styleable.SpringIndicator_siIndicatorColors, 0);
		radiusMax = a.getDimension(R.styleable.SpringIndicator_siRadiusMax, getResources().getDimension(R.dimen.si_default_radius_max));
		radiusMin = a.getDimension(R.styleable.SpringIndicator_siRadiusMin, getResources().getDimension(R.dimen.si_default_radius_min));
		a.recycle();

		if(indicatorColors != 0){
			indicatorColorArray = getResources().getIntArray(indicatorColors);
		}
		radiusOffset = radiusMax - radiusMin;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		createPoints();
		setSelectedTextColor(mViewPager.getCurrentItem());
	}

	public void setViewPager(ViewPager viewPager){
		this.mViewPager = viewPager;
		initBezierIndicator();
		setBezierChangeListener();
	}

	private void setBezierChangeListener() {
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				if (position < tabs.size() - 1) {
					float radiusOffsetHead = 0.5f;
					if (positionOffset < radiusOffsetHead) {
						mBezierView.getHeadPoint().setRadius(radiusMin);
					} else {
						mBezierView.getHeadPoint().setRadius(
								(positionOffset - radiusOffsetHead) /
										(1 - radiusOffsetHead) * radiusOffsetHead + radiusMin);
					}
					float radiusOffsetFoot = 0.5f;
					if (positionOffset < radiusOffsetFoot) {
						mBezierView.getFootPoint().setRadius((1 - positionOffset / radiusOffsetFoot) * radiusOffset + radiusMin);
					} else {
						mBezierView.getFootPoint().setRadius(radiusMin);
					}

					// x
					float headX = 1f;
					if (positionOffset < headMoveOffset) {
						float positionOffsetTemp = positionOffset / headMoveOffset;
						headX = (float) ((Math.atan(positionOffsetTemp * acceleration * 2 - acceleration) + (Math.atan(acceleration))) / (2 * (Math.atan(acceleration))));
					}
					mBezierView.getHeadPoint().setX(getTabX(position) - headX * getPositionDistance(position));
					float footX = 0f;
					if (positionOffset > footMoveOffset) {
						float positionOffsetTemp = (positionOffset - footMoveOffset) / (1 - footMoveOffset);
						footX = (float) ((Math.atan(positionOffsetTemp * acceleration * 2 - acceleration) + (Math.atan(acceleration))) / (2 * (Math.atan(acceleration))));
					}
					mBezierView.getFootPoint().setX(getTabX(position) - footX * getPositionDistance(position));

					// reset radius
					if (positionOffset == 0) {
						mBezierView.getHeadPoint().setRadius(radiusMax);
						mBezierView.getFootPoint().setRadius(radiusMax);
					}
				} else {
					mBezierView.getHeadPoint().setX(getTabX(position));
					mBezierView.getFootPoint().setX(getTabX(position));
					mBezierView.getHeadPoint().setRadius(radiusMax);
					mBezierView.getFootPoint().setRadius(radiusMax);
				}

				// set indicator colors
				// https://github.com/TaurusXi/GuideBackgroundColorAnimation
				if (indicatorColors != 0) {
					float length = (position + positionOffset) / mViewPager.getAdapter().getCount();
					int progress = (int) (length * INDICATOR_ANIM_DURATION);
					seek(progress);
				}

				mBezierView.postInvalidate();
				if (mDelegateListener != null) {
					mDelegateListener.onPageSelected(position, positionOffset, positionOffsetPixels);
				}
			}

			@Override
			public void onPageSelected(int position) {
				setSelectedTextColor(position);
				if (null != mDelegateListener) {
					mDelegateListener.onPageSelected(position);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				if (mDelegateListener != null) {
					mDelegateListener.onPageScrollStateChanged(state);
				}
			}
		});
	}

	private void seek(int progress) {

	}

	private float getPositionDistance(int position) {
		float tarX = tabs.get(position + 1).getX();
		float oriX = tabs.get(position).getX();
		return oriX - tarX;
	}

	private float getTabX(int position) {
		return tabs.get(position).getX() + tabs.get(position).getWidth() / 2;
	}

	private void initBezierIndicator() {
		addPointView();
		addTabContainerView();
		addTabItems();
	}

	private void addTabItems() {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
		tabs = new ArrayList<>();
		for(int i = 0;i<mViewPager.getAdapter().getCount();i++){
			TextView textView = new TextView(getContext());
			if(mViewPager.getAdapter().getPageTitle(i) != null){
				textView.setText(mViewPager.getAdapter().getPageTitle(i));
			}

			textView.setGravity(Gravity.CENTER);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
			textView.setTextColor(getResources().getColor(textColor));
			if(textBg != 0){
				textView.setBackgroundResource(textBg);
			}
			final int pos = i;
			textView.setLayoutParams(layoutParams);
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mTabClickListener == null || mTabClickListener.onTabClick(pos)) {
						mViewPager.setCurrentItem(pos);
					}
				}
			});

			tabs.add(textView);
			mTabContainer.addView(textView);
		}
	}

	private void addPointView() {
		mBezierView = new BezierView(getContext());
		mBezierView.setIndicatorColor(getResources().getColor(indicatorColor));
		addView(mBezierView);
	}

	private void addTabContainerView(){
		mTabContainer = new LinearLayout(getContext());
		mTabContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
		mTabContainer.setOrientation(LinearLayout.HORIZONTAL);
		mTabContainer.setGravity(Gravity.CENTER);
		addView(mTabContainer);
	}

	private void createPoints() {
		View view = tabs.get(mViewPager.getCurrentItem());
		mBezierView.getHeadPoint().setX(view.getX() + view.getWidth() / 2);
		mBezierView.getHeadPoint().setY(view.getY() + view.getHeight() / 2);
		mBezierView.getFootPoint().setX(view.getX() + view.getWidth() / 2);
		mBezierView.getFootPoint().setY(view.getY() + view.getHeight() / 2);
		mBezierView.animCreate();
	}

	private void setSelectedTextColor(int position){
		for (TextView tab : tabs) {
			tab.setTextColor(getResources().getColor(textColor));
		}
		tabs.get(position).setTextColor(getResources().getColor(selectedTextColor));
	}

	private TabClickListener mTabClickListener;

	public DelegateListener getDelegateListener() {
		return mDelegateListener;
	}

	private DelegateListener mDelegateListener;

	public void setTabClickListener(TabClickListener tabClickListener) {
		mTabClickListener = tabClickListener;
	}

	public interface DelegateListener{
		void onPageSelected(int pos);

		void onPageSelected(int position, float positionOffset, int positionOffsetPixels);

		void onPageScrollStateChanged(int state);
	}
}
