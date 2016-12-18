package com.zepp.www.transitionsample.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zepp.www.transitionsample.R;
import com.zepp.www.transitionsample.library.TransitionListenerAdapter;

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

public class ExplodeAndEpicenterExample extends Fragment {


    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecyclerView = new RecyclerView(container.getContext());
        mRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                 ViewGroup.LayoutParams.MATCH_PARENT));
        mRecyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 4));
        mRecyclerView.setAdapter(new Adapter());
        return mRecyclerView;
    }

    private void letsExplodeIt(View clickedView) {
        // save rect of view in screen coordinated
        final Rect viewRect = new Rect();
        clickedView.getGlobalVisibleRect(viewRect);

        //TransitionSet set = new TransitionSet()
        //        .addTransition(new Explode().setEpicenterCallback(new Transition.EpicenterCallback() {
        //            @Override
        //            public Rect onGetEpicenter(Transition transition) {
        //                return viewRect;
        //            }
        //        }).excludeTarget(clickedView, true))
        //        .addTransition(new Fade().addTarget(clickedView))
        //        .addListener(new Transition.TransitionListenerAdapter() {
        //            @Override
        //            public void onTransitionEnd(Transition transition) {
        //                getActivity().onBackPressed();
        //            }
        //        });

        TransitionSet set = new TransitionSet();
        set.addTransition(new Explode()).setEpicenterCallback(new Transition.EpicenterCallback() {
            @Override public Rect onGetEpicenter(Transition transition) {
                return viewRect;
            }
        });
        set.addTransition(new Fade()).addListener(new TransitionListenerAdapter() {
            @Override public void onTransitionEnd(Transition transition) {
                getActivity().onBackPressed();
            }
        });

        TransitionManager.beginDelayedTransition(mRecyclerView, set);

        // remove all views from Recycler View
        mRecyclerView.setAdapter(null);
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(
                    LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.explode_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View clickedView) {
                    letsExplodeIt(clickedView);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 32;
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
        }

    }
}
