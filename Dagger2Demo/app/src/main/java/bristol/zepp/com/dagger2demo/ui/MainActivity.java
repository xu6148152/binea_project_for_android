package bristol.zepp.com.dagger2demo.ui;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import bristol.zepp.com.dagger2demo.ActivityModule;
import bristol.zepp.com.dagger2demo.DemoApplication;
import bristol.zepp.com.dagger2demo.R;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject LocationManager locationManager;

    private HomeComponent component;

    HomeComponent component() {
        if (component == null) {
            component = DaggerHomeComponent.builder()
                    .applicationComponent(((DemoApplication) getApplication()).component())
                    .activityModule(new ActivityModule(this))
                    .build();
        }
        return component;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new HomeFragment())
                    .commit();
        }
    }
}
