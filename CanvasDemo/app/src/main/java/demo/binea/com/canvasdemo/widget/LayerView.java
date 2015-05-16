package demo.binea.com.canvasdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import demo.binea.com.canvasdemo.R;

/**
 * Created by xubinggui on 15/1/24.
 */
public class LayerView extends View {
	private final String TAG = LayerView.class.getSimpleName();
	private Paint mPaint;
	private int mViewWidth;
	private int mViewHeight;
	private Bitmap mBitmap;

	public LayerView(Context context) {
		this(context, null);
	}

	public LayerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LayerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
//		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		Log.d(TAG,"width " + w + " height " + h);
		mViewWidth = w;
		mViewHeight = h;

		mBitmap = Bitmap.createScaledBitmap(mBitmap, mViewWidth, mViewHeight, true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//save layer
//		saveLayer(canvas);

//		canvasScale(canvas);

//		skewCanvas(canvas);

		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		Matrix matrix = new Matrix();
		matrix.setScale(0.8F, 0.35F);
		matrix.postTranslate(100, 100);
		canvas.setMatrix(matrix);
		canvas.drawBitmap(mBitmap, 0, 0, null);
		canvas.restore();
	}

	/**
	 * 错切
	 * @param canvas
	 */
	private void skewCanvas(Canvas canvas) {
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.skew(0.5F, 0F);
		canvas.drawBitmap(mBitmap, 0, 0, null);
		canvas.restore();
	}

	private void canvasScale(Canvas canvas) {
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(0.8F, 0.35F, mViewWidth, 0);
		canvas.drawBitmap(mBitmap, 0, 0, null);
		canvas.restore();
	}

	private void saveLayer(Canvas canvas) {
		mPaint.setColor(Color.RED);

		canvas.drawRect(mViewWidth / 2F - 200, mViewHeight / 2F - 200, mViewWidth / 2F + 200, mViewHeight / 2F + 200, mPaint);

//		canvas.save();

//		canvas.saveLayer(mViewWidth / 2F - 100, mViewHeight / 2F - 100, mViewWidth / 2F + 100, mViewHeight / 2F + 100, null, Canvas.ALL_SAVE_FLAG);
//		canvas.saveLayerAlpha(mViewWidth / 2F - 100, mViewHeight / 2F - 100, mViewWidth / 2F + 100, mViewHeight / 2F + 100, 0x55, Canvas.ALL_SAVE_FLAG);

		int saveID1 = canvas.save(Canvas.CLIP_SAVE_FLAG);
		canvas.clipRect(mViewWidth / 2F - 200, mViewHeight / 2F - 200, mViewWidth / 2F + 200, mViewHeight / 2F + 200);
		canvas.drawColor(Color.GREEN);

		int saveID2 = canvas.save(Canvas.MATRIX_SAVE_FLAG);

		canvas.rotate(5);
		mPaint.setColor(Color.BLUE);
		canvas.drawRect(mViewWidth / 2F - 100, mViewHeight / 2F - 100, mViewWidth / 2F + 100, mViewHeight / 2F + 100, mPaint);

		canvas.restoreToCount(saveID2);
	}
}
