package com.example.xubinggui.shadertest;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by xubinggui on 15/1/3.
 */
public class MyTextView extends TextView {
	private boolean topDown = false;
	public MyTextView(Context context) {
		this(context, null);
	}

	public MyTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		TextPaint textPaint = getPaint();
//		textPaint.setColor(getCurrentTextColor());
//		textPaint.drawableState = getDrawableState();
//
//		canvas.save();
//
//		if(topDown){
//			canvas.translate(getWidth(), 0);
			canvas.rotate(90,getWidth()/2,getHeight()/2);
//		}else {
//			canvas.translate(0, getHeight());
//			canvas.rotate(-90);
//		}


//		canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());

		getLayout().draw(canvas);
//		canvas.restore();
	}
}
