package demo.binea.com.dragtoplayout.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.greenrobot.event.EventBus;
import demo.binea.com.dragtoplayout.DataService;
import demo.binea.com.dragtoplayout.R;
import demo.binea.com.dragtoplayout.Util.AttachUtil;
import github.chenupt.multiplemodel.recycler.ModelRecyclerAdapter;

/**
 * Created by xubinggui on 5/23/15.
 */
public class RecyclerFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_recycler, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews();
	}

	private void initViews() {
		RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
		// init recycler view
		ModelRecyclerAdapter adapter = new ModelRecyclerAdapter(getActivity(), DataService.getInstance().getModelManager());
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(adapter);
		// set data source
		adapter.setList(DataService.getInstance().getList());
		adapter.notifyDataSetChanged();



		// attach top listener
		recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				EventBus.getDefault().post(AttachUtil.isRecyclerViewAttach(recyclerView));
			}
		});


	}

}
