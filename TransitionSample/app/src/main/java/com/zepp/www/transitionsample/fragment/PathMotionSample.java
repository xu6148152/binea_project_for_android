package com.zepp.www.transitionsample.fragment;

import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.view.Gravity;
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

public class PathMotionSample extends BaseFragment {

    @Override protected int getLayoutId() {
        return R.layout.fragment_path;
    }

    @Override protected Transition createTransition() {
        final Transition transition = new ChangeBounds();
        transition.setPathMotion(new ArcMotion());
        transition.setDuration(500);
        return transition;
    }

    @Override protected void handleClick() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mButton.getLayoutParams();
        params.gravity = isOrigin ? (Gravity.RIGHT | Gravity.BOTTOM) : (Gravity.LEFT | Gravity.TOP);
        mButton.setLayoutParams(params);
    }
}
