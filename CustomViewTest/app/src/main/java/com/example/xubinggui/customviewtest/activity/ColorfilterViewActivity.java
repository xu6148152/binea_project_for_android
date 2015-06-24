package com.example.xubinggui.customviewtest.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.xubinggui.customviewtest.R;
import com.example.xubinggui.customviewtest.widget.CircleView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by xubinggui on 15/3/29.
 */
public class ColorfilterViewActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {

	@InjectView(R.id.circle_view)
	CircleView circle_view;
	@InjectView(R.id.sb_red)
	SeekBar sb_red;
	@InjectView(R.id.sb_green)
	SeekBar sb_green;
	@InjectView(R.id.sb_blue)
	SeekBar sb_blue;
	@InjectView(R.id.sb_alpha)
	SeekBar sb_alpha;
	@InjectView(R.id.iv_image)
	ImageView iv_image;

	private Canvas mCanvas;
	private Paint mPaint;

	private Bitmap baseBitmap;
	private Bitmap afterBitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.circle_view_layout);

		ButterKnife.inject(this);
		sb_red.setOnSeekBarChangeListener(this);
		sb_green.setOnSeekBarChangeListener(this);
		sb_blue.setOnSeekBarChangeListener(this);
		sb_alpha.setOnSeekBarChangeListener(this);

		baseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aa);
		afterBitmap = Bitmap.createBitmap(baseBitmap.getWidth(), baseBitmap.getHeight(), baseBitmap.getConfig());

		mCanvas = new Canvas(afterBitmap);
		mPaint = new Paint();
		reset();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		final float redValue = sb_red.getProgress()/128f;
		final float greenValue = sb_green.getProgress()/128f;
		final float blueValue = sb_blue.getProgress()/128f;
		final float alphaValue = sb_alpha.getProgress()/128f;

		updateResColor(redValue, greenValue, blueValue, alphaValue);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	private void updateResColor(float redValue, float greenValue, float blueValue, float alphaValue){
		ColorMatrix matrix = new ColorMatrix(new float[]{
				redValue, 0, 0, 0, 0,
				0, greenValue, 0, 0, 0,
				0, 0, blueValue, 0, 0,
				0, 0, 0, alphaValue, 0
		});
		mPaint.setColorFilter(new ColorMatrixColorFilter(matrix));
		mCanvas.drawBitmap(baseBitmap, new Matrix(), mPaint);
		iv_image.setImageBitmap(afterBitmap);
		circle_view.setColorMatrix(new float[]{
				redValue, 0, 0, 0, 0,
				0, greenValue, 0, 0, 0,
				0, 0, blueValue, 0, 0,
				0, 0, 0, alphaValue, 0
		});
	}

	@OnClick(R.id.btn_reset)
	public void reset(){
		updateResColor(1,1,1,1);
		sb_red.setProgress(128);
		sb_green.setProgress(128);
		sb_blue.setProgress(128);
		sb_alpha.setProgress(128);
	}
}
