package demo.binea.com.turnpageview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import demo.binea.com.turnpageview.R;

/**
 * Created by xubinggui on 15/1/25.
 */
public class IconView extends View {
	private Bitmap mBitmap;// 位图
	private TextPaint mPaint;// 绘制文本的画笔
	private String mStr;// 绘制的文本
	private float mTextSize;// 画笔的文本尺寸

	private int screenW;
	private int screenH;

	private enum Ratio {
		WIDTH, HEIGHT
	}

	public IconView(Context context) {
		this(context, null);
	}

	public IconView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// 计算参数
		calArgs(context);

		// 初始化
		init();
	}

	private void init() {
		if (null == mBitmap) {
			mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.nick1);
		}

		if (null == mStr || mStr.trim().length() == 0) {
			mStr = "Binea";
		}

		mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
		mPaint.setColor(Color.LTGRAY);
		mPaint.setTextSize(mTextSize);
		mPaint.setTextAlign(Paint.Align.CENTER);
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);

	}

	private void calArgs(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Point point = new Point();
		wm.getDefaultDisplay().getSize(point);

		screenW = point.x;
		screenH = point.y;

		mTextSize = screenH / 10f;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getMeasureSize(widthMeasureSpec, Ratio.WIDTH), getMeasureSize(heightMeasureSpec, Ratio.HEIGHT));
	}

	private int getMeasureSize(int measureSpec, Ratio ratio) {
		int result = 0;

		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);

		switch (mode) {
			case MeasureSpec.EXACTLY:
				result = size;
				break;
			default:
				if (ratio == Ratio.WIDTH) {
					float textWidth = mPaint.measureText(mStr);
					result = ((int) (textWidth >= mBitmap.getWidth() ? textWidth : mBitmap.getWidth())) + getPaddingLeft() + getPaddingRight();
				} else if (ratio == Ratio.HEIGHT) {
					result = ((int) ((mPaint.descent() - mPaint.ascent()) * 2 + mBitmap.getHeight())) + getPaddingTop() + getPaddingBottom();
				}
				if (mode == MeasureSpec.AT_MOST) {
					result = Math.min(result, size);
				}
				break;
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, getWidth() / 2 - mBitmap.getWidth() / 2, getHeight() / 2 - mBitmap.getHeight() / 2, null);
		canvas.drawText(mStr, getWidth() / 2, mBitmap.getHeight() + getHeight() / 2 - mBitmap.getHeight() / 2 - mPaint.ascent(), mPaint);
	}
}
