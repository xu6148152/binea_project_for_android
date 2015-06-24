package com.example.xubinggui.customviewtest.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.xubinggui.customviewtest.R;
import com.example.xubinggui.customviewtest.widget.CircleView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by xubinggui on 15/3/29.
 */
public class PorterDuffColorFilterActivity extends ActionBarActivity {

	@InjectView(R.id.port_duff_color_view)
	CircleView port_duff_color_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.porter_duff_color_filter_layout);
		ButterKnife.inject(this);
		port_duff_color_view.setPorterDuffColorFilter(Color.GREEN);
	}
}
