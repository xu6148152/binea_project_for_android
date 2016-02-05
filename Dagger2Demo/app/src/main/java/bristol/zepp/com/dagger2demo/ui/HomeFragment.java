package bristol.zepp.com.dagger2demo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import javax.inject.Inject;

/**
 * Created by xubinggui on 2/5/16.
 */
public class HomeFragment extends Fragment {

    @Inject ActivityTitleController titleController;

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).component().inject(this);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        TextView tv = new TextView(getActivity());
        tv.setGravity(Gravity.CENTER);
        tv.setText("Hello, World");
        return tv;
    }

    @Override public void onResume() {
        super.onResume();
        titleController.setTitle("Home Fragment");
    }
}
