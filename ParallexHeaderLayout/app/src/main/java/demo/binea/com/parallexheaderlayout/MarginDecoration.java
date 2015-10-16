package demo.binea.com.parallexheaderlayout;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by xubinggui on 10/16/15.
 */
public class MarginDecoration extends RecyclerView.ItemDecoration {

    private int letMargin;

    public MarginDecoration(Context context){
        letMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, context.getResources().getDisplayMetrics());
    }

    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
            RecyclerView.State state) {
        outRect.set(20, 20, 20, 20);
    }
}
