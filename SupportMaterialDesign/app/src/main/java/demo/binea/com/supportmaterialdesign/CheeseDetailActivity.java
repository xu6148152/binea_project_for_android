package demo.binea.com.supportmaterialdesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import demo.binea.com.supportmaterialdesign.data.Cheeses;

/**
 * Created by xubinggui on 6/8/15.
 */
public class CheeseDetailActivity extends AppCompatActivity {

	public static final String EXTRA_NAME = "cheese_name";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		Intent intent = getIntent();
		final String cheeseName = intent.getStringExtra(EXTRA_NAME);

		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		final ActionBar actionBar = getSupportActionBar();
		if(null != actionBar) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		CollapsingToolbarLayout collapsingToolbar =
				(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		collapsingToolbar.setTitle(cheeseName);

		loadBackdrop();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void loadBackdrop() {
		final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
		Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(imageView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sample_actions, menu);
		return true;
	}
}
