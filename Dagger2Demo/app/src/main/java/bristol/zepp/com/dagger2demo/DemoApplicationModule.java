package bristol.zepp.com.dagger2demo;

import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by xubinggui on 2/5/16.
 */
@Module public class DemoApplicationModule {
    private final DemoApplication mAppliction;

    public DemoApplicationModule(DemoApplication application) {
        this.mAppliction = application;
    }

    @Provides @Singleton Application application() {
        return mAppliction;
    }

    @Provides @Singleton LocationManager provideLocationManager() {
        return (LocationManager) mAppliction.getSystemService(Context.LOCATION_SERVICE);
    }
}
