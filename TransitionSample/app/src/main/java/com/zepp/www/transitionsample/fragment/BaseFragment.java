package com.zepp.www.transitionsample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.zepp.www.transitionsample.R;

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

public abstract class BaseFragment extends Fragment {

    protected Transition mTransition;

    protected boolean isOrigin;

    protected ViewGroup transitionsContainer;

    protected Button mButton;

    protected TextView mTextView1;

    protected TextView mTextView;

    protected ImageView mImageView;


    @Nullable @Override public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);

        transitionsContainer = (ViewGroup) view.findViewById(R.id.transitions_container);
        mButton = (Button) transitionsContainer.findViewById(R.id.button);
        mTextView1 = (TextView) transitionsContainer.findViewById(R.id.text1);
        mTextView = (TextView) transitionsContainer.findViewById(R.id.text);
        mImageView = (ImageView) transitionsContainer.findViewById(R.id.image);

        if (mImageView != null) {
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    isOrigin = !isOrigin;
                    TransitionManager.beginDelayedTransition(transitionsContainer, mTransition);
                    handleClick();
                }
            });
        }
        mTransition = createTransition();

        if (mButton != null) {
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    isOrigin = !isOrigin;
                    TransitionManager.beginDelayedTransition(transitionsContainer, mTransition);
                    handleClick();
                }
            });
        }
        return view;
    }

    protected abstract Transition createTransition();

    protected abstract void handleClick();

    protected abstract int getLayoutId();

    //protected Transition getTransition() {
    //    return mTransition;
    //}
}
