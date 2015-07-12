package demo.binea.com.mediacodecdemo.filter.BlurFilter;

import android.content.Context;
import demo.binea.com.mediacodecdemo.filter.CameraFilter;
import demo.binea.com.mediacodecdemo.filter.FilterGroup;

public class CameraFilterGaussianBlur extends FilterGroup<CameraFilter> {

    public CameraFilterGaussianBlur(Context context, float blur) {
        super();
        addFilter(new CameraFilterGaussianSingleBlur(context, blur, false));
        addFilter(new CameraFilterGaussianSingleBlur(context, blur, true));
    }
}
