package demo.binea.com.supportmaterialdesign;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by xubinggui on 6/8/15.
 */
public class AppContext extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Fresco.initialize(this);
	}
}
