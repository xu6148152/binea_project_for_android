package demo.binea.com.flyrefresh.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import demo.binea.com.flyrefresh.R;

/**
 * Created by xubinggui on 6/4/15.
 */
public class ItemViewHolder extends RecyclerView.ViewHolder {
	ImageView icon;
	TextView title;
	TextView subTitle;

	public ItemViewHolder(View itemView) {
		super(itemView);
		icon = (ImageView) itemView.findViewById(R.id.icon);
		title = (TextView) itemView.findViewById(R.id.title);
		subTitle = (TextView) itemView.findViewById(R.id.subtitle);
	}
}
