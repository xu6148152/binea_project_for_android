package bristol.zepp.com.dagger2demo;

import android.app.Application;
import android.location.LocationManager;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Created by xubinggui on 2/5/16.
 */
@Singleton
@Component(modules=DemoApplicationModule.class)
public interface ApplicationComponent {
    void inject(DemoApplication application);

    Application application();
    LocationManager locationManager();
}
