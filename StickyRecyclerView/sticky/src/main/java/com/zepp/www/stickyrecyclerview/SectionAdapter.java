package com.zepp.www.stickyrecyclerview;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xubinggui on 5/21/16.
 * //                            _ooOoo_
 * //                           o8888888o
 * //                           88" . "88
 * //                           (| -_- |)
 * //                            O\ = /O
 * //                        ____/`---'\____
 * //                      .   ' \\| |// `.
 * //                       / \\||| : |||// \
 * //                     / _||||| -:- |||||- \
 * //                       | | \\\ - /// | |
 * //                     | \_| ''\---/'' | |
 * //                      \ .-\__ `-` ___/-. /
 * //                   ___`. .' /--.--\ `. . __
 * //                ."" '< `.___\_<|>_/___.' >'"".
 * //               | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * //                 \ \ `-. \_ __\ /__ _/ .-` / /
 * //         ======`-.____`-.___\_____/___.-`____.-'======
 * //                            `=---='
 * //
 * //         .............................................
 * //                  佛祖镇楼                  BUG辟易
 */
@SuppressWarnings("unused") public class SectionAdapter
        extends RecyclerView.Adapter<SectionAdapter.SectionAdapterViewHolder> {

    @IntDef({ TYPE_HEADER, TYPE_GHOST_HEADER, TYPE_ITEM, TYPE_FOOTER })

    @Retention(RetentionPolicy.SOURCE)

    //Declare the ItemType annotation
    public @interface ITEM_TYPE {
    }

    public static final int TYPE_HEADER = 0;

    public static final int TYPE_GHOST_HEADER = 1;

    public static final int TYPE_ITEM = 2;

    public static final int TYPE_FOOTER = 3;

    @Override public SectionAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override public void onBindViewHolder(SectionAdapterViewHolder holder, int position) {

    }

    @Override public int getItemCount() {
        return 0;
    }

    public boolean sectionHasHeader(int sectionIndex) {
        return false;
    }

    public int getAdapterPositionForSectionHeader(int sectionIndex) {
        return 0;
    }

    public int getSectionForAdapterPosition(int mFirstViewAdapterPosition) {
        return 0;
    }

    class SectionAdapterViewHolder extends RecyclerView.ViewHolder {

        public SectionAdapterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
