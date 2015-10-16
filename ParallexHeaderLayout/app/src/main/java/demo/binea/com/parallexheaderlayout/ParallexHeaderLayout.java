package demo.binea.com.parallexheaderlayout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by xubinggui on 10/16/15.
 */
public class ParallexHeaderLayout extends FrameLayout {

    private View mHeader;
    private View mContent;
    private ViewDragHelper mViewDragHelper;

    private int ratio = 1;

    private int offset = 0;

    enum STATE{
        NORMAL,OVER_SCROLL,SCROLL_UP;
    }

    private STATE mCurrentState = STATE.NORMAL;

    private final static String TAG = ParallexHeaderLayout.class.getCanonicalName();

    public ParallexHeaderLayout(Context context) {
        super(context);
    }

    public ParallexHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, mCallback);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount() < 2){
            throw new IllegalArgumentException("please ensure layout has tow children");
        }
        mHeader = getChildAt(0);
        mContent = getChildAt(1);
    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override public int getViewVerticalDragRange(View child) {
            return Integer.MAX_VALUE;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            Log.d(TAG, "top " + top + " dx " + dx + " dy " + dy);
            mHeader.offsetTopAndBottom(dy);
            mContent.offsetTopAndBottom(dy * ratio);
            offset +=dy;
        }

        @Override public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Log.d(TAG, "xvel " + xvel + " yvel " + yvel);
            resetLayout();
        }
    };

    private void resetLayout() {
        mHeader.animate().setDuration(200).translationY(0);
        mContent.animate().setDuration(200).translationY(0);
    }

    @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean shouldInterceptTouch = false;
        final int action = ev.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            downY = ev.getY();
        }else if(action == MotionEvent.ACTION_MOVE){
            if(ev.getY() - downY > 0){
                RecyclerView view = (RecyclerView) mContent;
                if(view.getScrollY() >= 0){
                    shouldInterceptTouch = true;
                }else{
                    shouldInterceptTouch = false;
                }
            }else{
                shouldInterceptTouch = false;
            }
        }
        Log.d(TAG, "onInterceptTouchEvent " + shouldInterceptTouch);
        return shouldInterceptTouch && mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    float downY = 0;
    @Override public boolean onTouchEvent(MotionEvent event) {
        try{
            mViewDragHelper.processTouchEvent(event);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @Override public void computeScroll() {
        if(mViewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
