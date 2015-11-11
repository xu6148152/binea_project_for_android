package com.astuetz;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.astuetz.pagerslidingtabstrip.R;

/**
 * Created by xubinggui on 10/15/15.
 */
public class SlidingTabWithIndicatorLayout extends AppBarLayout {

    private PagerSlidingTabStrip tabs;
    private LinearLayout ll_indicator_container;
    private int indicator;

    public SlidingTabWithIndicatorLayout(Context context) {
        this(context, null);
    }

    public SlidingTabWithIndicatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setBackgroundColor(getResources().getColor(R.color.common_bg));
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.SlidingTabWithIndicatorLayout);
        indicator = ta.getResourceId(R.styleable.SlidingTabWithIndicatorLayout_indicator, -1);
        int tabHeight = ta.getDimensionPixelOffset(
                R.styleable.SlidingTabWithIndicatorLayout_tab_height, dp2px(40)
                );
        ta.recycle();
        View view = LayoutInflater.from(context).inflate(R.layout.sliding_tabs, this, true);
        ll_indicator_container =
                (LinearLayout) view.findViewById(R.id.ll_indicator_container);
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.top_tabs);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabs.getLayoutParams();
        lp.height = tabHeight;
        tabs.setLayoutParams(lp);
    }

    public void setViewPagerWithIndicator(ViewPager viewPager) {
        if(null == viewPager){
            return;
        }
        int count = viewPager.getAdapter().getCount();
        for(int i = 0; i <count; i++) {
            View view = new View(getContext());
            view.setLayoutParams(new LinearLayout.LayoutParams(dp2px(6), dp2px(6)));
            view.setBackgroundResource(indicator);
            ll_indicator_container.addView(view);
            if(i == 0) {
                view.setSelected(true);
            }else {
                LinearLayout.MarginLayoutParams mlp = (MarginLayoutParams) view.getLayoutParams();
                mlp.leftMargin = dp2px(9);
                view.setLayoutParams(mlp);
            }
        }
        tabs.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {
                updateIndicator(position);
            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateIndicator(int pos){
        for(int i = 0; i<ll_indicator_container.getChildCount();i++){
            if(i == pos) {
                ll_indicator_container.getChildAt(i).setSelected(true);
            }else{
                ll_indicator_container.getChildAt(i).setSelected(false);
            }
        }
    }

    public void setTypeFace(Typeface typeFace){
        tabs.setTypeface(typeFace, 0);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
