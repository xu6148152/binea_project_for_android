package android.support.v4.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * {@linkplain #setChildrenDrawingOrderEnabledCompat(boolean)} does some reflection that isn't needed.
 * And was making view creation time rather large. So lets override it and make it better!
 */
public class BetterViewPager extends ViewPager {

    private int mTouchSlop;
    private int originX;

    public BetterViewPager(Context context) {
        super(context);
    }

    public BetterViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public void setChildrenDrawingOrderEnabledCompat(boolean enable) {
        setChildrenDrawingOrderEnabled(enable);
    }

    //@Override public boolean dispatchTouchEvent(MotionEvent ev) {
    //    final int action = ev.getAction();
    //    switch (action) {
    //        case MotionEvent.ACTION_DOWN:
    //            originX = (int) ev.getX();
    //            break;
    //        case MotionEvent.ACTION_MOVE:
    //            final int xDiff = calculateDistanceX(ev);
    //            Log.d("dispatchTouchEvent", "xDiff " + xDiff);
    //            if(xDiff > mTouchSlop) {
    //                return true;
    //            }
    //            break;
    //    }
    //    return super.dispatchTouchEvent(ev);
    //}

    private int calculateDistanceX(MotionEvent ev) {
        int x = (int) ev.getX();
        int diff = Math.abs(x - originX);
        originX = x;
        return diff;
    }
}
