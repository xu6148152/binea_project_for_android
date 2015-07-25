package demo.binea.com.fitchart.widget;

import android.graphics.Path;

/**
 * Created by xubinggui on 7/24/15.
 */
public interface Render {
    Path buildPath(float animationProgress, float animationSeek);
}
