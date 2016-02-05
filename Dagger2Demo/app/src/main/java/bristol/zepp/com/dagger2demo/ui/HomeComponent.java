package bristol.zepp.com.dagger2demo.ui;

import android.app.Activity;
import android.support.v4.app.Fragment;
import bristol.zepp.com.dagger2demo.AbstractActivityComponent;
import bristol.zepp.com.dagger2demo.ActivityModule;
import bristol.zepp.com.dagger2demo.ApplicationComponent;
import bristol.zepp.com.dagger2demo.PerActivity;
import dagger.Component;

/**
 * Created by xubinggui on 2/5/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface HomeComponent extends AbstractActivityComponent{
    void inject(Activity activity);
    void inject(Fragment fragment);
}
