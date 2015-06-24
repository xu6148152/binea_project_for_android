package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import com.example.xubinggui.customviewtest.R;
import com.example.xubinggui.customviewtest.util.Utils;

/**
 * Created by xubinggui on 15/3/29.
 */
public class CustomBitmapView extends View {
	private Bitmap mBitmap;
	private Paint mPaint;
	private Context mContext;
	private Xfermode avoidXfermode;
	private float x;
	private float y;
	private float w;
	private float h;

	public CustomBitmapView(Context context) {
		this(context, null);
	}

	public CustomBitmapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initPaint();
		initRes();
	}

	private void initPaint() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	private void initRes() {
		mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.aa);

		x = Utils.getScreenWidth(mContext) / 2;
		y = Utils.getScreenHeight(mContext) / 2;
		w = Utils.getScreenWidth(mContext) / 2 + mBitmap.getWidth() / 2;
		h = Utils.getScreenHeight(mContext) / 2 + mBitmap.getHeight() / 2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, x, y, mPaint);

		mPaint.setARGB(255, 211, 53, 243);
		mPaint.setXfermode(avoidXfermode);
		canvas.drawRect(x, y, w, h, mPaint);

	}

	public void setAvoidXfermode(AvoidXfermode.Mode mode){
		avoidXfermode = new AvoidXfermode(0XFFFFFFFF, 0, mode);
	}
}
