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

import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.zepp.www.transitionsample.R;

public class ImageTransformSample extends BaseFragment {
    @Override protected Transition createTransition() {
        TransitionSet transition = new TransitionSet();
        transition.addTransition(new ChangeBounds()).addTransition(new ChangeImageTransform());
        return transition;
    }

    @Override protected void handleClick() {
        ViewGroup.LayoutParams params = mImageView.getLayoutParams();
        params.height = isOrigin ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT;
        mImageView.setLayoutParams(params);

        mImageView.setScaleType(isOrigin ? ImageView.ScaleType.CENTER_CROP : ImageView.ScaleType.FIT_CENTER);
    }

    @Override protected int getLayoutId() {
        return R.layout.fragment_image_transform;
    }
}
