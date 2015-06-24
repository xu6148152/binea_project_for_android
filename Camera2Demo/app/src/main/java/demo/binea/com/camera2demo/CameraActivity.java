package demo.binea.com.camera2demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class CameraActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		if (null == savedInstanceState) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, Camera2BasicFragment.newInstance())
					.commit();
		}

	}
}
