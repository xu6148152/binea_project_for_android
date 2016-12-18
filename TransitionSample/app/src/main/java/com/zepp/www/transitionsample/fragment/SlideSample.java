package com.zepp.www.transitionsample.fragment;

import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;
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

public class SlideSample extends BaseFragment {
    @Override protected Transition createTransition() {
        return new Slide(Gravity.RIGHT);
    }

    @Override protected void handleClick() {
        mTextView.setVisibility(isOrigin ? View.VISIBLE : View.GONE);
    }

    @Override protected int getLayoutId() {
        return R.layout.fragment_slide;
    }
}
