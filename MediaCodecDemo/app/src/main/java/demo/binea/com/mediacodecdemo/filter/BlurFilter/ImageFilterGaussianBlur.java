package demo.binea.com.mediacodecdemo.filter.BlurFilter;

import android.content.Context;
import demo.binea.com.mediacodecdemo.filter.CameraFilter;
import demo.binea.com.mediacodecdemo.filter.FilterGroup;

public class ImageFilterGaussianBlur extends FilterGroup<CameraFilter> {

    public ImageFilterGaussianBlur(Context context, float blur) {
        super();
        addFilter(new ImageFilterGaussianSingleBlur(context, blur, false));
        addFilter(new ImageFilterGaussianSingleBlur(context, blur, true));
    }
}
