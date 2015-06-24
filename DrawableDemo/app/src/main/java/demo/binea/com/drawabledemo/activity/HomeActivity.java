package demo.binea.com.drawabledemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;

import demo.binea.com.drawabledemo.R;
import demo.binea.com.drawabledemo.widget.CircleImageDrawable;
import demo.binea.com.drawabledemo.widget.RoundImageDrawable;

/**
 * Created by xubinggui on 15/3/7.
 */
public class HomeActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_02);

		ImageView iv_01 = (ImageView) findViewById(R.id.iv_01);
		iv_01.setImageDrawable(new RoundImageDrawable(bitmap));

		ImageView iv_02 = (ImageView) findViewById(R.id.iv_02);
		iv_02.setImageDrawable(new CircleImageDrawable(bitmap));

	}

	public void onDrawableState(View view) {
		startActivity(new Intent(this, DrawableStateListActivity.class));
	}
}
