package com.zepp.www.stickyrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.View;
import java.util.HashSet;

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

/**
 * StickyHeaderLayoutManager
 * Provides equivalent behavior to a simple LinearLayoutManager, but where section header items
 * are positioned in a "sticky" manner like the section headers in iOS's UITableView.
 * StickyHeaderLayoutManager MUST be used in conjunction with SectioningAdapter.
 *
 * @see SectionAdapter
 */
@SuppressWarnings("unused")
public class StickyHeaderLayoutManager extends RecyclerView.LayoutManager {
    public enum HeaderPosition {
        NONE,
        NATURE,
        STICKY,
        TRAILING,
    }


    /**
     * Callback interface for monitoring when header positions change between members of HeaderPosition enum values.
     * This can be useful if client code wants to change appearance for headers in HeaderPosition.STICKY vs normal positioning.
     *
     * @see HeaderPosition
     */
    public interface HeaderPositionChangedCallback {
        /**
         * Called when a sections header positioning approach changes. The position can be HeaderPosition.NONE, HeaderPosition.NATURAL, HeaderPosition.STICKY or HeaderPosition.TRAILING
         *
         * @param sectionIndex the sections [0...n)
         * @param header       the header view
         * @param oldPosition  the previous positioning of the header (NONE, NATURAL, STICKY or TRAILING)
         * @param newPosition  the new positioning of the header (NATURAL, STICKY or TRAILING)
         */
        void onHeaderPositionChanged(int sectionIndex, View header, HeaderPosition oldPosition, HeaderPosition newPosition);
    }

    // holds all the visible section headers
    HashSet<View> visibleHeaderViews = new HashSet<>();

    // holds the HeaderPosition for each header
    SparseIntArray headerPosition = new SparseIntArray();

    HeaderPositionChangedCallback mHeaderPositionChangedCallback;

    // adapter position of first (lowest-y-value) visible item.
    int mFirstViewAdapterPosition;

    // top of first (lowest-y-value) visible item.
    int mFirstViewTop;

    SectionAdapter mSectionAdapter;

    public HeaderPositionChangedCallback getHeaderPositionChangedCallback() {
        return mHeaderPositionChangedCallback;
    }

    /**
     * Assign callback object to be notified when a header view position changes between states of the HeaderPosition enum
     *
     * @param headerPositionChangedCallback the callback
     * @see HeaderPosition
     */
    public void setHeaderPositionChangedCallback(HeaderPositionChangedCallback headerPositionChangedCallback) {
        this.mHeaderPositionChangedCallback = headerPositionChangedCallback;
    }

    @Override public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        try {
            mSectionAdapter = (SectionAdapter) view.getAdapter();
        }catch (ClassCastException e) {
            throw new RuntimeException("using SectionAdapter or its subclass");
        }
    }

    @Override public void onDetachedFromWindow(
            RecyclerView view, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        mSectionAdapter = null;
        recycler.clear();
    }

    @Override public void onLayoutChildren(
            RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);


    }

    @Override public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return null;
    }
}
