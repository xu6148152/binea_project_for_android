package com.zepp.www.transitionsample.fragment;

import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.view.Gravity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
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

public class InterpolatorDurationStartDelaySample extends BaseFragment {

    @Override protected Transition createTransition() {
        Transition transition = new ChangeBounds();
        transition.setDuration(isOrigin ? 700 : 300);
        transition.setInterpolator(isOrigin ? new FastOutSlowInInterpolator() : new AccelerateDecelerateInterpolator());
        transition.setStartDelay(isOrigin ? 0 : 500);
        return transition;
    }

    @Override protected void handleClick() {
        mTransition = createTransition();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mButton.getLayoutParams();
        params.gravity = isOrigin ? (Gravity.RIGHT | Gravity.TOP) : (Gravity.LEFT | Gravity.TOP);
        mButton.setLayoutParams(params);
    }

    @Override protected int getLayoutId() {
        return R.layout.fragment_interpolator;
    }
}
