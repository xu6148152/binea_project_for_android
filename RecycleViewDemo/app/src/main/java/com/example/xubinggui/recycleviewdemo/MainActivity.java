package com.example.xubinggui.recycleviewdemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setHasFixedSize(true);

		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(mLayoutManager);
		String[] dataSet = {"1","2","3","4"};
		MyAdapter mAdapter = new MyAdapter(dataSet);
		recyclerView.setAdapter(mAdapter);
	}

	public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
		private String[] mDataSet;

		public MyAdapter(String[] myDataSet){
			this.mDataSet = myDataSet;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
			View view = View.inflate(MainActivity.this,R.layout.recycle_view_item,null);
			ViewHolder vh = new ViewHolder(view);
			return vh;
		}

		@Override
		public void onBindViewHolder(ViewHolder viewHolder, int i) {
			viewHolder.mTextView.setText(mDataSet[i]);
		}

		@Override
		public int getItemCount() {
			return mDataSet.length;
		}

		public class ViewHolder extends RecyclerView.ViewHolder {
			// each data item is just a string in this case
			public TextView mTextView;
			public ViewHolder(View v) {
				super(v);
				mTextView = (TextView) v.findViewById(R.id.tv_text);
			}
		}
	}

}
