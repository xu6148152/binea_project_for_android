package demo.binea.com.turnpageview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xubinggui on 15/1/24.
 */
public class TurnPageView extends View {
	private List<Bitmap> mBitmaps;// 位图数据列表
	private TextPaint mTextPaint;
	private int mViewWidth;
	private int mViewHeight;
	private int mClipX;

	private float mTextSizeLarger;
	private float mTextSizeNormal;

	private float autoAreaLeft;
	private float autoAreaRight;

	private boolean isLastPage = false;

	private long pageIndex = 0;

	private boolean isNextPage = false;

	private float mCurPointX = 0;

	private float mMoveValid = 0;

	public TurnPageView(Context context) {
		this(context, null);
	}

	public TurnPageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TurnPageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		mTextSizeLarger = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, context.getResources().getDisplayMetrics());

		mTextSizeNormal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, context.getResources().getDisplayMetrics());
		;

		mTextPaint = new TextPaint();
	}

	/**
	 * 设置位图数据
	 *
	 * @param mBitmaps 位图数据列表
	 */
	public synchronized void setBitmaps(List<Bitmap> mBitmaps) {

		if (null == mBitmaps || mBitmaps.size() == 0)
			throw new IllegalArgumentException("no bitmap to display");

		if (mBitmaps.size() < 2)
			throw new IllegalArgumentException("fuck you and fuck to use imageview");

		this.mBitmaps = mBitmaps;
		invalidate();
	}

	/**
	 * 默认显示
	 *
	 * @param canvas Canvas对象
	 */
	private void defaultDisplay(Canvas canvas) {
		// 绘制底色
		canvas.drawColor(Color.WHITE);

		// 绘制标题文本
		mTextPaint.setTextSize(mTextSizeLarger);
		mTextPaint.setColor(Color.RED);
		canvas.drawText("FBI WARNING", mViewWidth / 2, mViewHeight / 4, mTextPaint);

		// 绘制提示文本
		mTextPaint.setTextSize(mTextSizeNormal);
		mTextPaint.setColor(Color.BLACK);
		canvas.drawText("Please set data use setBitmaps method", mViewWidth / 2, mViewHeight / 3, mTextPaint);
	}

	/**
	 * 初始化位图数据
	 * 缩放位图尺寸与屏幕匹配
	 */
	private void initBitmaps() {
		if (mBitmaps == null) {
			return;
		}
		List<Bitmap> temp = new ArrayList<Bitmap>();
		for (int i = 0; i < mBitmaps.size(); i++) {
			Bitmap bitmap = Bitmap.createScaledBitmap(mBitmaps.get(i), mViewWidth, mViewHeight, true);
			temp.add(bitmap);
		}
		mBitmaps = temp;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mViewWidth = w;
		mViewHeight = h;

		mClipX = w;

		// 计算控件左侧和右侧自动吸附的区域
		autoAreaLeft = mViewWidth * 1 / 5F;
		autoAreaRight = mViewWidth * 4 / 5F;

		mMoveValid = mViewWidth * 1 / 100F;

	}

	/**
	 * 绘制位图
	 *
	 * @param canvas Canvas对象
	 */
	private void drawBitmaps(Canvas canvas) {
		// 绘制位图前重置isLastPage为false
		isLastPage = false;

		// 限制pageIndex的值范围
		pageIndex = pageIndex < 0 ? 0 : pageIndex;
		pageIndex = pageIndex > mBitmaps.size() ? mBitmaps.size() : pageIndex;

		// 计算数据起始位置
		long start = mBitmaps.size() - 2 - pageIndex;
		long end = mBitmaps.size() - pageIndex;

    /*
     * 如果数据起点位置小于0则表示当前已经到了最后一张图片
     */
		if (start < 0) {
			// 此时设置isLastPage为true
			isLastPage = true;

			// 并显示提示信息
			showToast("This is fucking lastest page");

			// 强制重置起始位置
			start = 0;
			end = 1;
		}

		for (long i = start; i < end; i++) {
			canvas.save();

        /*
         * 仅裁剪位于最顶层的画布区域
         * 如果到了末页则不在执行裁剪
         */
			if (!isLastPage && i == end - 1) {
				canvas.clipRect(0, 0, mClipX, mViewHeight);
			}
			canvas.drawBitmap(mBitmaps.get((int)i), 0, 0, null);

			canvas.restore();
		}
	}

	private void showToast(String s) {
		Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// 每次触发TouchEvent重置isNextPage为true
		isNextPage = true;

	    /*
	     * 判断当前事件类型
	     */
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:// 触摸屏幕时
				// 获取当前事件点x坐标
				mCurPointX = event.getX();

		        /*
		         * 如果事件点位于回滚区域
		         */
				if (mCurPointX < autoAreaLeft) {
					// 那就不翻下一页了而是上一页
					isNextPage = false;
					pageIndex--;
					mClipX = (int) mCurPointX;
					invalidate();
				}
				break;
			case MotionEvent.ACTION_MOVE:// 滑动时
				float SlideDis = mCurPointX - event.getX();
				if (Math.abs(SlideDis) > mMoveValid) {
					// 获取触摸点的x坐标
					mClipX = (int) event.getX();

					invalidate();
				}
				break;
			case MotionEvent.ACTION_UP:// 触点抬起时
				// 判断是否需要自动滑动
				judgeSlideAuto();

		        /*
		         * 如果当前页不是最后一页
		         * 如果是需要翻下一页
		         * 并且上一页已被clip掉
		         */
				if (!isLastPage && isNextPage && mClipX <= 0) {
					pageIndex++;
					mClipX = mViewWidth;
					invalidate();
				}
				break;
		}
		return true;
	}

	private void judgeSlideAuto() {
	/*
     * 如果裁剪的右端点坐标在控件左端五分之一的区域内，那么我们直接让其自动滑到控件左端
     */
		if (mClipX < autoAreaLeft) {
			while (mClipX > 0) {
				mClipX--;
				invalidate();
			}
		}
	/*
     * 如果裁剪的右端点坐标在控件右端五分之一的区域内，那么我们直接让其自动滑到控件右端
     */
		if (mClipX > autoAreaRight) {
			while (mClipX < mViewWidth) {
				mClipX++;
				invalidate();
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		initBitmaps();

		if (mBitmaps == null || mBitmaps.size() < 1) {
			defaultDisplay(canvas);
		} else {

			drawBitmaps(canvas);
		}
	}
}
