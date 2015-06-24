package com.example.xubinggui.customviewtest.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.example.xubinggui.customviewtest.R;
import com.example.xubinggui.customviewtest.util.Utils;

/**
 * Created by xubinggui on 15/4/5.
 */
public class BitmapMeshView extends View {
	private static final int WIDTH = 19;
	private static final int HEIGHT = 19;
	private static final int COUNT = (WIDTH + 1) * (HEIGHT + 1);

	private Bitmap mBitmap;
	private float[] verts;

	private Context mContext;

	private int mScreenX;
	private int mScreenY;

	public BitmapMeshView(Context context) {
		this(context, null);
	}

	public BitmapMeshView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.mContext = context;

		init();
	}

	private void init() {
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aa);

		verts = new float[COUNT * 2];

		mScreenX = (int) Utils.getScreenWidth(mContext);
		mScreenY = (int) Utils.getScreenHeight(mContext);

//		mBitmap = Bitmap.createScaledBitmap(mBitmap, mScreenX, mScreenY, false);

//		int index = 0;
//		float multiple = mBitmap.getWidth();
//		for (int y = 0; y <= HEIGHT; y++) {
//			float fy = mBitmap.getHeight() * y / HEIGHT;
//			for (int x = 0; x <= WIDTH; x++) {
//				float fx = mBitmap.getWidth() * x / WIDTH + ((HEIGHT - y) * 1.0F / HEIGHT * multiple);
//				setXY(fx, fy, index);
//				index += 1;
//			}
//
//		}

		int index = 0;
		float multipleY = mBitmap.getHeight() / HEIGHT;
		float multipleX = mBitmap.getWidth() / WIDTH;
		for (int y = 0; y <= HEIGHT; y++) {
			float fy = multipleY * y;
			for (int x = 0; x <= WIDTH; x++) {
				float fx = multipleX * x;

				setXY(fx, fy, index);

				if (5 == y) {
					if (8 == x) {
						setXY(fx - multipleX, fy - multipleY, index);
					}
					if (9 == x) {
						setXY(fx + multipleX, fy - multipleY, index);
					}
				}
				if (6 == y) {
					if (8 == x) {
						setXY(fx - multipleX, fy + multipleY, index);
					}
					if (9 == x) {
						setXY(fx + multipleX, fy + multipleY, index);
					}
				}

				index += 1;
			}
		}
	}

	private void setXY(float fx, float fy, int index) {
		verts[index * 2] = fx;
		verts[index * 2 + 1] = fy;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
	}
}


