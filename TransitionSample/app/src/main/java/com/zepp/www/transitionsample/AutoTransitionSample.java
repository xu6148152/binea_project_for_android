package com.zepp.www.transitionsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//  Created by xubinggui on 17/12/2016.
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
public class AutoTransitionSample extends Fragment {

    @Nullable @Override public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_autotransition, container, false);

        final TextView textView = (TextView) view.findViewById(R.id.text);
        final ViewGroup transitionGroup = (ViewGroup) view.findViewById(R.id.transitions_container);
        //transitionGroup.findViewById(R.id.button).setOnClickListener(visible -> {
        //    TransitionManager.beginDelayedTransition(transitionGroup);
        //
        //    textView.setVisibility(visible ? View.VISIBLE : View.GONE);
        //});
        transitionGroup.findViewById(R.id.button).setOnClickListener(new VisibleToggleClickListener() {
             @Override protected void changeVisibility(boolean visible) {
                 TransitionManager.beginDelayedTransition(transitionGroup);

                 textView.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
        return view;
    }
}
