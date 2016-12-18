package com.zepp.www.transitionsample.fragment;

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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zepp.www.transitionsample.R;
import com.zepp.www.transitionsample.VisibleToggleClickListener;
import com.zepp.www.transitionsample.library.Scale;

public class ScaleSample extends Fragment {
    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scale, container, false);

        final ViewGroup transitionsContainer = (ViewGroup) view.findViewById(R.id.transitions_container);
        final TextView text1 = (TextView) transitionsContainer.findViewById(R.id.text1);

        transitionsContainer.findViewById(R.id.button1).setOnClickListener(new VisibleToggleClickListener() {

            @Override protected void changeVisibility(boolean visible) {
                TransitionManager.beginDelayedTransition(transitionsContainer, new Scale());
                text1.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
            }
        });

        final TextView text2 = (TextView) transitionsContainer.findViewById(R.id.text2);

        transitionsContainer.findViewById(R.id.button2).setOnClickListener(new VisibleToggleClickListener() {

            @Override protected void changeVisibility(boolean visible) {
                TransitionSet set = new TransitionSet().addTransition(new Scale(0.7f))
                                                       .addTransition(new Fade())
                                                       .setInterpolator(
                                                               visible ? new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator());
                TransitionManager.beginDelayedTransition(transitionsContainer, set);
                text2.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
            }
        });

        return view;
    }
}
