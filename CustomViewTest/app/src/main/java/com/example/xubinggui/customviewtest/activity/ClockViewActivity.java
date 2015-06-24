package com.example.xubinggui.customviewtest.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.LinearLayout;

import com.example.xubinggui.customviewtest.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by xubinggui on 15/3/29.
 */
public class ClockViewActivity extends ActionBarActivity {

	@InjectView(R.id.ll_root)
	LinearLayout ll_root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.clock_view_layout);
		ButterKnife.inject(this);
		ll_root.setBackgroundColor(Color.argb(255, 255, 128, 103));
	}
}
