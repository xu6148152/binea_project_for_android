package demo.binea.com.bezierindicator.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import demo.binea.com.bezierindicator.R;

/**
 * Created by xubinggui on 5/21/15.
 */
public class GuideFragment extends Fragment {
	private int bgRes;
	private ImageView imageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bgRes = getArguments().getInt("data");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_guide, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		imageView = (ImageView) view.findViewById(R.id.image);
		imageView.setBackgroundResource(bgRes);
	}
}
