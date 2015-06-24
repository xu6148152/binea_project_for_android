package com.example.xubinggui.customviewtest.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.xubinggui.customviewtest.R;
import com.example.xubinggui.customviewtest.widget.CircleView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by xubinggui on 15/3/29.
 */
public class LightColorMatrixActivity extends ActionBarActivity {

	@InjectView(R.id.circle_view_after)
	CircleView circle_view_after;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.light_color_matrix_layout);

		ButterKnife.inject(this);
		circle_view_after.setLightColorFilter();
	}
}
