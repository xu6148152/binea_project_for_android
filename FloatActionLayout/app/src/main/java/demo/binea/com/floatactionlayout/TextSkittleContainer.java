package demo.binea.com.floatactionlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by xubinggui on 7/6/15.
 */
public class TextSkittleContainer extends FrameLayout {

    int position;

    public TextSkittleContainer(Context context) {
        super(context);
    }

    public TextSkittleContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextSkittleContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    int getPosition() {
        return position;
    }

    void setPosition(int position) {
        this.position = position;
    }

    Skittle getSkittle() {
        return (Skittle) getChildAt(1);
    }
}
