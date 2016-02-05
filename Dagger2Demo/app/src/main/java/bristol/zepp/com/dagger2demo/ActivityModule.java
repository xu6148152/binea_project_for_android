package bristol.zepp.com.dagger2demo;

import android.app.Activity;
import dagger.Module;
import dagger.Provides;

/**
 * Created by xubinggui on 2/5/16.
 */
@Module public class ActivityModule {
    private Activity activity;

    public ActivityModule(Activity mActivity) {
        this.activity = mActivity;
    }

    @Provides @PerActivity Activity activity() {
        return activity;
    }
}
