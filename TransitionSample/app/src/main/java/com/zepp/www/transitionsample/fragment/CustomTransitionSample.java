package com.zepp.www.transitionsample.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionValues;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.zepp.www.transitionsample.R;
import com.zepp.www.transitionsample.library.util.IntProperty;

//  Created by xubinggui on 18/12/2016.
//                            _ooOoo_  
//                           o8888888o  
//                           88" . "88  
//                           (| -_- |)  
//                            O\ = /O  
//                        ____/`---'\____  
//                      .   ' \\| |// `.  
//                       / \\||| : |||// \  
//                     / _||||| -:- |||||- \  
//                       | | \\\ - /// | |  
//                     | \_| ''\---/'' | |  
//                      \ .-\__ `-` ___/-. /  
//                   ___`. .' /--.--\ `. . __  
//                ."" '< `.___\_<|>_/___.' >'"".  
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
//                 \ \ `-. \_ __\ /__ _/ .-` / /  
//         ======`-.____`-.___\_____/___.-`____.-'======  
//                            `=---='  
//  
//         .............................................  
//                  佛祖镇楼                  BUG辟易 

public class CustomTransitionSample extends Fragment {
    private ViewGroup mTransitionsContainer;
    private ProgressBar mProgressBar;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, container, false);

        mTransitionsContainer = (ViewGroup) view.findViewById(R.id.transitions_container);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setProgress(mProgressBar.getProgress() - 20);
            }
        });
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setProgress(mProgressBar.getProgress() + 30);
            }
        });

        return view;
    }

    private void setProgress(int value) {
        TransitionManager.beginDelayedTransition(mTransitionsContainer, new ProgressTransition());
        value = Math.max(0, Math.min(100, value));
        mProgressBar.setProgress(value);
    }

    private static class ProgressTransition extends Transition {

        /**
         * Property is like a helper that contain setter and getter in one place
         */
        private static final Property<ProgressBar, Integer> PROGRESS_PROPERTY = new IntProperty<ProgressBar>() {

            @Override public void setValue(ProgressBar progressBar, int value) {
                progressBar.setProgress(value);
            }

            @Override public Integer get(ProgressBar progressBar) {
                return progressBar.getProgress();
            }
        }.optimize();

        /**
         * Internal name of property. Like a bundles for intent
         */
        private static final String PROPNAME_PROGRESS = "ProgressTransition:progress";

        @Override public void captureStartValues(TransitionValues transitionValues) {
            captureValues(transitionValues);
        }

        @Override public void captureEndValues(TransitionValues transitionValues) {
            captureValues(transitionValues);
        }

        private void captureValues(TransitionValues transitionValues) {
            if (transitionValues.view instanceof ProgressBar) {
                // save current progress in the values map
                ProgressBar progressBar = ((ProgressBar) transitionValues.view);
                transitionValues.values.put(PROPNAME_PROGRESS, progressBar.getProgress());
            }
        }

        @Override public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
            if (startValues != null && endValues != null && endValues.view instanceof ProgressBar) {
                ProgressBar progressBar = (ProgressBar) endValues.view;
                int start = (Integer) startValues.values.get(PROPNAME_PROGRESS);
                int end = (Integer) endValues.values.get(PROPNAME_PROGRESS);
                if (start != end) {
                    // first of all we need to return the start value, because right now
                    // the view is have the end value
                    progressBar.setProgress(start);
                    // create animator with our progressBar, property and end value
                    return ObjectAnimator.ofInt(progressBar, PROGRESS_PROPERTY, end);
                }
            }
            return null;
        }
    }
}
