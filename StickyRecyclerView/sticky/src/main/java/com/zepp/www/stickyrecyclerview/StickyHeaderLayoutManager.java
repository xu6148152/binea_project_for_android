package com.zepp.www.stickyrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashSet;
import java.util.Set;

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
@SuppressWarnings("unused") public class StickyHeaderLayoutManager
        extends RecyclerView.LayoutManager {
    public enum HeaderPosition {
        NONE,
        NATURAL,
        STICKY,
        TRAILING;

        public static HeaderPosition toHeaderPosition(int value) {
            switch (value) {
                case 0:
                    return NONE;
                case 1:
                    return NATURAL;
                case 2:
                    return STICKY;
                default:
                    return TRAILING;
            }
        }

        public int toInt() {
            switch (this) {
                case NONE:
                    return 0;
                case NATURAL:
                    return 1;
                case STICKY:
                    return 2;
                default:
                    return 3;
            }
        }
    }

    /**
     * Callback interface for monitoring when header positions change between members of
     * HeaderPosition enum values.
     * This can be useful if client code wants to change appearance for headers in
     * HeaderPosition.STICKY vs normal positioning.
     *
     * @see HeaderPosition
     */
    public interface HeaderPositionChangedCallback {
        /**
         * Called when a sections header positioning approach changes. The position can be
         * HeaderPosition.NONE, HeaderPosition.NATURAL, HeaderPosition.STICKY or
         * HeaderPosition.TRAILING
         *
         * @param sectionIndex the sections [0...n)
         * @param header the header view
         * @param oldPosition the previous positioning of the header (NONE, NATURAL, STICKY or
         * TRAILING)
         * @param newPosition the new positioning of the header (NATURAL, STICKY or TRAILING)
         */
        void onHeaderPositionChanged(
                int sectionIndex, View header, HeaderPosition oldPosition,
                HeaderPosition newPosition);
    }

    // holds all the visible section headers
    HashSet<View> mVisibleHeaderViews = new HashSet<>();

    // holds the HeaderPosition for each header
    SparseIntArray mHeaderPosition = new SparseIntArray();

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
     * Assign callback object to be notified when a header view position changes between states of
     * the HeaderPosition enum
     *
     * @param headerPositionChangedCallback the callback
     * @see HeaderPosition
     */
    public void setHeaderPositionChangedCallback(
            HeaderPositionChangedCallback headerPositionChangedCallback) {
        this.mHeaderPositionChangedCallback = headerPositionChangedCallback;
    }

    @Override public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        try {
            mSectionAdapter = (SectionAdapter) view.getAdapter();
        } catch (ClassCastException e) {
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
        updateFirstAdapterPosition();
        int top = mFirstViewTop;

        //RESET
        mVisibleHeaderViews.clear();
        mHeaderPosition.clear();
        detachAndScrapAttachedViews(recycler);

        int height;
        int left = getPaddingLeft();
        int right = getWidth() - getPaddingRight();
        int parentBottom = getHeight() - getPaddingBottom();

        // walk through adapter starting at firstViewAdapterPosition stacking each vended item
        for (int adapterPosition = mFirstViewAdapterPosition;
             adapterPosition < state.getItemCount(); adapterPosition++) {
            View v = recycler.getViewForPosition(adapterPosition);
            addView(v);
            measureChildWithMargins(v, 0, 0);

            int itemViewType = getItemViewType(v);
            if (itemViewType == SectionAdapter.TYPE_HEADER) {
                mVisibleHeaderViews.add(v);

                //use the header's height
                height = getDecoratedMeasuredHeight(v);
                layoutDecorated(v, left, top, right, top + height);

                //we need to vend the ghost header and position/size it as same as the actual header
                adapterPosition++;
                View ghostHeader = recycler.getViewForPosition(adapterPosition);
                addView(ghostHeader);
                layoutDecorated(ghostHeader, left, top, right, top + height);
            } else if (itemViewType == SectionAdapter.TYPE_GHOST_HEADER) {
                // we need to back up and get the header for this ghostHeader
                View headerView = recycler.getViewForPosition(adapterPosition - 1);
                mVisibleHeaderViews.add(headerView);
                addView(headerView);
                measureChildWithMargins(headerView, 0, 0);
                height = getDecoratedMeasuredHeight(headerView);
                layoutDecorated(headerView, left, top, right, top + height);
                layoutDecorated(v, left, top, right, top + height);
            } else {
                height = getDecoratedMeasuredHeight(v);
                layoutDecorated(v, left, top, right, top + height);
            }

            top += height;

            // if the item we just laid out falls off the bottom of the view, we're done
            if (v.getBottom() >= parentBottom) {
                break;
            }
        }
        updateHeaderPosition(recycler);
    }

    /**
     * Get the header item for a given section, creating it if it's not already in the view
     * hierarchy
     *
     * @param recycler the recycler
     * @param sectionIndex the index of the section for in question
     * @return the header, or null if the adapter specifies no header for the section
     */
    View createSectionHeaderIfNeeded(RecyclerView.Recycler recycler, int sectionIndex) {
        if (mSectionAdapter.sectionHasHeader(sectionIndex)) {
            // first, see if we've already got a header for this section
            for (int i = 0, n = getChildCount(); i < n; i++) {
                View view = getChildAt(i);
                if (getItemViewType(view) == SectionAdapter.TYPE_HEADER
                        && getViewSectionIndex(view) == sectionIndex) {
                    return view;
                }
            }

            //need to create one
            int headerPosition = mSectionAdapter.getAdapterPositionForSectionHeader(sectionIndex);
            View sectionHeader = recycler.getViewForPosition(headerPosition);
            mVisibleHeaderViews.add(sectionHeader);
            addView(sectionHeader);
            measureChildWithMargins(sectionHeader, 0, 0);
        }
        return null;
    }

    @Override public int scrollVerticallyBy(
            int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() == 0 || dy == 0) {
            return 0;
        }

        int scrolled = 0;
        int left = getPaddingLeft();
        int right = getWidth() - getPaddingRight();

        if (dy < 0) {
            //content moving downwards
            View topView = getMostTopChildView();

            while (scrolled > dy) {
                //get most top view
                int hangingTop = Math.max(-getDecoratedTop(topView), 0);
                // scrollBy is positive, causing content to move downwards
                int scrollBy = Math.min(scrolled - dy, hangingTop);
                scrolled -= scrollBy;
                offsetChildrenVertical(scrollBy);

                //vend next view above topView
                if (mFirstViewAdapterPosition > 0 && scrolled > dy) {
                    mFirstViewAdapterPosition--;
                    // we're skipping headers. they should already be vended, but if we're vending a ghostHeader
                    // here an actual header will be vended if needed for measurement
                    int itemType = mSectionAdapter.getItemViewType(mFirstViewAdapterPosition);
                    boolean isHeader = itemType == SectionAdapter.TYPE_HEADER;

                    //skip this header, move to item above
                    if (isHeader) {
                        mFirstViewAdapterPosition--;
                        if (mFirstViewAdapterPosition < 0) {
                            break;
                        }
                    }

                    View v = recycler.getViewForPosition(mFirstViewAdapterPosition);
                    addView(v, 0);

                    int bottom = getDecoratedTop(v);
                    int top = 0;
                    boolean isGhostHeader = itemType == SectionAdapter.TYPE_GHOST_HEADER;
                    if (isGhostHeader) {
                        View header = createSectionHeaderIfNeeded(recycler,
                                                                  mSectionAdapter.getSectionForAdapterPosition(
                                                                          mFirstViewAdapterPosition));
                        top = bottom - getDecoratedMeasuredHeight(
                                header);//header is already measured
                    } else {
                        measureChildWithMargins(v, 0, 0);
                        top = bottom - getDecoratedMeasuredHeight(v);
                    }

                    layoutDecorated(v, left, top, right, bottom);
                    topView = v;
                } else {
                    break;
                }
            }
        } else {
            // content moving up, we're headed to bottom of list
            int parentHeight = getHeight();
            View bottomView = getMostBottomChildView();

            while (scrolled < dy) {
                int hangingBottom = Math.max(getDecoratedBottom(bottomView) - parentHeight, 0);
                int scrollBy = -Math.min(dy - scrolled, hangingBottom);
                scrolled -= scrollBy;
                offsetChildrenVertical(scrollBy);

                int adapterPosition = getViewAdapterPosition(bottomView);
                int nextAdapterPosition = adapterPosition + 1;

                if (scrolled < dy && nextAdapterPosition < state.getItemCount()) {
                    int top = getDecoratedBottom(bottomView);

                    int itemType = mSectionAdapter.getItemViewType(nextAdapterPosition);
                    if (itemType == SectionAdapter.TYPE_HEADER) {
                        // get the header and measure it so we can followup immediately
                        // by vending the ghost header
                        View headerView = createSectionHeaderIfNeeded(recycler,
                                                                      mSectionAdapter.getSectionForAdapterPosition(
                                                                              nextAdapterPosition));
                        int height = getDecoratedMeasuredHeight(headerView);
                        layoutDecorated(headerView, left, 0, right, height);

                        // but we need to vend the followup ghost header too
                        nextAdapterPosition++;
                        View ghostHeader = recycler.getViewForPosition(nextAdapterPosition);
                        addView(ghostHeader);
                        layoutDecorated(ghostHeader, left, top, right, top + height);
                        bottomView = ghostHeader;
                    } else if (itemType == SectionAdapter.TYPE_GHOST_HEADER) {
                        // get the header and measure it so we can followup immediately
                        // by vending the ghost header
                        View headerView = createSectionHeaderIfNeeded(recycler,
                                                                      mSectionAdapter.getSectionForAdapterPosition(
                                                                              nextAdapterPosition));
                        int height = getDecoratedMeasuredHeight(headerView);
                        layoutDecorated(headerView, left, 0, right, height);

                        // but we need to vend the followup ghost header too
                        View ghostHeader = recycler.getViewForPosition(nextAdapterPosition);
                        addView(ghostHeader);
                        layoutDecorated(ghostHeader, left, top, right, top + height);
                        bottomView = ghostHeader;
                    } else {
                        View v = recycler.getViewForPosition(nextAdapterPosition);
                        addView(v);

                        measureChildWithMargins(v, 0, 0);
                        int height = getDecoratedMeasuredHeight(v);
                        layoutDecorated(v, left, top, right, top + height);
                        bottomView = v;
                    }
                } else {
                    break;
                }
            }
        }

        View mostTopView = getMostTopChildView();
        if (mostTopView != null) {
            mFirstViewTop = getDecoratedTop(mostTopView);
        }
        updateHeaderPosition(recycler);
        recycleViewsOutOfBounds(recycler);
        return scrolled;
    }

    @Override public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                             ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override public boolean canScrollVertically() {
        return true;
    }

    private void recycleViewsOutOfBounds(RecyclerView.Recycler recycler) {
        int height = getHeight();
        int childrenNum = getChildCount();
        Set<Integer> remainSections = new HashSet<>();
        Set<View> viewsToRecycle = new HashSet<>();

        //recycle view but header
        for (int i = 0; i < childrenNum; i++) {
            View view = getChildAt(i);
            if (getItemViewType(view) != SectionAdapter.TYPE_HEADER) {
                if (getDecoratedBottom(view) < 0 || getDecoratedTop(view) > height) {
                    viewsToRecycle.add(view);
                } else {
                    remainSections.add(getViewSectionIndex(view));
                }
            } else {
                // second pass, for each "orphaned" header (a header who's section is completely recycled)
                // we remove it if it's gone offscreen
                int sectionIndex = getViewSectionIndex(view);
                if (!remainSections.contains(sectionIndex)) {
                    float translationY = view.getTranslationY();
                    if ((getDecoratedBottom(view) + translationY) < 0
                            || (getDecoratedTop(view) + translationY) > 0) {
                        viewsToRecycle.add(view);
                        mVisibleHeaderViews.remove(view);
                        mHeaderPosition.removeAt(sectionIndex);
                    }
                }
            }
        }

        //recycler
        for (View view : viewsToRecycle) {
            removeAndRecycleView(view, recycler);
        }

        // determine the adapter adapterPosition of first visible item
        updateFirstAdapterPosition();
    }

    private View getMostBottomChildView() {
        if (getChildCount() == 0) {
            return null;
        }

        View mostBottomView = null;
        int bottom = Integer.MIN_VALUE;
        for (int i = 0, e = getChildCount(); i < e; i++) {
            View view = getChildAt(i);

            if (getViewAdapterPosition(view) == RecyclerView.NO_POSITION
                    || getItemViewType(view) == SectionAdapter.TYPE_HEADER) {
                continue;
            }

            int b = getDecoratedBottom(view);
            if (b > bottom) {
                bottom = b;
                mostBottomView = view;
            }
        }
        return mostBottomView;
    }

    private View getMostTopChildView() {
        if (getChildCount() == 0) {
            return null;
        }

        // note: We can't use child view order because we muck with moving things to front
        View mostTopView = null;
        int top = Integer.MAX_VALUE;
        for (int i = 0, e = getChildCount(); i < e; i++) {
            View view = getChildAt(i);

            //ignore views which are removing
            // ignore headers
            if (getViewAdapterPosition(view) == RecyclerView.NO_POSITION
                    || getItemViewType(view) == SectionAdapter.TYPE_HEADER) {
                continue;
            }

            int t = getDecoratedTop(view);
            if (t < top) {
                top = t;
                mostTopView = view;
            }
        }
        return mostTopView;
    }

    private void updateHeaderPosition(RecyclerView.Recycler recycler) {
        // first, for each section represented by the current list of items,
        // ensure that the header for that section is extant
        Set<Integer> visitedSections = new HashSet<>();
        for (int i = 0, n = getChildCount(); i < n; i++) {
            View view = getChildAt(i);
            int sectionIndex = getViewSectionIndex(view);
            if (visitedSections.add(sectionIndex)) {
                createSectionHeaderIfNeeded(recycler, sectionIndex);
            }
        }

        //header is always at top
        int left = getPaddingLeft();
        int right = getWidth() - getPaddingRight();
        for (View headerView : mVisibleHeaderViews) {
            int sectionIndex = getViewSectionIndex(headerView);

            //find first and last non-header views in this section
            View ghostHeader = null;
            View firstViewInNextSection = null;
            for (int i = 0, e = getChildCount(); i < e; i++) {
                View view = getChildAt(i);
                int itemType = getItemViewType(view);
                if (itemType == SectionAdapter.TYPE_HEADER) {
                    continue;
                }

                int viewSectionIndex = getViewSectionIndex(view);
                if (sectionIndex == viewSectionIndex) {
                    if (itemType == SectionAdapter.TYPE_GHOST_HEADER) {
                        ghostHeader = view;
                    }
                } else if (viewSectionIndex == sectionIndex + 1) {
                    if (firstViewInNextSection == null) {
                        firstViewInNextSection = view;
                    }
                }
            }

            int height = getDecoratedMeasuredHeight(headerView);
            int top = getPaddingTop();
            //init position mark
            HeaderPosition headerPosition = HeaderPosition.STICKY;
            if (ghostHeader != null) {
                int ghostHeaderTop = getDecoratedTop(ghostHeader);
                if (ghostHeaderTop >= top) {
                    top = ghostHeaderTop;
                    headerPosition = HeaderPosition.NATURAL;
                }
            }

            if (firstViewInNextSection != null) {
                int nextViewTop = getDecoratedTop(firstViewInNextSection);
                if (nextViewTop - height < top) {
                    top = nextViewTop - height;
                    headerPosition = HeaderPosition.TRAILING;
                }
            }

            // now bring header to front of stack for overlap, and position it
            headerView.bringToFront();
            layoutDecorated(headerView, left, top, right, top + height);

            // notify adapter of positioning for this header
            recordHeaderPositionAndNotify(sectionIndex, headerView, headerPosition);
        }
    }

    private void recordHeaderPositionAndNotify(
            int sectionIndex, View headerView, HeaderPosition newHeaderPosition) {
        if (mHeaderPosition.indexOfKey(sectionIndex) != -1) {
            int position = mHeaderPosition.get(sectionIndex);
            HeaderPosition currentHeaderPosition = HeaderPosition.toHeaderPosition(position);
            if (currentHeaderPosition != newHeaderPosition) {
                mHeaderPosition.append(sectionIndex, currentHeaderPosition.toInt());
                if (mHeaderPositionChangedCallback != null) {
                    mHeaderPositionChangedCallback.onHeaderPositionChanged(sectionIndex, headerView,
                                                                           currentHeaderPosition,
                                                                           newHeaderPosition);
                }
            }
        } else {
            mHeaderPosition.put(sectionIndex, newHeaderPosition.toInt());
            if (mHeaderPositionChangedCallback != null) {
                mHeaderPositionChangedCallback.onHeaderPositionChanged(sectionIndex, headerView,
                                                                       HeaderPosition.NONE,
                                                                       newHeaderPosition);
            }
        }
    }

    /**
     * Updates firstViewAdapterPosition to the adapter position  of the highest item in the list -
     * e.g., the
     * adapter position of the item with lowest y value in the list
     *
     * @return the y value of the topmost view in the layout, or paddingTop if empty
     */
    private int updateFirstAdapterPosition() {
        //empty
        if (getChildCount() == 0) {
            mFirstViewAdapterPosition = 0;
            mFirstViewTop = getPaddingTop();
            return mFirstViewTop;
        }

        View mostTopView = getMostTopChildView();
        if (mostTopView != null) {
            mFirstViewAdapterPosition = getViewAdapterPosition(mostTopView);
            mFirstViewTop = Math.min(mostTopView.getTop(), getPaddingTop());
        }

        // as far as I can tell, if notifyDataSetChanged is called, onLayoutChildren
        // will be called, but all views will be marked as having NO_POSITION for
        // adapterPosition, which means the above approach of finding the topmostChildView
        // doesn't work. So, basically, leave firstViewAdapterPosition and firstViewTop
        // alone.
        return mFirstViewTop;
    }

    int getViewType(View view) {
        int adapterPosition = getViewAdapterPosition(view);
        return mSectionAdapter.getItemViewType(adapterPosition);
    }

    int getViewSectionIndex(View view) {
        int adapterPosition = getViewAdapterPosition(view);
        return mSectionAdapter.getSectionForAdapterPosition(adapterPosition);
    }

    SectionAdapter.ViewHolder getViewViewHolder(View view) {
        return (SectionAdapter.ViewHolder) view.getTag(
                R.id.sectioning_adapter_tag_key_view_viewholder);
    }

    int getViewAdapterPosition(View view) {
        return getViewViewHolder(view).getAdapterPosition();
    }
}
