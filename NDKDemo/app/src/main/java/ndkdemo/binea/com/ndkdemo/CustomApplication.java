package ndkdemo.binea.com.ndkdemo;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

/**
 * Created by xubinggui on 1/26/16.
 */
public class CustomApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        final PackageManager pm = getApplicationContext().getPackageManager();
        ApplicationInfo info;
        try {
            info = pm.getApplicationInfo(this.getPackageName(), 0);
        } catch (final PackageManager.NameNotFoundException e) {
            info = null;
        }

        final String applicationName = (String) (info != null ? pm.getApplicationLabel(info) : "(unknown)");
        Toast.makeText(getApplicationContext(), applicationName, Toast.LENGTH_SHORT).show();
    }
}
