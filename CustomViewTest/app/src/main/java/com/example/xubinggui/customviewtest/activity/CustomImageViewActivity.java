package com.example.xubinggui.customviewtest.activity;

import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.xubinggui.customviewtest.R;
import com.example.xubinggui.customviewtest.widget.CustomImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CustomImageViewActivity extends ActionBarActivity {

	@InjectView(R.id.imave_view)
	CustomImageView image_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_image_view);
		ButterKnife.inject(this);
		image_view.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.aa));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_custom_image_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
