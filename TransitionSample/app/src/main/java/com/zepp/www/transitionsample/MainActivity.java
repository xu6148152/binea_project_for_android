package com.zepp.www.transitionsample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListFragment listFragment = new SimpleListFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
    }

    public static class SimpleListFragment extends ListFragment {

        private String[] numbers_text = {
                "Simple animations with AutoTransition", "Interpolator, duration, start delay", "Path motion", "Slide transition", "Scale transition",
                "Explode transition and epicenter", "Transition names", "ChangeImageTransform transition", "Recolor transition", "Rotate transition",
                "Change text transition", "Custom transition", "Scene to scene transitions"
        };

        @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_list_item_1, numbers_text);
            setListAdapter(adapter);
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override public void onListItemClick(ListView l, View v, int position, long id) {
            Fragment fragment = createFragmentForPosition(position);
            if (fragment != null) {
                getActivity().getSupportFragmentManager()
                             .beginTransaction()
                             .replace(R.id.container, fragment)
                             .addToBackStack(String.valueOf(position))
                             .commit();
            }
        }

        private Fragment createFragmentForPosition(int index) {
            switch (index) {
                case 0:
                    return new AutoTransitionSample();
                case 1:
                    return new InterpolatorDurationStartDelaySample();
                //case 2:
                //    return new PathMotionSample();
                //case 3:
                //    return new SlideSample();
                //case 4:
                //    return new ScaleSample();
                //case 5:
                //    return new ExplodeAndEpicenterExample();
                //case 6:
                //    return new TransitionNameSample();
                //case 7:
                //    return new ImageTransformSample();
                //case 8:
                //    return new RecolorSample();
                //case 9:
                //    return new RotateSample();
                case 10:
                    return new ChangeTextSample();
                //case 11:
                //    return new CustomTransitionSample();
                //case 12:
                //    return new ScenesSample();
            }
            return null;
        }
    }
}
