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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.zepp.www.transitionsample.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransitionNameSample extends Fragment {
    @Nullable @Override @SuppressLint("DefaultLocale")
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_names, container, false);

        final ViewGroup layout = (ViewGroup) view.findViewById(R.id.transitions_container);
        final Button button = (Button) view.findViewById(R.id.button1);

        final List<String> titles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            titles.add(String.format("Item %d", i + 1));
        }

        createViews(inflater, layout, titles);
        button.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                TransitionManager.beginDelayedTransition(layout, new ChangeBounds());
                Collections.shuffle(titles);
                createViews(inflater, layout, titles);
            }
        });

        return view;
    }

    private static void createViews(LayoutInflater inflater, ViewGroup layout, List<String> titles) {
        layout.removeAllViews();
        for (String title : titles) {
            TextView textView = (TextView) inflater.inflate(R.layout.fragment_names_item, layout, false);
            textView.setText(title);
            textView.setTransitionName(title);
            layout.addView(textView);
        }
    }
}
