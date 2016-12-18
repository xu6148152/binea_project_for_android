package com.zepp.www.transitionsample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zepp.www.transitionsample.library.ChangeText;

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

public class ChangeTextSample extends Fragment {

    public static final String TEXT_1 = "Hi, i am text. Tap on me!";
    public static final String TEXT_2 = "Thanks! Another tap?";

    @Nullable @Override public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_text, container, false);

        final ViewGroup transitionContainer = (ViewGroup) view.findViewById(R.id.transitions_container);
        final TextView textView = (TextView) view.findViewById(R.id.text1);

        textView.setText(TEXT_1);
        final Transition transition = new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN);

        textView.setOnClickListener(new View.OnClickListener() {
            boolean mSecondText;

            @Override public void onClick(View v) {
                mSecondText = !mSecondText;
                //TransitionManager.beginDelayedTransition(transitionContainer, transition);
                textView.animate().alpha(0f).setListener(new AnimatorListenerAdapter() {
                    @Override public void onAnimationEnd(Animator animation) {
                        textView.setText(mSecondText ? TEXT_2 : TEXT_1);
                        textView.animate().setListener(null);
                        textView.animate().alpha(1f).start();
                    }
                }).start();
                //textView.animate().alpha(1f).start();
            }
        });
        return view;
    }
}
