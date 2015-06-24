package demo.binea.com.drawabledemo.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import demo.binea.com.drawabledemo.R;
import demo.binea.com.drawabledemo.widget.MessageListItem;

/**
 * Created by xubinggui on 15/3/7.
 */
public class DrawableStateListActivity extends ListActivity {

	private Message[] messages = new Message[] {
			new Message("Gas bill overdue", true),
			new Message("Congratulations, you've won!", true),
			new Message("I love you!", false),
			new Message("Please reply!", false),
			new Message("You ignoring me?", false),
			new Message("Not heard from you", false),
			new Message("Electricity bill", true),
			new Message("Gas bill", true), new Message("Holiday plans", false),
			new Message("Marketing stuff", false), };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getListView().setAdapter(new ArrayAdapter<Message>(this, -1, messages)
		{
			private LayoutInflater mInflater = LayoutInflater
					.from(getContext());

			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				if (convertView == null)
				{
					convertView = mInflater.inflate(R.layout.drawable_state_layout,
							parent, false);
				}
				MessageListItem messageListItem = (MessageListItem) convertView;
				TextView tv = (TextView) convertView
						.findViewById(R.id.id_msg_item_text);
				tv.setText(getItem(position).message);
				messageListItem.setMessageReaded(getItem(position).readed);
				return convertView;
			}

		});
	}

	private class Message{
		public String message;
		public boolean readed;
		public Message(String msg, boolean isRead){
			this.message = msg;
			this.readed = isRead;
		}
	}
}
