package bristol.zepp.com.dagger2demo;

import android.app.Activity;
import dagger.Component;

/**
 * Created by xubinggui on 2/5/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface AbstractActivityComponent {
    Activity activity();
}
