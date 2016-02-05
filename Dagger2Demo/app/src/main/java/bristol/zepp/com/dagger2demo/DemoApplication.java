package bristol.zepp.com.dagger2demo;

import android.app.Application;

/**
 * Created by xubinggui on 2/5/16.
 */
public class DemoApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .demoApplicationModule(new DemoApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent component() {
        return applicationComponent;
    }
}
