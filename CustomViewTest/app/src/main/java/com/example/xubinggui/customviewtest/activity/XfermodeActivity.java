package com.example.xubinggui.customviewtest.activity;

import android.graphics.AvoidXfermode;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.xubinggui.customviewtest.R;
import com.example.xubinggui.customviewtest.widget.CustomBitmapView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by xubinggui on 15/3/29.
 */
public class XfermodeActivity extends ActionBarActivity {

	@InjectView(R.id.avoid_mode_target)
	CustomBitmapView avoid_mode_target;
	@InjectView(R.id.avoid_mode_avoid)
	CustomBitmapView avoid_mode_avoid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xfermode_layout);
		ButterKnife.inject(this);

		avoid_mode_target.setAvoidXfermode(AvoidXfermode.Mode.TARGET);
		avoid_mode_avoid.setAvoidXfermode(AvoidXfermode.Mode.AVOID);
	}
}
