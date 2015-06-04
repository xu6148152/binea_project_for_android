package demo.binea.com.flyrefresh;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;
import java.util.Date;

import demo.binea.com.flyrefresh.adapter.ItemAdapter;
import demo.binea.com.flyrefresh.model.ItemData;
import demo.binea.com.flyrefresh.widget.FlyRefreshLayout;
import demo.binea.com.flyrefresh.widget.listener.PullRefreshListener;

public class MainActivity extends AppCompatActivity implements PullRefreshListener {

	private ArrayList<ItemData> mDataSet = new ArrayList<>();

	private FlyRefreshLayout mFlylayout;

	private RecyclerView mListView;

	private LinearLayoutManager mLayoutManager;

	private ItemAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initDataSet();
		setContentView(R.layout.activity_main);

//		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//		setSupportActionBar(toolbar);
//		getSupportActionBar().setDisplayShowTitleEnabled(false);

		mFlylayout = (FlyRefreshLayout) findViewById(R.id.fly_layout);

		mFlylayout.setPullRefreshListener(this);

		mListView = (RecyclerView) findViewById(R.id.list);

		mLayoutManager = new LinearLayoutManager(this);
		mListView.setLayoutManager(mLayoutManager);
		mAdapter = new ItemAdapter(this);

		mListView.setAdapter(mAdapter);
	}

	private void initDataSet() {
		mDataSet.add(new ItemData(Color.parseColor("#76A9FC"), R.mipmap.ic_assessment_white_24dp, "Meeting Minutes", new Date(2014 - 1900, 2, 9)));
		mDataSet.add(new ItemData(Color.GRAY, R.mipmap.ic_folder_white_24dp, "Favorites Photos", new Date(2014 - 1900, 1, 3)));
		mDataSet.add(new ItemData(Color.GRAY, R.mipmap.ic_folder_white_24dp, "Photos", new Date(2014 - 1900, 0, 9)));
	}

	@Override
	public void onRefresh(FlyRefreshLayout view) {
		View child = mListView.getChildAt(0);
		if (child != null) {
			bounceAnimateView(child.findViewById(R.id.icon));
		}

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mFlylayout.onRefreshFinish();
			}
		}, 2000);
	}

	private void bounceAnimateView(View view) {
		if (view == null) {
			return;
		}

		Animator swing = ObjectAnimator.ofFloat(view, "rotationX", 0, 30, -20, 0);
		swing.setDuration(400);
		swing.setInterpolator(new AccelerateInterpolator());
		swing.start();
	}

	@Override
	public void onRefreshAnimationEnd(FlyRefreshLayout view) {
		addItemData();
	}

	private void addItemData() {
		ItemData itemData = new ItemData(Color.parseColor("#FFC970"), R.mipmap.ic_smartphone_white_24dp, "Magic Cube Show", new Date());
		mDataSet.add(0, itemData);
		mAdapter.notifyItemInserted(0);
		mLayoutManager.scrollToPosition(0);
	}
}
