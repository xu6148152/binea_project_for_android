package demo.binea.com.flyrefresh;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;

import demo.binea.com.flyrefresh.model.ItemData;

public class MainActivity extends AppCompatActivity {

	private ArrayList<ItemData> mDataSet = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initDataSet();
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

//		mFlylayout = (FlyRefreshLayout) findViewById(R.id.fly_layout);
//
//		mFlylayout.setOnPullRefreshListener(this);
//
//		mListView = (RecyclerView) findViewById(R.id.list);
//
//		mLayoutManager = new LinearLayoutManager(this);
//		mListView.setLayoutManager(mLayoutManager);
//		mAdapter = new ItemAdapter(this);
//
//		mListView.setAdapter(mAdapter);
	}

	private void initDataSet() {
		mDataSet.add(new ItemData(Color.parseColor("#76A9FC"), R.mipmap.ic_assessment_white_24dp, "Meeting Minutes", new Date(2014 - 1900, 2, 9)));
		mDataSet.add(new ItemData(Color.GRAY, R.mipmap.ic_folder_white_24dp, "Favorites Photos", new Date(2014 - 1900, 1, 3)));
		mDataSet.add(new ItemData(Color.GRAY, R.mipmap.ic_folder_white_24dp, "Photos", new Date(2014 - 1900, 0, 9)));
	}
}
