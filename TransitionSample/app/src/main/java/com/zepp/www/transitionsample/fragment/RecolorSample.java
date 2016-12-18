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

import android.graphics.drawable.ColorDrawable;
import android.transition.Transition;
import com.zepp.www.transitionsample.R;
import com.zepp.www.transitionsample.library.Recolor;

public class RecolorSample extends BaseFragment {
    @Override protected Transition createTransition() {
        return new Recolor();
    }

    @Override protected void handleClick() {
        mButton.setTextColor(getResources().getColor(!isOrigin ? R.color.second_accent : R.color.colorAccent));
        mButton.setBackgroundDrawable(new ColorDrawable(getResources().getColor(!isOrigin ? R.color.colorAccent : R.color.second_accent)));
    }

    @Override protected int getLayoutId() {
        return R.layout.fragment_recolor;
    }
}
