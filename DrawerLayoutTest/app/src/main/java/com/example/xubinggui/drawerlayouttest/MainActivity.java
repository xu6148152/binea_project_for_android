package com.example.xubinggui.drawerlayouttest;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;


public class MainActivity extends ActionBarActivity implements GestureDetector.OnGestureListener{

	private final String TAG = MainActivity.class.getSimpleName();
	private DrawerLayout mDrawerLayout;

	private GestureDetectorCompat mDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDetector = new GestureDetectorCompat(this,this);

		initView();

		initListener();

	}

	public void OpenRightMenu(View view)
	{
		mDrawerLayout.openDrawer(Gravity.RIGHT);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
				Gravity.RIGHT);
	}

	public void openLeftMenu(){
		mDrawerLayout.openDrawer(Gravity.LEFT);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
				Gravity.LEFT);
	}

	private void initListener() {
		mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
			@Override
			public void onDrawerSlide(View view, float v) {
				final View content = mDrawerLayout.getChildAt(0);
				View mMenu = view;
				float scale = 1 - v;
				float rightScale = 0.8f + scale * 0.2f;

				if("LEFT".equals(mMenu.getTag())){
					float leftScale = 1 - 0.3f * scale;
					view.setScaleX(leftScale);
					view.setScaleY(leftScale);
					view.setAlpha(0.6f + 0.4f * (1 - scale));
					content.setTranslationX(mMenu.getMeasuredWidth() * (1-scale));
					content.setPivotX(0);
					content.setPivotY(content.getMeasuredHeight()/2);
					content.invalidate();
					content.setScaleX(rightScale);
					content.setScaleY(rightScale);
				}else{
					content.setTranslationX(
							-mMenu.getMeasuredWidth() * v);
					content.setPivotX(content.getMeasuredWidth());
					content.setPivotY(content.getMeasuredHeight() / 2);
					content.invalidate();
					content.setScaleX(rightScale);
					content.setScaleY(rightScale);
				}
			}

			@Override
			public void onDrawerOpened(View view) {

			}

			@Override
			public void onDrawerClosed(View view) {
				mDrawerLayout.setDrawerLockMode(
						DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
			}

			@Override
			public void onDrawerStateChanged(int i) {

			}
		});
	}

	private void initView() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
				Gravity.RIGHT);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//		if(Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY())){
//
//		}

		Log.d(TAG,"onFling");
		if(e1.getX() - e2.getX() > 0){
			//show left
			openLeftMenu();
		}
		return false;
	}
}
