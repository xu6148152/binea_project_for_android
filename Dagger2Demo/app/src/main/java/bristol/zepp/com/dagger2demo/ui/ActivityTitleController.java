package bristol.zepp.com.dagger2demo.ui;

import android.app.Activity;
import bristol.zepp.com.dagger2demo.PerActivity;
import javax.inject.Inject;

@PerActivity public class ActivityTitleController {
    private final Activity activity;

    @Inject public ActivityTitleController(Activity activity) {
        this.activity = activity;
    }

    public void setTitle(CharSequence title) {
        activity.setTitle(title);
    }
}