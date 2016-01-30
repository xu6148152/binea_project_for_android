package bristol.zepp.com.databindingdemo;

import android.databinding.BindingConversion;
import android.graphics.drawable.ColorDrawable;

/**
 * Created by xubinggui on 1/30/16.
 */
public class DataBindingHelper {

    @BindingConversion public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);
    }
}
