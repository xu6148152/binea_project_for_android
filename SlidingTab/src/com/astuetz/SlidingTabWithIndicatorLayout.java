package com.astuetz;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
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


    public SlidingTabWithIndicatorLayout(Context context) {
        this(context, null);
    }

    public SlidingTabWithIndicatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setBackgroundColor(getResources().getColor(R.color.common_bg));
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingTabWithIndicatorLayout);
        int indicator = ta.getResourceId(R.styleable.SlidingTabWithIndicatorLayout_indicator, -1);
        View view = LayoutInflater.from(context).inflate(R.layout.sliding_tabs, this, true);
        ll_indicator_container =
                (LinearLayout) view.findViewById(R.id.ll_indicator_container);
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.top_tabs);
        for(int i = 0;i<ll_indicator_container.getChildCount();i++){
            ll_indicator_container.getChildAt(i).setBackgroundResource(indicator);
            if(i == 0){
                ll_indicator_container.getChildAt(i).setSelected(true);
            }
        }
        ta.recycle();
    }

    public void setViewPagerWithIndicator(ViewPager viewPager) {
        if(null == viewPager){
            return;
        }
        tabs.getLayoutParams();
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
}
