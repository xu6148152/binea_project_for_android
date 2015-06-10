package demo.binea.com.supportmaterialdesign;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import demo.binea.com.supportmaterialdesign.data.Cheeses;

/**
 * Created by xubinggui on 6/8/15.
 */
public class CheeseDetailActivity extends AppCompatActivity {
	private static final Uri URI = Uri.parse(
			"http://apod.nasa.gov/apod/image/1410/20141008tleBaldridge001h990.jpg");

	public static final String EXTRA_NAME = "cheese_name";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Fresco.initialize(this);
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
		final SimpleDraweeView imageView = (SimpleDraweeView) findViewById(R.id.backdrop);
//		Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(imageView);
//		final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
		Resources resources = getResources();
		final int resId = Cheeses.getRandomCheeseDrawable();
//		final Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
//				resources.getResourcePackageName(resId) + '/' +
//				resources.getResourceTypeName(resId) + '/' +
//				resources.getResourceEntryName(resId));
//		final Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + Cheeses.getRandomCheeseDrawable());
		Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(resId)).build();
		imageView.setImageURI(uri);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sample_actions, menu);
		return true;
	}
}
