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

import android.transition.Transition;
import com.zepp.www.transitionsample.R;
import com.zepp.www.transitionsample.library.Rotate;

public class RotateSample extends BaseFragment {
    @Override protected Transition createTransition() {
        return new Rotate();
    }

    @Override protected void handleClick() {
        mImageView.setRotation(isOrigin ? 135 : 0);
    }

    @Override protected int getLayoutId() {
        return R.layout.fragment_rotate;
    }
}
