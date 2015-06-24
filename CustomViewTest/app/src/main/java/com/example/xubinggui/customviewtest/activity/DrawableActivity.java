package com.example.xubinggui.customviewtest.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

import com.example.xubinggui.customviewtest.R;
import com.example.xubinggui.customviewtest.widget.CircleImageDrawable;
import com.example.xubinggui.customviewtest.widget.RoundImageDrawable;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DrawableActivity extends ActionBarActivity {

	@InjectView(R.id.circle_drawable)
	ImageView circle_drawable;
	@InjectView(R.id.round_drawable)
	ImageView round_drawble;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawable);
		ButterKnife.inject(this);

		CircleImageDrawable circleImageDrawable = new CircleImageDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.aa));
		circle_drawable.setImageDrawable(circleImageDrawable);

		RoundImageDrawable roundImageDrawable = new RoundImageDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.aa));
		round_drawble.setImageDrawable(roundImageDrawable);
	}
}
