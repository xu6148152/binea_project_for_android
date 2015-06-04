package demo.binea.com.flyrefresh.adapter;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import demo.binea.com.flyrefresh.R;
import demo.binea.com.flyrefresh.model.ItemData;

/**
 * Created by xubinggui on 6/4/15.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

	private LayoutInflater mInflater;
	private DateFormat dateFormat;
	private ArrayList<ItemData> mDataSet = new ArrayList<>();

	public ItemAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		dateFormat = SimpleDateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH);
	}

	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view = mInflater.inflate(R.layout.view_list_item, viewGroup, false);
		return new ItemViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
		final ItemData data = mDataSet.get(i);
		ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
		drawable.getPaint().setColor(data.color);
		itemViewHolder.icon.setBackground(drawable);
		itemViewHolder.icon.setImageResource(data.icon);
		itemViewHolder.title.setText(data.title);
		itemViewHolder.subTitle.setText(dateFormat.format(data.time));
	}

	@Override
	public int getItemCount() {
		return mDataSet.size();
	}
}

