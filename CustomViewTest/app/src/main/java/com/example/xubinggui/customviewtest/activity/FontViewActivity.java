package com.example.xubinggui.customviewtest.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.xubinggui.customviewtest.R;

import butterknife.ButterKnife;

/**
 * Created by xubinggui on 15/4/4.
 */
public class FontViewActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.font_view_layout);

		ButterKnife.inject(this);

	}
}
